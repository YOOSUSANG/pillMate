package plaform.pillmate_spring.domain.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.PrintWriter;
@Slf4j
public class LoginDefaultFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }
    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("LoginDefaultFilter에 들어옴");
        String requestURI = request.getRequestURI();
        log.info(requestURI);
        if (!requestURI.matches("^\\/login$")) {
            log.info("check={}",requestURI);
            chain.doFilter(request, response);
            return ;
        }
        log.info("LoginDefaultFilter가 진행됨 /login이 들어왔다.");
        sendResponse(response, "No AccessToken Oauth2 Login please",HttpServletResponse.SC_UNAUTHORIZED);
    }
    private void sendResponse(HttpServletResponse response, String message,int status) {
        log.info(message);
        response.setStatus(status);
        try (PrintWriter writer = response.getWriter()) {
            writer.print(message);
            writer.flush();
        } catch (IOException e) {
            log.error("Error writing to response", e);
        }
    }
}
