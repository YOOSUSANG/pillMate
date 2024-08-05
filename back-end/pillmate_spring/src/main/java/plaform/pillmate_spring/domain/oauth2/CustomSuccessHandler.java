package plaform.pillmate_spring.domain.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import plaform.pillmate_spring.domain.entity.RedisRefreshToken;
import plaform.pillmate_spring.domain.jwt.JWTUtil;
import plaform.pillmate_spring.domain.repository.RedisRefreshTokenRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;
    private final RedisRefreshTokenRepository redisRefreshTokenRepository;

    //로그인 성공시 발생하는 부분
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("CustomOAuth2UserService 이후에 실행되는 로그인 성공");
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String username = customOAuth2User.getUsername();

        // 시큐리티 세션에서 권한(role) 가져오기
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        //유효시간 216.000ms -> 216초 -> 3분
//        String token = jwtUtil.createJwt(username, role, 60 * 60 * 60L);
//        response.addCookie(createCookie("Authorization", token));
//        response.sendRedirect("http://localhost:5173/");
        String access = jwtUtil.createJwt("Authorization", username, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        //Refresh 토큰 저장
//        addRefreshEntity(username, refresh, 86400000L);

        //redis에 Refresh 토큰 저장
        addRedisRefreshEntity(username, refresh);

        // jwt 토큰을 쿠키 header에 담아 응답한다.
        response.addCookie(createCookieAccess("Authorization", access));
        response.addCookie(createCookie("refresh", refresh));

        response.setStatus(HttpStatus.OK.value());


        response.sendRedirect("http://localhost:5173/generalsearch");
    }

    private Cookie createCookieAccess(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(600); // 대략 10분
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true); // 자바 스크립트가 해당 쿠기를 읽기만 가능하다. 이 쿠키를 자바스크립트에서 강제로 header에 담아서 보낼 순 없다. 이 쿠키는 HttpOnly라서 --> 대부분 서버는 httpOnly
        return cookie;
    }
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60); // 대략 24시간?
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true); // 자바 스크립트가 해당 쿠기를 읽기만 가능하다. 이 쿠키를 자바스크립트에서 강제로 header에 담아서 보낼 순 없다. 이 쿠키는 HttpOnly라서 --> 대부분 서버는 httpOnly
        return cookie;
    }

//    private void addRefreshEntity(String username, String refresh, Long expiredMs) {
//        Date date = new Date(System.currentTimeMillis() + expiredMs);
//        RefreshEntity refreshEntity = new RefreshEntity();
//
//        refreshEntity.setUsername(username);
//        refreshEntity.setRefresh(refresh);
//        refreshEntity.setExpiration(date.toString());
//
//        refreshRepository.save(refreshEntity);
//    }


    private void addRedisRefreshEntity(String username, String refresh) {
        RedisRefreshToken redisRefreshToken = new RedisRefreshToken();
        redisRefreshToken.setUsername(username);
        redisRefreshToken.setRefresh(refresh);
        redisRefreshTokenRepository.save(redisRefreshToken);
    }
}
