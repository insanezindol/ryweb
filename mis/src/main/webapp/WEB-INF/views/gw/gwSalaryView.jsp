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
	
	$(document).ready(function() {
		goLogin();
	});
	
	function goLogin() {
		var userId = "${userId }";
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
			, beforeSend: function(xmlHttpRequest) {
				cfOpenMagnificPopup();
			}
			, error: function(xhr, textStatus, errorThrown) {
				alert("시스템 오류가 발생했습니다.");
				cfCloseMagnificPopup();
				location.href="/main.do";
			}
			, success : function(json) {
				 if (json.resultCode == 0) {
					location.href="/hr/salaryView.do";
				} else {
					alert(json.resultMsg);
					cfCloseMagnificPopup();
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