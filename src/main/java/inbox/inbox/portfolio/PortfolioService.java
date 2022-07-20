package inbox.inbox.portfolio;

import static inbox.inbox.utils.ConstantManager.BE;
import static inbox.inbox.utils.ConstantManager.FE;
import static inbox.inbox.utils.ConstantManager.OFF;
import static inbox.inbox.utils.ConstantManager.ON;

import inbox.inbox.utils.UserInfoManager;
import java.security.NoSuchAlgorithmException;
import java.util.List;
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
        Random random = new Random(System.currentTimeMillis());
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
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
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

    // 포트폴리오 정보 가져오기
    public PortfolioResponseMessage getPortfolioInfo(String backendSwitch, String frontendSwitch) {
        // 필요한 변수 선언
        String range = "";
        Optional<Portfolio> portfolio;
        List<Portfolio> portfolioList = null;
        long rowCount;

        // portfolio 테이블의 로우 개수
        long tableCount = portfolioRepository.count();
        if (tableCount == 0) {
            throw new PortfolioNotFoundException();
        }
        // 선택한 범위 데이터 가져오기
        // 모든 범위
        if (Objects.equals(backendSwitch, ON) && Objects.equals(frontendSwitch, ON)) {
            portfolioList = portfolioRepository.findAll();
        }
        // 백엔드 포트폴리오만
        if (Objects.equals(backendSwitch, ON) && Objects.equals(frontendSwitch, OFF)) {
            portfolioList = portfolioRepository.findByRangeAll((byte) 0);
            if (portfolioList.size() == 0) {
                throw new PortfolioNotFoundException();
            }
        }
        // 프론트 포트폴리오만
        if (Objects.equals(backendSwitch, OFF) && Objects.equals(frontendSwitch, ON)) {
            portfolioList = portfolioRepository.findByRangeAll((byte) 1);
            if (portfolioList.size() == 0) {
                throw new PortfolioNotFoundException();
            }
        }
        if (portfolioList == null) {
            throw new RuntimeException();
        }
        rowCount = portfolioList.size();

        // 1 부터 선택 범위의 로우 개수 까지 랜덤한 숫자 뽑기(포트폴리오 테이블 인덱스)
        // TODO row 개수가 int 범위보다 큰 숫자일 경우가 있기 때문에 랜덤 뽑기 로직 변경 필요
        Random random = new Random(System.currentTimeMillis());
        int randomIdx = random.nextInt((int) rowCount);

        portfolio = portfolioRepository.findById(portfolioList.get(randomIdx).getPortfolio_idx());

        // 해당 인덱스 정보가 존재하지 않으면 예외 발생
        if (!portfolio.isPresent()) {
            throw new RuntimeException();
        }
        // rangeVal 이 0이면 be, 1이면 fe로 가공
        if (portfolio.get().getRangeVal() == 0) {
            range = BE;
        } else if (portfolio.get().getRangeVal() == 1) {
            range = FE;
        } else {
            throw new RuntimeException();
        }

        return PortfolioResponseMessage.builder().message("portfolio_data").range(range)
            .title(portfolio.get().getTitle())
            .fileName(portfolio.get().getPortfolioFile().getFile_name())
            .extension(portfolio.get().getPortfolioFile().getExtension())
            .portfolioDate(portfolio.get().getPortfolio_date()).about(
                portfolio.get().getAbout())
            .email(portfolio.get().getEmail()).createdDate(portfolio.get().getCreated_at())
            .build();
    }
}
