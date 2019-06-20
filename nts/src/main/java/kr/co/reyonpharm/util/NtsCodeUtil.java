package kr.co.reyonpharm.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NtsCodeUtil {
	
	public static String CD_A102Y = "A102Y"; // 보장성 보험, 장애인전용보장성보험
	public static String CD_B101Y = "B101Y"; // 의료비
	public static String CD_C101Y = "C101Y"; // 교육비
	public static String CD_C102Y = "C102Y"; // 교육비
	public static String CD_C202Y = "C202Y"; // 직업훈련비
	public static String CD_C301Y = "C301Y"; // 교복구입비
	public static String CD_C401Y = "C401Y"; // 학자금대출상환액
	public static String CD_D101Y = "D101Y"; // 개인연금저축
	public static String CD_E102Y = "E102Y"; // 연금저축
	public static String CD_F102Y = "F102Y"; // 퇴직연금
	public static String CD_G107Y = "G107Y"; // 신용카드
	public static String CD_G207M = "G207M"; // 현금영수증
	public static String CD_G307Y = "G307Y"; // 직불카드등
	public static String CD_J101Y = "J101Y"; // 주택임차차입금 원리금상환액
	public static String CD_J203Y = "J203Y"; // 장기주택저당차입금 이자상환액
	public static String CD_J301Y = "J301Y"; // 주택마련저축
	public static String CD_J401Y = "J401Y"; // 목돈 안드는 전세 이자상환액
	public static String CD_K101M = "K101M"; // 소기업소상공인 공제부금
	public static String CD_L102Y = "L102Y"; // 기부금
	public static String CD_N101Y = "N101Y"; // 장기집합투자증권저축
	public static String CD_M101Y = "M101Y"; // 장기주식형저축 : 2013년 서비스 종료(삭제)
	public static String CD_O101M = "O101M"; // 건강보험료
	public static String CD_P101M = "P101M"; // 국민연금보험료
	public static String CD_P102M = "P102M"; // 국민연금보험료
    
	public static String NM_A102Y = "보장성 보험, 장애인전용보장성보험";
	public static String NM_B101Y = "의료비";
	public static String NM_C101Y = "교육비";
	public static String NM_C102Y = "교육비";
	public static String NM_C202Y = "직업훈련비";
	public static String NM_C301Y = "교복구입비";
	public static String NM_C401Y = "학자금대출상환액";
	public static String NM_D101Y = "개인연금저축";
	public static String NM_E102Y = "연금저축";
	public static String NM_F102Y = "퇴직연금";
	public static String NM_G107Y = "신용카드";
	public static String NM_G207M = "현금영수증";
	public static String NM_G307Y = "직불카드등";
	public static String NM_J101Y = "주택임차차입금 원리금상환액";
	public static String NM_J203Y = "장기주택저당차입금 이자상환액";
	public static String NM_J301Y = "주택마련저축";
	public static String NM_J401Y = "목돈 안드는 전세 이자상환액";
	public static String NM_K101M = "소기업소상공인 공제부금";
	public static String NM_L102Y = "기부금";
	public static String NM_N101Y = "장기집합투자증권저축";
	public static String NM_M101Y = "장기주식형저축 : 2013년 서비스 종료(삭제)";
	public static String NM_O101M = "건강보험료";
	public static String NM_P101M = "국민연금보험료";
	public static String NM_P102M = "국민연금보험료";
	
	// 보장성 보험, 장애인전용보장성보험 자료코드를 보장성 보험, 장애인전용보장성보험 항목으로 변환해주는 함수
	public static String GETA102YNM(String dat_cd) {
		String value = "";
		if (dat_cd.equals("G0001")) {
			value = "보장성";
		} else if (dat_cd.equals("G0002")) {
			value = "장애인보장성";
		}
		return value;
	}
	
	// 의료비 자료코드를 의료비 항목으로 변환해주는 함수
	public static String GETB101YNM(String dat_cd) {
		String value = "";
		if (dat_cd.equals("G0003")) {
			value = "의료비";
		} else if (dat_cd.equals("G0025")) {
			value = "의료기기 구입비용";
		} else if (dat_cd.equals("G0026")) {
			value = "안경 또는 콘택트렌즈 구입비용";
		} else if (dat_cd.equals("G0027")) {
			value = "보청기 또는 장애인 보장구 구입비용";
		}
		return value;
	}
	
	// 교육비 자료코드를 교육비 항목으로 변환해주는 함수
	public static String GETC102YNM1(String dat_cd) {
		String value = "";
		if (dat_cd.equals("G0004")) {
			value = "유초중고(시도교육청)";
		} else if (dat_cd.equals("G0006")) {
			value = "대학교";
		} else if (dat_cd.equals("G0007")) {
			value = "기타(개별제출)";
		} else if (dat_cd.equals("G0008")) {
			value = "유초중고(개별제출)";
		}
		return value;
	}
	
	// 교육비 교육비종류를 교육비 항목으로 변환해주는 함수
	public static String GETC102YNM2(String dat_cd) {
		String value = "";
		if (dat_cd.equals("1")) {
			value = "유치원";
		} else if (dat_cd.equals("2")) {
			value = "초등학교";
		} else if (dat_cd.equals("3")) {
			value = "중학교";
		} else if (dat_cd.equals("4")) {
			value = "고등학교";
		} else if (dat_cd.equals("5")) {
			value = "전문대학";
		} else if (dat_cd.equals("6")) {
			value = "대학교";
		} else if (dat_cd.equals("7")) {
			value = "대학원";
		} else if (dat_cd.equals("8")) {
			value = "보육시설";
		} else if (dat_cd.equals("9")) {
			value = "기타";
		} else if (dat_cd.equals("C")) {
			value = "원격대학";
		} else if (dat_cd.equals("D")) {
			value = "학위취득과정";
		} else if (dat_cd.equals("E")) {
			value = "시간제과정";
		} else if (dat_cd.equals("F")) {
			value = "학원";
		} else if (dat_cd.equals("G")) {
			value = "체육시설";
		} else if (dat_cd.equals("H")) {
			value = "사회복지시설";
		} else if (dat_cd.equals("J")) {
			value = "장애인재활교육기관";
		} else if (dat_cd.equals("K")) {
			value = "발달재활서비스제공기관";
		} else if (dat_cd.equals("L")) {
			value = "학점은행제";
		}
		return value;
	}
	
	// 교육비 교육비구분를 교육비 항목으로 변환해주는 함수
	public static String GETC102YNM3(String dat_cd) {
		String value = "";
		if (dat_cd.equals("A")) {
			value = "일반교육비";
		} else if (dat_cd.equals("B")) {
			value = "현장체험학습비";
		}
		return value;
	}
	
	// 신용카드 종류를 신용카드 항목으로 변환해주는 함수
	public static String GETG107YNM(String dat_cd) {
		String value = "";
		if (dat_cd.equals("1")) {
			value = "일반";
		} else if (dat_cd.equals("2")) {
			value = "전통시장";
		} else if (dat_cd.equals("3")) {
			value = "대중교통";
		} else if (dat_cd.equals("4")) {
			value = "도서공연비";
		}
		return value;
	}
	
	// 장기주택저당차입금 이자상환액 종류를 대출종류 항목으로 변환해주는 함수
	public static String GETJ203YNM(String dat_cd) {
		String value = "";
		if (dat_cd.equals("1")) {
			value = "무주택자의 중도금 대출";
		} else if (dat_cd.equals("2")) {
			value = "기존주택구입";
		} else if (dat_cd.equals("3")) {
			value = "주택분양권 대출";
		}
		return value;
	}
	
	// 주택마련저축 종류를 저축구분 항목으로 변환해주는 함수
	public static String GETJ301YNM(String dat_cd) {
		String value = "";
		if (dat_cd.equals("1")) {
			value = "청약저축";
		} else if (dat_cd.equals("2")) {
			value = "주택청약종합저축";
		} else if (dat_cd.equals("4")) {
			value = "근로자주택마련저축";
		}
		return value;
	}
	
	// 퇴직연금 종류를 계좌유형 항목으로 변환해주는 함수
	public static String GETF102YNM(String dat_cd) {
		String value = "";
		if (dat_cd.equals("11")) {
			value = "근로자퇴직급여보장법";
		} else if (dat_cd.equals("12")) {
			value = "과학기술인공제";
		}
		return value;
	}
	
	// 소기업소상공인 공제부금 종류를 납입방법 항목으로 변환해주는 함수
	public static String GETK101MNM(String dat_cd) {
		String value = "";
		if (dat_cd.equals("M")) {
			value = "월납";
		} else if (dat_cd.equals("Q")) {
			value = "분기납";
		}
		return value;
	}
	
	// 기부금 종류를 기부유형 항목으로 변환해주는 함수
	public static String GETL102YNM(String dat_cd) {
		String value = "";
		if (dat_cd.equals("10")) {
			value = "법정기부금";
		} else if (dat_cd.equals("20")) {
			value = "정치자금기부금";
		} else if (dat_cd.equals("40")) {
			value = "지정기부금";
		} else if (dat_cd.equals("41")) {
			value = "종교단체기부금";
		} else if (dat_cd.equals("42")) {
			value = "우리사주기부금";
		}
		return value;
	}
	
}