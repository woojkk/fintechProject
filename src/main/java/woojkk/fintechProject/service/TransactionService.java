package woojkk.fintechProject.service;


import static woojkk.fintechProject.type.TransactionType.DEPOSIT;
import static woojkk.fintechProject.type.TransactionType.WITHDRAW;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woojkk.fintechProject.config.JwtAuthenticationProvider;
import woojkk.fintechProject.domain.Account;
import woojkk.fintechProject.domain.Transaction;
import woojkk.fintechProject.domain.UserVo;
import woojkk.fintechProject.exception.AccountException;
import woojkk.fintechProject.exception.ErrorCode;
import woojkk.fintechProject.repository.AccountRepository;
import woojkk.fintechProject.type.AccountStatus;
import woojkk.fintechProject.type.TransactionResultType;
import woojkk.fintechProject.type.TransactionType;

@Service
@RequiredArgsConstructor
public class TransactionService {

  private final AccountRepository accountRepository;
  private final JwtAuthenticationProvider provider;

  public void validateToken(Long accountId, String password, TransactionType transactionType,
      Long amount, String token) {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_ACCOUNT));

    UserVo vo = provider.getUserVo(token);

    if (!Objects.equals(account.getAccountUser().getId(), vo.getId())) {
      throw new AccountException(ErrorCode.UNAUTHORIZED_TOKEN);
    }

    if (account.getAccountStatus() == AccountStatus.UNREGISTERED) {
      throw new AccountException(ErrorCode.ALREADY_UNREGISTERED_ACCOUNT);
    }

    if (!account.getAccountPassword().equals(password)) {
      throw new AccountException(ErrorCode.NOT_MATCHED_PASSWORD);
    }

    if (transactionType != WITHDRAW) {
      throw new AccountException(ErrorCode.NOT_MATCHED_TRANSACTION_TYPE);
    }

    if (account.getBalance() < amount) {
      throw new AccountException(ErrorCode.WITHDRAW_FAIL_INSUFFICIENT_BALANCE);
    }

  }

  @Transactional
  public Transaction drawMoney(Long accountId, String password, TransactionType transactionType,
      Long amount, String token) {

    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_ACCOUNT));

    validateToken(accountId, password, transactionType, amount, token);

    if (account.getBalance() < amount) {
      throw new AccountException(ErrorCode.WITHDRAW_FAIL_INSUFFICIENT_BALANCE);
    }

    account.setBalance(account.getBalance() - amount);

    return Transaction.builder()
        .accountNumber(account.getAccountNumber())
        .account(account)
        .transactionType(WITHDRAW)
        .balance(account.getBalance())
        .amount(amount)
        .transactionResultType(TransactionResultType.SUCCESS)
        .transactedAt(LocalDateTime.now())
        .build();

  }

  @Transactional
  public Transaction depositMoney(Long accountId, String password, TransactionType transactionType,
      Long amount, String token) {

    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_ACCOUNT));

    validateToken(accountId, password, transactionType, amount, token);

    if (account.getBalance() + amount > account.getSetLimit()) {
      throw new AccountException(ErrorCode.ACCOUNT_LIMIT_EXCEEDED);
    }

    account.setBalance(account.getBalance() + amount);

    return Transaction.builder()
        .accountNumber(account.getAccountNumber())
        .account(account)
        .transactionType(DEPOSIT)
        .balance(account.getBalance())
        .amount(amount)
        .transactionResultType(TransactionResultType.SUCCESS)
        .transactedAt(LocalDateTime.now())
        .build();
  }
}
