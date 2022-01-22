package inu.market.message.controller;

import inu.market.ControllerTest;
import inu.market.message.dto.MessageResponse;
import inu.market.message.service.MessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static inu.market.CommonFixture.*;
import static inu.market.chatroom.ChatRoomFixture.TEST_CHAT_ROOM;
import static inu.market.message.MessageFixture.TEST_MESSAGE_RESPONSE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
class MessageControllerTest extends ControllerTest {

    @MockBean
    private MessageService messageService;

    @MockBean
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Test
    @DisplayName("채팅 이미지 업로드 API")
    void convertToImageUrl() throws Exception {
        // given
        Map<String, String> result = new HashMap<>();
        result.put("imageUrl", TEST_IMAGE_URL);

        given(messageService.uploadImage(any()))
                .willReturn(TEST_IMAGE_URL);

        // when
        mockMvc.perform(multipart("/api/messages/imageUrls").file(TEST_IMAGE_FILE)
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andDo(document("message/imageUrls",
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
        then(messageService).should(times(1)).uploadImage(any());
    }

    @Test
    @DisplayName("메세지 이미지 URL 생성 API 예외")
    void convertToImageUrlNotExist() throws Exception {
        // when
        mockMvc.perform(multipart("/api/messages/imageUrls").file(TEST_EMPTY_IMAGE_FILE)
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("마지막 채팀 메세지 조회 API")
    void findLastByRoomId() throws Exception {
        // given
        given(messageService.findLastByRoomId(any()))
                .willReturn(TEST_MESSAGE_RESPONSE);

        // when
        mockMvc.perform(get("/api/chat/rooms/{roomId}/messages/new", TEST_CHAT_ROOM.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(TEST_MESSAGE_RESPONSE)))
                .andDo(document("message/findLast",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("roomId").description("채팅방 ID")
                        ),
                        responseFields(
                                fieldWithPath("senderId").type(JsonFieldType.NUMBER).description("회원 ID"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("메세지 내용"),
                                fieldWithPath("messageType").type(JsonFieldType.STRING).description("메세지 타입 문자/이미지"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("메세지 생성 시간")
                        )
                ));

        // then
        then(messageService).should(times(1)).findLastByRoomId(any());
    }

    @Test
    @DisplayName("채팅 메세지 조회 API")
    void findByRoomId() throws Exception {
        // given
        List<MessageResponse> result = Arrays.asList(TEST_MESSAGE_RESPONSE);
        given(messageService.findByRoomId(any(), any(), any()))
                .willReturn(result);

        // when
        mockMvc.perform(get("/api/chat/rooms/{roomId}/messages", TEST_CHAT_ROOM.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("size", "10")
                        .param("lastMessageDate", LocalDateTime.now().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andDo(document("message/find",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("roomId").description("채팅방 ID")
                        ),
                        requestParameters(
                                parameterWithName("size").description("페이지 사이즈"),
                                parameterWithName("lastMessageDate").description("마지막 메세지 시간")
                        ),
                        responseFields(
                                fieldWithPath("[].senderId").type(JsonFieldType.NUMBER).description("회원 ID"),
                                fieldWithPath("[].nickName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING).description("메세지 내용"),
                                fieldWithPath("[].messageType").type(JsonFieldType.STRING).description("메세지 타입 문자/이미지"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("메세지 생성 시간")
                        )
                ));

        // then
        then(messageService).should(times(1)).findByRoomId(any(), any(), any());
    }

}