package clofi.codeython.problem.domain;

import clofi.codeython.common.domain.BaseEntity;
import clofi.codeython.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@NoArgsConstructor
public class Record extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_no", nullable = false)
    private Long recordNo;

	@Column(name = "written_code", nullable = false, columnDefinition = "TEXT")
	private String writtenCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_no", nullable = false)
	private Member member;

    @ManyToOne
    @JoinColumn(name = "problem_no", nullable = false)
    private Problem problem;

    @Column(name = "language", nullable = false, length = 20)
    private String language;

    @Column(name = "accuracy", nullable = false, columnDefinition = "int default 0")
    private int accuracy;

	@Column(name = "grade")
	private Integer grade;

	@Column(name = "member_cnt")
	private Integer memberCnt;

	public Record(String writtenCode, Member member, Problem problem, String language, int accuracy,
				  Integer grade, Integer memberCnt) {
		this.writtenCode = writtenCode;
		this.member = member;
		this.problem = problem;
		this.language = language;
		this.accuracy = accuracy;
		this.grade = grade;
		this.memberCnt = memberCnt;
	}
}
