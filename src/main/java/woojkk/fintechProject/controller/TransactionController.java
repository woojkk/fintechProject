package woojkk.fintechProject.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import woojkk.fintechProject.aop.AccountLock;
import woojkk.fintechProject.dto.TransactionDto;
import woojkk.fintechProject.dto.TransactionDto.Response;
import woojkk.fintechProject.dto.TransactionInfo;
import woojkk.fintechProject.service.LockService;
import woojkk.fintechProject.service.TransactionService;

@RestController
@RequiredArgsConstructor
public class TransactionController {

  private final TransactionService transactionService;
  private final LockService lockService;

  @PostMapping("/transaction/withdraw")
  public TransactionDto.Response withdrawMoney(@RequestHeader(name = "X-AUTH-TOKEN") String token,
      @RequestBody @Valid TransactionDto.Request request) {

    return Response.from(transactionService.drawMoney(
        request.getAccountId(),
        request.getAccountPassword(),
        request.getTransactionType(),
        request.getAmount(),
        token
    ));
  }

  @PostMapping("/transaction/deposit")
  public TransactionDto.Response depositMoney(@RequestHeader(name = "X-AUTH-TOKEN") String token,
      @RequestBody @Valid TransactionDto.Request request) {

    return Response.from(transactionService.depositMoney(
        request.getAccountId(),
        request.getAccountPassword(),
        request.getTransactionType(),
        request.getAmount(),
        token
    ));
  }

  @PostMapping("/transaction/remittance")
  @AccountLock
  public TransactionDto.Response remittanceMoney(@RequestHeader(name = "X-AUTH-TOKEN") String token,
      @RequestBody @Valid TransactionDto.Request request) {

    lockService.lock(request.getAccountId());

    return Response.from(transactionService.remittanceMoney(
          request.getAccountId(),
          request.getAccountPassword(),
          request.getTransactionType(),
          request.getBank(),
          request.getAccountNumber(),
          request.getAmount(),
          token
      ));
  }

  @GetMapping("/transaction/search/{accountId}")
  public List<TransactionInfo> getTransactionsByAccountId(@RequestHeader(name = "X-AUTH-TOKEN") String token,
      @PathVariable Long accountId) {

    return transactionService.getTransactionsByAccountId(accountId, token)
        .stream().map(transaction -> TransactionInfo.builder()
            .receiverAccountNumber(transaction.getReceiverAccountNumber())
            .transactionType(transaction.getTransactionType())
            .amount(transaction.getAmount())
            .bank(transaction.getReceiverBank())
            .transactionResultType(transaction.getTransactionResultType())
            .transactedAt(transaction.getTransactedAt())
            .build())
        .collect(Collectors.toList());
  }

}
