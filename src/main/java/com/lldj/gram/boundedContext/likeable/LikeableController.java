package com.lldj.gram.boundedContext.likeable;

import com.lldj.gram.base.request.Rq;
import com.lldj.gram.base.request.RsData;
import com.lldj.gram.boundedContext.instagram.Instagram;
import com.lldj.gram.boundedContext.likeable.form.LikeableAddForm;
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

import java.util.List;

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
    public String list(Model model) {
        log.info("호감 리스트 요청 확인");

        Member member = rq.getMember();
        List<Likeable> likeableList = member.getLikeableList();
        model.addAttribute("likeableList", likeableList);

        log.info("호감 리스트 전달 완료 likeable list size = {}", likeableList.size());
        return "usr/likeable/list";
    }

    //-- 호감 삭제 --//
    @PostMapping("delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable Long id) {
        log.info("호감 삭제 요청 확인 likeable id = {}", id);
        Instagram instagram = likeableService.findOne(id).getInstagram();
        RsData deleteRs = likeableService.delete(id);

        if (deleteRs.isFail()) {
            log.info("삭제 실패 msg = {}", deleteRs.getMsg());
            return rq.historyBack(deleteRs.getMsg());
        }

        log.info("호감삭제 성공");
        return rq.redirectWithMsg("/instagram/detail/" + instagram.getId(), deleteRs.getMsg());
    }
}
