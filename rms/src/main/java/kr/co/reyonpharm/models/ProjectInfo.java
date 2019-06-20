package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class ProjectInfo {
	private @Getter @Setter String rnum;
	private @Getter @Setter String projectSeq;
	private @Getter @Setter String projectName;
	private @Getter @Setter String status;
	private @Getter @Setter String saupCode;
	private @Getter @Setter String deptCode;
	private @Getter @Setter String deptName;
	private @Getter @Setter String regSabun;
	private @Getter @Setter String regName;
	private @Getter @Setter String regDate;
	private @Getter @Setter String updSabun;
	private @Getter @Setter String updName;
	private @Getter @Setter String updDate;
	private @Getter @Setter String projectStartDate;
	private @Getter @Setter String projectEndDate;
	
	private @Getter @Setter long currseq;
}
