package woojkk.fintechProject.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import woojkk.fintechProject.domain.Transaction;
import woojkk.fintechProject.type.TransactionResultType;
import woojkk.fintechProject.type.TransactionType;

public class TransactionDto {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {

    @NotNull
    private Long accountId;

    @NotBlank
    private String accountPassword;

    private TransactionType transactionType;

    @NotNull
    @Min(10)
    private Long amount;

  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Response {

    private TransactionResultType transactionResultType;
    private Long transactionAmount;
    private Long balance;
    private LocalDateTime TransactedAt;


    public static Response from(Transaction transaction) {
      return Response.builder()
          .transactionAmount(transaction.getAmount())
          .balance(transaction.getBalance())
          .transactionResultType(TransactionResultType.SUCCESS)
          .TransactedAt(LocalDateTime.now())
          .build();
    }
  }
}
