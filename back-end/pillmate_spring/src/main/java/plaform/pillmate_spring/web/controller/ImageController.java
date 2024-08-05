package plaform.pillmate_spring.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;
import plaform.pillmate_spring.domain.oauth2.CustomOAuth2User;
import plaform.pillmate_spring.domain.service.MemberService;
import plaform.pillmate_spring.domain.service.S3Service;
import plaform.pillmate_spring.web.dto.ImageResponseDto;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "이미지 API", description = "AWS S3 이미지 처리 컨트롤러")
@RequiredArgsConstructor
public class ImageController {

    private final S3Service s3Service;
    private final MemberService memberService;

    @Operation(summary = "이미지 업로드", description = "파라미터: 이미지 파일 ")
    @Parameter(name = "request", description = "MultipartRequest")
    @ApiResponse(responseCode = "200", description = "이미지 업로드 성공", content = {
            @Content(schema = @Schema(implementation = ImageResponseDto.class))
    })
    @PostMapping("/image/upload")
    public ResponseEntity<?> imageUpload(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, MultipartRequest request) throws Exception {
        String username = customOAuth2User.getUsername();
        String s3Url = s3Service.imageUpload(request);
        memberService.profileUpdate(username, s3Url);
        return ResponseEntity.ok(new ImageResponseDto(s3Url));
    }
}
