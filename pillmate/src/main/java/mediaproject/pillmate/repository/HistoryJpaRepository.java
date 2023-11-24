package mediaproject.pillmate.repository;

import jakarta.transaction.Transactional;
import mediaproject.pillmate.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryJpaRepository extends JpaRepository<History, Long> {

    History save(History history);

    @Query("select h from History h where h.userId = :id")
    List<History> findTakePillByUserId(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("delete from History h where h.userId = :id and h.pillName = :name")
    void deleteTakePillByPillName(@Param("id") Long id, @Param("name") String name);

    void deleteAll();


}
