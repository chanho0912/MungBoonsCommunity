package com.communityProject.post;

import com.communityProject.domain.Post;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface PostRepositoryExtension {

    List<Post> findByKeyword(String keyword);
}
