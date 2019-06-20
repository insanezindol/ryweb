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
		<link href="/css/easy-autocomplete.min.css" rel="stylesheet">
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
		<script type="text/javascript" src="/js/jquery.easy-autocomplete.min.js"></script>
		<!-- common function -->
		<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
		<!-- page script -->
		<script type="text/javascript">
			
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu12");
				
				// 직원 리스트 ajax
				getTotalUserListAjax();
			});
			
			// 직원 리스트 ajax
			function getTotalUserListAjax() {
				var params = {
					auth : "reyon"
				}
				
				var request = $.ajax({
					url: "/common/getTotalUserListAjax.json"
					, type : "POST"
					, timeout: 10000
					, data : params
					, dataType : "json"
					, error: function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					}
					, success : function(json) {
						if (json.resultCode == 0){
							var totalList = json.resultMsg;
							
							var options1 = {
								placeholder : "이름 입력",
								data : totalList,
								getValue : "kname",
								template : {
									type : "custom",
									method : function(value, item) {
										return value + " " + item.posLog + " - " + item.deptName;
									}
								},
								list : {
									match : {
										enabled : true
									},
									onChooseEvent : function() {
										var deptCode = $("#receiveName").getSelectedItemData().deptCode;
										var deptName = $("#receiveName").getSelectedItemData().deptName;
										var sabun = $("#receiveName").getSelectedItemData().sabun;
										var name = $("#receiveName").getSelectedItemData().kname;
										
										if("${sabun}" == sabun) {
											alert("인수자에 본인은 입력할 수 없습니다.");
											$("#receiveName").val("");
										} else {
											$("#receiveSabun").val(sabun);
											alert("인수자 정보 [" + deptName + " : " + name + "] 지정하였습니다.\n[등록] 버튼을 클릭하여 최종 완료 해주세요.");
										}
									},
								}
							};
							
							$("#receiveName").easyAutocomplete(options1);
						} else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
						} else {
							alert(json.resultMsg);
						}
					}
				});
			}
			
			function goConfirm() {
				var receiveSabun = $("#receiveSabun").val();
				
				if (receiveSabun == "") {
					alert("인수자를 입력해 주세요.");
					return;
				}
				
				if (confirm("등록하시겠습니까?")) {
					var params = {
						auth : "reyon",
						receiveSabun : receiveSabun,
					}
					
					var request = $.ajax({
						url : '/takeOverAddAjax.json',
						type : 'POST',
						timeout : 30000,
						data : params,
						dataType : 'json',
						beforeSend : function(xmlHttpRequest) {
							cfOpenMagnificPopup();
						},
						error : function(xhr, textStatus, errorThrown) {
							alert("시스템 오류가 발생했습니다.");
						},
						success : function(json) {
							if (json.resultCode == 1) {
								alert("인계 등록이 완료 되었습니다.");
								location.href = "/takeOver.do";
							} else if (json.resultCode == 1201) {
								alert(json.resultMsg);
								cfLogin();
							} else {
								alert(json.resultMsg);
							}
						},
						complete : function() {
							cfCloseMagnificPopup();
						}
					});
				}
			}
			
			// 수락/거절 처리
			function confirmTakeOver(seq, status){
				var statusText = "";
				if(status == "AA"){
					statusText = "수락";
				} else if(status == "99"){
					statusText = "거절";
				}
				
				if(confirm(statusText + " 하시겠습니까?")){
					var params = {
						auth : "reyon",
						seq : seq,
						status : status,
					}
					
					var request = $.ajax({
						url: '/takeOverModifyAjax.json'
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
								alert(statusText + "이 완료 되었습니다.");
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
			
			<sec:authentication property="principal.username" var="principalUsername" />
			
			<!--main content start-->
			<section id="main-content">
				<!--overview start-->
				<section class="wrapper">
					
					<!-- title start --> 
					<div class="row">
						<div class="col-lg-12">
							<h3 class="page-header">
								<i class="fa fa-gift"></i> 인수인계 관리
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-gift"></i>인수인계 관리</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body middle start -->
					<c:set var="denyTakeOverCnt" value="0"/>
					<c:if test="${fn:length(giveList) != 0}">
						<c:forEach var="result" items="${giveList}" varStatus="status">
							<c:if test="${result.status == '99' }"> <c:set var="denyTakeOverCnt" value="${denyTakeOverCnt + 1}"/></c:if>
							<div class="row">
								<div class="col-lg-12">
									<section class="panel" style="margin-bottom: 5px;">
										<header class="panel-heading">인계 내역</header>
										<div class="panel-body">
											<form class="form-horizontal">
												<div class="form-group">
													<label class="col-lg-2 control-label">인수인계 정보</label>
													<div class="col-lg-4">
														<p class="form-control-static">${result.giveName }  &#x25BA;  ${result.receiveName }</p>
													</div>
													<label class="col-lg-2 control-label">상태</label>
													<div class="col-lg-4">
														<p class="form-control-static">
															<c:choose>
																<c:when test="${result.status == '01' }">인계자 신청 완료, 인수자 수락 대기중</c:when>
																<c:when test="${result.status == '99' }">인계자 신청 완료, 인수자 거절</c:when>
																<c:when test="${result.status == 'AA' }">인계자 신청 완료, 인수자 수락 완료</c:when>
															</c:choose>
														</p>
													</div>
												</div>
												<div class="form-group">
													<label class="col-lg-2 control-label">인수인계 신청 일시</label>
													<div class="col-lg-4">
														<p class="form-control-static">${result.regDate }</p>
													</div>
													<label class="col-lg-2 control-label">인수인계 수락/거절 일시</label>
													<div class="col-lg-4">
														<p class="form-control-static">
															<c:choose>
																<c:when test="${result.giveDate == null }">수락/거절 대기중</c:when>
																<c:otherwise>${result.giveDate }</c:otherwise>
															</c:choose>
														</p>
													</div>
												</div>
											</form>
										</div>
									</section>
								</div>
							</div>
						</c:forEach>
					</c:if>
					
					<c:if test="${denyTakeOverCnt == fn:length(giveList) }">
						<div class="row">
							<div class="col-lg-12">
								<section class="panel" style="margin-bottom: 5px;">
									<header class="panel-heading">등록된 인계 내역이 없습니다.</header>
									<div class="panel-body">
										<form class="form-horizontal">
											<div class="form-group">
												<label class="col-lg-2 control-label">인수자 입력</label>
												<div class="col-lg-8">
													<input type="text" style="display: none;" />
													<input type="text" id="receiveName" class="form-control" maxlength="10">
													<input type="hidden" id="receiveSabun">
												</div>
												<div class="col-lg-2">
													<a class="btn btn-default" href="javascript:;" title="등록하기" onClick="javascript:goConfirm();"><span class="icon_check"></span>&nbsp;등록</a>
												</div>
											</div>
										</form>
									</div>
								</section>
							</div>
						</div>
					</c:if>
					
					<!-- body middle end -->
					
					<br><br>
					
					<!-- body top start -->
					<c:choose>
						<c:when test="${fn:length(receiveList) == 0}">
							<div class="row">
								<div class="col-lg-12">
									<section class="panel" style="margin-bottom: 5px;">
										<header class="panel-heading">인수 내역</header>
										<div class="panel-body">
											<form class="form-horizontal">
												<div class="form-group">
													<div class="col-lg-12">
														<p class="form-control-static" style="text-align: center;">인수 내역이 없습니다.</p>
													</div>
												</div>
											</form>
										</div>
									</section>
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<c:set var="receiveListLength" value="${fn:length(receiveList)}"/>
							<c:forEach var="result" items="${receiveList}" varStatus="status">
								<div class="row">
									<div class="col-lg-12">
										<section class="panel" style="margin-bottom: 5px;">
											<header class="panel-heading">인수 내역</header>
											<div class="panel-body">
												<form class="form-horizontal">
													<div class="form-group">
														<label class="col-lg-2 control-label">인수인계 정보</label>
														<div class="col-lg-4">
															<p class="form-control-static">${receiveList[receiveListLength - status.count].giveName }  &#x25BA;  ${receiveList[receiveListLength - status.count].receiveName }</p>
														</div>
														<label class="col-lg-2 control-label">상태</label>
														<div class="col-lg-4">
															<p class="form-control-static">
																<c:choose>
																	<c:when test="${receiveList[receiveListLength - status.count].status == '01' }">
																		<a class="btn btn-success" href="javascript:;" title="수락" onClick="javascript:confirmTakeOver('${receiveList[receiveListLength - status.count].giveSeq }','AA');"><span class="icon_check"></span>&nbsp;수락</a>
																		<a class="btn btn-danger" href="javascript:;" title="거절" onClick="javascript:confirmTakeOver('${receiveList[receiveListLength - status.count].giveSeq }','99');"><span class="icon_close"></span>&nbsp;거절</a>
																	</c:when>
																	<c:when test="${receiveList[receiveListLength - status.count].status == '99' }">인계자 신청 완료, 인수자 거절</c:when>
																	<c:when test="${receiveList[receiveListLength - status.count].status == 'AA' }">인계자 신청 완료, 인수자 수락 완료</c:when>
																</c:choose>
															</p>
														</div>
													</div>
													<div class="form-group">
														<label class="col-lg-2 control-label">인수인계 신청 일시</label>
														<div class="col-lg-4">
															<p class="form-control-static">${receiveList[receiveListLength - status.count].regDate }</p>
														</div>
														<label class="col-lg-2 control-label">인수인계 수락/거절 일시</label>
														<div class="col-lg-4">
															<p class="form-control-static">
																<c:choose>
																	<c:when test="${receiveList[receiveListLength - status.count].giveDate == null }">수락/거절 대기중</c:when>
																	<c:otherwise>${receiveList[receiveListLength - status.count].giveDate }</c:otherwise>
																</c:choose>
															</p>
														</div>
													</div>
												</form>
											</div>
										</section>
									</div>
								</div>
							</c:forEach>
						</c:otherwise>
					</c:choose>
					<!-- body top end -->
					
					<!-- bottom button start -->
					<div class="btn-row text-right">
						<a class="btn btn-default" href="javascript:;" title="메인으로 이동" style="margin-top: 7px;" onClick="javascript:goMain();"><span class="icon_house_alt"></span>&nbsp;메인</a>
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