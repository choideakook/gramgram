package com.lldj.gram.boundedContext.member;

import com.lldj.gram.base.request.Rq;
import com.lldj.gram.base.request.RsData;
import com.lldj.gram.boundedContext.member.form.MemberJoinForm;
import com.lldj.gram.boundedContext.member.form.MemberLoginForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final Rq rq;

    //-- 가입 폼 --//
    @GetMapping("/join")
    @PreAuthorize("isAnonymous()")
    public String joinForm(MemberJoinForm form) {

        log.info("회원가입 폼 요청 확인");
        return "usr/member/join";
    }

    //-- 가입 처리 --//
    @PostMapping("/join")
    @PreAuthorize("isAnonymous()")
    public String join(@Valid MemberJoinForm form) {

        log.info("join 요청 확인 username = {}", form.getUsername());

        RsData<Member> joinRs = memberService.join(form);

        if (joinRs.isFail()) {
            log.info("회원가입 검증 실패 errors = {}", joinRs.getMsg());
            return rq.historyBack(joinRs);
        }
        log.info("회원가입 성공 member id ={}", joinRs.getData().getId());
        return rq.redirectWithMsg("/member/login", joinRs);
    }

    //-- 로그인 폼 --//
    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String loginForm(MemberLoginForm form) {
        log.info("로그인 폼 요청 확인");
        return "usr/member/login";
    }

    //-- 프로필 --//
    @GetMapping("/profile")
    public String profile() {
        return "usr/member/profile";
    }

    //-- 호감 목록 --//

}
