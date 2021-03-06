package inu.market.chatroom.controller;

import inu.market.ControllerTest;
import inu.market.chatroom.dto.ChatRoomResponse;
import inu.market.chatroom.service.ChatRoomService;
import inu.market.user.UserFixture;
import inu.market.user.dto.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static inu.market.CommonFixture.TEST_AUTHORIZATION;
import static inu.market.chatroom.ChatRoomFixture.TEST_CHAT_ROOM;
import static inu.market.chatroom.ChatRoomFixture.TEST_CHAT_ROOM_RESPONSE;
import static inu.market.item.ItemFixture.TEST_ITEM;
import static inu.market.user.UserFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatRoomController.class)
class ChatRoomControllerTest extends ControllerTest {

    @MockBean
    private ChatRoomService chatRoomService;

    @Test
    @DisplayName("????????? ?????? API")
    void create() throws Exception {
        // given
        Map<String, Long> result = new HashMap<>();
        result.put("roomId", TEST_CHAT_ROOM.getId());

        given(chatRoomService.create(any(), any()))
                .willReturn(TEST_CHAT_ROOM.getId());

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/items/{itemId}/chat/rooms", TEST_ITEM.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andDo(document("chatRooms/create",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("itemId").description("?????? ID")
                        ),
                        responseFields(
                                fieldWithPath("roomId").type(JsonFieldType.NUMBER).description("????????? ID")
                        )
                ));
        // then
        then(chatRoomService).should(times(1)).create(any(), any());
    }

    @Test
    @DisplayName("????????? ?????? API")
    void findBySellerOrBuyer() throws Exception {
        // given
        List<ChatRoomResponse> result = Arrays.asList(TEST_CHAT_ROOM_RESPONSE);

        given(chatRoomService.findBySellerOrBuyer(any()))
                .willReturn(result);

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/chat/rooms")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andDo(document("chatRooms/findBySellerOrBuyer",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        responseFields(
                                fieldWithPath("[].roomId").type(JsonFieldType.NUMBER).description("????????? ID"),
                                fieldWithPath("[].item.itemId").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("[].item.title").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("[].item.mainImageUrl").type(JsonFieldType.STRING).description("?????? ?????? ????????? URL"),
                                fieldWithPath("[].item.price").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                fieldWithPath("[].item.favoriteCount").type(JsonFieldType.NUMBER).description("?????? ??? ??????"),
                                fieldWithPath("[].item.status").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("[].item.createdAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                fieldWithPath("[].item.updatedAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                fieldWithPath("[].user.userId").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("[].user.nickName").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("[].user.imageUrl").type(JsonFieldType.STRING).description("????????? URL"),
                                fieldWithPath("[].user.score").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("[].user.pushToken").type(JsonFieldType.STRING).description("pushToken")
                        )
                ));

        // then
        then(chatRoomService).should(times(1)).findBySellerOrBuyer(any());
    }

    @Test
    @DisplayName("????????? ?????? ?????? API")
    void findById() throws Exception {
        // given
        given(chatRoomService.findById(any(), any()))
                .willReturn(TEST_CHAT_ROOM_RESPONSE);

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/chat/rooms/{roomId}", TEST_CHAT_ROOM.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(TEST_CHAT_ROOM_RESPONSE)))
                .andDo(document("chatRooms/findById",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("roomId").description("????????? ID")
                        ),
                        responseFields(
                                fieldWithPath("roomId").type(JsonFieldType.NUMBER).description("????????? ID"),
                                fieldWithPath("item.itemId").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("item.title").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("item.mainImageUrl").type(JsonFieldType.STRING).description("?????? ?????? ????????? URL"),
                                fieldWithPath("item.price").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                fieldWithPath("item.favoriteCount").type(JsonFieldType.NUMBER).description("?????? ??? ??????"),
                                fieldWithPath("item.status").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("item.createdAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                fieldWithPath("item.updatedAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                fieldWithPath("user.userId").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("user.nickName").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("user.imageUrl").type(JsonFieldType.STRING).description("????????? URL"),
                                fieldWithPath("user.score").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("user.pushToken").type(JsonFieldType.STRING).description("pushToken")
                        )
                ));

        // then
        then(chatRoomService).should(times(1)).findById(any(), any());
    }

    @Test
    @DisplayName("????????? ?????? API")
    void delete() throws Exception {
        // given
        willDoNothing()
                .given(chatRoomService)
                .delete(any(), any());

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/chat/rooms/{roomId}", TEST_CHAT_ROOM.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION))
                .andExpect(status().isOk())
                .andDo(document("chatRooms/delete",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("roomId").description("????????? ID")
                        )
                ));

        // then
        then(chatRoomService).should(times(1)).delete(any(), any());
    }

    @Test
    @DisplayName("?????? ????????? ????????? ?????? API")
    void findByItemId() throws Exception {
        // given
        List<UserResponse> result = Arrays.asList(TEST_USER_RESPONSE);

        given(chatRoomService.findByItemId(any()))
                .willReturn(result);

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/items/{itemId}/chat/rooms/buyers", TEST_ITEM.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andDo(document("chatRooms/findByItemId",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("itemId").description("?????? ID")
                        ),
                        responseFields(
                                fieldWithPath("[].userId").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("[].nickName").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("[].imageUrl").type(JsonFieldType.STRING).description("????????? URL"),
                                fieldWithPath("[].score").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("[].pushToken").type(JsonFieldType.STRING).description("pushToken")
                        )
                ));

        // then
        then(chatRoomService).should(times(1)).findByItemId(any());
    }
}