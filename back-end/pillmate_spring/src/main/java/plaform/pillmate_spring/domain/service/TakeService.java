package plaform.pillmate_spring.domain.service;


import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import plaform.pillmate_spring.domain.entity.Member;
import plaform.pillmate_spring.domain.entity.Pill;
import plaform.pillmate_spring.domain.entity.Take;
import plaform.pillmate_spring.domain.repository.MemberRepository;
import plaform.pillmate_spring.domain.repository.PillRepository;
import plaform.pillmate_spring.domain.repository.TakeRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TakeService {

    private final MemberRepository memberRepository;
    private final PillRepository pillRepository;
    private final TakeRepository takeRepository;

    @Transactional
    public Long pillTakeOne(Long memberId, String name) throws BadRequestException {
        Optional<Member> findOptionalMember = memberRepository.findById(memberId);
        Optional<Pill> findOptionalPill = pillRepository.findByName(name);
        Member member = ValidationService.validationMember(findOptionalMember);
        Pill pill = ValidationService.validationPill(findOptionalPill);
        Take pillTake = Take.createTake(member, pill);
        Take saveTake = takeRepository.save(pillTake);
        return saveTake.getId();
    }

    public List<Take> findTakePill(Long memberId) {
        List<Take> takePill = takeRepository.findByMemberId(memberId);
        return takePill;
    }

    @Transactional
    public void removeTakePill(Long memberId, String name) {
        takeRepository.deleteByMemberIdAndPillName(memberId, name);
    }

}
