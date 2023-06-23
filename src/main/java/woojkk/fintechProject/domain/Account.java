package woojkk.fintechProject.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import woojkk.fintechProject.type.AccountStatus;
import woojkk.fintechProject.type.AccountType;
import woojkk.fintechProject.type.Bank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "account", indexes = @Index(name = "idxAccountNumber", columnList = "accountNumber"))
public class Account extends BaseEntity {


  @ManyToOne
  private AccountUser accountUser;
  @Enumerated(EnumType.STRING)
  private Bank bank;
  private String accountPassword;
  private String accountNumber;
  @Enumerated(EnumType.STRING)
  private AccountStatus accountStatus;
  @Enumerated(EnumType.STRING)
  private AccountType accountType;
  private Long balance;
  private Long setLimit;

  private LocalDateTime registeredAt;
  private LocalDateTime unRegisteredAt;

}
