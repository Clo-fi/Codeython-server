package clofi.codeython.problem.controller.response;

import clofi.codeython.problem.domain.LanguageType;

public record BaseCodeResponse(
        LanguageType language, String code) {
}
