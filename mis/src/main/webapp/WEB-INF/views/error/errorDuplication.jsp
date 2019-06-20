<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
	<title>REYON WEB SERVICE</title>
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
	<!-- Custom Js -->
	<script src="/js/admin.js"></script>
	<!-- common function -->
	<script type="text/javascript" src="/js/common.js"></script>
	<script type="text/javascript">
		function goHome(){
			var url = "/main.do";
			location.href = url;
		}
	</script>
	</head>

	<body class="theme-indigo">
		
		<!--top start-->
		<div class="overlay"></div>
		<nav class="navbar">
			<div class="container-fluid">
				<div class="navbar-header">
					<a href="javascript:;" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse" aria-expanded="false"></a>
					<a href="javascript:;" class="bars"></a> <a class="navbar-brand" href="/main.do">REYON Pharmaceutical</a>
				</div>
				<div class="collapse navbar-collapse" id="navbar-collapse">
		               <ul class="nav navbar-nav navbar-right">
		                   <li class="pull-right"><a href="javascript:void(0);" class="js-right-sidebar" data-close="true"><i class="material-icons">more_vert</i></a></li>
		               </ul>
		           </div>
			</div>
		</nav>
		<!--top end-->
		
		<!--sidebar start-->
		<section>
			<aside id="leftsidebar" class="sidebar">
				<div class="user-info">
					<div class="image">
						<img src="/images/user1.png" width="48" height="48" />
					</div>
					<div class="info-container">
						<div class="name" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							ERROR
						</div>
						<div class="email">
							에러가 발생하였습니다.
						</div>
					</div>
				</div>
		
				<div class="menu">
					<ul class="list">
						<li class="header">&nbsp;</li>
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
			<aside id="rightsidebar" class="right-sidebar">
				<div class="tab-content">
				</div>
			</aside>
		</section>
		<!--sidebar end-->
	
		<section class="content">
			<div class="container-fluid">
				<div class="block-header">
					<h2>ERROR MESSAGE</h2>
				</div>
	            <div class="row clearfix">
	                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	                    <div class="card">
	                        <div class="header bg-indigo">
	                            <h2>요청하신 페이지를 바르게 표시할 수 없습니다.</h2>
	                        </div>
	                        <div class="body">
	                            <div>관리자에게 문의하세요.</div>
	                            <div>에러 코드 : ${resultCode}</div>
								<div>에러 메시지 : ${resultMsg}</div>
	                            <div class="button-demo align-right">
	                                <button type="button" class="btn bg-indigo waves-effect" onClick="javascript:goHome();"><i class="material-icons">home</i><span>HOME</span></button>
	                            </div>
	                        </div>
	                    </div>
	                </div>
	            </div>
			</div>
		</section>
		
		<!--loading Popup start-->
		<%@ include file="/WEB-INF/views/include/loading.jsp"%>
		<!--loading Popup end--> 
		
	</body>
</html>