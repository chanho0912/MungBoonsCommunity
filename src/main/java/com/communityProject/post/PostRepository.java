package com.communityProject.post;

import com.communityProject.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long> {

}
