package plaform.pillmate_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import plaform.pillmate_spring.domain.History;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {


    History save(History history);

    List<History> findAllByUserId(Long id);

    void deleteByUserIdAndAndPillName(Long id, String name);

    void deleteAll();


}
