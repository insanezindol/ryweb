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
			
			// 검색어 입력영역에 엔터키 이벤트 바인드
			$("#yyyy").keydown(function (key) {
			    if (key.keyCode == 13) goSearch();
			});
			$("#chitNum").keydown(function (key) {
			    if (key.keyCode == 13) goSearch();
			});
		});
		
		// 조회
		function goSearch() {
			var yyyy = $("#yyyy").val();
			var chitNum = $("#chitNum").val();
			
			if (yyyy == "") {
				alert("년도를 입력해 주세요.");
				return;
			}
			
			if (chitNum == "") {
				alert("전표번호를 입력해 주세요.");
				return;
			}
			
			$("#resultDiv").hide();
			$("#resultDiv1").hide();
			var params = {
				auth : "reyon",
				yyyy : yyyy,
				chitNum : chitNum,
			}
			
			var request = $.ajax({
				url: '/itt/getGroupwareChitAjax.json'
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
						var info1 = json.info1;
						var info2 = json.info2;
						$("#resultTbody").empty();
						$("#resultTbody1").empty();
						
						var htmlStr = '';
						if(info1 == null){
							htmlStr += '<tr>';
							htmlStr += '<td class="text-center" colspan="8">조회된 자료가 없습니다.</td>';
							htmlStr += '</tr>';
						} else {
							htmlStr += '<tr>';
							htmlStr += '<td class="text-center">'+info1.tsYy+'</td>';
							htmlStr += '<td class="text-center">'+info1.tsNo+'</td>';
							htmlStr += '<td class="text-center">'+info1.issUer+'</td>';
							htmlStr += '<td class="text-center">'+info1.kname+'</td>';
							htmlStr += '<td class="text-center">'+info1.tsStatus+'</td>';
							htmlStr += '<td class="text-center">'+info1.gwIcheDatetime+'</td>';
							htmlStr += '<td class="text-center">'+info1.gwStatus+'</td>';
							htmlStr += '<td class="text-center"><a class="btn btn-success btn-sm" href="javascript:;" title="초기화" title="상태 초기화" onclick="javascript:goReset();"><span class="icon_refresh"></span>&nbsp;초기화</a></td>';
							htmlStr += '</tr>';
							
						}
						
						var htmlStr1 = '';
						if(info2 == null){
							htmlStr1 += '<tr>';
							htmlStr1 += '<td class="text-center" colspan="5">조회 된 자료가 없습니다.</td>';
							htmlStr1 += '</tr>';
						} else {
							htmlStr1 += '<tr>';
							htmlStr1 += '<td class="text-center">'+info2.approKey+'</td>';
							htmlStr1 += '<td class="text-center">'+info2.approState+'</td>';
							htmlStr1 += '<td class="text-center">'+info2.gwToMisStatus+'</td>';
							htmlStr1 += '<td class="text-center">'+info2.gwToMisDatetime+'</td>';
							htmlStr1 += '<td class="text-center"><a class="btn btn-danger btn-sm" href="javascript:;" title="삭제" title="연동 데이터 삭제" onclick="javascript:goDelete();"><span class="icon_close_alt2"></span>&nbsp;삭제</a></td>';
							htmlStr1 += '</tr>';
						}
						
						$("#resultTbody").append(htmlStr);
						$("#resultTbody1").append(htmlStr1);
						$("#resultDiv").show();
						$("#resultDiv1").show();
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
		
		// 상태 초기화
		function goReset() {
			var yyyy = $("#yyyy").val();
			var chitNum = $("#chitNum").val();
			
			if (yyyy == "") {
				alert("년도를 입력해 주세요.");
				return;
			}
			
			if (chitNum == "") {
				alert("전표번호를 입력해 주세요.");
				return;
			}
			
			if (confirm("["+yyyy+"-"+chitNum+"] 초기화 하려고하는 전표번호가 맞습니까?\n[확인]을 클릭하시면 해당 전표의 그룹웨어 상태가 초기화 됩니다.")) {
				var params = {
					auth : "reyon",
					yyyy : yyyy,
					chitNum : chitNum,
					actionCode : "upd",
				}
				
				var request = $.ajax({
					url: '/itt/setGroupwareChitAjax.json'
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
						if (json.resultCode == 1){
							alert("해당 전표의 그룹웨어 상태가 초기화 되었습니다.");
							goSearch();
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
		
		// 연동 데이터 삭제
		function goDelete() {
			var yyyy = $("#yyyy").val();
			var chitNum = $("#chitNum").val();
			
			if (yyyy == "") {
				alert("년도를 입력해 주세요.");
				return;
			}
			
			if (chitNum == "") {
				alert("전표번호를 입력해 주세요.");
				return;
			}
			
			if (confirm("["+yyyy+"-"+chitNum+"] 삭제 하려고하는 전표번호가 맞습니까?\n[확인]을 클릭하시면 해당 전표의 그룹웨어 연동 데이터가 삭제 됩니다.")) {
				var params = {
					auth : "reyon",
					yyyy : yyyy,
					chitNum : chitNum,
					actionCode : "del",
				}
				
				var request = $.ajax({
					url: '/itt/setGroupwareChitAjax.json'
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
						if (json.resultCode == 1){
							alert("해당 전표의 그룹웨어 연동 데이터가 삭제 되었습니다.");
							goSearch();
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
								<i class="fa fa-repeat"></i> 전표 전자결재 초기화
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-star-o"></i>정보관리팀</li>
								<li><i class="fa fa-repeat"></i>전표 전자결재 초기화</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<br>
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel">
								<header class="panel-heading">전표 전자결재 초기화</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">사용방법</label>
											<div class="col-lg-10">
												<span>1. 년도와 전표번호를 입력한다.</span><br>
												<span>2. [조회] 버튼을 클릭한다.</span><br>
												<span>3. 하단에 2개의 테이블이 보인다.</span><br>
												<span style="margin-left:10px;"> 1) 상단 테이블은 그룹웨어 인터페이스 DB의 전자결재 연동 테이블 내용이다.</span><br>
												<span style="margin-left:10px;"> 2) 하단 테이블은 회계 DB의 전표 테이블 내용이다.</span><br>
												<span>4. 그룹웨어 DB(GWIF) 쪽 박스안에 있는 [연동 데이터 삭제]를 클릭한다.</span><br>
												<span style="margin-left:10px;"> → GWIF.dbo.tblMisAppro : APPRO_KEY</span><br>
												<span>5. 회계 DB(RYACC) 쪽 박스안에 있는 [상태 초기화]를 클릭한다.</span><br>
												<span style="margin-left:10px;"> → RYACC.RYATS01MT : GW_STATUS = NULL, GW_ICHE_DATETIME = NULL</span><br>
												<span style="color: red;">6. 그룹웨어 관리자로 로그인 한 후 해당 전표를 [완전삭제] 해준다.</span><br>
												<span>7. 완료</span>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">년도</label>
											<div class="col-lg-8">
												<input type="text" id="yyyy" class="form-control" maxlength="4" placeholder="ex) 2019">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">전표번호</label>
											<div class="col-lg-8">
												<input type="text" id="chitNum" class="form-control" maxlength="8" placeholder="ex) 123456">
											</div>
											<div class="col-lg-2">
												<input type="text" style="display: none;" />
												<a class="btn btn-default" href="javascript:;" title="조회" onClick="javascript:goSearch();"><span class="icon_check"></span>&nbsp;조회</a>
											</div>
										</div>
									</form>
								</div>
							</section>
						</div>
					</div>
					<!-- body top end -->
					
					<!-- main table start -->
					<div class="row" id="resultDiv1" style="display:none;">
						<div class="col-lg-12">
							<section class="panel">
								<header class="panel-heading">그룹웨어 DB (GWIF)</header>
								<div class="panel-body">
									<table class="table table-bordered">
										<colgroup>
											<col style="width: 20%;">
											<col style="width: 20%;">
											<col style="width: 20%;">
											<col style="width: 20%;">
											<col style="width: 20%;">
										</colgroup>
										<thead>
											<tr>
												<th class="text-center">전자결재 키</th>
												<th class="text-center">전자결재 상태</th>
												<th class="text-center">회계 테이블 적용 여부</th>
												<th class="text-center">회계 테이블 적용 시간</th>
												<th class="text-center">연동 데이터 삭제</th>
											</tr>
										</thead>
										<tbody id="resultTbody1">
										</tbody>
									</table>
								</div>
							</section>
						</div>
					</div>
					
					<div class="row" id="resultDiv" style="display:none;">
						<div class="col-lg-12">
							<section class="panel">
								<header class="panel-heading">회계 DB(RYACC)</header>
								<div class="panel-body">
									<table class="table table-bordered">
										<colgroup>
											<col style="width: 12.5%;">
											<col style="width: 12.5%;">
											<col style="width: 12.5%;">
											<col style="width: 12.5%;">
											<col style="width: 12.5%;">
											<col style="width: 12.5%;">
											<col style="width: 12.5%;">
											<col style="width: 12.5%;">
										</colgroup>
										<thead>
											<tr>
												<th class="text-center">년도</th>
												<th class="text-center">번호</th>
												<th class="text-center">사번</th>
												<th class="text-center">이름</th>
												<th class="text-center">전표 상태</th>
												<th class="text-center">그룹웨어IF시간</th>
												<th class="text-center">그룹웨어 상태</th>
												<th class="text-center">상태 초기화</th>
											</tr>
										</thead>
										<tbody id="resultTbody">
										</tbody>
									</table>
								</div>
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