package dev.office.networkoffice.gathering.service;

import java.util.List;

import dev.office.networkoffice.gathering.controller.dto.request.GatheringCancelDto;
import dev.office.networkoffice.gathering.controller.dto.request.GatheringSuccessDto;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringDeleteResponse;
import dev.office.networkoffice.gathering.domain.GatheringStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.office.networkoffice.gathering.controller.dto.request.GatheringDto;
import dev.office.networkoffice.gathering.controller.dto.request.GatheringModifyDto;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringListResponseDto;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringResponseDto;
import dev.office.networkoffice.gathering.domain.Category;
import dev.office.networkoffice.gathering.entity.Gathering;
import dev.office.networkoffice.gathering.repository.GatheringRepository;
import dev.office.networkoffice.user.entity.User;
import dev.office.networkoffice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GatheringService {

    private final GatheringRepository gatheringRepository;
    private final UserRepository userRepository;

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
                        .host(host)
                        .status(GatheringStatus.IN_PROGRESS)
                        .build()
        );
        return GatheringResponseDto.from(gathering);
    }

    //모임 정보 시동구 조회
    @Transactional(readOnly = true)
    public GatheringListResponseDto getGatheringByPlace(Long userId, String si, String dong, String gu) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        List<GatheringResponseDto> gatherings = gatheringRepository.findDetailAddressBySiAndDongAndGu(si, dong, gu).stream()
                .filter((gathering) -> gathering.getGatheringStatus().equals(GatheringStatus.IN_PROGRESS))
                .map(GatheringResponseDto::from)
                .toList();
        return GatheringListResponseDto.from(gatherings);
    }

    //모임 수정
    @Transactional
    public GatheringResponseDto modifyGatheringInfoByHost(Long hostId, GatheringModifyDto modifyDto) {
        Gathering gathering = verifyHostAndFindGathering(hostId, modifyDto.id());
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
    public GatheringDeleteResponse cancelGatheringByHost(Long hostId, GatheringCancelDto cancelDto) {
        Gathering gathering = verifyHostAndFindGathering(hostId, cancelDto.gatheringId());

        gathering.changeStatusToCancel(cancelDto.reason());

        return GatheringDeleteResponse.from(gathering.getGatheringStatus().name());
    }

    @Transactional
    public GatheringDeleteResponse successGatheringByHost(Long hostId, GatheringSuccessDto successDto) {
        Gathering gathering = verifyHostAndFindGathering(hostId, successDto.gatheringId());

        gathering.changeStatusToSuccessFul(successDto.review(), successDto.star());

        return GatheringDeleteResponse.from(gathering.getGatheringStatus().name());
    }

    private Gathering verifyHostAndFindGathering(Long hostId, Long gatheringId) {
        Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(
                () -> new IllegalArgumentException("모임없음.")
        );

        User host = userRepository.findById(hostId).orElseThrow(
                () -> new IllegalArgumentException("유저 없음.")
        );

        if (gathering.getHost().equals(host)) {
            return gathering;
        }

        throw new IllegalStateException("권한이 없습니다.");
    }

}
