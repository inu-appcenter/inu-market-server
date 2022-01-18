package inu.market.block.controller;

import inu.market.ControllerTest;
import inu.market.block.dto.BlockResponse;
import inu.market.block.service.BlockService;
import inu.market.category.dto.CategoryResponse;
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
import static inu.market.block.BlockFixture.*;
import static inu.market.category.CategoryFixture.TEST_CATEGORY_RESPONSE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BlockController.class)
class BlockControllerTest extends ControllerTest {

    @MockBean
    private BlockService blockService;

    @Test
    @DisplayName("차단 생성 API")
    void create() throws Exception {
        // given
        willDoNothing()
                .given(blockService)
                .create(any(), any());

        // when
        mockMvc.perform(post("/api/blocks")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TEST_BLOCK_CREATE_REQUEST)))
                .andExpect(status().isOk())
                .andDo(document("block/create",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("차단 회원 ID")
                        )
                ));

        // then
        then(blockService).should(times(1)).create(any(), any());
    }

    @Test
    @DisplayName("차단 해제 API")
    void delete() throws Exception {
        // given
        willDoNothing()
                .given(blockService)
                .delete(any(), any());

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/blocks/{blockId}", TEST_BLOCK.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION))
                .andExpect(status().isOk())
                .andDo(document("block/delete",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("blockId").description("차단 ID")
                        )
                ));

        // then
        then(blockService).should(times(1)).delete(any(), any());
    }

    @Test
    @DisplayName("차단 리스트 조회 API")
    void findByUserId() throws Exception {
        // given
        List<BlockResponse> result = Arrays.asList(TEST_BLOCK_RESPONSE);
        given(blockService.findByUserId(any()))
                .willReturn(result);

        // when
        mockMvc.perform(get("/api/blocks")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andDo(document("block/find",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        responseFields(
                                fieldWithPath("[].blockId").type(JsonFieldType.NUMBER).description("차단 ID"),
                                fieldWithPath("[].target.userId").type(JsonFieldType.NUMBER).description("차단 회원 ID"),
                                fieldWithPath("[].target.nickName").type(JsonFieldType.STRING).description("차단 회원 닉네임"),
                                fieldWithPath("[].target.imageUrl").type(JsonFieldType.STRING).description("차단 회원 이미지 URL"),
                                fieldWithPath("[].target.score").type(JsonFieldType.NUMBER).description("차단 회원 점수"),
                                fieldWithPath("[].target.pushToken").type(JsonFieldType.STRING).description("차단 회원 pushToken")
                        )
                ));

        // then
        then(blockService).should(times(1)).findByUserId(any());
    }
}