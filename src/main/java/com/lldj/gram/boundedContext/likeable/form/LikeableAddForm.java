package com.lldj.gram.boundedContext.likeable.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeableAddForm {

    @NotBlank
    @Size(min = 4, max = 10)
    private String instagramName;

    @NotBlank
    @Size(min = 1, max = 1)
    private Integer attractive;
}
