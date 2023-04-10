package com.lldj.gram.boundedContext.likeable;

import com.lldj.gram.boundedContext.likeable.form.LikableAddForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/likeable")
@RequiredArgsConstructor
public class LikeableController {

    private final LikeableService likeableService;

    //-- 호감 등록 폼 --//
    @GetMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String addLike(LikableAddForm form)
}
