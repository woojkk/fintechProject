package woojkk.fintechProject.domain;

import java.util.Locale;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import woojkk.fintechProject.dto.LoginForm;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class AccountUser extends BaseEntity {

  private String name;

  private String password;

  private String email;



  public static AccountUser from(LoginForm form) {
    return AccountUser.builder()
        .email(form.getEmail().toLowerCase(Locale.ROOT))
        .password(form.getPassword())
        .build();
  }
}
