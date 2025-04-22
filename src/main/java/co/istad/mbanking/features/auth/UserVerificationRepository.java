package co.istad.mbanking.features.auth;

import co.istad.mbanking.domain.User;
import co.istad.mbanking.domain.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {

    Optional<UserVerification> findByUserAndVerifiedCode(User user, String verifiedCode);

    Optional<UserVerification> findByUser(User user);

}
