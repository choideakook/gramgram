package com.lldj.gram.boundedContext.member;

import com.lldj.gram.boundedContext.instagram.Instagram;
import com.lldj.gram.boundedContext.likeable.Likeable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    private String instagramName;
    private String providerTypeCode;

    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "member")
    private List<Likeable> likeableList = new ArrayList<>();


    //-- create method --//

    // create member //
    protected static Member createMember(String providerTypeCode, String username, String password) {
        Member member = new Member();
        member.providerTypeCode = providerTypeCode;
        member.username = username;
        member.password = password;
        return member;
    }

    // instagram 연동 //
    public void addInstagramName(String instagramName) {
        this.instagramName = instagramName;
    }


    //-- create authorize --//
    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        // 모든 회원에게 member 권한 부여 //
        grantedAuthorities.add(new SimpleGrantedAuthority("member"));

        // admin 권한 부여 //
        if ("admin".equals(username))
            grantedAuthorities.add(new SimpleGrantedAuthority("admin"));

        return grantedAuthorities;
    }
}
