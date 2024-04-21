package clofi.codeython.problem.domain.request;

import clofi.codeython.problem.domain.LanguageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BaseCodeRequest {
    @NotNull
    @NotBlank(message = "언어는 공백일 수 없습니다.")
    private LanguageType language;

    @NotBlank(message = "코드는 공백일 수 없습니다.")
    private String code;

    public BaseCodeRequest(LanguageType language, String code) {
        this.language = language;
        this.code = code;
    }
}
