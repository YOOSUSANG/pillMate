package plaform.pillmate_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import plaform.pillmate_spring.domain.History;
import plaform.pillmate_spring.repository.HistoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HistoryService {

    private final HistoryRepository historyRepository;

    @Transactional
    public History join(History history) {
        History saveHistory = historyRepository.save(history);
        return saveHistory;
    }

    public List<History> findTakePill(Long id) {
        List<History> takePillByUserId = historyRepository.findAllByUserId(id);
        return takePillByUserId;
    }

    @Transactional
    public void removeTakePill(Long id, String name) {
        historyRepository.deleteByUserIdAndAndPillName(id, name);
    }
    @Transactional
    public void AllPillRemove() {
        historyRepository.deleteAll();
    }
}
