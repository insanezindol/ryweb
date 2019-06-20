package kr.co.reyonpharm.handler;

public class CustomException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private int errorCode; // 에러 코드
	private String errorMsg; // 에러 메시지

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	// CustomExceptionCodes를 이용한 에러코드/에러메시지 처리
	public CustomException(CustomExceptionCodes code) {
		super(code.getMsg());

		this.errorCode = code.getId();
		this.errorMsg = code.getMsg();
	}

	// 특별한 경우 직접 입력한 에러코드/에러메시지 처리
	public CustomException(int errorCode, String errorMsg) {
		super(errorMsg);

		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

}
