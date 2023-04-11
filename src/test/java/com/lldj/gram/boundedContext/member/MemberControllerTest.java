package com.lldj.gram.boundedContext.member;

import com.lldj.gram.base.request.Rq;
import com.lldj.gram.base.request.RsData;
import com.lldj.gram.boundedContext.member.form.MemberJoinForm;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private MemberService memberService;
    @Autowired private Rq rq;

    @Test
    void 회원가입_처리() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/member/join")
                .with(csrf())
                .param("username", "user10")
                .param("password", "1234")
                .param("password2", "1234")
                ).andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/member/login?msg=**"))
        ;

        Member member = memberService.findByUsername("user10").orElse(null);
        assertThat(member).isNotNull();
    }

    @Test
    void 로그인_폼() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/member/login"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("loginForm"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        <span class="label-text">비빌번호</span>
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <span>로그인</span>
                        """.stripIndent().trim())));
    }

    private Member createMember() {
        MemberJoinForm form = new MemberJoinForm("user1", "1234", "1234");
        RsData<Member> createMember = memberService.join(form);
        return createMember.getData();
    }

    @Test
    // @Rollback(value = false) // DB에 흔적이 남는다.
    @DisplayName("로그인 처리")
    void t005() throws Exception {
        createMember();
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/member/login")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "user1")
                        .param("password", "1234")
                )
                .andDo(print());

        // 세션에 접근해서 user 객체를 가져온다.
        MvcResult mvcResult = resultActions.andReturn();
        HttpSession session = mvcResult.getRequest().getSession(false);// 원래 getSession 을 하면, 만약에 없을 경우에 만들어서라도 준다., false 는 없으면 만들지 말라는 뜻
        SecurityContext securityContext = (SecurityContext)session.getAttribute("SPRING_SECURITY_CONTEXT");
        User user = (User)securityContext.getAuthentication().getPrincipal();

        assertThat(user.getUsername()).isEqualTo("user1");

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/**"));

    }
}