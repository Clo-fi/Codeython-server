package clofi.codeython.problem.domain;

import clofi.codeython.common.util.StringListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity @Getter
@NoArgsConstructor
public class Testcase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "testcase_no", nullable = false)
    private Long testcaseNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_no", nullable = false)
    private Problem problem;

    @ElementCollection
    @Column(name = "input", nullable = false, columnDefinition = "TEXT")
    private List<String> input;

    @Column(name = "output", nullable = false, columnDefinition = "TEXT")
    private String output;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    public Testcase(Problem problem, List<String> input, String output, String description) {
        this.problem = problem;
        this.input = input;
        this.output = output;
        this.description = description;
    }

    public Testcase(Problem problem, List<String> input, String output) {
        this(problem, input, output, "");
    }
}
