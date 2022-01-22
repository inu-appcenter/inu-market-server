package inu.market.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationEntryPointTest {

    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() {
        customAuthenticationEntryPoint = new CustomAuthenticationEntryPoint(new ObjectMapper());
    }

    @Test
    @DisplayName("인증 예외를 핸들링한다.")
    void commence() throws ServletException, IOException {
        // given
        given(response.getWriter())
                .willReturn(printWriter);

        // when
        customAuthenticationEntryPoint.commence(request, response, new AuthenticationException("인증 예외") {
        });

        // then
        then(response).should(times(1)).getWriter();
    }
}