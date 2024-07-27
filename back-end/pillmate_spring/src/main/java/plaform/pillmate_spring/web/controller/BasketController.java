package plaform.pillmate_spring.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import plaform.pillmate_spring.domain.service.BasketService;
import plaform.pillmate_spring.domain.service.MemberService;
import plaform.pillmate_spring.domain.service.PillService;
import plaform.pillmate_spring.web.dto.*;


@Slf4j
@RestController
@Tag(name = "장바구니 API", description = "장바구니 관련")
@RequiredArgsConstructor
public class BasketController {


    private final PillService pillService;
    private final MemberService memberService;
    private final BasketService basketService;

    @Operation(summary = "알약 추가")
    @ApiResponse(responseCode = "200", description = "알약 추가 성공")
    @PostMapping("/basket/pills/add")
    public ResponseEntity<?> addPillInBasket(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestBody PillAddInBasketDto pillAddInBasketDto) throws BadRequestException {
        Pill pill = pillService.find(pillAddInBasketDto.getPillId());
        String username = customOAuth2User.getUsername();
        Member member = memberService.findUsername(username);
        basketService.addItem(member.getBasket().getId(), pill.getName(), pillAddInBasketDto.getQuantity());
        log.info("확인: {}, {}, {}", pillAddInBasketDto.getPillId(), pill.getName(), pillAddInBasketDto.getQuantity());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "알약 삭제")
    @ApiResponse(responseCode = "200", description = "알약 삭제 성공")
    @PostMapping("/basket/pills/delete/{BasketPillItemId}")
    public ResponseEntity<?> deletePillInBasket(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @PathVariable("BasketPillItemId") Long BasketPillItemId) throws BadRequestException {
        String username = customOAuth2User.getUsername();
        Member member = memberService.findUsername(username);
        basketService.removeItem(member.getBasket().getId(), BasketPillItemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "알약 전체 삭제")
    @ApiResponse(responseCode = "200", description = "알약 전체 삭제 성공")
    @PostMapping("/basket/clear")
    public ResponseEntity<?> clearPillInBasket(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) throws BadRequestException {
        String username = customOAuth2User.getUsername();
        Member member = memberService.findUsername(username);
        basketService.allRemoveItem(member.getBasket().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "알약 수량 수정")
    @ApiResponse(responseCode = "200", description = "알약 수량 수정 성공")
    @PostMapping("/basket/pills/edit")
    public ResponseEntity<?> editPillInBasket(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestBody PillEditBasketDto pillEditBasketDto) throws BadRequestException {
        basketService.editItemQuantity(pillEditBasketDto.getBasketPillItemId(), pillEditBasketDto.getQuantity());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "아이템들 조회")
    @ApiResponse(responseCode = "200", description = "아이템들 조회 성공", content = {
            @Content(schema = @Schema(implementation = BasketContentsDto.class))
    })
    @GetMapping("/basket/pills")
    public ResponseEntity<?> baskets(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) throws BadRequestException {
        String username = customOAuth2User.getUsername();
        Member member = memberService.findUsername(username);
        BasketContentsDto basketContentsDto = new BasketContentsDto(member.getBasket());
        return ResponseEntity.ok().body(new JsonResult<>(basketContentsDto));
    }

}
