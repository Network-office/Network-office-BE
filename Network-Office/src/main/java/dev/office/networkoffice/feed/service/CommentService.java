package dev.office.networkoffice.feed.service;

import dev.office.networkoffice.feed.dto.request.CommentWrite;
import dev.office.networkoffice.feed.entity.Comment;
import dev.office.networkoffice.feed.entity.Feed;
import dev.office.networkoffice.feed.repository.CommentRepository;
import dev.office.networkoffice.feed.repository.FeedRepository;
import dev.office.networkoffice.user.entity.User;
import dev.office.networkoffice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    @Transactional
    public void writeComment(Long userId, CommentWrite commentWrite) {
        User author = findUserById(userId);
        Feed targetFeed = findFeedById(commentWrite.feedId());
        Comment comment = Comment.writeNewComment(targetFeed, author, commentWrite.text());
        commentRepository.save(comment);
    }

    @Transactional
    public void removeComment(Long userId, Long commentId) {
        User author = findUserById(userId);
        Comment targetComment = findCommentById(commentId);
        checkAuthor(targetComment, author);
        commentRepository.delete(targetComment);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    private Feed findFeedById(Long feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 피드입니다."));
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
    }

    private void checkAuthor(Comment targetComment, User author) {
        if (!targetComment.isCreatedBy(author)) {
            throw new IllegalArgumentException("해당 댓글의 작성자가 아닙니다.");
        }
    }
}
