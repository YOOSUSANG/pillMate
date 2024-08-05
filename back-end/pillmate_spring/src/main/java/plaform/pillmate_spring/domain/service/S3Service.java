package plaform.pillmate_spring.domain.service;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import plaform.pillmate_spring.config.S3Config;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Config s3Config;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // local
    private String localLocation = "C:\\s3localmage\\";

    public String imageUpload(MultipartRequest request) throws IOException {
        MultipartFile file = request.getFile("upload");
        String filename = file.getOriginalFilename();
        String ext = filename.substring(filename.indexOf("."));
        String uuidFileName = UUID.randomUUID() + ext;
        String localPath = localLocation + uuidFileName;
        File localFile = new File(localPath);
        file.transferTo(localFile);

        s3Config.amazonS3Client().putObject(new PutObjectRequest(bucket, uuidFileName, localFile).withCannedAcl(CannedAccessControlList.PublicRead));
        // s3에 저장된 파일 url 가져오기
        String s3Url = s3Config.amazonS3Client().getUrl(bucket, uuidFileName).toString();
        localFile.delete();
        return s3Url;
    }
}
