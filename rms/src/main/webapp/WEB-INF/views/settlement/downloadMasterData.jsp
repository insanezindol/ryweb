<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>이연제약 관리 시스템</title>
		<link href="/css/bootstrap.min.css" rel="stylesheet">
		<link href="/css/bootstrap-theme.css?ver=20190619" rel="stylesheet">
		<link href="/css/elegant-icons-style.css" rel="stylesheet" />
		<link href="/css/font-awesome.min.css" rel="stylesheet" />
		<!-- Custom styles -->
		<link href="/css/style.css?ver=20190619" rel="stylesheet">
		<link href="/css/style-responsive.css" rel="stylesheet" />
		<link href="/css/jquery-ui-1.10.4.min.css" rel="stylesheet">
		<!-- javascripts -->
		<script type="text/javascript" src="/js/jquery-1.8.3.min.js"></script>
		<script type="text/javascript" src="/js/jquery-ui-1.10.4.min.js"></script>
		<script type="text/javascript" src="/js/jquery-ui-1.9.2.custom.min.js"></script>
		<!-- moment script -->
		<script type="text/javascript" src="/js/moment.js"></script>
  		<script type="text/javascript" src="/js/moment-ko.js"></script>
		<!-- bootstrap -->
		<script type="text/javascript" src="/js/bootstrap.min.js"></script>
		<!-- custom script for this page-->
		<script type="text/javascript" src="/js/scripts.js?ver=20190619"></script>
		<script type="text/javascript" src="/js/jquery.autosize.min.js"></script>
		<script type="text/javascript" src="/js/jquery.placeholder.min.js"></script>
		<script type="text/javascript" src="/js/jquery.slimscroll.min.js"></script>
		<!-- common function -->
		<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
		<!-- page script -->
		<script type="text/javascript">
			
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu11");
			});
			
			// 엑셀 다운로드
			function downloadExcel() {
				var form = document.createElement("form");
				form.name = "downForm";
				form.id = "downForm";
				form.method = "POST";
				form.action = "/settlement/downloadExcelMasterData.do";
				var auth = document.createElement("input");
				auth.type = "hidden";
				auth.name = "auth";
				auth.value = "reyon";
				var yymm = document.createElement("input");
				yymm.type = "hidden";
				yymm.name = "yymm";
				yymm.value = $("#yymm").val();
				var saup = document.createElement("input");
				saup.type = "hidden";
				saup.name = "saup";
				saup.value = $("#saup").val();
				var gubun = document.createElement("input");
				gubun.type = "hidden";
				gubun.name = "gubun";
				gubun.value = $("#gubun").val();
				$(form).append(auth);
				$(form).append(yymm);
				$(form).append(saup);
				$(form).append(gubun);
				$("#container").append(form);
				form.submit();
				$("#downForm").remove();
			}

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
								<i class="fa fa-database"></i> 기초데이터 다운로드
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-krw"></i>연말정산 관리</li>
								<li><i class="fa fa-database"></i>기초데이터 다운로드</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- search bar start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel">
								<header class="panel-heading">기초데이터 다운로드</header>
								<div class="panel-body" id="searchPanel">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">귀속년도</label>
											<div class="col-lg-8">
												<select id="yymm" class="form-control">
													<option value="2017">2017년</option>
													<option value="2018">2018년</option>
													<option value="2019">2019년</option>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">사업장</label>
											<div class="col-lg-8">
												<select id="saup" class="form-control">
													<option value="10">본사</option>
													<option value="20">진천</option>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">주/종 구분</label>
											<div class="col-lg-8">
												<select id="gubun" class="form-control">
													<option value="master">주현근무지</option>
													<option value="slave">종전근무지</option>
												</select>
											</div>
											<div class="col-lg-2">
												<input type="text" style="display: none;" />
												<a class="btn btn-default" href="javascript:;" title="다운로드 하기" onClick="javascript:downloadExcel();"><span class="icon_download"></span>&nbsp;다운로드</a>
											</div>
										</div>
									</form>
								</div>
							</section>
						</div>
					</div>
					<!-- search bar end -->
					
				</section>
				<!--overview end-->
				
			</section>
			<!--main content end-->
			
		</section>
		<!-- container section start -->
		
		<!--loading Popup start-->
		<%@ include file="/WEB-INF/views/include/loading.jsp"%>
		<!--loading Popup end--> 
		
	</body>
</html>