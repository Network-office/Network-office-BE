package dev.office.networkoffice.gathering.service;

import java.util.List;

import dev.office.networkoffice.gathering.controller.dto.request.GatheringCancelDto;
import dev.office.networkoffice.gathering.controller.dto.request.GatheringSuccessDto;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringClosedResponse;
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
        User host = findUser(userId);

        Gathering gathering = gatheringRepository.save(
                Gathering.builder()
                        .category(Category.valueOf(dto.category()))
                        .title(dto.title())
                        .description(dto.description())
                        .placeInfo(dto.placeInfoConstructor())
                        .timeInfo(dto.timeInfoConstructor())
                        .host(host)
                        .build()
        );
        return GatheringResponseDto.from(gathering);
    }

    //모임 정보 시동구 조회
    @Transactional(readOnly = true)
    public GatheringListResponseDto getGatheringByPlace(Long userId, String si, String dong, String gu) {
        List<GatheringResponseDto> gatherings = gatheringRepository.findDetailAddressBySiAndDongAndGu(si, dong, gu)
                .stream()
                .filter(this::isGatheringInProgress)
                .map(GatheringResponseDto::from)
                .toList();
        return GatheringListResponseDto.from(gatherings);
    }

    private boolean isGatheringInProgress(Gathering gathering){
        return gathering.getGatheringStatus()
                .equals(GatheringStatus.IN_PROGRESS);
    }

    //모임 수정
    @Transactional
    public GatheringResponseDto modifyGatheringInfoByHost(Long hostId, GatheringModifyDto modifyDto) {
        Gathering gathering = verifyHostAndFindGathering(hostId, modifyDto.id());

        gathering.modifyGatheringInfo(
                modifyDto.title(),
                modifyDto.description(),
                Category.valueOf(modifyDto.category()),
                modifyDto.placeInfoConstructor(),
                modifyDto.timeInfoConstructor()
        );

        return GatheringResponseDto.from(gathering);
    }

    /**
     * 모임 파토. 호스트만 지울 수 있고, 지우기 전에 사유를 기록해야됨.
     *
     * @param hostId
     * @param cancelDto
     */
    @Transactional
    public GatheringClosedResponse cancelGatheringByHost(Long hostId, GatheringCancelDto cancelDto) {
        Gathering gathering = verifyHostAndFindGathering(hostId, cancelDto.gatheringId());

        gathering.changeStatusToCancel(cancelDto.reason());

        return GatheringClosedResponse.from(gathering.getGatheringStatus().name());
    }

    @Transactional
    public GatheringClosedResponse successGatheringByHost(Long hostId, GatheringSuccessDto successDto) {
        Gathering gathering = verifyHostAndFindGathering(hostId, successDto.gatheringId());

        gathering.changeStatusToSuccessFul(successDto.review(), successDto.star());

        return GatheringClosedResponse.from(gathering.getGatheringStatus().name());
    }

    private Gathering verifyHostAndFindGathering(Long hostId, Long gatheringId) {
        Gathering gathering = findGathering(gatheringId);

        User host = findUser(hostId);

        if (gathering.getHost().equals(host)) {
            return gathering;
        }

        throw new IllegalStateException("권한이 없습니다.");
    }

    private User findUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(
                () -> new IllegalArgumentException("유저 없음.")
        );
    }

    private Gathering findGathering(Long gatheringId){
        return gatheringRepository.findById(gatheringId)
                .orElseThrow(
                () -> new IllegalArgumentException("모임없음.")
        );
    }

}
