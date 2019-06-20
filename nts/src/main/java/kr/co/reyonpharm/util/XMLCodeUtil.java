package kr.co.reyonpharm.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XMLCodeUtil {
	
	public static String CD_A101Y = "A101Y"; // 공제신고서
	public static String CD_B101Y = "B101Y"; // 연금저축등 소득.세액 공제명세
	public static String CD_C101Y = "C101Y"; // 월세액.거주자간 주택임차차입금 상환액
	public static String CD_D101Y = "D101Y"; // 의료비 지급명세
	public static String CD_E101Y = "E101Y"; // 기부금 명세
	public static String CD_F101Y = "F101Y"; // 신용카드등 소득공제신청서
    
	public static String NM_A101Y = "공제신고서";
	public static String NM_B101Y = "연금저축등 소득.세액 공제명세";
	public static String NM_C101Y = "월세액.거주자간 주택임차차입금 상환액";
	public static String NM_D101Y = "의료비 지급명세";
	public static String NM_E101Y = "기부금 명세";
	public static String NM_F101Y = "신용카드등 소득공제신청서";
	
	// 세대주여부
	public static String hshrClCdToName(String cd) {
		String value = "";
		if (cd.equals("1")) {
			value = "세대주";
		} else if (cd.equals("2")) {
			value = "세대원";
		}
		return value;
	}
	
	// 거주구분
	public static String rsdtClCdToName(String cd) {
		String value = "";
		if (cd.equals("1")) {
			value = "거주자";
		} else if (cd.equals("2")) {
			value = "비거주자";
		}
		return value;
	}
	
	// 소득세 원천징수세액 조정신청
	public static String inctxWhtxTxamtMetnCdToName(String cd) {
		String value = "";
		if (cd.equals("01")) {
			value = "120%";
		} else if (cd.equals("02")) {
			value = "100%";
		} else if (cd.equals("03")) {
			value = "80%";
		}
		return value;
	}
	
	// 분납신청 여부
	public static String inpmYnToName(String cd) {
		String value = "";
		if (cd.equals("Y")) {
			value = "신청";
		} else if (cd.equals("N")) {
			value = "미신청";
		}
		return value;
	}
	
	// 인적공제 항목 변동여부
	public static String prifChngYnToName(String cd) {
		String value = "";
		if (cd.equals("Y")) {
			value = "변동";
		} else if (cd.equals("N")) {
			value = "전년과동일";
		}
		return value;
	}
	
	// 인적공제 항목 변동여부
	public static String suptFmlyRltClCdToName(String cd) {
		String value = "";
		if (cd.equals("0")) {
			value = "소득자 본인";
		} else if (cd.equals("1")) {
			value = "소득자의 직계존속";
		} else if (cd.equals("2")) {
			value = "배우자의 직계존속";
		} else if (cd.equals("3")) {
			value = "배우자";
		} else if (cd.equals("4")) {
			value = "직계비속(자녀,입양자)";
		} else if (cd.equals("5")) {
			value = "직계비속(자녀,입양자 제외)";
		} else if (cd.equals("6")) {
			value = "형제자매";
		} else if (cd.equals("7")) {
			value = "수급자";
		} else if (cd.equals("8")) {
			value = "위탁아동";
		}
		return value;
	}

	// 외국인근로자입국목적코드
	public static String frgrLbrrEntcPupCdToName(String cd) {
		String value = "";
		if (cd.equals("01")) {
			value = "정부간협약";
		} else if (cd.equals("02")) {
			value = "기술도입계약";
		} else if (cd.equals("03")) {
			value = "조특법상감면";
		} else if (cd.equals("04")) {
			value = "조세제약상감면";
		} else if (cd.equals("ZZ")) {
			value = "해당없음";
		}
		return value;
	}
	
}