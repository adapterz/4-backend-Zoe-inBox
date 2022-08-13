package inbox.inbox.exception;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;

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

    // 유효성 검사
    private boolean isDateValid(Date input) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = sdf.parse(sdf.format(input));
        Date now = sdf.parse(sdf.format(new Date()));
        if (!(date.equals(now) || date.before(now))) {
            System.out.println("valid false ");
            return false;

        }
        System.out.println("valid true ");
        return true;
    }

}
