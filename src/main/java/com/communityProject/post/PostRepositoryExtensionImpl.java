package com.communityProject.post;

import com.communityProject.domain.*;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Set;

public class PostRepositoryExtensionImpl extends QuerydslRepositorySupport implements PostRepositoryExtension{
    public PostRepositoryExtensionImpl() {
        super(Post.class);
    }

    @Override
    public Page<Post> findByKeyword(String keyword, Pageable pageable) {
        QPost post = QPost.post;
        JPQLQuery<Post> query = from(post)
                .where(post.title.containsIgnoreCase(keyword)
                        .or(post.tags.any().title.containsIgnoreCase(keyword)))
                .leftJoin(post.tags, QTag.tag).fetchJoin()
                .leftJoin(post.whoLikes, QAccount.account).fetchJoin()
                .distinct();

        JPQLQuery<Post> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Post> fetchResults = pageableQuery.fetchResults();
        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }

    @Override
    public List<Post> findByTags(Set<Tag> tags) {
        QPost post = QPost.post;
        JPQLQuery<Post> query = from(post).where(post.tags.any().in(tags))
                .leftJoin(post.tags, QTag.tag).fetchJoin()
                .orderBy(post.createdAt.desc()).distinct().limit(5);
        return query.fetch();
    }

}
