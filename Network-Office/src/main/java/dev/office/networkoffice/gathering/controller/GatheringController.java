package dev.office.networkoffice.gathering.controller;

import dev.office.networkoffice.gathering.controller.docs.GatheringApiDocs;
import dev.office.networkoffice.gathering.controller.dto.request.GatheringCancelDto;
import dev.office.networkoffice.gathering.controller.dto.request.GatheringDto;
import dev.office.networkoffice.gathering.controller.dto.request.GatheringSuccessDto;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringClosedResponse;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringListResponseDto;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringResponseDto;
import dev.office.networkoffice.gathering.service.GatheringService;
import dev.office.networkoffice.global.annotation.CurrentUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/gathering")
@RequiredArgsConstructor
public class GatheringController implements GatheringApiDocs {

    private final GatheringService gatheringService;

    @GetMapping
    public GatheringListResponseDto findGatheringList(@CurrentUserId Long userId,
                                                      @RequestParam("si") String si,
                                                      @RequestParam("dong") String dong,
                                                      @RequestParam("gu") String gu) {
        return gatheringService.getGatheringByPlace(si, dong, gu);
    }

    @PostMapping
    public GatheringResponseDto createGathering(@CurrentUserId Long userId,
                                                @RequestBody GatheringDto gatheringDto) {
        return gatheringService.createGathering(userId, gatheringDto);
    }

    @PutMapping("{gatheringId}")
    public GatheringResponseDto modifyGatheringByHost(@CurrentUserId Long userId,
                                                      @PathVariable("gatheringId") Long gatheringId,
                                                      @RequestBody GatheringDto gatheringDto) {
        return gatheringService.modifyGatheringInfoByHost(userId, gatheringId, gatheringDto);
    }

    @PostMapping("{gatheringId}/success")
    public GatheringClosedResponse successGatheringByHost(@CurrentUserId Long userId,
                                                          @PathVariable("gatheringId") Long gatheringId,
                                                          GatheringSuccessDto successDto) {
        return gatheringService.successGatheringByHost(userId, gatheringId, successDto);
    }

    @PostMapping("{gatheringId}/cancel")
    public GatheringClosedResponse cancelGatheringByHost(@CurrentUserId Long userId,
                                                         @PathVariable("gatheringId") Long gatheringId,
                                                         GatheringCancelDto cancelDto) {
        return gatheringService.cancelGatheringByHost(userId, gatheringId, cancelDto);
    }
}
