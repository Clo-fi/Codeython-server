package clofi.codeython.problem.domain;

import clofi.codeython.common.domain.BaseEntity;
import clofi.codeython.common.util.StringListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity @Getter
@DynamicInsert
@NoArgsConstructor
public class Problem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_no", nullable = false)
    private Long problemNo;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Convert(converter = StringListConverter.class)
    @Column(name = "limit_factor", columnDefinition = "TEXT")
    private List<String> limitFactor;

    @Column(name = "limit_time", nullable = false)
    private int limitTime;

    @Column(name = "difficulty", nullable = false, columnDefinition = "int default 1")
    private int difficulty;

    @Convert(converter = StringListConverter.class)
    @Column(name = "type", nullable = false, length = 50)
    private List<String> type;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Problem(String title, String content, List<String> limitFactor,
                   int limitTime, int difficulty, List<String> type) {
        this.title = title;
        this.content = content;
        this.limitFactor = limitFactor;
        this.limitTime = limitTime;
        this.difficulty = difficulty;
        this.type = type;
    }

}
