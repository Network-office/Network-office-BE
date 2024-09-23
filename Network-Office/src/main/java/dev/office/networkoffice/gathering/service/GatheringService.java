package dev.office.networkoffice.gathering.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.office.networkoffice.gathering.controller.dto.request.GatheringDto;
import dev.office.networkoffice.gathering.controller.dto.request.GatheringModifyDto;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringListResponseDto;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringResponseDto;
import dev.office.networkoffice.gathering.domain.Category;
import dev.office.networkoffice.gathering.domain.DeletedType;
import dev.office.networkoffice.gathering.domain.GatheringAuthority;
import dev.office.networkoffice.gathering.entity.DeletedGathering;
import dev.office.networkoffice.gathering.entity.DeletedGatheringManager;
import dev.office.networkoffice.gathering.entity.Gathering;
import dev.office.networkoffice.gathering.entity.GatheringUserConfirmManager;
import dev.office.networkoffice.gathering.repository.DeletedGatheringManagerRepository;
import dev.office.networkoffice.gathering.repository.DeletedGatheringRepository;
import dev.office.networkoffice.gathering.repository.GatheringManagerRepository;
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
	private final DeletedGatheringManagerService deletedGatheringManagerService;
	private final GatheringManagerRepository gatheringManagerRepository;
	private final DeletedGatheringRepository deletedGatheringRepository;

	// 모임 생성
	@Transactional
	public GatheringResponseDto createGathering(Long userId, GatheringDto dto){
		User host = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("유저 없음"));

		Gathering gathering =  gatheringRepository.save(
			Gathering.builder()
				.category(Category.valueOf(dto.category()))
				.title(dto.title())
				.description(dto.description())
				.placeInfo(dto.placeInfoConstructor())
				.timeInfo(dto.timeInfoConstructor())
			.build()
		);
		gatheringAuthorityManagerService.createHostAuthority(gathering,host);
		return GatheringResponseDto.from(gathering);
	}

	//TODO: 모임 상세 조회 -> 삭제된 모임인지 확인하는 검증 필요할듯.

	//모임 정보 시동구 조회
	@Transactional(readOnly = true)
	public GatheringListResponseDto getGatheringByPlace(Long userId, String si, String dong, String gu){
		User user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("유저 없음"));
		List<GatheringResponseDto> gatherings = gatheringRepository.findDetailAddressBySiAndDongAndGu(si,dong,gu).stream()
			.map((GatheringResponseDto::from))
			.toList();
		return GatheringListResponseDto.from(gatherings);
	}

	//모임 수정
	@Transactional
	public GatheringResponseDto modifyGatheringInfoByHost(Long hostId, GatheringModifyDto modifyDto){
		GatheringUserConfirmManager confirmManager = gatheringAuthorityManagerService.findAuthorityManager_withHostIdAndGatheringId(hostId,
			modifyDto.id());
		Gathering gathering = confirmManager.getGathering();
		gathering.modifyGatheringInfo(modifyDto);
		return GatheringResponseDto.from(gathering);
	}

	/**
	 * 호스트만 지울 수 있고, 지우기 전에 사유를 기록해야됨.
	 * @param hostId
	 * @param gatheringId
	 */
	@Transactional
	public void deleteGatheringByHost(Long hostId, Long gatheringId, String reason, DeletedType deletedType){

		GatheringUserConfirmManager confirmManager = gatheringAuthorityManagerService.findAuthorityManager_withHostIdAndGatheringId(hostId, gatheringId);
		Optional<DeletedGatheringManager> deletedGatheringManager = deletedGatheringManagerService.findDeletedGatheringByPastId(gatheringId);
		if (deletedGatheringManager.isPresent()){
			//TODO: 이미 삭제된 모임입니다. 에러
		}
		DeletedGathering deletedGathering = deletedGatheringRepository.save(
			DeletedGathering.builder()
			.reason(reason)
			.deletedType(deletedType)
			.build());
		DeletedGatheringManager gatherings =  deletedGatheringManagerService.savingDeletedGathering(
			confirmManager.getUser(),
			deletedGathering
		);
		gatheringManagerRepository.delete(confirmManager);//삭제
	}


}
