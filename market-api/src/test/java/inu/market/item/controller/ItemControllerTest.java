package inu.market.item.controller;

import inu.market.ControllerTest;
import inu.market.item.dto.ItemResponse;
import inu.market.item.service.ItemService;
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

import static inu.market.CommonFixture.*;
import static inu.market.item.ItemFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest extends ControllerTest {

    @MockBean
    private ItemService itemService;

    @Test
    @DisplayName("상품 이미지 생성 API")
    void convertToImageUrls() throws Exception {
        // given
        Map<String, List<String>> result = new HashMap<>();
        result.put("imageUrls", Arrays.asList(TEST_IMAGE_URL));

        given(itemService.uploadImages(any()))
                .willReturn(Arrays.asList(TEST_IMAGE_URL));

        // when
        mockMvc.perform(multipart("/api/items/imageUrls").file(TEST_IMAGES_FILE)
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andDo(document("item/imageUrls",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        requestParts(
                                partWithName("images").description("이미지 파일")
                        ),
                        responseFields(
                                fieldWithPath("imageUrls").type(JsonFieldType.ARRAY).description("이미지 URL 리스트")
                        )
                ));

        // then
        then(itemService).should(times(1)).uploadImages(any());
    }

    @Test
    @DisplayName("상품 생성 API")
    void create() throws Exception {
        // given
        Map<String, Long> response = new HashMap<>();
        response.put("itemId", TEST_ITEM.getId());

        given(itemService.create(any(), any()))
                .willReturn(TEST_ITEM.getId());
        // when
        mockMvc.perform(post("/api/items")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TEST_ITEM_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)))
                .andDo(document("item/create",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("상품 제목"),
                                fieldWithPath("contents").type(JsonFieldType.STRING).description("상품 설명"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                fieldWithPath("majorId").type(JsonFieldType.NUMBER).description("학과 ID"),
                                fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                                fieldWithPath("imageUrls").type(JsonFieldType.ARRAY).description("상품 이미지 리스트")
                        ),
                        responseFields(
                                fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("상품 ID")
                        )
                ));

        // then
        then(itemService).should(times(1)).create(any(), any());
    }

    @Test
    @DisplayName("상품 수정 API")
    void update() throws Exception {
        // given
        willDoNothing()
                .given(itemService)
                .update(any(), any(), any());

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/items/{itemId}", TEST_ITEM.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TEST_ITEM_REQUEST)))
                .andExpect(status().isOk())
                .andDo(document("item/update",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("itemId").description("상품 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("상품 제목"),
                                fieldWithPath("contents").type(JsonFieldType.STRING).description("상품 설명"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                fieldWithPath("majorId").type(JsonFieldType.NUMBER).description("학과 ID"),
                                fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                                fieldWithPath("imageUrls").type(JsonFieldType.ARRAY).description("상품 이미지 리스트")
                        )
                ));

        // then
        then(itemService).should(times(1)).update(any(), any(), any());
    }

    @Test
    @DisplayName("상품 상태 수정 API")
    void updateStatus() throws Exception {
        // given
        willDoNothing()
                .given(itemService)
                .updateStatus(any(), any(), any());

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/items/{itemId}", TEST_ITEM.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TEST_ITEM_UPDATE_STATUS_REQUEST)))
                .andExpect(status().isOk())
                .andDo(document("item/updateStatus",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("itemId").description("상품 ID")
                        ),
                        requestFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("상품 상태 판매중/예약중/판매완료")
                        )
                ));

        // then
        then(itemService).should(times(1)).updateStatus(any(), any(), any());
    }

    @Test
    @DisplayName("상품 삭제 API")
    void delete() throws Exception {
        // given
        willDoNothing()
                .given(itemService)
                .delete(any(), any());

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/items/{itemId}", TEST_ITEM.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION))
                .andExpect(status().isOk())
                .andDo(document("item/delete",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("itemId").description("상품 ID")
                        )
                ));

        // then
        then(itemService).should(times(1)).delete(any(), any());
    }

    @Test
    @DisplayName("상품 상세 조회 API")
    void findById() throws Exception {
        given(itemService.findById(any(), any()))
                .willReturn(TEST_ITEM_RESPONSE);

        // when
        mockMvc.perform(get("/api/items/{itemId}", TEST_ITEM.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(TEST_ITEM_RESPONSE)))
                .andDo(document("item/findById",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("itemId").description("상품 ID")
                        ),
                        responseFields(
                                fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("상품 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("상품 제목"),
                                fieldWithPath("contents").type(JsonFieldType.STRING).description("상품 설명"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                fieldWithPath("favoriteCount").type(JsonFieldType.NUMBER).description("상품 찜 회수"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("상품 상태 판매중/예약중/판매완료"),
                                fieldWithPath("favorite").type(JsonFieldType.BOOLEAN).description("상품 좋아요 여부"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("상품 등록 시간"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("상품 수정 시간"),
                                fieldWithPath("major.majorId").type(JsonFieldType.NUMBER).description("학과 ID"),
                                fieldWithPath("major.name").type(JsonFieldType.STRING).description("학과 이름"),
                                fieldWithPath("category.categoryId").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                                fieldWithPath("category.name").type(JsonFieldType.STRING).description("카테고리 이름"),
                                fieldWithPath("category.iconUrl").type(JsonFieldType.STRING).description("카테고리 아이콘 URL"),
                                fieldWithPath("seller.userId").type(JsonFieldType.NUMBER).description("회원 ID"),
                                fieldWithPath("seller.nickName").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("seller.imageUrl").type(JsonFieldType.STRING).description("이미지 URL"),
                                fieldWithPath("seller.score").type(JsonFieldType.NUMBER).description("점수"),
                                fieldWithPath("seller.pushToken").type(JsonFieldType.STRING).description("pushToken"),
                                fieldWithPath("imageUrls").type(JsonFieldType.ARRAY).description("상품 이미지 URL 리스트")
                        )));

        // then
        then(itemService).should(times(1)).findById(any(), any());
    }

    @Test
    @DisplayName("상품 검색 API")
    void findBySearchRequest() throws Exception {
        // given
        List<ItemResponse> result = Arrays.asList(TEST_ITEM_SIMPLE_RESPONSE);
        given(itemService.findBySearchRequest(any(), any()))
                .willReturn(result);

        // when
        mockMvc.perform(get("/api/items")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("size", "10")
                        .param("itemId", "1000")
                        .param("categoryId", "10")
                        .param("majorId", "10")
                        .param("searchWord", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andDo(document("item/findBySearch",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        requestParameters(
                                parameterWithName("size").description("페이지 사이즈"),
                                parameterWithName("itemId").description("마지막으로 받은 상품 ID"),
                                parameterWithName("categoryId").description("카테고리 ID"),
                                parameterWithName("majorId").description("학과 ID"),
                                parameterWithName("searchWord").description("검색어")
                        ),
                        responseFields(
                                fieldWithPath("[].itemId").type(JsonFieldType.NUMBER).description("상품 ID"),
                                fieldWithPath("[].title").type(JsonFieldType.STRING).description("상품 제목"),
                                fieldWithPath("[].mainImageUrl").type(JsonFieldType.STRING).description("상품 대표 이미지 URL"),
                                fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                fieldWithPath("[].favoriteCount").type(JsonFieldType.NUMBER).description("상품 찜 회수"),
                                fieldWithPath("[].status").type(JsonFieldType.STRING).description("상품 상태 판매중/예약중/판매완료"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("상품 등록 시간"),
                                fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("상품 수정 시간")
                        )
                ));

        // then
        then(itemService).should(times(1)).findBySearchRequest(any(), any());
    }

    @Test
    @DisplayName("판매자의 상품 조회 API")
    void findBySeller() throws Exception {
        // given
        List<ItemResponse> result = Arrays.asList(TEST_ITEM_SIMPLE_RESPONSE);
        given(itemService.findBySeller(any()))
                .willReturn(result);

        // when
        mockMvc.perform(get("/api/users/items")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andDo(document("item/findBySeller",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        responseFields(
                                fieldWithPath("[].itemId").type(JsonFieldType.NUMBER).description("상품 ID"),
                                fieldWithPath("[].title").type(JsonFieldType.STRING).description("상품 제목"),
                                fieldWithPath("[].mainImageUrl").type(JsonFieldType.STRING).description("상품 대표 이미지 URL"),
                                fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                fieldWithPath("[].favoriteCount").type(JsonFieldType.NUMBER).description("상품 찜 회수"),
                                fieldWithPath("[].status").type(JsonFieldType.STRING).description("상품 상태 판매중/예약중/판매완료"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("상품 등록 시간"),
                                fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("상품 수정 시간")
                        )
                ));

        // then
        then(itemService).should(times(1)).findBySeller(any());
    }
}