package dev.office.networkoffice.feed.controller;

import dev.office.networkoffice.feed.controller.docs.FeedApiDocs;
import dev.office.networkoffice.feed.dto.response.FeedDetails;
import dev.office.networkoffice.feed.dto.response.FeedInfo;
import dev.office.networkoffice.feed.dto.request.FeedWrite;
import dev.office.networkoffice.feed.service.FeedService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    @PostMapping
    public void writeFeed(Principal principal, @RequestBody FeedWrite request) {
        Long userId = Long.parseLong(principal.getName());
        feedService.writeFeed(userId, request);
    }

    @GetMapping
    public Slice<FeedInfo> viewFeeds(Pageable pageable) {
        return feedService.getFeeds(pageable);
    }

    @GetMapping("{feedId}")
    public FeedDetails viewFeedDetail(Principal principal, @PathVariable Long feedId) {
        Long userId = Long.parseLong(principal.getName());
        return feedService.getFeed(userId, feedId);
    }
}
