package meshgroup.testtask.repository;

import meshgroup.testtask.entity.PhoneData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {

    boolean existsByPhone(String phone);

    Optional<PhoneData> findByUser_IdAndPhone(Long userId, String phone);
}
