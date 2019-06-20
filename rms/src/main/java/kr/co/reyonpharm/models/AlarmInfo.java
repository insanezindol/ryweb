package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class AlarmInfo {
	private @Getter @Setter String rnum;
	private @Getter @Setter String alarmSeq;
	private @Getter @Setter String alarmSabun;
	private @Getter @Setter String alarmName;
	private @Getter @Setter String alarmDate;
	private @Getter @Setter String alarmYn;
	private @Getter @Setter String alarmTitle;
	private @Getter @Setter String alarmContents;
	private @Getter @Setter String regDate;
	private @Getter @Setter String regSabun;
	private @Getter @Setter String regName;
	private @Getter @Setter String resultDate;
	private @Getter @Setter String resultMsg;
	private @Getter @Setter String tokenId;
	private @Getter @Setter String deviceType;
	private @Getter @Setter String msgReceiveType;
}
