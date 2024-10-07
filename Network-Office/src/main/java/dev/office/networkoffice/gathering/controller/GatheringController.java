package dev.office.networkoffice.gathering.controller;

import dev.office.networkoffice.gathering.controller.docs.GatheringApiDocs;
import dev.office.networkoffice.gathering.controller.dto.request.GatheringCancelDto;
import dev.office.networkoffice.gathering.controller.dto.request.GatheringDto;
import dev.office.networkoffice.gathering.controller.dto.request.GatheringModifyDto;
import dev.office.networkoffice.gathering.controller.dto.request.GatheringSuccessDto;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringDeleteResponse;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringListResponseDto;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringResponseDto;
import dev.office.networkoffice.gathering.service.GatheringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/api/v1/gathering")
@RequiredArgsConstructor
public class GatheringController implements GatheringApiDocs {

    private final GatheringService gatheringService;

    @GetMapping
    public ResponseEntity<GatheringListResponseDto> findGatheringList(
            Principal principal,
            @RequestParam("si") String si,
            @RequestParam("dong") String dong,
            @RequestParam("gu") String gu
    ) {
        Long userId = findUserIdInSession(principal);
        return ResponseEntity.ok(gatheringService.getGatheringByPlace(userId, si, dong, gu));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<GatheringResponseDto> createGathering(Principal principal, @RequestBody GatheringDto gatheringDto) {
        Long userId = findUserIdInSession(principal);
        return ResponseEntity.ok(gatheringService.createGathering(userId, gatheringDto));
    }

    @PutMapping()
    public ResponseEntity<GatheringResponseDto> modifyGatheringByHost(Principal principal, @RequestBody GatheringModifyDto gatheringModifyDto) {
        Long userId = findUserIdInSession(principal);
        return ResponseEntity.ok(gatheringService.modifyGatheringInfoByHost(userId, gatheringModifyDto));
    }

    //delete 매핑은 requestBody를 통상 가지지 않기때문에 post로 작성.
    @PostMapping("/cancel")
    public ResponseEntity<GatheringDeleteResponse> cancelGatheringByHost(Principal principal, GatheringCancelDto cancelDto) {
        Long userId = findUserIdInSession(principal);
        return ResponseEntity.ok(gatheringService.cancelGatheringByHost(userId, cancelDto));
    }

    @PostMapping("/success")
    public ResponseEntity<GatheringDeleteResponse> successGatheringByHost(Principal principal, GatheringSuccessDto successDto) {
        Long userId = findUserIdInSession(principal);
        return ResponseEntity.ok(gatheringService.successGatheringByHost(userId, successDto));
    }

    private Long findUserIdInSession(Principal principal){
        return Long.parseLong(principal.getName());
    }

}
