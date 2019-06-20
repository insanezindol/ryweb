<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<meta name="theme-color" content="#2196F3">
<title>REYON ASSET</title>
<!-- Bootstrap Core Css -->
<link href="/plugins/bootstrap/css/bootstrap.css" rel="stylesheet">
<!-- Waves Effect Css -->
<link href="/plugins/node-waves/waves.css" rel="stylesheet" />
<!-- Animation Css -->
<link href="/plugins/animate-css/animate.css" rel="stylesheet" />
<!-- Custom Css -->
<link href="/css/style.css" rel="stylesheet">
<!-- Google Css -->
<link href="https://fonts.googleapis.com/css?family=Lato:400,300,300italic,400italic,600,600italic,700,700italic,800,800italic" rel="stylesheet">
<link href="https://fonts.googleapis.com/earlyaccess/nanumgothic.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=UA-116158631-2"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());
  gtag('config', 'UA-116158631-2');
</script>
<!-- Jquery Core Js -->
<script src="/plugins/jquery/jquery.min.js"></script>
<!-- Bootstrap Core Js -->
<script src="/plugins/bootstrap/js/bootstrap.js"></script>
<!-- Waves Effect Plugin Js -->
<script src="/plugins/node-waves/waves.js"></script>
<!-- Validation Plugin Js -->
<script src="/plugins/jquery-validation/jquery.validate.js"></script>
<!-- Custom Js -->
<script src="/js/admin.js"></script>
<!-- common function -->
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript">

	$(document).ready(function() {
		if(navigator.userAgent.indexOf("REYONASSET_Android") > -1){
			// alert("앱으로 접속하셨습니다.");
		} else {
			alert("이연제약 앱으로 접속하세요.");
			//location.href = "/asset/blank.do";
		}
		
		// 입력영역에 엔터키 이벤트 바인드
		$("#username").keydown(function(key) {
			if (key.keyCode == 13)
				checkLogin();
		});
		$("#password").keydown(function(key) {
			if (key.keyCode == 13)
				checkLogin();
		});
	});
	
	function checkLogin() {
		if(navigator.userAgent.indexOf("REYONASSET_Android") > -1){
			console.log("1");
			// 앱으로 접속시
			window.HybridApp.goLogin();
		} else {
			console.log("2");
			// 웹으로 접속시
			goLogin("dTmt74w0hpw:APA91bH7w2ZjiHihgsojKoGVF8r6PkTMIh1FHtVmjit__6Mf9euK068puSirF-vz8xhZEVGlX_SCLJ4f_5G7FOv8WFXaSssU77C8swIxXnC7filnxSoMVLwTDOHK6N41hcSU-wKDnn5C", "android", "Y");
		}
	}
	
	function goLogin(token, deviceType, msgReceiveType) {
		var auth = "reyon";
		var username = $("#username").val();
		var password = $("#password").val();
		password = encodeURIComponent(password, "UTF-8");
		token = encodeURIComponent(token, "UTF-8");

		if (username == "") {
			alert("사번을 입력해 주세요.");
			return;
		}
		if (password == "") {
			alert("비밀번호를 입력해 주세요.");
			return;
		}

		var params = {
				auth : auth,
				username : username,
				password : password,
				token : token,
				deviceType : deviceType,
				msgReceiveType : msgReceiveType,
		}

		var request = $.ajax({
			url : '/asset/loginCheckAjax.json',
			type : 'POST',
			timeout : 30000,
			data : params,
			dataType : 'json',
			beforeSend : function(xmlHttpRequest) {
				cfOpenMagnificPopup();
			},
			error : function(xhr, textStatus, errorThrown) {
				alert("시스템 오류가 발생했습니다.");
				cfCloseMagnificPopup();
			},
			success : function(json) {
				if (json.resultCode == 0) {
					location.href = "/asset/main.do";
				} else {
					alert(json.resultMsg);
					$("#password").val("");
					$("#password").focus();
					cfCloseMagnificPopup();
				}
			}
		});
	}
</script>
<style type="text/css">
html, body {
	margin: 0;
	padding: 0;
	min-width: 100%;
	width: 100%;
	max-width: 100%;
	min-height: 100%;
	height: 100%;
	max-height: 100%;
}
</style>
</head>
<body class="login-page">

	<div class="container">
		<div class="login-box">
			<div class="card">
				<div class="body">
					<form id="sign_in" method="POST">
						<div class="msg">
							<i class="material-icons" style="font-size: 90px;">face</i>
						</div>
						<div class="input-group">
							<span class="input-group-addon"> <i class="material-icons">person</i>
							</span>
							<div class="form-line">
								<input type="text" class="form-control" name="username" id="username" placeholder="사번" required autofocus>
							</div>
						</div>
						<div class="input-group">
							<span class="input-group-addon"> <i class="material-icons">lock</i>
							</span>
							<div class="form-line">
								<input type="password" class="form-control" name="password" id="password" placeholder="비밀번호" required>
							</div>
						</div>
						<div class="row">
							<div class="col-xs-8 p-t-5"></div>
							<div class="col-xs-4">
								<button class="btn btn-block bg-indigo waves-effect" type="button" onClick="javascript:checkLogin();">로그인</button>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="logo">
				<small style="text-align: right; margin-bottom: 10px;">Copyright&copy;2019 REYON PHARMACEUTICAL CO LTD</small>
			</div>
		</div>
	</div>

	<!--loading Popup start-->
	<%@ include file="/WEB-INF/views/include/loading.jsp"%>
	<!--loading Popup end-->
</body>
</html>