package woojkk.fintechProject.service;


import static woojkk.fintechProject.type.TransactionType.DEPOSIT;
import static woojkk.fintechProject.type.TransactionType.REMITTANCE;
import static woojkk.fintechProject.type.TransactionType.WITHDRAW;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woojkk.fintechProject.config.JwtAuthenticationProvider;
import woojkk.fintechProject.domain.Account;
import woojkk.fintechProject.domain.Transaction;
import woojkk.fintechProject.exception.AccountException;
import woojkk.fintechProject.exception.ErrorCode;
import woojkk.fintechProject.repository.AccountRepository;
import woojkk.fintechProject.repository.TransactionRepository;
import woojkk.fintechProject.type.AccountStatus;
import woojkk.fintechProject.type.Bank;
import woojkk.fintechProject.type.TransactionResultType;
import woojkk.fintechProject.type.TransactionType;

@Service
@RequiredArgsConstructor
public class TransactionService {

  private final AccountRepository accountRepository;
  private final TransactionRepository transactionRepository;
  private final JwtAuthenticationProvider provider;

  public void validateToken(Account account, String password, String token) {

    if (!provider.validateToken(token)) {
      throw new AccountException(ErrorCode.UNAUTHORIZED_TOKEN);
    }

    if (!account.getAccountPassword().equals(password)) {
      throw new AccountException(ErrorCode.NOT_MATCHED_PASSWORD);
    }

    if (account.getAccountStatus() == AccountStatus.UNREGISTERED) {
      throw new AccountException(ErrorCode.ALREADY_UNREGISTERED_ACCOUNT);
    }
  }

  @Transactional
  public Transaction drawMoney(Long accountId, String password, TransactionType transactionType,
      Long amount, String token) {

    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_ACCOUNT));

    validateToken(account, password, token);

    if (transactionType != WITHDRAW) {
      throw new AccountException(ErrorCode.NOT_MATCHED_TRANSACTION_TYPE);
    }

    if (account.getBalance() < amount) {
      throw new AccountException(ErrorCode.INSUFFICIENT_BALANCE);
    }

    account.setBalance(account.getBalance() - amount);

    return transactionRepository.save(Transaction.builder()
        .account(account)
        .transactionType(WITHDRAW)
        .balance(account.getBalance())
        .amount(amount)
        .transactionResultType(TransactionResultType.SUCCESS)
        .transactedAt(LocalDateTime.now())
        .build());
  }

  @Transactional
  public Transaction depositMoney(Long accountId, String password, TransactionType transactionType,
      Long amount, String token) {

    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_ACCOUNT));

    validateToken(account, password, token);

    if (transactionType != DEPOSIT) {
      throw new AccountException(ErrorCode.NOT_MATCHED_TRANSACTION_TYPE);
    }

    if (account.getBalance() + amount > account.getSetLimit()) {
      throw new AccountException(ErrorCode.ACCOUNT_LIMIT_EXCEEDED);
    }

    account.setBalance(account.getBalance() + amount);

    return transactionRepository.save(Transaction.builder()
        .account(account)
        .transactionType(DEPOSIT)
        .balance(account.getBalance())
        .amount(amount)
        .transactionResultType(TransactionResultType.SUCCESS)
        .transactedAt(LocalDateTime.now())
        .build());
  }

  @Transactional
  public Transaction remittanceMoney(Long accountId, String password,
      TransactionType transactionType, Bank receiverBank,
      String receiverAccountNumber, Long amount, String token) {

    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_ACCOUNT));

    validateToken(account, password, token);

    if (transactionType != REMITTANCE) {
      throw new AccountException(ErrorCode.NOT_MATCHED_TRANSACTION_TYPE);
    }

    if (account.getBalance() < amount) {
      throw new AccountException(ErrorCode.INSUFFICIENT_BALANCE);
    }

    Account receiverAccount = accountRepository.findByAccountNumberAndBank(receiverAccountNumber, receiverBank)
        .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_ACCOUNT));

    account.setBalance(account.getBalance() - amount);

    receiverAccount.setBalance(receiverAccount.getBalance() + amount);

    return transactionRepository.save(Transaction.builder()
        .account(account)
        .receiverBank(receiverBank)
        .receiverAccountNumber(receiverAccount.getAccountNumber())
        .transactionType(REMITTANCE)
        .balance(account.getBalance())
        .amount(amount)
        .transactionResultType(TransactionResultType.SUCCESS)
        .transactedAt(LocalDateTime.now())
        .build());
  }

  @Transactional
  public List<Transaction> getTransactionsByAccountId(Long accountId, String token) {

    if (!provider.validateToken(token)) {
      throw new AccountException(ErrorCode.UNAUTHORIZED_TOKEN);
    }

    return transactionRepository.findByAccountId(accountId)
        .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_TRANSACTION));
  }
}