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
			cfLNBMenuSelect("menu06");
			
			// datetimepicker
	        $("#startDatePicker").datetimepicker({
				format: "YYYY-MM-DD",
				sideBySide: true,
			});
	        $("#endDatePicker").datetimepicker({
				format: "YYYY-MM-DD",
				sideBySide: true,
			});
	        
	        // 테이블 노출
	        $("#divType${pageParam.searchType}").show();
	        
	        // 검색값 설정
	        $("input:radio[name=searchType]:input[value=${pageParam.searchType}]").attr("checked", true);
	        $("#startDate").val("${pageParam.startDate}");
	        $("#endDate").val("${pageParam.endDate}");
		});
		
		// 페이징 및 검색 파라메터 선언
		var pageParam = {
			searchType: ""
			, startDate : ""
			, endDate : ""
		}
		
		// 검색 파라메터 설정
		function goSearch(){
			pageParam.searchType = $(":input:radio[name=searchType]:checked").val();
			pageParam.startDate = $("#startDate").val();
			pageParam.endDate = $("#endDate").val();
			
			if(pageParam.searchType == ""){
				alert("통계종류를 선택해 주세요.");
				return;
			}
			if(pageParam.startDate == "" || pageParam.endDate == ""){
				alert("기간을 선택해 주세요.");
				return;
			}
			
			goUrl();
		}
		
		// 페이지 이동
		function goUrl(){
			location.href="/coming/comingStatistics.do?searchType="+pageParam.searchType+"&startDate="+pageParam.startDate+"&endDate="+pageParam.endDate;
		}
		
		// 엑셀 다운로드
		function downloadExcel() {
			var form = document.createElement("form");
			form.name = "downForm";
			form.id = "downForm";
			form.method = "POST";
			form.action = "/coming/comingExcelDownload.do";
			var auth = document.createElement("input");
			auth.type = "hidden";
			auth.name = "auth";
			auth.value = "reyon";
			var searchType = document.createElement("input");
			searchType.type = "hidden";
			searchType.name = "searchType";
			searchType.value = "${pageParam.searchType}";
			var startDate = document.createElement("input");
			startDate.type = "hidden";
			startDate.name = "startDate";
			startDate.value = "${pageParam.startDate}";
			var endDate = document.createElement("input");
			endDate.type = "hidden";
			endDate.name = "endDate";
			endDate.value = "${pageParam.endDate}";
			$(form).append(auth);
			$(form).append(searchType);
			$(form).append(startDate);
			$(form).append(endDate);
			$("#container").append(form);
			form.submit();
			$("#downForm").remove();
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
								<i class="fa fa-database"></i> 출입자 통계
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-address-card-o"></i>출입자 관리</li>
								<li><i class="fa fa-database"></i>출입자 통계</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">통계 추출 검색 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">통계 종류</label>
											<div class="col-lg-10">
												<div class="radios">
													<label class="label_radio" for="statType01">
														<input type="radio" name="searchType" id="statType01" value="01" checked /> 부서별 주차권 지급 현황
													</label>
													<label class="label_radio" for="statType02">
														<input type="radio" name="searchType" id="statType02" value="02" /> 주차권 지급 상세 목록
													</label>
													<!-- <label class="label_radio" for="statType03">
														<input type="radio" name="searchType" id="statType03" value="03" /> 출입자 상세 목록
													</label> -->
													<label class="label_radio" for="statType04">
														<input type="radio" name="searchType" id="statType04" value="04" /> KT&G 웹할인 상세 목록
													</label>
												</div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">기간</label>
											<div class="col-lg-5">
												<div class="input-group date" id="startDatePicker">
								                    <input type="text" id="startDate" class="form-control" placeholder="통계 시작 일자" />
								                    <span class="input-group-addon">
								                        <span class="glyphicon glyphicon-calendar"></span>
								                    </span>
								                </div>
											</div>
											<div class="col-lg-5">
												<div class="input-group date" id="endDatePicker">
								                    <input type="text" id="endDate" class="form-control" placeholder="통계 종료 일자" />
								                    <span class="input-group-addon">
								                        <span class="glyphicon glyphicon-calendar"></span>
								                    </span>
								                </div>
											</div>
										</div>
										<div class="form-group">
											<div class="col-lg-offset-2 col-lg-10">
												<a class="btn btn-default" href="javascript:;" title="검색하기" style="margin-top: 7px;" onClick="javascript:goSearch();"><span class="icon_search"></span>&nbsp;검색</a>
											</div>
										</div>
									</form>
								</div>
							</section>
						</div>
					</div>
					<!-- body top end -->
					
					<br>
					
					<!-- main table start -->
					<div class="row" id="divType01" style="display: none;">
						<div class="col-lg-12">
							<div class="btn-row text-right">
								<a class="btn btn-default" href="javascript:;" title="엑셀 다운로드 하기" style="margin-top: 7px;" onClick="javascript:downloadExcel();"><span class="icon_download"></span>&nbsp;엑셀 다운로드</a>
							</div>
							<div>
								<h3 style="text-align: center;">부서별 주차권 지급 현황</h3>
								<h4 style="text-align: right;">기간 : ${pageParam.startDate } ~ ${pageParam.endDate }</h4>
							</div>
							<section class="panel">
								<table class="table table-bordered">
									<colgroup>
										<col style="width: 14%;">
										<col style="width: 14%;">
										<col style="width: 12%;">
										<col style="width: 12%;">
										<col style="width: 12%;">
										<col style="width: 12%;">
										<col style="width: 12%;">
										<col style="width: 12%;">
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">부서코드</th>
											<th class="text-center">부서이름</th>
											<th class="text-center">2시간</th>
											<th class="text-center">3시간</th>
											<th class="text-center">4시간</th>
											<th class="text-center">6시간</th>
											<th class="text-center">종일권</th>
											<th class="text-center">계</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${fn:length(list1) == 0}">
												<tr style="letter-spacing: -1px">
													<td class="text-center" colspan="8">결과가 존재하지 않습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="result" items="${list1}" varStatus="status">
													<tr>
														<td class="text-center">${result.deptCode }</td>
														<td>${result.deptName }</td>
														<td class="text-center">${result.countHour2 }</a></td>
														<td class="text-center">${result.countHour3 }</td>
														<td class="text-center">${result.countHour4 }</td>
														<td class="text-center">${result.countHour6 }</td>
														<td class="text-center">${result.countHour24 }</td>
														<td class="text-center">${result.totalCount }</td>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
							</section>
						</div>
					</div>
					
					<div class="row" id="divType02" style="display: none;">
						<div class="col-lg-12">
							<div class="btn-row text-right">
								<a class="btn btn-default" href="javascript:;" title="엑셀 다운로드 하기" style="margin-top: 7px;" onClick="javascript:downloadExcel();"><span class="icon_download"></span>&nbsp;엑셀 다운로드</a>
							</div>
							<div>
								<h3 style="text-align: center;">주차권 지급 상세 목록</h3>
								<h4 style="text-align: right;">기간 : ${pageParam.startDate } ~ ${pageParam.endDate }</h4>
							</div>
							<section class="panel">
								<table class="table table-bordered">
									<colgroup>
										<col style="width: 5%;">
										<col style="width: 15%;">
										<col style="width: 10%;">
										<col style="width: 20%;">
										<col style="width: 15%;">
										<col style="width: 10%;">
										<col style="width: 5%;">
										<col style="width: 5%;">
										<col style="width: 5%;">
										<col style="width: 5%;">
										<col style="width: 5%;">
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">번호</th>
											<th class="text-center">방문 업체명</th>
											<th class="text-center">방문자 성함</th>
											<th class="text-center">방문 목적</th>
											<th class="text-center">지급일시</th>
											<th class="text-center">담당부서</th>
											<th class="text-center">2시간</th>
											<th class="text-center">3시간</th>
											<th class="text-center">4시간</th>
											<th class="text-center">6시간</th>
											<th class="text-center">종일권</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${fn:length(list2) == 0}">
												<tr style="letter-spacing: -1px">
													<td class="text-center" colspan="11">결과가 존재하지 않습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="result" items="${list2}" varStatus="status">
													<tr>
														<td class="text-center">${result.rnum }</td>
														<td class="text-center">${result.visitCompany }</a></td>
														<td class="text-center">${result.visitName }</td>
														<td class="text-center">${result.visitPurpose }</td>
														<td class="text-center">${result.giveDate }</td>
														<td class="text-center">${result.refDeptName }</td>
														<td class="text-center">${result.countHour2 }</td>
														<td class="text-center">${result.countHour3 }</td>
														<td class="text-center">${result.countHour4 }</td>
														<td class="text-center">${result.countHour6 }</td>
														<td class="text-center">${result.countHour24 }</td>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
							</section>
						</div>
					</div>
					
					<div class="row" id="divType03" style="display: none;">
						<div class="col-lg-12">
							<div class="btn-row text-right">
								<a class="btn btn-default" href="javascript:;" title="엑셀 다운로드 하기" style="margin-top: 7px;" onClick="javascript:downloadExcel();"><span class="icon_download"></span>&nbsp;엑셀 다운로드</a>
							</div>
							<div>
								<h3 style="text-align: center;">출입자 상세 목록</h3>
								<h4 style="text-align: right;">기간 : ${pageParam.startDate } ~ ${pageParam.endDate }</h4>
							</div>
							<section class="panel">
								<table class="table table-bordered">
									<colgroup>
										<col style="width: 10%;">
										<col style="width: 20%;">
										<col style="width: 20%;">
										<col style="width: 15%;">
										<col style="width: 15%;">
										<col style="width: 10%;">
										<col style="width: 10%;">
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">번호</th>
											<th class="text-center">방문 업체명</th>
											<th class="text-center">방문자 성함</th>
											<th class="text-center">방문 일시</th>
											<th class="text-center">접견부서</th>
											<th class="text-center">접견인</th>
											<th class="text-center">주차권지급여부</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${fn:length(list3) == 0}">
												<tr style="letter-spacing: -1px">
													<td class="text-center" colspan="7">결과가 존재하지 않습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="result" items="${list3}" varStatus="status">
													<tr>
														<td class="text-center">${result.rnum }</td>
														<td class="text-center">${result.visitCompany }</td>
														<td class="text-center"><a href="javascript:goDetailPage('${result.visitSeq }');">${result.visitName }</a></td>
														<td class="text-center">${result.visitStartDate }</td>
														<td class="text-center">${result.meetDeptName }</td>
														<td class="text-center">${result.meetName }</td>
														<td class="text-center">${result.ticketCheck }</td>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
							</section>
						</div>
					</div>
					
					<div class="row" id="divType04" style="display: none;">
						<div class="col-lg-12">
							<div class="btn-row text-right">
								<a class="btn btn-default" href="javascript:;" title="엑셀 다운로드 하기" style="margin-top: 7px;" onClick="javascript:downloadExcel();"><span class="icon_download"></span>&nbsp;엑셀 다운로드</a>
							</div>
							<div>
								<h3 style="text-align: center;">KT&G 웹할인 상세 목록</h3>
								<h4 style="text-align: right;">기간 : ${pageParam.startDate } ~ ${pageParam.endDate }</h4>
							</div>
							<section class="panel">
								<table class="table table-bordered">
									<colgroup>
										<col style="width: 5%;">
										<col style="width: 15%;">
										<col style="width: 10%;">
										<col style="width: 20%;">
										<col style="width: 15%;">
										<col style="width: 15%;">
										<col style="width: 20%;">
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">번호</th>
											<th class="text-center">방문 업체명</th>
											<th class="text-center">방문자 성함</th>
											<th class="text-center">방문 목적</th>
											<th class="text-center">지급일시</th>
											<th class="text-center">담당부서</th>
											<th class="text-center">정산금액</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${fn:length(list4) == 0}">
												<tr style="letter-spacing: -1px">
													<td class="text-center" colspan="7">결과가 존재하지 않습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="result" items="${list4}" varStatus="status">
													<tr>
														<td class="text-center">${result.rnum }</td>
														<td class="text-center">${result.visitCompany }</a></td>
														<td class="text-center">${result.visitName }</td>
														<td class="text-center">${result.visitPurpose }</td>
														<td class="text-center">${result.giveDate }</td>
														<td class="text-center">${result.refDeptName }</td>
														<td class="text-center"><fmt:formatNumber value="${result.webSalePrice }" pattern="#,###" /></td>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
							</section>
						</div>
					</div>
					<!-- main table end -->
					
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