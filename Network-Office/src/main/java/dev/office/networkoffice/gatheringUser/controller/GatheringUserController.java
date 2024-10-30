package dev.office.networkoffice.gatheringUser.controller;

import dev.office.networkoffice.gatheringUser.controller.docs.GatheringUserApiDocs;
import dev.office.networkoffice.gatheringUser.controller.dto.request.ChangeStatusDto;
import dev.office.networkoffice.gatheringUser.controller.dto.response.ApplicantUserDto;
import dev.office.networkoffice.gatheringUser.service.GatheringUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/gathering-user")
public class GatheringUserController implements GatheringUserApiDocs {

    private final GatheringUserService gatheringUserService;

    @PostMapping("{gatheringId}")
    public void applyGathering(Principal principal,
                               @PathVariable(name = "gatheringId") Long gatheringId) {
        Long userId = getUserId(principal);
        gatheringUserService.applyGathering(userId, gatheringId);
    }

    @GetMapping("{gatheringId}")
    public ApplicantUserDto getApplicantsInGatheringByHost(Principal principal,
                                                           @PathVariable(name = "gatheringId") Long gatheringId) {
        Long userId = getUserId(principal);
        return gatheringUserService.getApplicantsByHost(userId, gatheringId);
    }

    @PatchMapping("{applicantId}/status")
    public void patchApplicantStatusByHost(Principal principal,
                                           @PathVariable(name = "applicantId") Long applicantId,
                                           @RequestBody ChangeStatusDto changeStatusDto) {
        Long userId = getUserId(principal);
        gatheringUserService.patchApplicantStatus(userId, applicantId, changeStatusDto);
    }

    private Long getUserId(Principal principal) {
        return Long.parseLong(principal.getName());
    }
}
