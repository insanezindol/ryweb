package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class ContractInfo {
	private @Getter @Setter String rnum;
	private @Getter @Setter String contractSeq;
	private @Getter @Setter String saupGubun;
	private @Getter @Setter String division;
	private @Getter @Setter String username;
	private @Getter @Setter String startDate;
	private @Getter @Setter String endDate;
	private @Getter @Setter String roadAddr;
	private @Getter @Setter String jibunAddr;
	private @Getter @Setter String detailAddr;
	private @Getter @Setter String sinm;
	private @Getter @Setter String zipno;
	private @Getter @Setter String payment;
	private @Getter @Setter String deposit;
	private @Getter @Setter String rent;
	private @Getter @Setter String administrativeExpenses;
	private @Getter @Setter String remarks;
	private @Getter @Setter String attachFilepath;
	private @Getter @Setter String attachFilename;
	private @Getter @Setter String attachFilesize;
	private @Getter @Setter String status;
	private @Getter @Setter String regSabun;
	private @Getter @Setter String regName;
	private @Getter @Setter String regDate;
	private @Getter @Setter String updSabun;
	private @Getter @Setter String updName;
	private @Getter @Setter String updDate;
	private @Getter @Setter String positionX;
	private @Getter @Setter String positionY;
	
	private @Getter @Setter String limitDay;
	private @Getter @Setter String standardDate;
	private @Getter @Setter String paidMoney;
	private @Getter @Setter long currseq;
}
