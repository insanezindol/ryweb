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
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu02");
			});
			
			// 수정
			function modifyProject(){
				var seq = $("#seq").val();
				var url = "/project/projectModify.do?projectSeq=" + seq;
				var queStr = $("#queStr").val();
				if(queStr != ""){
					url += "&queStr="+encodeURIComponent(queStr);
				}
				location.href = url;
			}

			// 삭제
			function deleteProject(){
				var seq = $("#seq").val();
				
				var rCnt = "${fn:length(meetingList) }";
				if(rCnt != "0"){
					alert("관련 회의 정보가 존재하여 삭제할 수 없습니다.");
					return;
				}
				
				if(confirm("삭제 하시겠습니까?")){
					var params = {
						auth : "reyon",
						seq : seq
					}
					
					var request = $.ajax({
						url: '/project/projectDeleteAjax.json'
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
								var url = "/project/projectList.do";
								var queStr = $("#queStr").val();
								if(queStr != ""){
									url += "?" + queStr;
								}
								location.href = url;
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
			
			// 뒤로가기
			function goBack(){
				var url = "/project/projectList.do";
				var queStr = $("#queStr").val();
				if(queStr != ""){
					url += "?" + queStr;
				}
				location.href = url;
			}
			
			// 회의 상세 페이지로 이동
			function goDetailPage(no){
				var url = "/meeting/meetingView.do?meetingSeq=" + no;
				var queStr = $("#queStr").val();
				if(queStr != ""){
					url += "&queStr="+queStr;
				}
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
								<i class="fa fa-file-text-o"></i> 프로젝트 상세
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-briefcase"></i>프로젝트 관리</li>
								<li><i class="fa fa-file-text-o"></i>프로젝트 상세</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">프로젝트 상세 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">프로젝트 이름</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.projectName }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">프로젝트 기간</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.projectStartDate } ~ ${info.projectEndDate }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">상태</label>
											<div class="col-lg-10">
												<p class="form-control-static">
													<c:choose>
														<c:when test="${info.status == '01' }">진행전</c:when>
														<c:when test="${info.status == '02' }">진행중</c:when>
														<c:when test="${info.status == '03' }">중지</c:when>
														<c:when test="${info.status == '04' }">완료</c:when>
													</c:choose>
												</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">담당자</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.deptName } ${info.regName }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">참여자</label>
											<div class="col-lg-10">
												<p class="form-control-static">
													<c:choose>
														<c:when test="${fn:length(attList1) == 0}">등록된 참여자가 없습니다.</c:when>
														<c:otherwise>
															<c:forEach var="result" items="${attList1}" varStatus="status">
																${result.attendantDeptName} ${result.attendantName }<c:if test="${!status.last}">,</c:if>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">참고인</label>
											<div class="col-lg-10">
												<p class="form-control-static">
													<c:choose>
														<c:when test="${fn:length(attList2) == 0}">등록된 참고인이 없습니다.</c:when>
														<c:otherwise>
															<c:forEach var="result" items="${attList2}" varStatus="status">
																${result.attendantDeptName} ${result.attendantName }<c:if test="${!status.last}">,</c:if>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">등록인 / 등록시간</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.regName } / ${info.regDate }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">수정인 / 수정시간</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.updName } / ${info.updDate }</p>
											</div>
										</div>
									</form>
								</div>
							</section>
						</div>
					</div>
					<!-- body top end -->
					
					<!-- bottom button start -->
					<div class="btn-row text-right">
						<input type="hidden" id="seq" value="${info.projectSeq }">
						<input type="hidden" id="queStr" value="${queStr}" />
						<c:if test="${isMine }">
						<a class="btn btn-default" href="javascript:;" title="수정하기" style="margin-top: 7px;" onClick="javascript:modifyProject();"><span class="icon_pencil-edit"></span>&nbsp;수정</a>
						<a class="btn btn-default" href="javascript:;" title="삭제하기" style="margin-top: 7px;" onClick="javascript:deleteProject();"><span class="icon_trash"></span>&nbsp;삭제</a>
						</c:if>
						<a class="btn btn-default" href="javascript:;" title="이전으로" style="margin-top: 7px;" onClick="javascript:goBack();"><span class="arrow_back"></span>&nbsp;이전</a>
					</div>
					<!-- bottom button end -->
					
					<!-- body bottom start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">관련 회의 정보</header>
								<div class="panel-body">
									<table class="table table-bordered">
										<colgroup>
											<col style="width: 7%;">
											<col style="width: 30%;">
											<col style="width: 13%;">
											<col style="width: 10%;">
											<col style="width: 27%;">
											<col style="width: 13%;">
										</colgroup>
										<thead>
											<tr>
												<th class="text-center">번호</th>
												<th class="text-center">제목</th>
												<th class="text-center">부서</th>
												<th class="text-center">담당자</th>
												<th class="text-center">회의 일시</th>
												<th class="text-center">장소</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${fn:length(meetingList) == 0}">
													<tr style="letter-spacing: -1px">
														<td class="text-center" colspan="6">관련 회의가 존재하지 않습니다.</td>
													</tr>
												</c:when>
												<c:otherwise>
													<c:forEach var="result" items="${meetingList}" varStatus="status">
														<tr>
															<td class="text-center">${fn:length(meetingList) - status.index }</td>
															<td class="text-center"><a href="javascript:goDetailPage('${result.meetingSeq }');">${result.meetingName }</a></td>
															<td class="text-center">${result.deptName }</td>
															<td class="text-center">${result.regName }</td>
															<td class="text-center">${result.meetingStartDate } ~ ${result.meetingEndDate }</td>
															<td class="text-center">${result.codeName }</td>
														</tr>
													</c:forEach>
												</c:otherwise>
											</c:choose>
										</tbody>
									</table>
								</div>
							</section>
						</div>
					</div>
					<!-- body bottom end -->
					
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