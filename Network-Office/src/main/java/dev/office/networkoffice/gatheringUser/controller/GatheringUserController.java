package dev.office.networkoffice.gatheringUser.controller;

import dev.office.networkoffice.gatheringUser.controller.docs.GatheringUserApiDocs;
import dev.office.networkoffice.gatheringUser.controller.request.DenyUserDto;
import dev.office.networkoffice.gatheringUser.controller.response.ApplicantUserDto;
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
        return gatheringUserService.readApplicantsByGathering(userId, gatheringId);
    }

    @PatchMapping("deny")
    public void denyApplicants(Principal principal,
                               @RequestBody DenyUserDto denyUserDto) {
        Long userId = getUserId(principal);
        gatheringUserService.denyApplicants(userId, denyUserDto);
    }

    @PatchMapping("deports")
    public void deportsUser(Principal principal,
                            @RequestBody DenyUserDto denyUserDto) {
        Long userId = getUserId(principal);
        gatheringUserService.deportsUser(userId, denyUserDto);
    }

    @PatchMapping("accept")
    public void approvedApplicants(Principal principal,
                                   @PathVariable(name = "applicantId") Long applicantId) {
        Long userId = getUserId(principal);
        gatheringUserService.approvedApplicants(userId, applicantId);
    }

    @PatchMapping("block")
    public void blockUserInGathering(Principal principal,
                                     @RequestBody DenyUserDto denyUserDto) {
        Long userId = getUserId(principal);
        gatheringUserService.blockedUserInGathering(userId, denyUserDto);
    }

    private Long getUserId(Principal principal) {
        return Long.parseLong(principal.getName());
    }
}
