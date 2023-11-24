package mediaproject.pillmate.repository;

import mediaproject.pillmate.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

    //저장 -> jpaRepository에 존재
    User save(User user);

    //해당 유저 찾기 Id로-> jpaRepository에 존재
    Optional<User> findById(Long id);

    @Query("select u from User u where u.email = :email and u.password = :password")
    Optional<User> findByUser(@Param("email") String email, @Param("password") String password);

    //모두 조회 -> jpaRepository에 존재
    List<User> findAll();



    //유저 삭제 -> jpaRepository에 존재
    void deleteById(Long id);


    //모든 유저 삭제 -> jpaRepository에 존재
    void deleteAll();


}
