package plaform.pillmate_spring.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import plaform.pillmate_spring.domain.entity.Take;

import java.util.List;

@Repository
public interface TakeRepository extends JpaRepository<Take, Long> {

    List<Take> findByMemberId(Long memberId);

    void deleteByMemberIdAndPillName(Long memberId, String pillName);


}
