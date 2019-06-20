package kr.co.reyonpharm.models;

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
	private @Getter @Setter String searchStat; // 검색 상태
	private @Getter @Setter String queStr;     // 검색후 원래 페이지로 이동할 때 사용되는 쿼리스트링
	
	private @Getter @Setter String username; // 사업자 번호
	private @Getter @Setter String saupName; // 사업자 이름
	private @Getter @Setter String userRole; // 권한
	private @Getter @Setter String useYn;    // 사용구분
	private @Getter @Setter String title;    // 제목
	private @Getter @Setter String contents; // 내용
	private @Getter @Setter String ip;       // IP
	
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
