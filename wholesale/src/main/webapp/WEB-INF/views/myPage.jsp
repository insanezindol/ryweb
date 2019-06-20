<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>이연제약 도매관리 시스템</title>
		<!-- Bootstrap CSS -->
		<link href="/css/bootstrap.min.css" rel="stylesheet">
		<link href="/css/bootstrap-theme.css?ver=20190306" rel="stylesheet">
		<link href="/css/elegant-icons-style.css" rel="stylesheet" />
		<link href="/css/font-awesome.min.css" rel="stylesheet" />
		<!-- Custom styles -->
		<link href="/css/style.css?ver=20190306" rel="stylesheet">
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
		<script type="text/javascript" src="/js/scripts.js?ver=20190306"></script>
		<script type="text/javascript" src="/js/jquery.autosize.min.js"></script>
		<script type="text/javascript" src="/js/jquery.placeholder.min.js"></script>
		<script type="text/javascript" src="/js/jquery.slimscroll.min.js"></script>
		<!-- common function -->
		<script type="text/javascript" src="/js/common.js?ver=20190306"></script>
		<!-- page script -->
		<script type="text/javascript">
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu99");
				
				// 입력영역에 엔터키 이벤트 바인드
				$("#currentPwd").keydown(function (key) {
				    if (key.keyCode == 13) goPwdChange();
				});
				$("#pwd").keydown(function (key) {
				    if (key.keyCode == 13) goPwdChange();
				});
				$("#pwdCheck").keydown(function (key) {
				    if (key.keyCode == 13) goPwdChange();
				});
			});
			
			// 뒤로 가기
			function goMain(){
				location.href = "/main.do";
			}
			
			// 비밀번호 변경 팝업 열기
			function openPwdChangeModal() {
				$("#pwdChangeModal").modal("show");
			}
			
			// 비밀번호 변경
			function goPwdChange() {
				var currentPwd = $("#currentPwd").val();
				var pwd = $("#pwd").val();
				var pwdCheck = $("#pwdCheck").val();
				
				if (currentPwd == "") {
					alert("현재 비밀번호를 입력해 주세요.");
					return;
				}
				if (pwd == "") {
					alert("새 비밀번호를 입력해 주세요.");
					return;
				}
				if (pwdCheck == "") {
					alert("새 비밀번호 확인을 입력해 주세요.");
					return;
				}
				
				if (pwd.length < 6) {
					alert("비밀번호는 6자리 이상 입력해 주세요.");
					return;
				}
				if (pwd != pwdCheck) {
					alert("새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.");
					return;
				}
				if (currentPwd == pwd) {
					alert("현재 비밀번호와 새 비밀번호는 같을 수 없습니다.");
					return;
				}
				
				var params = {
					auth : "reyon",
					currentPwd : currentPwd,
					pwd : pwd,
				}
					
				var request = $.ajax({
					url: '/admin/pwdChangeAjax.json'
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
							alert("비밀번호 변경이 완료되었습니다.\n보안을 위해 다시 로그인 해주세요.");
							location.href="/logout.do";
						} else if (json.resultCode == 0){
							$("#currentPwd").val("");
							$("#pwd").val("");
							$("#pwdCheck").val("");
							alert("현재 비밀번호가 일치하지 않습니다.\n다시 확인해주세요.");
							$("#pwdChangeModal").modal("hide");
						} else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
						} else {
							alert(json.resultMsg);
						}
					}
					, complete : function() {
						cfCloseMagnificPopup();
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
								<i class="fa fa-user"></i> 마이 페이지
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>도매 관리 시스템</li>
								<li><i class="fa fa-user"></i>마이 페이지</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">내 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">사업자 번호</label>
											<div class="col-lg-10">
												<p class="form-control-static">${principalUsername }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">이름</label>
											<div class="col-lg-10">
												<p class="form-control-static">${principalSaupName }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">사용구분</label>
											<div class="col-lg-10">
												<p class="form-control-static">${principalUseYn }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">등록일자</label>
											<div class="col-lg-10">
												<p class="form-control-static">${principalRegDate }</p>
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
						<a class="btn btn-default" href="javascript:;" title="이전으로" style="margin-top: 7px;" onClick="javascript:openPwdChangeModal();"><i class="fa fa-key"></i>&nbsp;비밀번호 변경</a>
						<a class="btn btn-default" href="javascript:;" title="이전으로" style="margin-top: 7px;" onClick="javascript:goMain();"><i class="fa fa-arrow-circle-o-left"></i>&nbsp;이전</a>
					</div>
					<!-- bottom button end -->
					
				</section>
				<!--overview end-->
				
			</section>
			<!--main content end-->
			
		</section>
		<!-- container section start -->
		
		<!-- modal popup start -->
		<div aria-hidden="true" aria-labelledby="pwdChangeModalLabel" role="dialog" tabindex="-1" id="pwdChangeModal" class="modal fade" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog" style="width: 50%;">
				<div class="modal-content">
					<div class="modal-header">
						<button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
						<h4 class="modal-title">비밀번호 변경</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal">
							<div class="form-group">
								<label class="col-lg-2 control-label">현재 비밀번호</label>
								<div class="col-lg-10">
									<input type="password" id="currentPwd" class="form-control" maxlength="12" placeholder="현재 비밀번호를 입력하세요.">
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-2 control-label">새 비밀번호</label>
								<div class="col-lg-10">
									<input type="password" id="pwd" class="form-control" minlength="6" maxlength="12" placeholder="새 비밀번호를 입력하세요. (최소 6자리, 최대 12자리)">
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-2 control-label">새 비밀번호 확인</label>
								<div class="col-lg-10">
									<input type="password" id="pwdCheck" class="form-control" minlength="6" maxlength="12" placeholder="새 비밀번호를 한번 더 입력하세요.">
								</div>
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<button class="btn btn-success" type="button" onClick="javascript:goPwdChange();">변경</button>
						<button data-dismiss="modal" class="btn btn-default" type="button">취소</button>
					</div>
				</div>
			</div>
		</div>
		<!-- modal popup end -->
		
		<!--loading Popup start-->
		<%@ include file="/WEB-INF/views/include/loading.jsp"%>
		<!--loading Popup end--> 
		
	</body>
</html>