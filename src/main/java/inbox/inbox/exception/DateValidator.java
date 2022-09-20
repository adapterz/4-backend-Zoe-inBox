package inbox.inbox.exception;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;

// 입력한 포트폴리오 날짜가 현재 혹은 과거인지 유효성 검사해주는 유효성 검사기
// @PastOrPresent 어노테이션 사용 시 원하는 대로 유효성 검사를 해주지 않아서 직접 정의
public class DateValidator implements ConstraintValidator<DateValidated, Date> {

    private String returnMessage;

    @Override
    public void initialize(DateValidated requiredIfChecked) {
        returnMessage = requiredIfChecked.message();
    }

    @SneakyThrows
    @Override
    public boolean isValid(Date input, ConstraintValidatorContext context) {
        // 유효성 검사
        boolean valid;

        // Date 유효성 검사
        valid = isDateValid(input);

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(returnMessage).addConstraintViolation();
        }
        return valid;
    }

    // 날짜 유효성 검사
    // 현재 혹은 과거인지 & 2000년도 이후 날짜인지 유효성 검사
    private boolean isDateValid(Date input) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date inputDate = sdf.parse(sdf.format(input));
        Date now = sdf.parse(sdf.format(new Date()));
        Date twoThousandYear = sdf.parse("20000101");
        // 현재 혹은 과거가 아닐 때
        if (!(inputDate.equals(now) || inputDate.before(now))) {
            return false;
        }

        // 2000년도 이후 영상이 아닐 때
        if (!(twoThousandYear.before(inputDate) || twoThousandYear.equals(inputDate))) {
            return false;
        }
        return true;
    }
}
