package dev.office.networkoffice.gathering.entity;

import dev.office.networkoffice.gathering.domain.DeletedType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 모임을 삭제할 때는 호스트가 사유와 함께, 이 테이블에 추가한다.
 * find할 때 이 테이블에서 있는지 먼저 찾고 -> 있으면 사유만 띄우는 식으로 리젝.
 */
@Entity
@Getter
@Table(name = "deleted_gatherings")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeletedGathering {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long gatheringId;
	private String reason;
	private DeletedType deletedType;

	@Builder
	private DeletedGathering( Long userId, Long gatheringId){
		this.gatheringId = gatheringId;
	}
	public void writeReasonsForDeleted(String reason){
		this.reason = reason;
	}
}
