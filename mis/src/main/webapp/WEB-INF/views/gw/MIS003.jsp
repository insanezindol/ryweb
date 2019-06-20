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
	<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Lato:400,300,300italic,400italic,600,600italic,700,700italic,800,800italic">
	<link rel="stylesheet" href="https://fonts.googleapis.com/earlyaccess/nanumgothic.css">
	<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
	<link rel="stylesheet" href="/plugins/bootstrap/css/bootstrap.css?ver=20190618">
	<link rel="stylesheet" href="/plugins/node-waves/waves.css?ver=20190618">
	<link rel="stylesheet" href="/plugins/animate-css/animate.css?ver=20190618">
	<link rel="stylesheet" href="/plugins/sweetalert/sweetalert.css?ver=20190618">
    <link rel="stylesheet" href="/plugins/bootstrap-material-datetimepicker/css/bootstrap-material-datetimepicker.css?ver=20190618">
    <link rel="stylesheet" href="/plugins/waitme/waitMe.css?ver=20190618">
    <link rel="stylesheet" href="/plugins/bootstrap-select/css/bootstrap-select.css?ver=20190618">
	<link rel="stylesheet" href="/plugins/morrisjs/morris.css?ver=20190618">
	<link rel="stylesheet" href="/css/materialize.css?ver=20190618">
	<link rel="stylesheet" href="/css/style.css?ver=20190618">
	<link rel="stylesheet" href="/css/themes/all-themes.css?ver=20190618">
	<!-- Jquery Core Js -->
	<script src="/plugins/jquery/jquery.min.js?ver=20190618"></script>
	<!-- Bootstrap Core Js -->
	<script src="/plugins/bootstrap/js/bootstrap.js?ver=20190618"></script>
	<!-- Select Plugin Js -->
	<script src="/plugins/bootstrap-select/js/bootstrap-select.js?ver=20190618"></script>
	<script src="/plugins/bootstrap-select/js/i18n/defaults-ko_KR.min.js?ver=20190618"></script>
	<!-- Slimscroll Plugin Js -->
	<script src="/plugins/jquery-slimscroll/jquery.slimscroll.js?ver=20190618"></script>
	<!-- Waves Effect Plugin Js -->
	<script src="/plugins/node-waves/waves.js?ver=20190618"></script>
	<!-- SweetAlert Plugin Js -->
    <script src="/plugins/sweetalert/sweetalert.min.js?ver=20190618"></script>
	<!-- Autosize Plugin Js -->
    <script src="/plugins/autosize/autosize.js?ver=20190618"></script>
	<!-- Moment Plugin Js -->
    <script src="/plugins/momentjs/moment.js?ver=20190618"></script>
    <script src="/plugins/momentjs/moment-ko.js?ver=20190618"></script>
    <!-- Bootstrap Material Datetime Picker Plugin Js -->
    <script src="/plugins/bootstrap-material-datetimepicker/js/bootstrap-material-datetimepicker.js?ver=20190618"></script>
	<!-- Jquery CountTo Plugin Js -->
	<script src="/plugins/jquery-countto/jquery.countTo.js?ver=20190618"></script>
	<!-- Morris Plugin Js -->
	<script src="/plugins/raphael/raphael.min.js?ver=20190618"></script>
	<script src="/plugins/morrisjs/morris.js?ver=20190618"></script>
	<!-- AES script -->
	<script src="/js/AesUtil.js?ver=20190618"></script>
	<script src="/js/aes.js?ver=20190618"></script>
	<script src="/js/pbkdf2.js?ver=20190618"></script>
	<!-- Custom Js -->
	<script src="/js/admin.js?ver=20190618"></script>
	<script src="/js/pages/forms/basic-form-elements.js?ver=20190618"></script>
	<!-- common function -->
	<script type="text/javascript" src="/js/common.js?ver=20190618"></script>
	<script type="text/javascript">
		// 잔여연차 전역변수
		var HOLIDAY_RESULT_CNT = 0;
		
		$(document).ready(function() {
			$("#startdate").bootstrapMaterialDatePicker({
				lang : 'ko', weekStart : 0, time: false
			});
			$("#enddate").bootstrapMaterialDatePicker({
				lang : 'ko', weekStart : 0, time: false
			});
			
			// 나의 연차정보 가져오기
			initMyinfo();
			
			// 분류 선택 change event
			$("input[name=holidayGbn]").change(function() {
				var holidayGbn = $(this).val();
				var startdate = $("#startdate").val();
				var enddate = $("#enddate").val();
				
				if(holidayGbn != "" && startdate != "" && enddate != "") {
					var btDay = cfCalcDate(startdate, enddate);
					
					var product = 0;
					var viewProduct = 0;
					if (holidayGbn == "1" || holidayGbn == "4" || holidayGbn == "6" || holidayGbn == "8") {
						// '1','4','6','8' 연차, 특별휴가, 장기근속휴가, 기타
						product = 1;
						viewProduct = 1;
					} else if (holidayGbn == "2" || holidayGbn == "3") {
						// '2','3' 반차(오전), 반차(오후)
						product = 0.5;
						viewProduct = 0.5;
					} else {
						// '5','7' 생리휴가, 병가
						product = 0;
						viewProduct = 1;
					}
					
					var resultVal = HOLIDAY_RESULT_CNT - (btDay * product);
					
					$("#holidayTotalViewCnt").text((btDay * viewProduct)+"일");
					$("#holidayTotalCnt").text((btDay * product)+"일");
					$("#holidayResultCnt").text(resultVal + "일");
				}
			});
			
			// bootstrapMaterialDatePicker change event
			$('#startdate').on('change', function (e, date) {
				$("#enddate").bootstrapMaterialDatePicker("setMinDate", date);
				
				var holidayGbn = $("input[name=holidayGbn]:checked").val();
				var startdate = $("#startdate").val();
				var enddate = $("#enddate").val();
				
				if(holidayGbn != "" && startdate != "" && enddate != "") {
					var btDay = cfCalcDate(startdate, enddate);
					
					var product = 0;
					var viewProduct = 0;
					if (holidayGbn == "1" || holidayGbn == "4" || holidayGbn == "6" || holidayGbn == "8") {
						// '1','4','6','8' 연차, 특별휴가, 장기근속휴가, 기타
						product = 1;
						viewProduct = 1;
					} else if (holidayGbn == "2" || holidayGbn == "3") {
						// '2','3' 반차(오전), 반차(오후)
						product = 0.5;
						viewProduct = 0.5;
					} else {
						// '5','7' 생리휴가, 병가
						product = 0;
						viewProduct = 1;
					}
					
					var resultVal = HOLIDAY_RESULT_CNT - (btDay * product);
					
					$("#holidayTotalViewCnt").text((btDay * viewProduct)+"일");
					$("#holidayTotalCnt").text((btDay * product)+"일");
					$("#holidayResultCnt").text(resultVal + "일");
				}
			});
			$('#enddate').on('change', function (e, date) {
				$("#startdate").bootstrapMaterialDatePicker("setMaxDate", date);
				
				var holidayGbn = $("input[name=holidayGbn]:checked").val();
				var startdate = $("#startdate").val();
				var enddate = $("#enddate").val();
				
				if(holidayGbn != "" && startdate != "" && enddate != "") {
					var btDay = cfCalcDate(startdate, enddate);
					
					var product = 0;
					var viewProduct = 0;
					if (holidayGbn == "1" || holidayGbn == "4" || holidayGbn == "6" || holidayGbn == "8") {
						// '1','4','6','8' 연차, 특별휴가, 장기근속휴가, 기타
						product = 1;
						viewProduct = 1;
					} else if (holidayGbn == "2" || holidayGbn == "3") {
						// '2','3' 반차(오전), 반차(오후)
						product = 0.5;
						viewProduct = 0.5;
					} else {
						// '5','7' 생리휴가, 병가
						product = 0;
						viewProduct = 1;
					}
					
					var resultVal = HOLIDAY_RESULT_CNT - (btDay * product);
					
					$("#holidayTotalViewCnt").text((btDay * viewProduct)+"일");
					$("#holidayTotalCnt").text((btDay * product)+"일");
					$("#holidayResultCnt").text(resultVal + "일");
				}
			});
			
			// 사유 선택 change event
			$("input[name=reasonGbn]").change(function() {
				var reasonGbn = $(this).val();
				if (reasonGbn == "직접입력") {
					$("#reason").val("");
					$("#reason").show();
				} else {
					$("#reason").val(reasonGbn);
					$("#reason").hide();
				}
			});
		});
		
		// 나의 연차정보 가져오기
		function initMyinfo() {
			var d = new Date();
			var yymm = cfLeadingZeros(d.getFullYear(), 4);
			var sabun = $("#sabun").val();
			
			var params = {
				auth : "reyon",
				yymm : yymm,
				sabun : sabun,
			}
			
			var request = $.ajax({
				url: '/hr/getHolidayListAjax.json'
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
						$("#resultTbody1").empty();
						$("#resultTbody2").empty();
						$("#resultTitle1").html("나의 연차 정보 (" + yymm + "년)<small>휴가 신청 전 나의 연차 정보를 확인해주세요.</small>");
						
						var htmlStr1 = '';
						htmlStr1 += '<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 0px;">';
						htmlStr1 += '<table class="table table-bordered">';
						htmlStr1 += '<colgroup>';
						htmlStr1 += '<col style="width: 14%;">';
						htmlStr1 += '<col style="width: 14%;">';
						htmlStr1 += '<col style="width: 13%;">';
						htmlStr1 += '<col style="width: 13%;">';
						htmlStr1 += '<col style="width: 13%;">';
						htmlStr1 += '<col style="width: 13%;">';
						htmlStr1 += '<col style="width: 13%;">';
						htmlStr1 += '<col style="width: 7%;">';
						htmlStr1 += '</colgroup>';
						htmlStr1 += '<tbody>';
						htmlStr1 += '<tr>';
						htmlStr1 += '<th class="bg-indigo align-center">소속</th>';
						htmlStr1 += '<th class="bg-indigo align-center">이름</th>';
						htmlStr1 += '<th class="bg-indigo align-center">발생연차</th>';
						htmlStr1 += '<th class="bg-indigo align-center">조정연차</th>';
						htmlStr1 += '<th class="bg-indigo align-center">연차합계</th>';
						htmlStr1 += '<th class="bg-indigo align-center">사용연차</th>';
						htmlStr1 += '<th class="bg-indigo align-center">잔여연차</th>';
						htmlStr1 += '<th class="bg-indigo align-center">상세내역</th>';
						htmlStr1 += '</tr>';
						htmlStr1 += '<tr>';
						htmlStr1 += '<td class="align-center">'+info.deptName+'</td>';
						htmlStr1 += '<td class="align-center">'+info.kname+'</td>';
						htmlStr1 += '<td class="align-center">'+info.dayPlus+'일</td>';
						htmlStr1 += '<td class="align-center">'+info.dayMinus+'일</td>';
						htmlStr1 += '<td class="align-center">'+info.dayTotal+'일</td>';
						htmlStr1 += '<td class="align-center">'+info.useDay+'일</td>';
						htmlStr1 += '<td class="align-center">'+info.remainDay+'일</td>';
						htmlStr1 += '<td class="align-center">';
						htmlStr1 += '<button type="button" class="btn bg-blue waves-effect btn-xs waves-effect" onClick="javascript:addHistoryHoliday(\''+yymm+'\',\''+info.sabun+'\',\''+info.kname+'\',\''+info.deptName+'\',\''+info.posLog+'\');"><i class="material-icons">playlist_add</i></button>';
						htmlStr1 += '<button type="button" class="btn bg-red waves-effect btn-xs waves-effect" onClick="javascript:useHistoryHoliday(\''+yymm+'\',\''+info.sabun+'\',\''+info.kname+'\',\''+info.deptName+'\',\''+info.posLog+'\');"><i class="material-icons">playlist_add_check</i></button>';
						htmlStr1 += '</td>';
						htmlStr1 += '</tr>';
						htmlStr1 += '</tbody>';
						htmlStr1 += '</table>';
						htmlStr1 += '</div>';
						
						var htmlStr2 = '';
						if(list.length == 0){
							htmlStr2 += '<tr><td class="align-center" colspan="8">연차 사용 내역이 없습니다.</td></tr>';
						} else {
							for(var i=0; i<list.length; i++){
								htmlStr2 += '<tr>';
								htmlStr2 += '<td class="align-center">'+(i+1)+'</td>';
								htmlStr2 += '<td class="align-center">'+list[i].holidayGbnTxt+'</td>';
								htmlStr2 += '<td class="align-center">'+list[i].startdate+' ~ '+list[i].enddate+'</td>';
								htmlStr2 += '<td class="align-center">'+list[i].viewMinusCnt+'</td>';
								htmlStr2 += '<td class="align-center">'+list[i].minusYn+'</td>';
								htmlStr2 += '<td class="align-center">'+list[i].reason+'</td>';
								htmlStr2 += '<td class="align-center">'+list[i].takeover+'</td>';
								htmlStr2 += '<td class="align-center">'+list[i].gwStatusTxt+'</td>';
								htmlStr2 += '</tr>';
							}
						}
						
						HOLIDAY_RESULT_CNT = info.remainDay;
						$("#holidayTotalViewCnt").text("0일");
						$("#holidayTotalCnt").text("0일");
						$("#holidayResultCnt").text(info.remainDay+'일');
						$("#resultTbody1").append(htmlStr1);
						$("#resultTbody2").append(htmlStr2);
					}else{
						alert(json.resultMsg);
					}
				}
				, complete : function() {
					cfCloseMagnificPopup();
				}
			});
		}
		
		// 연차 사용 테이블 입력 및 결재 상신
		function insertHoliday() {
			var sabun = $("#sabun").val();
			var kname = $("#kname").val();
			var deptName = $("#deptName").val();
			var posLog = $("#posLog").val();
			var holidayGbn = $("input[name=holidayGbn]:checked").val();
			var holidayGbnTxt = $("#holidayGbnTxt"+holidayGbn).text();
			var holidayTotalViewCnt = $("#holidayTotalViewCnt").text().slice(0, -1);
			var holidayTotalCnt = $("#holidayTotalCnt").text().slice(0, -1);
			var startdate = $("#startdate").val();
			var enddate = $("#enddate").val();
			var reason = $("#reason").val();
			var takeover = $("#takeover").val();
			
			if (sabun == "") {
				alert("사용자 정보가 없습니다.");
				return;
			}
			if (holidayGbn == "") {
				alert("분류를 선택해주세요.");
				return;
			}
			if (startdate == "") {
				alert("시작일자를 선택해주세요.");
				return;
			}
			if (enddate == "") {
				alert("종료일자를 선택해주세요.");
				return;
			}
			if (reason == "") {
				alert("사유를 입력해주세요.");
				return;
			}
			if (takeover == "") {
				alert("업무인계자를 입력해주세요.");
				return;
			}
			
			var params = {
				auth : "reyon",
				type : "MIS003",
				sabun : sabun,
				kname : kname,
				deptName : deptName,
				posLog : posLog,
				holidayGbn : holidayGbn,
				holidayGbnTxt : holidayGbnTxt,
				holidayTotalViewCnt : holidayTotalViewCnt,
				holidayTotalCnt : holidayTotalCnt,
				startdate : startdate,
				enddate : enddate,
				reason : reason,
				takeover : takeover,
			}
			
			var request = $.ajax({
				url: '/gw/gwToMisAddAjax.json'
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
					if (json.resultCode == 1) {
						var url = "http://rygw.reyonpharm.co.kr/index.aspx?user_num=" + sabun + "&appro_key=" + json.approKey + "&surl=approval/appro_form_mis.aspx";
						window.open(url,"_blank");
						swal({
					        title: "휴가신청서 작성 완료",
					        text: "전자결재 상신 후 [확인] 버튼을 클릭하세요.",
					        type: "success",
					        closeOnClickOutside: false,
					        closeOnEsc: false,
					        confirmButtonText: "확인",
					    }, function () {
					    	location.href = "http://rygw.reyonpharm.co.kr/approval/appro_list.aspx?abox=send";
					    });
					} else if (json.resultCode == 1201) {
						alert(json.resultMsg);
						cfLogin();
					} else {
						alert(json.resultMsg);
					}
				}
				, complete : function() {
					cfCloseMagnificPopup();
				}
			});
		}
		
		// 결재 상신
		function goApproval() {
			var sabun = $("#sabun").val();
			var kname = $("#kname").val();
			var deptName = $("#deptName").val();
			var posLog = $("#posLog").val();
			var holidayGbn = $("input[name=holidayGbn]:checked").val();
			var holidayGbnTxt = $("#holidayGbnTxt"+holidayGbn).text();
			var holidayTotalViewCnt = $("#holidayTotalViewCnt").text().slice(0, -1);
			var holidayTotalCnt = $("#holidayTotalCnt").text().slice(0, -1);
			var startdate = $("#startdate").val();
			var enddate = $("#enddate").val();
			var reason = $("#reason").val();
			var takeover = $("#takeover").val();
			
			if (sabun == "") {
				alert("사용자 정보가 없습니다.");
				return;
			}
			if (holidayGbn == "") {
				alert("분류를 선택해주세요.");
				return;
			}
			if (startdate == "") {
				alert("시작일자를 선택해주세요.");
				return;
			}
			if (enddate == "") {
				alert("종료일자를 선택해주세요.");
				return;
			}
			if (reason == "") {
				alert("사유를 입력해주세요.");
				return;
			}
			if (takeover == "") {
				alert("업무인계자를 입력해주세요.");
				return;
			}
			
			// 결재 상신
			insertHoliday();
		}
		
		// 휴가 증감 내역
		function addHistoryHoliday(yymm, sabun, kname, deptName, posLog){
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
						$("#addHistoryModalTitle").text("연차 증감 내역 ("+yymm+"년)");
						$("#addHistoryModal").modal("show");
					}else{
						alert(json.resultMsg);
					}
				}
				, complete : function() {
					cfCloseMagnificPopup();
				}
			});
		}
		
		// 휴가 사용 내역
		function useHistoryHoliday(yymm, sabun, kname, deptName, posLog){
			$("#sabunTxt2").text(sabun);
			$("#knameTxt2").text(kname);
			$("#deptNameTxt2").text(deptName);
			$("#posLogTxt2").text(posLog);
			$("#useHistoryModalTitle").text("연차 사용 내역 ("+yymm+"년)");
			$("#useHistoryModal").modal("show");
		}
		
	</script>
