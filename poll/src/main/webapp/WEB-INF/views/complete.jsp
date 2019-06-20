<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Reyon Poll System</title>
<style type="text/css">
body {
	background-color: #56baed;
}
</style>
<!-- jquery -->
<script type="text/javascript" src="/poll/js/jquery.min.js"></script>
<!--[if lt IE 9]>
<script src="js/html5shiv.js"></script>
<script src="js/respond.min.js"></script>
<![endif]-->
<script type="text/javascript">
	$(document).ready(function() {
		alert("이미 투표에 참여하셨습니다.");
		location.href = "/poll/logout.do";
	});
</script>
</head>
<body>
</body>
</html>