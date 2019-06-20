package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class TakeOverInfo {
	private @Getter @Setter String rnum;
	private @Getter @Setter String level;
	private @Getter @Setter String giveSeq;
	private @Getter @Setter String giveSabun;
	private @Getter @Setter String giveName;
	private @Getter @Setter String receiveSabun;
	private @Getter @Setter String receiveName;
	private @Getter @Setter String giveDate;
	private @Getter @Setter String regDate;
	private @Getter @Setter String status;
}
