package inbox.inbox.portfolio;

import static inbox.inbox.utils.ConstantManager.BE;
import static inbox.inbox.utils.ConstantManager.FE;

import inbox.inbox.utils.UserInfoManager;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

// 포트폴리오 서비스
@RequiredArgsConstructor
@Service
public class PortfolioService {

    private final JavaMailSender javaMailSender;
    private final PortfolioConfirmRepository portfolioConfirmRepository;
    private final PortfolioRepository portfolioRepository;
    private final PortfolioFileRepository portfolioFileRepository;
    private final UserInfoManager userInfoManager;

    // 인증 메일 보내기
    public void sendConfirmCodeForEmailAuthentication(
        PortfolioConfirmDto portfolioConfirmDto) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        StringBuilder confirmText = new StringBuilder("인증번호: ");
        // 6자리 난수 생성 100000~999999
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        int confirmCode = (random.nextInt(900000) + 100000) % 1000000;

        // 수신자
        simpleMailMessage.setTo(portfolioConfirmDto.getEmail());
        // 메일 제목
        simpleMailMessage.setSubject("Inbox 포트폴리오 업로드를 위한 인증번호 메일입니다.");
        simpleMailMessage.setText(String.valueOf(confirmText.append(confirmCode)));
        // 메일 발송
        javaMailSender.send(simpleMailMessage);
        // dto 에 인증번호 정보 추가
        portfolioConfirmDto.setConfirmCode(confirmCode);
    }

    // Confirm 테이블에 이메일 인증을 위한 정보 추가 (인증번호, 유저 ip, User-Agent, email)
    public long addPortfolioEmailConfirm(PortfolioConfirmDto portfolioConfirmDto,
        HttpServletRequest request) throws NoSuchAlgorithmException {

        // User-Agent 와 ip 정보 얻어오기
        // User-Agent 정보 sha-256 으로 해싱
        String userAgentDigest = userInfoManager.getUserAgentDigest(request);
        portfolioConfirmDto.setUserAgentDigest(userAgentDigest);

        // 유저의 ip 정보 가져오기
        String ip = userInfoManager.getIp(request);
        portfolioConfirmDto.setIp(ip);

        // db에 저장 및 idx return
        return portfolioConfirmRepository.save(
                PortfolioConfirm.builder()
                    .email(portfolioConfirmDto.getEmail())
                    .ip(portfolioConfirmDto.getIp())
                    .confirmCode(
                        portfolioConfirmDto.getConfirmCode())
                    .userAgentDigest(portfolioConfirmDto.getUserAgentDigest()).build())
            .getConfirm_idx();
    }

    // 인증번호 확인 및 유저 정보 일치 여부 확인
    public void confirmForAuthentication(PortfolioConfirmDto portfolioConfirmDto,
        HttpServletRequest request)
        throws NoSuchAlgorithmException {

        // User-Agent 와 ip 정보 얻어오기
        // User-Agent 정보 sha-256 으로 해싱
        String userAgentDigest = userInfoManager.getUserAgentDigest(request);
        portfolioConfirmDto.setUserAgentDigest(userAgentDigest);

        // 유저의 ip 정보 가져오기
        String ip = userInfoManager.getIp(request);
        portfolioConfirmDto.setIp(ip);

        // confirm 테이블의 인덱스와 일치하는 정보 가져오기
        Optional<PortfolioConfirm> portfolioConfirm = portfolioConfirmRepository.findById(
            portfolioConfirmDto.getConfirmIdx());
        // 일치하는 정보가 없을 때 예외 처리
        if (!portfolioConfirm.isPresent()) {
            throw new PortfolioConfirmNotFoundException();
        }
        // 새 dto 에 db 에서 가져온 정보 옮기기
        PortfolioConfirmDto portfolioConfirmDtoFromDb = PortfolioConfirmDto.builder()
            .email(portfolioConfirm.get().getEmail())
            .confirmCode(portfolioConfirm.get().getConfirm_code())
            .userAgentDigest(portfolioConfirm.get().getUser_agent_digest())
            .ip(portfolioConfirm.get().getIp()).build();

        // 인증번호가 일치하지 않을 때
        if (portfolioConfirmDto.getConfirmCode() != portfolioConfirmDtoFromDb.getConfirmCode()) {
            throw new PortfolioConfirmUnauthorizedException();
        }
        // User-Agent digest 가 일치하지 않을 때
        if (!Objects.equals(portfolioConfirmDto.getUserAgentDigest(),
            portfolioConfirmDtoFromDb.getUserAgentDigest())) {
            throw new PortfolioConfirmUnauthorizedException();
        }
        // ip가 일치하지 않을때
        if (!Objects.equals(portfolioConfirmDto.getIp(), portfolioConfirmDtoFromDb.getIp())) {
            throw new PortfolioConfirmUnauthorizedException();
        }
        // email 이 일치하지 않을때
        if (!Objects.equals(portfolioConfirmDto.getEmail(), portfolioConfirmDtoFromDb.getEmail())) {
            throw new PortfolioConfirmUnauthorizedException();
        }

    }

    // 포트폴리오 정보 추가
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void addPortfolio(PortfolioDto portfolioDto) {
        Byte rangeMappingValue = 0;
        // file table 에 저장해줄 객체 생성 및 의존성 주입
        // filePath 를 fileName, extension 으로 쪼개서 file 테이블에 매핑해줄 객체에 값 넣어주기
        String[] fileInfo = portfolioDto.getFilePath().split("\\.");
        PortfolioFile portfolioFile = PortfolioFile.builder().fileName(fileInfo[0])
            .extension(fileInfo[1]).build();
        portfolioFile.setFile_idx(portfolioFileRepository.save(portfolioFile).getFile_idx());

        // range String -> byte 로 매핑
        if (Objects.equals(portfolioDto.getRange(), BE)) {
            rangeMappingValue = 0;
        } else if (Objects.equals(portfolioDto.getRange(), FE)) {
            rangeMappingValue = 1;
        }

        // portfolio table 에 저장해줄 객체 생성 및 의존성 주입
        Portfolio portfolio = Portfolio.builder().range(rangeMappingValue)
            .title(portfolioDto.getTitle()).about(
                portfolioDto.getAbout()).portfolioDate(portfolioDto.getDate())
            .email(portfolioDto.getEmail()).build();
        portfolio.setFile(portfolioFile);

        portfolioRepository.save(portfolio);
    }
}
