package com.communityProject.comment;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class CommentForm {

    @NotBlank
    @Length(max = 50)
    private String content;
}
