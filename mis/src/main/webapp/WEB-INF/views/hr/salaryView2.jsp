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
	<!-- Custom Js -->
	<script src="/js/admin.js"></script>
	<!-- common function -->
	<script type="text/javascript" src="/js/common.js"></script>
	<script type="text/javascript">
		
		var GLOBAL_PAY_TXT = "급여";
		
		// 지급 배열
		var JG_TITLE;
		var JG_VALUE;
		// 공제 배열
		var GJ_TITLE;
		var GJ_VALUE;
	
		$(function() {
			// 진입 시 메뉴 선택
			cfLNBMenuSelect("lnbHr");
			cfLNBChildMenuSelect("lnbSalaryView");
			
			// 지급 구분 불러오기
			//getSalaryTotalPayGb();
			
			// 지급 구분 변경 시
			$("#payGb").change(function(){
				$("#resultDiv").hide();
				var payGb = $('#payGb option:selected').val();
				if (payGb == "1") {
					GLOBAL_PAY_TXT = "급여";
				} else if (payGb == "2") {
					GLOBAL_PAY_TXT = "상여";
				} else if (payGb == "3") {
					GLOBAL_PAY_TXT = "연월차정기수당";
				} else if (payGb == "4") {
					GLOBAL_PAY_TXT = "특별상여";
				}
				getSalaryTotalPayDate();
			});
			
			// 조회 연월 변경 시
			$("#yymm").change(function(){
				$("#resultDiv").hide();
			});
		});
		
		// 지급 구분 불러오기
		function getSalaryTotalPayGb() {
			var params = {
				auth : "reyon"
			}
			
			var request = $.ajax({
				url: '/hr/salaryTotalPayGbAjax.json'
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
						$("#payGb").empty();
						var htmlStr = '';
						for(var i=0; i<list.length; i++){
							var payGb = list[i].payGb;
							var payTxt = "";
							if (payGb == "1") {
								payTxt = "급여";
							} else if (payGb == "2") {
								payTxt = "상여";
							} else if (payGb == "3") {
								payTxt = "연월차정기수당";
							} else if (payGb == "4") {
								payTxt = "특별상여";
							}
							htmlStr += '<option value="'+payGb+'">'+payTxt+'</option>';
						}
						$("#payGb").append(htmlStr);
						$('#payGb').selectpicker('refresh');
						getSalaryTotalPayDate();
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
		
		// 조회 연월 불러오기
		function getSalaryTotalPayDate() {
			var payGb = $("#payGb").val();
			
			if (payGb == "") {
				alert("지급 구분을 선택해 주세요.");
				return;
			}
			
			var params = {
				auth : "reyon",
				payGb : payGb,
			}
			
			var request = $.ajax({
				url: '/hr/salaryTotalPayDateAjax.json'
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
						$("#yymm").empty();
						var htmlStr = '';
						for(var i=0; i<list.length; i++){
							var yymm = list[i].yymm;
							var payDate = list[i].payDate;
							htmlStr += '<option value="'+yymm+'" data-subtext="('+payDate.substring(0,4)+'년 '+payDate.substring(4,6)+'월 '+payDate.substring(6,8)+'일 지급)">'+yymm.substring(0,4)+'년 '+yymm.substring(4,6)+'월 '+GLOBAL_PAY_TXT+'명세서</option>';
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
			var payGb = $("#payGb").val();
			var yymm = $("#yymm").val();
			
			if (payGb == "") {
				alert("지급 구분을 선택해 주세요.");
				return;
			}
			
			if (yymm == "") {
				alert("조회 연월을 선택해 주세요.");
				return;
			}
			
			$("#resultDiv").hide();
			var params = {
				auth : "reyon",
				payGb : payGb,
				yymm : yymm,
			}
			
			var request = $.ajax({
				url: '/hr/salaryViewAjax.json'
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
						$("#resultTbody").empty();
						$("#resultTitle").text(info.yymm.substring(0, 4) + "년 " + info.yymm.substring(4, 6) + "월 "+GLOBAL_PAY_TXT+" 명세서");
						
						buildJgArr(info);
						buildGjArr(info);
						var arrMaxLength = 0;
						if (JG_TITLE.length > GJ_TITLE.length) {
							arrMaxLength = JG_TITLE.length;
						} else {
							arrMaxLength = GJ_TITLE.length;
						}
						
						var htmlStr = '';
						htmlStr += '<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 0px;">';
						htmlStr += '<table class="table table-bordered" style="min-width: 830px; margin-bottom: 10px;">';
						htmlStr += '<colgroup>';
						htmlStr += '<col style="width: 12.5%;">';
						htmlStr += '<col style="width: 12.5%;">';
						htmlStr += '<col style="width: 12.5%;">';
						htmlStr += '<col style="width: 12.5%;">';
						htmlStr += '<col style="width: 12.5%;">';
						htmlStr += '<col style="width: 12.5%;">';
						htmlStr += '<col style="width: 12.5%;">';
						htmlStr += '<col style="width: 12.5%;">';
						htmlStr += '</colgroup>';
						htmlStr += '<tbody>';
						htmlStr += '<tr>';
						htmlStr += '<th class="bg-indigo align-center">소속</th>';
						htmlStr += '<td class="align-center">'+info.deptName+'</td>';
						htmlStr += '<th class="bg-indigo align-center">직급</th>';
						htmlStr += '<td class="align-center">'+info.grade+'</td>';
						htmlStr += '<th class="bg-indigo align-center">성명</th>';
						htmlStr += '<td class="align-center">'+info.kname+'</td>';
						htmlStr += '<th class="bg-indigo align-center">지급일자</th>';
						htmlStr += '<td class="align-center">'+info.payDate.substring(0, 4)+'-'+info.payDate.substring(4, 6)+'-'+info.payDate.substring(6, 8)+'</td>';
						htmlStr += '</tr>';
						htmlStr += '</tbody>';
						htmlStr += '</table>';
						htmlStr += '</div>';
						
						htmlStr += '<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 0px;">';
						htmlStr += '<table class="table table-bordered" style="min-width: 830px; margin-bottom: 10px;">';
						htmlStr += '<colgroup>';
						htmlStr += '<col style="width: 12.5%;">';
						htmlStr += '<col style="width: 12.5%;">';
						htmlStr += '<col style="width: 12.5%;">';
						htmlStr += '<col style="width: 12.5%;">';
						htmlStr += '<col style="width: 12.5%;">';
						htmlStr += '<col style="width: 12.5%;">';
						htmlStr += '</colgroup>';
						htmlStr += '<thead>';
						htmlStr += '<tr class="bg-indigo">';
						htmlStr += '<th class="align-center">지급총액</th>';
						htmlStr += '<th class="align-center">공제액계</th>';
						htmlStr += '<th class="align-center">차인지급액</th>';
						htmlStr += '<th class="align-center">연장 근로시간</th>';
						htmlStr += '<th class="align-center">휴일 근로시간</th>';
						htmlStr += '<th class="align-center">급(상)여율</th>';
						htmlStr += '</tr>';
						htmlStr += '</thead>';
						htmlStr += '<tbody>';
						htmlStr += '<tr>';
						htmlStr += '<td class="align-center">'+info.payTotal+'</td>';
						htmlStr += '<td class="align-center">'+info.subTotal+'</td>';
						htmlStr += '<td class="align-center">'+info.payRemain+'</td>';
						htmlStr += '<td class="align-center">'+info.otNight+'</td>';
						htmlStr += '<td class="align-center">'+info.otSunday+'</td>';
						htmlStr += '<td class="align-center">'+info.bonusRate+'</td>';
						htmlStr += '</tr>';
						htmlStr += '</tbody>';
						htmlStr += '</table>';
						htmlStr += '</div>';
						
						htmlStr += '<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 0px;">';
						htmlStr += '<table class="table table-bordered" style="min-width: 830px; margin-bottom: 10px;">';
						htmlStr += '<colgroup>';
						htmlStr += '<col style="width: 20%;">';
						htmlStr += '<col style="width: 30%;">';
						htmlStr += '<col style="width: 20%;">';
						htmlStr += '<col style="width: 30%;">';
						htmlStr += '</colgroup>';
						htmlStr += '<tbody>';
						htmlStr += '<tr class="bg-indigo">';
						htmlStr += '<th class="align-center">지급내역</th>';
						htmlStr += '<th class="align-center">금액</th>';
						htmlStr += '<th class="align-center">공제내역</th>';
						htmlStr += '<th class="align-center">금액</th>';
						htmlStr += '</tr>';
						for (var i = 0; i < arrMaxLength; i++) {
							htmlStr += '<tr>';
							if(JG_TITLE.length > i){
								htmlStr += '<th class="bg-indigo align-center">'+JG_TITLE[i]+'</th>';
								htmlStr += '<td class="align-right">'+JG_VALUE[i]+'</td>';
							} else {
								htmlStr += '<th class="bg-indigo align-center">&nbsp;</th>';
								htmlStr += '<td class="align-right">&nbsp;</td>';
							}
							if(GJ_TITLE.length > i){
								htmlStr += '<th class="bg-indigo align-center">'+GJ_TITLE[i]+'</th>';
								htmlStr += '<td class="align-right">'+GJ_VALUE[i]+'</td>';
							} else {
								htmlStr += '<th class="bg-indigo align-center">&nbsp;</th>';
								htmlStr += '<td class="align-right">&nbsp;</td>';
							}
							htmlStr += '</tr>';
						}
						htmlStr += '<tr>';
						htmlStr += '<th class="bg-indigo align-center">지급총액</th>';
						htmlStr += '<td class="align-right">'+info.payTotal+'</td>';
						htmlStr += '<th class="bg-indigo align-center">공제총액</th>';
						htmlStr += '<td class="align-right">'+info.subTotal+'</td>';
						htmlStr += '</tr>';
						htmlStr += '</tbody>';
						htmlStr += '</table>';
						htmlStr += '</div>';
						
						htmlStr += '<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 align-right" style="min-width: 830px;">';
						htmlStr += '<p><b>귀하의 노고에 감사드립니다. 이연제약(주)</b></p>';
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
		
		// 지급 배열 만들기
		function buildJgArr(info){
			JG_TITLE = [];
			JG_VALUE = [];
			if (info.aa01 != 0) {
				JG_TITLE.push("기본급");
				JG_VALUE.push(info.aa01);
			}
			if (info.aa02 != 0) {
				JG_TITLE.push("직책수당");
				JG_VALUE.push(info.aa02);
			}
			if (info.aa03 != 0) {
				JG_TITLE.push("업무수당");
				JG_VALUE.push(info.aa03);
			}
			if (info.aa04 != 0) {
				JG_TITLE.push("조정수당");
				JG_VALUE.push(info.aa04);
			}
			if (info.aa05 != 0) {
				JG_TITLE.push("면허수당");
				JG_VALUE.push(info.aa05);
			}
			if (info.aa06 != 0) {
				JG_TITLE.push("자격수당");
				JG_VALUE.push(info.aa06);
			}
			if (info.aa07 != 0) {
				JG_TITLE.push("연장근로수당");
				JG_VALUE.push(info.aa07);
			}
			if (info.aa08 != 0) {
				JG_TITLE.push("휴일근로수당");
				JG_VALUE.push(info.aa08);
			}
			if (info.aa09 != 0) {
				JG_TITLE.push("연차수당");
				JG_VALUE.push(info.aa09);
			}
			if (info.aa10 != 0) {
				JG_TITLE.push("소급");
				JG_VALUE.push(info.aa10);
			}
			if (info.aa11 != 0) {
				JG_TITLE.push("기타지급");
				JG_VALUE.push(info.aa11);
			}
			if (info.aa12 != 0) {
				JG_TITLE.push("학자금");
				JG_VALUE.push(info.aa12);
			}
			if (info.aa13 != 0) {
				JG_TITLE.push("식대");
				JG_VALUE.push(info.aa13);
			}
		}
		
		// 공제 배열 만들기
		function buildGjArr(info){
			GJ_TITLE = [];
			GJ_VALUE = [];
			
			if (info.cc01 != 0) {
				GJ_TITLE.push("소득세");
				GJ_VALUE.push(info.cc01);
			}
			if (info.cc02 != 0) {
				GJ_TITLE.push("주민세");
				GJ_VALUE.push(info.cc02);
			}
			if (info.cc03 != 0) {
				GJ_TITLE.push("농특세");
				GJ_VALUE.push(info.cc03);
			}
			if (info.cc04 != 0) {
				GJ_TITLE.push("연말정산");
				GJ_VALUE.push(info.cc04);
			}
			if (info.bb01 != 0) {
				GJ_TITLE.push("국민연금");
				GJ_VALUE.push(info.bb01);
			}
			if (info.bb02 != 0) {
				GJ_TITLE.push("건강보험");
				GJ_VALUE.push(info.bb02);
			}
			if (info.bb03 != 0) {
				GJ_TITLE.push("장기요양보험");
				GJ_VALUE.push(info.bb03);
			}
			if (info.bb04 != 0) {
				GJ_TITLE.push("고용보험");
				GJ_VALUE.push(info.bb04);
			}
			if (info.bb06 != 0) {
				GJ_TITLE.push("사우회비");
				GJ_VALUE.push(info.bb06);
			}
			if (info.bb07 != 0) {
				GJ_TITLE.push("대출이자");
				GJ_VALUE.push(info.bb07);
			}
			if (info.bb08 != 0) {
				GJ_TITLE.push("지급보류");
				GJ_VALUE.push(info.bb08);
			}
			if (info.bb09 != 0) {
				GJ_TITLE.push("채권압류");
				GJ_VALUE.push(info.bb09);
			}
			if (info.bb10 != 0) {
				GJ_TITLE.push("기타공제1");
				GJ_VALUE.push(info.bb10);
			}
			if (info.bb11 != 0) {
				GJ_TITLE.push("기타공제2");
				GJ_VALUE.push(info.bb11);
			}
		}
		
		// 엑셀 다운로드
		function excelDownload() {
			var form = document.createElement("form");
			form.name = "downForm";
			form.id = "downForm";
			form.method = "POST";
			form.action = "/hr/salaryExcelDownload.do";
			var payGb = document.createElement("input");
			payGb.type = "hidden";
			payGb.name = "payGb";
			payGb.value = $("#payGb").val();
			var yymm = document.createElement("input");
			yymm.type = "hidden";
			yymm.name = "yymm";
			yymm.value = $("#yymm").val();
			$(form).append(payGb);
			$(form).append(yymm);
			$(".content").append(form);
			form.submit();
			$("#downForm").remove();
		}
		
		// 출력
		function printTable() {
			var agent = navigator.userAgent.toLowerCase();
			if ( (navigator.appName == 'Netscape' && agent.indexOf('trident') != -1) || (agent.indexOf("msie") != -1)) {
			     printTableIE();
			} else {
				printTableNotIE();
			}
		}
		
		// 출력 (IE 아닌 브라우저)
		function printTableNotIE() {
			var html = document.querySelector('html');
			var printContents = document.getElementById("resultDiv").innerHTML;
			var printDiv = document.createElement("DIV");
			html.appendChild(printDiv);
			printDiv.innerHTML = printContents;
			document.body.style.display = 'none';
			window.print();
			document.body.style.display = 'block';
			printDiv.style.display = 'none';
		}
		
		// 출력 (IE 계열)
		function printTableIE() {
			var Contractor = $("#resultDiv").html();
			var printWindow = window.open("", "", "location=1,status=1,scrollbars=1,width=900,height=650");
			printWindow.document.write('<!DOCTYPE html><html><head>');
			printWindow.document.write('<link rel="stylesheet" href="/plugins/bootstrap/css/bootstrap.css">');
			printWindow.document.write('<link rel="stylesheet" href="/plugins/bootstrap-select/css/bootstrap-select.css">');
			printWindow.document.write('<link rel="stylesheet" href="/plugins/node-waves/waves.css">');
			printWindow.document.write('<link rel="stylesheet" href="/plugins/animate-css/animate.css">');
			printWindow.document.write('<link rel="stylesheet" href="/plugins/morrisjs/morris.css">');
			printWindow.document.write('<link rel="stylesheet" href="/css/style.css">');
			printWindow.document.write('<link rel="stylesheet" href="/css/themes/all-themes.css">');
			printWindow.document.write('<link href="https://fonts.googleapis.com/css?family=Lato:400,300,300italic,400italic,600,600italic,700,700italic,800,800italic" rel="stylesheet">');
			printWindow.document.write('<link href="https://fonts.googleapis.com/earlyaccess/nanumgothic.css" rel="stylesheet">');
			printWindow.document.write('<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">');
			printWindow.document.write('<style type="text/css">@media print{.no-print, .no-print, .header-dropdown *{display: none !important;}</style>');
			printWindow.document.write('</head><body onLoad="alert(\'프린터 설정에서 원고방향을 가로로 설정하여 출력하세요.\');">');
			/* printWindow.document.write('<div style="text-align:center;">');
			printWindow.document.write('<input type="button" id="btnPrint" value="출력" class="no-print" style="width:100px; margin-top:10px; margin-bottom:10px;" onclick="window.print();" />');
			printWindow.document.write('<input type="button" id="btnCancel" value="닫기" class="no-print"  style="width:100px; margin-top:10px; margin-bottom:10px;" onclick="window.close();" />');
			printWindow.document.write('</div>'); */
			printWindow.document.write(Contractor);
			printWindow.document.write('</body></html>');
			printWindow.document.close();
			printWindow.focus();
			printWindow.print();
			printWindow.close();
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
					<h2>준비중</h2>
				</div>
				<!-- title end -->
				
				<div class="row clearfix">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div class="card">
							<div class="header">
								<h2>준비중인 메뉴입니다.</h2>
							</div>
							<div class="body">준비중 입니다.</div>
						</div>
					</div>
				</div>
			
				<!-- title start -->
				<div class="block-header" style="display:none;">
					<h2>급여명세서 조회</h2>
				</div>
				<!-- title end -->

				<!-- search start -->
				<div class="row clearfix" style="display:none;">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div class="card">
							<div class="header">
								<h2>조회</h2>
							</div>
							<div class="body">
								<form class="form-horizontal">
									<div class="row clearfix">
										<div class="col-lg-2 col-md-2 col-sm-4 col-xs-5 form-control-label">
											<label for="payGb">지급 구분</label>
										</div>
										<div class="col-lg-10 col-md-10 col-sm-8 col-xs-7">
											<div class="form-group">
												<select id="payGb" class="form-control show-tick">
			                                    </select>
											</div>
										</div>
									</div>
									<div class="row clearfix">
										<div class="col-lg-2 col-md-2 col-sm-4 col-xs-5 form-control-label">
											<label for="yymm">조회 연월</label>
										</div>
										<div class="col-lg-10 col-md-10 col-sm-8 col-xs-7">
											<div class="form-group">
												<select id="yymm" class="form-control show-tick" data-show-subtext="true">
													<option value="">지급 구분을 선택해 주세요.</option>
			                                    </select>
											</div>
										</div>
									</div>
									<div class="row clearfix">
										<div class="col-lg-offset-2 col-md-offset-2 col-sm-offset-4 col-xs-offset-5">
											<button type="button" class="btn bg-indigo m-t-15 waves-effect" onClick="javascript:goSearch();">
												<i class="material-icons">search</i> <span>조회</span>
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
								<h2 id="resultTitle"></h2>
								<ul class="header-dropdown m-r--5">
									<li class="dropdown">   
										<a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
											<i class="material-icons">more_vert</i>
										</a>
										<ul class="dropdown-menu pull-right">
											<li><a href="javascript:excelDownload();">엑셀 다운로드</a></li>
											<li><a href="javascript:printTable();">출력</a></li>
										</ul>
									</li>
								</ul>
							</div>
							<div class="body table-responsive" id="resultTbody">
							</div>
						</div>
					</div>
				</div>
				<!-- result end -->

			</div>
		</section>
		<!-- contents end -->
		
		<!--loading Popup start-->
		<%@ include file="/WEB-INF/views/include/loading.jsp"%>
		<!--loading Popup end--> 

	</body>
</html>