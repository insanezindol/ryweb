package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class PcMessageInfo {
	private @Getter @Setter String rnum;
	private @Getter @Setter String messageSeq;
	private @Getter @Setter String username;
	private @Getter @Setter String contents;
	private @Getter @Setter String regDate;
	private @Getter @Setter String sendDate;
	private @Getter @Setter String receiveDate;
	private @Getter @Setter String messageYn;
	private @Getter @Setter String kname;
}
