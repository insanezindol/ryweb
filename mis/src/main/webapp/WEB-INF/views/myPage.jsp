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
		$(function() {
			// 진입 시 메뉴 선택
			cfLNBMenuSelect("lnbMain");
		});
		
		function goHome(){
			location.href = "/main.do";
		}
	</script>
	</head>

	<body class="theme-indigo">
		<!--top start-->
		<%@ include file="/WEB-INF/views/include/top.jsp"%>
		<!--top end-->
		
		<!--sidebar start-->
		<%@ include file="/WEB-INF/views/include/sidebar.jsp"%>
		<!--sidebar end-->
	
		<section class="content">
			<div class="container-fluid">
				<div class="block-header">
					<h2>마이 페이지</h2>
				</div>
				
				<!-- result start -->
				<div class="row clearfix">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div class="card">
							<div class="header">
								<h2>내 정보</h2>
							</div>
							<div class="body">
								<form class="form-horizontal">
									<div class="row clearfix">
										<div class="col-lg-2 col-md-2 col-sm-4 col-xs-5 form-control-label">
											<label>사번</label>
										</div>
										<div class="col-lg-10 col-md-10 col-sm-8 col-xs-7">
											<div class="form-group">
	                                            <p style="margin-top: 8px;"><sec:authentication property="principal.username" /></p>
	                                        </div>
										</div>
									</div>
									<div class="row clearfix">
										<div class="col-lg-2 col-md-2 col-sm-4 col-xs-5 form-control-label">
											<label>부서명</label>
										</div>
										<div class="col-lg-10 col-md-10 col-sm-8 col-xs-7">
											<div class="form-group">
												<p style="margin-top: 8px;"><sec:authentication property="principal.deptName" /></p>
											</div>
										</div>
									</div>
									<div class="row clearfix">
										<div class="col-lg-2 col-md-2 col-sm-4 col-xs-5 form-control-label">
											<label>직급</label>
										</div>
										<div class="col-lg-10 col-md-10 col-sm-8 col-xs-7">
											<div class="form-group">
	                                            <p style="margin-top: 8px;"><sec:authentication property="principal.posLog" /></p>
	                                        </div>
										</div>
									</div>
									<div class="row clearfix">
										<div class="col-lg-2 col-md-2 col-sm-4 col-xs-5 form-control-label">
											<label>이름</label>
										</div>
										<div class="col-lg-10 col-md-10 col-sm-8 col-xs-7">
											<div class="form-group">
												<p style="margin-top: 8px;"><sec:authentication property="principal.kname" /></p>
											</div>
										</div>
									</div>
									<div class="row clearfix">
										<div class="col-lg-offset-2 col-md-offset-2 col-sm-offset-4 col-xs-offset-5">
											<button type="button" class="btn bg-indigo m-t-15 waves-effect" onClick="javascript:goHome();">확인</button>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
				<!-- result end -->
				
			</div>
		</section>
		
		<!--loading Popup start-->
		<%@ include file="/WEB-INF/views/include/loading.jsp"%>
		<!--loading Popup end--> 
		
	</body>
</html>