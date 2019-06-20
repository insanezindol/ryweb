package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class PcReportInfo {
	private @Getter @Setter String rnum;
	private @Getter @Setter String reportSeq;
	private @Getter @Setter String ip;
	private @Getter @Setter String username;
	private @Getter @Setter String reportDay;
	private @Getter @Setter String reportFirstDate;
	private @Getter @Setter String reportLastDate;
	private @Getter @Setter String kname;
}
