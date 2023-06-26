package woojkk.fintechProject.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.Max;
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

public class CreateAccountDto {

  @Getter
  @Setter
  @AllArgsConstructor
  public static class Request {


    @NotNull
    private String accountPassword;

    @NotNull
    private Bank bank;

    @NotNull
    private Long initialBalance;

    @NotNull
    @Max(100000000)
    private Long setLimit;

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
    private Long initialBalance;
    private Long setLimit;
    private AccountType accountType;
    private AccountStatus accountStatus;
    private LocalDateTime registeredAt;

    public static Response from(Account account) {
      return Response.builder()
          .bank(account.getBank())
          .accountType(account.getAccountType())
          .initialBalance(account.getBalance())
          .accountNumber(account.getAccountNumber())
          .accountStatus(account.getAccountStatus())
          .setLimit(account.getSetLimit())
          .registeredAt(account.getRegisteredAt())
          .build();
    }
  }
}
