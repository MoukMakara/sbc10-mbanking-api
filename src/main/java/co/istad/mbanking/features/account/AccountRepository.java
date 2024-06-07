package co.istad.mbanking.features.account;

import co.istad.mbanking.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByActNo(String actNo);

    // SELECT EXISTS(SELECT * FROM accounts WHERE act_no = ?)
    boolean existsByActNo(String actNo);

}
