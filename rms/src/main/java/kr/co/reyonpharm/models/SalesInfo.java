package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class SalesInfo {
	private @Getter @Setter String deptCd;
	private @Getter @Setter String deptNo;
    private @Getter @Setter String deptNm;
    private @Getter @Setter String empNo;
    private @Getter @Setter String empNm;
    private @Getter @Setter String visitSalesCnt;
    private @Getter @Setter String planSalesCnt;
    private @Getter @Setter String gubun1;
    private @Getter @Setter String standardDate;
    private @Getter @Setter String visitSeq;
	private @Getter @Setter String sfaSalesNo;
	private @Getter @Setter String sfaCustNm;
	private @Getter @Setter String startStatus;
	private @Getter @Setter String gpsStartNum1;
	private @Getter @Setter String gpsStartNum2;
	private @Getter @Setter String endStatus;
	private @Getter @Setter String gpsEndNum1;
	private @Getter @Setter String gpsEndNum2;
	private @Getter @Setter String actCd;
	private @Getter @Setter String gpsLatitude;
	private @Getter @Setter String gpsLongitude;
}
