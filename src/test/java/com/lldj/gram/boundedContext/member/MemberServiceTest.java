package com.lldj.gram.boundedContext.member;

import com.lldj.gram.base.request.RsData;
import com.lldj.gram.boundedContext.member.form.MemberJoinForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;

    private Member createMember() {
        MemberJoinForm form = new MemberJoinForm("User", "1234", "1234");

        RsData<Member> createMember = memberService.join(form);
        return createMember.getData();
    }

    @Test
    void 회원_객체_생성() {
        Member member = createMember();

        Optional<Member> byUsername = memberService.findByUsername(member.getUsername());
        assertThat(byUsername.isPresent()).isTrue();

        Member findMember = byUsername.get();
        assertThat(findMember.getProviderTypeCode()).isEqualTo("GRAMGRAM");
        assertThat(findMember.getUsername()).isEqualTo("User");
        assertThat(findMember.getCreateDate()).isNotNull();
        System.out.println("password = " + findMember.getPassword());
        System.out.println("create data = " + findMember.getCreateDate());
    }
}