<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
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
<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
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
	
	var timerObj;
	$(document).ready(function() {
		var tz = "${tz }";
		var ago7days = lastWeek();
		
		if (tz >= ago7days) {
			// PASS LOGIN
			$(".loaderTxt").html("로그인 중...<br>&nbsp;");
			cfOpenMagnificPopup();
			timerObj = setInterval(countdownPassLogin, 1000);
		} else {
			// DENY LOGIN
			$(".loaderTxt").html("로그인 페이지로 이동 중...(만료된 링크)<br>&nbsp;");
			cfOpenMagnificPopup();
			timerObj = setInterval(countdownDenyLogin, 1000);
		}
	});
	
	var time_sec = 3;
	function countdownPassLogin() {
		if(time_sec == 0) {
			clearInterval(timerObj);
			goLogin();
		}
		$(".loaderTxt").html("로그인 중...<br>"+(time_sec--)+"초");
	}
	function countdownDenyLogin() {
		if(time_sec == 0) {
			clearInterval(timerObj);
			location.href="/logout.do";
		}
		$(".loaderTxt").html("로그인 페이지로 이동 중...(만료된 링크)<br>"+(time_sec--)+"초");
	}
	
	function lastWeek() {
		var today = new Date();
	    var dayOfMonth = today.getDate();
	    today.setDate(dayOfMonth - 7);
	    var dd = today.getDate();
	    var mm = today.getMonth()+1;
	    var yyyy = today.getFullYear();
	    if(dd<10){ dd='0'+dd; }
	    if(mm<10){ mm='0'+mm; }
	    return yyyy + "" + mm + '' + dd;
	}
	
	function goLogin() {
		var userId = "${uid }";
		var userPwd = "reyonadmin123!@#";
		userPwd = encodeURIComponent(userPwd, "UTF-8");
		
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
			, error: function(xhr, textStatus, errorThrown) {
				location.href="/main.do";
			}
			, success : function(json) {
				 if (json.resultCode == 0) {
					 location.href="/main.do";
				} else {
					alert(json.resultMsg);
					location.href="/main.do";
				}
			}
		});
	}
	
</script>
</head>
<body>
	<!--loading Popup start-->
	<%@ include file="/WEB-INF/views/include/loading.jsp"%>
	<!--loading Popup end--> 
</body>
</html>