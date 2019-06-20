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
		<link href="/css/easy-autocomplete.min.css" rel="stylesheet">
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
		<script type="text/javascript" src="/js/jquery.easy-autocomplete.min.js"></script>
		<!-- common function -->
		<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
		<!-- page script -->
		<script type="text/javascript">
		
		$(function() {
			// 진입 시 메뉴 선택
			cfLNBMenuSelect("menu99");
			
			// 사용자 검색
			getTotalUserListAjax();
		});
		
		// 직원 리스트 ajax
		function getTotalUserListAjax() {
			var params = {
				auth : "reyon"
			}
			
			var request = $.ajax({
				url: "/common/getTotalUserListAjax.json"
				, type : "POST"
				, timeout: 10000
				, data : params
				, dataType : "json"
				, error: function(xhr, textStatus, errorThrown) {
					alert("시스템 오류가 발생했습니다.");
				}
				, success : function(json) {
					if (json.resultCode == 0){
						var totalList = json.resultMsg;
						
						var options1 = {
							placeholder : "이름 입력",
							data : totalList,
							getValue : "kname",
							template : {
								type : "custom",
								method : function(value, item) {
									return value + " " + item.posLog + " - " + item.deptName;
								}
							},
							list : {
								match : {
									enabled : true
								},
								onChooseEvent : function() {
									var deptCode = $("#username").getSelectedItemData().deptCode;
									var deptName = $("#username").getSelectedItemData().deptName;
									var sabun = $("#username").getSelectedItemData().sabun;
									var name = $("#username").getSelectedItemData().kname;
									$("#sabun").val(sabun);
									$("#kname").val(name);
								},
							}
						};
						
						$("#username").easyAutocomplete(options1);
					} else if (json.resultCode == 1201) {
						alert(json.resultMsg);
						cfLogin();
					} else {
						alert(json.resultMsg);
					}
				}
			});
		}
		
		// 사진 조회
		function getPhoto() {
			$("#resultDiv").hide();
			var sabun = $("#sabun").val();
			var kname = $("#kname").val();
			if (sabun == "") {
				alert("사용자를 선택해 주세요.");
				return;
			}
			var htmlStr = '';
			htmlStr += '<tr>';
			htmlStr += '<td class="text-center">'+sabun+'</td>';
			htmlStr += '<td class="text-center">'+kname+'</td>';
			htmlStr += '<td class="text-center"><img src="/dev/userPhotoView.do?sabun='+sabun+'"></td>';
			htmlStr += '</tr>';
			$("#resultTbody").empty();
			$("#resultTbody").append(htmlStr);
			$("#resultDiv").show();
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
								<i class="fa fa-picture-o"></i> 사진 조회
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-star-o"></i>정보관리팀</li>
								<li><i class="fa fa-picture-o"></i>사진 조회</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">검색</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">검색</label>
											<div class="col-lg-10">
												<input type="text" id="username" class="form-control">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">이름</label>
											<div class="col-lg-10">
												<input type="text" id="kname" class="form-control" disabled>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">사번</label>
											<div class="col-lg-10">
												<input type="text" id="sabun" class="form-control" disabled>
											</div>
										</div>
										<div class="form-group">
											<div class="col-lg-offset-2 col-lg-10">
												<a class="btn btn-default" href="javascript:;" title="검색하기" style="margin-top: 7px;" onClick="javascript:getPhoto();"><span class="icon_search"></span>&nbsp;검색</a>
											</div>
										</div>
									</form>
								</div>
							</section>
						</div>
					</div>
					<!-- body top end -->
					
					<br>
					
					<!-- main table start -->
					<div class="row" id="resultDiv" style="display:none;">
						<div class="col-lg-12">
							<section class="panel">
								<table class="table table-bordered">
									<colgroup>
										<col style="width: 20%;">
										<col style="width: 20%;">
										<col style="width: 60%;">
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">사번</th>
											<th class="text-center">이름</th>
											<th class="text-center">사진</th>
										</tr>
									</thead>
									<tbody id="resultTbody">
									</tbody>
								</table>
							</section>
						</div>
					</div>
					<!-- main table end -->
					
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