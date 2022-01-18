package inu.market;

import com.fasterxml.jackson.databind.ObjectMapper;
import inu.market.config.LoginUserArgumentResolver;
import inu.market.security.filter.JwtAuthenticationFilter;
import inu.market.security.handler.CustomAccessDeniedHandler;
import inu.market.security.handler.CustomAuthenticationEntryPoint;
import inu.market.security.service.AuthService;
import inu.market.security.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class ControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected JwtUtil jwtUtil;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected LoginUserArgumentResolver loginUserArgumentResolver;

    @MockBean
    protected CustomAccessDeniedHandler accessDeniedHandler;

    @MockBean
    protected CustomAuthenticationEntryPoint authenticationEntryPoint;

    protected MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext wac, RestDocumentationContextProvider restDocumentationContextProvider) {
        JwtAuthenticationFilter jwtAuthenticationFilter = (JwtAuthenticationFilter) wac.getBean("jwtAuthenticationFilter");

        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .alwaysDo(print())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .addFilter(jwtAuthenticationFilter)
                .apply(documentationConfiguration(restDocumentationContextProvider)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }
}
