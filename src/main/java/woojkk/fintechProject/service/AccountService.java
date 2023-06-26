package woojkk.fintechProject.service;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woojkk.fintechProject.domain.Account;
import woojkk.fintechProject.domain.AccountUser;
import woojkk.fintechProject.exception.AccountException;
import woojkk.fintechProject.exception.ErrorCode;
import woojkk.fintechProject.repository.AccountRepository;
import woojkk.fintechProject.repository.AccountUserRepository;
import woojkk.fintechProject.type.AccountStatus;
import woojkk.fintechProject.type.AccountType;
import woojkk.fintechProject.type.Bank;

@Service
@RequiredArgsConstructor
public class AccountService {

  private static final int ACCOUNT_NUMBER_LENGTH = 10;

  private final AccountUserRepository accountUserRepository;
  private final AccountRepository accountRepository;

  @Transactional
  public Account createAccount(Long userId, Long initialBalance, String accountPassword,
      Bank bank, AccountType accountType, Long setLimit) {
    AccountUser accountUser = accountUserRepository.findById(userId)
        .orElseThrow(() -> new AccountException(ErrorCode.LOGIN_CHECK_FAIL));

    validateCreateAccount(accountUser, accountType, bank);

    String newAccountNumber;

    do {
      newAccountNumber = getRandomAccountNumber();
    } while (accountRepository.existsByAccountNumber(newAccountNumber));

    return accountRepository.save(Account.builder()
        .accountUser(accountUser)
        .accountPassword(accountPassword)
        .bank(bank)
        .accountNumber(newAccountNumber)
        .balance(initialBalance)
        .setLimit(setLimit)
        .accountType(accountType)
        .accountStatus(AccountStatus.IN_USE)
        .registeredAt(LocalDateTime.now())
        .build());
  }

  private String getRandomAccountNumber() {
    String uuid = UUID.randomUUID().toString().replaceAll("-", "");

    String onlyNumber = uuid.replaceAll("[^0-9]", "");

    return onlyNumber.substring(0, ACCOUNT_NUMBER_LENGTH);
  }

  private void validateCreateAccount(AccountUser accountUser, AccountType accountType, Bank bank) {
    Integer accountTypeCount =
        accountRepository.countByAccountUserAndAccountTypeAndBank(accountUser, accountType, bank);

    if (accountTypeCount >= 5) {
      throw new AccountException(ErrorCode.MAX_COUNT_PER_USER);
    }
  }

}
