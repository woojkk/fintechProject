package woojkk.fintechProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import woojkk.fintechProject.domain.Account;
import woojkk.fintechProject.domain.AccountUser;
import woojkk.fintechProject.type.AccountType;
import woojkk.fintechProject.type.Bank;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

  boolean existsByAccountNumber(String newAccountNumber);

  Integer countByAccountUserAndAccountTypeAndBank(AccountUser accountUser, AccountType accountType, Bank bank);

}
