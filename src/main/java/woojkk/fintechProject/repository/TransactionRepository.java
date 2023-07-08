package woojkk.fintechProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import woojkk.fintechProject.domain.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
