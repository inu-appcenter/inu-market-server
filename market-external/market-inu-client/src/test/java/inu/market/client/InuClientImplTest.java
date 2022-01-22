package inu.market.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import inu.market.common.NetworkException;
import inu.market.common.NotMatchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.stream.IntStream;

import static inu.market.client.InuClientImpl.INU_LOGIN_URL;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RestClientTest(value = InuClientImpl.class)
class InuClientImplTest {

    @Autowired
    private InuClientImpl inuClient;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("INU 로그인을 한다.")
    void login() {
        // given
        HttpHeaders headers = new HttpHeaders();
        IntStream.range(0, 18).forEach(value -> {
            headers.add("test" + value, "test" + value);
        });

        mockServer.expect(requestTo(INU_LOGIN_URL))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK).headers(headers));

        // when
        inuClient.login(201601757, "password");
    }

    @Test
    @DisplayName("잘못된 비밀번호로 INU 로그인을 하면 예외가 발생한다.")
    void loginNotMatch() {
        // given
        HttpHeaders headers = new HttpHeaders();
        IntStream.range(0, 17).forEach(value -> {
            headers.add("test" + value, "test" + value);
        });

        mockServer.expect(requestTo(INU_LOGIN_URL))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK).headers(headers));

        // when
        assertThrows(NotMatchException.class, () -> inuClient.login(201601757, "password"));
    }

    @Test
    @DisplayName("INU 서버가 장애가 나면 예외가 발생한다.")
    void loginNetworkError() {
        // given
        mockServer.expect(requestTo(INU_LOGIN_URL))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.SEE_OTHER));

        // when
        assertThrows(NetworkException.class, () -> inuClient.login(201601757, "password"));
    }

}