package com.communityProject.domain;

import com.communityProject.account.UserAccount;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account createdBy;

    private String content;

    private LocalDateTime createdAt;

    public boolean isManager(UserAccount userAccount) {
        return this.createdBy.getId().equals(userAccount.getAccount().getId());
    }
}
