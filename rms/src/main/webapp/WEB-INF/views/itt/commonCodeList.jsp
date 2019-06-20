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
			, s_grpCode: ""
			, s_grpName : ""
			, s_codeName : ""
			, s_useGbn : ""
		}
		
		$(function() {
			// 진입 시 메뉴 선택
			cfLNBMenuSelect("menu99");
			
			// 검색어 입력영역에 엔터키 이벤트 바인드
	        $("#s_grpCode").keydown(function (key) {
			    if (key.keyCode == 13) goSearch();
			});
	        $("#s_grpName").keydown(function (key) {
			    if (key.keyCode == 13) goSearch();
			});
	        $("#s_codeName").keydown(function (key) {
			    if (key.keyCode == 13) goSearch();
			});
	        $("#s_useGbn").keydown(function (key) {
			    if (key.keyCode == 13) goSearch();
			});
	     	
			// 검색목록 선택, 목록 상자에 검색조건의 값은 ""으로 셋팅 필요
			$("#s_grpCode").val("${pageParam.s_grpCode}");
			$("#s_grpName").val("${pageParam.s_grpName}");
			$("#s_codeName").val("${pageParam.s_codeName}");
			$("#s_useGbn").val("${pageParam.s_useGbn}");
			
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
			pageParam.s_grpCode = '${pageParam.s_grpCode}';
			pageParam.s_grpName = '${pageParam.s_grpName}';
			pageParam.s_codeName = '${pageParam.s_codeName}';
			pageParam.s_useGbn = '${pageParam.s_useGbn}';
			goUrl();
		}
		
		// 검색 파라메터 설정
		function goSearch(){
			pageParam.pageNo = 1;
			pageParam.pageSize = '${pageParam.pageSize}';
			pageParam.s_grpCode = $("#s_grpCode").val();
			pageParam.s_grpName = $("#s_grpName").val();
			pageParam.s_codeName = $("#s_codeName").val();
			pageParam.s_useGbn = $("#s_useGbn").val();
			goUrl();
		}
		
		// 페이지당 노출수 설정
		function goSize() {
			pageParam.pageNo = '${pageParam.pageNo}';
			pageParam.pageSize = $("#pageSize option:selected").val();
			pageParam.s_grpCode = '${pageParam.s_grpCode}';
			pageParam.s_grpName = '${pageParam.s_grpName}';
			pageParam.s_codeName = '${pageParam.s_codeName}';
			pageParam.s_useGbn = '${pageParam.s_useGbn}';
			goUrl();
		}
		
		// 페이지 이동
		function goUrl(){
			location.href="/itt/commonCodeList.do?pageNo="+pageParam.pageNo+"&pageSize="+pageParam.pageSize+"&s_grpCode="+encodeURIComponent(pageParam.s_grpCode, "UTF-8")+"&s_grpName="+encodeURIComponent(pageParam.s_grpName, "UTF-8")+"&s_codeName="+encodeURIComponent(pageParam.s_codeName, "UTF-8")+"&s_useGbn="+encodeURIComponent(pageParam.s_useGbn, "UTF-8");
		}
		
		// 상세 페이지로 이동
		function goDetailPage(grpCode, code){
			var url = "/itt/commonCodeView.do?grpCode=" + grpCode + "&code=" + code;
			var queStr = $("#queStr").val();
			if(queStr != ""){
				url += "&queStr="+queStr;
			}
			location.href = url;
		}
		
		// 등록 페이지로 이동
		function goAddPage(){
			var url = "/itt/commonCodeAdd.do";
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
								<i class="fa fa-list"></i> 공통 코드 목록
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-star-o"></i>정보관리팀</li>
								<li><i class="fa fa-book"></i>공통 코드 관리</li>
								<li><i class="fa fa-list"></i>공통 코드 목록</li>
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
											<label class="col-lg-2 control-label">그룹코드</label>
											<div class="col-lg-8">
												<input type="text" id="s_grpCode" class="form-control" maxlength="50">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">그룹이름</label>
											<div class="col-lg-8">
												<input type="text" id="s_grpName" class="form-control" maxlength="50">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">코드이름</label>
											<div class="col-lg-8">
								                <input type="text" id="s_codeName" class="form-control" maxlength="50">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">사용구분</label>
											<div class="col-lg-8">
								                <input type="text" id="s_useGbn" class="form-control" maxlength="50">
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
										<col style="width: 25%;">
										<col style="width: 30%;">
										<col style="width: 10%;">
										<col style="width: 10%;">
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">번호</th>
											<th class="text-center">그룹코드</th>
											<th class="text-center">코드</th>
											<th class="text-center">그룹이름</th>
											<th class="text-center">코드이름</th>
											<th class="text-center">사용구분</th>
											<th class="text-center">정렬구분</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${fn:length(list) == 0}">
												<tr style="letter-spacing: -1px">
													<td class="text-center" colspan="7">결과가 존재하지 않습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="result" items="${list}" varStatus="status">
													<tr>
														<td class="text-center">${(pageParam.totalCount - (pageParam.pageSize * (pageParam.pageNo - 1))) - status.index }</td>
														<td class="text-center">${result.grpCode }</td>
														<td class="text-center">${result.code }</td>
														<td class="text-center"><a href="javascript:goDetailPage('${result.grpCode }','${result.code }');">${result.grpName }</a></td>
														<td class="text-center">${result.codeName }</td>
														<td class="text-center">${result.useGbn }</td>
														<td class="text-center">${result.sortGbn }</td>
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