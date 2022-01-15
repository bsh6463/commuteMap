package hello.commute.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private String loginId;
    private String loginPw;

    public Member() {
    }

    public Member(String loginId, String loginPw) {
        this.loginId = loginId;
        this.loginPw = loginPw;
    }

}
