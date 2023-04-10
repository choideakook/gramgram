package com.lldj.gram.boundedContext.instagram;

import com.lldj.gram.base.request.RsData;
import com.lldj.gram.boundedContext.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InstagramService {

    private final InstagramRepository instagramRepository;


    //-- instagram member 연동 객체 생성 --//
    public RsData<Instagram> connection(String username, String gender, Member member) {

        Optional<Instagram> instagrams = instagramRepository.findByUsername(username);
        if (instagrams.isPresent())
            return RsData.of("F-1", String.format("%s 는 이미 연동되어있는 id 입니다.", username));

        Instagram instagram = Instagram.createInstagram(username, gender, member);
        Instagram saveInstagram = instagramRepository.save(instagram);
        return RsData.of("S-1", "instagram 아이디가 연동되었습니다.", saveInstagram);
    }


    //-- 호감 표시 받는 객체 생성 --//

    //-- find by name --//
    public RsData<Instagram> findByName(String name) {
        Optional<Instagram> instagrams = instagramRepository.findByUsername(name);

        if (instagrams.isPresent())
            return RsData.successOf(instagrams.get());

        return RsData.failOf(instagrams.get());
    }
}


