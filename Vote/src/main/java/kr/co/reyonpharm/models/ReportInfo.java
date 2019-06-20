package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class ReportInfo {
	private @Getter @Setter String voteSeq;
	private @Getter @Setter String voteRemarks;
	private @Getter @Setter String voteFilepath;
	private @Getter @Setter String voteFilename;
	private @Getter @Setter String regDate;
	private @Getter @Setter String realSeq;
	private @Getter @Setter String voteSum;
	private @Getter @Setter String voteAvg;
	private @Getter @Setter String cnt5;
	private @Getter @Setter String cnt4;
	private @Getter @Setter String cnt3;
	private @Getter @Setter String cnt2;
	private @Getter @Setter String cnt1;
}
