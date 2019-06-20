<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<sec:authentication property="principal.kname" var="principalKname" />
<sec:authentication property="principal.posLog" var="principalPosLog" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<title>Reyon Poll System</title>
<link href="/poll/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="/poll/css/simple-line-icons.css" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Lato:300,400,700,300italic,400italic,700italic" rel="stylesheet" type="text/css">
<link href="/poll/css/landing-page.min.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/poll/js/jquery.min.js"></script>
<script type="text/javascript" src="/poll/js/popper.min.js"></script>
<script type="text/javascript" src="/poll/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/poll/js/common.js"></script>
<script type="text/javascript">

	$(document).ready(function() {
		
	});
	
</script>
</head>

<body>

	<!-- Navigation -->
	<nav class="navbar navbar-light bg-light static-top">
		<div class="container">
			<a class="navbar-brand" href="#">이연제약 투표 시스템</a>
			<a class="btn btn-primary" href="/poll/logout.do">나가기</a>
		</div>
	</nav>

	<!-- Masthead -->
	<header class="masthead text-white text-center">
		<div class="overlay"></div>
		<div class="container">
			<div class="row">
				<div class="col-xl-9 mx-auto">
					<h3 class="mb-5">이연제약 CI 투표</h3>
					<h3 class="mb-5">${principalKname} ${principalPosLog}님 안녕하세요!</h3>
					<h3 class="mb-5">각각의 CI에 점수를 선택하고, 하단의 투표하기 버튼을 눌러주세요.</h3>
				</div>
			</div>
		</div>
	</header>

	<!-- Icons Grid -->
	<section class="features-icons bg-light text-center">
		<div class="container">
			<div class="row">
				<div class="col-lg-12">
					
				</div>
			</div>
		</div>
	</section>

	<!-- Footer -->
	<footer class="footer bg-light">
		<div class="container">
			<div class="row">
				<div class="col-lg-6 h-100 text-center text-lg-right my-auto">
					<ul class="list-inline mb-0">
						<li class="list-inline-item mr-3"><a href="#"> <i class="fa fa-facebook fa-2x fa-fw"></i>
						</a></li>
						<li class="list-inline-item mr-3"><a href="#"> <i class="fa fa-twitter fa-2x fa-fw"></i>
						</a></li>
						<li class="list-inline-item"><a href="#"> <i class="fa fa-instagram fa-2x fa-fw"></i>
						</a></li>
					</ul>
				</div>
			</div>
		</div>
	</footer>

</body>

</html>