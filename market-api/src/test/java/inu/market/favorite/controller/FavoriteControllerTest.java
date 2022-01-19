package inu.market.favorite.controller;

import inu.market.ControllerTest;
import inu.market.favorite.service.FavoriteService;
import inu.market.item.dto.ItemResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Arrays;
import java.util.List;

import static inu.market.CommonFixture.TEST_AUTHORIZATION;
import static inu.market.favorite.FavoriteFixture.TEST_FAVORITE_CREATE_REQUEST;
import static inu.market.favorite.FavoriteFixture.TEST_FAVORITE_DELETE_REQUEST;
import static inu.market.item.ItemFixture.TEST_ITEM_SIMPLE_RESPONSE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FavoriteController.class)
class FavoriteControllerTest extends ControllerTest {

    @MockBean
    private FavoriteService favoriteService;

    @Test
    @DisplayName("찜 생성 API")
    void create() throws Exception {
        // given
        willDoNothing()
                .given(favoriteService)
                .create(any(), any());

        // when
        mockMvc.perform(post("/api/favorites")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TEST_FAVORITE_CREATE_REQUEST)))
                .andExpect(status().isOk())
                .andDo(document("favorite/create",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("상품 ID")
                        )
                ));

        // then
        then(favoriteService).should(times(1)).create(any(), any());
    }

    @Test
    @DisplayName("찜 삭제 API")
    void delete() throws Exception {
        // given
        willDoNothing()
                .given(favoriteService)
                .delete(any(), any());

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/favorites")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TEST_FAVORITE_DELETE_REQUEST)))
                .andExpect(status().isOk())
                .andDo(document("favorite/delete",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("상품 ID")
                        )
                ));

        // then
        then(favoriteService).should(times(1)).delete(any(), any());
    }

    @Test
    @DisplayName("찜 목록 조회 API")
    void findByUserId() throws Exception {
        // given
        List<ItemResponse> response = Arrays.asList(TEST_ITEM_SIMPLE_RESPONSE);
        given(favoriteService.findByUserId(any()))
                .willReturn(response);

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/favorites")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)))
                .andDo(document("favorite/find",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        responseFields(
                                fieldWithPath("[].itemId").type(JsonFieldType.NUMBER).description("상품 ID"),
                                fieldWithPath("[].title").type(JsonFieldType.STRING).description("상품 제목"),
                                fieldWithPath("[].mainImageUrl").type(JsonFieldType.STRING).description("상품 대표 이미지 URL"),
                                fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                fieldWithPath("[].favoriteCount").type(JsonFieldType.NUMBER).description("상품 찜 회수"),
                                fieldWithPath("[].status").type(JsonFieldType.STRING).description("상품 상태"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("상품 등록 시간"),
                                fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("상품 수정 시간")
                        )
                ));

        // then
        then(favoriteService).should(times(1)).findByUserId(any());
    }
}