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
				, pageSize : 30
				, s_divide : ""
				, s_gubun : ""
				, s_user : ""
				, startDate : ""
				, endDate : ""
				, s_paid : ""
				, s_status : ""
			}
		
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu07");
				
				// datetimepicker
				$('#startDatetime').datetimepicker({
					format: 'YYYY-MM-DD',
					sideBySide: true,
				});
				$('#endDatetime').datetimepicker({
					format: 'YYYY-MM-DD',
					sideBySide: true,
					useCurrent: false,
				});
				$("#startDatetime").on("dp.change", function (e) {
		            $('#endDatetime').data("DateTimePicker").minDate(e.date);
		        });
		        $("#endDatetime").on("dp.change", function (e) {
		            $('#startDatetime').data("DateTimePicker").maxDate(e.date);
		        });
		        
		        // 검색어 입력영역에 엔터키 이벤트 바인드
		        $("#s_user").keydown(function (key) {
				    if (key.keyCode == 13) goSearch();
				});
		        
		     	// 검색목록 선택, 목록 상자에 검색조건의 값은 ""으로 셋팅 필요
				$("#s_divide").val("${pageParam.s_divide}").prop("selected", true);
				$("#s_gubun").val("${pageParam.s_gubun}").prop("selected", true);
				$("#s_user").val("${pageParam.s_user}");
				$("#startDate").val("${pageParam.startDate}");
				$("#endDate").val("${pageParam.endDate}");
				$("#s_paid").val("${pageParam.s_paid}").prop("selected", true);
				$("#s_status").val("${pageParam.s_status}").prop("selected", true);
				
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
				pageParam.s_divide = '${pageParam.s_divide}';
				pageParam.s_gubun = '${pageParam.s_gubun}';
				pageParam.s_user = '${pageParam.s_user}';
				pageParam.startDate = '${pageParam.startDate}';
				pageParam.endDate = '${pageParam.endDate}';
				pageParam.s_paid = '${pageParam.s_paid}';
				pageParam.s_status = '${pageParam.s_status}';
				goUrl();
			}
			
			// 검색 파라메터 설정
			function goSearch(){
				pageParam.pageNo = 1;
				pageParam.pageSize = '${pageParam.pageSize}';
				pageParam.s_divide = $("#s_divide option:selected").val();
				pageParam.s_gubun = $("#s_gubun option:selected").val();
				pageParam.s_user = $("#s_user").val();
				pageParam.startDate = $("#startDate").val();
				pageParam.endDate = $("#endDate").val();
				pageParam.s_paid = $("#s_paid option:selected").val();
				pageParam.s_status = $("#s_status option:selected").val();
				goUrl();
			}
			
			// 페이지당 노출수 설정
			function goSize() {
				pageParam.pageNo = '${pageParam.pageNo}';
				pageParam.pageSize = $("#pageSize option:selected").val();
				pageParam.s_divide = '${pageParam.s_divide}';
				pageParam.s_gubun = '${pageParam.s_gubun}';
				pageParam.s_user = '${pageParam.s_user}';
				pageParam.startDate = '${pageParam.startDate}';
				pageParam.endDate = '${pageParam.endDate}';
				pageParam.s_paid = '${pageParam.s_paid}';
				pageParam.s_status = '${pageParam.s_status}';
				goUrl();
			}
			
			// 페이지 이동
			function goUrl(){
				location.href="/realestate/realestateList.do?pageNo="+pageParam.pageNo+"&pageSize="+pageParam.pageSize+"&s_divide="+encodeURIComponent(pageParam.s_divide, "UTF-8")+"&s_gubun="+encodeURIComponent(pageParam.s_gubun, "UTF-8")+"&s_user="+encodeURIComponent(pageParam.s_user, "UTF-8")+"&startDate="+encodeURIComponent(pageParam.startDate, "UTF-8")+"&endDate="+encodeURIComponent(pageParam.endDate, "UTF-8")+"&s_paid="+encodeURIComponent(pageParam.s_paid, "UTF-8")+"&s_status="+encodeURIComponent(pageParam.s_status, "UTF-8");
			}
			
			// 상세 페이지로 이동
			function goDetailPage(no){
				var url = "/realestate/realestateView.do?contractSeq=" + no;
				var queStr = $("#queStr").val();
				if(queStr != ""){
					url += "&queStr="+queStr;
				}
				location.href = url;
			}
			
			// 등록 페이지로 이동
			function goAddPage(){
				var url = "/realestate/realestateAdd.do";
				location.href = url;
			}
			
			// 통계 페이지로 이동
			function goStatisticsPage(){
				var url = "/realestate/realestateStatistics.do";
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
								<i class="fa fa-list"></i> 부동산 계약 목록
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-building-o"></i>부동산 계약 관리</li>
								<li><i class="fa fa-list"></i>부동산 계약 목록</li>
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
											<label class="col-lg-2 control-label">분류</label>
											<div class="col-lg-8">
												<select id="s_divide" class="form-control">
													<option value="">== 분류 선택 ==</option>
													<option value="임대">임대</option>
													<option value="보유">보유</option>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">지점구분</label>
											<div class="col-lg-8">
												<select id="s_gubun" class="form-control" >
													<option value="">== 지점 구분 선택 ==</option>
													<option value="본사">본사</option>
													<option value="진천공장">진천공장</option>
													<option value="충주공장">충주공장</option>
													<option value="연구소">연구소</option>
													<option value="영업지점">영업지점</option>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">사용자</label>
											<div class="col-lg-8">
												<input type="text" id="s_user" class="form-control" >
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">계약기간</label>
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
											<label class="col-lg-2 control-label">지급구분</label>
											<div class="col-lg-8">
												<select id="s_paid" class="form-control" >
													<option value="">== 지급구분 선택 ==</option>
													<option value="월세">월세</option>
													<option value="연납">연납</option>
													<option value="전세">전세</option>
													<option value="보유">보유</option>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">상태</label>
											<div class="col-lg-8">
												<select id="s_status" class="form-control">
													<option value="ING">진행중</option>
													<option value="END">종료</option>
													<option value="ALL">전체</option>
												</select>
											</div>
											<div class="col-lg-2">
												<input type="text" style="display: none;" />
												<input type="hidden" name="queStr" id="queStr" value="${pageParam.queStr}" />
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
										<col style="width: 6%;">
										<col style="width: 6%;">
										<col style="width: 8%;">
										<col style="width: 12%;">
										<col style="width: 16%;">
										<col style="width: 6%;">
										<col style="width: 6%;">
										<col style="width: 10%;">
										<col style="width: 10%;">
										<col style="width: 10%;">
										<col style="width: 10%;">
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">번호</th>
											<th class="text-center">분류</th>
											<th class="text-center">지점구분</th>
											<th class="text-center">사용자</th>
											<th class="text-center">계약기간/매입일</th>
											<th class="text-center">상태</th>
											<th class="text-center">지급구분</th>
											<th class="text-center">보증금/매입금</th>
											<th class="text-center">임대료</th>
											<th class="text-center">관리비</th>
											<th class="text-center">합계</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${fn:length(list) == 0}">
												<tr style="letter-spacing: -1px">
													<td class="text-center" colspan="11">결과가 존재하지 않습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="result" items="${list}" varStatus="status">
													<tr>
														<td class="text-center">${(pageParam.totalCount - (pageParam.pageSize * (pageParam.pageNo - 1))) - status.index }</td>
														<td class="text-center">
															<c:choose>
																<c:when test="${result.payment == '보유' }">보유</c:when>
																<c:otherwise>임대</c:otherwise>
															</c:choose>
														</td>
														<td class="text-center">${result.saupGubun }</td>
														<td class="text-center"><a href="javascript:goDetailPage('${result.contractSeq }');">${result.username }</a></td>
														<td class="text-center">
															<c:choose>
																<c:when test="${result.payment == '보유' }">${result.startDate }</c:when>
																<c:otherwise>${result.startDate } ~ ${result.endDate }</c:otherwise>
															</c:choose>
														</td>
														<td class="text-center">
															<c:choose>
																<c:when test="${result.status == 'ING' }">
																	<span class="label label-success">진행중</span>
																</c:when>
																<c:when test="${result.status == 'END' }">
																	<span class="label label-danger">종료</span>
																</c:when>
															</c:choose>
														</td>
														<td class="text-center">${result.payment }</td>
														<td class="text-center"><fmt:formatNumber value="${result.deposit }" pattern="#,###" /></td>
														<td class="text-center"><fmt:formatNumber value="${result.rent }" pattern="#,###" /></td>
														<td class="text-center"><fmt:formatNumber value="${result.administrativeExpenses }" pattern="#,###" /></td>
														<td class="text-center"><fmt:formatNumber value="${result.rent + result.administrativeExpenses }" pattern="#,###" /></td>
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
						<a class="btn btn-default" href="javascript:;" title="통계확인" style="margin-top: 7px;" onClick="javascript:goStatisticsPage();"><span class="icon_datareport"></span>&nbsp;통계</a>
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