package clofi.codeython.problem.judge.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import clofi.codeython.problem.domain.Problem;
import clofi.codeython.problem.domain.Testcase;
import clofi.codeython.problem.judge.dto.ExecutionRequest;
import clofi.codeython.problem.judge.dto.ExecutionResponse;
import clofi.codeython.problem.judge.dto.SubmitRequest;
import clofi.codeython.problem.repository.ProblemRepository;
import clofi.codeython.problem.repository.TestcaseRepository;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class JudgeServiceTest {
    @Autowired
    JudgeService judgeService;

    @MockBean
    ProblemRepository problemRepository;

    @MockBean
    TestcaseRepository testcaseRepository;

    @BeforeEach
    void setUp() {
        given(problemRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new Problem(
                "title", "content", null, 1, 1, List.of("int", "int[]")
        )));
        given(testcaseRepository.findAllByProblemProblemNo(Mockito.any())).willReturn(List.of(
                new Testcase(null,
                        List.of("3", "[1, 2, 3]"),
                        "[2,4,6]", "description")
        ));
    }

    @DisplayName("자바 코드로 채점을 진행할 수 있다.")
    @Test
    void javaCodeSubmitTest() {
        // given
        SubmitRequest submitRequest = new SubmitRequest("java", """
                class Solution {
                    public int[] solution(int N, int[] values) {
                        int[] answer = new int[values.length];
                        for (int i = 0; i < N; i++) {
                            answer[i] = values[i] * 2;
                        }
                        return answer;
                    }
                }
                """, null);

        // when
        int actual = judgeService.submit(submitRequest, 1L);

        // then
        assertThat(actual).isEqualTo(100);
    }

    @DisplayName("자바스크립트 코드로 채점을 진행할 수 있다.")
    @Test
    void javascriptCodeSubmitTest() {
        // given
        SubmitRequest submitRequest = new SubmitRequest("javascript", """
                function solution(N, values) {
                  return values.map(v => v * 2)
                }
                """, null);

        // when
        int actual = judgeService.submit(submitRequest, 1L);

        // then
        assertThat(actual).isEqualTo(100);
    }

    @DisplayName("지원하지 않는 언어를 제출하면 오류가 발생한다.")
    @Test
    void invalidLanguageSubmitTest() {
        // given
        SubmitRequest submitRequest = new SubmitRequest("go", """
                function solution(N, values) {
                  return values.map(v => v * 2)
                }
                """, null);

        // when & then
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> judgeService.submit(submitRequest, 1L))
                .withMessage("GO(은)는 지원하지 않는 언어 종류입니다.");
    }

    @DisplayName("코드 채점 중 Exception이 발생하면 오류가 발생한다.")
    @Test
    void exceptionCodeSubmitTest() {
        // given
        SubmitRequest submitRequest = new SubmitRequest("java", """
                class Solution {
                    public int[] solution(int N, int[] values) {
                        int[] answer = new int[values.length];
                        for (int i = 0; i < N; i++) {
                            answer[i] = values[i] * 2;
                        }
                        throw new IllegalArgumentException("예외 발생");
                        return answer;
                    }
                }
                """, null);

        // when & then
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> judgeService.submit(submitRequest, 1L));
    }

    @DisplayName("자바 코드 실행결과 및 정답 여부를 확인할 수 있다.")
    @Test
    void javaCodeExecutionTest() {
        // given
        ExecutionRequest executionRequest = new ExecutionRequest("java", """
                class Solution {
                    public int[] solution(int N, int[] values) {
                        int[] answer = new int[values.length];
                        for (int i = 0; i < N; i++) {
                            answer[i] = values[i] * 2;
                        }
                        return answer;
                    }
                }
                """);

        // when
        List<ExecutionResponse> actual = judgeService.execution(executionRequest, 1L);

        // then
        assertThat(actual).containsExactly(new ExecutionResponse(true, "[2,4,6]"));
    }

    @DisplayName("자바스크립트 코드 실행결과 및 정답 여부를 확인할 수 있다.")
    @Test
    void javascriptCodeExecutionTest() {
        // given
        ExecutionRequest executionRequest = new ExecutionRequest("javascript", """
                function solution(N, values) {
                  return values.map(v => v * 2)
                }
                """);

        // when
        List<ExecutionResponse> actual = judgeService.execution(executionRequest, 1L);

        // then
        assertThat(actual).containsExactly(new ExecutionResponse(true, "[2,4,6]"));
    }

    @DisplayName("실행결과 및 정답 여부를 확인할 수 있다.(오답 코드)")
    @Test
    void incorrectOutputCodeTest() {
        // given
        ExecutionRequest executionRequest = new ExecutionRequest("java", """
                class Solution {
                    public int[] solution(int N, int[] values) {
                        int[] answer = new int[values.length];
                        System.out.println("Test message");
                        for (int i = 0; i < N; i++) {
                            answer[i] = values[i] * 2;
                        }
                        return answer;
                    }
                }
                """);

        // when
        List<ExecutionResponse> actual = judgeService.execution(executionRequest, 1L);

        // then
        assertThat(actual).containsExactly(new ExecutionResponse(false, String.format("Test message%n[2,4,6]")));
    }

    @DisplayName("코드 실행 중 Exception이 발생하면 오류가 발생한다.")
    @Test
    void exceptionCodeExecutionTest() {
        // given
        ExecutionRequest executionRequest = new ExecutionRequest("java", """
                class Solution {
                    public int[] solution(int N, int[] values) {
                        int[] answer = new int[values.length];
                        for (int i = 0; i < N; i++) {
                            answer[i] = values[i] * 2;
                        }
                        throw new IllegalArgumentException("예외 발생");
                        return answer;
                    }
                }
                """);

        // when & then
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> judgeService.execution(executionRequest, 1L));
    }

    @DisplayName("description이 없는 테스트 케이스는 실행하지 않는다.")
    @Test
    void emptyDescriptionTestcaseTest() {
        given(testcaseRepository.findAllByProblemProblemNo(Mockito.any())).willReturn(List.of(
                new Testcase(null,
                        List.of("3", "[1, 2, 3]"),
                        "[2,4,6]", null)
        ));
        // given
        ExecutionRequest executionRequest = new ExecutionRequest("java", """
                class Solution {
                    public int[] solution(int N, int[] values) {
                        int[] answer = new int[values.length];
                        for (int i = 0; i < N; i++) {
                            answer[i] = values[i] * 2;
                        }
                        return answer;
                    }
                }
                """);

        // when
        List<ExecutionResponse> execution = judgeService.execution(executionRequest, 1L);

        // then
        assertThat(execution).isEmpty();
    }
}
