package woojkk.fintechProject.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import woojkk.fintechProject.domain.AccountUser;

@Repository
public interface AccountUserRepository extends JpaRepository<AccountUser, Long> {
  Optional<AccountUser> findByEmail(String email);

}
