package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class PhoneInfo {
	private @Getter @Setter String rnum;
	private @Getter @Setter String deptName; 
	private @Getter @Setter String username; 
	private @Getter @Setter String kname;
	private @Getter @Setter String posLog;
	private @Getter String isSaveTmp;
	private @Getter @Setter Boolean isSave;
    private @Getter @Setter String phoneSeq;
    private @Getter @Setter String sabun;
    private @Getter @Setter String viewDept;
    private @Getter @Setter String viewPosi;
    private @Getter @Setter String viewName;
    private @Getter @Setter String phoneType;
    private @Getter @Setter String phoneType1;
    private @Getter @Setter String phoneType2;
    private @Getter @Setter String phoneType3;
    private @Getter @Setter String phonenum1;
    private @Getter @Setter String phonenum2;
    private @Getter @Setter String phonenum3;
    private @Getter @Setter String faxnum1;
    private @Getter @Setter String faxnum2;
    private @Getter @Setter String faxnum3;
    private @Getter @Setter String phonenum10;
    private @Getter @Setter String phonenum20;
    private @Getter @Setter String phonenum30;
    private @Getter @Setter String orderSeq1;
    private @Getter @Setter String orderSeq2;
    private @Getter @Setter String orderSeq3;
    private @Getter @Setter String regDate;
    private @Getter @Setter String regSabun;
    private @Getter @Setter String searchText;
    
	public void setIsSaveTmp(String isSaveTmp) {
		this.isSaveTmp = isSaveTmp;
		this.isSave = Boolean.valueOf(isSaveTmp);
	}
    
}
