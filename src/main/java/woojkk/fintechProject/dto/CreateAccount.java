package woojkk.fintechProject.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import woojkk.fintechProject.type.AccountStatus;
import woojkk.fintechProject.type.AccountType;
import woojkk.fintechProject.type.Bank;

public class CreateAccount {

  @Getter
  @Setter
  @AllArgsConstructor
  public static class Request {

    @NotNull
    private Long userId;

    @NotNull
    private Long initialBalance;

    @NotNull
    private String accountPassword;

    @NotNull
    private Bank bank;

    @NotNull
    private AccountType accountType;

    @NotNull
    @Max(100000000)
    private Long setLimit;
  }

  @Getter
  @Setter
  @NotNull
  @AllArgsConstructor
  @Builder
  public static class Response {
    private Long userId;
    private Bank bank;
    private AccountType accountType;
    private Long initialBalance;
    private String accountNumber;
    private AccountStatus accountStatus;
    private Long setLimit;
    private LocalDateTime registeredAt;

    public static Response from(AccountDto accountDto) {
      return Response.builder()
          .userId(accountDto.getUserId())
          .bank(accountDto.getBank())
          .accountType(accountDto.getAccountType())
          .initialBalance(accountDto.getBalance())
          .accountNumber(accountDto.getAccountNumber())
          .accountStatus(accountDto.getAccountStatus())
          .setLimit(accountDto.getSetLimit())
          .registeredAt(accountDto.getRegisteredAt())
          .build();
    }
  }
}
