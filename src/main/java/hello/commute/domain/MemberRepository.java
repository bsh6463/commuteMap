package hello.commute.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    void saveMember(Member member);
    Optional<List<Member>> findAll();
    Optional<Member> findMember(Long id);
    Optional<Member> findMember(String loginId);
    void deleteMember(Long id);
    void deleteMember(String loginId);
}
