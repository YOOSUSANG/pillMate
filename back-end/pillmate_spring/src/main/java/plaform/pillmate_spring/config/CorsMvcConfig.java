package plaform.pillmate_spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                // about response
                .exposedHeaders("Set-Cookie")
                // about request
                .allowedMethods("*")
                .allowCredentials(true)
                .allowedOrigins("http://localhost:5173");
    }
}
