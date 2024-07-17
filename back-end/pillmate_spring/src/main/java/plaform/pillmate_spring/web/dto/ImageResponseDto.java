package plaform.pillmate_spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Schema(description = "이미지 경로 DTO")
@AllArgsConstructor
@RequiredArgsConstructor
public class ImageResponseDto {
    @Schema(description = "이미지 S3 경로", defaultValue = "https://pillmate-s3-1.s3.ap-northeast-2.amazonaws.com/77feedd5-b4d1-42cf-8a29-a78c2bba8dbd.jpg")
    private String url;
}
