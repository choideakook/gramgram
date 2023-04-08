package com.lldj.gram.boundedContext.member;

import com.lldj.gram.base.request.RsData;
import com.lldj.gram.boundedContext.member.form.MemberJoinForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //-- find by name --//
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    //-- Security Join --//
    @Transactional
    public RsData<Member> join(MemberJoinForm form) {

        if (!form.getPassword().equals(form.getPassword2()))
            return RsData.of("F-1", "비밀번호가 일치하지 않습니다.");

        return Join("GRAMGRAM", form.getUsername(), form.getPassword());
    }

    //-- Social Join --//
    private RsData<Member> Join(String providerTypeCode, String username, String password) {

        if (this.findByUsername(username).isPresent())
            return RsData.of("F-1", "해당 아이디(%s)는 이미 사용중입니다.".formatted(username));

        if (StringUtils.hasText(password))
            password = passwordEncoder.encode(password);

        Member member = Member.createMember(providerTypeCode, username, password);
        memberRepository.save(member);

        return RsData.of("S-1", "회원가입이 완료되었습니다.", member);
    }
}
