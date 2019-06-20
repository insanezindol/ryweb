package kr.co.reyonpharm.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class MeetingInfo {
	private @Getter @Setter String rnum;
	private @Getter @Setter String meetingSeq;
	private @Getter @Setter String projectSeq;
	private @Getter @Setter String projectName;
	private @Getter @Setter String meetingName;
	private @Getter @Setter String meetingStartDate;
	private @Getter @Setter String meetingEndDate;
	private @Getter @Setter String meetingPlace;
	private @Getter @Setter String meetingType;
	private @Getter @Setter String meetingStatus;
	private @Getter @Setter String saupCode;
	private @Getter @Setter String deptCode;
	private @Getter @Setter String deptName;
	private @Getter @Setter String regSabun;
	private @Getter @Setter String regName;
	private @Getter @Setter String meetingRegDate;
	private @Getter @Setter String updSabun;
	private @Getter @Setter String updName;
	private @Getter @Setter String meetingUpdDate;
	private @Getter @Setter String visitCompany;
	private @Getter @Setter String visitName;
	private @Getter @Setter String meetingContents;
	private @Getter @Setter String decisionContents;
	private @Getter @Setter String planContents;
	private @Getter @Setter String issueContents;
	private @Getter @Setter String meetingResultRegDate;
	private @Getter @Setter String meetingResultUpdDate;
	private @Getter @Setter String sactionSabun;
	private @Getter @Setter String sactionName;
	private @Getter @Setter String sactionDate;
	private @Getter @Setter String sactionComment;
	private @Getter @Setter String confirmSabun;
	private @Getter @Setter String confirmName;
	private @Getter @Setter String confirmDate;
	private @Getter @Setter String confirmComment;
	private @Getter @Setter String returnSabun;
	private @Getter @Setter String returnName;
	private @Getter @Setter String returnDate;
	private @Getter @Setter String returnComment;
	private @Getter @Setter String attachFilepath1;
	private @Getter @Setter String attachFilename1;
	private @Getter @Setter String attachFilesize1;
	private @Getter @Setter String attachFilepath2;
	private @Getter @Setter String attachFilename2;
	private @Getter @Setter String attachFilesize2;
	private @Getter @Setter String attachFilepath3;
	private @Getter @Setter String attachFilename3;
	private @Getter @Setter String attachFilesize3;
	private @Getter @Setter String attachFilepath4;
	private @Getter @Setter String attachFilename4;
	private @Getter @Setter String attachFilesize4;
	private @Getter @Setter String attachFilepath5;
	private @Getter @Setter String attachFilename5;
	private @Getter @Setter String attachFilesize5;
	private @Getter @Setter String codeName;

	private @Getter @Setter long currseq;
	private @Getter @Setter String attFileType;
	private @Getter @Setter List<String> deptCodeList; // 부서 코드 리스트
	private @Getter @Setter String attSabun; // 참석자, 참고인 사번
	private @Getter @Setter String cnt;
	private @Getter @Setter List<String> sabunList; // 사번 리스트
	private @Getter @Setter List<String> attSabunList; // 참석자, 참고인 사번 리스트
}