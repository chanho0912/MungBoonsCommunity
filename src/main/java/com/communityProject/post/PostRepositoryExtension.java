package com.communityProject.post;

import com.communityProject.domain.Post;
import com.communityProject.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface PostRepositoryExtension {

    Page<Post> findByKeyword(String keyword, Pageable pageable);

    List<Post> findByTags(Set<Tag> tags);
}
