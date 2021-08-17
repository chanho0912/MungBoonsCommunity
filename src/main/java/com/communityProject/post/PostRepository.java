package com.communityProject.post;

import com.communityProject.domain.Post;
import com.communityProject.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long> {

    Post findPostWithTagsById(Long id);

}
