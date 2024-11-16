package dev.office.networkoffice.feed.service;

import dev.office.networkoffice.feed.entity.Feed;
import dev.office.networkoffice.feed.entity.Likes;
import dev.office.networkoffice.feed.repository.FeedRepository;
import dev.office.networkoffice.feed.repository.LikesRepository;
import dev.office.networkoffice.user.entity.User;
import dev.office.networkoffice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    @Transactional
    public void likeFeed(Long userId, Long feedId) {
        User user = findUserById(userId);
        Feed feed = findFeedById(feedId);
        if (likesRepository.existsByUserAndFeed(user, feed)) {
            throw new IllegalArgumentException("이미 좋아요를 누른 피드입니다.");
        }
        Likes likes = Likes.createLikes(user, feed);
        likesRepository.save(likes);
    }

    @Transactional
    public void unlikeFeed(Long userId, Long feedId) {
        User user = findUserById(userId);
        Feed feed = findFeedById(feedId);
        Likes likes = findLikesByUserAndFeed(user, feed);
        likesRepository.delete(likes);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    private Feed findFeedById(Long feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 피드입니다."));
    }

    private Likes findLikesByUserAndFeed(User user, Feed feed) {
        return likesRepository.findByUserAndFeed(user, feed)
                .orElseThrow(() -> new IllegalArgumentException("좋아요가 존재하지 않습니다."));
    }
}
