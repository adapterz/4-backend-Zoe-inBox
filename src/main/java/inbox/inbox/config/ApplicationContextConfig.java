package inbox.inbox.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// bean 객체 추가를 위한 설정
@ComponentScan("inbox.inbox.utils")
@Configuration
public class ApplicationContextConfig {

}
