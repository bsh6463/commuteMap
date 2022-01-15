package hello.commute.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Member addMember(Member member) {
        memberRepository.saveMember(member);
        return member;
    }

    @Override
    public Member findMember(Long id) {
        return memberRepository.findMember(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Member findMember(String loginId) {
        return memberRepository.findMember(loginId).orElseThrow(NoSuchElementException::new);
    }

    @Override
    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteMember(id);
    }

    @Override
    @Transactional
    public void deleteMember(String loginId) {
        memberRepository.deleteMember(loginId);
    }
}
