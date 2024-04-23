package clofi.codeython.problem.controller.response;

import java.util.List;


public record TestcaseResponse(
        List<String> inputCase, String outputCase, String description
) {
}
