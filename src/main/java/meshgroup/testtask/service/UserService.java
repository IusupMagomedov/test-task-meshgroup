package meshgroup.testtask.service;

import lombok.RequiredArgsConstructor;
import meshgroup.testtask.dto.response.UserResponse;
import meshgroup.testtask.entity.EmailData;
import meshgroup.testtask.entity.PhoneData;
import meshgroup.testtask.entity.User;
import meshgroup.testtask.exception.BusinessException;
import meshgroup.testtask.repository.EmailDataRepository;
import meshgroup.testtask.repository.PhoneDataRepository;
import meshgroup.testtask.repository.UserRepository;
import meshgroup.testtask.repository.UserSpecification;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static meshgroup.testtask.config.CacheConfig.USERS_CACHE;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final EmailDataRepository emailDataRepository;
    private final PhoneDataRepository phoneDataRepository;

    @CacheEvict(value = USERS_CACHE, allEntries = true)
    public void addEmail(Long userId, String email) {
        if (emailDataRepository.existsByEmail(email)) {
            throw new BusinessException("Email already in use", HttpStatus.CONFLICT);
        }
        User user = getUser(userId);
        EmailData emailData = new EmailData();
        emailData.setUser(user);
        emailData.setEmail(email);
        emailDataRepository.save(emailData);
    }

    @CacheEvict(value = USERS_CACHE, allEntries = true)
    public void deleteEmail(Long userId, String email) {
        User user = getUser(userId);
        if (user.getEmails().size() <= 1) {
            throw new BusinessException("Cannot delete the last email", HttpStatus.BAD_REQUEST);
        }
        EmailData emailData = emailDataRepository.findByUser_IdAndEmail(userId, email)
                .orElseThrow(() -> new BusinessException("Email not found", HttpStatus.NOT_FOUND));
        emailDataRepository.delete(emailData);
    }

    @CacheEvict(value = USERS_CACHE, allEntries = true)
    public void addPhone(Long userId, String phone) {
        if (phoneDataRepository.existsByPhone(phone)) {
            throw new BusinessException("Phone already in use", HttpStatus.CONFLICT);
        }
        User user = getUser(userId);
        PhoneData phoneData = new PhoneData();
        phoneData.setUser(user);
        phoneData.setPhone(phone);
        phoneDataRepository.save(phoneData);
    }

    @CacheEvict(value = USERS_CACHE, allEntries = true)
    public void deletePhone(Long userId, String phone) {
        User user = getUser(userId);
        if (user.getPhones().size() <= 1) {
            throw new BusinessException("Cannot delete the last phone", HttpStatus.BAD_REQUEST);
        }
        PhoneData phoneData = phoneDataRepository.findByUser_IdAndPhone(userId, phone)
                .orElseThrow(() -> new BusinessException("Phone not found", HttpStatus.NOT_FOUND));
        phoneDataRepository.delete(phoneData);
    }

    @Cacheable(value = USERS_CACHE, key = "{#dateOfBirth, #phone, #name, #email, #pageable}")
    @Transactional(readOnly = true)
    public Page<UserResponse> searchUsers(LocalDate dateOfBirth, String phone, String name, String email, Pageable pageable) {
        Specification<User> spec = Specification
                .where(UserSpecification.dateOfBirthAfter(dateOfBirth))
                .and(UserSpecification.phoneEquals(phone))
                .and(UserSpecification.nameLike(name))
                .and(UserSpecification.emailEquals(email));
        return userRepository.findAll(spec, pageable).map(this::toResponse);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found", HttpStatus.NOT_FOUND));
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .dateOfBirth(user.getDateOfBirth())
                .emails(user.getEmails().stream().map(EmailData::getEmail).toList())
                .phones(user.getPhones().stream().map(PhoneData::getPhone).toList())
                .build();
    }
}
