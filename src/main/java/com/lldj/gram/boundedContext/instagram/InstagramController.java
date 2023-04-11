package com.lldj.gram.boundedContext.instagram;

import com.lldj.gram.base.request.Rq;
import com.lldj.gram.base.request.RsData;
import com.lldj.gram.boundedContext.instagram.form.ConnectForm;
import com.lldj.gram.boundedContext.member.Member;
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
@RequestMapping("/instagram")
@RequiredArgsConstructor
public class InstagramController {

    private final InstagramService instagramService;
    private final Rq rq;

    //-- 인스타 연동 폼 --//
    @GetMapping("/connect")
    @PreAuthorize("isAuthenticated()")
    public String connect(ConnectForm form) {
        log.info("인스타 연동 요청 확인");
        return "usr/instagram/connect";
    }

    //-- 인스타 연동 처리 --//
    @PostMapping("/connect")
    @PreAuthorize("isAuthenticated()")
    public String showConnect(@Valid ConnectForm form) {
        log.info("인스타 연동 요청 확인 instagram name = {}", form.getUsername());

        Member member = rq.getMember();
        RsData<Instagram> instagramRs = instagramService.connection(form.getUsername(), form.getGender(), member);

        if (instagramRs.isFail()) {
            log.info("연동실패 msg = {}", instagramRs.getMsg());
            return rq.historyBack(instagramRs);
        }
        log.info("인스타 연동 성공 instagram name = {}", form.getUsername());
        return rq.redirectWithMsg("/", instagramRs);
    }
}
