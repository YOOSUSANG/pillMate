package plaform.pillmate_spring.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import plaform.pillmate_spring.domain.entity.History;
import plaform.pillmate_spring.domain.entity.Member;
import plaform.pillmate_spring.domain.oauth2.CustomOAuth2User;
import plaform.pillmate_spring.domain.service.HistoryService;
import plaform.pillmate_spring.domain.service.MemberService;
import plaform.pillmate_spring.web.dto.MemberInfoAfterJWTDto;

import java.security.Principal;
import java.util.*;
@Slf4j
@RestController
@RequiredArgsConstructor
public class PillController {


    private final MemberService memberService;
    private final HistoryService historyService;

    @GetMapping("/user")
    public MemberInfoAfterJWTDto getUserInfo(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) throws BadRequestException {
        String username = customOAuth2User.getUsername();
        Member member = memberService.findUsername(username);
        MemberInfoAfterJWTDto memberInfoAfterJWTDto = new MemberInfoAfterJWTDto(member);
        log.info("user complete");
        return memberInfoAfterJWTDto;
    }
    @PostMapping("/validation/jwt")
    public ResponseEntity<?> jwtValidation(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) throws BadRequestException {
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @PostMapping("/PillMyProfile")
//    public Member changeUserValue(@RequestBody Map<String, String> changeData) {
//        Set<String> keys = changeData.keySet();
//        List<String> keyList = new ArrayList<>(keys);
//        String key = keyList.get(0);
//        String email = keyList.get(1);
//        String password = changeData.get(email);
//        if (Objects.equals(key, "img")) {
//            System.out.println(changeData.get(key));
//            Long userId = memberService.changeImg(email, password, changeData.get(key));
//            Member changedImgMember = memberService.findUser(userId);
//            return changedImgMember;
//        }
//        if (Objects.equals(key, "newNickname")) {
//            Long userId = memberService.changeImg(email, password, changeData.get(key));
//            Member changedNickMember = memberService.findUser(userId);
//            return changedNickMember;
//
//        }
//        if (Objects.equals(key, "newPassword")) {
//            Long userId = memberService.changeImg(email, password, changeData.get(key));
//            Member changePasswordMember = memberService.findUser(userId);
//            return changePasswordMember;
//        }
//        return null;
//    }
    @PostMapping("/pill/detail")
    public History joinHistory(@RequestBody History history) {
        History joinHistory = historyService.join(history);
        return joinHistory;
    }
    @PostMapping("/record")
    public ResponseEntity<String>  removeHistory(@RequestBody Map<String, String> removeData) {
        String userId = removeData.get("userId");
        String pillName = removeData.get("pillName");
        historyService.removeTakePill(Long.valueOf(userId),pillName);
        return ResponseEntity.ok( "{\"message\": \"ok\"}");
    }


    //get은 PathVariable
    @GetMapping("/pill/detail/{id}")
    public List<History> findHistory(@PathVariable Long id) {
        List<History> takePill = historyService.findTakePill(id);
        return takePill;
    }

}
