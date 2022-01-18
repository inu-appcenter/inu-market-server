package inu.market.notice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import inu.market.ControllerTest;
import inu.market.category.dto.CategoryResponse;
import inu.market.notice.NoticeFixture;
import inu.market.notice.dto.NoticeResponse;
import inu.market.notice.service.NoticeService;
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
import static inu.market.category.CategoryFixture.*;
import static inu.market.notice.NoticeFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoticeController.class)
class NoticeControllerTest extends ControllerTest {

    @MockBean
    private NoticeService noticeService;

    @Test
    @DisplayName("공지사항 생성 API")
    void create() throws Exception {
        // given
        Map<String, Long> response = new HashMap<>();
        response.put("noticeId", TEST_NOTICE.getId());

        given(noticeService.create(any()))
                .willReturn(TEST_NOTICE.getId());
        // when
        mockMvc.perform(post("/api/notices")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TEST_NOTICE_CREATE_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)))
                .andDo(document("notice/create",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("공지사항 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("공지사항 내용")
                        ),
                        responseFields(
                                fieldWithPath("noticeId").type(JsonFieldType.NUMBER).description("공지사항 ID")
                        )
                ));

        // then
        then(noticeService).should(times(1)).create(any());
    }

    @Test
    @DisplayName("공지사항 수정 API")
    void update() throws Exception {
        // given
        willDoNothing()
                .given(noticeService)
                .update(any(), any());

        // when
        mockMvc.perform(put("/api/notices/{noticeId}", TEST_NOTICE.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TEST_NOTICE_UPDATE_REQUEST)))
                .andExpect(status().isOk())
                .andDo(document("notice/update",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("noticeId").description("공지사항 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("공지사항 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("공지사항 내용")
                        )
                ));

        // then
        then(noticeService).should(times(1)).update(any(), any());
    }

    @Test
    @DisplayName("공지사항 삭제 API")
    void delete() throws Exception {
        // given
        willDoNothing()
                .given(noticeService)
                .delete(any());

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/notices/{noticeId}", TEST_NOTICE.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION))
                .andExpect(status().isOk())
                .andDo(document("notice/delete",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("noticeId").description("공지사항 ID")
                        )
                ));

        // then
        then(noticeService).should(times(1)).delete(any());
    }

    @Test
    @DisplayName("공지사항 리스트 조회 API")
    void findAll() throws Exception {
        List<NoticeResponse> result = Arrays.asList(TEST_NOTICE_SIMPLE_RESPONSE);
        given(noticeService.findAll())
                .willReturn(result);

        // when
        mockMvc.perform(get("/api/notices")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andDo(document("notice/findAll",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        responseFields(
                                fieldWithPath("[].noticeId").type(JsonFieldType.NUMBER).description("공지사항 ID"),
                                fieldWithPath("[].title").type(JsonFieldType.STRING).description("공지사항 제목"),
                                fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("공지사항 수정 날짜")
                        )
                ));

        // then
        then(noticeService).should(times(1)).findAll();
    }

    @Test
    @DisplayName("공지사항 상세 조회 API")
    void findById() throws Exception {
        given(noticeService.findById(any()))
                .willReturn(TEST_NOTICE_RESPONSE);

        // when
        mockMvc.perform(get("/api/notices/{noticeId}", TEST_NOTICE.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(TEST_NOTICE_RESPONSE)))
                .andDo(document("notice/findById",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("noticeId").description("공지사항 ID")
                        ),
                        responseFields(
                                fieldWithPath("noticeId").type(JsonFieldType.NUMBER).description("공지사항 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("공지사항 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("공지사항 내용"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("공지사항 수정 날짜")
                        )
                ));

        // then
        then(noticeService).should(times(1)).findById(any());
    }
}