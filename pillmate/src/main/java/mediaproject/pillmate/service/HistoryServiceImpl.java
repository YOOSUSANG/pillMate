package mediaproject.pillmate.service;

import jakarta.transaction.Transactional;
import mediaproject.pillmate.domain.History;
import mediaproject.pillmate.repository.HistoryJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class HistoryServiceImpl implements HistoryService{

    private final HistoryJpaRepository historyJpaRepository;

    @Autowired
    public HistoryServiceImpl(HistoryJpaRepository historyJpaRepository) {
        this.historyJpaRepository = historyJpaRepository;
    }

    @Override
    public History join(History history) {
        History saveHistory = historyJpaRepository.save(history);
        return saveHistory;
    }

    @Override
    public List<History> findTakePill(Long id) {
        List<History> takePillByUserId = historyJpaRepository.findTakePillByUserId(id);
        return takePillByUserId;
    }
    @Override
    public void removeTakePill(Long id, String name) {
        historyJpaRepository.deleteTakePillByPillName(id,name);
    }
    @Override
    public void AllPillRemove() {
        historyJpaRepository.deleteAll();
    }
}
