package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class SaleInfo {
	private @Getter @Setter String yyyymm;
	private @Getter @Setter String gubun1;
	private @Getter @Setter String gijunDate;
	private @Getter @Setter String deptNm;
	private @Getter @Setter String pmokAmt;
	private @Getter @Setter String saleAmt;
	private @Getter @Setter String pmokRate;
	private @Getter @Setter String preSaleAmt;
	private @Getter @Setter String smokAmt;
	private @Getter @Setter String sukmAmt;
	private @Getter @Setter String smokRate;
	private @Getter @Setter String preSukmAmt;
	private @Getter @Setter String regDate;
}
