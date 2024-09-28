package dev.office.networkoffice.gathering.controller.dto;

import dev.office.networkoffice.gathering.controller.dto.docs.GatheringApiDocs;
import dev.office.networkoffice.gathering.controller.dto.request.GatheringCancelDto;
import dev.office.networkoffice.gathering.controller.dto.request.GatheringDto;
import dev.office.networkoffice.gathering.controller.dto.request.GatheringModifyDto;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringListResponseDto;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringResponseDto;
import dev.office.networkoffice.gathering.service.GatheringService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/gathering")
@RequiredArgsConstructor
public class GatheringController implements GatheringApiDocs {

    private final GatheringService gatheringService;
    private final HttpSession httpSession;


    @GetMapping
    public ResponseEntity<GatheringListResponseDto> findGatheringList(
            @RequestParam("userId") Long userId,
            @RequestParam("si") String si,
            @RequestParam("dong") String dong,
            @RequestParam("gu") String gu
    ) {
        return ResponseEntity.ok(gatheringService.getGatheringByPlace(Long.valueOf(userId), si, dong, gu));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<GatheringResponseDto> createGathering(Long userId, @RequestBody GatheringDto gatheringDto) {
        return ResponseEntity.ok(gatheringService.createGathering(userId, gatheringDto));
    }

    @PutMapping()
    public ResponseEntity<GatheringResponseDto> modifyGatheringByHost(@AuthenticationPrincipal Long userId, @RequestBody GatheringModifyDto gatheringModifyDto) {
        return ResponseEntity.ok(gatheringService.modifyGatheringInfoByHost(userId, gatheringModifyDto));
    }

    //delete 매핑은 requestBody를 통상 가지지 않기때문에 post로 작성.
    @PostMapping("/cancel")
    public ResponseEntity<GatheringResponseDto> cancelGatheringByHost(@AuthenticationPrincipal Long userId, GatheringCancelDto cancelDto) {
        return ResponseEntity.ok(gatheringService.cancelGatheringByHost(userId, cancelDto));
    }

}
