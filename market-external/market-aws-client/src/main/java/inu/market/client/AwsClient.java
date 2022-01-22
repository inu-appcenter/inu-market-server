package inu.market.client;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import inu.market.common.NetworkException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AwsClient {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    public String upload(MultipartFile image) {
        String fileName = createFileName(image.getContentType());

        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, image.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (Exception e) {
            throw new NetworkException("파일 업로드 중 오류가 발생했습니다.");
        }
    }

    private String createFileName(String contentType) {
        return UUID.randomUUID().toString() + "." + extractExt(contentType);
    }

    private String extractExt(String contentType) {
        int pos = contentType.lastIndexOf("/");
        return contentType.substring(pos + 1);
    }

}
