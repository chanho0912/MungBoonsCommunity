package com.communityProject.post;

import com.communityProject.domain.Tag;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
public class PostForm {
    @NotBlank
    @Length(max = 50)
    private String title;

    @NotBlank
    private String content;

    private String tags;
}
