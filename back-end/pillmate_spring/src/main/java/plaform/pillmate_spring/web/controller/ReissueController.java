package plaform.pillmate_spring.web.controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import plaform.pillmate_spring.domain.entity.RedisRefreshToken;
import plaform.pillmate_spring.domain.jwt.JWTUtil;
import plaform.pillmate_spring.domain.repository.RedisRefreshTokenRepository;

import java.io.IOException;
import java.util.Date;

@Slf4j
@RestController
@Tag(name = "JWT 토큰 재발급 API", description = "AccessToken, RefreshToken")
@RequiredArgsConstructor
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final RedisRefreshTokenRepository redisRefreshTokenRepository;


    @Operation(summary = "refreshToken 재발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "refreshToken 재발급 성공")
    })
    //access 토큰이 만료될 경우 여기로 보내기
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String refresh = null;
        Cookie[] cookies = request.getCookies();

        //refresh 토큰 얻기
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        //refresh 토큰이 없으면 토큰이 없다. -> 만료됨
        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }


        //refresh 토큰이 있는데 만료됐으면 -> redis 사용시 자동 만료
        try {
            jwtUtil.isExpired(refresh);

        } catch (ExpiredJwtException e) {
            // 리프레시 토큰이 만료되면 다시 재 로그인을 권장한다.
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }


        //토큰이 우리 서버에서 보낸 refresh 토큰 인지 확인 (시크릿 키로 검증)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invaild refresh token", HttpStatus.BAD_REQUEST);
        }


        //DB에 저장된 바로 이전 리프레쉬 토큰인지 확인(바로 이전이 아니면 X)
//        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        Boolean isExist = redisRefreshTokenRepository.existsByRefresh(refresh);
        if (!isExist) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        //리프레시 토큰으로 username과 role 가져오기
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);


        // 액세스 토큰과 리프레시 토큰 재 발급
        String newAccess = jwtUtil.createJwt("Authorization", username, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);


        //기존 refresh 토큰 삭제후, 새로운 refresh 토큰 저장
        RedisRefreshToken deleteRefresh = redisRefreshTokenRepository.findByRefresh(refresh);
        redisRefreshTokenRepository.delete(deleteRefresh);
        addRedisRefreshEntity(username, newRefresh);

        response.addCookie(createCookieAccess("Authorization", newAccess));
        response.addCookie(createCookie("refresh", newRefresh));
        log.info("refresh 토큰 검증 및 access 토큰 재 발급 완료");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        int koreaTime = (9 * 60 * 60);
        cookie.setMaxAge(24 * 60 * 60 + koreaTime);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true); // 자바 스크립트가 해당 쿠기를 읽기만 가능하다. 이 쿠키를 자바스크립트에서 강제로 header에 담아서 보낼 순 없다. 이 쿠키는 HttpOnly라서 --> 대부분 서버는 httpOnly
        return cookie;
    }

    private Cookie createCookieAccess(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        int koreaTime = (9 * 60 * 60);
        // 현재 시간에서 10분 후의 시간을 계산
        cookie.setMaxAge(60 * 10 + koreaTime);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true); // 자바 스크립트가 해당 쿠기를 읽기만 가능하다. 이 쿠키를 자바스크립트에서 강제로 header에 담아서 보낼 순 없다. 이 쿠키는 HttpOnly라서 --> 대부분 서버는 httpOnly
        return cookie;
    }

    private void addRedisRefreshEntity(String username, String refresh) {
        RedisRefreshToken redisRefreshToken = new RedisRefreshToken();
        redisRefreshToken.setUsername(username);
        redisRefreshToken.setRefresh(refresh);
        redisRefreshTokenRepository.save(redisRefreshToken);
    }
}
