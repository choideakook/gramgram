package com.lldj.gram.boundedContext.likeable;

import com.lldj.gram.base.request.RsData;
import com.lldj.gram.boundedContext.instagram.Instagram;
import com.lldj.gram.boundedContext.instagram.InstagramService;
import com.lldj.gram.boundedContext.member.Member;
import com.lldj.gram.boundedContext.member.MemberService;
import com.lldj.gram.boundedContext.member.form.MemberJoinForm;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LikeableServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private InstagramService instagramService;
    @Autowired
    private LikeableService likeableService;

    private Member createMember(String name) {
        MemberJoinForm form = new MemberJoinForm(name, "1234", "1234");

        RsData<Member> createMember = memberService.join(form);
        return createMember.getData();
    }

    private Instagram createInstagram(String name, Member member) {
        RsData<Instagram> instagrams = instagramService.connection(name, "M", member);
        return instagrams.getData();
    }

    @Test
    void 호감표시_등록() {
        Member member1 = createMember("user1");
        Instagram instagram1 = createInstagram("instagram1", member1);

        Member member2 = createMember("user2");
        Instagram instagram2 = createInstagram("instagram2", member2);

        RsData<Likeable> likeableRs = likeableService.like("instagram2", 1, member1);
        Likeable likeable = likeableRs.getData();

        assertThat(likeableRs.getResultCode()).isEqualTo("S-1");
        assertThat(likeable.getAttractive()).isEqualTo("외모");
        assertThat(likeable.getMember()).isSameAs(member1);
        assertThat(likeable.getMember().getInstagramName()).isEqualTo(instagram1.getUsername());
        assertThat(likeable.getMember().getUsername()).isSameAs("user1");
        assertThat(likeable.getInstagram()).isSameAs(instagram2);
        assertThat(likeable.getInstagram().getGender()).isSameAs("M");
    }

    @Test
    void 인스타그램_연동없이_호감표시_금지() {
        Member member = createMember("user");

        RsData<Likeable> likeableRs = likeableService.like("instagram", 1, member);

        assertThat(likeableRs.getResultCode()).isEqualTo("F-1");
    }

    @Test
    void 호감대상이_없을때_호감등록() {
        Member member = createMember("user");
        Instagram instagram = createInstagram("instagram1", member);

        RsData<Likeable> likeableRs = likeableService.like("instagram", 1, member);
        Likeable likeable = likeableRs.getData();

        assertThat(likeableRs.getResultCode()).isEqualTo("S-1");
        assertThat(likeable.getInstagram().getUsername()).isEqualTo("instagram");
        assertThat(likeable.getInstagram().getGender()).isEqualTo("U");
    }

    @Test
    void 자신에게_호감표시_금지() {
        Member member = createMember("user");
        Instagram instagram = createInstagram("instagram", member);

        RsData<Likeable> likeableRs = likeableService.like("instagram", 1, member);

        assertThat(likeableRs.getResultCode()).isEqualTo("F-2");
    }

    @Test
    void 호감표시는_최대_10명까지_가능() {
        Member member = createMember("user");
        createInstagram("instagram", member);

        for (int i = 0; i < 10; i++)
            likeableService.like("instagram" + i, 1, member);

        assertThat(member.getLikeableList().size()).isEqualTo(10);

        RsData<Likeable> instagram11 = likeableService.like("Instagram11", 1, member);

        assertThat(instagram11.getResultCode()).isEqualTo("F-3");
    }
}