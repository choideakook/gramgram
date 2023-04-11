package com.lldj.gram.boundedContext.likeable;

import com.lldj.gram.base.request.Rq;
import com.lldj.gram.base.request.RsData;
import com.lldj.gram.boundedContext.likeable.form.LikeableAddForm;
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
@RequestMapping("/likeable")
@RequiredArgsConstructor
public class LikeableController {

    private final LikeableService likeableService;
    private final Rq rq;

    //-- 호감 등록 폼 --//
    @GetMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String addLike(LikeableAddForm form) {
        log.info("호감 등록 폼 요청 확인");
        return "usr/likeable/add";
    }

    //-- 호감 등록 처리 --//
    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String showAddLike(@Valid LikeableAddForm form) {
        log.info("호감 등록 요청 확인 form = {}", form);
        Member member = rq.getMember();

        RsData<Likeable> likeableRs
                = likeableService.like(form.getInstagramName(), form.getAttractive(), member);
        if (likeableRs.isFail()) {
            log.info("호감 등록 실패 msg = {}", likeableRs.getMsg());
            return rq.historyBack(likeableRs);
        }

        log.info("호감 등록 성공 likeable = {}", likeableRs.getData().toString());
        return rq.redirectWithMsg("/likeable/list", likeableRs);
    }

    //-- 호감 리스트 --//
    @GetMapping("list")
    @PreAuthorize("isAuthenticated()")
    public String list() {
        return "usr/likeable/list";
    }
}
