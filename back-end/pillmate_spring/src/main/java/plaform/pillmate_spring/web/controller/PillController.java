package plaform.pillmate_spring.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import plaform.pillmate_spring.domain.entity.Member;
import plaform.pillmate_spring.domain.entity.Pill;
import plaform.pillmate_spring.domain.oauth2.CustomOAuth2User;
import plaform.pillmate_spring.domain.service.MemberService;
import plaform.pillmate_spring.domain.service.PillService;
import plaform.pillmate_spring.domain.service.TakeService;
import plaform.pillmate_spring.web.dto.MemberInfoAfterJWTDto;
import plaform.pillmate_spring.web.dto.PillRequestTakeDto;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Tag(name = "공용 API", description = "로그인, 검증, 알약 관련")
@RequiredArgsConstructor
public class PillController {


    private final MemberService memberService;
    private final PillService pillService;
    private final TakeService takeService;


    @Operation(summary = "회원 정보 조회")
    @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공", content = {
            @Content(schema = @Schema(implementation = MemberInfoAfterJWTDto.class))
    })
    @GetMapping("/user")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) throws BadRequestException {
        String username = customOAuth2User.getUsername();
        Member member = memberService.findUsername(username);
        MemberInfoAfterJWTDto memberInfoAfterJWTDto = new MemberInfoAfterJWTDto(member);
        log.info("user complete");
        return ResponseEntity.ok(memberInfoAfterJWTDto);
    }

    @Operation(summary = "JWT 검증")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JWT 검증 성공")
    })
    @PostMapping("/validation/jwt")
    public ResponseEntity<?> jwtValidation(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) throws BadRequestException {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "알약 복용")
    @Parameter(name = "pillRequestTakeDto", description = "알약 이름을 받는 DTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "약품 복용 성공")
    })
    @PostMapping("/pill/take")
    public ResponseEntity<?> joinHistory(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestBody PillRequestTakeDto pillRequestTakeDto) throws BadRequestException {
        String username = customOAuth2User.getUsername();
        log.info("name : {}", pillRequestTakeDto.getName());
        Member member = memberService.findUsername(username);
        takeService.pillTakeOne(member.getId(), pillRequestTakeDto.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "알약 복용 취소")
    @Parameter(name = "pillRequestTakeDto", description = "알약 이름을 받는 DTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "약품 복용 취소 성공")
    })
    @PostMapping("/pill/delete")
    public ResponseEntity<?> removeHistory(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestBody PillRequestTakeDto pillRequestTakeDto) throws BadRequestException {
        String username = customOAuth2User.getUsername();
        Member member = memberService.findUsername(username);
        takeService.removeTakePill(member.getId(), pillRequestTakeDto.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }


//    //get은 PathVariable
//    @GetMapping("/pill/detail/{id}")
//    public List<Pill> findHistory(@PathVariable Long id) {
//        List<Pill> takePill = pillService.findTakePill(id);
//        return takePill;
//    }

}
