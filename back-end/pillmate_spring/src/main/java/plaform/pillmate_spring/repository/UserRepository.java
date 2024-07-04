package plaform.pillmate_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import plaform.pillmate_spring.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmailAndPassword(String email, String password);

    List<User> findAll();

    void deleteById(Long id);

    void deleteAll();


}
