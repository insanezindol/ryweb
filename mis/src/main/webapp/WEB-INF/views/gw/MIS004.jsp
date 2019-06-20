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
	<link rel="stylesheet" href="/plugins/easy-autocomplete/easy-autocomplete.min.css?ver=20190618">
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
	<!-- easy-autocomplete -->
	<script src="/plugins/easy-autocomplete/jquery.easy-autocomplete.min.js?ver=20190618"></script>
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
	
		$(document).ready(function() {
			// 나의 초과근무 정보 가져오기
			initMyinfo();
			initMyList();
			// 근무자 정보 가져오기
			initUserinfo();
			
			var firstDay = getFirstday();
			var lastDay = getLastday();
			
			// bootstrapMaterialDatePicker init
			$("#startdate").bootstrapMaterialDatePicker({
				lang : 'ko',
				weekStart : 0,
				format : 'YYYY-MM-DD HH:mm',
				minDate : firstDay,
				maxDate : lastDay,
				minuteStep : 30,
			});
			$("#enddate").bootstrapMaterialDatePicker({
				lang : 'ko',
				weekStart : 0,
				format : 'YYYY-MM-DD HH:mm',
				minDate : firstDay,
				maxDate : lastDay,
				minuteStep : 30,
			});
			$("#restStarttime1").bootstrapMaterialDatePicker({
				lang : 'ko',
				weekStart : 0, 
				format : 'YYYY-MM-DD HH:mm', 
				minuteStep : 30,
			});
			$("#restStarttime2").bootstrapMaterialDatePicker({
				lang : 'ko',
				weekStart : 0, 
				format : 'YYYY-MM-DD HH:mm',
				minuteStep : 30,
			});
			$("#restStarttime3").bootstrapMaterialDatePicker({
				lang : 'ko',
				weekStart : 0, 
				format : 'YYYY-MM-DD HH:mm',
				minuteStep : 30,
			});
			
			// bootstrapMaterialDatePicker change event
			$('#startdate').on('change', function (e, date) {
				var startdate = $("#startdate").val();
				var enddate = $("#enddate").val();
				
				// 값 초기화
				$("#restStarttime1").val("");
				$("#restEndtime1").val("");
				$("#restStarttime2").val("");
				$("#restEndtime2").val("");
				$("#restStarttime3").val("");
				$("#restEndtime3").val("");
				$("#term1").val("0");
				$("#term2").val("0");
				$("#term3").val("0");
				$("#term1").selectpicker('refresh');
				$("#term2").selectpicker('refresh');
				$("#term3").selectpicker('refresh');
				
				$("#restStarttime1").bootstrapMaterialDatePicker("setMinDate", date);
			    $("#restStarttime2").bootstrapMaterialDatePicker("setMinDate", date);
			    $("#restStarttime3").bootstrapMaterialDatePicker("setMinDate", date);
				
				if(startdate != "" && enddate != "") {
					var btTime = cfCalcTime(startdate, enddate);
					
					if(btTime < 0) {
						alert("종료시간을 시작시간보다 미래로 설정하세요.\n일자/시간을 다시 선택 하세요.");
						$("#startdate").val("");
						$("#enddate").val("");
						$("#resttime1Div").hide();
						$("#resttime2Div").hide();
						$("#resttime3Div").hide();
						
						// 근무자 시간 계산
						$('input[name^="workerSabun"]').each(function() {
							getUserOvertimeinfo($(this).val());
						});
					} else {
						
						if (btTime >= 240 && btTime < 480) {
							// 4시간 ~ 8시간
							$("#resttime1Div").show();
							$("#resttime2Div").hide();
							$("#resttime3Div").hide();
						} else if (btTime >= 480 && btTime < 720) {
							// 8시간 ~ 12시간
							$("#resttime1Div").show();
							$("#resttime2Div").show();
							$("#resttime3Div").hide();
						} else if (btTime >= 720) {
							// 12시간 이상
							$("#resttime1Div").show();
							$("#resttime2Div").show();
							$("#resttime3Div").show();
						} else {
							// 4시간 미만
							$("#resttime1Div").hide();
							$("#resttime2Div").hide();
							$("#resttime3Div").hide();
						}
						
						// 근무자 시간 계산
						$('input[name^="workerSabun"]').each(function() {
							getUserOvertimeinfo($(this).val());
						});
					}
				}
			});
			
			$('#enddate').on('change', function (e, date) {
				var startdate = $("#startdate").val();
				var enddate = $("#enddate").val();
				
				// 값 초기화
				$("#restStarttime1").val("");
				$("#restEndtime1").val("");
				$("#restStarttime2").val("");
				$("#restEndtime2").val("");
				$("#restStarttime3").val("");
				$("#restEndtime3").val("");
				$("#term1").val("0");
				$("#term2").val("0");
				$("#term3").val("0");
				$("#term1").selectpicker('refresh');
				$("#term2").selectpicker('refresh');
				$("#term3").selectpicker('refresh');
				
				$("#restStarttime1").bootstrapMaterialDatePicker("setMaxDate", date);
				$("#restStarttime2").bootstrapMaterialDatePicker("setMaxDate", date);
				$("#restStarttime3").bootstrapMaterialDatePicker("setMaxDate", date);
				
				if(startdate != "" && enddate != "") {
					var btTime = cfCalcTime(startdate, enddate);
					
					if(btTime < 0) {
						alert("종료시간을 시작시간보다 미래로 설정하세요.\n일자/시간을 다시 선택 하세요.");
						$("#startdate").val("");
						$("#enddate").val("");
						$("#resttime1Div").hide();
						$("#resttime2Div").hide();
						$("#resttime3Div").hide();
						
						// 근무자 시간 계산
						$('input[name^="workerSabun"]').each(function() {
							getUserOvertimeinfo($(this).val());
						});
					} else {
						
						if (btTime >= 240 && btTime < 480) {
							// 4시간 ~ 8시간
							$("#resttime1Div").show();
							$("#resttime2Div").hide();
							$("#resttime3Div").hide();
						} else if (btTime >= 480 && btTime < 720) {
							// 8시간 ~ 12시간
							$("#resttime1Div").show();
							$("#resttime2Div").show();
							$("#resttime3Div").hide();
						} else if (btTime >= 720) {
							// 12시간 이상
							$("#resttime1Div").show();
							$("#resttime2Div").show();
							$("#resttime3Div").show();
						} else {
							// 4시간 미만
							$("#resttime1Div").hide();
							$("#resttime2Div").hide();
							$("#resttime3Div").hide();
						}
						
						// 근무자 시간 계산
						$('input[name^="workerSabun"]').each(function() {
							getUserOvertimeinfo($(this).val());
						});
					}
				}
			});
			
			
			$('#restStarttime1').on('change', function (e, date) {
				// 휴게 종료 시간 구하기
				calcEndTime("1");
				// 근무시간 및 휴게시간 범위 검증
				calcLimitTime();
				// 근무자 시간 계산
				$('input[name^="workerSabun"]').each(function() {
					getUserOvertimeinfo($(this).val());
				});
			});
			$('#restStarttime2').on('change', function (e, date) {
				// 휴게 종료 시간 구하기
				calcEndTime("2");
				// 근무시간 및 휴게시간 범위 검증
				calcLimitTime();
				// 근무자 시간 계산
				$('input[name^="workerSabun"]').each(function() {
					getUserOvertimeinfo($(this).val());
				});
			});
			$('#restStarttime3').on('change', function (e, date) {
				// 휴게 종료 시간 구하기
				calcEndTime("3");
				// 근무시간 및 휴게시간 범위 검증
				calcLimitTime();
				// 근무자 시간 계산
				$('input[name^="workerSabun"]').each(function() {
					getUserOvertimeinfo($(this).val());
				});
			});
			
			$("#term1").change(function() {
				// 휴게 종료 시간 구하기
				calcEndTime("1");
				// 근무시간 및 휴게시간 범위 검증
				calcLimitTime();
				// 근무자 시간 계산
				$('input[name^="workerSabun"]').each(function() {
					getUserOvertimeinfo($(this).val());
				});
			});
			
			$("#term2").change(function() {
				// 휴게 종료 시간 구하기
				calcEndTime("2");
				// 근무시간 및 휴게시간 범위 검증
				calcLimitTime();
				// 근무자 시간 계산
				$('input[name^="workerSabun"]').each(function() {
					getUserOvertimeinfo($(this).val());
				});
			});
			
			$("#term3").change(function() {
				// 휴게 종료 시간 구하기
				calcEndTime("3");
				// 근무시간 및 휴게시간 범위 검증
				calcLimitTime();
				// 근무자 시간 계산
				$('input[name^="workerSabun"]').each(function() {
					getUserOvertimeinfo($(this).val());
				});
			});
			
		});
		
		// 근무시간 및 휴게시간 범위 비교
		function calcLimitTime(){
			var startdate = $("#startdate").val();
			var enddate = $("#enddate").val();
			var restStart1 = $("#restStarttime1").val();
			var restStart2 = $("#restStarttime2").val();
			var restStart3 = $("#restStarttime3").val();
			var restEnd1 = $("#restEndtime1").val();
			var restEnd2 = $("#restEndtime2").val();
			var restEnd3 = $("#restEndtime3").val();
			
			if(startdate != ""){
				var startdateObj = new Date(parseInt(startdate.substring(0,4), 10),parseInt(startdate.substring(5,7), 10)-1,parseInt(startdate.substring(8,10), 10),parseInt(startdate.substring(11,13), 10),parseInt(startdate.substring(14,16), 10),parseInt("00", 10) );
				if(restStart1 != ""){
					var restStartDate1 = new Date(parseInt(restStart1.substring(0,4), 10),parseInt(restStart1.substring(5,7), 10)-1,parseInt(restStart1.substring(8,10), 10),parseInt(restStart1.substring(11,13), 10),parseInt(restStart1.substring(14,16), 10),parseInt("00", 10) );
					if(restStartDate1 < startdateObj) {
						alert("휴게시간은 근무시간을 초과할 수 없습니다.\n다시 선택해주세요.");
						$("#restStarttime1").val("");
						$("#restEndtime1").val("");
						$("#term1").val("0");
						$("#term1").selectpicker('refresh');
					}
				}
				if(restEnd1 != ""){
					var restEndDate1 = new Date(parseInt(restEnd1.substring(0,4), 10),parseInt(restEnd1.substring(5,7), 10)-1,parseInt(restEnd1.substring(8,10), 10),parseInt(restEnd1.substring(11,13), 10),parseInt(restEnd1.substring(14,16), 10),parseInt("00", 10) );
					if(restEndDate1 < startdateObj) {
						alert("휴게시간은 근무시간을 초과할 수 없습니다.\n다시 선택해주세요.");
						$("#restStarttime1").val("");
						$("#restEndtime1").val("");
						$("#term1").val("0");
						$("#term1").selectpicker('refresh');
					}
				}
				if(restStart2 != ""){
					var restStartDate2 = new Date(parseInt(restStart2.substring(0,4), 10),parseInt(restStart2.substring(5,7), 10)-1,parseInt(restStart2.substring(8,10), 10),parseInt(restStart2.substring(11,13), 10),parseInt(restStart2.substring(14,16), 10),parseInt("00", 10) );
					if(restStartDate2 < startdateObj) {
						alert("휴게시간은 근무시간을 초과할 수 없습니다.\n다시 선택해주세요.");
						$("#restStarttime2").val("");
						$("#restEndtime2").val("");
						$("#term2").val("0");
						$("#term2").selectpicker('refresh');
					}
				}
				if(restEnd2 != ""){
					var restEndDate2 = new Date(parseInt(restEnd2.substring(0,4), 10),parseInt(restEnd2.substring(5,7), 10)-1,parseInt(restEnd2.substring(8,10), 10),parseInt(restEnd2.substring(11,13), 10),parseInt(restEnd2.substring(14,16), 10),parseInt("00", 10) );
					if(restEndDate2 < startdateObj) {
						alert("휴게시간은 근무시간을 초과할 수 없습니다.\n다시 선택해주세요.");
						$("#restStarttime2").val("");
						$("#restEndtime2").val("");
						$("#term2").val("0");
						$("#term2").selectpicker('refresh');
					}
				}
				if(restStart3 != ""){
					var restStartDate3 = new Date(parseInt(restStart3.substring(0,4), 10),parseInt(restStart3.substring(5,7), 10)-1,parseInt(restStart3.substring(8,10), 10),parseInt(restStart3.substring(11,13), 10),parseInt(restStart3.substring(14,16), 10),parseInt("00", 10) );
					if(restStartDate3 < startdateObj) {
						alert("휴게시간은 근무시간을 초과할 수 없습니다.\n다시 선택해주세요.");
						$("#restStarttime3").val("");
						$("#restEndtime3").val("");
						$("#term3").val("0");
						$("#term3").selectpicker('refresh');
					}
				}
				if(restEnd3 != ""){
					var restEndDate3 = new Date(parseInt(restEnd3.substring(0,4), 10),parseInt(restEnd3.substring(5,7), 10)-1,parseInt(restEnd3.substring(8,10), 10),parseInt(restEnd3.substring(11,13), 10),parseInt(restEnd3.substring(14,16), 10),parseInt("00", 10) );
					if(restEndDate3 < startdateObj) {
						alert("휴게시간은 근무시간을 초과할 수 없습니다.\n다시 선택해주세요.");
						$("#restStarttime3").val("");
						$("#restEndtime3").val("");
						$("#term3").val("0");
						$("#term3").selectpicker('refresh');
					}
				}
			}
			
			if(enddate != ""){
				var endDateObj = new Date(parseInt(enddate.substring(0,4), 10),parseInt(enddate.substring(5,7), 10)-1,parseInt(enddate.substring(8,10), 10),parseInt(enddate.substring(11,13), 10),parseInt(enddate.substring(14,16), 10),parseInt("00", 10) );
				if(restStart1 != ""){
					var restStartDate1 = new Date(parseInt(restStart1.substring(0,4), 10),parseInt(restStart1.substring(5,7), 10)-1,parseInt(restStart1.substring(8,10), 10),parseInt(restStart1.substring(11,13), 10),parseInt(restStart1.substring(14,16), 10),parseInt("00", 10) );
					if(restStartDate1 > endDateObj) {
						alert("휴게시간은 근무시간을 초과할 수 없습니다.\n다시 선택해주세요.");
						$("#restStarttime1").val("");
						$("#restEndtime1").val("");
						$("#term1").val("0");
						$("#term1").selectpicker('refresh');
					}
				}
				if(restEnd1 != ""){
					var restEndDate1 = new Date(parseInt(restEnd1.substring(0,4), 10),parseInt(restEnd1.substring(5,7), 10)-1,parseInt(restEnd1.substring(8,10), 10),parseInt(restEnd1.substring(11,13), 10),parseInt(restEnd1.substring(14,16), 10),parseInt("00", 10) );
					if(restEndDate1 > endDateObj) {
						alert("휴게시간은 근무시간을 초과할 수 없습니다.\n다시 선택해주세요.");
						$("#restStarttime1").val("");
						$("#restEndtime1").val("");
						$("#term1").val("0");
						$("#term1").selectpicker('refresh');
					}
				}
				if(restStart2 != ""){
					var restStartDate2 = new Date(parseInt(restStart2.substring(0,4), 10),parseInt(restStart2.substring(5,7), 10)-1,parseInt(restStart2.substring(8,10), 10),parseInt(restStart2.substring(11,13), 10),parseInt(restStart2.substring(14,16), 10),parseInt("00", 10) );
					if(restStartDate2 > endDateObj) {
						alert("휴게시간은 근무시간을 초과할 수 없습니다.\n다시 선택해주세요.");
						$("#restStarttime2").val("");
						$("#restEndtime2").val("");
						$("#term2").val("0");
						$("#term2").selectpicker('refresh');
					}
				}
				if(restEnd2 != ""){
					var restEndDate2 = new Date(parseInt(restEnd2.substring(0,4), 10),parseInt(restEnd2.substring(5,7), 10)-1,parseInt(restEnd2.substring(8,10), 10),parseInt(restEnd2.substring(11,13), 10),parseInt(restEnd2.substring(14,16), 10),parseInt("00", 10) );
					if(restEndDate2 > startdate) {
						alert("휴게시간은 근무시간을 초과할 수 없습니다.\n다시 선택해주세요.");
						$("#restStarttime2").val("");
						$("#restEndtime2").val("");
						$("#term2").val("0");
						$("#term2").selectpicker('refresh');
					}
				}
				if(restStart3 != ""){
					var restStartDate3 = new Date(parseInt(restStart3.substring(0,4), 10),parseInt(restStart3.substring(5,7), 10)-1,parseInt(restStart3.substring(8,10), 10),parseInt(restStart3.substring(11,13), 10),parseInt(restStart3.substring(14,16), 10),parseInt("00", 10) );
					if(restStartDate3 > endDateObj) {
						alert("휴게시간은 근무시간을 초과할 수 없습니다.\n다시 선택해주세요.");
						$("#restStarttime3").val("");
						$("#restEndtime3").val("");
						$("#term3").val("0");
						$("#term3").selectpicker('refresh');
					}
				}
				if(restEnd3 != ""){
					var restEndDate3 = new Date(parseInt(restEnd3.substring(0,4), 10),parseInt(restEnd3.substring(5,7), 10)-1,parseInt(restEnd3.substring(8,10), 10),parseInt(restEnd3.substring(11,13), 10),parseInt(restEnd3.substring(14,16), 10),parseInt("00", 10) );
					if(restEndDate3 > endDateObj) {
						alert("휴게시간은 근무시간을 초과할 수 없습니다.\n다시 선택해주세요.");
						$("#restStarttime3").val("");
						$("#restEndtime3").val("");
						$("#term3").val("0");
						$("#term3").selectpicker('refresh');
					}
				}
			}
		}
		
		// 휴게 종료 시간 구하기 및 검증
		function calcEndTime(num) {
			var restStarttime = $("#restStarttime"+num).val();
			if(restStarttime != ""){
				var term = $("#term"+num).val();
				var tmpDate = new Date(parseInt(restStarttime.substring(0,4), 10),
						parseInt(restStarttime.substring(5,7), 10)-1,
						parseInt(restStarttime.substring(8,10), 10),
						parseInt(restStarttime.substring(11,13), 10),
						parseInt(restStarttime.substring(14,16), 10),
						parseInt("00", 10) );
				var d = cfAddMinutes(tmpDate, term);
				var yymmdd = cfLeadingZeros(d.getFullYear(), 4) + "-" + cfLeadingZeros(d.getMonth()+1, 2) + "-"  + cfLeadingZeros(d.getDate(), 2) + " " + cfLeadingZeros(d.getHours(), 2) + ":" + cfLeadingZeros(d.getMinutes(), 2);
				$("#restEndtime"+num).val(yymmdd);
				
				var restStart1 = $("#restStarttime1").val();
				var restStart2 = $("#restStarttime2").val();
				var restStart3 = $("#restStarttime3").val();
				var restEnd1 = $("#restEndtime1").val();
				var restEnd2 = $("#restEndtime2").val();
				var restEnd3 = $("#restEndtime3").val();
				
				if(restStart1 != "" && restEnd1 != "" && restStart2 != "" && restEnd2 != "") {
					var restStartDate1 = new Date(parseInt(restStart1.substring(0,4), 10),parseInt(restStart1.substring(5,7), 10)-1,parseInt(restStart1.substring(8,10), 10),parseInt(restStart1.substring(11,13), 10),parseInt(restStart1.substring(14,16), 10),parseInt("00", 10) );
					var restEndDate1 = new Date(parseInt(restEnd1.substring(0,4), 10),parseInt(restEnd1.substring(5,7), 10)-1,parseInt(restEnd1.substring(8,10), 10),parseInt(restEnd1.substring(11,13), 10),parseInt(restEnd1.substring(14,16), 10),parseInt("00", 10) );
					var restStartDate2 = new Date(parseInt(restStart2.substring(0,4), 10),parseInt(restStart2.substring(5,7), 10)-1,parseInt(restStart2.substring(8,10), 10),parseInt(restStart2.substring(11,13), 10),parseInt(restStart2.substring(14,16), 10),parseInt("00", 10) );
					var restEndDate2 = new Date(parseInt(restEnd2.substring(0,4), 10),parseInt(restEnd2.substring(5,7), 10)-1,parseInt(restEnd2.substring(8,10), 10),parseInt(restEnd2.substring(11,13), 10),parseInt(restEnd2.substring(14,16), 10),parseInt("00", 10) );
					if((restStartDate1 >= restStartDate2 && restStartDate1 < restEndDate2) || (restStartDate2 >= restStartDate1 && restStartDate2 < restEndDate1)){
						alert("휴게시간 (1)과 휴게시간 (2) 시간이 중복됩니다.\n다시 선택해주세요.");
						$("#restStarttime"+num).val("");
						$("#restEndtime"+num).val("");
						$("#term"+num).val("0");
						$("#term"+num).selectpicker('refresh');
					}
				}
				if(restStart1 != "" && restEnd1 != "" && restStart3 != "" && restEnd3 != "") {
					var restStartDate1 = new Date(parseInt(restStart1.substring(0,4), 10),parseInt(restStart1.substring(5,7), 10)-1,parseInt(restStart1.substring(8,10), 10),parseInt(restStart1.substring(11,13), 10),parseInt(restStart1.substring(14,16), 10),parseInt("00", 10) );
					var restEndDate1 = new Date(parseInt(restEnd1.substring(0,4), 10),parseInt(restEnd1.substring(5,7), 10)-1,parseInt(restEnd1.substring(8,10), 10),parseInt(restEnd1.substring(11,13), 10),parseInt(restEnd1.substring(14,16), 10),parseInt("00", 10) );
					var restStartDate3 = new Date(parseInt(restStart3.substring(0,4), 10),parseInt(restStart3.substring(5,7), 10)-1,parseInt(restStart3.substring(8,10), 10),parseInt(restStart3.substring(11,13), 10),parseInt(restStart3.substring(14,16), 10),parseInt("00", 10) );
					var restEndDate3 = new Date(parseInt(restEnd3.substring(0,4), 10),parseInt(restEnd3.substring(5,7), 10)-1,parseInt(restEnd3.substring(8,10), 10),parseInt(restEnd3.substring(11,13), 10),parseInt(restEnd3.substring(14,16), 10),parseInt("00", 10) );
					if((restStartDate1 >= restStartDate3 && restStartDate1 < restEndDate3) || (restStartDate3 >= restStartDate1 && restStartDate3 < restEndDate1)){
						alert("휴게시간 (1)과 휴게시간 (3) 시간이 중복됩니다.\n다시 선택해주세요.");
						$("#restStarttime"+num).val("");
						$("#restEndtime"+num).val("");
						$("#term"+num).val("0");
						$("#term"+num).selectpicker('refresh');
					}
				}
				if(restStart2 != "" && restEnd2 != "" && restStart3 != "" && restEnd3 != "") {
					var restStartDate2 = new Date(parseInt(restStart2.substring(0,4), 10),parseInt(restStart2.substring(5,7), 10)-1,parseInt(restStart2.substring(8,10), 10),parseInt(restStart2.substring(11,13), 10),parseInt(restStart2.substring(14,16), 10),parseInt("00", 10) );
					var restEndDate2 = new Date(parseInt(restEnd2.substring(0,4), 10),parseInt(restEnd2.substring(5,7), 10)-1,parseInt(restEnd2.substring(8,10), 10),parseInt(restEnd2.substring(11,13), 10),parseInt(restEnd2.substring(14,16), 10),parseInt("00", 10) );
					var restStartDate3 = new Date(parseInt(restStart3.substring(0,4), 10),parseInt(restStart3.substring(5,7), 10)-1,parseInt(restStart3.substring(8,10), 10),parseInt(restStart3.substring(11,13), 10),parseInt(restStart3.substring(14,16), 10),parseInt("00", 10) );
					var restEndDate3 = new Date(parseInt(restEnd3.substring(0,4), 10),parseInt(restEnd3.substring(5,7), 10)-1,parseInt(restEnd3.substring(8,10), 10),parseInt(restEnd3.substring(11,13), 10),parseInt(restEnd3.substring(14,16), 10),parseInt("00", 10) );
					if((restStartDate2 >= restStartDate3 && restStartDate2 < restEndDate3) || (restStartDate3 >= restStartDate2 && restStartDate3 < restEndDate2)){
						alert("휴게시간 (2)과 휴게시간 (3) 시간이 중복됩니다.\n다시 선택해주세요.");
						$("#restStarttime"+num).val("");
						$("#restEndtime"+num).val("");
						$("#term"+num).val("0");
						$("#term"+num).selectpicker('refresh');
					}
				}
			}
		}
		
		// 나의 초과근무 정보 가져오기
		function initMyinfo() {
			var d = new Date();
			var yymmdd = cfLeadingZeros(d.getFullYear(), 4) + cfLeadingZeros(d.getMonth()+1, 2) + cfLeadingZeros(d.getDate(), 2);
			var sabun = $("#sabun").val();
			
			var params = {
				auth : "reyon",
				yymmdd : yymmdd,
				sabun : sabun,
			}
			
			var request = $.ajax({
				url: '/hr/getOvertimeInfoAjax.json'
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
						$("#resultTbody1").empty();
						
						var htmlStr = '';
						htmlStr += '<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 0px;">';
						htmlStr += '<table class="table table-bordered">';
						htmlStr += '<colgroup>';
						htmlStr += '<col style="width: 15%;">';
						htmlStr += '<col style="width: 15%;">';
						htmlStr += '<col style="width: 20%;">';
						htmlStr += '<col style="width: 20%;">';
						htmlStr += '<col style="width: 20%;">';
						htmlStr += '<col style="width: 10%;">';
						htmlStr += '</colgroup>';
						htmlStr += '<tbody>';
						htmlStr += '<tr>';
						htmlStr += '<th class="bg-indigo align-center">소속</th>';
						htmlStr += '<th class="bg-indigo align-center">이름</th>';
						htmlStr += '<th class="bg-indigo align-center">1주 최대 연장근로 가능시간</th>';
						htmlStr += '<th class="bg-indigo align-center">초과 근무 시간</th>';
						htmlStr += '<th class="bg-indigo align-center">신청 가능 시간</th>';
						htmlStr += '<th class="bg-indigo align-center">상세내역</th>';
						htmlStr += '</tr>';
						htmlStr += '<tr>';
						htmlStr += '<td class="align-center">'+info.deptName+'</td>';
						htmlStr += '<td class="align-center">'+info.kname+'</td>';
						htmlStr += '<td class="align-center">12시간 (720분)</td>';
						htmlStr += '<td class="align-center">'+info.workingMinute+'분</td>';
						htmlStr += '<td class="align-center">'+info.remainMinute+'분</td>';
						htmlStr += '<td class="align-center">';
						htmlStr += '<button type="button" class="btn bg-red waves-effect btn-xs waves-effect" onClick="javascript:useHistoryOvertime(\''+info.sabun+'\',\''+info.kname+'\',\''+info.deptName+'\',\''+info.posLog+'\');"><i class="material-icons">playlist_add_check</i></button>';
						htmlStr += '</td>';
						htmlStr += '</tr>';
						htmlStr += '</tbody>';
						htmlStr += '</table>';
						htmlStr += '</div>';
						
						$("#workingMinute"+sabun).text("0분");
						$("#restMinute"+sabun).text("0분");
						$("#realWorkingMinute"+sabun).text("0분");
						$("#remainMinute"+sabun).text(info.remainMinute+"분");
						$("#resultTbody1").append(htmlStr);
					}else{
						alert(json.resultMsg);
					}
				}
				, complete : function() {
					cfCloseMagnificPopup();
				}
			});
		}
		
		function initMyList() {
			var d = new Date();
			var yymmdd = cfLeadingZeros(d.getFullYear(), 4) + cfLeadingZeros(d.getMonth()+1, 2) + cfLeadingZeros(d.getDate(), 2);
			var sabun = $("#sabun").val();
			
			var params = {
				auth : "reyon",
				yymmdd : yymmdd,
				sabun : sabun,
			}
			
			var request = $.ajax({
				url: '/hr/getOvertimeListAjax.json'
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
						$("#resultTbody2").empty();
						var htmlStr = '';
						if(list.length == 0){
							htmlStr += '<tr><td class="align-center" colspan="6">초과근무신청 내역이 없습니다.</td></tr>';
						} else {
							for(var i=0; i<list.length; i++){
								htmlStr += '<tr>';
								htmlStr += '<td class="align-center">'+(i+1)+'</td>';
								htmlStr += '<td class="align-center">'+list[i].overtimeGbnTxt+'</td>';
								htmlStr += '<td class="align-center">'+list[i].startdate+' ~ '+list[i].enddate+'</td>';
								htmlStr += '<td class="align-center">'+list[i].workingMinute+'분</td>';
								htmlStr += '<td class="align-center">'+list[i].reason+'</td>';
								htmlStr += '<td class="align-center">'+list[i].gwStatusTxt+'</td>';
								htmlStr += '</tr>';
							}
						}
						$("#resultTbody2").append(htmlStr);
					}else{
						alert(json.resultMsg);
					}
				}
				, complete : function() {
					cfCloseMagnificPopup();
				}
			});
		}
		
		// 근무자의 초과 근무 정보 가져오기
		function getUserOvertimeinfo(sabun) {
			var d = new Date();
			var yymmdd = cfLeadingZeros(d.getFullYear(), 4) + cfLeadingZeros(d.getMonth()+1, 2) + cfLeadingZeros(d.getDate(), 2);
			
			var params = {
				auth : "reyon",
				yymmdd : yymmdd,
				sabun : sabun,
			}
			
			var request = $.ajax({
				url: '/hr/getOvertimeInfoAjax.json'
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
						var remainMinute = info.remainMinute;
						var startdate = $("#startdate").val();
						var enddate = $("#enddate").val();
						if(startdate != "" && enddate != "") {
							var btTime = cfCalcTime(startdate, enddate);
							var sumMinute = parseInt($("#term1").val()) + parseInt($("#term2").val()) + parseInt($("#term3").val());
							$("#workingMinute"+sabun).text(btTime+"분");
							$("#restMinute"+sabun).text(sumMinute+"분");
							$("#realWorkingMinute"+sabun).text((btTime-sumMinute)+"분");
							$("#remainMinute"+sabun).text((remainMinute-btTime+sumMinute)+"분");
						} else {
							$("#workingMinute"+sabun).text("0분");
							$("#restMinute"+sabun).text("0분");
							$("#realWorkingMinute"+sabun).text("0분");
							$("#remainMinute"+sabun).text(remainMinute+"분");
						}
					}else{
						alert(json.resultMsg);
					}
				}
				, complete : function() {
					cfCloseMagnificPopup();
				}
			});
		}
		
		// 근무자 정보 가져오기
		function initUserinfo() {
			var params = {
				auth : "reyon",
			}
			
			var request = $.ajax({
				url: '/hr/getUserinfoListAjax.json'
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
					if (json.resultCode == 0) {
						var list = json.list;
						
						var options = {
							data : list,
							getValue : "kname",
							template : {
								type : "custom",
								method : function(value, item) {
									return value + " " + item.posLog + " - " + item.deptName;
								}
							},
							list : {
								match : {
									enabled : true
								},
								onChooseEvent : function() {
									var deptName = $("#searchName").getSelectedItemData().deptName;
									var sabun = $("#searchName").getSelectedItemData().sabun;
									var kname = $("#searchName").getSelectedItemData().kname;
									var posLog = $("#searchName").getSelectedItemData().posLog;
									
									$("#searchName").val("");
									if ($("#workerTr" + sabun).length > 0) {
										alert("이미 등록된 근무자 입니다.");
									} else {
										if ($("#workerTr0").length > 0) {
											$("#workerTr0").remove();
										}
										var tag = '<tr id="workerTr'+sabun+'">';
										tag += '<td class="align-center">';
										tag += deptName;
										tag += '<input type="hidden" name="workerDeptName[]" value="'+deptName+'">';
										tag += '</td>';
										tag += '<td class="align-center">';
										tag += kname;
										tag += '<input type="hidden" name="workerSabun[]" value="'+sabun+'">';
										tag += '<input type="hidden" name="workerKname[]" value="'+kname+'">';
										tag += '</td>';
										tag += '<td class="align-center">';
										tag += posLog;
										tag += '<input type="hidden" name="workerPosLog[]" value="'+posLog+'">';
										tag += '</td>';
										tag += '<td class="align-center" id="workingMinute'+sabun+'">';
										tag += '0분';
										tag += '</td>';
										tag += '<td class="align-center" id="restMinute'+sabun+'">';
										tag += '0분';
										tag += '</td>';
										tag += '<td class="align-center" id="realWorkingMinute'+sabun+'">';
										tag += '0분';
										tag += '</td>';
										tag += '<td class="align-center" id="remainMinute'+sabun+'">';
										tag += '0분';
										tag += '</td>';
										tag += '<td class="align-center">';
										tag += '<button type="button" class="btn bg-red btn-sm waves-effect" onclick="javascript:delWorkerTr(\'' + sabun + '\');">&nbsp;&nbsp;삭제&nbsp;&nbsp;</button>';
										tag += '</td>';
										tag += '</tr>';
										$("#resultTbody3").append(tag);
										
										getUserOvertimeinfo(sabun);
									}
								},
							}
						};
						$("#searchName").easyAutocomplete(options);
					} else {
						alert(json.resultMsg);
					}
				}
				, complete : function() {
					cfCloseMagnificPopup();
				}
			});
		}
		
		// 근무자 삭제
		function delWorkerTr(name) {
			$("#workerTr" + name).remove();
			
			if ($("#resultTbody3 tr").length < 1) {
				var tag = '<tr id="workerTr0">';
				tag += '<td class="align-center" colspan="8" >';
				tag += '근무자를 추가해 주세요.';
				tag += '</td>';
				tag += '</tr>';
				$("#resultTbody3").append(tag);
			}
		}
		
		// 결재 상신
		function goApproval() {
			var sabun = $("#sabun").val();
			var kname = $("#kname").val();
			var deptName = $("#deptName").val();
			var posLog = $("#posLog").val();
			var overtimeGbn = $("input[name=overtimeGbn]:checked").val();
			var overtimeGbnTxt = $("#overtimeGbnTxt"+overtimeGbn).text();
			var startdate = $("#startdate").val();
			var enddate = $("#enddate").val();
			var restStarttime1 = $("#restStarttime1").val();
			var restEndtime1 = $("#restEndtime1").val();
			var restStarttime2 = $("#restStarttime2").val();
			var restEndtime2 = $("#restEndtime2").val();
			var restStarttime3 = $("#restStarttime3").val();
			var restEndtime3 = $("#restEndtime3").val();
			var reason = $("#reason").val();
			var evidence = $("#evidence").val();
			var workerDeptNameArr = "";
			var workerSabunArr = "";
			var workerKnameArr = "";
			var workerPosLogArr = "";
			var realWorkingMinute = "";
			$('input[name^="workerDeptName"]').each(function() {
				workerDeptNameArr += $(this).val() + ",";
			});
			$('input[name^="workerSabun"]').each(function() {
				workerSabunArr += $(this).val() + ",";
			});
			$('input[name^="workerKname"]').each(function() {
				workerKnameArr += $(this).val() + ",";
			});
			$('input[name^="workerPosLog"]').each(function() {
				workerPosLogArr += $(this).val() + ",";
			});
			
			if (sabun == "") {
				alert("사용자 정보가 없습니다.");
				return;
			}
			if (overtimeGbn == "") {
				alert("분류를 선택해주세요.");
				return;
			}
			if (startdate == "") {
				alert("근무 시작일자를 선택해주세요.");
				return;
			}
			if (enddate == "") {
				alert("근무 종료일자를 선택해주세요.");
				return;
			}
			if (workerSabunArr == "") {
				alert("근무자를 입력해주세요.");
				return;
			}
			if (reason == "") {
				alert("사유를 입력해주세요.");
				return;
			}
			
			var isCheckFail = true;
			// 근무자 시간 계산
			$('input[name^="workerSabun"]').each(function( idx, workerSabunVal ) {
				// 검증용 변수
				var workingMinute = $("#workingMinute"+workerSabunVal.value).text().slice(0, -1);
				var restMinute = $("#restMinute"+workerSabunVal.value).text().slice(0, -1);
				var remainMinute = $("#remainMinute"+workerSabunVal.value).text().slice(0, -1);
				
				if(idx == 0){
					realWorkingMinute = workingMinute - restMinute;
				}
				
				if (workingMinute >= 240 && workingMinute < 480) {
					// 4시간 ~ 8시간
					if(restMinute < 30) {
						alert("4시간 이상 근무 시 휴게시간은 30분이상 필수로 선택 해야합니다.");
						isCheckFail = false;
						return false;
					}
				}
				if (workingMinute >= 480 && workingMinute < 720) {
					// 8시간 ~ 12시간
					if(restMinute < 60) {
						alert("8시간 이상 근무 시 휴게시간은 60분이상 필수로 선택 해야합니다.");
						isCheckFail = false;
						return false;
					}
				}
				if (workingMinute >= 720) {
					// 12시간 이상
					if(restMinute < 90) {
						alert("12시간 이상 근무 시 휴게시간은 90분이상 필수로 선택 해야합니다.");
						isCheckFail = false;
						return false;
					}
				}
				if (remainMinute.indexOf("-") != -1) {
					alert("근무자의 초과근무 가능시간을 초과하였습니다.\n[신청 후 잔여시간]을 [0분] 이상으로 신청해주세요.");
					isCheckFail = false;
					return false;
				}
			});
			
			if (isCheckFail) {
				var params = {
					auth : "reyon",
					type : "MIS004",
					sabun : sabun,
					kname : kname,
					deptName : deptName,
					posLog : posLog,
					overtimeGbn : overtimeGbn,
					overtimeGbnTxt : overtimeGbnTxt,
					startdate : startdate,
					enddate : enddate,
					restStarttime1 : restStarttime1,
					restEndtime1 : restEndtime1,
					restStarttime2 : restStarttime2,
					restEndtime2 : restEndtime2,
					restStarttime3 : restStarttime3,
					restEndtime3 : restEndtime3,
					reason : reason,
					evidence : evidence,
					workerDeptNameArr : workerDeptNameArr,
					workerSabunArr : workerSabunArr,
					workerKnameArr : workerKnameArr,
					workerPosLogArr : workerPosLogArr,
					workingMinute : realWorkingMinute,
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
							console.log(json);
							var url = "http://rygw.reyonpharm.co.kr/index.aspx?user_num=" + sabun + "&appro_key=" + json.approKey + "&surl=approval/appro_form_mis.aspx";
							window.open(url,"_blank");
							swal({
						        title: "초과근무신청서 작성 완료",
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
		}
		
		// 초과 근무 사용 내역
		function useHistoryOvertime(sabun, kname, deptName, posLog){
			$("#sabunTxt").text(sabun);
			$("#knameTxt").text(kname);
			$("#deptNameTxt").text(deptName);
			$("#posLogTxt").text(posLog);
			$("#useHistoryModal").modal("show");
		}
		
		// 금주 월요일 가져오기
		function getFirstday() {
			var curr = new Date;
			curr.setHours(0,0,0,0);
			var first = curr.getDate() - curr.getDay() + 1;
			var firstday = new Date(curr.setDate(first));
			return firstday;
		}
		
		// 차주 일요일 가져오기
		function getLastday() {
			var curr = new Date;
			curr.setHours(23,59,59,0);
			var last = curr.getDate() - curr.getDay() + 7;
			var lastday = new Date(curr.setDate(last));
			return lastday;
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
							초과근무신청서<small>하단 양식에 맞게 작성 후 전자결재 상신 버튼을 눌러주세요.</small>
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
						<h2 id="resultTitle1">나의 초과근무 정보 (금주)<small>초과근무 신청 전 나의 초과근무 정보를 확인해주세요.</small></h2>
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
							근무 구분<small>근무 구분을 선택해주세요.</small>
						</h2>
					</div>
					<div class="body">
						<div class="demo-radio-button">
							<input name="overtimeGbn" type="radio" id="overtimeGbn1" class="with-gap radio-col-indigo" value="1" checked="checked" /><label for="overtimeGbn1" id="overtimeGbnTxt1">연장근로 (19시를 초과하는 시간까지의 근로)</label> 
							<input name="overtimeGbn" type="radio" id="overtimeGbn2" class="with-gap radio-col-indigo" value="2" /><label for="overtimeGbn2" id="overtimeGbnTxt2">휴일근로 (취업규칙 제26조에 따른 유급휴일의 근로)</label>
							<input name="overtimeGbn" type="radio" id="overtimeGbn3" class="with-gap radio-col-indigo" value="3" /><label for="overtimeGbn3" id="overtimeGbnTxt3">야간근로 (22시 ~ 06시 사이의 근로)</label> 
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
							일자/시간 <small>시작일시와 종료일시를 선택해주세요.</small>
						</h2>
					</div>
					<div class="body" style="padding-bottom: 0px;">
					
						<form class="form-horizontal">
							<div class="row clearfix">
								<div class="col-sm-2 form-control-label">
									<label for="startdate">근무시간</label>
								</div>
								<div class="col-sm-3">
									<div class="form-group">
										<div class="form-line">
											<input type="text" id="startdate" class="form-control align-center" placeholder="근무 시작일시">
										</div>
									</div>
								</div>
								<div class="col-sm-2">
									<div class="align-center">부터</div>
								</div>
								<div class="col-sm-3">
									<div class="form-group">
										<div class="form-line">
											<input type="text" id="enddate" class="form-control align-center" placeholder="근무 종료일시">
										</div>
									</div>
								</div>
								<div class="col-sm-2">
									<div class="align-center">까지</div>
								</div>
							</div>
							
							<div class="row clearfix" id="resttime1Div" style="display:none;">
								<div class="col-sm-2 form-control-label">
									<label for="restStarttime1">휴게시간 (1)</label>
								</div>
								<div class="col-sm-3">
									<div class="form-group">
										<div class="form-line">
											<input type="text" id="restStarttime1" class="form-control align-center" placeholder="휴게시간 (1) 시작일시">
											<input type="hidden" id="restEndtime1">
										</div>
									</div>
								</div>
								<div class="col-sm-2">
									<div class="align-center">부터</div>
								</div>
								<div class="col-sm-3">
									<div class="form-group">
										<select id="term1" class="form-control show-tick" data-show-subtext="true">
											<option value="0"> == 선택 === </option>
											<option value="30">30분</option>
											<option value="60">60분</option>
	                                    </select>
									</div>
								</div>
								<div class="col-sm-2">
									<div class="align-center">동안</div>
								</div>
							</div>
							
							<div class="row clearfix" id="resttime2Div" style="display:none;">
								<div class="col-sm-2 form-control-label">
									<label for="restStarttime2">휴게시간 (2)</label>
								</div>
								<div class="col-sm-3">
									<div class="form-group">
										<div class="form-line">
											<input type="text" id="restStarttime2" class="form-control align-center" placeholder="휴게시간 (2) 시작일시">
											<input type="hidden" id="restEndtime2">
										</div>
									</div>
								</div>
								<div class="col-sm-2">
									<div class="align-center">부터</div>
								</div>
								<div class="col-sm-3">
									<div class="form-group">
										<select id="term2" class="form-control show-tick" data-show-subtext="true">
											<option value="0"> == 선택 === </option>
											<option value="30">30분</option>
											<option value="60">60분</option>
	                                    </select>
									</div>
								</div>
								<div class="col-sm-2">
									<div class="align-center">동안</div>
								</div>
							</div>
							
							<div class="row clearfix" id="resttime3Div" style="display:none;">
								<div class="col-sm-2 form-control-label">
									<label for="restStarttime3">휴게시간 (3)</label>
								</div>
								<div class="col-sm-3">
									<div class="form-group">
										<div class="form-line">
											<input type="text" id="restStarttime3" class="form-control align-center" placeholder="휴게시간 (3) 시작일시">
											<input type="hidden" id="restEndtime3">
										</div>
									</div>
								</div>
								<div class="col-sm-2">
									<div class="align-center">부터</div>
								</div>
								<div class="col-sm-3">
									<div class="form-group">
										<select id="term3" class="form-control show-tick" data-show-subtext="true">
											<option value="0"> == 선택 === </option>
											<option value="30">30분</option>
											<option value="60">60분</option>
	                                    </select>
									</div>
								</div>
								<div class="col-sm-2">
									<div class="align-center">동안</div>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row clearfix">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="card">
					<div class="header">
						<h2>
							근무자 <small>근무자를 입력해주세요.</small>
						</h2>
					</div>
					<div class="body">
						<div class="row clearfix">
							<div class="col-sm-12">
								<div class="form-group form-float">
									<div class="form-line">
										<input type="text" class="form-control" id="searchName" maxlength="20"> 
										<label class="form-label">근무자 이름 입력</label>
									</div>
								</div>
							</div>
						</div>
						<div class="row clearfix">
							<div class="col-sm-12">
								<table class="table table-bordered">
									<colgroup>
										<col style="width: 15%;">
										<col style="width: 15%;">
										<col style="width: 15%;">
										<col style="width: 11%;">
										<col style="width: 11%;">
										<col style="width: 11%;">
										<col style="width: 12%;">
										<col style="width: 10%;">
									</colgroup>
									<thead>
										<tr>
											<th class="bg-indigo align-center">소속</th>
											<th class="bg-indigo align-center">이름</th>
											<th class="bg-indigo align-center">직위</th>
											<th class="bg-indigo align-center">신청시간</th>
											<th class="bg-indigo align-center">휴게시간</th>
											<th class="bg-indigo align-center">실근무시간</th>
											<th class="bg-indigo align-center">신청 후 잔여시간</th>
											<th class="bg-indigo align-center">근무자 삭제</th>
										</tr>
									</thead>
									<tbody id="resultTbody3">
										<tr id="workerTr${info.sabun }">
											<td class="align-center">${info.deptName }<input type="hidden" name="workerDeptName[]" value="${info.deptName }"></td>
											<td class="align-center">${info.kname }<input type="hidden" name="workerSabun[]" value="${info.sabun }"><input type="hidden" name="workerKname[]" value="${info.kname }"></td>
											<td class="align-center">${info.posLog }<input type="hidden" name="workerPosLog[]" value="${info.posLog }"></td>
											<td class="align-center" id="workingMinute${info.sabun }">0분</td>
											<td class="align-center" id="restMinute${info.sabun }">0분</td>
											<td class="align-center" id="realWorkingMinute${info.sabun }">0분</td>
											<td class="align-center" id="remainMinute${info.sabun }">0분</td>
											<td class="align-center"><button type="button" class="btn bg-red btn-sm waves-effect" onClick="javascript:delWorkerTr('${info.sabun }');">&nbsp;&nbsp;삭제&nbsp;&nbsp;</button></td>
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
							<div class="col-sm-12">
								<div class="form-group form-float">
									<div class="form-line">
										<input type="text" class="form-control" id="reason" maxlength="200"> 
										<label class="form-label">사유</label>
									</div>
								</div>
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
							증빙자료 <small>증빙자료를 입력해주세요.</small>
						</h2>
					</div>
					<div class="body">
						<div class="row clearfix">
							<div class="col-sm-12">
								<div class="form-group form-float">
									<div class="form-line">
										<input type="text" class="form-control" id="evidence" maxlength="200">
										<label class="form-label">증빙자료</label>
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
		<div class="modal fade" id="useHistoryModal" tabindex="-1" role="dialog">
               <div class="modal-dialog modal-lg" role="document">
                   <div class="modal-content">
                       <div class="modal-header">
                           <h4 class="modal-title">초과근무신청 내역</h4>
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
										<td class="text-center" id="sabunTxt"></td>
										<td class="text-center" id="deptNameTxt"></td>
										<td class="text-center" id="posLogTxt"></td>
										<td class="text-center" id="knameTxt"></td>
									</tr>
								</tbody>
							</table>
							
							<table class="table table-bordered">
								<colgroup>
									<col style="width: 8%;">
									<col style="width: 10%;">
									<col style="width: 24%;">
									<col style="width: 10%;">
									<col style="width: 40%;">
									<col style="width: 8%;">
								</colgroup>
								<thead>
									<tr class="active">
										<th class="bg-indigo align-center">순번</th>
										<th class="bg-indigo align-center">분류</th>
										<th class="bg-indigo align-center">일자/시간</th>
										<th class="bg-indigo align-center">근무시간</th>
										<th class="bg-indigo align-center">사유</th>
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