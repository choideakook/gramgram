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

import static jakarta.persistence.FetchType.LAZY;
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
    private String instagramName;
    private String gender;
    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;

    @ManyToOne(fetch = LAZY)
    private Member member;

    @OneToMany(mappedBy = "instagram")
    private List<Likeable> likeableList = new ArrayList<>();
}
