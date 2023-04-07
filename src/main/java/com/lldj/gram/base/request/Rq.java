package com.lldj.gram.base.request;

import com.lldj.gram.boundedContext.member.Member;
import com.lldj.gram.boundedContext.member.MemberService;
import com.lldj.gram.standard.util.Ut;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Date;

@Component
@RequestScope
public class Rq {

    //-- field --//
    private final MemberService memberService;
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final HttpSession session;
    private final User user;

    // 레이지 로딩, 요청이 들어올 때 값을 입력 //
    private Member member = null;

    //-- 생성자 주입 --//
    public Rq(MemberService memberService, HttpServletRequest req, HttpServletResponse resp, HttpSession session) {
        this.memberService = memberService;
        this.req = req;
        this.resp = resp;
        this.session = session;

        // 현재 로그인한 회원의 인증정보를 가져옴 //
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 있다면 로그인 중임 //
        if (authentication.getPrincipal() instanceof User)
            this.user = (User) authentication.getPrincipal();

            // 없다면 로그아웃 중임 //
        else
            this.user = null;
    }

    //-- 로그인 되어 있는지 체크 --//
    public boolean isLogin() {
        return user != null;
    }

    //-- 로그아웃 되어 있는지 체크 --//
    public boolean isLogout() {
        return !isLogin();
    }

    //-- 로그인 된 회원의 객체 반환 --//
    public Member getMember() {
        if (isLogout()) return null;

        // 데이터가 없는지 체크
        if (member == null)
            member = memberService.findByUsername(user.getUsername()).orElseThrow();

        return member;
    }

    //-- history back chain --//

    // 뒤로가기 + 메세지 //
    public String historyBack(RsData rsData) {
        return historyBack(rsData.getMsg());
    }

    // 뒤로가기 + 메세지 //
    public String historyBack(String msg) {
        String referer = req.getHeader("referer");
        String key = "historyBackErrorMsg___" + referer;
        req.setAttribute("localStorageKeyAboutHistoryBackErrorMsg", key);
        req.setAttribute("historyBackErrorMsg", msg);
        return "common/js";
    }

    // 302 + 메세지 //
    public String redirectWithMsg(String url, RsData rsData) {
        return redirectWithMsg(url, rsData.getMsg());
    }

    // 302 + 메세지 //
    public String redirectWithMsg(String url, String msg) {
        return "redirect:" + urlWithMsg(url, msg);
    }

    private String urlWithMsg(String url, String msg) {
        // 기존 url 에 msg 파라미터가 있다면 그것을 지우고 새로 넣는다.
        return Ut.url.modifyQueryParam(url, "msg", msgWithTtl(msg));
    }

    // 메세지에 ttl 적용 //
    private String msgWithTtl(String msg) {
        return Ut.url.encode(msg) + ";ttl=" + new Date().getTime();
    }
}
