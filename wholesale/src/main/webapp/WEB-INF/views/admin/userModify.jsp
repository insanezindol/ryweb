<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>이연제약 도매관리 시스템</title>
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
				cfLNBMenuSelect("menu98");
				
				// 값 설정
				$("#saupName").val("${info.saupName }");
				$("#userRole").val("${info.userRole}").prop("selected", true);
				$("#useYn").val("${info.useYn}").prop("selected", true);
			});
			
			// 수정하기
			function goConfirm() {
				var username = $("#username").val();
				var pwd = $("#pwd").val();
				var saupName = $("#saupName").val();
				var userRole = $("#userRole").val();
				var useYn = $("#useYn").val();
				
				if (username == "") {
					alert("사업자 번호를 입력해 주세요.");
					return;
				}
				if (saupName == "") {
					alert("사업자 이름을 입력해 주세요.");
					return;
				}
				if (userRole == "") {
					alert("사용권한을 선택해 주세요.");
					return;
				}
				if (useYn == "") {
					alert("사용여부를 선택해 주세요.");
					return;
				}
				
				if(confirm("수정하시겠습니까?")){
					var params = {
						auth : "reyon",
						username : username,
						pwd : pwd,
						saupName : saupName,
						userRole : userRole,
						useYn : useYn,
					}
					
					var request = $.ajax({
						url: '/admin/userModifyAjax.json'
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
								alert("수정 완료 되었습니다.");
								var url = "/admin/userView.do?seq="+username;
								var queStr = $("#queStr").val();
								if(queStr != ""){
									url += "&queStr=" + encodeURIComponent(queStr);
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
				history.back();
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
								<i class="fa fa-pencil-square-o"></i> 사용자 수정
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>도매관리</li>
								<li><i class="fa fa-cog"></i>관리자</li>
								<li><i class="fa fa-user"></i>사용자 관리</li>
								<li><i class="fa fa-pencil-square-o"></i>사용자 수정</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">사용자 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">사업자 번호</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.username }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">비밀번호</label>
											<div class="col-lg-10">
												<input type="password" id="pwd" class="form-control" maxlength="20">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">사업자 이름</label>
											<div class="col-lg-10">
												<input type="text" id="saupName" class="form-control" maxlength="50">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">사용권한</label>
											<div class="col-lg-10">
												<select id="userRole" class="form-control" style="margin-top: 7px; margin-bottom: 10px;">
													<option value="ROLE_USER">일반사용자</option>
													<option value="ROLE_SUPERUSER">운영자</option>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">사용여부</label>
											<div class="col-lg-10">
												<select id="useYn" class="form-control" style="margin-top: 7px; margin-bottom: 10px;">
													<option value="Y">Y</option>
													<option value="N">N</option>
												</select>
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
						<input type="hidden" id="username" value="${info.username }">
						<input type="hidden" id="queStr" value="${queStr}" />
						<a class="btn btn-default" href="javascript:;" title="수정하기" style="margin-top: 7px;" onClick="javascript:goConfirm();"><i class="fa fa-check"></i>&nbsp;수정</a>
						<a class="btn btn-default" href="javascript:;" title="이전으로" style="margin-top: 7px;" onClick="javascript:goBack();"><i class="fa fa-arrow-circle-o-left"></i>&nbsp;이전</a>
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