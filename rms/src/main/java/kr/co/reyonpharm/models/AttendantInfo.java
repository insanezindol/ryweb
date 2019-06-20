package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class AttendantInfo {
	private @Getter @Setter String rnum;
	private @Getter @Setter String meetingSeq;
	private @Getter @Setter String scheduleSeq;
	private @Getter @Setter String attendantSabun;
	private @Getter @Setter String attendantName;
	private @Getter @Setter String attendantType;
	private @Getter @Setter String attendantDept;
	private @Getter @Setter String attendantDeptName;
	private @Getter @Setter String regSabun;
	private @Getter @Setter String regDate;
	private @Getter @Setter String orderSeq;
	private @Getter @Setter String hiddenGb;
}
