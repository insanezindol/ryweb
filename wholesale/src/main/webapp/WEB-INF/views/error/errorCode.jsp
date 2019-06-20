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
		<script type="text/javascript">
			
			function goPage(){
				var url = "/main.do";
				location.href = url;	
			}
			
		</script>
	</head>
	
	<body>
	
		<!-- container section start -->
		<section id="container" class="">
		
			<!-- header start -->
			<header class="header header-bg">
				<div class="toggle-nav">
					<div class="icon-reorder tooltips" data-original-title="네비게이션" data-placement="bottom">
						<i class="fa fa-bars"></i>
					</div>
				</div>
			
				<!--logo start-->
				<a href="/main.do" class="logo">이연제약 도매관리 시스템</a>
				<!--logo end-->
			</header>
			<!-- header end -->
		
			<!--sidebar start-->
			<aside>
				<div id="sidebar" class="nav-collapse ">
					<!-- sidebar menu start-->
					<ul class="sidebar-menu">
						
					</ul>
					<!-- sidebar menu end-->
				</div>
			</aside> 
			<!--sidebar end--> 
			
			<!--main content start-->
			<section id="main-content">
				<!--overview start-->
				<section class="wrapper">
				
					<!-- title start -->
					<div class="row">
						<div class="col-lg-12">
							<h3 class="page-header">
								<i class="fa fa-window-close-o"></i> 에러페이지
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-window-close-o"></i>에러 페이지</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<div class="row">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<div class="panel panel-default">
								<div class="panel-heading">
									<h2>
										<strong>에러 메시지</strong>
									</h2>
								</div>
								<div class="panel-body">
									<p>요청하신 페이지를 바르게 표시할 수 없습니다.</p>
									<div class="btn-row">
										<a class="btn btn-default" href="javascript:;" title="확인" style="margin-top: 7px;" onClick="javascript:goPage();"><i class="fa fa-check"></i>&nbsp;확인</a>
									</div>
								</div>
							</div>
						</div>
					</div>
					
				</section>
				<!--overview end-->
				
			</section>
			<!--main content end-->
			
		</section>
		<!-- container section start -->
		
	</body>
</html>