package inbox.inbox.config;

import static inbox.inbox.utils.ConstantManager.FRONT_URL;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// cors config
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(FRONT_URL)
            .allowedMethods("*")
            .allowCredentials(true).allowedHeaders("*").maxAge(86400);
    }
}