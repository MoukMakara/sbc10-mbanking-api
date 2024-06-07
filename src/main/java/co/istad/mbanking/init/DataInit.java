package co.istad.mbanking.init;

import co.istad.mbanking.domain.AccountType;
import co.istad.mbanking.domain.User;
import co.istad.mbanking.features.accounttype.AccountTypeRepository;
import co.istad.mbanking.features.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final UserRepository userRepository;
    private final AccountTypeRepository accountTypeRepository;

    @PostConstruct
    void init() {

        if (accountTypeRepository.count() == 0) {
            AccountType payroll = new AccountType();
            payroll.setName("Payroll");
            payroll.setAlias("payroll");
            payroll.setIsDeleted(false);
            payroll.setDescription("Payroll Account of User");

            AccountType saving = new AccountType();
            saving.setName("Saving");
            saving.setAlias("saving");
            saving.setIsDeleted(false);
            saving.setDescription("Saving Account of User");

            accountTypeRepository.save(payroll);
            accountTypeRepository.save(saving);
        }

        if (userRepository.count() == 0) {
            User user = new User();
            user.setUuid(UUID.randomUUID().toString());
            user.setName("Mouk Makara");
            user.setGender("Male");
            user.setPhoneNumber("096832724");
            user.setPin("1234");
            user.setPassword("8892");
            user.setNationalCardId("123456789");
            user.setProfileImage("user/avatar.png");
            user.setStudentCardId("ISTAD-000001");
            user.setIsDeleted(false);
            user.setIsBlocked(false);

            User user2 = new User();
            user2.setUuid(UUID.randomUUID().toString());
            user2.setName("Chan Bopha");
            user2.setGender("Female");
            user2.setPhoneNumber("077459947");
            user2.setPin("5678");
            user2.setPassword("8888");
            user2.setNationalCardId("88889999");
            user2.setProfileImage("user/avatar.png");
            user2.setIsDeleted(false);
            user2.setIsBlocked(false);

            //userRepository.save(user);
            //userRepository.save(user2);
            userRepository.saveAll(List.of(user, user2));
        }

    }

}
