package woojkk.fintechProject.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import woojkk.fintechProject.type.Bank;
import woojkk.fintechProject.type.TransactionResultType;
import woojkk.fintechProject.type.TransactionType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionInfo {

  private String receiverAccountNumber;
  private TransactionType transactionType;
  private Long amount;
  private Bank bank;
  private TransactionResultType transactionResultType;
  private LocalDateTime transactedAt;

}
