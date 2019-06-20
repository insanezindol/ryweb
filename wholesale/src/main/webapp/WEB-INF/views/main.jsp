<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>이연제약 도매관리 시스템</title>
		<!-- Bootstrap CSS -->
		<link href="/css/bootstrap.min.css" rel="stylesheet">
		<link href="/css/bootstrap-theme.css?ver=20190306" rel="stylesheet">
		<link href="/css/elegant-icons-style.css" rel="stylesheet" />
		<link href="/css/font-awesome.min.css" rel="stylesheet" />
		<!-- Custom styles -->
		<link href="/css/style.css?ver=20190306" rel="stylesheet">
		<link href="/css/style-responsive.css" rel="stylesheet" />
		<link href="/css/jquery-ui-1.10.4.min.css" rel="stylesheet">
		<!-- full calendar css-->
  		<link href="/css/fullcalendar.min.css?ver=20190306" rel="stylesheet" />
  		<link href="/css/fullcalendar.print.min.css?ver=20190306" rel="stylesheet" media="print" />
  		<style type="text/css">.fc-content:hover{cursor: pointer;}</style>
		<!-- jquery script -->
		<script type="text/javascript" src="/js/jquery-1.8.3.min.js"></script>
		<script type="text/javascript" src="/js/jquery-ui-1.10.4.min.js"></script>
		<script type="text/javascript" src="/js/jquery-ui-1.9.2.custom.min.js"></script>
		<!-- bootstrap script -->
		<script type="text/javascript" src="/js/bootstrap.min.js"></script>
		<!-- nice scroll script -->
		<!-- <script type="text/javascript" src="/js/jquery.scrollTo.min.js"></script>
		<script type="text/javascript" src="/js/jquery.nicescroll.js"></script> -->
		<!-- moment script -->
  		<script type="text/javascript" src="/js/moment.js"></script>
		<!-- full calendar script -->
		<script type="text/javascript" src="/js/fullcalendar.min.js?ver=20190306"></script>
		<script type="text/javascript" src="/js/fullcalendar-ko.js?ver=20190306"></script>
		<!-- custom script for this page-->
		<script type="text/javascript" src="/js/scripts.js?ver=20190306"></script>
		<script type="text/javascript" src="/js/jquery.autosize.min.js"></script>
		<script type="text/javascript" src="/js/jquery.placeholder.min.js"></script>
		<script type="text/javascript" src="/js/jquery.slimscroll.min.js"></script>
		<!-- common function -->
		<script type="text/javascript" src="/js/common.js?ver=20190306"></script>
		<!-- page script -->
		<script type="text/javascript">
			$(function() {
				
			});
		</script>
	</head>
	
	<body>
	
		<!-- container section start -->
		<section id="container" class="">
		
			<!-- header start -->
			<%@ include file="/WEB-INF/views/include/gnb.jsp"%>
			<!-- header end -->
		
			<!--sidebar start-->
			<%@ include file="/WEB-INF/views/include/lnb.jsp"%>
			<!--sidebar end--> 
			
			<!--main content start-->
			<section id="main-content">
				<!--overview start-->
				<section class="wrapper">
				
					<!-- title start -->
					<div class="row">
						<div class="col-lg-12">
							<h3 class="page-header">
								<i class="fa fa-home"></i> 메인&nbsp;&nbsp;&nbsp;&nbsp;
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>도매 관리 시스템</li>
								<li><i class="fa fa-home"></i>메인</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- main start -->
					<div class="row">
						<div class="col-lg-12">
						메인 화면 입니다.
						</div>
					</div>
					<!-- main end -->
			
				</section>
				<!--overview end-->
				
			</section>
			<!--main content end-->
			
		</section>
		<!-- container section start -->
		
	</body>
</html>