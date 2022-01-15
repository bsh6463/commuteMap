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

    /**
     * DB에 잘 저장되는지, 잘 불러와지는지 확인하기 위한 목적.
     */
    @Test
    @Transactional
    void crudTest(){
        //given
        Member member1 = new Member("loginId", "password");

        //when
        memberService.addMember(member1);

        //entityManager 초기화 -> 캐시삭제
        em.flush();
        em.clear();

        Member findMember = memberService.findMember(member1.getLoginId());

        //then
        assertThat(findMember.getLoginId()).isEqualTo(member1.getLoginId());
        assertThat(findMember.getLoginPw()).isEqualTo(member1.getLoginPw());

    }

}