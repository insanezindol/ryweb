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
				cfLNBMenuSelect("menu08");
				
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
					url : "/vehicle/vehicleStatisticsAjax.json",
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
								var list = json.list;
								
								htmlStr += '<div class="col-lg-12">';
								htmlStr += '<section class="panel">';
								htmlStr += '<header class="panel-heading">법인 차량 현황 ('+(list.length-1)+'대)</header>';
								htmlStr += '<table class="table table-bordered">';
								htmlStr += '<colgroup>';
								htmlStr += '<col style="width: 5%;">';
								htmlStr += '<col style="width: 15%;">';
								htmlStr += '<col style="width: 15%;">';
								htmlStr += '<col style="width: 12%;">';
								htmlStr += '<col style="width: 7%;">';
								htmlStr += '<col style="width: 15%;">';
								htmlStr += '<col style="width: 8%;">';
								htmlStr += '<col style="width: 15%;">';
								htmlStr += '<col style="width: 8%;">';
								htmlStr += '</colgroup>';
								htmlStr += '<thead>';
								htmlStr += '<tr>';
								htmlStr += '<th class="text-center" style="vertical-align: middle;">No.</th>';
								htmlStr += '<th class="text-center" style="vertical-align: middle;">차량종류</th>';
								htmlStr += '<th class="text-center" style="vertical-align: middle;">차량번호</th>';
								htmlStr += '<th class="text-center" style="vertical-align: middle;">사용자</th>';
								htmlStr += '<th class="text-center" style="vertical-align: middle;">지급구분</th>';
								htmlStr += '<th class="text-center" style="vertical-align: middle;">보험기간</th>';
								htmlStr += '<th class="text-center" style="vertical-align: middle;">보험금액</th>';
								htmlStr += '<th class="text-center" style="vertical-align: middle;">임대기간/매입일</th>';
								htmlStr += '<th class="text-center" style="vertical-align: middle;">임차료/월<br>(보험료포함)</th>';
								htmlStr += '</tr>';
								htmlStr += '</thead>';
								htmlStr += '<tbody>';
								if (list.length == 0) {
									htmlStr += '<tr>';
									htmlStr += '<td class="text-center" colspan="9">내용이 없습니다.</td>';
									htmlStr += '</tr>';
								} else {
									for(var i=0; i<list.length; i++){
										if (list[i].payment == '가합계') {
											htmlStr += '<tr>';
											htmlStr += '<td class="text-center" colspan="5">합계</td>';
											htmlStr += '<td class="text-center"></td>';
											htmlStr += '<td class="text-center">'+cfNumberWithCommas(list[i].insuranceMoney)+'</td>';
											htmlStr += '<td class="text-center"></td>';
											htmlStr += '<td class="text-center">'+cfNumberWithCommas(list[i].rentMoney)+'</td>';
											htmlStr += '</tr>';
										} else {
											htmlStr += '<tr>';
											htmlStr += '<td class="text-center">'+(i+1)+'</td>';
											htmlStr += '<td class="text-center">'+list[i].vehicleType+'</td>';
											htmlStr += '<td class="text-center">'+list[i].vehicleNo+'</td>';
											htmlStr += '<td class="text-center">'+list[i].username+'</td>';
											htmlStr += '<td class="text-center">'+list[i].payment+'</td>';
											if(list[i].insuranceStartDate != null && list[i].insuranceEndDate != null) {
												htmlStr += '<td class="text-center">'+list[i].insuranceStartDate+'~'+list[i].insuranceEndDate+'</td>';											
											} else {
												htmlStr += '<td class="text-center"></td>';
											}
											htmlStr += '<td class="text-center">'+cfNumberWithCommas(list[i].insuranceMoney)+'</td>';
											if(list[i].payment == '보유'){
												htmlStr += '<td class="text-center">'+list[i].rentStartDate+'</td>';
											} else {
												htmlStr += '<td class="text-center">'+list[i].rentStartDate+'~'+list[i].rentEndDate+'</td>';											
											}
											htmlStr += '<td class="text-center">'+cfNumberWithCommas(list[i].rentMoney)+'</td>';
											htmlStr += '</tr>';
										}
									}
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
				form.action = "/vehicle/vehicleExcelDownload.do";
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
				var url = "/vehicle/vehicleList.do";
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
								<i class="fa fa-database"></i> 법인 차량 통계
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-car"></i>법인 차량 관리</li>
								<li><i class="fa fa-database"></i>법인 차량 통계</li>
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
														<input type="radio" name="searchType" id="statType01" value="01" checked /> 법인 차량 현황
													</label>
													<!-- <label class="label_radio" for="statType02">
														<input type="radio" name="searchType" id="statType02" value="02" /> 지급구분별 (월세/연납/전세/보유)
													</label> -->
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