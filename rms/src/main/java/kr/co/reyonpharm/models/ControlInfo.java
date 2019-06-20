package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class ControlInfo {
	/*
	 * RYAZZ03IT & RYAZZ02MT(재고자산마스터) & RYBEB02MT(원부자재마스터)
	 */
	private @Getter @Setter String reqno; //의뢰번호(PK)
	private @Getter @Setter String congbn; //통제구분(제품 || 원부자재)
	private @Getter @Setter String jpmnm; //품명
	private @Getter @Setter String gyugk; //규격
	private @Getter @Setter String insdate; //일자
	private @Getter @Setter String conyn; //상태(통제중||통제해제)
	private @Getter @Setter String insemp; //등록사번
	
	private @Getter @Setter String ryno; //원부자재코드번호
	private @Getter @Setter String jpmgbn; //원부자재구분
	private @Getter @Setter String condesc; //통제내용
	private @Getter @Setter String kname; //작업자 이름
	private @Getter @Setter String mailId; //메일 ID

	private @Getter @Setter String reqfilename; //요청사 파일
	private @Getter @Setter String bffilename; //기존 파일
	private @Getter @Setter String affilename; //변경된 파일
	private @Getter @Setter String filepath; //파일경로
	
	private @Getter @Setter String soldesc; //처리내용
	private @Getter @Setter String logemp; //작업자사번
	private @Getter @Setter String cfemp; //확인자
	private @Getter @Setter String logdate; //처리날짜
	private @Getter @Setter String cfdate; //확인날짜
	private @Getter @Setter String relemp; //통제 해제한 사번
	private @Getter @Setter String reldate; //통제 해제 날짜
	
	private @Getter @Setter String sabun; //사번
	private @Getter @Setter long currseq; //DB Insert 후 Sequence 받아옴 
}
