package inbox.inbox.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

// 인증 요청한 유저의 ip 정보와 User-Agent 정보 얻어다 줄 관리자 클래스
@Component
public class UserInfoManager {

    // User-Agent Sha-256으로 해싱
    public String getUserAgentDigest(HttpServletRequest request) throws NoSuchAlgorithmException {
        // sha-256으로 해싱
        String userAgentInfo = request.getHeader("User-Agent");
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = messageDigest.digest(
            userAgentInfo.getBytes(StandardCharsets.UTF_8));
        int length = encodedHash.length;

        // 16진수로 해싱된 값 얻기
        StringBuilder hexEncodedHash = new StringBuilder();
        for (int cnt = 0; cnt < length; ++cnt) {
            String hex = Integer.toHexString(0xff & encodedHash[cnt]);
            if (hex.length() == 1) {
                hexEncodedHash.append('0');
            }
            hexEncodedHash.append(hex);
        }
        return hexEncodedHash.toString();
    }

    // 유저 ip 얻기
    public String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getHeader("Proxy-Client-IP");

        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getHeader("WL-Proxy-Client-IP");

        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getHeader("HTTP_CLIENT_IP");

        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getHeader("HTTP_X_FORWARDED_FOR");

        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getHeader("X-Real-IP");

        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getHeader("X-RealIP");

        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getHeader("REMOTE_ADDR");

        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getRemoteAddr();

        }

        return ip;
    }
}
