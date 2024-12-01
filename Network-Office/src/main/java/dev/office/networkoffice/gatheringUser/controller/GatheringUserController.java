package dev.office.networkoffice.gatheringUser.controller;

import dev.office.networkoffice.gatheringUser.controller.docs.GatheringUserApiDocs;
import dev.office.networkoffice.gatheringUser.controller.dto.request.ChangeStatusDto;
import dev.office.networkoffice.gatheringUser.controller.dto.response.ApplicantUserDto;
import dev.office.networkoffice.gatheringUser.service.GatheringUserService;
import dev.office.networkoffice.global.annotation.CurrentUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/gathering-user")
public class GatheringUserController implements GatheringUserApiDocs {

    private final GatheringUserService gatheringUserService;

    @PostMapping("{gatheringId}")
    public void applyGathering(@CurrentUserId Long userId,
                               @PathVariable(name = "gatheringId") Long gatheringId) {
        gatheringUserService.applyGathering(userId, gatheringId);
    }

    @GetMapping("{gatheringId}")
    public ApplicantUserDto getApplicantsInGatheringByHost(@CurrentUserId Long userId,
                                                           @PathVariable(name = "gatheringId") Long gatheringId) {
        return gatheringUserService.getApplicantsByHost(userId, gatheringId);
    }

    @PatchMapping("{applicantId}/status")
    public void patchApplicantStatusByHost(@CurrentUserId Long userId,
                                           @PathVariable(name = "applicantId") Long applicantId,
                                           @RequestBody ChangeStatusDto changeStatusDto) {
        gatheringUserService.patchApplicantStatus(userId, applicantId, changeStatusDto);
    }
}
