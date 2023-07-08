package woojkk.fintechProject.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
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
        .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_USER));



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

    Integer unregisteredAccountCount = accountRepository.countByAccountUserAndAccountTypeAndBankAndAccountStatus(
        accountUser, accountType, bank, AccountStatus.UNREGISTERED);

    accountTypeCount -= unregisteredAccountCount;


    if (accountTypeCount >= 5) {
      throw new AccountException(ErrorCode.MAX_COUNT_PER_USER);
    }
  }

  @Transactional
  public Account deleteAccount(Long userId, String accountNumber, String accountPassword,
      Bank bank, AccountType accountType) {

    AccountUser accountUser = accountUserRepository.findById(userId)
        .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_USER));

    Account account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_ACCOUNT));


    if (!checkPassword(account, accountPassword)) {
      throw new AccountException(ErrorCode.NOT_MATCHED_PASSWORD);
    }

    if (!Objects.equals(accountUser.getId(), account.getAccountUser().getId())) {
      throw new AccountException(ErrorCode.ACCOUNT_USER_UNMATCHED);
    }

    if (account.getAccountStatus() == AccountStatus.UNREGISTERED) {
      throw new AccountException(ErrorCode.ALREADY_UNREGISTERED_ACCOUNT);
    }

    if (account.getBalance() > 0) {
      throw new AccountException(ErrorCode.BALANCE_NOT_EMPTY);
    }

    if (account.getBank() != bank || account.getAccountType() != accountType) {
      throw new AccountException(ErrorCode.NOT_FOUND_ACCOUNT);
    }

    account.setAccountStatus(AccountStatus.UNREGISTERED);
    account.setUnRegisteredAt(LocalDateTime.now());

    return account;
  }

  private boolean checkPassword(Account account, String password) {
    return account.getAccountPassword().equals(password);
  }

  public List<Account> getAccountsByUserId(Long userId) {
    AccountUser accountUser = accountUserRepository.findById(userId)
        .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_USER));

    return accountRepository.findByAccountUser(accountUser);
  }

  public List<Account> getAccountsByUserIdAndBank(Long userId, Bank bank) {
    AccountUser accountUser = accountUserRepository.findById(userId)
        .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_USER));

    return accountRepository.findByAccountUserAndBank(accountUser, bank)
        .stream().filter(account -> account.getBank() == bank)
        .collect(Collectors.toList());
  }
}
