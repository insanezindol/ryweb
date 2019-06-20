package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class TokenInfo {
	private @Getter @Setter String rnum;
	private @Getter @Setter String sabun;
	private @Getter @Setter String tokenId;
	private @Getter @Setter String deviceType;
	private @Getter @Setter String msgReceiveType;
	private @Getter @Setter String regDate;
	private @Getter @Setter String updDate;
}
