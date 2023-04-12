package com.lldj.gram.boundedContext.instagram;

import com.lldj.gram.base.request.RsData;
import com.lldj.gram.boundedContext.likeable.Likeable;
import com.lldj.gram.boundedContext.likeable.LikeableService;
import com.lldj.gram.boundedContext.member.Member;
import com.lldj.gram.boundedContext.member.MemberService;
import com.lldj.gram.boundedContext.member.form.MemberJoinForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class InstagramServiceTest {

    @Autowired MemberService memberService;
    @Autowired InstagramService instagramService;
    @Autowired LikeableService likeableService;

    private Member createMember(String name) {
        MemberJoinForm form = new MemberJoinForm(name, "1234", "1234");

        RsData<Member> createMember = memberService.join(form);
        return createMember.getData();
    }

    @Test
    void 인스타_연동() {
        Member member = createMember("user");
        RsData<Instagram> connection
                = instagramService.connection("instaA", "male", member);

        Instagram instagram = connection.getData();
        RsData<Instagram> instagramRs = instagramService.findByName("instaA");
        Instagram findInstagram = instagramRs.getData();

        assertThat(connection.getResultCode()).isEqualTo("S-1");
        assertThat(instagram.getUsername()).isEqualTo("instaA");

        assertThat(instagram.getUsername()).isEqualTo(member.getInstagramName());
        assertThat(instagram).isSameAs(findInstagram);
    }

    @Test
    void 중복연동_금지() {
        Member memberA = createMember("userA");
        Member memberB = createMember("userB");
        RsData<Instagram> connectionA
                = instagramService.connection("instaA", "male", memberA);
        assertThat(connectionA.getResultCode()).isEqualTo("S-1");

        RsData<Instagram> connectionB
                = instagramService.connection("instaA", "male", memberB);
        assertThat(connectionB.getResultCode()).isEqualTo("F-1");
        System.out.println(connectionB.getMsg());
    }

    @Test
    void 이미_인스타가_생성되있을경우_연동() {
        Member member1 = createMember("user1");
        Instagram instagram1 = instagramService.connection("instagram1", "M", member1).getData();

        Likeable likeable = likeableService.like("instagra2", 1, member1).getData();

        assertThat(member1.getLikeableList().contains(likeable)).isTrue();
        assertThat(likeable.getInstagram().getGender()).isEqualTo("U");
        assertThat(likeable.getInstagram().getMemberName()).isNull();

        Member member2 = createMember("user2");
        instagramService.connection("instagra2", "M", member2);

        assertThat(member1.getLikeableList().contains(likeable)).isTrue();
        assertThat(likeable.getInstagram().getGender()).isEqualTo("M");
        assertThat(likeable.getInstagram().getMemberName()).isEqualTo("user2");
    }
}