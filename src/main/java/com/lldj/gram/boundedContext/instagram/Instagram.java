package com.lldj.gram.boundedContext.instagram;

import com.lldj.gram.boundedContext.likeable.Likeable;
import com.lldj.gram.boundedContext.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Instagram {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String username;
    private String gender;
    private String memberName;
    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "instagram")
    private List<Likeable> likeableList = new ArrayList<>();


    //-- create method --//

    // member 연동 instagram 생성 //
    protected static Instagram createInstagram(String username, String gender, Member member) {
        Instagram instagram = new Instagram();
        instagram.username = username;
        instagram.gender = gender;
        instagram.memberName = member.getUsername();
        member.addInstagramName(username);
        return instagram;
    }

    // 호감표시 받은 instagram 생성 //
    public static Instagram createInstagram(String username) {
        Instagram instagram = new Instagram();
        instagram.username = username;
        instagram.gender = "U";
        return instagram;
    }

    //-- business logic --//

    // member 연동 //
    protected void addMember(Member member, String gender) {
        this.gender = gender;
        this.memberName = member.getUsername();
        member.addInstagramName(this.username);
    }
}
