package hello.commute.domain;

public interface MemberService {

    Member addMember(Member member);
    Member findMember(Long id);
    Member findMember(String loginId);
    void deleteMember(Long id);
    void deleteMember(String loginId);

}