</head>
<body class="theme-indigo">

	<div class="container-fluid">
	
		<div class="row clearfix" style="margin-top: 20px;">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="card">
					<div class="header">
						<h2>
							휴가신청서<small>하단 양식에 맞게 작성 후 전자결재 상신 버튼을 눌러주세요.</small>
							<input type="hidden" id="sabun" value="${info.sabun }">
							<input type="hidden" id="kname" value="${info.kname }">
							<input type="hidden" id="deptName" value="${info.deptName }">
							<input type="hidden" id="posLog" value="${info.posLog }">
						</h2>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row clearfix">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="card">
					<div class="header">
						<h2 id="resultTitle1">나의 연차 정보 <small>휴가 신청 전 나의 연차 정보를 확인해주세요.</small></h2>
					</div>
					<div class="body">
						<div class="row clearfix" id="resultTbody1">
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row clearfix">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="card">
					<div class="header">
						<h2>
							분류 <small>휴가 종류를 선택해주세요.</small>
						</h2>
					</div>
					<div class="body">
						<div class="demo-radio-button">
							<input name="holidayGbn" type="radio" id="holidayGbn1" class="with-gap radio-col-indigo" value="1" checked="checked" /><label for="holidayGbn1" id="holidayGbnTxt1">연차</label> 
							<input name="holidayGbn" type="radio" id="holidayGbn2" class="with-gap radio-col-indigo" value="2" /><label for="holidayGbn2" id="holidayGbnTxt2">반차(오전)</label>
							<input name="holidayGbn" type="radio" id="holidayGbn3" class="with-gap radio-col-indigo" value="3" /><label for="holidayGbn3" id="holidayGbnTxt3">반차(오후)</label> 
							<input name="holidayGbn" type="radio" id="holidayGbn4" class="with-gap radio-col-indigo" value="4" /><label for="holidayGbn4" id="holidayGbnTxt4">특별휴가</label> 
							<input name="holidayGbn" type="radio" id="holidayGbn5" class="with-gap radio-col-indigo" value="5" /><label for="holidayGbn5" id="holidayGbnTxt5">생리휴가</label> 
							<input name="holidayGbn" type="radio" id="holidayGbn6" class="with-gap radio-col-indigo" value="6" /><label for="holidayGbn6" id="holidayGbnTxt6">장기근속휴가</label> 
							<input name="holidayGbn" type="radio" id="holidayGbn7" class="with-gap radio-col-indigo" value="7" /><label for="holidayGbn7" id="holidayGbnTxt7">병가</label> 
							<input name="holidayGbn" type="radio" id="holidayGbn8" class="with-gap radio-col-indigo" value="8" /><label for="holidayGbn8" id="holidayGbnTxt8">기타</label>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="row clearfix">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="card">
					<div class="header">
						<h2>
							일자/기간 <small>시작일자와 종료일자를 선택해주세요.</small>
						</h2>
					</div>
					<div class="body" style="padding-bottom: 0px;">
						<div class="row clearfix">
							<div class="col-sm-3">
								<div class="form-group">
									<div class="form-line">
										<input type="text" id="startdate" class="form-control align-center" placeholder="시작일">
									</div>
								</div>
							</div>
							<div class="col-sm-1">
								<div class="align-center">부터</div>
							</div>
							<div class="col-sm-3">
								<div class="form-group">
									<div class="form-line">
										<input type="text" id="enddate" class="form-control align-center" placeholder="종료일">
									</div>
								</div>
							</div>
							<div class="col-sm-2">
								<div class="align-center">까지 (<span id="holidayTotalViewCnt"></span>간)</div>
							</div>
							<div class="col-sm-3" style="margin-bottom: 0px;">
								<table class="table table-bordered">
									<colgroup>
										<col style="width: 50%;">
										<col style="width: 50%;">
									</colgroup>
									<tbody>
										<tr>
											<th class="bg-indigo align-center">차감연차</th>
											<th class="bg-indigo align-center">사용 후 잔여연차</th>
										</tr>
										<tr>
											<td class="align-center" id="holidayTotalCnt"></td>
											<td class="align-center" id="holidayResultCnt"></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row clearfix">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="card">
					<div class="header">
						<h2>
							사유 <small>사유를 입력해주세요.</small>
						</h2>
					</div>
					<div class="body">
						<div class="row clearfix">
							<div class="col-sm-6">
								<div class="demo-radio-button">
									<input name="reasonGbn" type="radio" id="reason1" class="with-gap radio-col-indigo" value="가족" checked="checked" /><label for="reason1">가족</label> 
									<input name="reasonGbn" type="radio" id="reason2" class="with-gap radio-col-indigo" value="건강" /><label for="reason2">건강</label>
									<input name="reasonGbn" type="radio" id="reason3" class="with-gap radio-col-indigo" value="개인사유" /><label for="reason3">개인사유</label> 
									<input name="reasonGbn" type="radio" id="reason4" class="with-gap radio-col-indigo" value="직접입력" /><label for="reason4">직접입력</label>
								</div>
							</div>
							<div class="col-sm-6">
								<input type="text" class="form-control" id="reason" maxlength="20" placeholder="사유를 입력해주세요." style="display: none;" value="가족">
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="row clearfix">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="card">
					<div class="header">
						<h2>
							업무인계자 <small>업무인계자를 입력해주세요.</small>
						</h2>
					</div>
					<div class="body">
						<div class="row clearfix">
							<div class="col-sm-12">
								<div class="form-group form-float">
									<div class="form-line">
										<input type="text" class="form-control" id="takeover" maxlength="100">  
										<label class="form-label">업무인계자</label>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row clearfix" style="margin-bottom:30px;">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 align-right">
				<button type="button" class="btn bg-indigo waves-effect" onClick="javascript:goApproval();"> <i class="material-icons">email</i> <span>전자 결재 상신</span> </button>
			</div>
		</div>
		
		<!-- history modal start -->
		<div class="modal fade" id="addHistoryModal" tabindex="-1" role="dialog">
               <div class="modal-dialog modal-lg" role="document">
                   <div class="modal-content">
                       <div class="modal-header">
                           <h4 class="modal-title" id="addHistoryModalTitle">연차 증감 내역</h4>
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
										<th class="text-center">직위</th>
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
										<th class="bg-indigo text-center">증가/차감</th>
										<th class="bg-indigo text-center">일수</th>
										<th class="bg-indigo text-center">사유</th>
										<th class="bg-indigo text-center">처리자</th>
										<th class="bg-indigo text-center">처리시간</th>
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
           <!-- history modal end -->
           
           <!-- history modal start -->
			<div class="modal fade" id="useHistoryModal" tabindex="-1" role="dialog">
	               <div class="modal-dialog modal-lg" role="document">
	                   <div class="modal-content">
	                       <div class="modal-header">
	                           <h4 class="modal-title" id="useHistoryModalTitle">연차 사용 내역</h4>
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
											<th class="text-center">직위</th>
											<th class="text-center">성명</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td class="text-center" id="sabunTxt2"></td>
											<td class="text-center" id="deptNameTxt2"></td>
											<td class="text-center" id="posLogTxt2"></td>
											<td class="text-center" id="knameTxt2"></td>
										</tr>
									</tbody>
								</table>
								
								<table class="table table-bordered">
									<colgroup>
										<col style="width: 8%;">
										<col style="width: 10%;">
										<col style="width: 16%;">
										<col style="width: 10%;">
										<col style="width: 10%;">
										<col style="width: 24%;">
										<col style="width: 12%;">
										<col style="width: 10%;">
									</colgroup>
									<thead>
										<tr class="active">
											<th class="bg-indigo align-center">순번</th>
											<th class="bg-indigo align-center">분류</th>
											<th class="bg-indigo align-center">일자/기간</th>
											<th class="bg-indigo align-center">휴가일수</th>
											<th class="bg-indigo align-center">차감대상</th>
											<th class="bg-indigo align-center">사유</th>
											<th class="bg-indigo align-center">업무인계자</th>
											<th class="bg-indigo align-center">상태</th>
										</tr>
									</thead>
									<tbody id="resultTbody2">
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
	           <!-- history modal end -->

	</div>

	<!--loading Popup start-->
	<%@ include file="/WEB-INF/views/include/loading.jsp"%>
	<!--loading Popup end--> 
	
</body>
</html>