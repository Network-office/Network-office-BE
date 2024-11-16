package dev.office.networkoffice.feed.service;

import dev.office.networkoffice.feed.dto.response.CommentDetails;
import dev.office.networkoffice.feed.dto.response.FeedDetails;
import dev.office.networkoffice.feed.dto.response.FeedInfo;
import dev.office.networkoffice.feed.dto.request.FeedWrite;
import dev.office.networkoffice.feed.entity.Comment;
import dev.office.networkoffice.feed.entity.Feed;
import dev.office.networkoffice.feed.repository.CommentRepository;
import dev.office.networkoffice.feed.repository.FeedRepository;
import dev.office.networkoffice.user.entity.User;
import dev.office.networkoffice.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public void writeFeed(Long userId, FeedWrite request) {
        User author = findUserById(userId);
        Feed newFeed = Feed.writeNewFeed(request.title(), request.contents(), request.category(), author);
        feedRepository.save(newFeed);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    @Transactional(readOnly = true)
    public Slice<FeedInfo> getFeeds(Pageable pageable) {
        return feedRepository.findAllByOrderByCreatedTimeDesc(pageable)
                .map(this::mapToFeedInfo);
    }

    private FeedInfo mapToFeedInfo(Feed feed) {
        return new FeedInfo(
                feed.getId(),
                feed.getTitle(),
                feed.getContents(),
                feed.getCategory(),
                feed.getAuthor().getId(),
                feed.getAuthor().getProfile().getDisplayName(),
                feed.getView(),
                feed.getLike(),
                feed.getCreatedTime()
        );
    }

    @Transactional
    public FeedDetails getFeed(Long userId, Long feedId) {
        // TODO: 조회수 기능 구현
        //  User viewer = findUserById(userId);
        Feed feed = findFeedById(feedId);
        feed.increaseView();
        List<CommentDetails> comments = getCommentDetails(feedId);
        return mapToFeedDetails(feed, comments);
    }

    private Feed findFeedById(Long feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 피드입니다."));
    }

    private List<CommentDetails> getCommentDetails(Long feedId) {
        return commentRepository.findByFeedId(feedId)
                .stream()
                .map(this::mapToCommentDetails)
                .toList();
    }

    private CommentDetails mapToCommentDetails(Comment comment) {
        return new CommentDetails(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getId(),
                comment.getAuthor().getProfile().getDisplayName(),
                comment.getCreatedTime());
    }

    private FeedDetails mapToFeedDetails(Feed feed, List<CommentDetails> comments) {
        return new FeedDetails(
                feed.getId(),
                feed.getTitle(),
                feed.getContents(),
                feed.getCategory(),
                feed.getAuthor().getId(),
                feed.getAuthor().getProfile().getDisplayName(),
                feed.getView(),
                feed.getLike(),
                feed.getCreatedTime(),
                comments
        );
    }
}
