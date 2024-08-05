package plaform.pillmate_spring.domain.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import plaform.pillmate_spring.domain.dto.MemberLoginOAuth;
import plaform.pillmate_spring.domain.oauth2.CustomOAuth2User;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {


    //jwt 검증을 위한 jwtUtil
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("JWTFilter에 들어옴");
        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("Authorization")){
                accessToken = cookie.getValue();
            }
        }
        //access 토큰이 없다면 다음 필터로 넘긴다. -> 로그인 성공 필터 등과 같은 다음 필터로 진행하기 위함
        //acceess 토큰이 없다는 것은 로그인을 하지 않았다는 뜻 -> 로그인이 필요하다는 usernamePassword전에 필터하나 만들어서 처리 해줘야 함
        if (accessToken == null) {
            log.info("accessToken이 header에 없다.");
            filterChain.doFilter(request, response);
            return ;
        }

        //토큰이 있는데 만료 여부 확인, 만료시 다음 필터로 넘기지 않음 -> 만료될 경우 애초에 다음 필터 검증할 필요가 없다.
        //만료됐는데 다음 필터로 넘기면, 로그인 필터와 같은 또 로그인해야 하는 경우가 발생한다.
        //우리는 재 로그인이 아니라 리프레쉬 토큰으로 액세스 토큰을 재발급만 하면됨
        try {
            //access 토큰이 만료됐으면 catch로 이동
            jwtUtil.isExpired(accessToken);

        } catch (ExpiredJwtException e) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");
            Cookie deleteAccessCookie = createDeleteAccessCookie();
            response.addCookie(deleteAccessCookie);
            //response status 401 응답코드 -> 권한없음 -> 프론트측에게 응답넘기기 -> 협의 이후 프론트측에서 reissue 에게 요청
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ;

        }

        String category = jwtUtil.getCategory(accessToken);
        //나의 access 토큰이 아니면
        if (!category.equals("Authorization")) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            //response status 401 응답코드-> 권한없음 -> 프론트측에게 응답넘기기 -> 협의
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ;
        }
        log.info("JWTFilter가 진행된다.");
        // 정상적인 액세스 토큰이 왔으면 스프링 시큐리티 세션에 저장해서 권한을 다룬다. -> http response와 request는 STATELESS라서 한번 요청 후 상태가 끊긴다.
        // role 저장은 필수
        String token = accessToken;
        // 세션에 저장할 기본 정보들만 .. 이떄 권한 role은 무조건 저장해야 한다.
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);
        MemberLoginOAuth memberOauth2Dto = MemberLoginOAuth.builder()
                .username(username)
                .role(role)
                .build();
        //************** 권한 설정 관련 -> jwt 토큰을 서버에서 받으면 접근 권한을 위해서 스프링 세션에 강제 저장**************

        // 이 부분은 권한 설정을 위해서 Authentication()에 장착하기 위함이다. 여기에서 오류가 안나면 해당 userDto -> username이 검증이 완료된 상태
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(memberOauth2Dto);
        log.info(memberOauth2Dto.getRole());
        //스프링 시큐리티 인증 토큰 생성 -> 오류 안나면 정상 인증돼서 토큰 생성됨
        //인증이 완료됐으니까 강제로 Authentication을 만들 수 있다. 이 username은 이미 인증이 돼었기 떄문이다. -> CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
        //JWT토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
        //권한정보들만 authenticaion으로 해서 저장한다.
        //authentication은 기본이 UsernamePassWordAuthenticationToken

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        //스프링 시큐리티 세션에 사용자 등록 *** 중요 ***
        //강제로 시큐리티 세션에 접근하여 Authentication 객체를 저장한 것이다. http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));를 무시
        //로그인할 떄 스프링 시큐리티 세션에 저장했는데 왜 또 다시 강제로 스프링 시큐리티 세션에 저장하냐 --> 현재 우리는 세션을 STATELESS로 설정했기 떄문에 로그인이 성공하면 세션에 남지만 다시 요청할 떄 는 세션에서 없어졌기
        //때문에 jwt가 유효하면 권한들을 시큐리티 세션에 강제로 저장해서 해당 유저가 해당 권한시 접근할 수 있게 한다.
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //정상 필터 체인 흐름
        filterChain.doFilter(request, response);
    }
    private Cookie createDeleteAccessCookie() {
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }
}
