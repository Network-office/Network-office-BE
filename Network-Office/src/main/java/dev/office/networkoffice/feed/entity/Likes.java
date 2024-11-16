package dev.office.networkoffice.feed.entity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@Getter
@Table(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    private Likes(User user, Feed feed) {
        this.user = user;
        this.feed = feed;
    }

    public static Likes createLikes(User user, Feed feed) {
        validateArguments(user, feed);
        return new Likes(user, feed);
    }

    private static void validateArguments(User user, Feed feed) {
        Assert.notNull(user, "좋아요를 누를 사용자 정보는 필수입니다.");
        Assert.notNull(feed, "좋아요를 누를 피드 정보는 필수입니다.");
    }
}
