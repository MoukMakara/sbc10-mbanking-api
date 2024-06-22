package co.istad.mbanking.features.auth;

import co.istad.mbanking.domain.Role;
import co.istad.mbanking.domain.User;
import co.istad.mbanking.domain.UserVerification;
import co.istad.mbanking.features.auth.dto.*;
import co.istad.mbanking.features.user.RoleRepository;
import co.istad.mbanking.features.user.UserRepository;
import co.istad.mbanking.mapper.UserMapper;
import co.istad.mbanking.util.RandomUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserVerificationRepository userVerificationRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String adminEmail;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public void verify(VerificationRequest verificationRequest) {

        // Validate email
        User user = userRepository
                .findByEmail(verificationRequest.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User has not been found"));

        // Validate verified code
        UserVerification userVerification = userVerificationRepository
                .findByUserAndVerifiedCode(user, verificationRequest.verifiedCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User verification has not been found"));

        // Is verified code expired?
        if (LocalTime.now().isAfter(userVerification.getExpiryTime())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Verified code has expired");
        }

        user.setIsVerified(true);
        userRepository.save(user);

        userVerificationRepository.delete(userVerification);
    }

    @Override
    public void sendVerification(String email) throws MessagingException {

        // Validate email
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User has not been found"));

        UserVerification userVerification = new UserVerification();
        userVerification.setUser(user);
        userVerification.setVerifiedCode(RandomUtil.random6Digits());
        userVerification.setExpiryTime(LocalTime.now().plusMinutes(1));
        userVerificationRepository.save(userVerification);

        // Prepare email for sending
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(email);
        helper.setFrom(adminEmail);
        helper.setSubject("User Verification");
        helper.setText(userVerification.getVerifiedCode());

        javaMailSender.send(message);

    }

    @Override
    public void resendVerification(String email) throws MessagingException {
        // Validate email
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User has not been found"));

        UserVerification userVerification = userVerificationRepository
                .findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User has not been found"));
        userVerification.setVerifiedCode(RandomUtil.random6Digits());
        userVerification.setExpiryTime(LocalTime.now().plusMinutes(1));
        userVerificationRepository.save(userVerification);

        // Prepare email for sending
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(email);
        helper.setFrom(adminEmail);
        helper.setSubject("User Verification");
        helper.setText(userVerification.getVerifiedCode());

        javaMailSender.send(message);
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {

        // Validate user's phone number
        if (userRepository.existsByPhoneNumber(registerRequest.phoneNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Phone number has already been used");
        }

        // Validate user's email
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email has already been used");
        }

        // Validate user's password
        if (!registerRequest.password().equals(registerRequest.confirmedPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Password does not match");
        }

        // Validate national ID card
        if (userRepository.existsByNationalCardId(registerRequest.nationalCardId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "National card ID has already been used");
        }

        // Validation term and policy
        if (!registerRequest.acceptTerm()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must accept the term");
        }

        User user = userMapper.fromRegisterRequest(registerRequest);

        // Set system data
        user.setUuid(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setProfileImage("profile/default-user.png");
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setIsVerified(false);

        Role roleUser = roleRepository.findRoleUser(); // default role
        Role roleCustomer = roleRepository.findRoleCustomer();
        List<Role> roles = List.of(roleUser, roleCustomer);
        user.setRoles(roles);
        userRepository.save(user);

        return RegisterResponse.builder()
                .message("You have registered successfully, please verify an email!")
                .email(user.getEmail())
                .build();
    }

}