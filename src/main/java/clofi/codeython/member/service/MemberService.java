package clofi.codeython.member.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import clofi.codeython.member.controller.response.MemberResponse;
import clofi.codeython.member.domain.Member;
import clofi.codeython.member.domain.request.CreateMemberRequest;
import clofi.codeython.member.domain.request.UpdateMemberRequest;
import clofi.codeython.member.repository.MemberRepository;
import clofi.codeython.member.service.dto.CustomMemberDetails;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long signUp(CreateMemberRequest createMemberRequest) {
        if (memberRepository.existsByNickname(createMemberRequest.getNickname())) {
            throw new IllegalArgumentException("이미 존재한 닉네임입니다.");
        }
        if (memberRepository.existsByUsername(createMemberRequest.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디 입니다.");
        }
        Member member = memberRepository.save(createMemberRequest.toMember(bCryptPasswordEncoder));
        return member.getUserNo();
    }

    public Long update(String userName, UpdateMemberRequest updateMemberRequest) {
        Member memberId = memberRepository.findByUsername(userName);
        Member member = memberRepository.findByUserNo(memberId.getUserNo())
            .orElseThrow(() -> new EntityNotFoundException("일치하는 사용자가 없습니다."));
        if (memberRepository.existsByNickname(updateMemberRequest.getNickname())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        member.updateNickName(updateMemberRequest.getNickname());
        return member.getUserNo();
    }

    public MemberResponse getMember(String userName) {
        Member member = memberRepository.findByUsername(userName);
        Integer exp = member.getExp();
        int level = 1;
        if (exp > 100) {
            level = exp / 100 + 1;
            exp = exp % 100;
        }
        return MemberResponse.of(member, level, exp);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member memberData = memberRepository.findByUsername(username);
        if (memberData != null) {
            return new CustomMemberDetails(memberData);
        }
        return null;
    }
}
