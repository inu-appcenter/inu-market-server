package inu.market;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

public class CommonFixture {

    public static final String TEST_AUTHORIZATION = "Bearer **";
    public static  final String TEST_IMAGE_URL = "이미지 URL";
    public static final Integer TEST_SIZE = 10;

    public static final MockMultipartFile TEST_IMAGE_FILE = new MockMultipartFile(
            "image", "이미지.png",
            MediaType.IMAGE_PNG_VALUE, "<<image>>".getBytes(StandardCharsets.UTF_8));

    public static final MockMultipartFile TEST_IMAGES_FILE = new MockMultipartFile(
            "images", "이미지.png",
            MediaType.IMAGE_PNG_VALUE, "<<image>>".getBytes(StandardCharsets.UTF_8));

    public static final MockMultipartFile TEST_EMPTY_IMAGE_FILE = new MockMultipartFile(
            "image", "이미지.png",
            MediaType.IMAGE_PNG_VALUE, "".getBytes(StandardCharsets.UTF_8));
}
