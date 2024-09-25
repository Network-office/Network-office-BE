package dev.office.networkoffice.gathering.entity;

import dev.office.networkoffice.gathering.domain.DeletedType;
import jakarta.persistence.Column;
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

//TODO: 작성하다보니 삭제되는 이유가 파토, 신고, 성공적 마침 3가지가 존재할 것같다. 따라서 인터페이스로 따로 빼고 고쳐야할듯하다.
@Entity
@Getter
@Table(name = "deleted_gatherings")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeletedGathering {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "deleted_gathering_id")
    private Long id;

    private String reason;

    private DeletedType deletedType;

    @Builder
    private DeletedGathering(String reason, DeletedType deletedType) {
        this.reason = reason;
        this.deletedType = deletedType;
    }

    public void updateReasonsForDeleted(String reason) {
        this.reason = reason;
    }
}
