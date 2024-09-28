package dev.office.networkoffice.gatheringAuthority.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.office.networkoffice.gathering.entity.Gathering;
import dev.office.networkoffice.gatheringAuthority.domain.GatheringAuthority;
import dev.office.networkoffice.gatheringAuthority.domain.GatheringAuthorityManager;
import dev.office.networkoffice.gatheringAuthority.repository.GatheringAuthorityRepository;
import dev.office.networkoffice.user.entity.User;
import lombok.RequiredArgsConstructor;

/**
 * 모임에 대해 호스트 권한, 확정된 인원 권한을 부여하는 서비스
 */
@Service
@RequiredArgsConstructor
public class GatheringAuthorityManagerService {

    private final GatheringAuthorityRepository gatheringAuthorityRepository;

    @Transactional
    public void createHostAuthority(Gathering gathering, User host) {
        gatheringAuthorityRepository.save(GatheringAuthorityManager.builder()
                .gatheringAuthority(GatheringAuthority.HOST_USER)
                .gathering(gathering)
                .user(host)
                .build());
    }

    @Transactional
    public void createConfirmedUserAuthority(Gathering gathering, User confirmedUser) {
        gatheringAuthorityRepository.save(GatheringAuthorityManager.builder()
                .gatheringAuthority(GatheringAuthority.CONFIRMED_USER)
                .gathering(gathering)
                .user(confirmedUser)
                .build());
    }

    public GatheringAuthorityManager findAuthorityManager_withHostIdAndGatheringId(Long hostId, Long gatheringId) {
        //TODO: not found exception
        return gatheringAuthorityRepository.findByGatheringAndUser(gatheringId, hostId, GatheringAuthority.HOST_USER).orElseThrow();
    }
}
