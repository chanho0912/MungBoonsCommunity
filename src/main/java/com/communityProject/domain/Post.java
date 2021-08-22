package com.communityProject.domain;

import com.communityProject.account.UserAccount;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NamedEntityGraph(name="Post.withTags", attributeNodes = {
        @NamedAttributeNode("tags")
})
@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor @Builder
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account createdBy;

    private String title;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String content;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String image;

    @ManyToMany
    private Set<Account> whoLikes = new HashSet<>();

    @ManyToMany
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    private LocalDateTime createdAt;

    private int commentCount;
    private int likesCount;
    private int countOfViews;

    private boolean published; // for hide

    /*
    * TODO
    * @ Comment
    *
    *
    * */

    public boolean isManager(UserAccount userAccount) {
        return this.createdBy.getId().equals(userAccount.getAccount().getId());
    }

    public boolean canLikeThisPost(UserAccount userAccount) {
        return !(this.getWhoLikes().contains(userAccount.getAccount()));
    }

    public void addLikesToPost(Account account) {
        this.whoLikes.add(account);
        this.likesCount = this.getWhoLikes().size();
    }
    public void deleteLikesToPost(Account account) {
        this.whoLikes.remove(account);
        this.likesCount = this.getWhoLikes().size();
    }
}
