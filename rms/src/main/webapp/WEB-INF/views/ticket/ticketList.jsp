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
				, s_visitCompany : ""
				, s_visitName : ""
				, s_meetingName : ""
				, startDate : ""
				, endDate : ""
				, s_refdeptCode : ""
				, s_deptCode : ""
				, s_regName : ""
				, s_status : ""
			}
		
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu05");
				
				// 부서 정보 가져오기
				getTotalDeptListAjax();
				
				// 검색어 입력영역에 엔터키 이벤트 바인드
				$("#s_visitCompany").keydown(function (key) {
				    if (key.keyCode == 13) goSearch();
				});
				$("#s_visitName").keydown(function (key) {
				    if (key.keyCode == 13) goSearch();
				});
				$("#s_meetingName").keydown(function (key) {
				    if (key.keyCode == 13) goSearch();
				});
				$("#s_regName").keydown(function (key) {
				    if (key.keyCode == 13) goSearch();
				});
				
				// 검색목록 선택, 목록 상자에 검색조건의 값은 ""으로 셋팅 필요
				$("#s_visitCompany").val("${pageParam.s_visitCompany}");
				$("#s_visitName").val("${pageParam.s_visitName}");
				$("#s_meetingName").val("${pageParam.s_meetingName}");
				$("#startDate").val("${pageParam.startDate}");
				$("#endDate").val("${pageParam.endDate}");
				$("#s_refdeptCode").val("${pageParam.s_refdeptCode}").prop("selected", true);
				$("#s_deptCode").val("${pageParam.s_deptCode}").prop("selected", true);
				$("#s_regName").val("${pageParam.s_regName}");
				$("#s_status").val("${pageParam.s_status}").prop("selected", true);
				
				//페이징 처리
				$('#pagingDiv').html(cfGetPagingHtml('${pageParam.totalCount}', '${pageParam.pageNo}', '${pageParam.pageSize}', 'goPage'));
				$("#pageSize").val("${pageParam.pageSize}").prop("selected", true);
				
				// datetimepicker
				$('#startDatetime').datetimepicker({
					format: 'YYYY-MM-DD HH:mm',
					sideBySide: true,
				});
				$('#endDatetime').datetimepicker({
					format: 'YYYY-MM-DD HH:mm',
					sideBySide: true,
					useCurrent: false,
				});
				$("#startDatetime").on("dp.change", function (e) {
		            $('#endDatetime').data("DateTimePicker").minDate(e.date);
		        });
		        $("#endDatetime").on("dp.change", function (e) {
		            $('#startDatetime').data("DateTimePicker").maxDate(e.date);
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
			
			// 부서 리스트 ajax
			function getTotalDeptListAjax() {
				var params = {
					auth : "reyon"
				}
				
				var request = $.ajax({
					url: "/common/getTotalDeptListAjax.json"
					, type : "POST"
					, timeout: 10000
					, data : params
					, dataType : "json"
					, async : false
					, error: function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					}
					, success : function(json) {
						if (json.resultCode == 0){
							var totalList = json.resultMsg;
							var tag = '<option value="">== 부서 선택 ==</option>';
							for(var i=0; i<totalList.length; i++){
								tag += '<option value="'+totalList[i].deptCode+'">'+totalList[i].deptParcoName+' - '+totalList[i].deptName+'</option>';
							}
							$("#s_refdeptCode").append(tag);
							$("#s_deptCode").append(tag);
						}else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
						}else{
							alert(json.resultMsg);
						}
					}
				});
			}
			
			// 페이징 파라메터 설정
			function goPage(pageNo){
				pageParam.pageNo = pageNo;
				pageParam.pageSize = '${pageParam.pageSize}';
				pageParam.s_visitCompany = '${pageParam.s_visitCompany}';
				pageParam.s_visitName = '${pageParam.s_visitName}';
				pageParam.s_meetingName = '${pageParam.s_meetingName}';
				pageParam.startDate = '${pageParam.startDate}';
				pageParam.endDate = '${pageParam.endDate}';
				pageParam.s_refdeptCode = '${pageParam.s_refdeptCode}';
				pageParam.s_deptCode = '${pageParam.s_deptCode}';
				pageParam.s_regName = '${pageParam.s_regName}';
				pageParam.s_status = '${pageParam.s_status}';
				goUrl();
			}
			
			// 검색 파라메터 설정
			function goSearch(){
				pageParam.pageNo = 1;
				pageParam.pageSize = '${pageParam.pageSize}';
				pageParam.s_visitCompany = $("#s_visitCompany").val();
				pageParam.s_visitName = $("#s_visitName").val();
				pageParam.s_meetingName = $("#s_meetingName").val();
				pageParam.startDate = $("#startDate").val();
				pageParam.endDate = $("#endDate").val();
				pageParam.s_refdeptCode = $("#s_refdeptCode option:selected").val();
				pageParam.s_deptCode = $("#s_deptCode option:selected").val();
				pageParam.s_regName = $("#s_regName").val();
				pageParam.s_status = $("#s_status option:selected").val();
				goUrl();
			}
			
			// 페이지당 노출수 설정
			function goSize() {
				pageParam.pageNo = '${pageParam.pageNo}';
				pageParam.pageSize = $("#pageSize option:selected").val();
				pageParam.s_visitCompany = '${pageParam.s_visitCompany}';
				pageParam.s_visitName = '${pageParam.s_visitName}';
				pageParam.s_meetingName = '${pageParam.s_meetingName}';
				pageParam.startDate = '${pageParam.startDate}';
				pageParam.endDate = '${pageParam.endDate}';
				pageParam.s_refdeptCode = '${pageParam.s_refdeptCode}';
				pageParam.s_deptCode = '${pageParam.s_deptCode}';
				pageParam.s_regName = '${pageParam.s_regName}';
				pageParam.s_status = '${pageParam.s_status}';
				goUrl();
			}
			
			// 페이지 이동
			function goUrl(){
				location.href="/ticket/ticketList.do?pageNo="+pageParam.pageNo+"&pageSize="+pageParam.pageSize+"&s_visitCompany="+encodeURIComponent(pageParam.s_visitCompany, "UTF-8")+"&s_visitName="+encodeURIComponent(pageParam.s_visitName, "UTF-8")+"&s_meetingName="+encodeURIComponent(pageParam.s_meetingName, "UTF-8")+"&startDate="+encodeURIComponent(pageParam.startDate, "UTF-8")+"&endDate="+encodeURIComponent(pageParam.endDate, "UTF-8")+"&s_refdeptCode="+encodeURIComponent(pageParam.s_refdeptCode, "UTF-8")+"&s_deptCode="+encodeURIComponent(pageParam.s_deptCode, "UTF-8")+"&s_regName="+encodeURIComponent(pageParam.s_regName, "UTF-8")+"&s_status="+encodeURIComponent(pageParam.s_status, "UTF-8");
			}
			
			// 상세 페이지로 이동
			function goDetailPage(no){
				var url = "/ticket/ticketView.do?parkingSeq=" + no;
				var queStr = $("#queStr").val();
				if(queStr != ""){
					url += "&queStr="+queStr;
				}
				location.href = url;
			}
			
			// 등록 페이지로 이동
			function goAddPage(){
				var url = "/ticket/ticketAdd.do";
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
								<i class="fa fa-list"></i> 주차권 지급 목록
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-car"></i>주차권 관리</li>
								<li><i class="fa fa-list"></i>주차권 지급 목록</li>
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
											<label class="col-lg-2 control-label">방문 업체명(본부명)</label>
											<div class="col-lg-8">
												<input type="text" id="s_visitCompany" class="form-control" maxlength="50">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">방문자 성함(사용자 성함)</label>
											<div class="col-lg-8">
												<input type="text" id="s_visitName" class="form-control" maxlength="50">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">방문목적</label>
											<div class="col-lg-8">
												<input type="text" id="s_meetingName" class="form-control" maxlength="50">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">지급일시</label>
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
											<label class="col-lg-2 control-label">담당부서</label>
											<div class="col-lg-8">
												<select id="s_refdeptCode" class="form-control"></select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">등록부서</label>
											<div class="col-lg-8">
												<select id="s_deptCode" class="form-control"></select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">등록인</label>
											<div class="col-lg-8">
												<input type="text" id="s_regName" class="form-control" maxlength="50">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">상태</label>
											<div class="col-lg-8">
												<select id="s_status" class="form-control">
													<option value="">== 상태 선택 ==</option>
													<option value="01">지급신청</option>
													<!-- <option value="99">지급거절</option> -->
													<option value="AA">지급완료</option>
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
					
					<jsp:useBean id="now" class="java.util.Date" />
					<fmt:formatDate var="toDay" value="${now}" pattern="yyyy-MM-dd" />
					
					<!-- main table start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel">
								<table class="table table-bordered">
									<colgroup>
										<col style="width: 5%;">
										<col style="width: 9%;">
										<col style="width: 9%;">
										<col style="width: 17%;">
										<col style="width: 4%;">
										<col style="width: 4%;">
										<col style="width: 4%;">
										<col style="width: 4%;">
										<col style="width: 4%;">
										<col style="width: 13%;">
										<col style="width: 9%;">
										<col style="width: 9%;">
										<col style="width: 9%;">
									</colgroup>
									<thead>
										<tr>
											<th class="text-center" style="vertical-align: middle;">번호</th>
											<th class="text-center" style="vertical-align: middle;">방문 업체명<br>(본부명)</th>
											<th class="text-center" style="vertical-align: middle;">방문자 성함<br>(사용자 성함)</th>
											<th class="text-center" style="vertical-align: middle;">방문목적</th>
											<th class="text-center" style="vertical-align: middle;">2시간</th>
											<th class="text-center" style="vertical-align: middle;">3시간</th>
											<th class="text-center" style="vertical-align: middle;">4시간</th>
											<th class="text-center" style="vertical-align: middle;">6시간</th>
											<th class="text-center" style="vertical-align: middle;">종일권</th>
											<th class="text-center" style="vertical-align: middle;">지급일시</th>
											<th class="text-center" style="vertical-align: middle;">담당부서</th>
											<th class="text-center" style="vertical-align: middle;">등록인</th>
											<th class="text-center" style="vertical-align: middle;">상태</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${fn:length(list) == 0}">
												<tr style="letter-spacing: -1px">
													<td class="text-center" colspan="13">결과가 존재하지 않습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="result" items="${list}" varStatus="status">
													<tr>
														<td class="text-center">${(pageParam.totalCount - (pageParam.pageSize * (pageParam.pageNo - 1))) - status.index }</td>
														<td class="text-center">${result.visitCompany }</td>
														<td class="text-center">${result.visitName }</td>
														<td class="text-center"><a href="javascript:goDetailPage('${result.parkingSeq }');">${result.visitPurpose }</a></td>
												<c:choose>
													<c:when test="${result.isWebSale == 'N' }">
														<td class="text-center">${result.countHour2 }</td>
														<td class="text-center">${result.countHour3 }</td>
														<td class="text-center">${result.countHour4 }</td>
														<td class="text-center">${result.countHour6 }</td>
														<td class="text-center">${result.countHour24 }</td>
													</c:when>
													<c:when test="${result.isWebSale == 'Y' }">
														<td class="text-center" colspan="5">KT&G 웹할인</td>
													</c:when>
												</c:choose>
														<td class="text-center">
															<c:choose>
																<c:when test="${result.giveDate == '' || result.giveDate == null }">
																	<span class="label label-success">지급대기중</span>
																</c:when>
																<c:when test="${fn:substring(result.giveDate,0,10) == toDay }">
																	<span class="label label-primary">오늘</span>
																	<span style="color:#007aff;">${result.giveDate }</span>
																</c:when>
																<c:otherwise>
																	<span class="label label-danger">완료</span>
																	<span style="color:#ff2d55;">${result.giveDate }</span>
																</c:otherwise>
															</c:choose>  
														</td>
														<td class="text-center">${result.refDeptName }</td>
														<td class="text-center">${result.deptName } (${result.regName })</td>
														<td class="text-center">
															<c:choose>
																<c:when test="${result.status == '01' }"><span style="color:#007aff;">지급신청</span></c:when>
																<c:when test="${result.status == '99' }"><span style="color:#ff2d55;">지급거절</span></c:when>
																<c:when test="${result.status == 'AA' }"><span style="color:#4cd964;">지급완료</span></c:when>
															</c:choose>
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