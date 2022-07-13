package inbox.inbox.portfolio;

import inbox.inbox.utils.UserInfoManager;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// 포트폴리오 서비스
@RequiredArgsConstructor
@Service
public class PortfolioService {

    private final JavaMailSender javaMailSender;
    private final PortfolioEmailConfirmRepository portfolioEmailConfirmRepository;
    private final UserInfoManager userInfoManager;

    // 인증 메일 보내기
    public void sendConfirmCodeForEmailAuthentication(
        PortfolioEmailConfirmDto portfolioEmailConfirmDto) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        StringBuilder confirmText = new StringBuilder("인증번호: ");
        // 6자리 난수 생성 100000~999999
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        int confirmCode = (random.nextInt(900000) + 100000) % 1000000;

        // 수신자
        simpleMailMessage.setTo(portfolioEmailConfirmDto.getEmail());
        // 메일 제목
        simpleMailMessage.setSubject("Inbox 포트폴리오 업로드를 위한 인증번호 메일입니다.");
        simpleMailMessage.setText(String.valueOf(confirmText.append(confirmCode)));
        // 메일 발송
        javaMailSender.send(simpleMailMessage);
        // dto 에 인증번호 정보 추가
        portfolioEmailConfirmDto.setConfirmCode(confirmCode);
    }

    // Confirm 테이블에 이메일 인증을 위한 정보 추가 (인증번호, 유저 ip, User-Agent, email)
    public long addPortfolioEmailConfirm(PortfolioEmailConfirmDto portfolioEmailConfirmDto,
        HttpServletRequest request) throws NoSuchAlgorithmException {

        // User-Agent 와 ip 정보 얻어오기
        // User-Agent 정보 sha-256 으로 해싱
        String userAgentDigest = userInfoManager.getUserAgentDigest(request);
        portfolioEmailConfirmDto.setUserAgentDigest(userAgentDigest);

        // 2. 요청한 유저의 user-agent 와 ip 정보 얻기

        return true;
    }

}
