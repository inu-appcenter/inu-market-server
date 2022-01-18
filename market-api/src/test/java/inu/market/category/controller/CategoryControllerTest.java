package inu.market.category.controller;

import inu.market.ControllerTest;
import inu.market.category.dto.CategoryResponse;
import inu.market.category.service.CategoryService;
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
import static inu.market.category.CategoryFixture.*;
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

@WebMvcTest(CategoryController.class)
class CategoryControllerTest extends ControllerTest {

    @MockBean
    private CategoryService categoryService;

    @Test
    @DisplayName("카테고리 아이콘 생성 API")
    void convertToIconUrl() throws Exception {
        // given
        Map<String, String> result = new HashMap<>();
        result.put("iconUrl", TEST_CATEGORY_ICON_URL);

        given(categoryService.uploadImage(any()))
                .willReturn(TEST_CATEGORY_ICON_URL);

        // when
        mockMvc.perform(multipart("/api/categories/iconUrls").file(TEST_IMAGE_FILE)
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andDo(document("category/iconUrls",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        requestParts(
                                partWithName("image").description("카테고리 아이콘 이미지")
                        ),
                        responseFields(
                                fieldWithPath("iconUrl").type(JsonFieldType.STRING).description("아이콘 URL")
                        )
                ));

        // then
        then(categoryService).should(times(1)).uploadImage(any());
    }

    @Test
    @DisplayName("카테고리 아이콘 생성 API 예외")
    void convertToIconUrlNotExist() throws Exception {
        // when
        mockMvc.perform(multipart("/api/categories/iconUrls").file(TEST_EMPTY_IMAGE_FILE)
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("카테고리 생성 API")
    void create() throws Exception {
        // given
        Map<String, Long> response = new HashMap<>();
        response.put("categoryId", TEST_CATEGORY.getId());

        given(categoryService.create(any()))
                .willReturn(TEST_CATEGORY.getId());
        // when
        mockMvc.perform(post("/api/categories")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TEST_CATEGORY_CREATE_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)))
                .andDo(document("category/create",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("카테고리 이름"),
                                fieldWithPath("iconUrl").type(JsonFieldType.STRING).description("카테고리 아이콘 URL")
                        ),
                        responseFields(
                                fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("카테고리 ID")
                        )
                ));

        // then
        then(categoryService).should(times(1)).create(any());
    }

    @Test
    @DisplayName("카테고리 수정 API")
    void update() throws Exception {
        // given
        willDoNothing()
                .given(categoryService)
                .update(any(), any());
        // when
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/categories/{categoryId}", TEST_CATEGORY.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TEST_CATEGORY_UPDATE_REQUEST)))
                .andExpect(status().isOk())
                .andDo(document("category/update",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("categoryId").description("카테고리 ID")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("카테고리 이름"),
                                fieldWithPath("iconUrl").type(JsonFieldType.STRING).description("카테고리 아이콘 URL")
                        )
                ));

        // then
        then(categoryService).should(times(1)).update(any(), any());
    }

    @Test
    @DisplayName("카테고리 삭제 API")
    void delete() throws Exception {
        // given
        willDoNothing()
                .given(categoryService)
                .delete(any());

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/categories/{categoryId}", TEST_CATEGORY.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION))
                .andExpect(status().isOk())
                .andDo(document("category/delete",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("categoryId").description("카테고리 ID")
                        )
                ));

        // then
        then(categoryService).should(times(1)).delete(any());
    }

    @Test
    @DisplayName("카테고리 리스트 조회 API")
    void findAll() throws Exception {
        // given
        List<CategoryResponse> result = Arrays.asList(TEST_CATEGORY_RESPONSE);
        given(categoryService.findAll())
                .willReturn(result);

        // when
        mockMvc.perform(get("/api/categories")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andDo(document("category/findAll",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        responseFields(
                                fieldWithPath("[].categoryId").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("카테고리 이름"),
                                fieldWithPath("[].iconUrl").type(JsonFieldType.STRING).description("카테고리 아이콘 URL")
                        )
                ));

        // then
        then(categoryService).should(times(1)).findAll();
    }
}