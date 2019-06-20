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
				, s_meetingName: ""
				, s_deptCode : ""
				, s_regName : ""
				, startDate : ""
				, endDate : ""
				, s_meetingPlace : ""
				, s_status : ""
			}
			
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu03");
				
				// 부서 정보 가져오기
				getTotalDeptListAjax();
				
				// 장소 정보 가져오기
				getCommonCodeListAjax();
				
				// 검색어 입력영역에 엔터키 이벤트 바인드
				$("#s_meetingName").keydown(function (key) {
				    if (key.keyCode == 13) goSearch();
				});
				$("#s_regName").keydown(function (key) {
				    if (key.keyCode == 13) goSearch();
				});
				
				// 검색목록 선택, 목록 상자에 검색조건의 값은 ""으로 셋팅 필요
				$("#s_meetingName").val("${pageParam.s_meetingName}");
				$("#s_deptCode").val("${pageParam.s_deptCode}").prop("selected", true);
				$("#s_regName").val("${pageParam.s_regName}");
				$("#startDate").val("${pageParam.startDate}");
				$("#endDate").val("${pageParam.endDate}");
				$("#s_meetingPlace").val("${pageParam.s_meetingPlace}").prop("selected", true);
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
			
			// 장소 리스트 ajax
			function getCommonCodeListAjax() {
				var params = {
					auth : "reyon",
					grpCode : "AG",
				}
				
				var request = $.ajax({
					url: "/common/getCommonCodeListAjax.json"
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
							var tag = '<option value="">== 장소 선택 ==</option>';
							for(var i=0; i<totalList.length; i++){
								tag += '<option value="'+totalList[i].code+'">'+totalList[i].codeName+'</option>';
							}
							$("#s_meetingPlace").append(tag);
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
				pageParam.s_meetingName = '${pageParam.s_meetingName}';
				pageParam.s_deptCode = '${pageParam.s_deptCode}';
				pageParam.s_regName = '${pageParam.s_regName}';
				pageParam.startDate = '${pageParam.startDate}';
				pageParam.endDate = '${pageParam.endDate}';
				pageParam.s_meetingPlace = '${pageParam.s_meetingPlace}';
				pageParam.s_status = '${pageParam.s_status}';
				goUrl();
			}
			
			// 검색 파라메터 설정
			function goSearch(){
				pageParam.pageNo = 1;
				pageParam.pageSize = '${pageParam.pageSize}';
				pageParam.s_meetingName = $("#s_meetingName").val();
				pageParam.s_deptCode = $("#s_deptCode option:selected").val();
				pageParam.s_regName = $("#s_regName").val();
				pageParam.startDate = $("#startDate").val();
				pageParam.endDate = $("#endDate").val();
				pageParam.s_meetingPlace = $("#s_meetingPlace option:selected").val();
				pageParam.s_status = $("#s_status").val();
				goUrl();
			}
			
			// 페이지당 노출수 설정
			function goSize() {
				pageParam.pageNo = '${pageParam.pageNo}';
				pageParam.pageSize = $("#pageSize option:selected").val();
				pageParam.s_meetingName = '${pageParam.s_meetingName}';
				pageParam.s_deptCode = '${pageParam.s_deptCode}';
				pageParam.s_regName = '${pageParam.s_regName}';
				pageParam.startDate = '${pageParam.startDate}';
				pageParam.endDate = '${pageParam.endDate}';
				pageParam.s_meetingPlace = '${pageParam.s_meetingPlace}';
				pageParam.s_status = '${pageParam.s_status}';
				goUrl();
			}
			
			// 페이지 이동
			function goUrl(){
				location.href="/meeting/meetingList.do?pageNo="+pageParam.pageNo+"&pageSize="+pageParam.pageSize+"&s_meetingName="+encodeURIComponent(pageParam.s_meetingName, "UTF-8")+"&s_deptCode="+encodeURIComponent(pageParam.s_deptCode, "UTF-8")+"&s_regName="+encodeURIComponent(pageParam.s_regName, "UTF-8")+"&startDate="+pageParam.startDate+"&endDate="+pageParam.endDate+"&s_meetingPlace="+encodeURIComponent(pageParam.s_meetingPlace, "UTF-8")+"&s_status="+encodeURIComponent(pageParam.s_status, "UTF-8");
			}
			
			// 상세 페이지로 이동
			function goDetailPage(no){
				var url = "/meeting/meetingView.do?meetingSeq=" + no;
				var queStr = $("#queStr").val();
				if(queStr != ""){
					url += "&queStr="+queStr;
				}
				location.href = url;
			}
			
			// 등록 페이지로 이동
			function goAddPage(){
				var url = "/meeting/meetingAdd.do";
				location.href = url;
			}
			
			// 방문자 관리로 보내기 모달 열기
			function openChangeMeetingTypeModal() {
				$("#changeMeetingTypeModal").modal();
			}
			
			// 방문자 관리로 보내기
			function changeMeetingType () {
				var seq = $("#changeMeetingTypeSeq").val();
				
				if (seq == "") {
					alert("항목을 선택해 주세요.");
					return;
				}
				
				var params = {
					auth : "reyon",
					meetingType : "02",
					seq : seq,
				}
				
				var request = $.ajax({
					url: "/common/changeMeetingTypeAjax.json"
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
							alert("선택한 항목을 방문자 관리로 보냈습니다.");
							location.reload();
						}else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
						}else{
							alert(json.resultMsg);
						}
					}
				});
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
								<i class="fa fa-list"></i> 회의 목록&nbsp;&nbsp;&nbsp;&nbsp;
								<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SUPERUSER')">
								<div class="btn-group">
									<button id="saupBtn00" class="btn btn-default" type="button" onClick="javascript:selectSaup('00');location.reload();">전체</button>
									<button id="saupBtn10" class="btn btn-default" type="button" onClick="javascript:selectSaup('10');location.reload();">본사</button>
									<button id="saupBtn20" class="btn btn-default" type="button" onClick="javascript:selectSaup('20');location.reload();">진천공장</button>
									<button id="saupBtn30" class="btn btn-default" type="button" onClick="javascript:selectSaup('30');location.reload();">충주공장</button>
									<button id="saupBtn40" class="btn btn-default" type="button" onClick="javascript:selectSaup('40');location.reload();">안양연구소</button>
									<button id="saupBtn50" class="btn btn-default" type="button" onClick="javascript:selectSaup('50');location.reload();">N-bio</button>
								</div>
								</sec:authorize>
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-calendar"></i>회의 관리</li>
								<li><i class="fa fa-list"></i>회의 목록</li>
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
											<label class="col-lg-2 control-label">제목</label>
											<div class="col-lg-8">
												<input type="text" id="s_meetingName" class="form-control" maxlength="50">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">부서</label>
											<div class="col-lg-8">
												<select id="s_deptCode" class="form-control"></select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">담당자</label>
											<div class="col-lg-8">
												<input type="text" id="s_regName" class="form-control" maxlength="50">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">회의 일시</label>
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
											<label class="col-lg-2 control-label">장소</label>
											<div class="col-lg-8">
												<select id="s_meetingPlace" class="form-control"></select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">상태</label>
											<div class="col-lg-8">
												<select id="s_status" class="form-control">
													<option value="">== 상태 선택 ==</option>
													<option value="YY">미진행 전체보기</option>
													<option value="ZZ">등록</option>
													<option value="02">결과미등록</option>
													<option value="03">결재미상신</option>
													<option value="04">결재대기</option>
													<option value="99">결재반려</option>
													<option value="BB">담당직권완료</option>
													<option value="AA">결재완료</option>
												</select>
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
					
					<jsp:useBean id="now" class="java.util.Date" />
					<fmt:formatDate var="toDay" value="${now}" pattern="yyyy-MM-dd" />
					
					<!-- main table start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel">
								<table class="table table-bordered">
									<colgroup>
										<col style="width: 7%;">
										<col style="width: 25%;">
										<col style="width: 10%;">
										<col style="width: 10%;">
										<col style="width: 25%;">
										<col style="width: 13%;">
										<col style="width: 10%;">
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">번호</th>
											<th class="text-center">제목</th>
											<th class="text-center">부서</th>
											<th class="text-center">담당자</th>
											<th class="text-center">회의 일시</th>
											<th class="text-center">장소</th>
											<th class="text-center">상태</th>
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
														<td class="text-center"><a href="javascript:goDetailPage('${result.meetingSeq }');">${result.meetingName }</a></td>
														<td class="text-center">${result.deptName }</td>
														<td class="text-center">${result.regName }</td>
														<td class="text-center">
															<c:choose>
																<c:when test="${fn:substring(result.meetingStartDate,0,10) > toDay }">
																	<span class="label label-success">예정</span>
																	<span style="color:#4cd964;">${result.meetingStartDate } ~ ${result.meetingEndDate }</span>
																</c:when>
																<c:when test="${fn:substring(result.meetingStartDate,0,10) == toDay }">
																	<span class="label label-primary">오늘</span>
																	<span style="color:#007aff;">${result.meetingStartDate } ~ ${result.meetingEndDate }</span>
																</c:when>
																<c:otherwise>
																	<span class="label label-danger">종료</span>
																	<span style="color:#ff2d55;">${result.meetingStartDate } ~ ${result.meetingEndDate }</span>
																</c:otherwise>
															</c:choose>
														</td>
														<td class="text-center">${result.codeName }</td>
														<td class="text-center">
															<c:choose>
																<c:when test="${result.meetingStatus == '01' || result.meetingStatus == '05' }">등록</c:when>
																<c:when test="${result.meetingStatus == '02' }">결과미등록</c:when>
																<c:when test="${result.meetingStatus == '03' }">결재미상신</c:when>
																<c:when test="${result.meetingStatus == '04' }">결재대기</c:when>
																<c:when test="${result.meetingStatus == '99' }"><span style="color:#ff2d55;">결재반려</span></c:when>
																<c:when test="${result.meetingStatus == 'AA' }">결재완료</c:when>
																<c:when test="${result.meetingStatus == 'BB' }">담당직권완료</c:when>
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
					<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SUPERUSER','ROLE_ITT','ROLE_CHONGMU')">
						<a class="btn btn-default" href="javascript:;" title="회의에서 방문자로 이동" style="margin-top: 7px;" onClick="javascript:openChangeMeetingTypeModal();"><span class="icon_refresh"></span>&nbsp;방문자 관리로 보내기</a>
					</sec:authorize>
						<a class="btn btn-default" href="javascript:;" title="등록하기" style="margin-top: 7px;" onClick="javascript:goAddPage();"><span class="icon_pencil"></span>&nbsp;등록</a>
					</div>
					<!-- bottom button end -->
					
				</section>
				<!--overview end-->
				
			</section>
			<!--main content end-->
			
			<!-- modal popup start -->
			<div aria-hidden="true" aria-labelledby="changeMeetingTypeModalLabel" role="dialog" tabindex="-1" id="changeMeetingTypeModal" class="modal fade">
				<div class="modal-dialog" style="width: 70%;">
					<div class="modal-content">
						<div class="modal-header">
							<button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
							<h4 class="modal-title">방문자 관리로 보내기</h4>
						</div>
						<div class="modal-body">
							<form class="form-horizontal" role="form">
								<div class="form-group">
									<label for="searchText" class="col-lg-2 control-label">항목 선택</label>
									<div class="col-lg-8">
						                <select id="changeMeetingTypeSeq" class="form-control">
						                	<option value=""> == 방문자 관리로 보낼 항목을 선택하세요 == </option>
					                	<c:forEach var="result" items="${list}" varStatus="status">
					                		<option value="${result.meetingSeq }">${result.meetingName } [${result.deptName } - ${result.regName }]</option>
										</c:forEach>
						                </select>
									</div>
									<div class="col-lg-2">
										<a class="btn btn-default" href="javascript:;" title="회의에서 방문자로 이동" onClick="javascript:changeMeetingType();"><span class="icon_refresh"></span>&nbsp;보내기</a>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
			<!-- modal popup end -->
			
		</section>
		<!-- container section start -->
		
		<!--loading Popup start-->
		<%@ include file="/WEB-INF/views/include/loading.jsp"%>
		<!--loading Popup end--> 
		
	</body>
</html>