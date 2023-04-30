package com.lldj.gram.boundedContext.instagram;

import com.lldj.gram.base.request.Rq;
import com.lldj.gram.base.request.RsData;
import com.lldj.gram.boundedContext.instagram.form.ConnectForm;
import com.lldj.gram.boundedContext.likeable.Likeable;
import com.lldj.gram.boundedContext.likeable.LikeableService;
import com.lldj.gram.boundedContext.member.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/instagram")
@RequiredArgsConstructor
public class InstagramController {

    private final InstagramService instagramService;
    private final LikeableService likeableService;
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
            return rq.historyBack(instagramRs.getMsg());
        }
        log.info("인스타 연동 성공 instagram name = {}", form.getUsername());
        return rq.redirectWithMsg("/", instagramRs);
    }

    //-- 인스타그램 상세페이지 --//
    @GetMapping("detail/{id}")
    public String InstagramDetail(
            @PathVariable Long id,
            Model model
    ) {
        log.info("인스타그램 상세페이지 요청 확인 instagram id = {}", id);
        Member member = rq.getMember();
        RsData<Instagram> instagramRs = instagramService.findOne(id);

        if (instagramRs.isFail()){
            log.info("인스타그램 조회 실패 msg = {}", instagramRs.getMsg());
            return rq.historyBack(instagramRs);
        }

        Long likeableId = instagramService.likeableFinder(member, instagramRs.getData());

        model.addAttribute("likeableId", likeableId);
        model.addAttribute("instagram", instagramRs.getData());
        log.info("인스타그램 상세페이지 응답 instagram = {}", instagramRs.getData().toString());
        return "usr/instagram/detail";

    }
}
