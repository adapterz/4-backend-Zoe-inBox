package inbox.inbox.portfolio;

import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PortfolioService {

    private final JavaMailSender javaMailSender;
    private final PortfolioEmailConfirmRepository portfolioEmailConfirmRepository;

    public boolean SendConfirmCodeForEmailAuthentication(
        PortfolioEmailConfirmDto portfolioEmailConfirmDto) {
        /*
         * 1. 인증 메일 보내기
         * 2. 요청한 유저의 user-agent 와 ip, email, 인증번호 db에 저장
         */

        // 1. 인증 메일 보내기
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        StringBuffer confirmText = new StringBuffer("인증번호: ");
        // 6자리 난수 생성 100000~999999
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        int confirmNumber = (random.nextInt(900000) + 100000) % 1000000;

        // 수신자
        simpleMailMessage.setTo(portfolioEmailConfirmDto.getEmail());
        // 메일 제목
        simpleMailMessage.setSubject("Inbox 포트폴리오 업로드를 위한 인증번호 메일입니다.");
        simpleMailMessage.setText(String.valueOf(confirmText.append(confirmNumber)));
        // 메일 발송
        javaMailSender.send(simpleMailMessage);

        // 2. 요청한 유저의 user-agent 와 ip 정보 얻기

        return true;
    }

}
