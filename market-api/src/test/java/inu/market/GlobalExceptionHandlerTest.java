package inu.market;

import inu.market.common.DuplicateException;
import inu.market.item.controller.ItemController;
import inu.market.item.service.ItemService;
import inu.market.major.controller.MajorController;
import inu.market.major.dto.MajorCreateRequest;
import inu.market.major.service.MajorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.access.AccessDeniedException;

import static inu.market.CommonFixture.TEST_AUTHORIZATION;
import static inu.market.item.ItemFixture.TEST_ITEM;
import static inu.market.item.ItemFixture.TEST_ITEM_UPDATE_STATUS_REQUEST;
import static inu.market.major.MajorFixture.TEST_MAJOR;
import static inu.market.major.MajorFixture.TEST_MAJOR_UPDATE_REQUEST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({MajorController.class, ItemController.class})
class GlobalExceptionHandlerTest extends ControllerTest {

    @MockBean
    private MajorService majorService;

    @MockBean
    private ItemService itemService;

    @Test
    @DisplayName("???????????? ????????? ?????? ?????? ?????????")
    void handleValidationExceptions() throws Exception {
        // given
        MajorCreateRequest request = new MajorCreateRequest("");

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/majors/parents")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andDo(document("exception/MethodArgumentNotValidException",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("???????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("attributes").type(JsonFieldType.OBJECT).description("?????? ????????? ?????? ??????"),
                                fieldWithPath("attributes.name").type(JsonFieldType.STRING).description("?????? ????????? ?????????")
                        )));
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????? ?????????")
    void HandleValidationBindExceptions() throws Exception {
        // when
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/items")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andDo(document("exception/BindException",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("attributes").type(JsonFieldType.OBJECT).description("?????? ????????? ?????? ??????"),
                                fieldWithPath("attributes.size").type(JsonFieldType.STRING).description("?????? ????????? ?????????")
                        )));
    }

    @Test
    @DisplayName("????????? ?????? ????????? ?????? ?????? ?????????")
    void handleAccessDeniedExceptions() throws Exception {
        // given
        willThrow(new AccessDeniedException("????????? ????????????."))
                .given(itemService)
                .updateStatus(any(), any(), any());

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/items/{itemId}", TEST_ITEM.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TEST_ITEM_UPDATE_STATUS_REQUEST)))
                .andExpect(status().isForbidden())
                .andDo(document("exception/AccessDeniedException",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("itemId").description("?????? ID")
                        ),
                        requestFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("attributes").type(JsonFieldType.NULL).description("??? X Null")
                        )
                ));

        // then
        then(itemService).should(times(1)).updateStatus(any(), any(), any());
    }

    @Test
    @DisplayName("??????/??????/?????????/??????????????? ?????? ?????? ?????? ?????????")
    void handleBadRequestExceptions() throws Exception {
        // given
        willThrow(new DuplicateException(TEST_MAJOR_UPDATE_REQUEST.getName() + "??? ?????? ???????????? ???????????????."))
                .given(majorService)
                .update(any(), any());

        // when
        mockMvc.perform(put("/api/majors/{majorId}", TEST_MAJOR.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TEST_MAJOR_UPDATE_REQUEST)))
                .andExpect(status().is4xxClientError())
                .andDo(document("exception/BadRequestException",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("majorId").description("????????????(??????) ID")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("attributes").type(JsonFieldType.NULL).description("??? X Null")
                        )
                ));

        // then
        then(majorService).should(times(1)).update(any(), any());
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????????")
    void handleException() throws Exception {
        // given
        willThrow(new RuntimeException(TEST_MAJOR_UPDATE_REQUEST.getName() + "??? ?????? ???????????? ???????????????."))
                .given(majorService)
                .update(any(), any());

        // when
        mockMvc.perform(put("/api/majors/{majorId}", TEST_MAJOR.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TEST_MAJOR_UPDATE_REQUEST)))
                .andExpect(status().is5xxServerError())
                .andDo(document("exception/Exception",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("majorId").description("????????????(??????) ID")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("attributes").type(JsonFieldType.NULL).description("??? X Null")
                        )
                ));

        // then
        then(majorService).should(times(1)).update(any(), any());
    }
}