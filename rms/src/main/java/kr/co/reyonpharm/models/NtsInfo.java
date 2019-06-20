package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class NtsInfo {
	private @Getter @Setter String yymm;
	private @Getter @Setter String payGb;
	private @Getter @Setter String sabun;
	private @Getter @Setter String itemKey;
	private @Getter @Setter String itemCd;
	private @Getter @Setter String itemData;
	private @Getter @Setter String edpEmpNo;
	private @Getter @Setter String edpDate;
	private @Getter @Setter int orderSeq;
	private @Getter @Setter String jumin;
	private @Getter @Setter int existCnt;
	private @Getter @Setter String gbn;
	
	private @Getter @Setter String kname;
	private @Getter @Setter String userNum;
	private @Getter @Setter String determinedTaxIncome;
	private @Getter @Setter String determinedTaxLocalIncome;
	private @Getter @Setter String determinedTaxSpecialRural;
	private @Getter @Setter String deductedTaxIncome;
	private @Getter @Setter String deductedTaxLocalIncome;
	private @Getter @Setter String deductedTax_SpecialRural;
	private @Getter @Setter String regDate;
}
