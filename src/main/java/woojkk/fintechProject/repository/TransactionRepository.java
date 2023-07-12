package woojkk.fintechProject.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import woojkk.fintechProject.domain.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  Optional<List<Transaction>> findByAccountId(Long accountId);
}
