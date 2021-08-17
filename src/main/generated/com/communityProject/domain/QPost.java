package com.communityProject.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 1569844985L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final StringPath content = createString("content");

    public final NumberPath<Long> countOfViews = createNumber("countOfViews", Long.class);

    public final QAccount createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final StringPath path = createString("path");

    public final BooleanPath published = createBoolean("published");

    public final DateTimePath<java.time.LocalDateTime> publishedDateTime = createDateTime("publishedDateTime", java.time.LocalDateTime.class);

    public final SetPath<Tag, QTag> tags = this.<Tag, QTag>createSet("tags", Tag.class, QTag.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public final SetPath<Account, QAccount> whoLikes = this.<Account, QAccount>createSet("whoLikes", Account.class, QAccount.class, PathInits.DIRECT2);

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.createdBy = inits.isInitialized("createdBy") ? new QAccount(forProperty("createdBy")) : null;
    }

}

