package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class PollInfo {
	private @Getter @Setter String voteSabun;
	private @Getter @Setter String voteDate;
	private @Getter @Setter String voteChoice;
	
	private @Getter @Setter String sabun;
	private @Getter @Setter String deptName;
	private @Getter @Setter String kname;
	private @Getter @Setter String posLog;
	private @Getter @Setter String cnt;
}
