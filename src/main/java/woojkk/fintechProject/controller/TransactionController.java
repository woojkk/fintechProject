package woojkk.fintechProject.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import woojkk.fintechProject.dto.TransactionDto;
import woojkk.fintechProject.dto.TransactionDto.Response;
import woojkk.fintechProject.service.TransactionService;

@RestController
@RequiredArgsConstructor
public class TransactionController {

  private final TransactionService transactionService;

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
}
