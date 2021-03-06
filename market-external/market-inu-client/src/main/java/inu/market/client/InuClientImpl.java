package inu.market.client;

import inu.market.common.NetworkException;
import inu.market.common.NotMatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class InuClientImpl implements InuClient {

    public static final String INU_LOGIN_URL = "https://cyber.inu.ac.kr/login/index.php";

    private final RestTemplate restTemplate;

    public InuClientImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public void login(Integer inuId, String password) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", String.valueOf(inuId));
        params.add("password", password);

        HttpEntity<LinkedMultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(INU_LOGIN_URL, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            if (response.getHeaders().size() == 17) {
                throw new NotMatchException("학번 혹은 비밀번호가 맞지 않습니다.");
            }
        } else {
            throw new NetworkException("서버와 통신 중 오류가 발생했습니다.");
        }
    }
}
