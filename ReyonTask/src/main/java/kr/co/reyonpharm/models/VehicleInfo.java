package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class VehicleInfo {
	private @Getter @Setter String rnum;
	private @Getter @Setter String vehicleSeq;
	private @Getter @Setter String vehicleNo;
	private @Getter @Setter String vehicleType;
	private @Getter @Setter String division;
	private @Getter @Setter String username;
	private @Getter @Setter String payment;
	private @Getter @Setter String rentStartDate;
	private @Getter @Setter String rentEndDate;
	private @Getter @Setter String rentMoney;
	private @Getter @Setter String insuranceStartDate;
	private @Getter @Setter String insuranceEndDate;
	private @Getter @Setter String insuranceMoney;
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
	
	private @Getter @Setter String limitDay;
	private @Getter @Setter String standardDate;
	private @Getter @Setter long currseq;
}
