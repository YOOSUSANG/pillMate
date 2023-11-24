package mediaproject.pillmate.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_table")
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String userImg;
    private String userNickname;

    @Column(unique = true)
    private String email;
    private String password;

    public User(String userNickname, String email, String password) {
        this.userNickname = userNickname;
        this.email = email;
        this.password = password;
    }

    public User() {

    }
}
