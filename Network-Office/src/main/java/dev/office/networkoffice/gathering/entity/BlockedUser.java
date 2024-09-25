package dev.office.networkoffice.gathering.entity;

import dev.office.networkoffice.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 해당 유저로 찾는게 더 빠를까 모임 아이디로 찾는게 더 빠를까
 * 차단된 유저를 찾아야하는 경우의 수를 생각해보자.
 */
@Entity
@Getter
@Table(name = "blocked_users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlockedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "blocked_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;

    private String reason;

    @Builder
    public BlockedUser(User user, Gathering gathering, String reason) {
        this.user = user;
        this.gathering = gathering;
        this.reason = reason;
    }

    public void writeReasonsForRejection(String reason) {
        this.reason = reason;
    }
}
