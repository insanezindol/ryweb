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
			cfLNBMenuSelect("menu99");
		});
		
		// 수정 페이지로 이동
		function goModify() {
			var reqSeq = $("#reqSeq").val();
			var url = "/itt/groupwareExtModify.do?reqSeq=" + reqSeq;
			var queStr = $("#queStr").val();
			if(queStr != ""){
				url += "&queStr="+encodeURIComponent(queStr);
			}
			location.href = url;
		}
		
		// 삭제 액션
		function goDelete() {
			var reqSeq = $("#reqSeq").val();
			if(confirm("삭제하시겠습니까?")){
				var params = {
					auth : "reyon",
					reqSeq : reqSeq,
				}
				
				var request = $.ajax({
					url: '/itt/groupwareExtDeleteAjax.json'
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
							var url = "/itt/groupwareExtList.do";
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
			var url = "/itt/groupwareExtList.do";
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
								<i class="fa fa-file-text-o"></i> 그룹웨어 외부접속 상세
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-star-o"></i>정보관리팀</li>
								<li><i class="fa fa-users"></i>그룹웨어 외부접속</li>
								<li><i class="fa fa-file-text-o"></i>그룹웨어 외부접속 상세</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">신청인</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.kname }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">신청시간</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.startDate } ~ ${info.endDate }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">해당시간 중 적용 타입</label>
											<div class="col-lg-10">
												<p class="form-control-static">
												<c:choose>
													<c:when test="${info.accessIngType == '1' }">모든 IP 접속허용</c:when>
													<c:when test="${info.accessIngType == '2' }">접속관리 IP만 허용</c:when>
													<c:when test="${info.accessIngType == '3' }">모든 IP 접속거부</c:when>
												</c:choose>
												</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">해당시간 후 적용 타입</label>
											<div class="col-lg-10">
												<p class="form-control-static">
												<c:choose>
													<c:when test="${info.accessEndType == '1' }">모든 IP 접속허용</c:when>
													<c:when test="${info.accessEndType == '2' }">접속관리 IP만 허용</c:when>
													<c:when test="${info.accessEndType == '3' }">모든 IP 접속거부</c:when>
												</c:choose>
												</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">사유</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.reqComment }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">상태</label>
											<div class="col-lg-10">
												<p class="form-control-static">
												<c:choose>
													<c:when test="${info.status == '1' }">신청</c:when>
													<c:when test="${info.status == '2' }">진행중</c:when>
													<c:when test="${info.status == '3' }">완료</c:when>
												</c:choose>
												</p>
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
						<input type="hidden" id="reqSeq" value="${info.reqSeq}" />
						<input type="hidden" id="queStr" value="${queStr}" />
						<a class="btn btn-default" href="javascript:;" title="수정하기" style="margin-top: 7px;" onClick="javascript:goModify();"><span class="icon_pencil-edit"></span>&nbsp;수정</a>
						<a class="btn btn-default" href="javascript:;" title="삭제하기" style="margin-top: 7px;" onClick="javascript:goDelete();"><span class="icon_trash"></span>&nbsp;삭제</a>
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