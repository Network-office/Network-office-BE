package dev.office.networkoffice.feed.entity;

import dev.office.networkoffice.global.entity.BaseTimeEntity;
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
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    private Comment(Feed feed, User author, String text) {
        this.feed = feed;
        this.author = author;
        this.text = text;
    }

    public static Comment writeNewComment(Feed feed, User author, String text) {
        validateArguments(feed, author, text);
        return new Comment(feed, author, text);
    }

    private static void validateArguments(Feed feed, User author, String text) {
        Assert.notNull(feed, "댓글을 작성할 피드 정보는 필수입니다.");
        Assert.notNull(author, "피드의 작성자는 필수입니다.");
        Assert.hasText(text, "작성할 댓글 내용은 필수입니다.");
    }

    public boolean isCreatedBy(User targetAuthor) {
        return author.equals(targetAuthor);
    }
}
