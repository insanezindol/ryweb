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
		
		// 페이징 및 검색 파라메터 선언
		var pageParam = {
			pageNo : ""
			, pageSize : 15
			, s_sabun: ""
			, s_regName : ""
			, s_contents : ""
			, regDate : ""
			, sendDate : ""
			, receiveDate : ""
		}
		
		$(function() {
			// 진입 시 메뉴 선택
			cfLNBMenuSelect("menu09");
			
			// datetimepicker
			$('#regDatetime').datetimepicker({
				format: 'YYYY-MM-DD',
				sideBySide: true,
			});
			$('#sendDatetime').datetimepicker({
				format: 'YYYY-MM-DD',
				sideBySide: true,
			});
			$('#receiveDatetime').datetimepicker({
				format: 'YYYY-MM-DD',
				sideBySide: true,
			});
			
			// 검색어 입력영역에 엔터키 이벤트 바인드
	        $("#s_sabun").keydown(function (key) {
			    if (key.keyCode == 13) goSearch();
			});
	        $("#s_regName").keydown(function (key) {
			    if (key.keyCode == 13) goSearch();
			});
	        $("#s_contents").keydown(function (key) {
			    if (key.keyCode == 13) goSearch();
			});
	     	
			// 검색목록 선택, 목록 상자에 검색조건의 값은 ""으로 셋팅 필요
			$("#s_sabun").val("${pageParam.s_sabun}");
			$("#s_regName").val("${pageParam.s_regName}");
			$("#s_contents").val("${pageParam.s_contents}");
			$("#regDate").val("${pageParam.regDate}");
			$("#sendDate").val("${pageParam.sendDate}");
			$("#receiveDate").val("${pageParam.receiveDate}");
			
			//페이징 처리
			$('#pagingDiv').html(cfGetPagingHtml('${pageParam.totalCount}', '${pageParam.pageNo}', '${pageParam.pageSize}', 'goPage'));
			$("#pageSize").val("${pageParam.pageSize}").prop("selected", true);
			
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
		
		// 페이징 파라메터 설정
		function goPage(pageNo){
			pageParam.pageNo = pageNo;
			pageParam.pageSize = '${pageParam.pageSize}';
			pageParam.s_sabun = '${pageParam.s_sabun}';
			pageParam.s_regName = '${pageParam.s_regName}';
			pageParam.s_contents = '${pageParam.s_contents}';
			pageParam.regDate = '${pageParam.regDate}';
			pageParam.sendDate = '${pageParam.sendDate}';
			pageParam.receiveDate = '${pageParam.receiveDate}';
			goUrl();
		}
		
		// 검색 파라메터 설정
		function goSearch(){
			pageParam.pageNo = 1;
			pageParam.pageSize = '${pageParam.pageSize}';
			pageParam.s_sabun = $("#s_sabun").val();
			pageParam.s_regName = $("#s_regName").val();
			pageParam.s_contents = $("#s_contents").val();
			pageParam.regDate = $("#regDate").val();
			pageParam.sendDate = $("#sendDate").val();
			pageParam.receiveDate = $("#receiveDate").val();
			goUrl();
		}
		
		// 페이지당 노출수 설정
		function goSize() {
			pageParam.pageNo = '${pageParam.pageNo}';
			pageParam.pageSize = $("#pageSize option:selected").val();
			pageParam.s_sabun = '${pageParam.s_sabun}';
			pageParam.s_regName = '${pageParam.s_regName}';
			pageParam.s_contents = '${pageParam.s_contents}';
			pageParam.regDate = '${pageParam.regDate}';
			pageParam.sendDate = '${pageParam.sendDate}';
			pageParam.receiveDate = '${pageParam.receiveDate}';
			goUrl();
		}
		
		// 페이지 이동
		function goUrl(){
			location.href="/app/pcMessageList.do?pageNo="+pageParam.pageNo+"&pageSize="+pageParam.pageSize+"&s_sabun="+encodeURIComponent(pageParam.s_sabun, "UTF-8")+"&s_regName="+encodeURIComponent(pageParam.s_regName, "UTF-8")+"&s_contents="+encodeURIComponent(pageParam.s_contents, "UTF-8")+"&regDate="+encodeURIComponent(pageParam.regDate, "UTF-8")+"&sendDate="+encodeURIComponent(pageParam.sendDate, "UTF-8")+"&receiveDate="+encodeURIComponent(pageParam.receiveDate, "UTF-8");
		}
		
		// 등록 페이지로 이동
		function goAddPage(){
			var url = "/app/pcMessageAdd.do";
			location.href = url;
		}
		
		// 메시지 삭제
		function deleteMessage(messageSeq){
			if(confirm("삭제 하시겠습니까?")){
				var params = {
					auth : "reyon",
					messageSeq : messageSeq,
				}
				
				var request = $.ajax({
					url: '/app/pcMessageDeleteAjax.json'
					, type : 'POST'
					, timeout: 30000
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
							alert("삭제 완료 되었습니다.");
							location.reload();
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
								<i class="fa fa-list"></i> PC 메시지 전송 목록
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-desktop"></i>PC 관리</li>
								<li><i class="fa fa-list"></i>PC 메시지 전송 목록</li>
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
											<label class="col-lg-2 control-label">사번</label>
											<div class="col-lg-8">
												<input type="text" id="s_sabun" class="form-control" maxlength="50">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">이름</label>
											<div class="col-lg-8">
												<input type="text" id="s_regName" class="form-control" maxlength="50">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">내용</label>
											<div class="col-lg-8">
												<input type="text" id="s_contents" class="form-control" maxlength="50">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">등록일자</label>
											<div class="col-lg-8">
								                <div class="input-group date" id="regDatetime">
								                    <input type="text" id="regDate" class="form-control" />
								                    <span class="input-group-addon">
								                        <span class="glyphicon glyphicon-calendar"></span>
								                    </span>
								                </div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">발송일자</label>
											<div class="col-lg-8">
								                <div class="input-group date" id="sendDatetime">
								                    <input type="text" id="sendDate" class="form-control" />
								                    <span class="input-group-addon">
								                        <span class="glyphicon glyphicon-calendar"></span>
								                    </span>
								                </div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">수신일자</label>
											<div class="col-lg-8">
								                <div class="input-group date" id="receiveDatetime">
								                    <input type="text" id="receiveDate" class="form-control" />
								                    <span class="input-group-addon">
								                        <span class="glyphicon glyphicon-calendar"></span>
								                    </span>
								                </div>
											</div>
											<div class="col-lg-2">
												<input type="text" style="display: none;" />
												<input type="hidden" id="queStr" value="${pageParam.queStr}" />
												<a class="btn btn-default" href="javascript:;" title="검색하기" onClick="javascript:goSearch();"><span class="icon_search"></span>&nbsp;검색</a>
											</div>
										</div>
									</form>
								</div>
							</section>
						</div>
					</div>
					<!-- search bar end -->
					
					<!-- pageSize bar start -->
					<div class="row">
						<div class="col-lg-12">
							<table style="width: 100%">
								<colgroup>
									<col width="50%">
									<col width="50%">
								</colgroup>
								<tbody>
									<tr>
										<td>총 <fmt:formatNumber value="${pageParam.totalCount }" pattern="#,###" /> 건</td>
										<td style="text-align: right;">
											<select class="input-sm m-bot15" name="pageSize" id="pageSize"  onChange="javascript:goSize();">
												<option value="15">15건씩 보기</option>
												<option value="30">30건씩 보기</option>
												<option value="50">50건씩 보기</option>
												<option value="100">100건씩 보기</option>
											</select>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<!-- pageSize bar end -->
					
					<!-- main table start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel">
								<table class="table table-bordered">
									<colgroup>
										<col style="width: 5%;">
										<col style="width: 10%;">
										<col style="width: 10%;">
										<col style="width: 19%;">
										<col style="width: 14%;">
										<col style="width: 14%;">
										<col style="width: 14%;">
										<col style="width: 7%;">
										<col style="width: 7%;">
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">번호</th>
											<th class="text-center">사번</th>
											<th class="text-center">이름</th>
											<th class="text-center">내용</th>
											<th class="text-center">등록시간</th>
											<th class="text-center">발송시간</th>
											<th class="text-center">수신시간</th>
											<th class="text-center">수신여부</th>
											<th class="text-center">삭제</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${fn:length(list) == 0}">
												<tr style="letter-spacing: -1px">
													<td class="text-center" colspan="9">결과가 존재하지 않습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="result" items="${list}" varStatus="status">
													<tr>
														<td class="text-center">${(pageParam.totalCount - (pageParam.pageSize * (pageParam.pageNo - 1))) - status.index }</td>
														<td class="text-center">${result.username }</td>
														<td class="text-center">${result.kname }</td>
														<td class="text-center">${result.contents }</td>
														<td class="text-center">${result.regDate }</td>
														<td class="text-center">${result.sendDate }</td>
														<td class="text-center">${result.receiveDate }</td>
														<td class="text-center">${result.messageYn }</td>
														<td class="text-center"><a class="btn btn-default btn-sm" href="javascript:;" title="삭제하기" onClick="javascript:deleteMessage('${result.messageSeq }');"><span class="icon_trash"></span>&nbsp;삭제</a></td>
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
					
					<!-- page navigation start -->
					<div class="btn-row text-center" id="pagingDiv"></div>
					<!-- page navigation end -->
					
					<!-- bottom button start -->
					<div class="btn-row text-right">
						<a class="btn btn-default" href="javascript:;" title="등록하기" style="margin-top: 7px;" onClick="javascript:goAddPage();"><span class="icon_pencil"></span>&nbsp;등록</a>
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