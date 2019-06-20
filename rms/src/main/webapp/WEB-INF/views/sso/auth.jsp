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
	
	$(document).ready(function() {
		goLogin();
	});
	
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
			, beforeSend: function(xmlHttpRequest) {
				cfOpenMagnificPopup();
			}
			, error: function(xhr, textStatus, errorThrown) {
				// alert("시스템 오류가 발생했습니다.");
				cfCloseMagnificPopup();
				location.href="/main.do";
			}
			, success : function(json) {
				 if (json.resultCode == 0) {
					 location.href="/main.do";
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