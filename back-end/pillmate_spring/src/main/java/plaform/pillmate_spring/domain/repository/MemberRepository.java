package plaform.pillmate_spring.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import plaform.pillmate_spring.domain.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member save(Member member);

    Optional<Member> findById(Long id);
    Optional<Member> findByUsername(String username);
    Optional<Member> findByEmail(String email);

    List<Member> findAll();

    void deleteById(Long id);

    void deleteAll();


}
