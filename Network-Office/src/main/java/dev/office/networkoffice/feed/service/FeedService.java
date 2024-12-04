package dev.office.networkoffice.feed.service;

import dev.office.networkoffice.feed.dto.response.CommentDetails;
import dev.office.networkoffice.feed.dto.response.FeedDetails;
import dev.office.networkoffice.feed.dto.response.FeedInfo;
import dev.office.networkoffice.feed.dto.request.FeedWrite;
import dev.office.networkoffice.feed.entity.Comment;
import dev.office.networkoffice.feed.entity.Feed;
import dev.office.networkoffice.feed.repository.CommentRepository;
import dev.office.networkoffice.feed.repository.FeedRepository;
import dev.office.networkoffice.feed.repository.LikesRepository;
import dev.office.networkoffice.feed.repository.VisitedRepository;
import dev.office.networkoffice.feed.repository.dto.FeedWithLikeCount;
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
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final VisitedRepository visitedRepository;

    @Transactional
    public void writeFeed(Long userId, FeedWrite request) {
        User author = findUserById(userId);
        Feed newFeed = Feed.writeNewFeed(request.title(), request.contents(), request.category(), author);
        feedRepository.save(newFeed);
    }

    @Transactional(readOnly = true)
    public Slice<FeedInfo> getFeeds(Pageable pageable) {
        return feedRepository.findAllFeedsWithLikeCount(pageable)
                .map(this::mapToFeedInfo);
    }

    @Transactional
    public FeedDetails getFeed(Long userId, Long feedId) {
        incrementViewCountIfFirstVisit(userId, feedId);
        Feed feed = findFeedById(feedId);
        List<CommentDetails> comments = getCommentDetails(feedId);
        boolean isLiked = getLiked(userId, feedId);
        return mapToFeedDetails(feed, comments, isLiked);
    }

    private void incrementViewCountIfFirstVisit(Long userId, Long feedId) {
        if (!visitedRepository.isFeedVisited(userId, feedId)) {
            feedRepository.incrementViewCount(feedId);
            visitedRepository.save(userId, feedId);
        }
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    private FeedInfo mapToFeedInfo(FeedWithLikeCount feedWithLikeCount) {
        return new FeedInfo(
                feedWithLikeCount.feed().getId(),
                feedWithLikeCount.feed().getTitle(),
                feedWithLikeCount.feed().getContents(),
                feedWithLikeCount.feed().getCategory(),
                feedWithLikeCount.feed().getAuthor().getId(),
                feedWithLikeCount.feed().getAuthor().getProfile().getDisplayName(),
                feedWithLikeCount.feed().getView(),
                feedWithLikeCount.likeCount(),
                feedWithLikeCount.feed().getCreatedTime()
        );
    }

    private Feed findFeedById(Long feedId) {
        return feedRepository.findFeedByIdWithAuthor(feedId)
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

    private FeedDetails mapToFeedDetails(Feed feed, List<CommentDetails> comments, boolean isLiked) {
        return new FeedDetails(
                feed.getId(),
                feed.getTitle(),
                feed.getContents(),
                feed.getCategory(),
                feed.getAuthor().getId(),
                feed.getAuthor().getProfile().getDisplayName(),
                feed.getView(),
                getLikes(feed.getId()),
                isLiked,
                feed.getCreatedTime(),
                comments
        );
    }

    private boolean getLiked(Long userId, Long feedId) {
        if (userId == null) {
            return false;
        }
        return isLiked(userId, feedId);
    }

    private boolean isLiked(Long userId, Long feedId) {
        return likesRepository.existsByUserIdAndFeedId(userId, feedId);
    }

    private Long getLikes(Long feedId) {
        return likesRepository.countByFeedId(feedId);
    }
}
