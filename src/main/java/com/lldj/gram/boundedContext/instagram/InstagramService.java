package com.lldj.gram.boundedContext.instagram;

import com.lldj.gram.base.request.RsData;
import com.lldj.gram.boundedContext.likeable.Likeable;
import com.lldj.gram.boundedContext.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InstagramService {

    private final InstagramRepository instagramRepository;


    //-- instagram member 연동 객체 생성 --//
    @Transactional
    public RsData<Instagram> connection(String username, String gender, Member member) {

        // instagram 객체가 이미 생성되어있다면 이미 연동이 되었는지 확인
        Optional<Instagram> instagrams = instagramRepository.findByUsername(username);
        if (instagrams.isPresent())
            return connection(member, instagrams, gender);

        // 객체가 없다면 새로 생성해서 연동
        Instagram instagram = Instagram.createInstagram(username, gender, member);
        Instagram saveInstagram = instagramRepository.save(instagram);
        return RsData.of("S-1", "instagram 아이디가 연동되었습니다.", saveInstagram);
    }

    //-- 연동 여부 확인 --//
    private static RsData<Instagram> connection(Member member, Optional<Instagram> instagrams, String gender) {
        Instagram instagram = instagrams.get();

        if (instagram.getMemberName() == null) {
            instagram.addMember(member, gender);
            return RsData.of("S-1", "instagram 아이디가 연동되었습니다.", instagram);
        }else
            return RsData.of("F-1",
                    String.format("%s 는 이미 연동되어있는 id 입니다.", instagram.getUsername()));
    }

    //-- instagram 임시 객체 생성 --//
    public Instagram connection(String username) {
        Instagram instagram = Instagram.createInstagram(username);
        return instagramRepository.save(instagram);
    }

    //-- find by name --//
    public RsData<Instagram> findByName(String name) {
        Optional<Instagram> instagrams = instagramRepository.findByUsername(name);

        if (instagrams.isPresent())
            return RsData.successOf(instagrams.get());

        return RsData.of("F-1", "존재하지 않는 아이디입니다.");
    }

    //-- find by id --//
    public RsData<Instagram> findOne(Long id) {
        Optional<Instagram> instagrams = instagramRepository.findById(id);

        if (instagrams.isPresent())
            return RsData.successOf(instagrams.get());

        return RsData.of("F-1", "id 를 찾을 수 없습니다.");
    }

    //-- member 가 해당 instagram 에 호감을 표시했는지 확인 --//
    public Long likeableFinder(Member member, Instagram instagram) {
        List<Likeable> mLikeableList = member.getLikeableList();
        List<Likeable> iLikeableList = instagram.getLikeableList();

        for (Likeable likeable : mLikeableList)
            if (iLikeableList.contains(likeable))
                return likeable.getId();

        return -1L;
    }
}


