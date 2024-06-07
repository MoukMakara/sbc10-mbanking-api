package co.istad.mbanking.features.accounttype;

import co.istad.mbanking.domain.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountTypeRepository extends JpaRepository<AccountType, Integer> {

    boolean existsByAlias(String alias);

    // SELECT * FROM account_types WHERE alias = ?
    Optional<AccountType> findByAlias(String alias);

}
