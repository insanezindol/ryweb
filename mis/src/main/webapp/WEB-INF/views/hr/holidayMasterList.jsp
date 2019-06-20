<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<meta name="theme-color" content="#2196F3">
	<title>REYON WEB SERVICE</title>
	<link rel="stylesheet" href="/plugins/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" href="/plugins/bootstrap-select/css/bootstrap-select.css">
	<link rel="stylesheet" href="/plugins/node-waves/waves.css">
	<link rel="stylesheet" href="/plugins/animate-css/animate.css">
	<link rel="stylesheet" href="/plugins/morrisjs/morris.css">
	<link rel="stylesheet" href="/plugins/jquery-spinner/css/bootstrap-spinner.css">
	<link rel="stylesheet" href="/css/style.css">
	<link rel="stylesheet" href="/css/themes/all-themes.css">
	<!-- Google Css -->
	<link href="https://fonts.googleapis.com/css?family=Lato:400,300,300italic,400italic,600,600italic,700,700italic,800,800italic" rel="stylesheet">
	<link href="https://fonts.googleapis.com/earlyaccess/nanumgothic.css" rel="stylesheet">
	<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
	<!-- Global site tag (gtag.js) - Google Analytics -->
	<script async src="https://www.googletagmanager.com/gtag/js?id=UA-116158631-2"></script>
	<script>
	  window.dataLayer = window.dataLayer || [];
	  function gtag(){dataLayer.push(arguments);}
	  gtag('js', new Date());
	
	  gtag('config', 'UA-116158631-2');
	</script>
	<!-- Jquery Core Js -->
	<script src="/plugins/jquery/jquery.min.js"></script>
	<!-- Bootstrap Core Js -->
	<script src="/plugins/bootstrap/js/bootstrap.js"></script>
	<!-- Select Plugin Js -->
	<script src="/plugins/bootstrap-select/js/bootstrap-select.js"></script>
	<script src="/plugins/bootstrap-select/js/i18n/defaults-ko_KR.min.js"></script>
	<!-- Slimscroll Plugin Js -->
	<script src="/plugins/jquery-slimscroll/jquery.slimscroll.js"></script>
	<!-- Waves Effect Plugin Js -->
	<script src="/plugins/node-waves/waves.js"></script>
	<!-- Jquery CountTo Plugin Js -->
	<script src="/plugins/jquery-countto/jquery.countTo.js"></script>
	<!-- Morris Plugin Js -->
	<script src="/plugins/raphael/raphael.min.js"></script>
	<script src="/plugins/morrisjs/morris.js"></script>
	<!-- Spinner Js -->
	<script src="/plugins/jquery-spinner/js/jquery.spinner.js"></script>
	<!-- Custom Js -->
	<script src="/js/admin.js"></script>
	<!-- excel parser -->
	<script type="text/javascript" src="/js/shim.js"></script>
	<script type="text/javascript" src="/js/jszip.js"></script>
	<script type="text/javascript" src="/js/xlsx.js"></script>
	<script type="text/javascript" src="/js/ods.js"></script>
	<!-- common function -->
	<script type="text/javascript" src="/js/common.js"></script>
	<script type="text/javascript">
	
		var EXCEL_DATA = new Array(); // 엑셀을 파싱하여 나온 데이터
		var X = XLSX; // 엑셀 파싱에서 사용되는 변수
		var SHEET_NAME = "1"; // 파싱할 엑셀파일 내 시트 이름
		var EXCEL_STATUS = 0; // 엑셀 업로드 진행 상태
		var COMPLETE_CNT = 0; // 엑셀 업로드 성공 건수
	
		$(function() {
			// 진입 시 메뉴 선택
			cfLNBMenuSelect("lnbHr");
			cfLNBChildMenuSelect("lnbHolidayMasterList");
			
			// 조회년도 불러오기
			getHolidayYYMM();
			
			// 엔터키 바인드
			$("#deptName").keydown(function (key) {
			    if (key.keyCode == 13) goSearch();
			});
			$("#kname").keydown(function (key) {
			    if (key.keyCode == 13) goSearch();
			});
			
			// 엑셀선택 버튼 이벤트 리스너
			$("#xlfile").change(function(e) {
				setTimeout( function() {
					try {
						if( $("#xlfile").val() != '' ) {
							var files = e.target.files;
							var f = files[0];
							var reader = new FileReader();
							var name = f.name;
							reader.onload = function(e) {
								var data = e.target.result;
								var arr = fixdata(data);
								wb = X.read(btoa(arr), {
									type : 'base64'
								});

								// csv 파싱
								var workbook = exceltocsv(wb);
								parseExcelFile(workbook);
							};
							reader.readAsArrayBuffer(f);
						} else {
							EXCEL_STATUS = 0;
						}
					} catch(err) {
						console.log(err.message);
					}
		        }, 500);
			});
		});
		
		// 엑셀 파싱에 사용되는 함수
		function fixdata(data) {
			var o = "", l = 0, w = 10240;
			for (; l < data.byteLength / w; ++l)
				o += String.fromCharCode.apply(null, new Uint8Array(data.slice(l * w, l * w + w)));
			o += String.fromCharCode.apply(null, new Uint8Array(data.slice(l * w)));
			return o;
		}
		
		// 엑셀을 파싱하여 csv형태로 변환하는 함수
		function exceltocsv(workbook) {
			var result = [];
			workbook.SheetNames.forEach(function(sheetName) {
				if (sheetName == SHEET_NAME) {
					var csv = X.utils.sheet_to_csv(workbook.Sheets[sheetName]);
					if(csv.length > 0){
						result.push(csv);
					}
				}
			});
			return result.join("\n");
		}
		
		// 엑셀파일 파싱
		function parseExcelFile(workbook) {
			try {
				EXCEL_DATA = new Array();
				
				var rowCnt = 0;
				var rows = workbook.split('&&');
				for (var i = 0; i < rows.length; i++) {
					// LID 셀의 값이 제목이면 skip
					if( i != 0){
						var cells = rows[i].split('||');
						
						// LID 셀의 값이 비어있으면 end
						if( typeof cells[0] == "undefined" || cells[0] == "" ){
							break;
						}
						
						var yymm = cells[0];
						var sabun = cells[1];
						var addDay = cells[2];
						var addDayType = cells[3];
						var addDayComment = cells[4];
						
						excelLogging(yymm + "," + sabun + "," + addDay + "," + addDayType + "," + addDayComment);
						
						var obj = {
								yymm : yymm,
								sabun : sabun,
								addDay : addDay,
								addDayType : addDayType,
								addDayComment : addDayComment,
						}
						EXCEL_DATA[rowCnt++] = obj;
					}
				}
				excelLogging("엑셀 불러오기 완료");
				EXCEL_STATUS = 1;
			} catch(error) {
			    excelLogging("엑셀 불러오기 에러 : " + error.message);
			}
		}
		
		// 조회년도 불러오기
		function getHolidayYYMM() {
			var params = {
				auth : "reyon",
				type : "ALL"
			}
			
			var request = $.ajax({
				url: '/hr/getHolidayYYMMAjax.json'
				, type : 'POST'
				, timeout: 60000
				, data : params
				, dataType : 'json'
				, beforeSend: function(xmlHttpRequest) {
					cfOpenMagnificPopup();
				}
				, error: function(xhr, textStatus, errorThrown) {
					alert("시스템 오류가 발생했습니다.");
				}
				, success : function(json) {
					if (json.resultCode == 0){
						$("#yymm").empty();
						var list = json.list;
						var htmlStr = '';
						for(var i=0; i<list.length; i++){
							var yymm = list[i].yymm;
							htmlStr += '<option value="'+yymm+'">'+yymm+'년</option>';
						}
						$("#yymm").append(htmlStr);
						$('#yymm').selectpicker('refresh');
					}else if (json.resultCode == 1201) {
						alert(json.resultMsg);
						cfLogin();
					}else{
						alert(json.resultMsg);
					}
				}
				, complete : function() {
					cfCloseMagnificPopup();
				}
			});
		}
		
		// 조회
		function goSearch() {
			var yymm = $("#yymm").val();
			var deptName = $("#deptName").val();
			var kname = $("#kname").val();
			
			if (yymm == "") {
				alert("조회년도를 선택해 주세요.");
				return;
			}
			
			$("#resultDiv").hide();
			var params = {
				auth : "reyon",
				yymm : yymm,
				deptName : deptName,
				kname : kname
			}
			
			var request = $.ajax({
				url: '/hr/getHolidayMasterListAjax.json'
				, type : 'POST'
				, timeout: 60000
				, data : params
				, dataType : 'json'
				, beforeSend: function(xmlHttpRequest) {
					cfOpenMagnificPopup();
				}
				, error: function(xhr, textStatus, errorThrown) {
					alert("시스템 오류가 발생했습니다.");
				}
				, success : function(json) {
					if (json.resultCode == 0){
						var info = json.info;
						var list = json.list;
						$("#resultTbody").empty();
						$("#resultTitle").text("직원 연차 정보 (" + yymm + "년)");
						
						var htmlStr = '';
						htmlStr += '<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 0px;">';
						htmlStr += '<table class="table table-bordered" style="min-width: 830px; margin-bottom: 10px;">';
						htmlStr += '<colgroup>';
						htmlStr += '<col style="width: 10%;">';
						htmlStr += '<col style="width: 20%;">';
						htmlStr += '<col style="width: 10%;">';
						htmlStr += '<col style="width: 10%;">';
						htmlStr += '<col style="width: 10%;">';
						htmlStr += '<col style="width: 10%;">';
						htmlStr += '<col style="width: 10%;">';
						htmlStr += '<col style="width: 10%;">';
						htmlStr += '<col style="width: 10%;">';
						htmlStr += '</colgroup>';
						htmlStr += '<tbody>';
						htmlStr += '<tr>';
						htmlStr += '<th class="bg-indigo align-center">순번</th>';
						htmlStr += '<th class="bg-indigo align-center">소속</th>';
						htmlStr += '<th class="bg-indigo align-center">사번</th>';
						htmlStr += '<th class="bg-indigo align-center">이름</th>';
						htmlStr += '<th class="bg-indigo align-center">직위</th>';
						htmlStr += '<th class="bg-indigo align-center">(+)연차</th>';
						htmlStr += '<th class="bg-indigo align-center">(-)연차</th>';
						htmlStr += '<th class="bg-indigo align-center">증감계</th>';
						htmlStr += '<th class="bg-indigo align-center">메뉴</th>';
						htmlStr += '</tr>';
						
						if (list.length == 0) {
							htmlStr += '<tr>';
							htmlStr += '<td class="align-center" colspan="9">조회 내용이 없습니다.</td>';
							htmlStr += '</tr>';
						} else {
							for(var i=0; i<list.length; i++){
								htmlStr += '<tr>';
								htmlStr += '<td class="align-center">'+(i+1)+'</td>';
								htmlStr += '<td class="align-center">'+list[i].deptName+'</td>';
								htmlStr += '<td class="align-center">'+list[i].sabun+'</td>';
								htmlStr += '<td class="align-center">'+list[i].kname+'</td>';
								htmlStr += '<td class="align-center">'+list[i].posLog+'</td>';
								htmlStr += '<td class="align-center">'+list[i].dayPlus+'</td>';
								htmlStr += '<td class="align-center">'+list[i].dayMinus+'</td>';
								htmlStr += '<td class="align-center">'+list[i].dayTotal+'</td>';
								htmlStr += '<td class="align-center">';
								htmlStr += '<button type="button" class="btn bg-blue waves-effect btn-xs waves-effect" onClick="javascript:plusMinusHoliday(\''+list[i].sabun+'\',\''+list[i].kname+'\',\''+list[i].deptName+'\',\''+list[i].posLog+'\',\''+list[i].dayPlus+'\',\''+list[i].dayMinus+'\',\''+list[i].dayTotal+'\',\'1\');"><i class="material-icons">add_circle_outline</i></button>';
								htmlStr += '<button type="button" class="btn bg-red waves-effect btn-xs waves-effect" onClick="javascript:plusMinusHoliday(\''+list[i].sabun+'\',\''+list[i].kname+'\',\''+list[i].deptName+'\',\''+list[i].posLog+'\',\''+list[i].dayPlus+'\',\''+list[i].dayMinus+'\',\''+list[i].dayTotal+'\',\'2\');"><i class="material-icons">remove_circle_outline</i></button>';
								htmlStr += '<button type="button" class="btn bg-green waves-effect btn-xs waves-effect" onClick="javascript:historyHoliday(\''+list[i].sabun+'\',\''+list[i].kname+'\',\''+list[i].deptName+'\',\''+list[i].posLog+'\',\''+list[i].dayPlus+'\',\''+list[i].dayMinus+'\',\''+list[i].dayTotal+'\');"><i class="material-icons">history</i></button>';
								htmlStr += '</td>';
								htmlStr += '</tr>';
							}
						}
						
						htmlStr += '</tbody>';
						htmlStr += '</table>';
						htmlStr += '</div>';
						
						$("#resultTbody").append(htmlStr);
						$("#resultDiv").show();
					}else if (json.resultCode == 1201) {
						alert(json.resultMsg);
						cfLogin();
					}else{
						alert(json.resultMsg);
					}
				}
				, complete : function() {
					cfCloseMagnificPopup();
				}
			});
		}
		
		// 휴가 증가/감소
		function plusMinusHoliday(sabun, kname, deptName, posLog, dayPlus, dayMinus, dayTotal, addDayType){
			$("#sabun").val(sabun);
			$("#sabunTxt").text(sabun);
			$("#knameTxt").text(kname);
			$("#deptNameTxt").text(deptName);
			$("#posLogTxt").text(posLog);
			$("#plusHolidayTxt").text(dayPlus);
			$("#minusHolidayTxt").text(dayMinus);
			$("#totalHolidayTxt").text(dayTotal);
			
			$("#addDayType").val(addDayType);
			$("#addDayType").selectpicker('refresh');
			$("#addDay").val("1");
			$("#addDayComment").val("");
			
			$("#plusMinusModal").modal("show");
		}
		
		// 휴가 증가/감소 액션
		function addHoliday(){
			var yymm = $("#yymm").val();
			var sabun = $("#sabun").val();
			var addDayType = $("#addDayType").val();
			var addDay = $("#addDay").val();
			var addDayComment = $("#addDayComment").val();
			
			if (yymm == "") {
				alert("조회년도를 선택해 주세요.");
				return;
			}
			if (sabun == "") {
				alert("직원을 선택해 주세요.");
				return;
			}
			if (addDayType == "") {
				alert("증가/차감을 선택해 주세요.");
				return;
			}
			if (addDay == "") {
				alert("일수를 입력해 주세요.");
				return;
			}
			if (addDayComment == "") {
				alert("사유를 입력해 주세요.");
				return;
			}
			
			if(confirm("저장하시겠습니까?")){
				var params = {
					auth : "reyon",
					yymm : yymm,
					sabun : sabun,
					addDayType : addDayType,
					addDay : addDay,
					addDayComment : addDayComment,
				}
				
				var request = $.ajax({
					url: '/hr/holidayMasterAddAjax.json'
					, type : 'POST'
					, timeout: 30000
					, data : params
					, dataType : 'json'
					, beforeSend: function(xmlHttpRequest) {
						cfOpenMagnificPopup();
					}
					, error: function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					}
					, success : function(json) {
						if (json.resultCode == 1){
							alert("저장 완료 되었습니다.");
							goSearch();
							$("#plusMinusModal").modal("hide");
						}else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
						}else{
							alert(json.resultMsg);
						}
					}
					, complete : function() {
						cfCloseMagnificPopup();
					}
				});
			}
		}
		
		// 휴가 증감 내역
		function historyHoliday(sabun, kname, deptName, posLog, dayPlus, dayMinus, dayTotal){
			var yymm = $("#yymm").val();
			
			$("#sabunTxt1").text(sabun);
			$("#knameTxt1").text(kname);
			$("#deptNameTxt1").text(deptName);
			$("#posLogTxt1").text(posLog);
			
			var params = {
				auth : "reyon",
				yymm : yymm,
				sabun : sabun,
			}
			
			var request = $.ajax({
				url: '/hr/getHolidayMasterDetailListAjax.json'
				, type : 'POST'
				, timeout: 60000
				, data : params
				, dataType : 'json'
				, beforeSend: function(xmlHttpRequest) {
					cfOpenMagnificPopup();
				}
				, error: function(xhr, textStatus, errorThrown) {
					alert("시스템 오류가 발생했습니다.");
				}
				, success : function(json) {
					if (json.resultCode == 0){
						var list = json.list;
						$("#holidayDetailTbody").empty();
						var htmlStr = '';
						if (list.length == 0) {
							htmlStr += '<tr>';
							htmlStr += '<td class="align-center" colspan="5">조회 내용이 없습니다.</td>';
							htmlStr += '</tr>';
						} else {
							for (var i=0; i<list.length; i++) {
								htmlStr += '<tr>';
								htmlStr += '<td class="align-center">'+list[i].addDayTypeTxt+'</td>';
								htmlStr += '<td class="align-center">'+list[i].addDay+'</td>';
								htmlStr += '<td class="align-center">'+list[i].addDayComment+'</td>';
								htmlStr += '<td class="align-center">'+list[i].regKname+'</td>';
								htmlStr += '<td class="align-center">'+list[i].regDate+'</td>';
								htmlStr += '</tr>';
							}
						}
						$("#holidayDetailTbody").append(htmlStr);
						$("#historyModal").modal("show");
					}else if (json.resultCode == 1201) {
						alert(json.resultMsg);
						cfLogin();
					}else{
						alert(json.resultMsg);
					}
				}
				, complete : function() {
					cfCloseMagnificPopup();
				}
			});
		}
		
		// 엑셀 업로드 모달 팝업
		function excelUploadModal(){
			EXCEL_STATUS = 0;
			$("#excelLogArea").val("");
			$("#xlfile").val("");
			$("#excelUploadModal").modal("show");
		}
		
		// 엑셀 다운로드
		function excelDownload() {
			excelLogging("엑셀 다운로드 시작");
			location.href="/xls/reyon_holiday.xls";
			excelLogging("엑셀 다운로드 완료");
		}
		
		// 엑셀 검증
		function excelVerification() {
			if (EXCEL_STATUS == 0) {
				excelLogging("[2번 엑셀 업로드]를 먼저 진행하세요.");
				return;
			} else {
				excelLogging("엑셀 검증 시작");
				
				if(EXCEL_DATA.length == 0) {
					excelLogging("엑셀 검증 에러 : 검증할 데이터가 없습니다.");
					return;
				} else {
					for (var i = 0; i < EXCEL_DATA.length; i++) {
						// 변수 세팅
						var yymm = EXCEL_DATA[i].yymm;
						var sabun = EXCEL_DATA[i].sabun;
						var addDay = EXCEL_DATA[i].addDay;
						var addDayType = EXCEL_DATA[i].addDayType;
						var addDayComment = EXCEL_DATA[i].addDayComment;
						
						// 값이 있는지 검사
						if (yymm == "") {
							excelLogging("엑셀 검증 에러 : 값이 비었음(해당년도)(" + (i+2) + "행)");
							return;
						}
						if (sabun == "") {
							excelLogging("엑셀 검증 에러 : 값이 비었음(사번)(" + (i+2) + "행)");
							return;
						}
						if (addDay == "") {
							excelLogging("엑셀 검증 에러 : 값이 비었음(일수)(" + (i+2) + "행)");
							return;
						}
						if (addDayType == "") {
							excelLogging("엑셀 검증 에러 : 값이 비었음(증가/차감)(" + (i+2) + "행)");
							return;
						}
						if (addDayComment == "") {
							excelLogging("엑셀 검증 에러 : 값이 비었음(사유)(" + (i+2) + "행)");
							return;
						}
						
						// 값을 검증 
						if (!/^[0-9]{4}$/.test(yymm)){
							excelLogging("엑셀 검증 에러 : 숫자 4자리가 아님(해당년도)(" + (i+2) + "행)");
							return;
						}
						if (!/^[0-9]{8}$/.test(sabun)){
							excelLogging("엑셀 검증 에러 : 숫자 8자리가 아님(사번)(" + (i+2) + "행)");
							return;
						}
						if (!/[0-9]$/.test(addDay)){
							excelLogging("엑셀 검증 에러 : 숫자가 아님(일수)(" + (i+2) + "행)");
							return;
						}
						if (addDayType != "증가" && addDayType != "차감"){
							excelLogging("엑셀 검증 에러 : 알수없는 문자(증가/차감)(" + (i+2) + "행)");
							return;
						}
					}
					// 검증 완료
					EXCEL_STATUS = 2;
					excelLogging("엑셀 검증 완료");
				}
			}
		}
		
		// 데이터 적용
		function excelSubmit() {
			if (EXCEL_STATUS == 0) {
				excelLogging("[2번 엑셀 업로드]를 먼저 진행하세요.");
			} else if (EXCEL_STATUS == 1) {
				excelLogging("[3번 엑셀 검증을] 먼저 진행하세요.");
			} else {
				excelLogging("데이터 적용 시작");
				
				if(confirm("적용하시겠습니까?")){
					cfOpenMagnificPopup();
					COMPLETE_CNT = 0;
					
					for (var i = 0; i < EXCEL_DATA.length; i++) {
						// 변수 세팅
						var yymm = EXCEL_DATA[i].yymm;
						var sabun = EXCEL_DATA[i].sabun;
						var addDay = EXCEL_DATA[i].addDay;
						var addDayTypeStr = EXCEL_DATA[i].addDayType;
						var addDayComment = EXCEL_DATA[i].addDayComment;
						
						var addDayType = "";
						if(addDayTypeStr == "증가"){
							addDayType = "1";
						} else if(addDayTypeStr == "차감") {
							addDayType = "2";
						}
						
						var params = {
							auth : "reyon",
							yymm : yymm,
							sabun : sabun,
							addDayType : addDayType,
							addDay : addDay,
							addDayComment : addDayComment,
						}
						
						var request = $.ajax({
							url: '/hr/holidayMasterAddAjax.json'
							, type : 'POST'
							, timeout: 30000
							, data : params
							, dataType : 'json'
							, async : false
							, beforeSend: function(xmlHttpRequest) {
								
							}
							, error: function(xhr, textStatus, errorThrown) {
								excelLogging("시스템 오류가 발생했습니다. ("+yymm+","+sabun+","+addDayType+","+addDay+","+addDayComment+")");
							}
							, success : function(json) {
								if (json.resultCode == 1) {
									COMPLETE_CNT++;
								} else if (json.resultCode == 1201) {
									alert(json.resultMsg);
									cfLogin();
								} else {
									excelLogging(json.resultMsg);
								}
							}
							, complete : function() {
								excelLogging("데이터 적용중("+yymm+","+sabun+","+addDayType+","+addDay+","+addDayComment+")(" + COMPLETE_CNT + "/" + EXCEL_DATA.length + ")");
							}
						});
					}
					
					excelLogging("데이터 적용 완료");
					cfCloseMagnificPopup();
				}
			}
		}
		
		// 엑셀 로그 기록
		function excelLogging(txt){
			var textarea = $("#excelLogArea");
			textarea.val( textarea.val() + "\n" + getTimeStamp() + " " + txt);
		}
		
		// 현재시간
		function getTimeStamp() {
			var d = new Date();
			var s = "["+cfLeadingZeros(d.getFullYear(), 4) + '.' + cfLeadingZeros(d.getMonth() + 1, 2) + '.' + cfLeadingZeros(d.getDate(), 2) + ' ' +cfLeadingZeros(d.getHours(), 2) + ':' + cfLeadingZeros(d.getMinutes(), 2) + ':' + cfLeadingZeros(d.getSeconds(), 2)+"]";
			return s;
		}
		
	</script>
	</head>

	<body class="theme-indigo">
		<!--top start-->
		<%@ include file="/WEB-INF/views/include/top.jsp"%>
		<!--top end-->
		
		<!--sidebar start-->
		<%@ include file="/WEB-INF/views/include/sidebar.jsp"%>
		<!--sidebar end-->

		<!-- contents start -->
		<section class="content">
			<div class="container-fluid">
				
				<!-- title start -->
				<div class="block-header">
					<h2>연차 마스터 관리</h2>
				</div>
				<!-- title end -->

				<!-- search start -->
				<div class="row clearfix">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div class="card">
							<div class="header">
								<h2>조회</h2>
							</div>
							<div class="body">
								<form class="form-horizontal">
									<div class="row clearfix">
										<div class="col-lg-2 col-md-2 col-sm-4 col-xs-5 form-control-label">
											<label for="yymm">조회년도 <span style="color:red;">(필수)</span></label>
										</div>
										<div class="col-lg-10 col-md-10 col-sm-8 col-xs-7">
											<div class="form-group">
												<select id="yymm" class="form-control show-tick" data-show-subtext="true">
			                                    </select>
											</div>
										</div>
									</div>
									<div class="row clearfix">
										<div class="col-lg-2 col-md-2 col-sm-4 col-xs-5 form-control-label">
											<label for="deptName">소속</label>
										</div>
										<div class="col-lg-10 col-md-10 col-sm-8 col-xs-7">
	                                        <div class="form-group">
	                                            <div class="form-line">
	                                                <input type="text" id="deptName" class="form-control" placeholder="소속 입력 (EQUAL 검색)">
	                                            </div>
	                                        </div>
	                                    </div>
									</div>
									<div class="row clearfix">
										<div class="col-lg-2 col-md-2 col-sm-4 col-xs-5 form-control-label">
											<label for="kname">이름</label>
										</div>
										<div class="col-lg-10 col-md-10 col-sm-8 col-xs-7">
											<div class="form-group">
	                                            <div class="form-line">
	                                                <input type="text" id="kname" class="form-control" placeholder="이름 입력 (LIKE 검색)">
	                                            </div>
	                                        </div>
										</div>
									</div>
									<div class="row clearfix">
										<div class="col-lg-offset-2 col-md-offset-2 col-sm-offset-4 col-xs-offset-5">
											<button type="button" class="btn bg-indigo m-t-15 waves-effect" onClick="javascript:goSearch();">
												<i class="material-icons">search</i> <span>조회</span>
											</button>
											<button type="button" class="btn bg-green m-t-15 waves-effect" onClick="javascript:excelUploadModal();">
												<i class="material-icons">attachment</i> <span>엑셀 업로드 (일괄 작업)</span>
											</button>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
				<!-- search end -->

				<!-- result start -->
				<div class="row clearfix" id="resultDiv" style="display:none;">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div class="card">
							<div class="header">
								<h2 id="resultTitle">직원 연차 정보</h2>
							</div>
							<div class="body table-responsive" id="resultTbody">
							</div>
						</div>
					</div>
				</div>
				<!-- result end -->
				
				<!-- plus minus modal -->
				<div class="modal fade" id="plusMinusModal" tabindex="-1" role="dialog">
	                <div class="modal-dialog modal-lg" role="document">
	                    <div class="modal-content">
	                        <div class="modal-header">
	                            <h4 class="modal-title">연차 추가/제거</h4>
	                        </div>
	                        <div class="modal-body">
	                        	<input type="hidden" id="sabun">
	                        	<div class="body table-responsive">
                            		<table class="table table-bordered">
										<colgroup>
											<col style="width: 25%;">
											<col style="width: 25%;">
											<col style="width: 25%;">
											<col style="width: 25%;">
										</colgroup>
										<thead>
											<tr class="active">
												<th class="text-center">사번</th>
												<th class="text-center">부서명</th>
												<th class="text-center">직위</th>
												<th class="text-center">성명</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td class="text-center" id="sabunTxt"></td>
												<td class="text-center" id="deptNameTxt"></td>
												<td class="text-center" id="posLogTxt"></td>
												<td class="text-center" id="knameTxt"></td>
											</tr>
										</tbody>
									</table>
									
                            		<table class="table table-bordered">
										<colgroup>
											<col style="width: 25%;">
											<col style="width: 25%;">
											<col style="width: 25%;">
											<col style="width: 25%;">
										</colgroup>
										<tbody>
											<tr class="active">
												<th class="text-center" rowspan="2" style="vertical-align: middle;">현재연차</th>
												<th class="text-center">(+)연차</th>
												<th class="text-center">(-)연차</th>
												<th class="text-center">증감계</th>
											</tr>
											<tr>
												<td class="text-center" id="plusHolidayTxt"></td>
												<td class="text-center" id="minusHolidayTxt"></td>
												<td class="text-center" id="totalHolidayTxt"></td>
											</tr>
										</tbody>
									</table>
									
                            		<table class="table table-bordered">
										<colgroup>
											<col style="width: 20%;">
											<col style="width: 20%;">
											<col style="width: 60%;">
										</colgroup>
										<thead>
											<tr class="active">
												<th class="text-center">증가/차감</th>
												<th class="text-center">일수</th>
												<th class="text-center">사유</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td class="text-center">
													<select class="form-control show-tick" id="addDayType">
				                                        <option value="1">연차증가</option>
				                                        <option value="2">연차차감</option>
				                                    </select>
												</td>
												<td class="text-center">
													<div class="input-group spinner" data-trigger="spinner">
				                                        <div class="form-line">
				                                            <input type="text" class="form-control text-center" value="1" data-rule="quantity" id="addDay">
				                                        </div>
				                                        <span class="input-group-addon">
				                                            <a href="javascript:;" class="spin-up" data-spin="up"><i class="glyphicon glyphicon-chevron-up"></i></a>
				                                            <a href="javascript:;" class="spin-down" data-spin="down"><i class="glyphicon glyphicon-chevron-down"></i></a>
				                                        </span>
				                                    </div>
												</td>
												<td class="text-center">
													<input type="text" class="form-control" id="addDayComment" placeholder="사유 입력">
												</td>
											</tr>
										</tbody>
									</table>
								</div>
	                        </div>
	                        <div class="modal-footer">
	                            <button type="button" class="btn btn-link waves-effect" onclick="javascript:addHoliday();">저장</button>
	                            <button type="button" class="btn btn-link waves-effect" data-dismiss="modal">닫기</button>
	                        </div>
	                    </div>
	                </div>
	            </div>
	            
	            <!-- history modal -->
				<div class="modal fade" id="historyModal" tabindex="-1" role="dialog">
	                <div class="modal-dialog modal-lg" role="document">
	                    <div class="modal-content">
	                        <div class="modal-header">
	                            <h4 class="modal-title">연차 내역 조회</h4>
	                        </div>
	                        <div class="modal-body">
	                        	<div class="body table-responsive">
                            		<table class="table table-bordered">
										<colgroup>
											<col style="width: 25%;">
											<col style="width: 25%;">
											<col style="width: 25%;">
											<col style="width: 25%;">
										</colgroup>
										<thead>
											<tr class="active">
												<th class="text-center">사번</th>
												<th class="text-center">부서명</th>
												<th class="text-center">직급</th>
												<th class="text-center">성명</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td class="text-center" id="sabunTxt1"></td>
												<td class="text-center" id="deptNameTxt1"></td>
												<td class="text-center" id="posLogTxt1"></td>
												<td class="text-center" id="knameTxt1"></td>
											</tr>
										</tbody>
									</table>
									
                            		<table class="table table-bordered">
										<colgroup>
											<col style="width: 15%;">
											<col style="width: 10%;">
											<col style="width: 40%;">
											<col style="width: 15%;">
											<col style="width: 20%;">
										</colgroup>
										<thead>
											<tr class="active">
												<th class="text-center">증가/차감</th>
												<th class="text-center">일수</th>
												<th class="text-center">사유</th>
												<th class="text-center">처리자</th>
												<th class="text-center">처리시간</th>
											</tr>
										</thead>
										<tbody id="holidayDetailTbody">
										</tbody>
									</table>
								</div>
	                        </div>
	                        <div class="modal-footer">
	                            <button type="button" class="btn btn-link waves-effect" data-dismiss="modal">확인</button>
	                        </div>
	                    </div>
	                </div>
	            </div>
	            
	            <!-- excel upload modal -->
				<div class="modal fade" id="excelUploadModal" tabindex="-1" role="dialog">
	                <div class="modal-dialog modal-lg" role="document">
	                    <div class="modal-content">
	                        <div class="modal-header">
	                            <h4 class="modal-title">엑셀 업로드 (일괄 작업)</h4>
	                        </div>
	                        <div class="modal-body">
	                        	<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
	                        		<h4>1. 엑셀 양식 다운로드</h4>
		                            <p class="m-t-15 m-b-30">
		                                <button type="button" class="btn bg-green waves-effect" onClick="javascript:excelDownload();">엑셀 다운로드</button>
		                            </p>
		                            <h4>2. 엑셀 업로드</h4>
		                            <p class="m-t-15 m-b-30">
		                            	<form>
											<div class="form-group">
												<input type="file" class="form-control-file" id="xlfile">
											</div>
										</form>
		                            </p>
		                            <h4>3. 엑셀 검증</h4>
		                            <p class="m-t-15 m-b-30">
		                                <button type="button" class="btn btn-primary waves-effect" onClick="javascript:excelVerification();">엑셀 검증</button>
		                            </p>
		                            <h4>4. 데이터 적용</h4>
		                            <p class="m-t-15 m-b-30">
		                                <button type="button" class="btn bg-red waves-effect" onClick="javascript:excelSubmit();">데이터 적용</button>
		                            </p>
	                        	</div>
	                        	<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
	                        		<div class="form-group">
                                        <div class="form-line">
                                            <textarea id="excelLogArea" rows="18" class="form-control no-resize" placeholder="엑셀 로그 영역" readonly></textarea>
                                        </div>
                                    </div>
	                        	</div>
	                        </div>
	                        <div class="modal-footer">
	                            <button type="button" class="btn btn-link waves-effect" data-dismiss="modal">닫기</button>
	                        </div>
	                    </div>
	                </div>
	            </div>

			</div>
		</section>
		<!-- contents end -->
		
		<!--loading Popup start-->
		<%@ include file="/WEB-INF/views/include/loading.jsp"%>
		<!--loading Popup end--> 

	</body>
</html>