package dev.office.networkoffice.chat.entity;

import dev.office.networkoffice.gathering.entity.Gathering;
import dev.office.networkoffice.global.entity.BaseTimeEntity;
import dev.office.networkoffice.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "chat_messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "chat_message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;

    private String content;

    private String writer;

    @Builder
    public ChatMessage(
            User author,
            Gathering gathering,
            String content,
            String writer) {
        this.author = author;
        this.gathering = gathering;
        this.content = content;
        this.writer = writer;
    }
}
