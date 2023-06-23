package woojkk.fintechProject.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import woojkk.fintechProject.domain.Account;
import woojkk.fintechProject.type.AccountStatus;
import woojkk.fintechProject.type.AccountType;
import woojkk.fintechProject.type.Bank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
  private Long userId;
  private String accountPassword;
  private Bank bank;
  private String accountNumber;
  private Long balance;
  private Long setLimit;
  private AccountType accountType;
  private AccountStatus accountStatus;

  private LocalDateTime registeredAt;
  private LocalDateTime unRegisteredAt;

  public static AccountDto fromEntity(Account account) {
    return AccountDto.builder()
        .userId(account.getId())
        .accountPassword(account.getAccountPassword())
        .bank(account.getBank())
        .accountNumber(account.getAccountNumber())
        .balance(account.getBalance())
        .setLimit(account.getSetLimit())
        .accountType(account.getAccountType())
        .accountStatus(account.getAccountStatus())
        .registeredAt(account.getRegisteredAt())
        .unRegisteredAt(account.getUnRegisteredAt())
        .build();
  }
}
