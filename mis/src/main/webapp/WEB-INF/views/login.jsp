<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<meta name="theme-color" content="#2196F3">
<title>REYON WEB SERVICE</title>
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
<!-- AES script -->
<script src="/js/AesUtil.js"></script>
<script src="/js/aes.js"></script>
<script src="/js/pbkdf2.js"></script>
<!-- common function -->
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript">
	var COOKIES_EXPIRES_DATE = 30; // 30일 동안 쿠키 저장
	var aesUtil = new AesUtil(128, 1000); // 암호화 객체

	$(document).ready(function() {
		// 저장된 쿠키값을 가져와서 ID 칸에 넣어준다. 없으면 공백으로 들어감.
		var rememberId = getCookie("rememberId");

		if (rememberId != "") {
			$("#userId").val(rememberId);
			$("#idSaveCheck").attr("checked", true);
		}

		$("#idSaveCheck").change(function() { // 체크박스에 변화가 있다면,
			if ($("#idSaveCheck").is(":checked")) { // ID 저장하기 체크했을 때,
				var userInputId = $("#userId").val();
				setCookie("rememberId", userInputId, COOKIES_EXPIRES_DATE);
			} else {
				deleteCookie("rememberId");
			}
		});

		// ID 저장하기를 체크한 상태에서 ID를 입력하는 경우, 이럴 때도 쿠키 저장.
		$("#userId").keyup(function() {
			if ($("#idSaveCheck").is(":checked")) {
				var userInputId = $("#userId").val();
				setCookie("rememberId", userInputId, COOKIES_EXPIRES_DATE);
			}
		});

		// 입력영역에 엔터키 이벤트 바인드
		$("#userId").keydown(function(key) {
			if (key.keyCode == 13)
				goLogin();
		});
		$("#userPwd").keydown(function(key) {
			if (key.keyCode == 13)
				goLogin();
		});
	});

	function goLogin() {
		var userId = $("#userId").val();
		var userPwd = $("#userPwd").val();
		userPwd = encodeURIComponent(userPwd, "UTF-8");

		if (userId == "") {
			alert("사번을 입력해 주세요.");
			return;
		}
		if (userPwd == "") {
			alert("비밀번호를 입력해 주세요.");
			return;
		}

		var params = {
			userId : userId,
			userPwd : userPwd
		}

		var request = $.ajax({
			url : '/loginAjax.json',
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
					setCookie("auth_i", aesUtil.customEncrypt(userId), 1);
					setCookie("auth_p", aesUtil.customEncrypt(userPwd), 1);
					location.href = "/main.do";
				} else {
					alert(json.resultMsg);
					$("#userPwd").val("");
					$("#userPwd").focus();
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
								<input type="text" class="form-control" name="userId" id="userId" placeholder="사번" required autofocus>
							</div>
						</div>
						<div class="input-group">
							<span class="input-group-addon"> <i class="material-icons">lock</i>
							</span>
							<div class="form-line">
								<input type="password" class="form-control" name="userPwd" id="userPwd" placeholder="비밀번호" required>
							</div>
						</div>
						<div class="row">
							<div class="col-xs-8 p-t-5">
								<input type="checkbox" name="idSaveCheck" id="idSaveCheck" class="filled-in chk-col-indigo"> <label for="idSaveCheck">사번 기억하기</label>
							</div>
							<div class="col-xs-4">
								<button class="btn btn-block bg-indigo waves-effect" type="button" onClick="javascript:goLogin();">로그인</button>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="logo">
				<small style="text-align: right; margin-bottom: 10px;">&copy; REYON PHARMACEUTICAL CO LTD</small> <small style="text-align: right; margin-bottom: 5px;">Internet Explorer10 이상 또는 Chrome에서</small> <small style="text-align: right;">최적화된 서비스 이용이 가능합니다.</small>
			</div>
		</div>
	</div>

	<!--loading Popup start-->
	<%@ include file="/WEB-INF/views/include/loading.jsp"%>
	<!--loading Popup end-->
</body>
</html>