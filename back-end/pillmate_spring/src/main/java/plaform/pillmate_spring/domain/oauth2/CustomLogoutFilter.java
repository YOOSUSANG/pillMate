package plaform.pillmate_spring.domain.oauth2;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;
import plaform.pillmate_spring.domain.entity.RedisRefreshToken;
import plaform.pillmate_spring.domain.jwt.JWTUtil;
import plaform.pillmate_spring.domain.repository.RedisRefreshTokenRepository;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class CustomLogoutFilter extends GenericFilterBean {
    private final JWTUtil jwtUtil;
    private final RedisRefreshTokenRepository redisRefreshTokenRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response,chain);
    }

    //모든 필터가 거치는데 여기는 로그아웃만 체크해주고 싶다.
    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException{
        log.info("로그아웃 필터에 들어옴");
        String requestURI = request.getRequestURI();
        if (!requestURI.matches("^\\/logout$")) {

            //로그아웃이 아니면 다른 필터로 이동
            chain.doFilter(request, response);
            return ;
        }
        String requestMethod = request.getMethod();

        if (!requestMethod.equals("POST")) {

            //로그아웃이 아니면 다른 필터로 이동
            chain.doFilter(request, response);
            return ;
        }
        log.info("로그아웃이 진행된다.");
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        //쿠키에 리프레쉬 토큰이 있는지 확인
        if (refresh == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ;
        }

        try {
            jwtUtil.isExpired(refresh);

        } catch (ExpiredJwtException e) {
            //response status code -> refresh 토큰이 만료됐으므로 이미 로그아웃이 진행됨.
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //내 서버에서 발급한 리프레쉬 토큰인지 확인
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        log.info("내 서버가 리프레쉬 토큰을 발급했다.");
//        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        Boolean isExist = redisRefreshTokenRepository.existsByRefresh(refresh);
        //내 db에 존재하지 않으면 해당 refresh 토큰은 바로 이전에 발급한 리프레쉬 토큰이 아니다.
        if (!isExist) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        log.info("리프레쉬 토큰이 db에 존재한다.");

        RedisRefreshToken deleteRefresh = redisRefreshTokenRepository.findByRefresh(refresh);
        redisRefreshTokenRepository.delete(deleteRefresh);
        logger.info("redis에서 해당 refresh 제거 ");

        //만료기간이 지난 리프레쉬 토큰 클라이언트에게 갱신 리프레쉬과 액세스 토큰 둘다 없애기
        Cookie logoutRefreshCookie = createLogoutRefreshCookie();
        Cookie logoutAccessCookie = createLogoutAccessCookie();
        response.addCookie(logoutAccessCookie);
        response.addCookie(logoutRefreshCookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private Cookie createLogoutRefreshCookie() {
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }

    private Cookie createLogoutAccessCookie() {
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }
}
