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
		
			// 페이징 및 검색 파라메터 선언
			var pageParam = {
				pageNo : ""
				, pageSize : 15
				, searchType: "meetingStatus"
				, searchText : ""
				, startDate : ""
				, endDate : ""
			}
			
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu12");
				
				//페이징 처리
				$('#pagingDiv').html(cfGetPagingHtml('${pageParam.totalCount}', '${pageParam.pageNo}', '${pageParam.pageSize}', 'goPage'));
				
				// 검색값 설정
				$("#searchText").val("${pageParam.searchText}").prop("selected", true);
			});
			
			// 페이징 파라메터 설정
			function goPage(pageNo){
				pageParam.pageNo = pageNo;
				pageParam.searchType = '${pageParam.searchType}';
				pageParam.searchText = '${pageParam.searchText}';
				pageParam.startDate = '${pageParam.startDate}';
				pageParam.endDate = '${pageParam.endDate}';
				goUrl();
			}
			
			// 검색 파라메터 설정
			function goSearch(){
				pageParam.pageNo = 1;
				pageParam.searchText = $("#searchText").val();
				goUrl();
			}
			
			// 페이지 이동
			function goUrl(){
				location.href="/toDo.do?pageNo="+pageParam.pageNo+"&pageSize="+pageParam.pageSize+"&searchType="+pageParam.searchType+"&searchText="+encodeURIComponent(pageParam.searchText, "UTF-8");
			}
			
			// 뒤로 가기
			function goMain(){
				location.href = "/main.do";
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
								<i class="fa fa-list-ol"></i> 진행 대기 내역
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-list-ol"></i>진행 대기 내역</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- search bar start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel">
								<!-- <header class="panel-heading">검색</header> -->
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">검색조건</label>
											<label class="col-lg-8">
												<select id="searchText" class="form-control" style="margin-bottom: 10px;">
													<option value="">전체</option>
													<option value="NR">결과미등록</option>
													<option value="NC">결재대기</option>
													<option value="RD">결재반려</option>
												</select>
											</label>
											<div class="col-lg-2">
												<input type="text" style="display: none;" />
												<a class="btn btn-default" href="javascript:;" title="검색하기" onClick="javascript:goSearch();"><span class="icon_search"></span>&nbsp;검색</a>
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
										<col style="width: 10%;">
										<col style="width: 25%;">
										<col style="width: 40%;">
										<col style="width: 13%;">
										<col style="width: 12%;">
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">번호</th>
											<th class="text-center">회의 일시</th>
											<th class="text-center">제목</th>
											<th class="text-center">장소</th>
											<th class="text-center">상태</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${fn:length(list) == 0}">
												<tr style="letter-spacing: -1px">
													<td class="text-center" colspan="5">결과가 존재하지 않습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="result" items="${list}" varStatus="status">
													<tr>
														<td class="text-center">${(pageParam.totalCount - (pageParam.pageSize * (pageParam.pageNo - 1))) - status.index }</td>
														<td class="text-center">${result.meetingStartDate } ~ ${result.meetingEndDate }</td>
														<td class="text-center">${result.meetingName }</td>
														<td class="text-center">${result.codeName }</td>
														<td class="text-center">
															<c:if test="${result.meetingStatus == '02' && result.meetingType == '01' }">
															<a href="/meeting/meetingResultAdd.do?meetingSeq=${result.meetingSeq }" data-toggle="tooltip" title="결과등록하기">결과미등록</a>
															</c:if>
															<c:if test="${result.meetingStatus == '02' && result.meetingType == '02' }">
															<a href="/visitor/visitorResultAdd.do?meetingSeq=${result.meetingSeq }" data-toggle="tooltip" title="결과등록하기">결과미등록</a>
															</c:if>
															<c:if test="${ (result.meetingStatus == '03' || result.meetingStatus == '04') && result.meetingType == '01' }">
															<a href="/meeting/meetingView.do?meetingSeq=${result.meetingSeq }" data-toggle="tooltip" title="결재대기">결재대기</a>
															</c:if>
															<c:if test="${ (result.meetingStatus == '03' || result.meetingStatus == '04') && result.meetingType == '02' }">
															<a href="/visitor/visitorView.do?meetingSeq=${result.meetingSeq }" data-toggle="tooltip" title="결재대기">결재대기</a>
															</c:if>
															<c:if test="${result.meetingStatus == '99' && result.meetingType == '01' }">
															<a href="/meeting/meetingView.do?meetingSeq=${result.meetingSeq }" data-toggle="tooltip" title="결재반려">결재반려</a>
															</c:if>
															<c:if test="${result.meetingStatus == '99' && result.meetingType == '02' }">
															<a href="/visitor/visitorView.do?meetingSeq=${result.meetingSeq }" data-toggle="tooltip" title="결재반려">결재반려</a>
															</c:if>
														</td>
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