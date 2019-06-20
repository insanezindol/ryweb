package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class OvertimeInfo {
	private @Getter @Setter String seq;
	private @Getter @Setter String overtimeSeq;
	private @Getter @Setter String overtimeGbn;
	private @Getter @Setter String startdate;
	private @Getter @Setter String enddate;
	private @Getter @Setter String restStarttime1;
	private @Getter @Setter String restEndtime1;
	private @Getter @Setter String restStarttime2;
	private @Getter @Setter String restEndtime2;
	private @Getter @Setter String restStarttime3;
	private @Getter @Setter String restEndtime3;
	private @Getter @Setter String workingMinute;
	private @Getter @Setter String remainMinute;
	private @Getter @Setter String reason;
	private @Getter @Setter String evidence;
	private @Getter @Setter String status;
	private @Getter @Setter String regSabun;
	private @Getter @Setter String regDate;
	private @Getter @Setter String updSabun;
	private @Getter @Setter String updDate;
	private @Getter @Setter String gwRegDate;
	private @Getter @Setter String gwUpdDate;
	private @Getter @Setter String gwStatus;
	private @Getter @Setter int orderSeq;
	
	private @Getter @Setter long currseq;
	private @Getter @Setter String yymmdd;
	private @Getter @Setter String sabun;
	private @Getter @Setter String kname;
	private @Getter @Setter String deptName;
	private @Getter @Setter String posLog;
	private @Getter @Setter String overtimeGbnTxt;
	private @Getter @Setter String gwStatusTxt;
	private @Getter @Setter String[] workerDeptNameArr;
	private @Getter @Setter String[] workerSabunArr;
	private @Getter @Setter String[] workerKnameArr;
	private @Getter @Setter String[] workerPosLogArr;

}

