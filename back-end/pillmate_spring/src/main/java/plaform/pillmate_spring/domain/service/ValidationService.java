package plaform.pillmate_spring.domain.service;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import plaform.pillmate_spring.domain.entity.Member;

import java.util.Optional;

@Service
public class ValidationService {

    public static void validationLoginMember(Optional<Member> findOptionalMember) throws BadRequestException {
        if (findOptionalMember.isPresent()) {
            throw new BadRequestException("이미 회원 존재합니다.");
        }

    }
    public static Member validationMember(Optional<Member> findOptionalMember) throws BadRequestException {
        if (findOptionalMember.isPresent()) {
            return findOptionalMember.get();
        }else
            throw new BadRequestException("회원을 찾을 수 없습니다");

    }
}
