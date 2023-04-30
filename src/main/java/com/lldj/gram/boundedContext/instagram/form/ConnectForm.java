package com.lldj.gram.boundedContext.instagram.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConnectForm {

    @NotBlank
    @Size(min = 4, max = 30)
    private final String username;

    @NotBlank
    @Size(min = 1, max = 1)
    private final String gender;
}
