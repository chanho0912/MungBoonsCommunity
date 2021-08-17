package com.communityProject.domain;

import com.communityProject.account.UserAccount;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String path;

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
    private Set<Tag> tags = new HashSet<>();

    private LocalDateTime publishedDateTime;

    private Long countOfViews;

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
}
