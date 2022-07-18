package inbox.inbox.portfolio;


import static inbox.inbox.utils.ConstantManager.AVI;
import static inbox.inbox.utils.ConstantManager.BE;
import static inbox.inbox.utils.ConstantManager.FE;
import static inbox.inbox.utils.ConstantManager.MOV;
import static inbox.inbox.utils.ConstantManager.MP4;
import static inbox.inbox.utils.ConstantManager.WEBM;
import static inbox.inbox.utils.ConstantManager.WMV;

import com.fasterxml.jackson.annotation.JsonFormat;
import inbox.inbox.exception.ValidationGroup;
import inbox.inbox.exception.ValidationGroup.PortfolioValidationGroup;
import inbox.inbox.exception.ValuesAllowed;
import java.util.Date;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Portfolio 테이블과 관련된 데이터 옮겨줄 객체의 클래스
@Data
@NoArgsConstructor
public class PortfolioDto {

    @NotEmpty(groups = {ValidationGroup.PortfolioValidationGroup.class}, message = "filePath")
    @ValuesAllowed(values = {MP4, AVI, WEBM, WMV, MOV}, groups = {
        PortfolioValidationGroup.class}, message = "filePath")
    @Size(groups = {
        ValidationGroup.PortfolioValidationGroup.class}, min = 66, max = 69, message = "filePath")
    private String filePath;

    @NotEmpty(groups = {ValidationGroup.PortfolioValidationGroup.class}, message = "range")
    @ValuesAllowed(values = {BE, FE}, groups = {
        PortfolioValidationGroup.class}, message = "range")
    private String range;

    @NotEmpty(groups = {ValidationGroup.PortfolioValidationGroup.class}, message = "title")
    @Size(max = 20, groups = {ValidationGroup.PortfolioValidationGroup.class}, message = "title")
    private String title;

    @NotNull(groups = {ValidationGroup.PortfolioValidationGroup.class}, message = "date")
    @PastOrPresent(groups = {ValidationGroup.PortfolioValidationGroup.class}, message = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private Date date;

    @NotEmpty(groups = {ValidationGroup.PortfolioValidationGroup.class}, message = "about")
    @Size(max = 30, groups = {ValidationGroup.PortfolioValidationGroup.class}, message = "about")
    private String about;

    @Min(value = 1, message = "confirmIdx", groups = {
        ValidationGroup.PortfolioValidationGroup.class})
    private long confirmIdx;

    @Min(value = 100000, message = "confirmCode", groups = {
        ValidationGroup.PortfolioValidationGroup.class})
    @Max(value = 999999, message = "confirmCode", groups = {
        ValidationGroup.PortfolioValidationGroup.class})
    private int confirmCode;

    @NotBlank(groups = {ValidationGroup.PortfolioValidationGroup.class}, message = "email")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%& '*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$", groups = {
        ValidationGroup.PortfolioValidationGroup.class}, message = "email")
    @Size(max = 100, groups = {ValidationGroup.PortfolioValidationGroup.class}, message = "email")
    private String email;

    @Builder
    PortfolioDto(long confirmIdx, int confirmCode, String email) {
        this.confirmIdx = confirmIdx;
        this.confirmCode = confirmCode;
        this.email = email;
    }

}
