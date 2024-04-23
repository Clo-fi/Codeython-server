package clofi.codeython.problem.repository;

import clofi.codeython.member.domain.Member;
import clofi.codeython.problem.domain.Problem;
import clofi.codeython.problem.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {

    Optional<Record> findByProblem(Problem problem);

    List<Record> findAllByMemberOrderByUpdatedAtDesc(Member member);

    List<Record> findAllByProblemAndMemberOrderByCreatedAtDesc(Problem problem, Member member);

    List<Record> findAllByProblemAndMember(Problem problem, Member member);
}
