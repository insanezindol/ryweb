package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class UserInfo {
	private @Getter @Setter String rnum;
	private @Getter @Setter String username;
	private @Getter @Setter String password;
	private @Getter @Setter String saupName;
	private @Getter @Setter String userRole;
	private @Getter @Setter String useYn;
	private @Getter @Setter String regDate;
	private @Getter @Setter String updDate;
}
