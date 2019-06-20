package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class NoticeInfo {
	private @Getter @Setter String rnum;
	private @Getter @Setter String noticeSeq;
	private @Getter @Setter String title;
	private @Getter @Setter String contents;
	private @Getter @Setter String attachFilepath;
	private @Getter @Setter String attachFilename;
	private @Getter @Setter String attachFilesize;
	private @Getter @Setter String regUser;
	private @Getter @Setter String regName;
	private @Getter @Setter String regDate;
	private @Getter @Setter String updUser;
	private @Getter @Setter String updName;
	private @Getter @Setter String updDate;
	
	private @Getter @Setter long currseq;
}
