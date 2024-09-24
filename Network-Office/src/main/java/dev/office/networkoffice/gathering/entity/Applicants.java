package dev.office.networkoffice.gathering.entity;

import dev.office.networkoffice.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 신청 테이블을 따로 작성한다.
 * 신청을 넣는다 ->  신청 엔티티를 생성한다.
 * 신청을 수락한다. -> 신청 엔티티를 제거하고 모임유저에 추가.
 * 신청을 거절한다. -> 신청 엔티티를 제거한다.
 */
@Entity
@Getter
@Table(name = "applicants")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Applicants {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "applicant_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gathering_id")
	private Gathering gathering;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Builder
	private Applicants(Long id, Gathering gathering, User user) {
		this.id = id;
		this.gathering = gathering;
		this.user = user;
	}
}
