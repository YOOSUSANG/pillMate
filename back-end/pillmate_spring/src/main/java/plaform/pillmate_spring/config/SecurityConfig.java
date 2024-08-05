package plaform.pillmate_spring.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import plaform.pillmate_spring.domain.filter.LoginDefaultFilter;
import plaform.pillmate_spring.domain.jwt.JWTFilter;
import plaform.pillmate_spring.domain.jwt.JWTUtil;
import plaform.pillmate_spring.domain.oauth2.CustomLogoutFilter;
import plaform.pillmate_spring.domain.oauth2.CustomOAuth2UserService;
import plaform.pillmate_spring.domain.oauth2.CustomSuccessHandler;
import plaform.pillmate_spring.domain.repository.RedisRefreshTokenRepository;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final RedisRefreshTokenRepository redisRefreshTokenRepository;
    private final JWTUtil jwtUtil;


    //스프링 시큐리티와 관련된 cors -> 보안 필터 체인에서의 cors 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173")); //localhost:3000에 대한 모든 요청 허용
                        //get, post, put 모든 요청 허용
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        //어떤 헤더도 다 받을 수 있다.
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L); // 요청에 대한 응답이 3600초 동안 캐시된다. 브라우저는 일정 시간 동알 동일한 요청에 대한 preflight 요청을 다시 보내지 않고 이전에 받은 응답을 사용할 수 있다.


                        //우리쪽에서 데이터를 줄 경우 웹페이지에서 보이게 하는 응답 헤더들 -> 응답 관련
                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));


        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //Form 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        // JWT 필터 추가 이후로 설정한 이유는 만약 /oauth2/authorization/서비스 명으로 접근 시 Oauth2LoginAuthenticationFilter가 작동되는데 이 필터 이전에 jwtFilter를 등록하면 로그인 불가능이다. 따라서 이후 설정
        // 그래서 이후로 설정한다. Oauth2LoginAuthenticationFilter가 true면 AutenticationSuccessHanlder로 catch 하여 jwt필터 무시
        // /oauth2/authorization/서비스 명 경우에만 Oauth2LoginAuthenticationFilter가 작동
        http
                .addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);

        http.addFilterBefore(new CustomLogoutFilter(jwtUtil, redisRefreshTokenRepository), LogoutFilter.class);
        // accesstoken 없을 떄 작동하는 필터 추가
        http.addFilterAfter(new LoginDefaultFilter(), UsernamePasswordAuthenticationFilter.class);


        //oauth2
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)))
                        .successHandler(customSuccessHandler)
                );


        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/reissue").permitAll()
                        .requestMatchers("/index.html").permitAll()
                        .requestMatchers("/assets/**").permitAll()
                        .anyRequest().authenticated());

        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }

}
