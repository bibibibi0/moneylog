package org.codenova.moneylog.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.codenova.moneylog.Request.FindPasswordRequest;
import org.codenova.moneylog.Request.LoginRequest;
import org.codenova.moneylog.entity.Verification;
import org.codenova.moneylog.repository.VerificationRepository;
import org.codenova.moneylog.vo.KakaoTokenResponse;
import org.codenova.moneylog.vo.NaverProfileResponse;
import org.codenova.moneylog.vo.NaverTokenResponse;
import org.codenova.moneylog.entity.User;
import org.codenova.moneylog.repository.UserRepository;
import org.codenova.moneylog.service.KakaoApiService;
import org.codenova.moneylog.service.MailService;
import org.codenova.moneylog.service.NaverApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Controller
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final UserRepository userRepository;
    private KakaoApiService kakaoApiService;
    private NaverApiService naverApiService;

    private MailService mailService;
    private VerificationRepository verificationRepository;

    @GetMapping("/login")
    public String loginHandle(Model model) {
        log.info("loginHandle...executed");

        model.addAttribute("kakaoClientId", "3dd41579c6f34f12629d4f0c6d07bec3");
        model.addAttribute("kakaoRedirectUri", "http://192.168.10.21:8080/auth/kakao/callback");

        model.addAttribute("naverClientId", "ZCHHLlkTUUZiafrmF7FK");
        model.addAttribute("naverRedirectUrl", "http://192.168.10.21:8080/auth/naver/callback");

        return "auth/login";
    }

    @PostMapping("/login")
    public String loginPostHandle(@ModelAttribute LoginRequest loginRequest, HttpSession session, Model model) {
        User user =
                userRepository.findByEmail(loginRequest.getEmail());
        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            session.setAttribute("user", user);
            return "redirect:/index";
        } else {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/signup")
    public String signUpHandle(Model model) {


        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signUpPostHandle(@ModelAttribute User user) {
        User found = userRepository.findByEmail(user.getEmail());
        if (found == null) {
            user.setProvider("LOCAL");
            user.setVerified("F");
            userRepository.save(user);
            mailService.sendWelcomeHtmlMessage(user);
        }
        return "redirect:/index";
    }


    @GetMapping("/kakao/callback")
    public String kakaoCallbackHandle(@RequestParam("code") String code,
                                      HttpSession session
    ) throws JsonProcessingException {
        //log.info("code = {}", code);
        KakaoTokenResponse response = kakaoApiService.exchangeToken(code);
        //log.info("response.idToken = {}", response.getIdToken());
        DecodedJWT decodedJWT = JWT.decode(response.getIdToken());
        decodedJWT.getAudience();
        String sub = decodedJWT.getClaim("sub").asString();
        String nickname = decodedJWT.getClaim("nickname").asString();
        String picture = decodedJWT.getClaim("picture").asString();

        User found = userRepository.findByProviderAndProviderId("KAKAO", sub);
        if (found != null) {
            session.setAttribute("user", found);
        } else {
            User user = User.builder().provider("KAKAO").providerId(sub).
                    nickname(nickname).picture(picture).verified("T").build();
            userRepository.save(user);
            session.setAttribute("user", user);
        }

        //log.info("decodedJWT: sub={}, nickname={}, picture={}", sub, nickname, picture);
        return "redirect:/index";
    }

    @GetMapping("/naver/callback")
    public String naverCallbackHandle(@RequestParam("code") String code, @RequestParam("state") String state) throws JsonProcessingException {
        //확인 완료 log.info("code = {}, state = {}", code, state);

        NaverTokenResponse tokenResponse = naverApiService.exchangeToken(code, state);
        //확인 완료 log.info("accessToken = {}", tokenResponse.getAccessToken());

        naverApiService.exchangeProfile(tokenResponse.getAccessToken());

        NaverProfileResponse profileResponse
                = naverApiService.exchangeProfile(tokenResponse.getAccessToken());
        /*
        log.info("profileResponse id = {}", profileResponse.getId());
        log.info("profileResponse nickname = {}", profileResponse.getNickname());
        log.info("profileResponse profileImage = {}", profileResponse.getProfileImage()); 확인 완료!!
        네이버에서 정보 받아옴 개쓰레기같은거 개처힘드네*/


        return "redirect:/index";
    }

    @GetMapping("/find-password")
    public String findPasswordHandle(Model model) {
        return "auth/find-password";
    }

    @PostMapping("/find-password")
    public String findPasswordPostHandle(@ModelAttribute @Valid FindPasswordRequest req,
                                         BindingResult result,
                                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", "이메일 형식이 아닙니다.");
            return "auth/find-password-error";
        }


        User found = userRepository.findByEmail(req.getEmail());
        if (found == null) {
            model.addAttribute("error", "해당 이메일로 임시번호를 전송할 수 없습니다.");
            return "auth/find-password-error";
        }

        String temporalPassword = UUID.randomUUID().toString().substring(0, 8);
        userRepository.updatePasswordByEmail(req.getEmail(), temporalPassword);
        mailService.sendTemporalPasswordMessage(req.getEmail(), temporalPassword);

        return "auth/find-password-success";
    }

    @GetMapping("/send-token")
    public String sendTokenHandle(@SessionAttribute("user") User user,
                                                           Model model) {
        String token = UUID.randomUUID().toString().replace("-", "");

        Verification one = Verification.builder()
                .token(token)
                .expiresAt(LocalDateTime.now().plusDays(1))
                .userEmail(user.getEmail())
                .build();
        verificationRepository.save(one);
        mailService.sendVerificationMessage(user, one);
            // ^ 어디에 보내야 하는지와 생성된 토큰 번호를 넘겨줘야 함!

        return "auth/send-token";
    }

    @GetMapping("/email-verify")
    public String emailVerifyHandle(@RequestParam("token") String token, Model model) {
        Verification found = verificationRepository.findByToken(token);
        if (found == null) {
            model.addAttribute("error", "유효하지 않은 인증 토큰입니다.");
            return "auth/email-verify-error";
        }
        if (LocalDateTime.now().isAfter(found.getExpiresAt())) {
            model.addAttribute("error", "유효기간이 만료된 인증 토큰입니다.");
            return "auth/email-verify-error";
        }
        String userEmail = found.getUserEmail();
        userRepository.updateVerifiedByEmail(userEmail);
        // found.getExpiresAt();  // 토큰이 가진 유효 만료 시점
        //LocalDateTime.now();  // 인증 시점

        return "auth/email-verify-success";
    }
}

