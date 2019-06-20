package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class TicketInfo {
	private @Getter @Setter String rnum;
	private @Getter @Setter String parkingSeq;
	private @Getter @Setter String visitSeq;
	private @Getter @Setter String visitCompany;
	private @Getter @Setter String visitName;
	private @Getter @Setter String visitPurpose;
	private @Getter @Setter String giveDate;
	private @Getter @Setter String countHour2;
	private @Getter @Setter String countHour3;
	private @Getter @Setter String countHour4;
	private @Getter @Setter String countHour6;
	private @Getter @Setter String countHour8;
	private @Getter @Setter String countHour10;
	private @Getter @Setter String countHour24;
	private @Getter @Setter String status;
	private @Getter @Setter String deptCode;
	private @Getter @Setter String deptName;
	private @Getter @Setter String regSabun;
	private @Getter @Setter String regName;
	private @Getter @Setter String regDate;
	private @Getter @Setter String updDate;
	private @Getter @Setter String refDeptCode;
	private @Getter @Setter String refDeptName;
	private @Getter @Setter String refDeptParco;
	private @Getter @Setter String confirmSabun;
	private @Getter @Setter String confirmName;
	private @Getter @Setter String totalCount;
	private @Getter @Setter String isWebSale;
	private @Getter @Setter String webSalePrice;
}
