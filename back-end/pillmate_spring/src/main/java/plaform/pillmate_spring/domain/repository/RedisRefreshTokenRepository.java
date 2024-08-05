package plaform.pillmate_spring.domain.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import plaform.pillmate_spring.domain.entity.RedisRefreshToken;

@Repository
@Transactional(readOnly = true)
public interface RedisRefreshTokenRepository extends CrudRepository<RedisRefreshToken, Long> {
    Boolean existsByRefresh(String refresh);
    RedisRefreshToken findByRefresh(String refresh);
}