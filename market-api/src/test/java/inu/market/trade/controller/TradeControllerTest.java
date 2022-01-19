package inu.market.trade.controller;

import inu.market.ControllerTest;
import inu.market.item.dto.ItemResponse;
import inu.market.trade.service.TradeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Arrays;
import java.util.List;

import static inu.market.CommonFixture.TEST_AUTHORIZATION;
import static inu.market.item.ItemFixture.TEST_ITEM_SIMPLE_RESPONSE;
import static inu.market.trade.TradeFixture.TEST_TRADE_CREATE_REQUEST;
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
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TradeController.class)
class TradeControllerTest extends ControllerTest {

    @MockBean
    private TradeService tradeService;

    @Test
    @DisplayName("거래 생성 API")
    void create() throws Exception {
        // given
        willDoNothing()
                .given(tradeService)
                .create(any(), any());

        // when
        mockMvc.perform(post("/api/trades")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TEST_TRADE_CREATE_REQUEST)))
                .andExpect(status().isOk())
                .andDo(document("trade/create",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("상품 구매자 ID"),
                                fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("상품 ID")
                        )
                ));

        // then
        then(tradeService).should(times(1)).create(any(), any());
    }

    @Test
    @DisplayName("구매한 상품 조회 API")
    void findByBuyerId() throws Exception {
        // given
        List<ItemResponse> result = Arrays.asList(TEST_ITEM_SIMPLE_RESPONSE);
        given(tradeService.findByBuyerId(any()))
                .willReturn(result);

        // when
        mockMvc.perform(get("/api/trades")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andDo(document("trade/find",
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
        then(tradeService).should(times(1)).findByBuyerId(any());
    }
}