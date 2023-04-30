package com.lldj.gram.boundedContext.likeable.form;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@AllArgsConstructor
public class LikeableUpdateForm {

    @NotNull
    @Range(min = 0, max = 3)
    private Integer attractive;
}
