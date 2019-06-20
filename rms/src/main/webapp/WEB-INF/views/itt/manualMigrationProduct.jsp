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
			cfLNBMenuSelect("menu99");
			
			// datetimepicker
			$('#outdatetime').datetimepicker({
				format: 'YYYY-MM-DD',
				sideBySide: true,
			});
		});
		
		// 조회
		function getMigration() {
			var outdate = $("#outdate").val();
			
			if (outdate == "") {
				alert("기준일을 선택해 주세요.");
				return;
			}
			
			if(confirm("조회하시겠습니까?")){
				$("#selectDiv").hide();
				$("#resultDiv").hide();
				var params = {
					auth : "reyon",
					outdate : outdate,
				}
				
				var request = $.ajax({
					url: '/itt/getMigrationProductAjax.json'
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
							$("#selectBtsTbody").empty();
							$("#selectMigTbody").empty();
							
							var btsList = json.btsList;
							var htmlBtsStr = '';
							for(var i=0; i<btsList.length; i++){
								htmlBtsStr += '<tr>';
								htmlBtsStr += '<td class="text-center">'+outdate+'</td>';
								htmlBtsStr += '<td class="text-center">'+btsList[i].cnt+'건 </td>';
								htmlBtsStr += '</tr>';
							}
							
							var migList = json.migList;
							var htmlMigStr = '';
							for(var i=0; i<migList.length; i++){
								htmlMigStr += '<tr>';
								htmlMigStr += '<td class="text-center">'+outdate+'</td>';
								htmlMigStr += '<td class="text-center">'+migList[i].cnt+'건 </td>';
								htmlMigStr += '</tr>';
							}
							
							$("#selectBtsTbody").append(htmlBtsStr);
							$("#selectMigTbody").append(htmlMigStr);
							$("#selectDiv").show();
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
		
		// 마이그레이션 시작
		function startMigration() {
			var outdate = $("#outdate").val();
			
			if (outdate == "") {
				alert("기준일을 선택해 주세요.");
				return;
			}
			
			if(confirm("해당작업이 시작되면 종료가 불가능합니다.\n인터넷 창을 종료해도 작업은 계속 진행됩니다.\n해당 화면은 최대 30분까지 대기합니다.\n작업 완료시 하단에 작업결과가 보여집니다.\n\n시작하시겠습니까?")){
				$("#resultDiv").hide();
				$("#selectDiv").hide();
				var params = {
					auth : "reyon",
					outdate : outdate,
				}
				
				var request = $.ajax({
					url: '/itt/manualMigrationProductAjax.json'
					, type : 'POST'
					, timeout: 1800000 // 최대 30분 대기
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
							$("#resultTbody").empty();
							
							var htmlStr = '';
							htmlStr += '<tr>';
							htmlStr += '<td class="text-center">'+outdate+'</td>';
							htmlStr += '<td class="text-center">'+json.deleteSize+'</td>';
							htmlStr += '<td class="text-center">'+json.originSize+'</td>';
							htmlStr += '<td class="text-center">'+json.copySize+'</td>';
							htmlStr += '<td class="text-center">'+json.startDate+'</td>';
							htmlStr += '<td class="text-center">'+json.endDate+'</td>';
							htmlStr += '</tr>';
							
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
								<i class="fa fa-database"></i> 제품 일련번호 이관
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-star-o"></i>정보관리팀</li>
								<li><i class="fa fa-database"></i>제품 일련번호 이관</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<br>
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">제품 일련번호 이관</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">개요</label>
											<div class="col-lg-10">
												<p class="form-control-static">스마트폰에서 제품 일련번호 조회를 위한 Database Migration</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">내용</label>
											<div class="col-lg-10">
												<p class="form-control-static">
												 ※ 공장 SQL Server DB (SLITEMTAKINGOUT)에서 본사 Oracle DB (BTS_SLITEMTAKINGOUT_ETC)로 데이터 복사<br>
												 1. BTS_SLITEMTAKINGOUT_ETC 테이블에서 기준일 데이터 삭제<br>
												 2. SLITEMTAKINGOUT 테이블에서 기준일 데이터 조회<br>
												 3. 조회된 데이터를 BTS_SLITEMTAKINGOUT_ETC 테이블에 등록<br>
												 4. 완료
												</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">기준일</label>
											<div class="col-lg-8">
												<div class="input-group date" id="outdatetime">
								                    <input type="text" id="outdate" class="form-control" />
								                    <span class="input-group-addon">
								                        <span class="glyphicon glyphicon-calendar"></span>
								                    </span>
								                </div>
											</div>
											<div class="col-lg-2">
												<a class="btn btn-default" href="javascript:;" title="BTS_SLITEMTAKINGOUT_ETC 의 데이터 건수 조회" onClick="javascript:getMigration();"><span class="icon_check"></span>&nbsp;조회</a>
												<a class="btn btn-default" href="javascript:;" title="SLITEMTAKINGOUT 에서 BTS_SLITEMTAKINGOUT_ETC 로 이관 시작" onClick="javascript:startMigration();"><span class="icon_check"></span>&nbsp;이관 시작</a>
											</div>
										</div>
									</form>
								</div>
							</section>
						</div>
					</div>
					<!-- body top end -->
					
					<!-- main table start -->
					<div class="row" id="selectDiv" style="display:none;">
						<div class="col-lg-6">
							<section class="panel">
								<table class="table table-bordered">
									<colgroup>
										<col style="width: 15%;">
										<col style="width: 15%;">
									</colgroup>
									<thead>
										<tr>
											<th colspan="2" class="text-center">SLITEMTAKINGOUT 데이터</th>
										</tr>
										<tr>
											<th class="text-center">기준일</th>
											<th class="text-center">원천 데이터 건수</th>
										</tr>
									</thead>
									<tbody id="selectBtsTbody">
									</tbody>
								</table>
							</section>
						</div>
						<div class="col-lg-6">
							<section class="panel">
								<table class="table table-bordered">
									<colgroup>
										<col style="width: 15%;">
										<col style="width: 15%;">
									</colgroup>
									<thead>
										<tr>
											<th colspan="2" class="text-center">BTS_SLITEMTAKINGOUT_ETC 데이터</th>
										</tr>
										<tr>
											<th class="text-center">기준일</th>
											<th class="text-center">이관 된 데이터 건수</th>
										</tr>
									</thead>
									<tbody id="selectMigTbody">
									</tbody>
								</table>
							</section>
						</div>
					</div>
					
					<div class="row" id="resultDiv" style="display:none;">
						<div class="col-lg-12">
							<section class="panel">
								<table class="table table-bordered">
									<colgroup>
										<col style="width: 15%;">
										<col style="width: 15%;">
										<col style="width: 15%;">
										<col style="width: 15%;">
										<col style="width: 20%;">
										<col style="width: 20%;">
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">기준일</th>
											<th class="text-center">기존 데이터 삭제 건수</th>
											<th class="text-center">원천 데이터 건수</th>
											<th class="text-center">이관 성공 건수</th>
											<th class="text-center">시작시간</th>
											<th class="text-center">종료시간</th>
										</tr>
									</thead>
									<tbody id="resultTbody">
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