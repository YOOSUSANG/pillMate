package plaform.pillmate_spring.domain.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import plaform.pillmate_spring.domain.dto.MemberLoginOAuth;
import plaform.pillmate_spring.domain.entity.Member;
import plaform.pillmate_spring.domain.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(MemberLoginOAuth memberLoginOAuth) throws BadRequestException {
        Optional<Member> findOptionalMember = memberRepository.findByUsername(memberLoginOAuth.getUsername());
        ValidationService.validationLoginMember(findOptionalMember);
        Member member = Member.createMemberByOAuth(memberLoginOAuth.getUsername(), memberLoginOAuth.getName(), memberLoginOAuth.getNickname(), memberLoginOAuth.getEmail(), memberLoginOAuth.getGender(), memberLoginOAuth.getRole());
        Member joinMember = memberRepository.save(member);
        return joinMember.getId();
    }

    @Transactional
    public void dataUpdate(MemberLoginOAuth memberLoginOAuth) throws BadRequestException {
        Optional<Member> findOptionalMember = memberRepository.findByUsername(memberLoginOAuth.getUsername());
        Member member = ValidationService.validationMember(findOptionalMember);
        Member.updateMemberByOAuth(member, memberLoginOAuth.getName(),memberLoginOAuth.getNickname() ,memberLoginOAuth.getEmail(),memberLoginOAuth.getGender() ,memberLoginOAuth.getRole());
    }

    public Member find(String email) throws BadRequestException {
        Optional<Member> findOptionalMember = memberRepository.findByEmail(email);
        Member member = ValidationService.validationMember(findOptionalMember);
        return member;
    }

    public Member findUsername(String username) throws BadRequestException {
        Optional<Member> findOptionalMember = memberRepository.findByUsername(username);
        Member member = ValidationService.validationMember(findOptionalMember);
        return member;
    }


    public Member find(Long id) {
        Optional<Member> findOptionalUser = memberRepository.findById(id);
        return findOptionalUser.get();
    }

    public List<Member> findAll() {
        List<Member> all = memberRepository.findAll();
        return all;
    }

    @Transactional
    public void removeUser(Long id) {
        memberRepository.deleteById(id);
    }


    @Transactional
    public void removeAllUser() {
        memberRepository.deleteAll();

    }


}
