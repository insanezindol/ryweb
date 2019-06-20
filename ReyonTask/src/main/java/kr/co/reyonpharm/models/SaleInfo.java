package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class SaleInfo {
	private @Getter @Setter String rnum;
	private @Getter @Setter String yyyymm;
	private @Getter @Setter String gubun1;
	private @Getter @Setter String gijunDate;
	private @Getter @Setter String deptNm;
	private @Getter @Setter double pmokAmt;
	private @Getter @Setter double saleAmt;
	private @Getter @Setter double pmokRate;
	private @Getter @Setter double preSaleAmt;
	private @Getter @Setter double smokAmt;
	private @Getter @Setter double sukmAmt;
	private @Getter @Setter double smokRate;
	private @Getter @Setter double preSukmAmt;
	private @Getter @Setter String regDate;
}
