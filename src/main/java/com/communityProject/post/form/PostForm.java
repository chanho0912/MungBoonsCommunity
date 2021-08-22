package com.communityProject.post.form;

import com.communityProject.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data @AllArgsConstructor @NoArgsConstructor
public class PostForm {
    @NotBlank
    @Length(max = 50)
    private String title;

    @NotBlank
    private String content;

    private String tags;
}
