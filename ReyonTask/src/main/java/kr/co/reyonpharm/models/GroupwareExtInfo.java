package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class GroupwareExtInfo {
	private @Getter @Setter String rnum;
	private @Getter @Setter String reqSeq;
	private @Getter @Setter String sabun;
	private @Getter @Setter String kname;
	private @Getter @Setter String deptName;
	private @Getter @Setter String startDate;
	private @Getter @Setter String endDate;
	private @Getter @Setter String accessIngType;
	private @Getter @Setter String accessEndType;
	private @Getter @Setter String reqComment;
	private @Getter @Setter String status;
}
