package com.lldj.gram.base.init;

import com.lldj.gram.base.request.RsData;
import com.lldj.gram.boundedContext.instagram.InstagramService;
import com.lldj.gram.boundedContext.likeable.LikeableService;
import com.lldj.gram.boundedContext.member.Member;
import com.lldj.gram.boundedContext.member.MemberService;
import com.lldj.gram.boundedContext.member.form.MemberJoinForm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//@Configuration
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final MemberService memberService;
        private final InstagramService instagramService;
        private final LikeableService likeableService;

        public void dbInit1() {
            MemberJoinForm form1 = new MemberJoinForm("user1", "1234", "1234");
            MemberJoinForm form2 = new MemberJoinForm("user2", "1234", "1234");
            Member member1 = memberService.join(form1).getData();
            Member member2 = memberService.join(form2).getData();

            instagramService.connection("instagramA", "M", member1);
            instagramService.connection("instagramB", "W", member2);

            for (int i = 0; i < 10; i++)
                likeableService.like("attractive" + i, 1, member1);
        }
    }
}
