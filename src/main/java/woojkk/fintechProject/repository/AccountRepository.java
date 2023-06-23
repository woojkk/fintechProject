package woojkk.fintechProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import woojkk.fintechProject.domain.Account;
import woojkk.fintechProject.domain.AccountUser;
import woojkk.fintechProject.type.AccountType;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

  boolean existsByAccountNumber(String newAccountNumber);

  Integer countByAccountUserAndAccountType(AccountUser accountUser, AccountType accountType);
}
