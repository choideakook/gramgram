package com.lldj.gram.boundedContext.likeable;

import com.lldj.gram.boundedContext.likeable.form.LikeableAddForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/likeable")
@RequiredArgsConstructor
public class LikeableController {

    private final LikeableService likeableService;

    //-- 호감 등록 폼 --//
    @GetMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String addLike() {
        log.info("호감 등록 폼 요청 확인");
        return "usr/likeable/add";
    }
}
