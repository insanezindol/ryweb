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
		
		$(function() {
			// 진입 시 메뉴 선택
			cfLNBMenuSelect("menu09");
		});
		
		// 수정 페이지로 이동
		function modifyContents(seq){
			var url = "/app/pcReleaseModify.do?reqSeq=" + seq;
			var queStr = $("#queStr").val();
			if(queStr != ""){
				url += "&queStr="+encodeURIComponent(queStr);
			}
			location.href = url;
		}
		
		// 삭제 액션
		function deleteContents(seq){
			if(confirm("삭제하시겠습니까?")){
				var params = {
					auth : "reyon",
					reqSeq : seq
				}
				
				var request = $.ajax({
					url: '/app/pcReleaseDeleteAjax.json'
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
							alert("삭제되었습니다.");
							var url = "/app/pcReleaseList.do";
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
		
		// 거절, 승인
		function confirmResult(status){
			var reqSeq = $("#reqSeq").val();
			var confirmComment = $("#confirmComment").val();
			var statusStr = "";
			if(status == '99') {
				statusStr = "거절";
			} else {
				statusStr = "승인";
			}
			
			if(confirm(statusStr + "하시겠습니까?")){
				var params = {
					auth : "reyon",
					reqSeq : reqSeq,
					status : status,
					confirmComment : confirmComment,
				}
				
				var request = $.ajax({
					url: '/app/pcReleaseConfirmAjax.json'
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
							alert(statusStr + " 완료 되었습니다.");
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
			var url = "/app/pcReleaseList.do";
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
								<i class="fa fa-file-text-o"></i> PC 종료 해제 상세
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-desktop"></i>PC 관리</li>
								<li><i class="fa fa-file-text-o"></i>PC 종료 해제 상세</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">PC 종료 해제 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">신청인</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.username }-${info.kname }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">이용 시간</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.startDate } ~ ${info.endDate }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">사유</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.reqComment }</p>
											</div>
										</div>
								<c:choose>
									<c:when test="${info.status == '01' }">
										<div class="form-group">
											<label class="col-lg-2 control-label">승인 / 거절</label>
											<div class="col-lg-10">
												<div class="input-group">
								                    <input type="text" id="confirmComment" class="form-control" placeholder="승인 , 거절 코멘트" maxlength="60" />
								                    <span class="input-group-addon" onClick="javascript:confirmResult('AA');">
														<span class="input-group-text" style="cursor: pointer;"> 승인 </span>
								                    </span>
								                    <span class="input-group-addon" onClick="javascript:confirmResult('99');">
														<span class="input-group-text" style="cursor: pointer;"> 거절 </span>
								                    </span>
								                </div>
											</div>
										</div>
									</c:when>
									<c:when test="${info.status == '99' || info.status == 'AA' }">
										<div class="form-group">
											<label class="col-lg-2 control-label">승인 / 거절</label>
											<div class="col-lg-10">
												<p class="form-control-static">
													<c:choose>
														<c:when test="${info.status == '99' }">[신청거절] ${info.confirmName } - ${info.confirmComment } (${info.confirmDate })</c:when>
														<c:when test="${info.status == 'AA' }">[승인완료] ${info.confirmName } - ${info.confirmComment } (${info.confirmDate })</c:when>
													</c:choose>
												</p>
											</div>
										</div>
									</c:when>
								</c:choose>
									</form>
								</div>
							</section>
						</div>
					</div>
					<!-- body top end -->
					
					<!-- bottom button start -->
					<div class="btn-row text-right">
						<input type="hidden" id="reqSeq" value="${info.reqSeq}" />
						<input type="hidden" id="queStr" value="${queStr}" />
					<c:if test="${info.status == '01' }">
						<a class="btn btn-default" href="javascript:;" title="수정하기" style="margin-top: 7px;" onClick="javascript:modifyContents('${info.reqSeq }');"><span class="icon_pencil-edit"></span>&nbsp;수정</a>
						<a class="btn btn-default" href="javascript:;" title="삭제하기" style="margin-top: 7px;" onClick="javascript:deleteContents('${info.reqSeq }');"><span class="icon_trash"></span>&nbsp;삭제</a>
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