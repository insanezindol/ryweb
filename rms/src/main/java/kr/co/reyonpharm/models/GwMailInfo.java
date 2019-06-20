package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class GwMailInfo {
	private @Getter @Setter String num;
	private @Getter @Setter String userNum;
	private @Getter @Setter String userId;
	private @Getter @Setter String mfrom;
	private @Getter @Setter String mfromName;
	private @Getter @Setter String mto;
	private @Getter @Setter String rcvDate;
	private @Getter @Setter String subject;
	private @Getter @Setter String contents;
	private @Getter @Setter String isdelete;
	private @Getter @Setter String issend;
	private @Getter @Setter String isattach;
	private @Getter @Setter String attachPath;
	private @Getter @Setter String uid;
	private @Getter @Setter String isbulk;
	private @Getter @Setter String letterNum;
	private @Getter @Setter String sysCd;
	private @Getter @Setter String pgmName;
	private @Getter @Setter String ipaddress;
	private @Getter @Setter String pcname;
	private @Getter @Setter String edpEmpNo;
	private @Getter @Setter String edpDate;
}
