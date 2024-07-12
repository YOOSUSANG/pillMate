package plaform.pillmate_spring.domain.entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "refreshToken",timeToLive = 80400)
@Getter
@Setter
public class RedisRefreshToken {
    @Id
    private Long id;
    private String username;
    @Indexed
    private String refresh;

}
