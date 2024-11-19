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
@Table(name = "feeds")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "feed_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "contents", columnDefinition = "TEXT")
    private String contents;

    @Column(name = "category")
    private String category;

    @Column(name = "views", nullable = false)
    private long view = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    private Feed(String title, String contents, String category, User author) {
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.author = author;
    }

    public static Feed writeNewFeed(String title, String contents, String category, User author) {
        validateArguments(title, contents, category, author);
        return new Feed(title, contents, category, author);
    }

    private static void validateArguments(String title, String contents, String category, User author) {
        Assert.hasText(title, "피드에 제목은 필수입니다.");
        Assert.hasText(contents, "피드의 내용은 필수입니다.");
        Assert.hasText(category, "피드의 카테고리는 필수입니다.");
        Assert.notNull(author, "피드의 작성자는 필수입니다.");
    }

    public void increaseView() {
        view++;
    }
}
