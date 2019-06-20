package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class LoginHistoryInfo {
	private @Getter @Setter String rnum;
	private @Getter @Setter String loginSeq;
	private @Getter @Setter String sessionId;
	private @Getter @Setter String username;
	private @Getter @Setter String ip;
	private @Getter @Setter String loginDate;
	private @Getter @Setter String logoutDate;
}
