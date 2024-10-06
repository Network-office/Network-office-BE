package dev.office.networkoffice.gatheringUser.controller;

import dev.office.networkoffice.gatheringUser.controller.docs.GatheringUserApiDocs;
import dev.office.networkoffice.gatheringUser.controller.request.DenyUserDto;
import dev.office.networkoffice.gatheringUser.controller.response.ApplicantUserDto;
import dev.office.networkoffice.gatheringUser.service.GatheringUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gathering-user")
public class GatheringUserApiController implements GatheringUserApiDocs {

    private final GatheringUserService gatheringUserService;

    @PostMapping("/{gatheringId}")
    public ResponseEntity<Void> applyGathering(Long userId, @PathVariable(name = "gatheringId") Long gatheringId) {
        gatheringUserService.applyGathering(userId, gatheringId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{gatheringId}")
    public ResponseEntity<ApplicantUserDto> getApplicantsInGatheringByHost(Long userId, @PathVariable(name = "gatheringId") Long gatheringId) {
        return ResponseEntity.ok(gatheringUserService.readApplicantsByGathering(userId, gatheringId));
    }

    @PatchMapping("/deny")
    public ResponseEntity<Void> denyApplicants(Long userId, @RequestBody DenyUserDto denyUserDto) {
        gatheringUserService.denyApplicants(userId, denyUserDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/deports")
    public ResponseEntity<Void> deportsUser(Long userId, @RequestBody DenyUserDto denyUserDto) {
        gatheringUserService.deportsUser(userId, denyUserDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/accept")
    public ResponseEntity<Void> approvedApplicants(Long userId, @PathVariable(name = "applicantId") Long applicantId) {
        gatheringUserService.approvedApplicants(userId, applicantId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/block")
    public ResponseEntity<Void> blockUserInGathering(Long userId, @RequestBody DenyUserDto denyUserDto) {
        gatheringUserService.blockedUserInGathering(userId, denyUserDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
