package plaform.pillmate_spring.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter @Getter
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;
    private Long userId;
    private String pillName;

    public History() {


    }

    public History(Long userId, String pillName) {
        this.userId = userId;
        this.pillName = pillName;
    }
}