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
				cfLNBMenuSelect("menu10");

				// datetimepicker
				$('#startDatetime').datetimepicker({
					format: 'YYYY-MM',
					sideBySide: true,
				});
				$('#endDatetime').datetimepicker({
					format: 'YYYY-MM',
					sideBySide: true,
					useCurrent: false,
				});
				var today = new Date();
			    var dd = today.getDate();
			    var mm = today.getMonth()+1;
			    var yyyy = today.getFullYear();
			    if(dd<10){ dd='0'+dd; } 
			    if(mm<10){ mm='0'+mm; } 
			    var today = yyyy + "-" + mm;
			    $("#startDate").val(today);
			    $("#endDate").val(today);
		        
		        // 지점 정보 가져오기
		        getJijum();
		        
		     	// 지점 변경 시
				$("#s_jijum").change(function() {
					var jijumVal = $(this).val();
					$("#s_user").empty();
					$("#s_account").empty();
					if(jijumVal != ""){
						getUser(jijumVal);
					}
				});
				// 담당자 변경 시
				$("#s_user").change(function() {
					var jijumVal = $("#s_jijum").val();
					var userVal = $(this).val();
					$("#s_account").empty();
					if(userVal != ""){
						getAccount(jijumVal, userVal);
					}
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
			
			// 지점 정보 가져오기
			function getJijum(){
				var params = {
					auth : "reyon",
				}

				var request = $.ajax({
					url : '/sfe/salesDeptAjax.json',
					type : 'POST',
					timeout : 30000,
					data : params,
					dataType : 'json',
					beforeSend : function(xmlHttpRequest) {
						cfOpenMagnificPopup();
					},
					error : function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					},
					success : function(json) {
						if (json.resultCode == 0) {
							var list = json.list;
							var tag = '<option value="">== 지점 선택 ==</option>';
							for(var i=0; i<list.length; i++){
								tag += '<option value="'+list[i].DEPT_CD+'">'+list[i].DEPT_NM+'</option>';
							}
							$("#s_jijum").append(tag);
						} else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
						} else {
							alert(json.resultMsg);
						}
					},
					complete : function() {
						cfCloseMagnificPopup();
					}
				});
			}
			
			// 담당자 정보 가져오기
			function getUser(deptNo){
				var params = {
					auth : "reyon",
					deptNo : deptNo,
				}

				var request = $.ajax({
					url : '/sfe/salesUserAjax.json',
					type : 'POST',
					timeout : 30000,
					data : params,
					dataType : 'json',
					beforeSend : function(xmlHttpRequest) {
						cfOpenMagnificPopup();
					},
					error : function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					},
					success : function(json) {
						if (json.resultCode == 0) {
							$("#s_user").empty();
							var list = json.list;
							var tag = '<option value="">== 담당자 선택 ==</option>';
							for(var i=0; i<list.length; i++){
								tag += '<option value="'+list[i].empNo+'">'+list[i].empNm+'</option>';
							}
							$("#s_user").append(tag);
						} else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
						} else {
							alert(json.resultMsg);
						}
					},
					complete : function() {
						cfCloseMagnificPopup();
					}
				});
			}
			
			// 거래처 정보 가져오기
			function getAccount(deptNo, empNo){
				var params = {
					auth : "reyon",
					deptNo : deptNo,
					empNo : empNo,
				}

				var request = $.ajax({
					url : '/sfe/salesAccountAjax.json',
					type : 'POST',
					timeout : 30000,
					data : params,
					dataType : 'json',
					beforeSend : function(xmlHttpRequest) {
						cfOpenMagnificPopup();
					},
					error : function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					},
					success : function(json) {
						if (json.resultCode == 0) {
							$("#s_account").empty();
							var list = json.list;
							var tag = '<option value="">== 거래처 선택 ==</option>';
							for(var i=0; i<list.length; i++){
								tag += '<option value="'+list[i].CUS_CD+'">'+list[i].CUS_NM+'</option>';
							}
							$("#s_account").append(tag);
						} else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
						} else {
							alert(json.resultMsg);
						}
					},
					complete : function() {
						cfCloseMagnificPopup();
					}
				});
			}
			
			// 검색
			function goSearch(){
				var startDate = $("#startDate").val();
				var endDate = $("#endDate").val();
				var s_jijum = $("#s_jijum option:selected").val();
				var s_user = $("#s_user option:selected").val();
				var s_account = $("#s_account option:selected").val();
				
				if (startDate == "" || endDate == "") {
					alert("조회월(필수)을 입력해 주세요.");
					return;
				}
				if (s_jijum == "") {
					alert("지점(필수)을 입력해 주세요.");
					return;
				}
				
				var params = {
					auth : "reyon",
					startDate : startDate,
					endDate : endDate,
					s_jijum : s_jijum,
					s_user : s_user,
					s_account : s_account,
				}

				var request = $.ajax({
					url : '/sfe/salesProfitListAjax.json',
					type : 'POST',
					timeout : 30000,
					data : params,
					dataType : 'json',
					beforeSend : function(xmlHttpRequest) {
						cfOpenMagnificPopup();
					},
					error : function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					},
					success : function(json) {
						if (json.resultCode == 0) {
							$("#resultBody").empty();
							var list = json.list;
							var tag = '';
							if(list.length == 0){
								tag += '<tr><td colspan="7" class="text-center">결과가 존재하지 않습니다.</td></tr>';
							} else {
								for(var i=0; i<list.length; i++){
									tag += '<tr>';
									tag += '<td class="text-center">';
									if(list[i].DEPT_NM != null){
										tag += list[i].DEPT_NM;
									}
									tag += '</td>';
									tag += '<td class="text-center">';
									if(list[i].EMP_NM != null){
										tag += list[i].EMP_NM;
									}
									tag += '</td>';
									tag += '<td class="text-center">';
									if(list[i].CUS_NM != null){
										tag += list[i].CUS_NM;
									}
									tag += '</td>';
									tag += '<td class="text-center">';
									if(list[i].DEPARTMENT_NM != null){
										tag += list[i].DEPARTMENT_NM;
									}
									tag += '</td>';
									tag += '<td class="text-center">';
									if(list[i].JPM_NM != null){
										tag += list[i].JPM_NM;
									}
									tag += '</td>';
									tag += '<td class="text-center">';
									if(list[i].ACTIVIT_SUM_CNT != null){
										tag += list[i].ACTIVIT_SUM_CNT;
									}
									tag += '</td>';
									tag += '<td class="text-center">';
									if(list[i].SALE_AMT != null){
										tag += list[i].SALE_AMT;
									}
									tag += '</td>';
									tag += '</tr>';
								}
							}
							$("#resultBody").append(tag);
						} else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
						} else {
							alert(json.resultMsg);
						}
					},
					complete : function() {
						cfCloseMagnificPopup();
					}
				});
			}
			
			// 엑셀 다운로드
			function downloadExcel() {
				var startDate = $("#startDate").val();
				var endDate = $("#endDate").val();
				var s_jijum = $("#s_jijum option:selected").val();
				var s_user = $("#s_user option:selected").val();
				var s_account = $("#s_account option:selected").val();
				
				if (startDate == "" || endDate == "") {
					alert("조회월(필수)을 입력해 주세요.");
					return;
				}
				if (s_jijum == "") {
					alert("지점(필수)을 입력해 주세요.");
					return;
				}
				
				// undefined check
				if (typeof s_user === "undefined") {
					s_user = "";
				}
				if (typeof s_account === "undefined") {
					s_account = "";
				}
				
				var form = document.createElement("form");
				form.name = "downForm";
				form.id = "downForm";
				form.method = "POST";
				form.action = "/sfe/sfeDownloadExcel.do";
				var auth = document.createElement("input");
				auth.type = "hidden";
				auth.name = "auth";
				auth.value = "reyon";
				var excelType = document.createElement("input");
				excelType.type = "hidden";
				excelType.name = "excelType";
				excelType.value = "50";
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
				param3.name = "s_jijum";
				param3.value = s_jijum;
				var param4 = document.createElement("input");
				param4.type = "hidden";
				param4.name = "s_user";
				param4.value = s_user;
				var param5 = document.createElement("input");
				param5.type = "hidden";
				param5.name = "s_account";
				param5.value = s_account;
				$(form).append(auth);
				$(form).append(excelType);
				$(form).append(param1);
				$(form).append(param2);
				$(form).append(param3);
				$(form).append(param4);
				$(form).append(param5);
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
								<i class="fa fa-mobile"></i> 방문 매출 현황
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-briefcase"></i>SFE 관리</li>
								<li><i class="fa fa-mobile"></i>방문 매출 현황</li>
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
											<label class="col-lg-2 control-label">조회월 (필수)</label>
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
											<label class="col-lg-2 control-label">지점 (필수)</label>
											<div class="col-lg-8">
												<select id="s_jijum" class="form-control"></select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">담당자</label>
											<div class="col-lg-8">
												<select id="s_user" class="form-control"></select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">거래처</label>
											<div class="col-lg-8">
												<select id="s_account" class="form-control"></select>
											</div>
											<div class="col-lg-2">
												<input type="text" style="display: none;" />
												<input type="hidden" id="queStr" value="${pageParam.queStr}" />
												<a class="btn btn-default" href="javascript:;" title="검색하기" onClick="javascript:goSearch();"><span class="icon_search"></span>&nbsp;검색</a>
												<a class="btn btn-default" href="javascript:;" title="엑셀 다운로드 하기" onClick="javascript:downloadExcel();"><span class="icon_download"></span>&nbsp;엑셀</a>
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
										<col style="width: 7%;">
										<col style="width: 7%;">
										<col style="width: 25%;">
										<col style="width: 23%;">
										<col style="width: 24%;">
										<col style="width: 7%;">
										<col style="width: 7%;">
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">지점</th>
											<th class="text-center">담당자</th>
											<th class="text-center">거래처</th>
											<th class="text-center">진료과</th>
											<th class="text-center">제품</th>
											<th class="text-center">CALL</th>
											<th class="text-center">매출</th>
										</tr>
									</thead>
									<tbody id="resultBody">
										<tr><td colspan="7" class="text-center">결과가 존재하지 않습니다.</td></tr>
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