package plaform.pillmate_spring.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import plaform.pillmate_spring.domain.entity.BasketPillItem;

@Repository
public interface BasketPillItemRepository extends JpaRepository<BasketPillItem, Long> {


}
