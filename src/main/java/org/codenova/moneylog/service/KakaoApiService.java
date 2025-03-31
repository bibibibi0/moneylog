package org.codenova.moneylog.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codenova.moneylog.Vo.KakaoTokenResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
@AllArgsConstructor
@Slf4j
public class KakaoApiService {
    private ObjectMapper objectMapper;

    public KakaoTokenResponse exchangeToken(String code) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","authorization_code");
        body.add("client_id","3dd41579c6f34f12629d4f0c6d07bec3");
        body.add("redirect_url","http://192.168.10.21:8080/auth/kakao/callback");
        body.add("code",code);

        ResponseEntity<String> response = restTemplate.exchange("https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                String.class
        );

        log.info("kakao API response : {}", response.getBody());
        KakaoTokenResponse kakaoTokenResponse =
                objectMapper.readValue(response.getBody(), KakaoTokenResponse.class);

        return kakaoTokenResponse;
    }

}
