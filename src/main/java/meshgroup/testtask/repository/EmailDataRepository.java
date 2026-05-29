package meshgroup.testtask.repository;

import meshgroup.testtask.entity.EmailData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailDataRepository extends JpaRepository<EmailData, Long> {

    boolean existsByEmail(String email);

    Optional<EmailData> findByUser_IdAndEmail(Long userId, String email);
}
