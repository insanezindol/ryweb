<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>이연제약 투표 시스템</title>
<!-- bootstrap css -->
<link rel="stylesheet" href="css/bootstrap.min.css" >
<style type="text/css">
@import url('https://fonts.googleapis.com/css?family=Nanum+Gothic');
body {
	width: 100wh;
	height: 90vh;
	color: #fff;
	background: linear-gradient(-45deg, #EE7752, #E73C7E, #23A6D5, #23D5AB);
	background-size: 400% 400%;
	-webkit-animation: Gradient 15s ease infinite;
	-moz-animation: Gradient 15s ease infinite;
	animation: Gradient 15s ease infinite;
	font-family: 'Nanum Gothic', sans-serif;
}

.vote-bg-custom {
	width: 100wh;
	color: #fff;
	background: linear-gradient(-45deg, #EE7752, #E73C7E, #23A6D5, #23D5AB);
	background-size: 400% 400%;
	-webkit-animation: Gradient 15s ease infinite;
	-moz-animation: Gradient 15s ease infinite;
	animation: Gradient 15s ease infinite;
}

@-webkit-keyframes Gradient {
	0% {
		background-position: 0% 50%
	}
	50% {
		background-position: 100% 50%
	}
	100% {
		background-position: 0% 50%
	}
}

@-moz-keyframes Gradient {
	0% {
		background-position: 0% 50%
	}
	50% {
		background-position: 100% 50%
	}
	100% {
		background-position: 0% 50%
	}
}

@keyframes Gradient {
	0% {
		background-position: 0% 50%
	}
	50% {
		background-position: 100% 50%
	}
	100% {
		background-position: 0% 50%
	}
}

h1,
h6 {
	font-family: 'Open Sans';
	font-weight: 300;
	text-align: center;
	position: absolute;
	top: 45%;
	right: 0;
	left: 0;
}

.card-container.card {
    max-width: 350px;
    padding: 40px 40px;
}

.btn {
    font-weight: 700;
    height: 36px;
    -moz-user-select: none;
    -webkit-user-select: none;
    user-select: none;
    cursor: default;
}

/*
 * Card component
 */
.card {
    background-color: #F7F7F7;
    /* just in case there no content*/
    padding: 20px 25px 30px;
    margin: 0 auto 25px;
    margin-top: 100px;
    /* shadows and rounded borders */
    -moz-border-radius: 2px;
    -webkit-border-radius: 2px;
    border-radius: 2px;
    -moz-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
    -webkit-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
    box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
}

.profile-img-card {
    width: 96px;
    height: 96px;
    margin: 0 auto 10px;
    display: block;
    -moz-border-radius: 50%;
    -webkit-border-radius: 50%;
    border-radius: 50%;
}

/*
 * Form styles
 */
.profile-name-card {
    font-size: 16px;
    font-weight: bold;
    text-align: center;
    margin: 10px 0 0;
    min-height: 1em;
}

.form-signin #inputEmail,
.form-signin #inputPassword {
    direction: ltr;
    height: 44px;
    font-size: 16px;
}

.form-signin input[type=email],
.form-signin input[type=password],
.form-signin input[type=text],
.form-signin button {
    width: 100%;
    display: block;
    margin-bottom: 10px;
    z-index: 1;
    position: relative;
    -moz-box-sizing: border-box;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
}

.form-signin .form-control:focus {
    border-color: rgb(104, 145, 162);
    outline: 0;
    -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgb(104, 145, 162);
    box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgb(104, 145, 162);
}

.btn.btn-signin {
    background-color: #55B359;
    padding: 0px;
    font-weight: 700;
    font-size: 14px;
    height: 36px;
    -moz-border-radius: 3px;
    -webkit-border-radius: 3px;
    border-radius: 3px;
    border: none;
    -o-transition: all 0.218s;
    -moz-transition: all 0.218s;
    -webkit-transition: all 0.218s;
    transition: all 0.218s;
}

.btn.btn-signin:hover,
.btn.btn-signin:active,
.btn.btn-signin:focus {
    background-color: #489A4B;
}

</style>
<!-- jquery -->
<script type="text/javascript" src="js/jquery-3.3.1.min.js"></script>
<!-- popper -->
<script type="text/javascript" src="js/popper.js"></script>
<!-- bootstrap script -->
<script type="text/javascript" src="js/bootstrap.js"></script>
<!--[if lt IE 9]>
<script src="js/html5shiv.js"></script>
<script src="js/respond.min.js"></script>
<![endif]-->

<script type="text/javascript">

	$(document).ready(function() {
		
		// 버전 체크
		if (isIE () && isIE () <= 9) {
			alert("현재 사용하시는 브라우저는 IE9이하 버전입니다.\n최신 브라우저(스마트폰 모바일 브라우저, IE10 이상, Chrome)에서 서비스 이용이 가능합니다.");
		}
		
		// 입력영역에 엔터키 이벤트 바인드
		$("#userId").keydown(function (key) {
		    if (key.keyCode == 13) goLogin();
		});
		$("#userPwd").keydown(function (key) {
		    if (key.keyCode == 13) goLogin();
		});
	});

	// 로그인 액션
	function goLogin() {
		if ($("#userId").val() == "") {
			alert("사번을 입력해 주세요.");
			return;
		}
		
		if ($("#userPwd").val() == "") {
			alert("MIS 비밀번호를 입력해 주세요.");
			return;
		}
		
		var params = {
				userId : $("#userId").val() , 
				userPwd : $("#userPwd").val()
		}
		
		var request = $.ajax({
			url: './loginAjax.json'
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
					location.href="./main.do";
				} else {
					alert(json.resultMsg);
					$("#userPwd").val("");
					$("#userPwd").focus();
					cfCloseMagnificPopup();
				}
			}
		});
	}
	
	// 로딩 팝업 열기
	function cfOpenMagnificPopup() {
		$.magnificPopup.open({
			items: {
				src: '#loadingPopup',
			},
			type: 'inline',
			removalDelay: 200,
			mainClass: 'my-mfp-slide-bottom',
			showCloseBtn: false,
			closeOnBgClick: false,
			enableEscapeKey: false,
		});
	}

	// 로딩 팝업 닫기
	function cfCloseMagnificPopup() {
		$('#loadingPopup').magnificPopup('close');
	}
	
	// 브라우저 버전 체크
	function isIE () {
		var myNav = navigator.userAgent.toLowerCase();
		return (myNav.indexOf('msie') != -1) ? parseInt(myNav.split('msie')[1]) : false;
	}

</script>

</head>
<body>

	<div class="container">
		<div class="card card-container">
			<img id="profile-img" class="profile-img-card" src="img/main_icon.png" />
			<p id="profile-name" class="profile-name-card"></p>
			<form class="form-signin">
				<input type="text" id="userId" class="form-control" placeholder="사번" required autofocus>
				<input type="password" id="userPwd" class="form-control" placeholder="MIS 비밀번호" required>
				<button class="btn btn-lg btn-primary btn-block btn-signin vote-bg-custom" type="button" onClick="javascript:goLogin();">로그인</button>
			</form>
		</div>
		<div class="text-right">
			<div class="credits" style="margin-top: 20px; color: white; font-size: 10pt;">ⓒ REYON PHARMACEUTICAL CO LTD</div>
			<div class="credits" style="color: white; font-size: 10pt;">IE10 이상 또는 Chrome에서 최적화 된 서비스 이용이 가능합니다.</div>
		</div>
	</div>
	
	<!--loading Popup start-->
	<%@ include file="/WEB-INF/views/loading.jsp"%>
	<!--loading Popup end--> 
	
</body>
</html>