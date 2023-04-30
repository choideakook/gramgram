package com.lldj.gram.boundedContext.likeable;

import com.lldj.gram.boundedContext.instagram.Instagram;
import com.lldj.gram.boundedContext.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Likeable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String attractive; // 1:외모 2:성격 3:능력
    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;

    @ManyToOne(fetch = LAZY)
    private Member member;

    @ManyToOne(fetch = LAZY)
    private Instagram instagram;


    //-- create method --//
    protected static Likeable createLike(int attractive, Member member, Instagram instagram) {
        Likeable likeable = new Likeable();
        likeable.attractiveMapper(attractive);
        likeable.member = member;
        likeable.instagram = instagram;
        member.getLikeableList().add(likeable);
        instagram.getLikeableList().add(likeable);
        return likeable;
    }

    //-- business logic --//

    // 매력코드 변환 //
    public String attractiveMapper(Integer attractive) {
        return switch (attractive) {
            case 1 -> this.attractive = "외모";
            case 2 -> this.attractive = "성격";
            default -> this.attractive = "능력";
        };
    }

    // 매력 update //
    protected void updateAttractive(Integer attractiveCode) {
        String attractive = this.attractiveMapper(attractiveCode);
        this.attractive = attractive;
    }
}
