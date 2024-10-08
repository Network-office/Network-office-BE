package dev.office.networkoffice.gathering.controller;

import dev.office.networkoffice.gathering.controller.docs.GatheringApiDocs;
import dev.office.networkoffice.gathering.controller.dto.request.GatheringCancelDto;
import dev.office.networkoffice.gathering.controller.dto.request.GatheringDto;
import dev.office.networkoffice.gathering.controller.dto.request.GatheringSuccessDto;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringClosedResponse;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringListResponseDto;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringResponseDto;
import dev.office.networkoffice.gathering.service.GatheringService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/v1/gathering")
@RequiredArgsConstructor
public class GatheringController implements GatheringApiDocs {

    private final GatheringService gatheringService;

    @GetMapping()
    public GatheringListResponseDto findGatheringList(Principal principal,
                                                      @RequestParam("si") String si,
                                                      @RequestParam("dong") String dong,
                                                      @RequestParam("gu") String gu) {
        return gatheringService.getGatheringByPlace(si, dong, gu);
    }

    @PostMapping()
    public GatheringResponseDto createGathering(Principal principal,
                                                @RequestBody GatheringDto gatheringDto) {
        Long userId = getUserId(principal);
        return gatheringService.createGathering(userId, gatheringDto);
    }

    @PutMapping({"{gatheringId}"})
    public GatheringResponseDto modifyGatheringByHost(Principal principal,
                                                      @PathVariable("gatheringId") Long gatheringId,
                                                      @RequestBody GatheringDto gatheringDto) {
        Long userId = getUserId(principal);
        return gatheringService.modifyGatheringInfoByHost(userId, gatheringId, gatheringDto);
    }

    @PostMapping("success")
    public GatheringClosedResponse successGatheringByHost(Principal principal,
                                                          GatheringSuccessDto successDto) {
        Long userId = getUserId(principal);
        return gatheringService.successGatheringByHost(userId, successDto);
    }

    @PostMapping("cancel")
    public GatheringClosedResponse cancelGatheringByHost(Principal principal,
                                                         GatheringCancelDto cancelDto) {
        Long userId = getUserId(principal);
        return gatheringService.cancelGatheringByHost(userId, cancelDto);
    }

    private Long getUserId(Principal principal) {
        return Long.parseLong(principal.getName());
    }
}
