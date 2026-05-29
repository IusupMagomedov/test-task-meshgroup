package meshgroup.testtask.config;

import lombok.RequiredArgsConstructor;
import meshgroup.testtask.entity.Account;
import meshgroup.testtask.entity.EmailData;
import meshgroup.testtask.entity.PhoneData;
import meshgroup.testtask.entity.User;
import meshgroup.testtask.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            return;
        }

        createUser("Ivan Petrov", LocalDate.of(1990, 5, 15), "ivan@example.com", "79201234567",
                new BigDecimal("1000.00"), "password123");

        createUser("Maria Sidorova", LocalDate.of(1985, 8, 22), "maria@example.com", "79207654321",
                new BigDecimal("500.00"), "password123");

        createUser("Alexey Ivanov", LocalDate.of(2000, 1, 10), "alexey@example.com", "79209876543",
                new BigDecimal("2500.00"), "password123");
    }

    private void createUser(String name, LocalDate dateOfBirth, String email, String phone,
                            BigDecimal initialBalance, String rawPassword) {
        User user = new User();
        user.setName(name);
        user.setDateOfBirth(dateOfBirth);
        user.setPassword(passwordEncoder.encode(rawPassword));

        EmailData emailData = new EmailData();
        emailData.setEmail(email);
        emailData.setUser(user);
        user.setEmails(List.of(emailData));

        PhoneData phoneData = new PhoneData();
        phoneData.setPhone(phone);
        phoneData.setUser(user);
        user.setPhones(List.of(phoneData));

        Account account = new Account();
        account.setUser(user);
        account.setBalance(initialBalance);
        account.setInitialBalance(initialBalance);
        user.setAccount(account);

        userRepository.save(user);
    }
}
