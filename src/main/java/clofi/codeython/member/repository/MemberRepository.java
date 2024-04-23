package clofi.codeython.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import clofi.codeython.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserNo(Long userNo);

    Member findByUsername(String userName);

    Boolean existsByNickname(String nickName);

    Boolean existsByUsername(String userName);

    Member findByNickname(String nickName);

	List<Member> findTop5ByOrderByExpDesc();

    List<Member> findAllByOrderByExpDesc();
}
