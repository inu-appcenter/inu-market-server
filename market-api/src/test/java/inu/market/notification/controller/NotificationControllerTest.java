package inu.market.notification.controller;

import inu.market.ControllerTest;
import inu.market.notification.dto.NotificationResponse;
import inu.market.notification.service.NotificationService;
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
import static inu.market.notification.NotificationFixture.TEST_NOTIFICATION;
import static inu.market.notification.NotificationFixture.TEST_NOTIFICATION_RESPONSE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest extends ControllerTest {

    @MockBean
    private NotificationService notificationService;

    @Test
    @DisplayName("회원 알림 조회 API")
    void findByUserId() throws Exception {
        // given
        List<NotificationResponse> result = Arrays.asList(TEST_NOTIFICATION_RESPONSE);

        given(notificationService.findByUserId(any(), any()))
                .willReturn(result);

        // when
        mockMvc.perform(get("/api/notifications")
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("notificationId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andDo(document("notification/find",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        requestParameters(
                                parameterWithName("notificationId").description("알림 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].notificationId").type(JsonFieldType.NUMBER).description("알림 ID"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING).description("알림 내용"),
                                fieldWithPath("[].read").type(JsonFieldType.BOOLEAN).description("알림 읽음 여부"),
                                fieldWithPath("[].notificationType").type(JsonFieldType.STRING).description("알림 메세지 타입"),
                                fieldWithPath("[].referenceId").type(JsonFieldType.NUMBER).description("알림 참조 ID"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("알림 생성 시간")
                        )
                ));

        // then
        then(notificationService).should(times(1)).findByUserId(any(), any());
    }

    @Test
    @DisplayName("알림 읽음 처리 API")
    void updateRead() throws Exception {
        // given
        willDoNothing()
                .given(notificationService)
                .updateRead(any(), any());

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/notifications/{notificationId}", TEST_NOTIFICATION.getId())
                        .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION))
                .andExpect(status().isOk())
                .andDo(document("notification/update",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("notificationId").description("알림 ID")
                        )
                ));

        // then
        then(notificationService).should(times(1)).updateRead(any(), any());
    }
}