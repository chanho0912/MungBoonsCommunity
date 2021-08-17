package com.communityProject.post.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class PostUpdateForm {
    @NotBlank
    @Length(max = 50)
    private String title;

    @NotBlank
    private String content;
}
