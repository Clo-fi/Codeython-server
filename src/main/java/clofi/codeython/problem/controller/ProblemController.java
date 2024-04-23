package clofi.codeython.problem.controller;

import clofi.codeython.member.service.dto.CustomMemberDetails;
import clofi.codeython.problem.controller.response.AllProblemResponse;
import clofi.codeython.problem.controller.response.GetProblemResponse;
import clofi.codeython.problem.controller.response.RecordResponse;
import clofi.codeython.problem.domain.request.CreateProblemRequest;
import clofi.codeython.problem.service.ProblemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @PostMapping("/api/problems")
    public ResponseEntity<Long> createProblem(
            @RequestBody @Valid CreateProblemRequest createProblemRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(problemService.createProblem(createProblemRequest));
    }

    @GetMapping("/api/problems")
    public ResponseEntity<List<AllProblemResponse>> getAllProblem(
            @AuthenticationPrincipal CustomMemberDetails userDetails) {

        return ResponseEntity.ok(problemService.getAllProblem(userDetails.getMember()));
    }

    @GetMapping("/api/problems/{problemId}")
    public ResponseEntity<GetProblemResponse> getProblem(@PathVariable("problemId") Long problemNo,
                                                         @AuthenticationPrincipal CustomMemberDetails userDetails) {
        return ResponseEntity.ok(problemService.getProblem(problemNo, userDetails.getMember()));
    }

    @GetMapping("/api/recent-records")
    public ResponseEntity<List<RecordResponse>> getRecord(
            @AuthenticationPrincipal CustomMemberDetails userDetails) {
        String username = userDetails.getUsername();

        return ResponseEntity.ok(problemService.getRecord(username));
    }

}
