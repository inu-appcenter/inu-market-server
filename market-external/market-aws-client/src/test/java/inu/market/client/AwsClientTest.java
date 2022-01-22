package inu.market.client;

import com.amazonaws.services.s3.AmazonS3Client;
import inu.market.common.NetworkException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AwsClientTest {

    @InjectMocks
    private AwsClient awsClient;

    @Mock
    private AmazonS3Client amazonS3Client;

    @Test
    @DisplayName("이미지를 업로드한다.")
    void upload() throws MalformedURLException {
        // given
        MockMultipartFile image = new MockMultipartFile("image", "이미지.png",
                MediaType.IMAGE_PNG_VALUE, "<<image>>".getBytes(StandardCharsets.UTF_8));
        given(amazonS3Client.putObject(any()))
                .willReturn(null);

        given(amazonS3Client.getUrl(any(), any()))
                .willReturn(new URL("http:이미지 URL"));

        // when
        String result = awsClient.upload(image);

        // then
        assertThat(result).isEqualTo("http:이미지 URL");
        then(amazonS3Client).should(times(1)).putObject(any());
        then(amazonS3Client).should(times(1)).getUrl(any(), any());
    }

    @Test
    @DisplayName("이미지 업로드 중 예외가 발생한다.")
    void uploadNetworkException() throws MalformedURLException {
        // given
        MockMultipartFile image = new MockMultipartFile("image", "이미지.png",
                MediaType.IMAGE_PNG_VALUE, "<<image>>".getBytes(StandardCharsets.UTF_8));

        willThrow(new NetworkException(""))
                .given(amazonS3Client)
                .putObject(any());

        // when
        assertThrows(NetworkException.class, () -> awsClient.upload(image));

        // then
        then(amazonS3Client).should(times(1)).putObject(any());
    }
}