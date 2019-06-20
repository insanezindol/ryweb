package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class GwApprovalInfo {
	private @Getter @Setter String approKey;
	private @Getter @Setter String userNum;
	private @Getter @Setter String approState;
	private @Getter @Setter String upDate;
	private @Getter @Setter String iseditable;
	private @Getter @Setter String endDate;
	private @Getter @Setter String fcode;
	private @Getter @Setter String subject;
	private @Getter @Setter String contents;
	private @Getter @Setter String saCode;
	private @Getter @Setter String approSerial;
	private @Getter @Setter String gwToMisStatus;
	
	private @Getter @Setter String gwStatus;
	private @Getter @Setter String tsYy;
	private @Getter @Setter String tsNo;
	private @Getter @Setter String reqDate;
	private @Getter @Setter String reqDeptCode;
	private @Getter @Setter String reqSeq;
	private @Getter @Setter String holidaySeq;
	private @Getter @Setter String overtimeSeq;
}
