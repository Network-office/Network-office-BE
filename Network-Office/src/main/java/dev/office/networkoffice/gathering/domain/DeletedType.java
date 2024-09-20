package dev.office.networkoffice.gathering.domain;

/**
 * 신고 상태는 파토, 성공, 신고 이렇게 3가지 경우의 수가 있을 것 같습니다. 별점 이런것들도 고려하면 인터페이스등으로 추상화하는게 맞다고 생각합니다.
 */
public enum DeletedType {
	PATO,
	SUCCESSFUL,
	REPORT

}
