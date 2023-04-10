package com.lldj.gram.boundedContext.likeable.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikableAddForm {

    @NotBlank
    @Size(min = 1, max = 1)
    private int attractive;
}
