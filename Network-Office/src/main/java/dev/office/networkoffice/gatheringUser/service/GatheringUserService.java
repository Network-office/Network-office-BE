package dev.office.networkoffice.gatheringUser.service;

import dev.office.networkoffice.gathering.domain.ReasonForDeportation;
import dev.office.networkoffice.gatheringUser.controller.request.DenyUserDto;
import dev.office.networkoffice.gatheringUser.controller.response.ApplicantUserDto;
import dev.office.networkoffice.gatheringUser.domain.GatheringUserStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.office.networkoffice.gatheringUser.domain.GatheringUser;
import dev.office.networkoffice.gathering.entity.Gathering;
import dev.office.networkoffice.gatheringUser.repository.GatheringUserRepository;
import dev.office.networkoffice.gathering.repository.GatheringRepository;
import dev.office.networkoffice.user.entity.User;
import dev.office.networkoffice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GatheringUserService {

    private final GatheringUserRepository gatheringUserRepository;
    private final GatheringRepository gatheringRepository;
    private final UserRepository userRepository;

    /**
     * 모임 신청.
     * @param userId      : 신청자 아이디
     * @param gatheringId : 신청하려는 모임 아이디
     */
    @Transactional
    public void applyGathering(Long userId, Long gatheringId) {
        User user = getUserById(userId);
        Gathering gathering = getGatheringById(gatheringId);
        verifyExistApplicant(user,gathering);

        //신청 엔티티 생성
        GatheringUser gatheringUser = GatheringUser.builder()
                .gathering(gathering)
                .user(user)
                .gatheringUserStatus(GatheringUserStatus.APPLY_USER)
                .build();
        gatheringUserRepository.save(gatheringUser);
    }

    /**
     * 호스트가 볼 수 있는 모임 신청자들 목록
     * @param hostId
     * @param gatheringId
     */
    @Transactional(readOnly = true)
    public ApplicantUserDto readApplicantsByGathering(Long hostId, Long gatheringId) {
        Gathering gathering = getGatheringById(gatheringId);
        isHostInGathering(gathering.getHost(), hostId);
        return new ApplicantUserDto(gatheringUserRepository.findAllByGatheringAndGatheringUserStatus(gathering, GatheringUserStatus.APPLY_USER));
    }

    /**
     * 모임신청을 거절하면? 거절 사유를 작성하고 변경.
     * @param hostId
     * @param denyUserDto
     */
    @Transactional
    public void denyApplicants(Long hostId, DenyUserDto denyUserDto) {
        GatheringUser gatheringUser = getGatheringUserById(denyUserDto.applicantId());
        isHostInGathering(gatheringUser.getGathering().getHost(), hostId);
        gatheringUser.updateGatheringUserStatus(GatheringUserStatus.DENIED_USER);
        gatheringUser.updateDeniedReason(denyUserDto.reason());
    }

    /**
     * 유저를 사유와 함께 추방합니다.
     * @param hostId
     * @param denyUserDto
     */
    @Transactional
    public void deportsUser(Long hostId, DenyUserDto denyUserDto) {
        GatheringUser gatheringUser = getGatheringUserById(denyUserDto.applicantId());
        isHostInGathering(gatheringUser.getGathering().getHost(), hostId);
        gatheringUser.updateGatheringUserStatus(GatheringUserStatus.DEPORTATION_USER);
        gatheringUser.updateDeportationReason(ReasonForDeportation.valueOf(denyUserDto.reason()));
    }

    /**
     * 모임 신청을 수락하면? -> 모임 확정된 인원으로 추가.
     */
    @Transactional
    public void approvedApplicants(Long hostId, Long gatheringUserId) {
        GatheringUser gatheringUser = getGatheringUserById(gatheringUserId);
        isHostInGathering(gatheringUser.getGathering().getHost(),hostId);
        gatheringUser.updateGatheringUserStatus(GatheringUserStatus.CONFIRMED_USER);
    }

    @Transactional
    public void blockedUserInGathering(Long hostId, DenyUserDto denyUserDto) {
        GatheringUser gatheringUser = getGatheringUserById(denyUserDto.applicantId());
        isHostInGathering(gatheringUser.getGathering().getHost(),hostId);
        gatheringUser.updateGatheringUserStatus(GatheringUserStatus.BLOCKED_USER);
        gatheringUser.updateDeportationReason(ReasonForDeportation.valueOf(denyUserDto.reason()));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
    }

    private Gathering getGatheringById(Long gatheringId) {
        return gatheringRepository.findById(gatheringId)
                .orElseThrow(() -> new IllegalArgumentException("모임 없음"));
    }

    private GatheringUser getGatheringUserById(Long gatheringUserId){
        return gatheringUserRepository.findById(gatheringUserId)
                .orElseThrow(() -> new IllegalArgumentException("신청 없음"));
    }

    private void verifyExistApplicant(User user, Gathering gathering) {

        GatheringUser gatheringUser = gatheringUserRepository.findByUserAndGathering(user, gathering);
        if (gatheringUser!=null){
            switch (gatheringUser.getGatheringUserStatus()){
                case DEPORTATION_USER, BLOCKED_USER -> {
                    throw new IllegalArgumentException(gatheringUser.getReasonForDeportation().name());
                }
            }
        }
    }

    private void isHostInGathering(User gatheringHost, Long hostId){
        if (!gatheringHost.getId().equals(hostId)){
            throw new IllegalArgumentException("권한이 없는 유저입니다.");
        }
    }
}
