package com.lldj.gram.boundedContext.member;

import com.lldj.gram.base.request.RsData;
import com.lldj.gram.boundedContext.instagram.Instagram;
import com.lldj.gram.boundedContext.likeable.Likeable;
import com.lldj.gram.boundedContext.member.form.MemberJoinForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

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

//    @Test
//    void initDB_테스트() {
//        Member member = memberService.findByUsername("user1").get();
//
//        List<Instagram> instagramList = member.getInstagramList();
//        List<Likeable> likeableList = member.getLikeableList();
//
//        System.out.println("== 호감을 표시한 instagram 목록 ==");
//        for (Instagram instagram : instagramList) {
//            System.out.println(instagram.getUsername() + "\n");
//        }
//
//        System.out.println("\n== 호감을 표시한 likeable 목록 ==");
//        for (Likeable likeable : likeableList) {
//            System.out.println(likeable.getAttractive() + "\n");
//        }
//    }
}