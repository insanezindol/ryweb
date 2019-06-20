package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class ScheduleInfo {
	private @Getter @Setter String rnum;
	private @Getter @Setter String scheduleSeq;
	private @Getter @Setter String scheduleType;
	private @Getter @Setter String scheduleTypeName;
	private @Getter @Setter String scheduleName;
	private @Getter @Setter String scheduleStarttime;
	private @Getter @Setter String scheduleEndtime;
	private @Getter @Setter String scheduleRemark;
	private @Getter @Setter String scheduleStatus;
	private @Getter @Setter String regSabun;
	private @Getter @Setter String regName;
	private @Getter @Setter String regDate;
	private @Getter @Setter String updSabun;
	private @Getter @Setter String updName;
	private @Getter @Setter String updDate;
	private @Getter @Setter String deptCode;
	private @Getter @Setter String deptName;
	private @Getter @Setter String attendantName;
	private @Getter @Setter long currseq;
}
