<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>이연제약 관리 시스템</title>
<!-- Bootstrap CSS -->
<link href="/css/bootstrap.min.css" rel="stylesheet">
<!-- bootstrap theme -->
<link href="/css/bootstrap-theme.css?ver=20190619" rel="stylesheet">
<!--external css-->
<!-- font icon -->
<link href="/css/elegant-icons-style.css" rel="stylesheet" />
<link href="/css/font-awesome.css" rel="stylesheet" />
<!-- Custom styles -->
<link href="/css/style.css?ver=20190619" rel="stylesheet">
<link href="/css/style-responsive.css" rel="stylesheet" />
<!-- jquery script -->
<script type="text/javascript" src="/js/jquery-1.8.3.min.js"></script>
<!-- AES script -->
<script src="/js/AesUtil.js"></script>
<script src="/js/aes.js"></script>
<script src="/js/pbkdf2.js"></script>
<!-- common function -->
<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
<!--[if lt IE 9]>
<script src="/js/html5shiv.js"></script>
<script src="/js/respond.min.js"></script>
<![endif]-->

<script type="text/javascript">

	var COOKIES_EXPIRES_DATE = 30; // 30일 동안 쿠키 저장

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
		$("#userId").keydown(function (key) {
		    if (key.keyCode == 13) goLogin();
		});
		$("#userPwd").keydown(function (key) {
		    if (key.keyCode == 13) goLogin();
		});
		
		checkPassPcAjax();
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
				userId : userId , 
				userPwd : userPwd
		}
		
		var request = $.ajax({
			url: '/loginAjax.json'
			, type : 'POST'
			, timeout: 30000
			, data : params
			, dataType : 'json'
			, beforeSend: function(xmlHttpRequest) {
				cfOpenMagnificPopup();
			}
			, error: function(xhr, textStatus, errorThrown) {
				alert("시스템 오류가 발생했습니다.");
				cfCloseMagnificPopup();
			}
			, success : function(json) {
				 if (json.resultCode == 0) {
					location.href="/main.do";
				} else {
					alert(json.resultMsg);
					$("#userPwd").val("");
					$("#userPwd").focus();
					cfCloseMagnificPopup();
				}
			}
		});
	}
	
	function checkPassPcAjax() {
		var params = {
				auth : "reyon" 
		}
		
		var request = $.ajax({
			url: '/checkPassPcAjax.json'
			, type : 'POST'
			, timeout: 30000
			, data : params
			, dataType : 'json'
			, error: function(xhr, textStatus, errorThrown) {
				
			}
			, success : function(json) {
				if (json.resultCode == 1) {
					 $("#userId").val(json.clientID);
					 $("#userPwd").val(json.clientPW);
					 goLogin();
				}
			}
		});
	}

	function goCompany() {
		var win = window.open("http://reyonpharm.co.kr/", "_blank");
		win.focus();
	}
	
</script>

</head>


<body class="login-img3-body">

	<div class="container">
	
		<form class="login-form">
			<div class="login-wrap">
				<p class="login-img">
					<i class="icon_lock_alt"></i>
				</p>
				<div class="input-group">
					<span class="input-group-addon"><i class="icon_profile"></i></span> <input type="text" id="userId" class="form-control" placeholder="사번" autofocus>
				</div>
				<div class="input-group">
					<span class="input-group-addon"><i class="icon_key_alt"></i></span> <input type="password" id="userPwd" class="form-control" placeholder="비밀번호">
				</div>
				<label class="checkbox"> 
					<input type="checkbox" id="idSaveCheck"> 사번 기억하기
				</label>
				<input class="btn btn-custom btn-lg btn-block" type="button" onClick="javascript:goLogin();" value="로그인" />
			</div>
		</form>
		
		<div class="text-right">
			<div class="credits" style="margin-top: 20px;">
				<a href="javascript:;" onClick="javascript:goCompany();" style="color: black;">ⓒ REYON PHARMACEUTICAL CO LTD</a>
			</div>
			<div class="credits" style="color: black;">
				이연제약 관리시스템은 Internet Explorer10 이상 또는 Chrome에서 최적화된 서비스 이용이 가능합니다.
			</div>
		</div>
		
	</div>
	
	<!--loading Popup start-->
	<%@ include file="/WEB-INF/views/include/loading.jsp"%>
	<!--loading Popup end--> 

</body>
</html>