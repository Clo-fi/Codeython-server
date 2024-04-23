package clofi.codeython.problem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import clofi.codeython.problem.domain.Problem;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    Problem findByProblemNo(Long problemNo);

    boolean existsByTitle(String title);
}
