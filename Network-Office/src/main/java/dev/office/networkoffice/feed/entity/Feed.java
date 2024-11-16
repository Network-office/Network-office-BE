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
    private Long view = 0L;

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
        return new Feed(title, contents, category, author);
    }

    public void increaseView() {
        view++;
    }
}
