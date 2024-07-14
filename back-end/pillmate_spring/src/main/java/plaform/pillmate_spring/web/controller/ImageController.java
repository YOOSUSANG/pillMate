package plaform.pillmate_spring.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;
import plaform.pillmate_spring.domain.oauth2.CustomOAuth2User;
import plaform.pillmate_spring.domain.service.MemberService;
import plaform.pillmate_spring.domain.service.S3Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final S3Service s3Service;
    private final MemberService memberService;


    @PostMapping("/image/upload")
    public Map<String, Object> imageUpload(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, MultipartRequest request) throws Exception {
        HashMap<String, Object> responseData = new HashMap<>();
        String username = customOAuth2User.getUsername();
        try {
            String s3Url = s3Service.imageUpload(request);
            responseData.put("uploaded", true);
            responseData.put("url", s3Url);
            memberService.profileUpdate(username, s3Url);
            return responseData;

        } catch (IOException e) {
            responseData.put("uploaded", false);
            return responseData;
        }
    }
}
