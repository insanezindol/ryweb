package kr.co.reyonpharm.handler;

public enum CustomExceptionCodes {

	/**
	내부 시스템 에러
	**/
	// 파라메터 관련
	MISSING_PARAMETER (1001, "파라미터가 부족합니다."),
	INVALID_PARAMETER (1002, "파라미터가 적합하지 않습니다."),
	
	// 권한 관련
	NOT_AUTHORIZED (1101, "권한이 없습니다."),
	USER_NOT_EXIST (1102, "존재하지 않는 사용자 입니다."),
	INVALID_PASSWORD (1103, "ID 혹은 비밀번호가 올바르지 않습니다."),
	NOT_ALLOWED_IP (1104, "허용된 IP가 아닙니다. 정보관리팀으로 문의바랍니다."),
	
	// 세션 관련
	SESSION_EXPIRE (1201, "세션이 만료되었습니다."),
	CONCURRENT_SESSION_EXCEED (1201, "동시 접속으로 로그아웃 되었습니다."),
	SESSION_EXPIRE_FOR_POPUP (1202, "세션이 만료되었습니다."),
	
	// DB 관련
	INVALID_SEQUENCE (1301, "요청한 시퀀스에 맞는 정보가 없습니다."),
	NOT_AFFECTED (1302,"처리된 정보가 없습니다."),
	INVALID_RESERVATION (1303, "예약이 불가능 한 상태입니다.\n다시 확인하여 주세요."),
	IMPOSSIBLE_DELETE_PROJECT (1304, "관련 회의가 존재하여 삭제가 불가능합니다.\n관련 회의를 확인하여 주세요."),
	NOT_SMARTPHONE_USER (1305, "등록된 스마트폰 정보가 없습니다."),
	NOT_TOKENID_USER (1306, "등록된 토큰 정보가 없습니다."),
	NOT_MSG_RECEIVE_USER (1307, "알람 수신 OFF 상태 입니다."),
	ALREADY_VOTE (1308, "이미 투표에 참여하셨습니다."),
	
	// FILE 관련
	FILE_NOT_EXIST (1401, "파일이 존재하지 않습니다."),
	
	/**
	HTTP 에러
	**/
	// HTTP 에러
	ERROR_400 (4400, "잘못된 요청 입니다."),
	ERROR_401 (4401, "접근할 수 없는 페이지입니다."),
	ERROR_403 (4403, "세션이 만료되었거나 접근 권한이 없는 페이지입니다."),
	ERROR_404 (4404, "페이지를 찾을 수 없습니다."),
	ERROR_405 (4405, "잘못된 요청 입니다."),
	ERROR_406 (4406, "페이지를 표시할 수 없습니다."),
	ERROR_408 (4408, "페이지를 표시할 수 없습니다."),
	ERROR_409 (4409, "페이지를 표시할 수 없습니다."),
	ERROR_412 (4412, "페이지를 표시할 수 없습니다."),
	ERROR_500 (4500, "페이지를 표시할 수 없습니다."),
	ERROR_501 (4501, "페이지를 표시할 수 없습니다."),
	ERROR_502 (4502, "페이지를 표시할 수 없습니다."),
	ERROR_503 (4503, "페이지를 표시할 수 없습니다."),
	
	/* 알수 없는 오류*/
	SYSTEM_ERROR (9001, "시스템 오류가 발생했습니다.");
	
	private final int id;
	private final String msg;

	CustomExceptionCodes(int id, String msg) {
		this.id = id;
		this.msg = msg;
	}

	public int getId() {
		return this.id;
	}

	public String getMsg() {
		return this.msg;
	}
	
}
