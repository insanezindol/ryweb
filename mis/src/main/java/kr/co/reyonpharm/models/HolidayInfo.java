package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class HolidayInfo {
	private @Getter @Setter String seq;
	private @Getter @Setter String yymm;
	private @Getter @Setter String sabun;
	private @Getter @Setter String occurDay;
	private @Getter @Setter String addDay;
	private @Getter @Setter String addDayType;
	private @Getter @Setter String addDayComment;
	private @Getter @Setter String remainDay;
	private @Getter @Setter String useDay;
	private @Getter @Setter String status;
	private @Getter @Setter String regSabun;
	private @Getter @Setter String regDate;
	private @Getter @Setter String holidaySeq;
	private @Getter @Setter String holidayGbn;
	private @Getter @Setter String holidayGbnTxt;
	private @Getter @Setter String viewMinusCnt;
	private @Getter @Setter String holidayTotalCnt;
	private @Getter @Setter String startdate;
	private @Getter @Setter String enddate;
	private @Getter @Setter String reason;
	private @Getter @Setter String takeover;
	private @Getter @Setter String updSabun;
	private @Getter @Setter String updDate;
	private @Getter @Setter String gwRegDate;
	private @Getter @Setter String gwUpdDate;
	private @Getter @Setter String gwStatus;
	private @Getter @Setter String gwStatusTxt;
	private @Getter @Setter String kname;
	private @Getter @Setter String deptName;
	private @Getter @Setter double minusCnt;
	private @Getter @Setter String minusYn;
	private @Getter @Setter String deptCode;
	private @Getter @Setter String posLog;
	private @Getter @Setter String dayPlus;
	private @Getter @Setter String dayMinus;
	private @Getter @Setter String dayTotal;
	private @Getter @Setter String addDayTypeTxt;
	private @Getter @Setter String regKname;
	
	private @Getter @Setter long currseq;
}

