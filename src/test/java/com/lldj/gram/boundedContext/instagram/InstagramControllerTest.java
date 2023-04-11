package com.lldj.gram.boundedContext.instagram;

import com.lldj.gram.base.request.Rq;
import com.lldj.gram.base.request.RsData;
import com.lldj.gram.boundedContext.likeable.Likeable;
import com.lldj.gram.boundedContext.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class InstagramControllerTest {

    @Autowired
    Rq rq;
    @Autowired
    InstagramService instagramService;

    @Test
    void name() {

    }
}