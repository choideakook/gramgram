package com.lldj.gram.boundedContext.member.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberJoinForm {
    @NotBlank
    @Size(min = 4, max = 30)
    private final String username;

    @NotBlank
    @Size(min = 4, max = 30)
    private final String password;

    @NotBlank
    @Size(min = 4, max = 30)
    private final String password2;

}
