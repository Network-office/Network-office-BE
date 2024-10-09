package dev.office.networkoffice.gatheringUser.service;

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

import java.util.List;

@Service
@RequiredArgsConstructor
public class GatheringUserService {

    private final GatheringUserRepository gatheringUserRepository;
    private final GatheringRepository gatheringRepository;
    private final UserRepository userRepository;

    /**
     * 모임 신청
     *
     * @param userId      : 신청자 아이디
     * @param gatheringId : 신청하려는 모임 아이디
     */
    @Transactional
    public void applyGathering(Long userId, Long gatheringId) {
        User user = getUserById(userId);
        Gathering gathering = getGatheringById(gatheringId);
        validateUserApplicationEligibility(user, gathering);
        GatheringUser gatheringUser = GatheringUser.builder()
                .gathering(gathering)
                .user(user)
                .build();
        gatheringUserRepository.save(gatheringUser);
    }

    /**
     * 호스트가 볼 수 있는 모임 신청자들 목록
     */
    @Transactional(readOnly = true)
    public ApplicantUserDto getApplicantsByHost(Long hostId, Long gatheringId) {
        List<GatheringUser> applicants = gatheringUserRepository.findApplicantsInGatheringByHost(gatheringId, hostId, GatheringUserStatus.APPLY_USER);
        return new ApplicantUserDto(applicants);
    }

    /**
     * 모임신청을 거절하면? 거절 사유를 작성하고 변경.
     */
    @Transactional
    public void denyApplicants(Long hostId, DenyUserDto denyUserDto) {
        GatheringUser gatheringUser = getGatheringUserByHost(denyUserDto.applicantId(), hostId);
        gatheringUser.denyApplicants(denyUserDto.reason());
    }

    /**
     * 유저를 사유와 함께 추방합니다.
     */
    @Transactional
    public void deportsUser(Long hostId, DenyUserDto denyUserDto) {
        GatheringUser gatheringUser = getGatheringUserByHost(denyUserDto.applicantId(), hostId);
        gatheringUser.deportApplicants(denyUserDto.reason());
    }

    /**
     * 모임 신청을 수락하면? -> 모임 확정된 인원으로 추가.
     */
    @Transactional
    public void approvedApplicants(Long hostId, Long gatheringUserId) {
        GatheringUser gatheringUser = getGatheringUserByHost(gatheringUserId, hostId);
        gatheringUser.confirmApplicants();
    }

    @Transactional
    public void blockedUserInGathering(Long hostId, DenyUserDto denyUserDto) {
        GatheringUser gatheringUser = getGatheringUserByHost(denyUserDto.applicantId(), hostId);
        gatheringUser.blockApplicants(denyUserDto.reason());
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    private Gathering getGatheringById(Long gatheringId) {
        return gatheringRepository.findById(gatheringId)
                .orElseThrow(() -> new IllegalArgumentException("모임을 찾을 수 없습니다."));
    }

    private GatheringUser getGatheringUserByHost(Long gatheringUserId, Long userId) {
        return gatheringUserRepository.findGatheringUserByHost(gatheringUserId, userId)
                .orElseThrow(() -> new IllegalArgumentException("신청 이력을 찾을 수 없습니다."));
    }

    private void validateUserApplicationEligibility(User user, Gathering gathering) {
        if (gathering.isHost(user)) {
            throw new IllegalStateException("이미 호스트입니다.");
        }
        GatheringUser gatheringUser = findGatheringUser(user, gathering);
        if (gatheringUser == null) {
            return;
        }

        if (!gatheringUser.isEligibleForReapplication()) {
            throw new IllegalArgumentException("다시 신청할 수 없는 상태입니다.");
        }
    }

    private GatheringUser findGatheringUser(User user, Gathering gathering) {
        return gatheringUserRepository.findByUserAndGathering(user, gathering)
                .orElse(null);
    }
}
