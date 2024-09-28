package dev.office.networkoffice.gathering.service;

import java.util.List;

import dev.office.networkoffice.gathering.controller.dto.request.GatheringCancelDto;
import dev.office.networkoffice.gathering.domain.DeletedGatheringStatus;
import dev.office.networkoffice.gathering.domain.ReasonForCanceled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.office.networkoffice.gathering.controller.dto.request.GatheringDto;
import dev.office.networkoffice.gathering.controller.dto.request.GatheringModifyDto;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringListResponseDto;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringResponseDto;
import dev.office.networkoffice.gathering.domain.Category;
import dev.office.networkoffice.gathering.domain.GatheringStatus;
import dev.office.networkoffice.gathering.entity.DeletedGathering;
import dev.office.networkoffice.gathering.entity.Gathering;
import dev.office.networkoffice.gathering.entity.GatheringUserConfirmManager;
import dev.office.networkoffice.gathering.repository.DeletedGatheringRepository;
import dev.office.networkoffice.gathering.repository.GatheringRepository;
import dev.office.networkoffice.user.entity.User;
import dev.office.networkoffice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GatheringService {

    private final GatheringRepository gatheringRepository;
    private final UserRepository userRepository;
    private final GatheringAuthorityManagerService gatheringAuthorityManagerService;
    private final DeletedGatheringRepository deletedGatheringRepository;

    // 모임 생성
    @Transactional
    public GatheringResponseDto createGathering(Long userId, GatheringDto dto) {
        User host = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Gathering gathering = gatheringRepository.save(
                Gathering.builder()
                        .category(Category.valueOf(dto.category()))
                        .title(dto.title())
                        .description(dto.description())
                        .placeInfo(dto.placeInfoConstructor())
                        .timeInfo(dto.timeInfoConstructor())
                        .build()
        );
        gatheringAuthorityManagerService.createHostAuthority(gathering, host);
        return GatheringResponseDto.from(gathering);
    }

    //모임 정보 시동구 조회
    @Transactional(readOnly = true)
    public GatheringListResponseDto getGatheringByPlace(Long userId, String si, String dong, String gu) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        List<GatheringResponseDto> gatherings = gatheringRepository.findDetailAddressBySiAndDongAndGu(si, dong, gu).stream()
                .filter((gathering)->gathering.getGatheringStatus().equals(GatheringStatus.ACTIVE))
                .map(GatheringResponseDto::from)
                .toList();
        return GatheringListResponseDto.from(gatherings);
    }

    //모임 수정
    @Transactional
    public GatheringResponseDto modifyGatheringInfoByHost(Long hostId, GatheringModifyDto modifyDto) {
        GatheringUserConfirmManager confirmManager = gatheringAuthorityManagerService.findAuthorityManager_withHostIdAndGatheringId(hostId,
                modifyDto.id());
        Gathering gathering = confirmManager.getGathering();
        gathering.modifyGatheringInfo(modifyDto);
        return GatheringResponseDto.from(gathering);
    }

    /**
     * 모임 파토. 호스트만 지울 수 있고, 지우기 전에 사유를 기록해야됨.
     *
     * @param hostId
     * @param cancelDto
     */
    @Transactional
    public GatheringResponseDto cancelGatheringByHost(Long hostId, GatheringCancelDto cancelDto) {
        GatheringUserConfirmManager confirmManager = gatheringAuthorityManagerService.findAuthorityManager_withHostIdAndGatheringId(hostId, cancelDto.gatheringId());
        Gathering gathering = confirmManager.getGathering();
        if (gathering.getDeletedGathering() != null) {
            throw new IllegalArgumentException("이미 삭제된 모임의 id입니다.");
        }

        DeletedGathering deletedGathering = deletedGatheringRepository.save(
                DeletedGathering.builder()
                        .reasonForCanceled(cancelDto.reason())
                        .status(DeletedGatheringStatus.CANCELED)
                        .build());

        gathering.changeStatusToCanceled(GatheringStatus.CLOSED,deletedGathering);
        return GatheringResponseDto.from(gathering);

    }


}
