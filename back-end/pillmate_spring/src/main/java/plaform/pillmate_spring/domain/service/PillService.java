package plaform.pillmate_spring.domain.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import plaform.pillmate_spring.domain.dto.PillRequestData;
import plaform.pillmate_spring.domain.entity.Pill;
import plaform.pillmate_spring.domain.repository.PillRepository;

import java.util.Optional;

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

    public Pill find(Long pillId) throws BadRequestException {
        Pill pill = findPill(pillId);
        return pill;
    }

    public Pill find(String name) throws BadRequestException {
        Pill pill = findPill(name);
        return pill;
    }

    private Pill findPill(String name) throws BadRequestException {
        Optional<Pill> findOptionalPill = pillRepository.findByName(name);
        Pill pill = ValidationService.validationPill(findOptionalPill);
        return pill;
    }


    private Pill findPill(Long pillId) throws BadRequestException {
        Optional<Pill> findOptionalPill = pillRepository.findById(pillId);
        Pill pill = ValidationService.validationPill(findOptionalPill);
        return pill;
    }

    @Transactional
    public void AllPillRemove() {
        pillRepository.deleteAll();
    }
}
