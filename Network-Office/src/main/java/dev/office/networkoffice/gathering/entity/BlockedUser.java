package dev.office.networkoffice.gathering.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 해당 유저로 찾는게 더 빠를까 모임 아이디로 찾는게 더 빠를까
 * 차단된 유저를 찾아야하는 경우의 수를 생각해보자.
 *
 */
@Entity
@Getter
@Table(name = "blocked_users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlockedUser {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long userId;
	private Long gatheringId;
	private String reason;

	public void writeReasonsForRejection(String reason){
		this.reason = reason;
	}
}
