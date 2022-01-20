package inu.market.user.controller;

import inu.market.ControllerTest;
import inu.market.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.HashMap;
import java.util.Map;

import static inu.market.CommonFixture.*;
import static inu.market.user.UserFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest extends ControllerTest {

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("프로필 이미지 URL 생성 API")
    void convertToImageUrl() throws Exception {
        // given
        Map<String, String> result = new HashMap<>();
        result.put("imageUrl", TEST_IMAGE_URL);

        given(userService.uploadImage(any()))
                .willReturn(TEST_IMAGE_URL);

        // when
        mockMvc.perform(multipart("/api/users/imageUrls").file(TEST_IMAGE_FILE)
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andDo(document("user/imageUrls",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        requestParts(
                                partWithName("image").description("이미지 파일")
                        ),
                        responseFields(
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("이미지 URL")
                        )
                ));

        // then
        then(userService).should(times(1)).uploadImage(any());
    }

    @Test
    @DisplayName("프로필 이미지 URL 생성 API 예외")
    void convertToImageUrlNotExist() throws Exception {
        // when
        mockMvc.perform(multipart("/api/users/imageUrls").file(TEST_EMPTY_IMAGE_FILE)
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("회원 가입 API")
    void create() throws Exception {
        // given
        given(userService.create(any()))
                .willReturn(TEST_AUTHORIZATION.substring(7));

        // when
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TEST_USER_CREATE_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION))
                .andDo(document("user/create",
                        responseHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("inuId").type(JsonFieldType.NUMBER).description("학번"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                        )
                ));
        // then
        then(userService).should(times(1)).create(any());
    }

    @Test
    @DisplayName("회원 로그인 API")
    void login() throws Exception {
        // given
        given(userService.login(any()))
                .willReturn(TEST_AUTHORIZATION.substring(7));

        // when
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TEST_USER_LOGIN_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION))
                .andDo(document("user/login",
                        responseHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("inuId").type(JsonFieldType.NUMBER).description("학번"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("pushToken").type(JsonFieldType.STRING).description("pushToken")
                        )
                ));
        // then
        then(userService).should(times(1)).login(any());
    }

    @Test
    @DisplayName("프로필 수정 API")
    void updateProfile() throws Exception {
        // given
        willDoNothing()
                .given(userService)
                .updateProfile(any(), any());

        // when
        mockMvc.perform(patch("/api/users/profile")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TEST_USER_UPDATE_PROFILE_REQUEST)))
                .andExpect(status().isOk())
                .andDo(document("user/update",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("nickName").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("이미지 URL")
                        )
                ));
        // then
        then(userService).should(times(1)).updateProfile(any(), any());
    }

    @Test
    @DisplayName("프로필 조회 API")
    void findByLoginId() throws Exception {
        // given
        given(userService.findById(any()))
                .willReturn(TEST_USER_RESPONSE);

        // when
        mockMvc.perform(get("/api/users/profile")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(TEST_USER_RESPONSE)))
                .andDo(document("user/findProfile",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        responseFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("회원 ID"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("이미지 URL"),
                                fieldWithPath("score").type(JsonFieldType.NUMBER).description("점수"),
                                fieldWithPath("pushToken").type(JsonFieldType.STRING).description("pushToken")
                        )
                ));
        // then
        then(userService).should(times(1)).findById(any());
    }
}