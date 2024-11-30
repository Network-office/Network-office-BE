package dev.office.networkoffice.feed.controller;

import dev.office.networkoffice.feed.controller.docs.FeedApiDocs;
import dev.office.networkoffice.feed.dto.response.FeedDetails;
import dev.office.networkoffice.feed.dto.response.FeedInfo;
import dev.office.networkoffice.feed.dto.request.FeedWrite;
import dev.office.networkoffice.feed.service.FeedService;
import dev.office.networkoffice.feed.service.LikesService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/feeds")
public class FeedController implements FeedApiDocs {

    private final FeedService feedService;
    private final LikesService likesService;

    @PostMapping
    public void writeFeed(Principal principal, @RequestBody FeedWrite request) {
        Long userId = Long.parseLong(principal.getName());
        feedService.writeFeed(userId, request);
    }

    @GetMapping
    public Slice<FeedInfo> viewFeeds(@PageableDefault(sort = "id", direction = Sort.Direction.DESC)
                                     Pageable pageable) {
        return feedService.getFeeds(pageable);
    }

    @GetMapping("{feedId}")
    public FeedDetails viewFeedDetail(Principal principal, @PathVariable Long feedId) {
        Long userId = Long.parseLong(principal.getName());
        return feedService.getFeed(userId, feedId);
    }

    @PostMapping("{feedId}/likes")
    public void feedLikes(Principal principal, @PathVariable Long feedId) {
        Long userId = Long.parseLong(principal.getName());
        likesService.likeFeed(userId, feedId);
    }

    @PostMapping("{feedId}/unlikes")
    public void feedUnlikes(Principal principal, @PathVariable Long feedId) {
        Long userId = Long.parseLong(principal.getName());
        likesService.unlikeFeed(userId, feedId);
    }
}
