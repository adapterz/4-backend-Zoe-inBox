package inbox.inbox.exception;

import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

// 특정 값 유효성 검사 validator
public class ValuesAllowedValidator implements ConstraintValidator<ValuesAllowed, String> {

    private List<String> expectedValues;
    private String returnMessage;

    @Override
    public void initialize(ValuesAllowed requiredIfChecked) {
        expectedValues = Arrays.asList(requiredIfChecked.values());
        returnMessage = requiredIfChecked.message();
    }

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {
        // 유효성 검사
        boolean valid;

        // 유효성 검사하는 키 값이 filePath 일 때 확장자 유효성 검사
        if (Objects.equals(returnMessage, "filePath")) {
            valid = isFilePathValid(input);
            // 키 값이 filePath 가 아닐 때 유효성 검사
        } else {
            valid = expectedValues.contains(input);
        }

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(returnMessage).addConstraintViolation();
        }
        return valid;
    }

    // 키 값이 filePath 일 때 유효성 검사
    private boolean isFilePathValid(String input) {
        String[] fileInfo = input.split("\\.");
        // . 으로 구분했을 때 배열의 칸 수가 두개가 아닐 때 유효하지 않음
        if (fileInfo.length != 2) {
            return false;
            // 지정한 확장자 값이 아닐 때
        } else {
            return expectedValues.contains(fileInfo[1]);
        }
    }
}

