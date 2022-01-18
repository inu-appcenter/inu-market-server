package inu.market.major.controller;

import inu.market.ControllerTest;
import inu.market.major.dto.MajorResponse;
import inu.market.major.service.MajorService;
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
import static inu.market.major.MajorFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MajorController.class)
class MajorControllerTest extends ControllerTest {

    @MockBean
    private MajorService majorService;

    @Test
    @DisplayName("단과대학 생성 API")
    void createParent() throws Exception {
        // given
        Map<String, Long> response = new HashMap<>();
        response.put("majorId", TEST_PARENT_MAJOR.getId());

        given(majorService.createParent(any()))
                .willReturn(TEST_PARENT_MAJOR.getId());

        // when
        mockMvc.perform(post("/api/majors/parents")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TEST_MAJOR_CREATE_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)))
                .andDo(document("major/parentCreate",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("단과대학 이름")
                        ),
                        responseFields(
                                fieldWithPath("majorId").type(JsonFieldType.NUMBER).description("단과대학(학과) ID")
                        )
                ));

        // then
        then(majorService).should(times(1)).createParent(any());
    }

    @Test
    @DisplayName("학과 생성 API")
    void createChild() throws Exception {
        // given
        Map<String, Long> response = new HashMap<>();
        response.put("majorId", TEST_MAJOR.getId());

        given(majorService.createChildren(any(), any()))
                .willReturn(TEST_MAJOR.getId());

        // when
        mockMvc.perform(post("/api/majors/{majorId}/children", TEST_PARENT_MAJOR.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TEST_MAJOR_CREATE_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)))
                .andDo(document("major/childrenCreate",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("majorId").description("단과대학(학과) ID")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("학과 이름")
                        ),
                        responseFields(
                                fieldWithPath("majorId").type(JsonFieldType.NUMBER).description("학과 ID")
                        )
                ));

        // then
        then(majorService).should(times(1)).createChildren(any(), any());
    }

    @Test
    @DisplayName("학과 수정 API")
    void update() throws Exception {
        // given
        willDoNothing()
                .given(majorService)
                .update(any(), any());

        // when
        mockMvc.perform(put("/api/majors/{majorId}", TEST_MAJOR.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TEST_MAJOR_UPDATE_REQUEST)))
                .andExpect(status().isOk())
                .andDo(document("major/update",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("majorId").description("단과대학(학과) ID")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("학과 이름")
                        )
                ));

        // then
        then(majorService).should(times(1)).update(any(), any());
    }

    @Test
    @DisplayName("학과 삭제 API")
    void delete() throws Exception {
        // given
        willDoNothing()
                .given(majorService)
                .delete(any());

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/majors/{majorId}", TEST_MAJOR.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION))
                .andExpect(status().isOk())
                .andDo(document("major/delete",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("majorId").description("단과대학(학과) ID")
                        )
                ));

        // then
        then(majorService).should(times(1)).delete(any());
    }

    @Test
    @DisplayName("단과대학 리스트 조회 API")
    void findParents() throws Exception {
        // given
        List<MajorResponse> result = Arrays.asList(TEST_MAJOR_RESPONSE);
        given(majorService.findParents())
                .willReturn(result);

        // when
        mockMvc.perform(get("/api/majors/parents")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andDo(document("major/findParents",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        responseFields(
                                fieldWithPath("[].majorId").type(JsonFieldType.NUMBER).description("던과대학 ID"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("단과대학 이름")
                        )
                ));

        // then
        then(majorService).should(times(1)).findParents();
    }

    @Test
    @DisplayName("학과 리스트 조회 API")
    void findChildrenById() throws Exception {
        // given
        List<MajorResponse> result = Arrays.asList(TEST_MAJOR_RESPONSE);
        given(majorService.findChildrenById(any()))
                .willReturn(result);

        // when
        mockMvc.perform(get("/api/majors/{majorId}/children", TEST_PARENT_MAJOR.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andDo(document("major/findChildren",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("majorId").description("단과대학 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].majorId").type(JsonFieldType.NUMBER).description("학과 ID"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("학과 이름")
                        )
                ));

        // then
        then(majorService).should(times(1)).findChildrenById(any());
    }
}