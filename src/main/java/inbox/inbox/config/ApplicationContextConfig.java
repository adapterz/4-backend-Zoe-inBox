package inbox.inbox.config;

import static inbox.inbox.config.ConstantList.INBOX_PACKAGE;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// bean 객체 추가를 위한 설정
@ComponentScan(INBOX_PACKAGE + ".utils")
@Configuration
public class ApplicationContextConfig {

}
