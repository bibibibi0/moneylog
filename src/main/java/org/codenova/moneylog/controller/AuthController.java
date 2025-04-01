package org.codenova.moneylog.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codenova.moneylog.Vo.KakaoTokenResponse;
import org.codenova.moneylog.entity.User;
import org.codenova.moneylog.repository.UserRepository;
import org.codenova.moneylog.service.KakaoApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final UserRepository userRepository;
    private KakaoApiService kakaoApiService;

    @GetMapping("/login")
    public String loginHandle(Model model) {
        log.info("loginHandle...executed");

        model.addAttribute("kakaoClientId", "3dd41579c6f34f12629d4f0c6d07bec3");
        model.addAttribute("kakaoRedirectUri", "http://192.168.10.21:8080/auth/kakao/callback");

        return "auth/login";
    }

    @GetMapping("/signup")
    public String signUpHandle(Model model) {


        return"auth/signup";
}

@PostMapping("/signup")
public String signUpPostHandle(@ModelAttribute User user) {
    User found = userRepository.findByEmail(user.getEmail());
    if (found == null) {
        user.setProvider("LOCAL");
        user.setVerified("F");
        userRepository.save(user);
    }
    return "redirect:/index";
}



    @GetMapping("/kakao/callback")
    public String kakaoCallbackHandle(@RequestParam("code") String code) throws JsonProcessingException {
      //log.info("code = {}", code);
      KakaoTokenResponse response = kakaoApiService.exchangeToken(code);
        log.info("response.idToken = {}",response.getIdToken());
        DecodedJWT decodedJWT = JWT.decode(response.getIdToken());
        decodedJWT.getAudience();
        String sub = decodedJWT.getClaim("sub").toString();
        String nickname = decodedJWT.getClaim("nickname").toString();
        String picture = decodedJWT.getClaim("picture").toString();

        log.info("decodedJWT: sub={}, nickname={}, picture={}", sub, nickname, picture);
        return "redirect:/";
    }
}
