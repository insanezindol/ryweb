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
	<!-- AES script -->
	<script src="/js/AesUtil.js"></script>
	<script src="/js/aes.js"></script>
	<script src="/js/pbkdf2.js"></script>
	<!-- Custom Js -->
	<script src="/js/admin.js"></script>
	<!-- common function -->
	<script type="text/javascript" src="/js/common.js"></script>
	<script type="text/javascript">
		var aesUtil = new AesUtil(128, 1000); // 암호화 객체
		
		$(function() {
			// 진입 시 메뉴 선택
			cfLNBMenuSelect("lnbMain");
		});
		
		// 링크 실행
		function goPage(url) {
			var win = window.open(url, '_blank');
			win.focus();
		}
		
		// 그룹웨어 SSO 실행
		function goGWPage() {
			var auth_i = aesUtil.customDecrypt(getCookie("auth_i"));
			var auth_p = aesUtil.customDecrypt(getCookie("auth_p"));
			var url = "http://rymgw.reyonpharm.co.kr/index.aspx?txtuserid="+auth_i+"&txtpassword="+auth_p;
			if (!navigator.userAgent.match(/Android|Mobile|iP(hone|od|ad)|BlackBerry|IEMobile|Kindle|NetFront|Silk-Accelerated|(hpw|web)OS|Fennec|Minimo|Opera M(obi|ini)|Blazer|Dolfin|Dolphin|Skyfire|Zune/)) {
				// PC에서만
				url = "http://rygw.reyonpharm.co.kr/index.aspx?txtuserid="+auth_i+"&txtpassword="+auth_p;
			}
			var win = window.open(url, '_blank');
			win.focus();
		}
		
		// 이연 관리시스템 SSO 실행
		function goRMSPage() {
			var auth_i = aesUtil.customDecrypt(getCookie("auth_i"));
			var url = "http://rms.reyonpharm.co.kr/sso/auth.do?uid="+encodeURIComponent(btoa(auth_i));
			var win = window.open(url, '_blank');
			win.focus();
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
					<h2>메인</h2>
				</div>
				
				<!-- LINK -->
	           <!-- <div class="row clearfix">
	                <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
	                    <div class="info-box bg-red hover-zoom-effect" onClick="javascript:goGWPage();">
	                        <div class="icon">
	                            <i class="material-icons">group_work</i>
	                        </div>
	                        <div class="content">
	                            <div class="text">LINK</div>
	                            <div class="number count-to">그룹웨어</div>
	                        </div>
	                    </div>
	                </div>
	                <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
	                    <div class="info-box bg-pink hover-zoom-effect" onClick="javascript:goRMSPage();">
	                        <div class="icon">
	                            <i class="material-icons">meeting_room</i>
	                        </div>
	                        <div class="content">
	                            <div class="text">LINK</div>
	                            <div class="number count-to">회의 관리</div>
	                        </div>
	                    </div>
	                </div>
	                <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
	                    <div class="info-box bg-purple hover-zoom-effect" onClick="javascript:goPage('http://as82.kr/reyon/');">
	                        <div class="icon">
	                            <i class="material-icons">desktop_windows</i>
	                        </div>
	                        <div class="content">
	                            <div class="text">LINK</div>
	                            <div class="number count-to">PC 원격제어</div>
	                        </div>
	                    </div>
	                </div>
	                <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
	                    <div class="info-box bg-deep-purple hover-zoom-effect" onClick="javascript:goPage('http://www.esafe.or.kr');">
	                        <div class="icon">
	                            <i class="material-icons">school</i>
	                        </div>
	                        <div class="content">
	                            <div class="text">LINK</div>
	                            <div class="number">산업안전협회</div>
	                        </div>
	                    </div>
	                </div>
	            </div>
	            <div class="row clearfix">
	            	<div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
	                    <div class="info-box bg-teal hover-zoom-effect" onClick="javascript:goPage('http://asp.4nb.co.kr/reyonvc/login.asp');">
	                        <div class="icon">
	                            <i class="material-icons">videocam</i>
	                        </div>
	                        <div class="content">
	                            <div class="text">LINK</div>
	                            <div class="number">화상회의</div>
	                        </div>
	                    </div>
	                </div>
	                <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
	                    <div class="info-box bg-green hover-zoom-effect" onClick="javascript:goPage('https://cert.benecafe.co.kr/member/login?&cmpyNo=ABA');">
	                        <div class="icon">
	                            <i class="material-icons">shopping_cart</i>
	                        </div>
	                        <div class="content">
	                            <div class="text">LINK</div>
	                            <div class="number">임직원 복지몰</div>
	                        </div>
	                    </div>
	                </div>
	                <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
	                    <div class="info-box bg-light-indigo hover-zoom-effect" onClick="javascript:goPage('http://go.bizmro.com/');">
	                        <div class="icon">
	                            <i class="material-icons">shopping_basket</i>
	                        </div>
	                        <div class="content">
	                            <div class="text">LINK</div>
	                            <div class="number">MRO 전용몰</div>
	                        </div>
	                    </div>
	                </div>
	            </div> -->
	            <!--// LINK -->
				
			</div>
		</section>
		
		<!--loading Popup start-->
		<%@ include file="/WEB-INF/views/include/loading.jsp"%>
		<!--loading Popup end--> 
		
	</body>
</html>