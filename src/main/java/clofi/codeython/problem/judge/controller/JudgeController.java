package clofi.codeython.problem.judge.controller;

import clofi.codeython.member.service.dto.CustomMemberDetails;
import clofi.codeython.problem.judge.dto.ExecutionRequest;
import clofi.codeython.problem.judge.dto.ExecutionResponse;
import clofi.codeython.problem.judge.dto.SubmitRequest;
import clofi.codeython.problem.judge.dto.SubmitResponse;
import clofi.codeython.problem.judge.service.JudgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class JudgeController {
    private final JudgeService judgeService;

    @PostMapping("/api/problems/{problemId}/result")
    public SubmitResponse submit(@RequestBody SubmitRequest submitRequest,
                                 @PathVariable("problemId") long problemNo,
                                 @AuthenticationPrincipal CustomMemberDetails userDetails) {
        return judgeService.submit(submitRequest, problemNo, userDetails.getMember());
    }

    @PostMapping("/api/problems/{problemId}/execution")
    public List<ExecutionResponse> execution(@RequestBody ExecutionRequest executionRequest,
                                             @PathVariable("problemId") long problemNo) {
        return judgeService.execution(executionRequest, problemNo);
    }
}
