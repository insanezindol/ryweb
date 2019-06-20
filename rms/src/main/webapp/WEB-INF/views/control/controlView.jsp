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
		<script type="text/javascript" src="/js/scripts.js?ver=20190619"></script> <!-- Update into server -->
		<script type="text/javascript" src="/js/jquery.autosize.min.js"></script>
		<script type="text/javascript" src="/js/jquery.placeholder.min.js"></script>
		<script type="text/javascript" src="/js/jquery.slimscroll.min.js"></script>
		<!-- common function -->
		<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
		<!-- page script -->
		<script type="text/javascript">
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu17");
			});
			
			// 수정
			function modifyProduction(){
				var num = $("#num").val();
				var url = "/control/controlModify.do?num=" + num;
				var queStr = $("#queStr").val();
				if(queStr != ""){
					url += "&queStr="+encodeURIComponent(queStr);
				}
				location.href = url;
			}

			// 삭제
			function deleteMetarial(){
				var reqno = $("#reqno").val();
				
				if(confirm("삭제 하시겠습니까?")){
					var params = {
						auth : "reyon",
						reqno : reqno
					}
					
					var request = $.ajax({
						url: '/control/controlDeleteAjax.json'
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
								var url = "/control/controlList.do";
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
				var url = "/control/controlList.do";
				var queStr = $("#queStr").val();
				if(queStr != ""){
					url += "?" + queStr;
				}
				location.href = url;
			}
			
			// 첨부파일 다운로드
			function downloadFile(seq, filename) {
				location.href = "/control/controlFileDownload.do?dwAuth=reyon&dwSeq="
						+ seq + "&dwFilename=" + encodeURIComponent(filename);
			}
			
			// Confirm
			function goConfirm(reqno) {
				var params = {
						auth : "reyon",
						reqno : reqno
				}
				
				var request = $.ajax({
					url: '/control/controlConfirmAjax.json'
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
							alert("승인 완료 되었습니다.");
							var url = "/control/controlList.do";
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
			
			// Release
			function goRelease(reqno) {
				var params = {
						auth : "reyon",
						reqno : reqno
				}
				
				var request = $.ajax({
					url: '/control/controlReleaseAjax.json'
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
							alert("해제 완료 되었습니다.");
							var url = "/control/controlList.do";
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
			
			// Check
			function goCheck(reqno) {
				var params = {
					auth : "reyon",
					reqno : reqno
				}
				var url = "/control/controlCheck.do?auth=reyon&reqno="+reqno;
				
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
								<i class="fa fa-list"></i> 재고자산 통제 상세
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-bell-o"></i>재고자산 통제 관리</li>
								<li><i class="fa fa-file-text-o"></i>재고자산 통제 상세</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
						<c:if test="${logInfo.cfemp != null}">
								<section class="panel" style="margin-bottom: 5px;">
									<header class="panel-heading">재고자산 통제 승인 및 해제</header>
									<div class="panel-body">
										<form class="form-horizontal">
											<div class="form-group">
											<label class="col-lg-2 control-label">확인</label>
											<div class="col-lg-10">
												<p class="form-control-static">${logInfo.cfemp }</p>
											</div>
										</div>
										</form>
										<form class="form-horizontal">
											<div class="form-group">
											<label class="col-lg-2 control-label">확인 날짜</label>
											<div class="col-lg-10">
												<p class="form-control-static">${logInfo.cfdate }</p>
											</div>
										</div>
										</form>
										<c:if test="${info.relemp != null }">
											<form class="form-horizontal">
												<div class="form-group">
												<label class="col-lg-2 control-label">해제</label>
												<div class="col-lg-10">
													<p class="form-control-static">${info.relemp }</p>
												</div>
											</div>
											</form>
											<form class="form-horizontal">
												<div class="form-group">
												<label class="col-lg-2 control-label">해제 날짜</label>
												<div class="col-lg-10">
													<p class="form-control-static">${info.reldate }</p>
												</div>
											</div>
											</form>
										</c:if>
									</div>
								</section>
						</c:if>
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">재고자산 통제 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">품명</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.jpmnm }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">통제 내용</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.condesc }</p>
											</div>
										</div>										
										<div class="form-group">
											<label class="col-lg-2 control-label">요청서</label>
											<div class="col-lg-10">
												<p class="form-control-static"><a href="javascript:downloadFile('${fileInfo.reqno }','${fileInfo.reqfilename }');">${fileInfo.reqfilename }</a></p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">통제자</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.kname }</p>
											</div>
										</div>	
										<div class="form-group">
											<label class="col-lg-2 control-label">등록일시 ( 시행기간 )</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.insdate }</p>
											</div>
										</div>
									</form>
								</div>
							</section>
							<c:if test="${logInfo != null}">
								<section class="panel" style="margin-bottom: 5px;">
									<header class="panel-heading">재고자산 통제 검증 정보</header>
									<div class="panel-body">
										<form class="form-horizontal">
											<div class="form-group">
												<label class="col-lg-2 control-label">처리내용</label>
												<div class="col-lg-10">
													<p class="form-control-static">${logInfo.soldesc }</p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-2 control-label">기존 도안</label>
												<div class="col-lg-10">
													<p class="form-control-static"><a href="javascript:downloadFile('${fileInfo.reqno }','${fileInfo.bffilename }');">${fileInfo.bffilename }</a></p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-2 control-label">변경된 도안</label>
												<div class="col-lg-10">
													<p class="form-control-static"><a href="javascript:downloadFile('${fileInfo.reqno }','${fileInfo.affilename }');">${fileInfo.affilename }</a></p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-2 control-label">확인자</label>
												<div class="col-lg-10">
													<p class="form-control-static">${logInfo.kname }</p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-2 control-label">확인날짜</label>
												<div class="col-lg-10">
													<p class="form-control-static">${logInfo.logdate }</p>
												</div>
											</div>
										</form>
									</div>
								</section>
							</c:if>
						</div>
					</div>
					<!-- body top end -->
					
					<!-- bottom button start -->
					<div class="btn-row text-right">
						<input type="hidden" id="reqno" value="${info.reqno }">
						<input type="hidden" id="queStr" value="${queStr}" />
						<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')">
						<c:if test="${logInfo.logemp != null and logInfo.cfemp == null }">
						<a class="btn btn-default" href="javascript:;" title="승인하기" style="margin-top: 7px;" onClick="javascript:goConfirm(${info.reqno });"><span class="glyphicon glyphicon-ok"></span>&nbsp;승인</a>	
						</c:if>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')">
						<c:if test="${logInfo.logemp != null and logInfo.cfemp != null and info.relemp == null}">
						<a class="btn btn-default" href="javascript:;" title="통제해제" style="margin-top: 7px;" onClick="javascript:goRelease(${info.reqno });"><span class="glyphicon glyphicon-ok"></span>&nbsp;해제</a>	
						</c:if>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')">
						<c:if test="${logInfo.logemp == null and fileInfo.affilename == null}">
						<a class="btn btn-default" href="javascript:;" title="확인하기" style="margin-top: 7px;" onClick="javascript:goCheck(${info.reqno });"><span class="glyphicon glyphicon-ok"></span>&nbsp;확인</a>	
						</c:if>
						</sec:authorize>
						<c:if test="${isMine and logInfo.cfemp == null}">
						<a class="btn btn-default" href="javascript:;" title="삭제하기" style="margin-top: 7px;" onClick="javascript:deleteMetarial();"><span class="icon_trash"></span>&nbsp;삭제</a>
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