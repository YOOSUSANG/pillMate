package plaform.pillmate_spring.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import plaform.pillmate_spring.domain.entity.Pill;

import java.util.List;
import java.util.Optional;

@Repository
public interface PillRepository extends JpaRepository<Pill, Long> {


    Pill save(Pill pill);

    Optional<Pill> findByName(String name);

    void deleteAll();


}
