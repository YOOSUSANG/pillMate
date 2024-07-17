package plaform.pillmate_spring.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import plaform.pillmate_spring.domain.dto.PillRequestData;
import plaform.pillmate_spring.domain.entity.Pill;
import plaform.pillmate_spring.domain.repository.PillRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PillService {

    private final PillRepository pillRepository;

    @Transactional
    public Pill join(PillRequestData pillRequestData) {
        Pill pill = Pill.createPill(pillRequestData.getName(), pillRequestData.getClassNo(), pillRequestData.getImgKey()
                , pillRequestData.getDlMaterial(), pillRequestData.getDlMaterialEn(), pillRequestData.getDlCustomShape()
                , pillRequestData.getColorClass1(), pillRequestData.getDrugShape());
        Pill savePill = pillRepository.save(pill);
        return savePill;
    }

    @Transactional
    public void AllPillRemove() {
        pillRepository.deleteAll();
    }
}
