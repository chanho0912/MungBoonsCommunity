package com.communityProject.post;

import com.communityProject.domain.Post;
import com.communityProject.domain.QPost;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class PostRepositoryExtensionImpl extends QuerydslRepositorySupport implements PostRepositoryExtension{
    public PostRepositoryExtensionImpl() {
        super(Post.class);
    }

    @Override
    public List<Post> findByKeyword(String keyword) {
        QPost post = QPost.post;
        JPQLQuery<Post> query = from(post)
                .where(post.title.containsIgnoreCase(keyword)
                        .or(post.tags.any().title.containsIgnoreCase(keyword)));

        return query.fetch();
    }
}
