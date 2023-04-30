package com.lldj.gram.boundedContext.likeable.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@AllArgsConstructor
public class LikeableAddForm {

    @NotBlank
    @Size(min = 4, max = 20)
    private String instagramName;

    @NotNull
    @Range(min = 0, max = 3)
    private Integer attractive;
}
