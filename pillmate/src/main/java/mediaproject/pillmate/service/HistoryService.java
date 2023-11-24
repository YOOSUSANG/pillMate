package mediaproject.pillmate.service;

import jakarta.transaction.Transactional;
import mediaproject.pillmate.domain.History;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HistoryService {

    History join(History history);

    List<History> findTakePill(Long id);

    void removeTakePill(Long id, String name);

    void AllPillRemove();
}
