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
				cfLNBMenuSelect("menu05");
			});
			
			// 수정
			function modifyTicket(){
				var seq = $("#seq").val();
				var url = "/ticket/ticketModify.do?parkingSeq=" + seq;
				var queStr = $("#queStr").val();
				if(queStr != ""){
					url += "&queStr="+encodeURIComponent(queStr);
				}
				location.href = url;
			}

			// 삭제
			function deleteTicket(){
				var seq = $("#seq").val();
				
				if(confirm("삭제 하시겠습니까?")){
					var params = {
						auth : "reyon",
						seq : seq
					}
					
					var request = $.ajax({
						url: '/ticket/ticketDeleteAjax.json'
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
								var url = "/ticket/ticketList.do";
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
			
			// 지급 완료/거절 처리
			function confirmTicket(status){
				var seq = $("#seq").val();
				
				var statusText = "";
				if(status == "AA"){
					statusText = "지급 완료 처리";
				} else if(status == "99"){
					statusText = "지급 거절 처리";
				}
				
				if(confirm(statusText + " 하시겠습니까?")){
					var params = {
						auth : "reyon",
						seq : seq,
						status : status,
					}
					
					var request = $.ajax({
						url: '/ticket/ticketConfirmAjax.json'
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
								alert(statusText + "가 완료 되었습니다.");
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
			
			// 뒤로가기
			function goBack(){
				var url = "/ticket/ticketList.do";
				var queStr = $("#queStr").val();
				if(queStr != ""){
					url += "?" + queStr;
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
								<i class="fa fa-file-text-o"></i> 주차권 지급 상세
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-car"></i>주차권 관리</li>
								<li><i class="fa fa-file-text-o"></i>주차권 지급 상세</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">주차권 지급 상세 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">방문 업체명(본부명)</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.visitCompany }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">방문자 성함(사용자 성함)</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.visitName }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">방문목적</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.visitPurpose }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">담당부서</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.refDeptName }</p>
											</div>
										</div>
										
										<div class="form-group">
											<label class="col-lg-2 control-label">지급구분</label>
											<div class="col-lg-10">
												<p class="form-control-static">
													<c:choose>
														<c:when test="${info.isWebSale == 'N' }">주차권 카드</c:when>
														<c:when test="${info.isWebSale == 'Y' }">KT&G 웹할인</c:when>
													</c:choose>
												</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">지급목록</label>
											<div class="col-lg-10">
												<div class="row">
													<div class="col-lg-4 col-md-6 col-sm-12 col-xs-12">
												<c:choose>
													<c:when test="${info.isWebSale == 'N' }">
														<table class="table table-bordered">
															<tbody>
																<tr>
																	<th class="text-center">항목</th>
																	<th class="text-center">수량</th>
																</tr>
																<tr>
																	<td class="text-center">2시간 이용권</td>
																	<td class="text-center">${info.countHour2 }</td>
																</tr>
																<tr>
																	<td class="text-center">3시간 이용권</td>
																	<td class="text-center">${info.countHour3 }</td>
																</tr>
																<tr>
																	<td class="text-center">4시간 이용권</td>
																	<td class="text-center">${info.countHour4 }</td>
																</tr>
																<tr>
																	<td class="text-center">6시간 이용권</td>
																	<td class="text-center">${info.countHour6 }</td>
																</tr>
																<tr>
																	<td class="text-center">종일권</td>
																	<td class="text-center">${info.countHour24 }</td>
																</tr>
																<tr>
																	<th class="text-center">합계</th>
																	<td class="text-center">${info.countHour2 + info.countHour3 + info.countHour4 + info.countHour6 + info.countHour8 + info.countHour10 + info.countHour24 }</td>
																</tr>
															</tbody>
														</table>
													</c:when>
													<c:when test="${info.isWebSale == 'Y' }">
														<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SUPERUSER','ROLE_ITT','ROLE_CHONGMU')">
														<table class="table table-bordered">
															<tbody>
																<tr>
																	<th class="text-center">항목</th>
																	<th class="text-center">금액</th>
																</tr>
																<tr>
																	<td class="text-center">KT&G 웹할인</td>
																	<td class="text-center"><fmt:formatNumber value="${info.webSalePrice }" pattern="#,###" /> 원</td>
																</tr>
															</tbody>
														</table>
														</sec:authorize>
													</c:when>
												</c:choose>
													</div>
												</div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">등록인 / 등록일 / 수정일</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.deptName } ${info.regName } / ${info.regDate } / ${info.updDate }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">상태</label>
											<div class="col-lg-10">
												<p class="form-control-static">
													<c:choose>
														<c:when test="${info.status == '01' }">지급신청</c:when>
														<c:when test="${info.status == '99' }">지급거절</c:when>
														<c:when test="${info.status == 'AA' }">지급완료</c:when>
													</c:choose>
												</p>
											</div>
										</div>
										<c:if test="${info.status == 'AA' }">
										<div class="form-group">
											<label class="col-lg-2 control-label">지급 담당자</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.confirmName }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">지급일시</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.giveDate }</p>
											</div>
										</div>
										</c:if>
										<c:if test="${info.status == '99' }">
										<div class="form-group">
											<label class="col-lg-2 control-label">거절 담당자</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.confirmName }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">거절일시</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.giveDate }</p>
											</div>
										</div>
										</c:if>
									</form>
								</div>
							</section>
						</div>
					</div>
					<!-- body top end -->
					
					<!-- bottom button start -->
					<div class="btn-row text-right">
						<input type="hidden" id="seq" value="${info.parkingSeq }">
						<input type="hidden" id="queStr" value="${queStr}" />
						<c:if test="${isMine && info.status == '01' }">
						<a class="btn btn-default" href="javascript:;" title="수정하기" style="margin-top: 7px;" onClick="javascript:modifyTicket();"><span class="icon_pencil-edit"></span>&nbsp;수정</a>
						<a class="btn btn-default" href="javascript:;" title="삭제하기" style="margin-top: 7px;" onClick="javascript:deleteTicket();"><span class="icon_trash"></span>&nbsp;삭제</a>
						</c:if>
						<c:if test="${isGeneralMagager }">
							<c:if test="${info.status == '01' }">
						<a class="btn btn-success" href="javascript:;" title="지급완료" style="margin-top: 7px;" onClick="javascript:confirmTicket('AA');"><span class="icon_check"></span>&nbsp;지급완료</a>
						<!-- <a class="btn btn-danger" href="javascript:;" title="지급거절" style="margin-top: 7px;" onClick="javascript:confirmTicket('99');"><span class="icon_close"></span>&nbsp;지급거절</a> -->
							</c:if>
						<a class="btn btn-default" href="javascript:;" title="수정하기" style="margin-top: 7px;" onClick="javascript:modifyTicket();"><span class="icon_pencil-edit"></span>&nbsp;수정</a>
						<a class="btn btn-default" href="javascript:;" title="삭제하기" style="margin-top: 7px;" onClick="javascript:deleteTicket();"><span class="icon_trash"></span>&nbsp;삭제</a>
						</c:if>
						<a class="btn btn-default" href="javascript:;" title="이전으로" style="margin-top: 7px;" onClick="javascript:goBack();"><span class="arrow_back"></span>&nbsp;이전</a>
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