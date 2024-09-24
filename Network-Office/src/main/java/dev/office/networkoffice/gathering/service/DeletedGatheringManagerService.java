package dev.office.networkoffice.gathering.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.office.networkoffice.gathering.entity.DeletedGathering;
import dev.office.networkoffice.gathering.entity.DeletedGatheringManager;
import dev.office.networkoffice.gathering.repository.DeletedGatheringManagerRepository;
import dev.office.networkoffice.user.entity.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeletedGatheringManagerService {

	private final DeletedGatheringManagerRepository deletedGatheringManagerRepository;

	public Optional<DeletedGatheringManager> findDeletedGatheringByPastId(Long gatheringId){
		return deletedGatheringManagerRepository.findByOriginGatheringId(gatheringId);
	}

	@Transactional
	public DeletedGatheringManager savingDeletedGathering(User host, DeletedGathering gathering){
		return deletedGatheringManagerRepository.save(
			DeletedGatheringManager.builder()
			.user(host)
			.deletedGathering(gathering)
			.build()
		);
	}
}
