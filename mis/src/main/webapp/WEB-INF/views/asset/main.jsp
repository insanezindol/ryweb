<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<meta name="theme-color" content="#2196F3">
	<title>REYON ASSET</title>
	<link rel="stylesheet" href="/plugins/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" href="/plugins/bootstrap-select/css/bootstrap-select.css">
	<link rel="stylesheet" href="/plugins/node-waves/waves.css">
	<link rel="stylesheet" href="/plugins/animate-css/animate.css">
	<link rel="stylesheet" href="/plugins/morrisjs/morris.css">
	<link rel="stylesheet" href="/css/style.css">
	<link rel="stylesheet" href="/css/themes/all-themes.css">
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
	<!-- Select Plugin Js -->
	<script src="/plugins/bootstrap-select/js/bootstrap-select.js"></script>
	<script src="/plugins/bootstrap-select/js/i18n/defaults-ko_KR.min.js"></script>
	<!-- Slimscroll Plugin Js -->
	<script src="/plugins/jquery-slimscroll/jquery.slimscroll.js"></script>
	<!-- Waves Effect Plugin Js -->
	<script src="/plugins/node-waves/waves.js"></script>
	<!-- Jquery CountTo Plugin Js -->
	<script src="/plugins/jquery-countto/jquery.countTo.js"></script>
	<!-- Morris Plugin Js -->
	<script src="/plugins/raphael/raphael.min.js"></script>
	<script src="/plugins/morrisjs/morris.js"></script>
	<!-- AES script -->
	<script src="/js/AesUtil.js"></script>
	<script src="/js/aes.js"></script>
	<script src="/js/pbkdf2.js"></script>
	<!-- Custom Js -->
	<script src="/js/admin.js"></script>
	<!-- common function -->
	<script type="text/javascript" src="/js/common.js?ver=20190327"></script>
	<script type="text/javascript">
		var aesUtil = new AesUtil(128, 1000); // 암호화 객체
		
		$(function() {
			// 진입 시 메뉴 선택
			cfLNBMenuSelect("lnbMain");
			
			// set sidemenu info
			cfSetLnbInfo();
		});
		
	</script>
	</head>

	<body class="theme-indigo">
		<!--top start-->
		<div class="overlay"></div>
		<nav class="navbar">
			<div class="container-fluid">
				<div class="navbar-header">
					<a href="javascript:;" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse" aria-expanded="false"></a>
					<a href="javascript:;" class="bars"></a> <a class="navbar-brand" href="/asset/main.do">REYON Pharmaceutical</a>
				</div>
			</div>
		</nav>
		<!--top end-->
		
		<!--sidebar start-->
		<section>
			<aside id="leftsidebar" class="sidebar">
				<div class="user-info">
					<div class="image">
						<img id="sidebar_img" src="" width="48" height="48" />
					</div>
					<div class="info-container">
						<div id="sidebar_name" class="name" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"></div>
						<div id="sidebar_dept" class="email"></div>
					</div>
				</div>
		
				<div class="menu">
					<ul class="list">
						<li class="header">NAVIGATION</li>
						<li id="lnbMain"><a href="/asset/main.do"> <i class="material-icons">home</i> <span>메인</span></a></li>
						<!-- <li id="lnbHr">
							<a href="javascript:;" class="menu-toggle"> <i class="material-icons">person</i> <span>인력정보</span></a>
							<ul class="ml-menu">
								<li id="lnbSalaryView"><a href="/hr/salaryView.do">급여명세서 조회</a></li>
							</ul>
						</li>
						<li id="lnbSales">
							<a href="javascript:;" class="menu-toggle"> <i class="material-icons">local_hospital</i> <span>영업정보</span></a>
							<ul class="ml-menu">
								<li id="lnbSalesPerformanceView"><a href="/sales/salesPerformanceView.do">판매/수금 목표대비 실적 조회</a></li>
							</ul>
						</li> -->
						<li id="lnbMain"><a href="/asset/logout.do"> <i class="material-icons">highlight_off</i> <span>로그아웃</span></a></li>
					</ul>
				</div>
		
				<div class="legal">
					<div class="copyright">
						<a href="javascript:;">&copy; REYON PHARMACEUTICAL CO LTD.</a>
					</div>
					<div class="version">
						<b>Version : </b>1.0
					</div>
				</div>
			</aside>
		</section>
		<!--sidebar end-->
	
		<section class="content">
			<div class="container-fluid">
				<div class="block-header">
					<h2>ALARM LIST</h2>
				</div>
				<c:choose>
					<c:when test="${fn:length(alarmInfo) == 0}">
				
					</c:when>
					<c:otherwise>
						<c:forEach var="result" items="${alarmInfo}" varStatus="status">
				<div class="row clearfix">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div class="card">
							<div class="header bg-indigo">
								<h2>${result.alarmTitle }<small>${result.alarmDate }</small></h2>
							</div>
							<div class="body">${result.alarmContents }</div>
						</div>
					</div>
				</div>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</div>
		</section>
		
		<!--loading Popup start-->
		<%@ include file="/WEB-INF/views/include/loading.jsp"%>
		<!--loading Popup end--> 
		
	</body>
</html>