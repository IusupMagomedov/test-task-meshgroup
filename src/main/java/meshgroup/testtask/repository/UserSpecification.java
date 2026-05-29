package meshgroup.testtask.repository;

import jakarta.persistence.criteria.Join;
import meshgroup.testtask.entity.EmailData;
import meshgroup.testtask.entity.PhoneData;
import meshgroup.testtask.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class UserSpecification {

    private UserSpecification() {}

    public static Specification<User> dateOfBirthAfter(LocalDate dateOfBirth) {
        return (root, query, cb) ->
                dateOfBirth == null ? null : cb.greaterThan(root.get("dateOfBirth"), dateOfBirth);
    }

    public static Specification<User> phoneEquals(String phone) {
        return (root, query, cb) -> {
            if (phone == null) return null;
            Join<User, PhoneData> phones = root.join("phones");
            return cb.equal(phones.get("phone"), phone);
        };
    }

    public static Specification<User> nameLike(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(root.get("name"), name + "%");
    }

    public static Specification<User> emailEquals(String email) {
        return (root, query, cb) -> {
            if (email == null) return null;
            Join<User, EmailData> emails = root.join("emails");
            return cb.equal(emails.get("email"), email);
        };
    }
}
