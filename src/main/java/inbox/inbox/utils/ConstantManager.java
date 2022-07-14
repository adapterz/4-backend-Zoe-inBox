package inbox.inbox.utils;

import org.springframework.stereotype.Component;

// 상수 관리자 클래스 (일부 상수 표현식만 받아들일 수 있는 요소들에 사용되는 상수는 static final 로 선언)
@Component
final public class ConstantManager {

    // 경로 이름
    public static final String FILTER_PATH = "/filters";
    public static final String PORTFOLIO_PATH = "/portfolios";

    // 응답 메세지
    public final String INVALID_REQUEST = "invalid_request";
    public final String UNEXPECTED_ERROR = "unexpected_error";
    public final String AT_LEAST_ONE_SHOULD_BE_ON = "at_least_one_should_be_on";
    public final String SEND_MAIL = "send_mail";

    // Controller 에서 쓰일 상수 값
    public final String FILTER = "filter";
    public static final String ON = "on";
    public static final String OFF = "off";
    public static final String FE = "fe";
    public static final String BE = "be";
}
