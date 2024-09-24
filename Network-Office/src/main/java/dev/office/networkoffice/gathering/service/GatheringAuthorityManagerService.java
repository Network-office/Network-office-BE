package dev.office.networkoffice.gathering.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.office.networkoffice.gathering.entity.Gathering;
import dev.office.networkoffice.gathering.domain.GatheringAuthority;
import dev.office.networkoffice.gathering.entity.GatheringUserConfirmManager;
import dev.office.networkoffice.gathering.repository.GatheringManagerRepository;
import dev.office.networkoffice.user.entity.User;
import lombok.RequiredArgsConstructor;

/**
 * 모임에 대해 호스트 권한, 확정된 인원 권한을 부여하는 서비스
 */
@Service
@RequiredArgsConstructor
public class GatheringAuthorityManagerService {

	private final GatheringManagerRepository gatheringManagerRepository;

	@Transactional
	public void createHostAuthority(Gathering gathering, User host){
		gatheringManagerRepository.save(GatheringUserConfirmManager.builder()
			.gatheringAuthority(GatheringAuthority.HOST)
			.gathering(gathering)
			.user(host)
			.build());
	}

	@Transactional
	public void createConfirmedUserAuthority(Gathering gathering, User confirmedUser){
		gatheringManagerRepository.save(GatheringUserConfirmManager.builder()
			.gatheringAuthority(GatheringAuthority.CONFIRMED)
			.gathering(gathering)
			.user(confirmedUser)
			.build());
	}

	public GatheringUserConfirmManager findAuthorityManager_withHostIdAndGatheringId(Long hostId, Long gatheringId){
		//TODO: not found exception
		return gatheringManagerRepository.findByGatheringAndUser(gatheringId,hostId,GatheringAuthority.HOST).orElseThrow();
	}
}
