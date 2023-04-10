package com.lldj.gram.boundedContext.instagram;

import com.lldj.gram.base.request.Rq;
import com.lldj.gram.base.request.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/instagram")
@RequiredArgsConstructor
public class InstagramController {

    private final InstagramService instagramService;
    private final Rq rq;

    //-- 인스타 연동 폼 --//
    @GetMapping("/connect")
    @PreAuthorize("isAuthenticated()")
    public String connect() {
        return "usr/instagram/connect";
    }
}
