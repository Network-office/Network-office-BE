package dev.office.networkoffice.gathering.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.office.networkoffice.gathering.entity.Applicants;
import dev.office.networkoffice.gathering.entity.Gathering;
import dev.office.networkoffice.gathering.repository.ApplicantRepository;
import dev.office.networkoffice.gathering.repository.GatheringRepository;
import dev.office.networkoffice.user.entity.User;
import dev.office.networkoffice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GatheringApplyService {

	private final ApplicantRepository applicantRepository;
	private final GatheringRepository gatheringRepository;
	private final UserRepository userRepository;
	private final GatheringAuthorityManagerService managerService;

	/**
	 * 모임 신청.
	 * @param applicantId : 신청자 아이디
	 * @param gatheringId : 신청하려는 모임 아이디
	 */
	@Transactional
	public void applyGathering(Long applicantId, Long gatheringId){
		User user = userRepository.findById(applicantId)
			.orElseThrow(()-> new RuntimeException("유저없음"));//신청자 검증
		Gathering gathering = gatheringRepository.findById(gatheringId)
			.orElseThrow(()-> new RuntimeException("모임없음")); //모임검증
		//기존 모임에 이미 user가 있는지 체크

		//신청 엔티티 생성
		Applicants applicants = Applicants.builder()
			.gathering(gathering)
			.user(user)
			.build();
		applicantRepository.save(applicants);
	}

	/**
	 * 호스트가 볼 수 있는 모임 신청자들 목록
	 * @param hostId
	 * @param gatheringId
	 */

	@Transactional(readOnly = true)
	public List<Applicants> readApplicantsByGathering(Long hostId, Long gatheringId){
		Gathering gathering = gatheringRepository.findById(gatheringId)
			.orElseThrow(()-> new RuntimeException("모임없음"));
		if ((gathering.getHostId().equals(hostId))){ //해당 유저가 호스트인지 검사한다.
			throw new RuntimeException("호스트가 아니라 조회 할 수 없다.");
		}
		return applicantRepository.findAllByGathering(gathering);
	}

	/**
	 * 모임신청을 거절하면? 그냥 신청을 삭제
	 * @param hostId
	 * @param applicantId
	 */
	@Transactional
	public void denyApplicants(Long hostId, Long applicantId){
		Applicants applicants = findApplicantsByHost(hostId, applicantId);
		applicantRepository.delete(applicants);
	}

	/**
	 * 모임 신청을 수락하면? -> 모임 확정된 인원으로 추가.
	 */
	@Transactional
	public void approvedApplicants(Long hostId, Long applicantId){
		Applicants applicants = findApplicantsByHost(hostId,applicantId);
		managerService.createConfirmedUserAuthority(applicants.getGathering(), applicants.getUser());
		applicantRepository.delete(applicants);
	}

	private Applicants findApplicantsByHost(Long hostId, Long applicantId){
		Applicants applicants = applicantRepository.findById(applicantId)
			.orElseThrow(() -> new RuntimeException("신청id로 찾음"));

		if (!(applicants.getGathering().getHostId().equals(hostId))){
			throw new RuntimeException("호스트나 모임 일치하지않음.");
		}
		return applicants;
	}
}
