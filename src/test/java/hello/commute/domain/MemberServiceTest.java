package hello.commute.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = true)
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    EntityManager em;

    @Test
    @Transactional
    void crudTest(){
        Member member1 = new Member("loginId", "password");

        memberService.addMember(member1);

        em.flush();
        em.clear();

        Member findMember = memberService.findMember(member1.getLoginId());
        assertThat(findMember.getLoginId()).isEqualTo(member1.getLoginId());
        assertThat(findMember.getLoginPw()).isEqualTo(member1.getLoginPw());

    }

}