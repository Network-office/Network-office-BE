package dev.office.networkoffice.gatheringUser.controller;

import dev.office.networkoffice.gatheringUser.controller.docs.GatheringUserApiDocs;
import dev.office.networkoffice.gatheringUser.controller.request.DenyUserDto;
import dev.office.networkoffice.gatheringUser.controller.response.ApplicantUserDto;
import dev.office.networkoffice.gatheringUser.service.GatheringUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gathering-user")
public class GatheringUserController implements GatheringUserApiDocs {

    private final GatheringUserService gatheringUserService;

    @PostMapping("/{gatheringId}")
    public ResponseEntity<Void> applyGathering(Principal principal, @PathVariable(name = "gatheringId") Long gatheringId) {
        Long userId = findUserIdInSession(principal);
        gatheringUserService.applyGathering(userId, gatheringId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{gatheringId}")
    public ResponseEntity<ApplicantUserDto> getApplicantsInGatheringByHost(Principal principal, @PathVariable(name = "gatheringId") Long gatheringId) {
        Long userId = findUserIdInSession(principal);
        return ResponseEntity.ok(gatheringUserService.readApplicantsByGathering(userId, gatheringId));
    }

    @PatchMapping("/deny")
    public ResponseEntity<Void> denyApplicants(Principal principal, @RequestBody DenyUserDto denyUserDto) {
        Long userId = findUserIdInSession(principal);
        gatheringUserService.denyApplicants(userId, denyUserDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/deports")
    public ResponseEntity<Void> deportsUser(Principal principal, @RequestBody DenyUserDto denyUserDto) {
        Long userId = findUserIdInSession(principal);
        gatheringUserService.deportsUser(userId, denyUserDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/accept")
    public ResponseEntity<Void> approvedApplicants(Principal principal, @PathVariable(name = "applicantId") Long applicantId) {
        Long userId = findUserIdInSession(principal);
        gatheringUserService.approvedApplicants(userId, applicantId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/block")
    public ResponseEntity<Void> blockUserInGathering(Principal principal, @RequestBody DenyUserDto denyUserDto) {
        Long userId = findUserIdInSession(principal);
        gatheringUserService.blockedUserInGathering(userId, denyUserDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private Long findUserIdInSession(Principal principal){
        return Long.parseLong(principal.getName());
    }


}
