package hello.commute.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository{

    @PersistenceContext
    private final EntityManager em;


    @Override
    public void saveMember(Member member) {
        em.persist(member);
    }

    @Override
    public Optional<List<Member>> findAll() {
     return Optional.ofNullable(em.createQuery("select m from Member m", Member.class).getResultList());
    }

    @Override
    public Optional<Member> findMember(Long id) {
        return em.createQuery("select m from Member m where m.id = :id",Member.class)
                .setParameter("id", id)
                .getResultList().stream().findAny();
    }

    @Override
    public Optional<Member> findMember(String loginId) {
        return em.createQuery("select m from Member m where m.loginId = :loginId",Member.class)
                .setParameter("loginId", loginId)
                .getResultList().stream().findAny();
    }

    @Override
    public void deleteMember(Long id) {
        em.remove(findMember(id));
    }

    @Override
    public void deleteMember(String loginId) {
        em.remove(findMember(loginId));
    }
}
