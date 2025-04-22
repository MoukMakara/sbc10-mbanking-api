package co.istad.mbanking.init;

import co.istad.mbanking.domain.AccountType;
import co.istad.mbanking.domain.Role;
import co.istad.mbanking.domain.User;
import co.istad.mbanking.features.accounttype.AccountTypeRepository;
import co.istad.mbanking.features.user.RoleRepository;
import co.istad.mbanking.features.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final PasswordEncoder passwordEncoder;

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

            Role user = new Role();
            user.setName("USER");

            Role customer = new Role();
            customer.setName("CUSTOMER");

            Role manager = new Role();
            manager.setName("MANAGER");

            Role admin = new Role();
            admin.setName("ADMIN");

            roleRepository.saveAll(List.of(user, customer, manager, admin));

            User user1 = new User();
            user1.setUuid(UUID.randomUUID().toString());
            user1.setName("Mouk Makara");
            user1.setGender("Male");
            user1.setPhoneNumber("0975494981");
            user1.setEmail("admin@gmail.com");
            user1.setPin("1234");
            user1.setPassword(passwordEncoder.encode("Qwer123@#$"));
            user1.setNationalCardId("123456789");
            user1.setProfileImage("user/avatar.png");
            user1.setStudentCardId("ISTAD-000001");
            user1.setIsDeleted(false);
            user1.setIsBlocked(false);
            user1.setIsVerified(true);
            user1.setRoles(List.of(user, admin));

            User user2 = new User();
            user2.setUuid(UUID.randomUUID().toString());
            user2.setName("Chan Dara");
            user2.setGender("Male");
            user2.setPhoneNumber("099459947");
            user2.setEmail("manager@gmail.com");
            user2.setPin("1234");
            user2.setPassword(passwordEncoder.encode("Qwer123@#$"));
            user2.setNationalCardId("88889999");
            user2.setProfileImage("user/avatar.png");
            user2.setStudentCardId("ISTAD-000002");
            user2.setIsDeleted(false);
            user2.setIsBlocked(false);
            user2.setIsVerified(true);
            user2.setRoles(List.of(user, manager));

            User user3 = new User();
            user3.setUuid(UUID.randomUUID().toString());
            user3.setName("Pich Bopha");
            user3.setGender("Female");
            user3.setPhoneNumber("012459947");
            user3.setEmail("customer@gmail.com");
            user3.setPin("7777");
            user3.setPassword(passwordEncoder.encode("Qwer123@#$"));
            user3.setNationalCardId("88889991");
            user3.setProfileImage("user/avatar.png");
            user3.setStudentCardId("ISTAD-000003");
            user3.setIsDeleted(false);
            user3.setIsBlocked(false);
            user3.setIsVerified(true);
            user3.setRoles(List.of(user, customer));

            //userRepository.save(user);
            //userRepository.save(user2);
            userRepository.saveAll(List.of(user1, user2, user3));
        }

    }

}
