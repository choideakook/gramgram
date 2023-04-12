package com.lldj.gram.boundedContext.likeable;

import com.lldj.gram.base.request.RsData;
import com.lldj.gram.boundedContext.instagram.Instagram;
import com.lldj.gram.boundedContext.instagram.InstagramService;
import com.lldj.gram.boundedContext.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeableService {

    private final LikeableRepository likeableRepository;
    private final InstagramService instagramService;

    //-- 호감 생성 --//
    @Transactional
    public RsData<Likeable> like(String instagramName, int attractive, Member member) {

        // member 가 instagram 연동이 되어있는지 확인
        if (!StringUtils.hasText(member.getInstagramName()))
            return RsData.of("F-1", "인스타그램 연동이 되어있지 않습니다.");

        // 자기 스르로 호감표시 금지
        if (member.getInstagramName().equals(instagramName))
            return RsData.of("F-2", "자기 자신에게 호감표시할 수 없습니다.");

        // 10명까지 호감 표시 가능
        if(member.getLikeableList().size() >= 10)
            return RsData.of("F-3", "호감표시는 최대 10명까지 가능합니다.");

        // 인스타 이름으로 호감표시 대상 검색
        RsData<Instagram> instagramRs = instagramService.findByName(instagramName);
        Instagram instagram = instagramRs.getData();

        // 호감표시 대상이 존재하지 않는다면 인스타 생성
        if (instagramRs.isFail())
            instagram = instagramService.connection(instagramName);

        // 존재한다면 바로 호감 표시 저장
        Likeable likeable = Likeable.createLike(attractive, member, instagram);
        Likeable saveLikable = likeableRepository.save(likeable);
        return RsData.of("S-1", "호감표시가 완료되었습니다.", saveLikable);
    }

    //-- find by id --//
    public Likeable findOne(Long likeableId) {
        return likeableRepository.findById(likeableId).get();
    }

    //-- delete likeable --//
    @Transactional
    public RsData delete(Long id) {
        Optional<Likeable> byId = likeableRepository.findById(id);

        if (byId.isPresent()) {
            likeableRepository.delete(byId.get());
            return RsData.of("S-1", "삭제 성공");
        }

        return RsData.of("F-1", "존재하지 않는 id 입니다.");
    }

    //-- 중복 호감 여부 확인 --//
    public boolean duplicateInvalid(List<Likeable> likeableList, String instagramName) {
        List<Instagram> instagramList = new ArrayList<>();
        for (Likeable likeable : likeableList)
            instagramList.add(likeable.getInstagram());

        for (Instagram instagram : instagramList)
            if (instagram.getUsername().equals(instagramName))
                return false;

        return true;
    }

    //-- 매력 수정 로직 --//
    @Transactional
    public void updateAttractive(Likeable likeable, Integer attractive) {
        likeable.updateAttractive(attractive);
    }
}
