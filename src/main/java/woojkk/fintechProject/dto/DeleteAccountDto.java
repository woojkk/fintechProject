package woojkk.fintechProject.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import woojkk.fintechProject.domain.Account;
import woojkk.fintechProject.type.AccountStatus;
import woojkk.fintechProject.type.AccountType;
import woojkk.fintechProject.type.Bank;

public class DeleteAccountDto {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {


    @NotNull
    private String accountPassword;

    @NotNull
    private String accountNumber;

    @NotNull
    private Bank bank;

    @NotNull
    private AccountType accountType;

  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Response {
    private Bank bank;
    private String accountNumber;
    private AccountType accountType;
    private AccountStatus accountStatus;
    private LocalDateTime unRegisteredAt;

    public static Response from(Account account) {
      return Response.builder()
          .bank(account.getBank())
          .accountNumber(account.getAccountNumber())
          .accountType(account.getAccountType())
          .accountStatus(account.getAccountStatus())
          .unRegisteredAt(account.getUnRegisteredAt())
          .build();
    }
  }
}
