<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>이연제약 관리 시스템</title>
		<link href="/css/bootstrap.min.css" rel="stylesheet">
		<link href="/css/bootstrap-theme.css?ver=20190619" rel="stylesheet">
		<link href="/css/elegant-icons-style.css" rel="stylesheet" />
		<link href="/css/font-awesome.min.css" rel="stylesheet" />
		<!-- Custom styles -->
		<link href="/css/style.css?ver=20190619" rel="stylesheet">
		<link href="/css/style-responsive.css" rel="stylesheet" />
		<link href="/css/jquery-ui-1.10.4.min.css" rel="stylesheet">
		<!-- javascripts -->
		<script type="text/javascript" src="/js/jquery-1.8.3.min.js"></script>
		<script type="text/javascript" src="/js/jquery-ui-1.10.4.min.js"></script>
		<script type="text/javascript" src="/js/jquery-ui-1.9.2.custom.min.js"></script>
		<!-- moment script -->
		<script type="text/javascript" src="/js/moment.js"></script>
  		<script type="text/javascript" src="/js/moment-ko.js"></script>
		<!-- bootstrap -->
		<script type="text/javascript" src="/js/bootstrap.min.js"></script>
		<!-- custom script for this page-->
		<script type="text/javascript" src="/js/scripts.js?ver=20190619"></script>
		<script type="text/javascript" src="/js/jquery.autosize.min.js"></script>
		<script type="text/javascript" src="/js/jquery.placeholder.min.js"></script>
		<script type="text/javascript" src="/js/jquery.slimscroll.min.js"></script>
		<!-- common function -->
		<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
		<!-- page script -->
		<script type="text/javascript">
		
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu07");
				
				$("#standardDatePicker").datetimepicker({
					format: "YYYY-MM-DD",
					sideBySide: true,
				});
			});
			
			// 통계 조회
			function goSearch() {
				var searchType = $(":input:radio[name=searchType]:checked").val();
				var standardDate = $("#standardDate").val();

				if (searchType == "") {
					alert("통계 종류를 선택해 주세요.");
					return;
				}
				if (standardDate == "") {
					alert("기준일자를 입력해 주세요.");
					return;
				}
				
				var params = {
					auth : "reyon",
					searchType : searchType,
					standardDate : standardDate,
				}

				var request = $.ajax({
					url : "/realestate/realestateStatisticsAjax.json",
					type : "POST",
					timeout : 10000,
					data : params,
					dataType : "json",
					beforeSend : function(xmlHttpRequest) {
						cfOpenMagnificPopup();
						$("#resultDiv").empty();
					},
					error : function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					},
					success : function(json) {
						if (json.resultCode == 0) {
							var htmlStr = '';
							
							if(searchType == "01"){
								var officeList = json.officeList;
								var personList = json.personList;
								var totalList = json.totalList;
								
								// 사무실용
								htmlStr += '<div class="col-lg-12">';
								htmlStr += '<section class="panel">';
								htmlStr += '<header class="panel-heading">사무실용</header>';
								htmlStr += '<table class="table table-bordered">';
								htmlStr += '<colgroup>';
								htmlStr += '<col style="width: 8%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 35%;">';
								htmlStr += '<col style="width: 7%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '</colgroup>';
								htmlStr += '<thead>';
								htmlStr += '<tr>';
								htmlStr += '<th class="text-center">구분</th>';
								htmlStr += '<th class="text-center">사용자</th>';
								htmlStr += '<th class="text-center">소재지</th>';
								htmlStr += '<th class="text-center">지급구분</th>';
								htmlStr += '<th class="text-center">보증금</th>';
								htmlStr += '<th class="text-center">임대료</th>';
								htmlStr += '<th class="text-center">관리비</th>';
								htmlStr += '<th class="text-center">계</th>';
								htmlStr += '</tr>';
								htmlStr += '</thead>';
								htmlStr += '<tbody>';
								if (officeList.length == 1) {
									htmlStr += '<tr>';
									htmlStr += '<td class="text-center" colspan="8">계약내용이 없습니다.</td>';
									htmlStr += '</tr>';
								} else {
									for(var i=0; i<officeList.length; i++){
										htmlStr += '<tr>';
										if(officeList[i].payment == "소계"){
											htmlStr += '<td class="text-center" colspan="4">'+officeList[i].payment+'</td>';
										} else {
											htmlStr += '<td class="text-center">'+officeList[i].saupGubun+'</td>';
											htmlStr += '<td class="text-center">'+officeList[i].username+'</td>';
											htmlStr += '<td class="text-center">'+officeList[i].roadAddr+'</td>';
											htmlStr += '<td class="text-center">'+officeList[i].payment+'</td>';
										}
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(officeList[i].deposit)+'</td>';
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(officeList[i].rent)+'</td>';
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(officeList[i].administrativeExpenses)+'</td>';
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(officeList[i].paidMoney)+'</td>';
										htmlStr += '</tr>';
									}
								}
								htmlStr += '</tbody>';
								htmlStr += '</table>';
								htmlStr += '</section>';
								htmlStr += '</div>';
								
								// 개인용
								htmlStr += '<div class="col-lg-12">';
								htmlStr += '<section class="panel">';
								htmlStr += '<header class="panel-heading">개인용</header>';
								htmlStr += '<table class="table table-bordered">';
								htmlStr += '<colgroup>';
								htmlStr += '<col style="width: 8%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 35%;">';
								htmlStr += '<col style="width: 7%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '</colgroup>';
								htmlStr += '<thead>';
								htmlStr += '<tr>';
								htmlStr += '<th class="text-center">구분</th>';
								htmlStr += '<th class="text-center">사용자</th>';
								htmlStr += '<th class="text-center">소재지</th>';
								htmlStr += '<th class="text-center">지급구분</th>';
								htmlStr += '<th class="text-center">보증금</th>';
								htmlStr += '<th class="text-center">임대료</th>';
								htmlStr += '<th class="text-center">관리비</th>';
								htmlStr += '<th class="text-center">계</th>';
								htmlStr += '</tr>';
								htmlStr += '</thead>';
								htmlStr += '<tbody>';
								if (personList.length == 1) {
									htmlStr += '<tr>';
									htmlStr += '<td class="text-center" colspan="8">계약내용이 없습니다.</td>';
									htmlStr += '</tr>';
								} else {
									for(var i=0; i<personList.length; i++){
										htmlStr += '<tr>';
										if(personList[i].payment == "소계"){
											htmlStr += '<td class="text-center" colspan="4">'+personList[i].payment+'</td>';
										} else {
											htmlStr += '<td class="text-center">'+personList[i].saupGubun+'</td>';
											htmlStr += '<td class="text-center">'+personList[i].username+'</td>';
											htmlStr += '<td class="text-center">'+personList[i].roadAddr+'</td>';
											htmlStr += '<td class="text-center">'+personList[i].payment+'</td>';
										}
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(personList[i].deposit)+'</td>';
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(personList[i].rent)+'</td>';
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(personList[i].administrativeExpenses)+'</td>';
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(personList[i].paidMoney)+'</td>';
										htmlStr += '</tr>';
									}
								}
								htmlStr += '</tbody>';
								htmlStr += '</table>';
								htmlStr += '</section>';
								htmlStr += '</div>';
								
								// 합계
								htmlStr += '<div class="col-lg-12">';
								htmlStr += '<section class="panel">';
								htmlStr += '<table class="table table-bordered">';
								htmlStr += '<colgroup>';
								htmlStr += '<col style="width: 8%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 35%;">';
								htmlStr += '<col style="width: 7%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '</colgroup>';
								htmlStr += '<tbody>';
								for(var i=0; i<totalList.length; i++){
									htmlStr += '<tr>';
									if(totalList[i].payment == "합계"){
										htmlStr += '<td class="text-center" colspan="4">'+totalList[i].payment+'</td>';
									} else {
										htmlStr += '<td class="text-center">'+totalList[i].saupGubun+'</td>';
										htmlStr += '<td class="text-center">'+totalList[i].username+'</td>';
										htmlStr += '<td class="text-center">'+totalList[i].roadAddr+'</td>';
										htmlStr += '<td class="text-center">'+totalList[i].payment+'</td>';
									}
									htmlStr += '<td class="text-center">'+cfNumberWithCommas(totalList[i].deposit)+'</td>';
									htmlStr += '<td class="text-center">'+cfNumberWithCommas(totalList[i].rent)+'</td>';
									htmlStr += '<td class="text-center">'+cfNumberWithCommas(totalList[i].administrativeExpenses)+'</td>';
									htmlStr += '<td class="text-center">'+cfNumberWithCommas(totalList[i].paidMoney)+'</td>';
									htmlStr += '</tr>';
								}
								htmlStr += '</tbody>';
								htmlStr += '</table>';
								htmlStr += '</section>';
								htmlStr += '</div>';
							} else if(searchType == "02"){
								var monthlyList = json.monthlyList;
								var yearlyList = json.yearlyList;
								var rentList = json.rentList;
								var possessionList = json.possessionList;
								var totalList = json.totalList;
								
								// 월세
								htmlStr += '<div class="col-lg-12">';
								htmlStr += '<section class="panel">';
								htmlStr += '<header class="panel-heading">월세</header>';
								htmlStr += '<table class="table table-bordered">';
								htmlStr += '<colgroup>';
								htmlStr += '<col style="width: 8%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 35%;">';
								htmlStr += '<col style="width: 7%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '</colgroup>';
								htmlStr += '<thead>';
								htmlStr += '<tr>';
								htmlStr += '<th class="text-center">구분</th>';
								htmlStr += '<th class="text-center">사용자</th>';
								htmlStr += '<th class="text-center">소재지</th>';
								htmlStr += '<th class="text-center">지급구분</th>';
								htmlStr += '<th class="text-center">보증금</th>';
								htmlStr += '<th class="text-center">임대료</th>';
								htmlStr += '<th class="text-center">관리비</th>';
								htmlStr += '<th class="text-center">계</th>';
								htmlStr += '</tr>';
								htmlStr += '</thead>';
								htmlStr += '<tbody>';
								if (monthlyList.length == 1) {
									htmlStr += '<tr>';
									htmlStr += '<td class="text-center" colspan="8">계약내용이 없습니다.</td>';
									htmlStr += '</tr>';
								} else {
									for(var i=0; i<monthlyList.length; i++){
										htmlStr += '<tr>';
										if(monthlyList[i].payment == "소계"){
											htmlStr += '<td class="text-center" colspan="4">'+monthlyList[i].payment+'</td>';
										} else {
											htmlStr += '<td class="text-center">'+monthlyList[i].saupGubun+'</td>';
											htmlStr += '<td class="text-center">'+monthlyList[i].username+'</td>';
											htmlStr += '<td class="text-center">'+monthlyList[i].roadAddr+'</td>';
											htmlStr += '<td class="text-center">'+monthlyList[i].payment+'</td>';
										}
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(monthlyList[i].deposit)+'</td>';
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(monthlyList[i].rent)+'</td>';
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(monthlyList[i].administrativeExpenses)+'</td>';
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(monthlyList[i].paidMoney)+'</td>';
										htmlStr += '</tr>';
									}
								}
								htmlStr += '</tbody>';
								htmlStr += '</table>';
								htmlStr += '</section>';
								htmlStr += '</div>';
								
								// 연납
								htmlStr += '<div class="col-lg-12">';
								htmlStr += '<section class="panel">';
								htmlStr += '<header class="panel-heading">연납</header>';
								htmlStr += '<table class="table table-bordered">';
								htmlStr += '<colgroup>';
								htmlStr += '<col style="width: 8%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 35%;">';
								htmlStr += '<col style="width: 7%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '</colgroup>';
								htmlStr += '<thead>';
								htmlStr += '<tr>';
								htmlStr += '<th class="text-center">구분</th>';
								htmlStr += '<th class="text-center">사용자</th>';
								htmlStr += '<th class="text-center">소재지</th>';
								htmlStr += '<th class="text-center">지급구분</th>';
								htmlStr += '<th class="text-center">보증금</th>';
								htmlStr += '<th class="text-center">임대료</th>';
								htmlStr += '<th class="text-center">관리비</th>';
								htmlStr += '<th class="text-center">계</th>';
								htmlStr += '</tr>';
								htmlStr += '</thead>';
								htmlStr += '<tbody>';
								if (yearlyList.length == 1) {
									htmlStr += '<tr>';
									htmlStr += '<td class="text-center" colspan="8">계약내용이 없습니다.</td>';
									htmlStr += '</tr>';
								} else {
									for(var i=0; i<yearlyList.length; i++){
										htmlStr += '<tr>';
										if(yearlyList[i].payment == "소계"){
											htmlStr += '<td class="text-center" colspan="4">'+yearlyList[i].payment+'</td>';
										} else {
											htmlStr += '<td class="text-center">'+yearlyList[i].saupGubun+'</td>';
											htmlStr += '<td class="text-center">'+yearlyList[i].username+'</td>';
											htmlStr += '<td class="text-center">'+yearlyList[i].roadAddr+'</td>';
											htmlStr += '<td class="text-center">'+yearlyList[i].payment+'</td>';
										}
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(yearlyList[i].deposit)+'</td>';
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(yearlyList[i].rent)+'</td>';
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(yearlyList[i].administrativeExpenses)+'</td>';
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(yearlyList[i].paidMoney)+'</td>';
										htmlStr += '</tr>';
									}
								}
								htmlStr += '</tbody>';
								htmlStr += '</table>';
								htmlStr += '</section>';
								htmlStr += '</div>';
								
								// 전세
								htmlStr += '<div class="col-lg-12">';
								htmlStr += '<section class="panel">';
								htmlStr += '<header class="panel-heading">전세</header>';
								htmlStr += '<table class="table table-bordered">';
								htmlStr += '<colgroup>';
								htmlStr += '<col style="width: 8%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 35%;">';
								htmlStr += '<col style="width: 7%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '</colgroup>';
								htmlStr += '<thead>';
								htmlStr += '<tr>';
								htmlStr += '<th class="text-center">구분</th>';
								htmlStr += '<th class="text-center">사용자</th>';
								htmlStr += '<th class="text-center">소재지</th>';
								htmlStr += '<th class="text-center">지급구분</th>';
								htmlStr += '<th class="text-center">보증금</th>';
								htmlStr += '<th class="text-center">임대료</th>';
								htmlStr += '<th class="text-center">관리비</th>';
								htmlStr += '<th class="text-center">계</th>';
								htmlStr += '</tr>';
								htmlStr += '</thead>';
								htmlStr += '<tbody>';
								if (rentList.length == 1) {
									htmlStr += '<tr>';
									htmlStr += '<td class="text-center" colspan="8">계약내용이 없습니다.</td>';
									htmlStr += '</tr>';
								} else {
									for(var i=0; i<rentList.length; i++){
										htmlStr += '<tr>';
										if(rentList[i].payment == "소계"){
											htmlStr += '<td class="text-center" colspan="4">'+rentList[i].payment+'</td>';
										} else {
											htmlStr += '<td class="text-center">'+rentList[i].saupGubun+'</td>';
											htmlStr += '<td class="text-center">'+rentList[i].username+'</td>';
											htmlStr += '<td class="text-center">'+rentList[i].roadAddr+'</td>';
											htmlStr += '<td class="text-center">'+rentList[i].payment+'</td>';
										}
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(rentList[i].deposit)+'</td>';
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(rentList[i].rent)+'</td>';
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(rentList[i].administrativeExpenses)+'</td>';
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(rentList[i].paidMoney)+'</td>';
										htmlStr += '</tr>';
									}
								}
								htmlStr += '</tbody>';
								htmlStr += '</table>';
								htmlStr += '</section>';
								htmlStr += '</div>';
								
								// 보유
								htmlStr += '<div class="col-lg-12">';
								htmlStr += '<section class="panel">';
								htmlStr += '<header class="panel-heading">보유</header>';
								htmlStr += '<table class="table table-bordered">';
								htmlStr += '<colgroup>';
								htmlStr += '<col style="width: 8%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 35%;">';
								htmlStr += '<col style="width: 7%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '</colgroup>';
								htmlStr += '<thead>';
								htmlStr += '<tr>';
								htmlStr += '<th class="text-center">구분</th>';
								htmlStr += '<th class="text-center">사용자</th>';
								htmlStr += '<th class="text-center">소재지</th>';
								htmlStr += '<th class="text-center">지급구분</th>';
								htmlStr += '<th class="text-center">보증금</th>';
								htmlStr += '<th class="text-center">임대료</th>';
								htmlStr += '<th class="text-center">관리비</th>';
								htmlStr += '<th class="text-center">계</th>';
								htmlStr += '</tr>';
								htmlStr += '</thead>';
								htmlStr += '<tbody>';
								if (possessionList.length == 1) {
									htmlStr += '<tr>';
									htmlStr += '<td class="text-center" colspan="8">계약내용이 없습니다.</td>';
									htmlStr += '</tr>';
								} else {
									for(var i=0; i<possessionList.length; i++){
										htmlStr += '<tr>';
										if(possessionList[i].payment == "소계"){
											htmlStr += '<td class="text-center" colspan="4">'+possessionList[i].payment+'</td>';
										} else {
											htmlStr += '<td class="text-center">'+possessionList[i].saupGubun+'</td>';
											htmlStr += '<td class="text-center">'+possessionList[i].username+'</td>';
											htmlStr += '<td class="text-center">'+possessionList[i].roadAddr+'</td>';
											htmlStr += '<td class="text-center">'+possessionList[i].payment+'</td>';
										}
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(possessionList[i].deposit)+'</td>';
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(possessionList[i].rent)+'</td>';
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(possessionList[i].administrativeExpenses)+'</td>';
										htmlStr += '<td class="text-center">'+cfNumberWithCommas(possessionList[i].paidMoney)+'</td>';
										htmlStr += '</tr>';
									}
								}
								htmlStr += '</tbody>';
								htmlStr += '</table>';
								htmlStr += '</section>';
								htmlStr += '</div>';
								
								// 합계
								htmlStr += '<div class="col-lg-12">';
								htmlStr += '<section class="panel">';
								htmlStr += '<table class="table table-bordered">';
								htmlStr += '<colgroup>';
								htmlStr += '<col style="width: 8%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 35%;">';
								htmlStr += '<col style="width: 7%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '<col style="width: 10%;">';
								htmlStr += '</colgroup>';
								htmlStr += '<tbody>';
								for(var i=0; i<totalList.length; i++){
									htmlStr += '<tr>';
									if(totalList[i].payment == "합계"){
										htmlStr += '<td class="text-center" colspan="4">'+totalList[i].payment+'</td>';
									} else {
										htmlStr += '<td class="text-center">'+totalList[i].saupGubun+'</td>';
										htmlStr += '<td class="text-center">'+totalList[i].username+'</td>';
										htmlStr += '<td class="text-center">'+totalList[i].roadAddr+'</td>';
										htmlStr += '<td class="text-center">'+totalList[i].payment+'</td>';
									}
									htmlStr += '<td class="text-center">'+cfNumberWithCommas(totalList[i].deposit)+'</td>';
									htmlStr += '<td class="text-center">'+cfNumberWithCommas(totalList[i].rent)+'</td>';
									htmlStr += '<td class="text-center">'+cfNumberWithCommas(totalList[i].administrativeExpenses)+'</td>';
									htmlStr += '<td class="text-center">'+cfNumberWithCommas(totalList[i].paidMoney)+'</td>';
									htmlStr += '</tr>';
								}
								htmlStr += '</tbody>';
								htmlStr += '</table>';
								htmlStr += '</section>';
								htmlStr += '</div>';
								
								
							}
							
							$("#resultDiv").html(htmlStr);
							$("#excelDownBtn").css("display", "inline-block");
						} else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
						} else {
							alert(json.resultMsg);
							cfCloseMagnificPopup();
						}
					},
					complete : function() {
						cfCloseMagnificPopup();
					}
				});
			}
			
			// 엑셀 다운로드
			function downloadExcel() {
				var form = document.createElement("form");
				form.name = "downForm";
				form.id = "downForm";
				form.method = "POST";
				form.action = "/realestate/realestateExcelDownload.do";
				var auth = document.createElement("input");
				auth.type = "hidden";
				auth.name = "auth";
				auth.value = "reyon";
				var searchType = document.createElement("input");
				searchType.type = "hidden";
				searchType.name = "searchType";
				searchType.value = $(":input:radio[name=searchType]:checked").val();
				var standardDate = document.createElement("input");
				standardDate.type = "hidden";
				standardDate.name = "standardDate";
				standardDate.value = $("#standardDate").val();
				$(form).append(auth);
				$(form).append(searchType);
				$(form).append(standardDate);
				$("#container").append(form);
				form.submit();
				$("#downForm").remove();
			}
			
			// 등록 페이지로 이동
			function goListPage(){
				var url = "/realestate/realestateList.do";
				location.href = url;
			}
			
		</script>
	</head>
	
	<body>
	
		<!-- container section start -->
		<section id="container" class="">
		
			<!-- header start -->
			<%@ include file="/WEB-INF/views/include/gnb.jsp"%>
			<!-- header end -->
		
			<!--sidebar start-->
			<%@ include file="/WEB-INF/views/include/lnb.jsp"%>
			<!--sidebar end--> 
			
			<!--main content start-->
			<section id="main-content">
				<!--overview start-->
				<section class="wrapper">
					
					<!-- title start --> 
					<div class="row">
						<div class="col-lg-12">
							<h3 class="page-header">
								<i class="fa fa-database"></i> 부동산 계약 통계
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-building-o"></i>부동산 계약 관리</li>
								<li><i class="fa fa-database"></i>부동산 계약 통계</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 19px;">
								<header class="panel-heading">통계 추출 검색 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">통계 종류</label>
											<div class="col-lg-10">
												<div class="radios">
													<label class="label_radio" for="statType01">
														<input type="radio" name="searchType" id="statType01" value="01" checked /> 용도별 (사무실용/개인용)
													</label>
													<label class="label_radio" for="statType02">
														<input type="radio" name="searchType" id="statType02" value="02" /> 지급구분별 (월세/연납/전세/보유)
													</label>
												</div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">기준일자</label>
											<div class="col-lg-3">
												<div class="input-group date" id="standardDatePicker">
								                    <input type="text" id="standardDate" class="form-control" placeholder="기준 일자" />
								                    <span class="input-group-addon">
								                        <span class="glyphicon glyphicon-calendar"></span>
								                    </span>
								                </div>
											</div>
											<div class="col-lg-7">
											</div>
										</div>
										<div class="form-group">
											<div class="col-lg-offset-2 col-lg-10">
												<a class="btn btn-default" href="javascript:;" title="조회하기" style="margin-top: 7px;" onClick="javascript:goSearch();"><span class="icon_search"></span>&nbsp;조회</a>
											</div>
										</div>
									</form>
								</div>
							</section>
						</div>
					</div>
					<!-- body top end -->
					
					<!-- main table start -->
					<div class="row" id="resultDiv"></div>
					<!-- main table end -->
					
					<!-- bottom button start -->
					<div class="btn-row text-right">
						<a class="btn btn-default" href="javascript:;" title="엑셀 다운로드 하기" id="excelDownBtn" style="margin-top: 7px; display: none;" onClick="javascript:downloadExcel();"><span class="icon_download"></span>&nbsp;엑셀 다운로드</a>
						<a class="btn btn-default" href="javascript:;" title="이전으로" style="margin-top: 7px;" onClick="javascript:goListPage();"><span class="arrow_back"></span>&nbsp;이전</a>
					</div>
					<!-- bottom button end -->
					
				</section>
				<!--overview end-->
				
			</section>
			<!--main content end-->
			
		</section>
		<!-- container section start -->
		
		<!--loading Popup start-->
		<%@ include file="/WEB-INF/views/include/loading.jsp"%>
		<!--loading Popup end--> 
		
	</body>
</html>