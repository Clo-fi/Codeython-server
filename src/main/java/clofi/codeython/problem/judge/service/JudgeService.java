package clofi.codeython.problem.judge.service;

import clofi.codeython.problem.judge.domain.ResultCalculator;
import clofi.codeython.problem.judge.domain.creator.ExecutionFileCreator;
import clofi.codeython.problem.judge.dto.SubmitRequest;
import clofi.codeython.problem.domain.LanguageType;
import clofi.codeython.problem.domain.Problem;
import clofi.codeython.problem.domain.Testcase;
import clofi.codeython.problem.repository.ProblemRepository;
import clofi.codeython.problem.repository.TestcaseRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
@Service
public class JudgeService {
    private final Map<String, ExecutionFileCreator> executionFileCreatorMap;
    private final ResultCalculator resultCalculator;
    private final ProblemRepository problemRepository;
    private final TestcaseRepository testcaseRepository;

    public int submit(SubmitRequest submitRequest, Long problemNo) {
        Problem problem = problemRepository.findById(problemNo)
                .orElseThrow(() -> new IllegalArgumentException("없는 문제 번호입니다."));

        String route = UUID.randomUUID() + "/";
        createFolder(route);
        try {
            ExecutionFileCreator executionFileCreator = executionFileCreatorMap
                    .get(LanguageType.getCreatorName(submitRequest.getLanguage()));

            executionFileCreator.create(problem.getType(), submitRequest.getCode(), route);

            List<Testcase> testcases = testcaseRepository.findAllByProblemProblemNo(problemNo);

            return resultCalculator.calculate(route, submitRequest.getLanguage(), testcases);
        } finally {
            cleanup(route);
        }
    }

    private void createFolder(String route) {
        try {
            Files.createDirectory(Path.of(route));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void cleanup(String route) {
        try {
            FileUtils.deleteDirectory(new File(route));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
