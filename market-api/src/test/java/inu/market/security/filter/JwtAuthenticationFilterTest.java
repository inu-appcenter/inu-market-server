package inu.market.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import inu.market.category.dto.CategoryResponse;
import inu.market.category.service.CategoryService;
import inu.market.security.util.JwtUtil;
import inu.market.user.domain.UserRepository;
import inu.market.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static inu.market.category.CategoryFixture.TEST_CATEGORY_RESPONSE;
import static inu.market.user.UserFixture.TEST_USER;
import static inu.market.user.UserFixture.TEST_USER_RESPONSE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class JwtAuthenticationFilterTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        JwtAuthenticationFilter jwtAuthenticationFilter = (JwtAuthenticationFilter) wac.getBean("jwtAuthenticationFilter");
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .alwaysDo(print())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .addFilter(jwtAuthenticationFilter)
                .build();
    }

    @Test
    @DisplayName("필터링을 한다.")
    void doFilter() throws Exception {
        // given
        String jwtToken = "Bearer " + jwtUtil.createToken(TEST_USER.getId());

        given(userRepository.findById(any()))
                .willReturn(Optional.of(TEST_USER));

        given(userService.findById(any()))
                .willReturn(TEST_USER_RESPONSE);
        // when
        mockMvc.perform(get("/api/users/profile")
                        .header(HttpHeaders.AUTHORIZATION, jwtToken)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(TEST_USER_RESPONSE)));

        // then
        then(userRepository).should(times(1)).findById(any());
        then(userService).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("필터링을 한다.")
    void doFilterNotRequiredLoginArgumentResolver() throws Exception {
        // given
        String jwtToken = "Bearer " + jwtUtil.createToken(TEST_USER.getId());

        given(userRepository.findById(any()))
                .willReturn(Optional.of(TEST_USER));

        List<CategoryResponse> result = Arrays.asList(TEST_CATEGORY_RESPONSE);
        given(categoryService.findAll())
                .willReturn(result);

        // when
        mockMvc.perform(get("/api/categories")
                        .header(HttpHeaders.AUTHORIZATION, jwtToken)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)));

        // then
        then(userRepository).should(times(1)).findById(any());
        then(categoryService).should(times(1)).findAll();
    }

    @Test
    @DisplayName("JWT 토큰이 없는 요청을 필터링을 한다.")
    void doFilterNotAuthorization() throws Exception {
        // given
        String jwtToken = "bearer **";

        // when
        mockMvc.perform(get("/api/users/profile")
                        .header(HttpHeaders.AUTHORIZATION, jwtToken)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is5xxServerError());
    }


}