package kr.co.reyonpharm.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class PageParam
{
	private int pageNo;
	private int pageSize;
	private int startRow;
	private int endRow;
	private int totalCount;
	private @Getter @Setter String searchType; // 검색 조건
	private @Getter @Setter String searchText; // 검색어
	private @Getter @Setter String startDate; // 검색 시작일
	private @Getter @Setter String endDate; // 검색 종료일
	private @Getter @Setter String searchStat; // 검색 상태
	private @Getter @Setter String queStr;// 검색후 원래 페이지로 이동할 때 사용되는 쿼리스트링
	
	private @Getter @Setter String saupCode; // 사업장 코드
	private @Getter @Setter String deptCode; // 부서 코드
	private @Getter @Setter List<String> deptCodeList; // 부서 코드 리스트
	private @Getter @Setter String sabun; // 사번
	private @Getter @Setter List<String> sabunList; // 사번 리스트
	private @Getter @Setter String meetingType; // 회의종류
	private @Getter @Setter String attSabun; // 참석자, 참고인 사번
	private @Getter @Setter List<String> attSabunList; // 참석자, 참고인 사번 리스트
	private @Getter @Setter String visitSeq; // 방문 고유번호
	
	private @Getter @Setter String s_projectName; // 프로젝트 이름
	private @Getter @Setter String s_deptCode; // 등록 부서 코드
	private @Getter @Setter String s_refdeptCode; // 담당 부서 코드
	private @Getter @Setter String s_regName; // 담당자
	private @Getter @Setter String s_status; // 상태
	private @Getter @Setter String s_meetingName; // 회의제목
	private @Getter @Setter String s_meetingPlace; // 회의장소
	private @Getter @Setter String s_visitCompany; // 방문업체
	private @Getter @Setter String s_visitName; // 방문자명
	private @Getter @Setter String s_divide; // 분류
	private @Getter @Setter String s_gubun; // 구분
	private @Getter @Setter String s_user; // 사용자
	private @Getter @Setter String s_paid; // 지급구분
	private @Getter @Setter String s_carnum; // 차량번호
	private @Getter @Setter String s_cartype; // 차량종류
	private @Getter @Setter String s_sabun; // 사번
	private @Getter @Setter String s_ip; // 아이피
	private @Getter @Setter String s_contents; // 내용
	private @Getter @Setter String regDate; // 등록일자
	private @Getter @Setter String sendDate; // 발신일자
	private @Getter @Setter String receiveDate; // 수신일자
	private @Getter @Setter String s_grpCode; // 그룹코드
	private @Getter @Setter String s_grpName; // 그룹이름
	private @Getter @Setter String s_codeName; // 코드이름
	private @Getter @Setter String s_useGbn; // 사용구분
	private @Getter @Setter String s_reqComment; // 사유
	private @Getter @Setter String s_scheduleType; // 일정 종류
	private @Getter @Setter String s_scheduleName; // 일정 이름
	private @Getter @Setter String s_remark; // 상세 내용
	
	private @Getter @Setter String s_auth; // 상세 내용
	private @Getter @Setter String s_content; // 상세 내용
	private @Getter @Setter String s_emrg; // 상세 내용
	private @Getter @Setter String s_wDate; // 상세 내용
	
	public PageParam () {
		this.totalCount = 0;
		this.pageNo = 0; // 페이지 번호
		this.pageSize = 0; // 페이지 당 글 수
		this.startRow = 1; // 쿼리로 조회시 시작 로우 번호 (기본값 1)
		this.endRow = 10; // 쿼리로 조회시 마지막 로우 번호 (기본값 10)
	}
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize==0?1:pageSize;
		if (this.pageNo != 0) {
			this.startRow = (this.pageNo - 1) * pageSize + 1;
			this.endRow = (this.pageNo - 1) * pageSize + pageSize;
		}
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
		if (this.pageSize != 0) {
			this.startRow = (pageNo - 1) * this.pageSize + 1;
			this.endRow = (pageNo - 1) * this.pageSize + this.pageSize;
		}
	}
	
	public int getStartRow() {
		return startRow;
	}
	public int getEndRow() {
		return endRow;
	}

	public int getTotalCount() {
		return totalCount;
	}
	
	public int setTotalCount(int totalCount) {
		return this.totalCount = totalCount;
	}
	
}
