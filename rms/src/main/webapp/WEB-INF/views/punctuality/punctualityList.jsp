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
		<!-- jsgrid -->
		<link rel="stylesheet" href="/css/jsgrid.min.css" />
		<link rel="stylesheet" href="/css/jsgrid-theme.min.css" />
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
		<!-- jsgrid -->
		<script type="text/javascript" src="/js/jsgrid.min.js"></script>
		<!-- common function -->
		<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
		<!-- page script -->
		<script type="text/javascript">
		
		$(function() {
			// 진입 시 메뉴 선택
			cfLNBMenuSelect("menu15");
			
			// 검색어 입력영역에 엔터키 이벤트 바인드
			$("#deptName").keydown(function (key) {
			    if (key.keyCode == 13) goSearch();
			});
			$("#capsDept").keydown(function (key) {
			    if (key.keyCode == 13) goSearch();
			});
			$("#kname").keydown(function (key) {
			    if (key.keyCode == 13) goSearch();
			});
			
			// datetimepicker
			$('#startDatetime').datetimepicker({
				format: 'YYYY-MM-DD',
				sideBySide: true,
			});
			$('#endDatetime').datetimepicker({
				format: 'YYYY-MM-DD',
				sideBySide: true,
			});
			
			// 검색창 열기 닫기 버튼
	        $('#openSearchPanel').click(function(){
	        	$(this).text(function(i,old){
	        		if ( old == '검색창 열기 ▼' ) {
	        			setCookie("sCollapse", "open", 1);
	        			return '검색창 닫기 ▲';
	        		} else {
	        			setCookie("sCollapse", "close", 1);
	        			return '검색창 열기 ▼';
	        		}
	        	});
	        });
	        if (getCookie("sCollapse") == "open") {
	        	$('#openSearchPanel').text("검색창 닫기 ▲");
	        	$('#searchPanel').collapse('show');
	        }
		});
		
		// 검색 파라메터 설정
		function goSearch(){
			var startDate = $("#startDate").val().replace(/-/gi,"");
			var endDate = $("#endDate").val().replace(/-/gi,"");
			var deptName = $("#deptName").val();
			var capsDept = $("#capsDept").val();
			var kname = $("#kname").val();
			var startWorkNull = $("#startWorkNull").is(":checked");
			var startWorkAfter = $("#startWorkAfter").is(":checked");
			var endWorkAfter = $("#endWorkAfter").is(":checked");
			
			if (startDate == "" || endDate == "") {
				alert("조회일자를 입력해 주세요.");
				return;
			}
			
			var params = {
				auth : "reyon",
				startDate : startDate,
				endDate : endDate,
				deptName : deptName,
				capsDept : capsDept,
				kname : kname,
				startWorkNull : startWorkNull,
				startWorkAfter : startWorkAfter,
				endWorkAfter : endWorkAfter,
			}
			
			var request = $.ajax({
				url: "/punctuality/getPunctualityListAjax.json"
				, type : "POST"
				, timeout: 10000
				, data : params
				, dataType : "json"
				, beforeSend: function(xmlHttpRequest) {
					cfOpenMagnificPopup();
				}
				, error: function(xhr, textStatus, errorThrown) {
					alert("시스템 오류가 발생했습니다.");
				}
				, success : function(json) {
					if (json.resultCode == 0){
						$("#resultBody").empty();
						var totalList = json.list;
						var tag = '';
						if (totalList.length == 0 ){
							tag += '<tr style="letter-spacing: -1px">';
							tag += '<td class="text-center" colspan="10">결과가 존재하지 않습니다.</td>';
							tag += '</tr>';
						} else {
							for(var i=0; i<totalList.length; i++){
								tag += '<tr>';
								tag += '<td class="text-center">'+(i+1)+'</td>';
								tag += '<td class="text-center">'+totalList[i].deptName+'</td>';
								tag += '<td class="text-center">';
								if(totalList[i].capsDept != null) {
									tag += totalList[i].capsDept;
								}
								tag += '</td>';
								tag += '<td class="text-center">'+totalList[i].kname+'</td>';
								tag += '<td class="text-center">'+totalList[i].posLog+'</td>';
								tag += '<td class="text-center">';
								if(totalList[i].inDate != null) {
									tag += totalList[i].inDate;
								}
								tag += '</td>';
								tag += '<td class="text-center">'+totalList[i].actYn+'</td>';
								tag += '<td class="text-center">';
								if(totalList[i].startWork != null) {
									tag += totalList[i].startWork;
								}
								tag += '</td>';
								tag += '<td class="text-center">';
								if(totalList[i].endWork != null) {
									tag += totalList[i].endWork;
								}
								tag += '</td>';
								tag += '<td class="text-center">';
								if(totalList[i].bigo != null) {
									tag += totalList[i].bigo;
								}
								tag += '</td>';
								tag += '</tr>';
							}
						}
						$("#resultBody").append(tag);
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
		
		// 엑셀 다운로드
		function goExcel() {
			var startDate = $("#startDate").val().replace(/-/gi,"");
			var endDate = $("#endDate").val().replace(/-/gi,"");
			var deptName = $("#deptName").val();
			var capsDept = $("#capsDept").val();
			var kname = $("#kname").val();
			var startWorkNull = $("#startWorkNull").is(":checked");
			var startWorkAfter = $("#startWorkAfter").is(":checked");
			var endWorkAfter = $("#endWorkAfter").is(":checked");
			
			if (startDate == "" || endDate == "") {
				alert("조회일자를 입력해 주세요.");
				return;
			}
			
			var form = document.createElement("form");
			form.name = "downForm";
			form.id = "downForm";
			form.method = "POST";
			form.action = "/punctuality/punctualityExcelDownload.do";
			var auth = document.createElement("input");
			auth.type = "hidden";
			auth.name = "auth";
			auth.value = "reyon";
			var param1 = document.createElement("input");
			param1.type = "hidden";
			param1.name = "startDate";
			param1.value = startDate;
			var param2 = document.createElement("input");
			param2.type = "hidden";
			param2.name = "endDate";
			param2.value = endDate;
			var param3 = document.createElement("input");
			param3.type = "hidden";
			param3.name = "deptName";
			param3.value = deptName;
			var param4 = document.createElement("input");
			param4.type = "hidden";
			param4.name = "kname";
			param4.value = kname;
			var param5 = document.createElement("input");
			param5.type = "hidden";
			param5.name = "startWorkNull";
			param5.value = startWorkNull;
			var param6 = document.createElement("input");
			param6.type = "hidden";
			param6.name = "startWorkAfter";
			param6.value = startWorkAfter;
			var param7 = document.createElement("input");
			param7.type = "hidden";
			param7.name = "endWorkAfter";
			param7.value = endWorkAfter;
			var param8 = document.createElement("input");
			param8.type = "hidden";
			param8.name = "capsDept";
			param8.value = capsDept;
			$(form).append(auth);
			$(form).append(param1);
			$(form).append(param2);
			$(form).append(param3);
			$(form).append(param4);
			$(form).append(param5);
			$(form).append(param6);
			$(form).append(param7);
			$(form).append(param8);
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
								<i class="fa fa-list"></i> 진천공장 근태 조회
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-bar-chart"></i>진천공장 근태 조회</li>
								<li><i class="fa fa-list"></i>근태 조회 목록</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- search bar start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel">
								<header class="panel-heading"><a id="openSearchPanel" data-toggle="collapse" href="#searchPanel" role="button" aria-expanded="false" aria-controls="searchPanel">검색창 열기 ▼</a></header>
								<div class="panel-body collapse" id="searchPanel">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">조회일자 <span style="color: red;">(필수)</span></label>
											<div class="col-lg-8">
												<div class="row">
													<div class="col-lg-5">
														<div class="input-group date" id="startDatetime">
										                    <input type="text" id="startDate" class="form-control" />
										                    <span class="input-group-addon">
										                        <span class="glyphicon glyphicon-calendar"></span>
										                    </span>
										                </div>
													</div>
													<div class="col-lg-1 text-center">~</div>
													<div class="col-lg-5">
														<div class="input-group date" id="endDatetime">
										                    <input type="text" id="endDate" class="form-control" />
										                    <span class="input-group-addon">
										                        <span class="glyphicon glyphicon-calendar"></span>
										                    </span>
										                </div>
													</div>
												</div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">조회옵션</label>
											<div class="col-lg-8">
												<div class="checkbox">
													<label><input type="checkbox" id="startWorkNull">출근시간 미 기록 직원</label>
												</div>
												<div class="checkbox">
													<label><input type="checkbox" id="startWorkAfter">지각 (출근시간 08:30 이후 기록 직원)</label>
												</div>
												<div class="checkbox">
													<label><input type="checkbox" id="endWorkAfter">초과근무 (퇴근시간 18:00 이후 기록 직원)</label>
												</div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">인사부서명 조회</label>
											<div class="col-lg-8">
								                <input type="text" id="deptName" class="form-control" maxlength="50">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">캡스부서명 조회</label>
											<div class="col-lg-8">
								                <input type="text" id="capsDept" class="form-control" maxlength="50">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">조회대상자</label>
											<div class="col-lg-8">
								                <input type="text" id="kname" class="form-control" maxlength="50">
											</div>
											<div class="col-lg-2">
												<input type="text" style="display: none;" />
												<input type="hidden" id="queStr" value="${pageParam.queStr}" />
												<a class="btn btn-default" href="javascript:;" title="검색하기" onClick="javascript:goSearch();"><span class="icon_search"></span>&nbsp;검색</a>
												<a class="btn btn-default" href="javascript:;" title="엑셀받기" onClick="javascript:goExcel();"><span class="icon_download"></span>&nbsp;엑셀</a>
											</div>
										</div>
									</form>
								</div>
							</section>
						</div>
					</div>
					<!-- search bar end -->
					
					<!-- main table start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel">
								<table class="table table-bordered">
									<colgroup>
										<col style="width: 5%;">
										<col style="width: 20%;">
										<col style="width: 20%;">
										<col style="width: 10%;">
										<col style="width: 7%;">
										<col style="width: 7%;">
										<col style="width: 5%;">
										<col style="width: 8%;">
										<col style="width: 8%;">
										<col style="width: 10%;">
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">번호</th>
											<th class="text-center">인사부서명</th>
											<th class="text-center">캡스부서명</th>
											<th class="text-center">이름</th>
											<th class="text-center">직위</th>
											<th class="text-center">근태일자</th>
											<th class="text-center">근무일</th>
											<th class="text-center">출근시간</th>
											<th class="text-center">퇴근시간</th>
											<th class="text-center">비고</th>
										</tr>
									</thead>
									<tbody id="resultBody">
										<tr style="letter-spacing: -1px">
											<td class="text-center" colspan="10">결과가 존재하지 않습니다.</td>
										</tr>
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