<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>이연제약 관리 시스템</title>
		<!-- Bootstrap CSS -->
		<link href="/css/bootstrap.min.css" rel="stylesheet">
		<link href="/css/bootstrap-theme.css?ver=20190619" rel="stylesheet">
		<link href="/css/elegant-icons-style.css" rel="stylesheet" />
		<link href="/css/font-awesome.min.css" rel="stylesheet" />
		<!-- Custom styles -->
		<link href="/css/style.css?ver=20190619" rel="stylesheet">
		<link href="/css/style-responsive.css" rel="stylesheet" />
		<link href="/css/jquery-ui-1.10.4.min.css" rel="stylesheet">
		<!-- full calendar css-->
  		<link href="/css/fullcalendar.min.css?ver=20190619" rel="stylesheet" />
  		<link href="/css/fullcalendar.print.min.css?ver=20190619" rel="stylesheet" media="print" />
  		<style type="text/css">.fc-content:hover{cursor: pointer;}</style>
		<!-- jquery script -->
		<script type="text/javascript" src="/js/jquery-1.8.3.min.js"></script>
		<script type="text/javascript" src="/js/jquery-ui-1.10.4.min.js"></script>
		<script type="text/javascript" src="/js/jquery-ui-1.9.2.custom.min.js"></script>
		<!-- bootstrap script -->
		<script type="text/javascript" src="/js/bootstrap.min.js"></script>
		<!-- nice scroll script -->
		<!-- <script type="text/javascript" src="/js/jquery.scrollTo.min.js"></script>
		<script type="text/javascript" src="/js/jquery.nicescroll.js"></script> -->
		<!-- moment script -->
  		<script type="text/javascript" src="/js/moment.js"></script>
		<!-- full calendar script -->
		<script type="text/javascript" src="/js/fullcalendar.min.js?ver=20190619"></script>
		<script type="text/javascript" src="/js/fullcalendar-ko.js?ver=20190619"></script>
		<!-- common function -->
		<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
		<!-- page script -->
		<script type="text/javascript">
		
			// 페이징 및 검색 파라메터 선언
			var pageParam = {
				pageNo : ""
				, pageSize : 15
				, s_auth: ""
				, s_content : ""
				, regDate : ""
			}
		
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu17");
				
				// datetimepicker
				$('#regDate1').datetimepicker({
					format: 'YYYY-MM-DD',
					sideBySide: true,
				});
				
				// 검색목록 선택, 목록 상자에 검색조건의 값은 ""으로 셋팅 필요
				$("#s_content").val("${pageParam.s_content}");
				$("#regDate").val("${pageParam.regDate}");
				
				// 페이징 처리 << 나중에 지워도 되면 지워야함 "중복" ---------------------------
				$('#pagingDiv').html(cfGetPagingHtml('${pageParam.totalCount}', '${pageParam.pageNo}', '${pageParam.pageSize}', 'goPage'));
				$("#pageSize").val("${pageParam.pageSize}").prop("selected", true);
				
				$("#s_content").keydown(function (key) {
				    if (key.keyCode == 13) goSearch();
				});
				
				// 페이징 처리
				$('#pagingDiv').html(cfGetPagingHtml('${pageParam.totalCount}', '${pageParam.pageNo}', '${pageParam.pageSize}', 'goPage'));
				$("#pageSize").val("${pageParam.pageSize}").prop("selected", true);
				
			});
			
				// 페이징 파라메터 설정
				function goPage(pageNo){
					pageParam.pageNo = pageNo;
					pageParam.pageSize = '${pageParam.pageSize}';
					pageParam.s_content = '${pageParam.s_content}';
					pageParam.regDate = '${pageParam.regDate}';
					goUrl();
				};
				
				// 검색 파라메터 설정
				function goSearch(){
					pageParam.pageNo = 1;
					pageParam.pageSize = '${pageParam.pageSize}';
					pageParam.s_content = $("#s_content").val();
					pageParam.regDate = $("#regDate").val();
					pageParam.s_gubun = $("#conyn option:selected").val();
					goUrl();
				};
				
				// 페이지당 노출수 설정
				function goSize() {
					pageParam.pageNo = '${pageParam.pageNo}';
					pageParam.pageSize = $("#pageSize option:selected").val();
					pageParam.s_auth = '${pageParam.s_auth}';
					pageParam.s_content = '${pageParam.s_content}';
					pageParam.regDate = '${pageParam.regDate}';
					goUrl();
				};
				
				// 페이지 이동
				function goUrl(){
					location.href="/control/controlList.do?pageNo="+pageParam.pageNo+"&pageSize="+pageParam.pageSize+"&regDate="+encodeURIComponent(pageParam.regDate, "UTF-8")+"&s_content="+encodeURIComponent(pageParam.s_content, "UTF-8")+"&s_gubun="+encodeURIComponent(pageParam.s_gubun, "UTF-8");
				};
				
				// 상세 페이지로 이동
				function goDetailPage(num){
					var url = "/control/controlView.do?num=" + num;
					var queStr = $("#queStr").val();
					if(queStr != ""){
						url += "&queStr="+queStr;
					}
					location.href = url;
				}
				
				// 등록 페이지로 이동
				function goAddPage(){
					var url = "/control/controlAdd.do";
					var queStr = $("#queStr").val();
					if(queStr != ""){
						url += "?queStr="+queStr;
					}
					location.href = url;
				};
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
								<i class="fa fa-home"></i> 재고자산 통제 관리&nbsp;&nbsp;&nbsp;&nbsp;
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-home"></i>재고자산 통제 관리</li>
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
											<label class="col-lg-2 control-label">품명</label>
											<div class="col-lg-8">
												<input type="text" id="s_content" class="form-control" maxlength="50">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">처리상태</label>
											<div class="col-lg-8">
												<select id="conyn" class="form-control">
													<option value>== 처리 상태 ==</option>
													<option value="YES">통제중</option>
													<option value="NO">해제</option>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">등록일자</label>
											<div class="col-lg-8">
												<div class="input-group date" id="regDate1">
								                    <input type="text" id="regDate" class="form-control" />
								                    <span class="input-group-addon">
								                        <span class="glyphicon glyphicon-calendar"></span>
								                    </span>
								                </div>
											</div>
											<div class="col-lg-2">
												<input type="text" style="display: none;" />
												<input type="hidden" id="queStr" value="${pageParam.queStr}" />
												<a class="btn btn-default" href="javascript:;" title="검색하기" style="margin-top: 7px;" onClick="javascript:goSearch();"><span class="icon_search"></span>&nbsp;검색</a>
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
										<col style="width: 10%;">
										<col style="width: 10%;">
										<col style="width: 40%;">
										<col style="width: 20%;">
										<col style="width: 10%;">
										<col style="width: 10%;">
										<col style="width: 20%;">
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">번호</th>
											<th class="text-center">구분</th>
											<th class="text-center">품명</th>
											<th class="text-center">내용</th>
											<th class="text-center">규격</th>
											<th class="text-center">상태</th>
											<th class="text-center">등록일자</th>
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
														<td class="text-center">
															<c:choose>
																<c:when test="${result.congbn == 1 }">제품</c:when>
																<c:when test="${result.congbn == 2 }">자재</c:when>
															</c:choose>
														</td>
														<td class="text-center"><a href="javascript:goDetailPage('${result.reqno }');">${result.jpmnm }</a></td>
														<td class="text-center">${result.condesc }</td>
														<td class="text-center">${result.gyugk }</td>
														<td class="text-center">
															<c:choose>
																<c:when test="${result.conyn == 'YES'}">통제중</c:when>
																<c:when test="${result.conyn == 'NO' }">해제</c:when>
															</c:choose>
														</td>
														<td class="text-center">${result.insdate }</td>
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
		<!-- container section end -->
		
		
	</body>
</html>