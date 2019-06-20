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
			
			// 조회
			function goSearch(){
				goFileSearch();
				goDBSearch();
			}
			
			// 지급명세서 파일 조회
			function goFileSearch() {
				var yymm = $("#yymm").val();

				var params = {
					auth : "reyon",
					yymm : yymm,
					status : "ING"
				}

				var request = $.ajax({
					url : "/settlement/getSpecificationListAjax.json",
					type : "POST",
					timeout : 10000,
					data : params,
					dataType : "json",
					beforeSend: function(xmlHttpRequest) {
						cfOpenMagnificPopup();
					},
					error : function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					},
					success : function(json) {
						if (json.resultCode == 0) {
							$("#resultFileBody").empty();
							var list = json.list;
							var tag = '';
							if (list.length == 0) {
								tag += '<tr>';
								tag += '<td class="text-center" colspan="4">결과가 없습니다.</td>';
								tag += '</tr>';
							} else {
								for (var i = 0; i < list.length; i++) {
									tag += '<tr>';
									tag += '<td class="text-center">' + (i + 1) + '</td>';
									tag += '<td class="text-center">' + list[i].fileName + '</td>';
									tag += '<td class="text-center"><a class="btn btn-primary btn-sm" href="javascript:;" onClick="javascript:goAnalyze('+(i + 1)+',\'' + list[i].fileName + '\');"><span class="icon_check_alt2"></span>&nbsp;분석</a></td>';
									tag += '<td class="text-center" id="analyzeResult_'+(i + 1)+'"></td>';
									tag += '</tr>';
								}
							}
							$("#resultFileBody").append(tag);
							$("#resultDiv").show();
						} else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
						} else {
							alert(json.resultMsg);
						}
					},
					complete : function() {
						cfCloseMagnificPopup();
					}
				});
			}
			
			// 지급명세서 DB 조회
			function goDBSearch() {
				var yymm = $("#yymm").val();

				var params = {
					auth : "reyon",
					yymm : yymm
				}

				var request = $.ajax({
					url : "/settlement/getSpecificationDBListAjax.json",
					type : "POST",
					timeout : 10000,
					data : params,
					dataType : "json",
					beforeSend: function(xmlHttpRequest) {
						cfOpenMagnificPopup();
					},
					error : function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					},
					success : function(json) {
						if (json.resultCode == 0) {
							$("#resultDBBody").empty();
							var list = json.list;
							var tag = '';
							if (list.length == 0) {
								tag += '<tr>';
								tag += '<td class="text-center" colspan="5">결과가 없습니다.</td>';
								tag += '</tr>';
							} else {
								for (var i = 0; i < list.length; i++) {
									tag += '<tr>';
									tag += '<td class="text-center">' + (i + 1) + '</td>';
									tag += '<td class="text-center">' + list[i].deptName + '</td>';
									tag += '<td class="text-center">' + list[i].kname + '</td>';
									tag += '<td class="text-center">' + list[i].retireYn;
									if(list[i].retireYn == 'O'){
										tag += ' (' + list[i].retireDay + ')';	
									}
									tag += '</td>';
									if(list[i].elementCnt != null){
										tag += '<td class="text-center">' + list[i].elementCnt + '</td>';
									} else {
										tag += '<td class="text-center">미등록</td>';
									}
									tag += '</tr>';
								}
							}
							$("#resultDBBody").append(tag);
							$("#resultDiv").show();
						} else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
						} else {
							alert(json.resultMsg);
						}
					},
					complete : function() {
						cfCloseMagnificPopup();
					}
				});
			}
			
			// 분석
			function goAnalyze(idx, fileName) {
				var yymm = $("#yymm").val();

				var params = {
					auth : "reyon",
					yymm : yymm,
					fileName : fileName,
				}

				var request = $.ajax({
					url : "/settlement/analyzeSpecificationAjax.json",
					type : "POST",
					timeout : 10000,
					data : params,
					dataType : "json",
					beforeSend: function(xmlHttpRequest) {
						cfOpenMagnificPopup();
					},
					error : function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					},
					success : function(json) {
						if (json.resultCode == 0) {
							$("#analyzeResult_"+idx).text("");
							$("#analyzeResult_"+idx).text(json.resultMsg);
						} else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
						} else {
							$("#analyzeResult_"+idx).text(json.resultMsg);
						}
					},
					complete : function() {
						cfCloseMagnificPopup();
					}
				});
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
								<i class="fa fa-database"></i> 지급명세서 분석
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-krw"></i>연말정산 관리</li>
								<li><i class="fa fa-database"></i>지급명세서 분석</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- search bar start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel">
								<header class="panel-heading">지급명세서 파일/DB 조회</header>
								<div class="panel-body" id="searchPanel">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">귀속년도</label>
											<div class="col-lg-8">
												<select id="yymm" class="form-control">
													<option value="201812">2018년</option>
													<!-- <option value="201912">2019년</option> -->
												</select>
											</div>
											<div class="col-lg-2">
												<input type="text" style="display: none;" />
												<a class="btn btn-default" href="javascript:;" title="조회하기" onClick="javascript:goSearch();"><span class="icon_search"></span>&nbsp;조회</a>
											</div>
										</div>
									</form>
								</div>
							</section>
						</div>
					</div>
					<!-- search bar end -->
					
					<!-- main table start -->
					<div class="row" id="resultDiv" style="display:none;">
						<div class="col-lg-6">
							<section class="panel">
								<table class="table table-bordered">
									<colgroup>
										<col style="width: 7%;">
										<col style="width: 43%;">
										<col style="width: 20%;">
										<col style="width: 30%;">
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">번호</th>
											<th class="text-center">파일이름</th>
											<th class="text-center">분석</th>
											<th class="text-center">결과</th>
										</tr>
									</thead>
									<tbody id="resultFileBody">
										<tr><td class="text-center" colspan="4">결과가 없습니다.</td></tr>
									</tbody>
								</table>
							</section>
						</div>
						<div class="col-lg-6">
							<section class="panel">
								<table class="table table-bordered">
									<colgroup>
										<col style="width: 7%;">
										<col style="width: 28%;">
										<col style="width: 28%;">
										<col style="width: 16%;">
										<col style="width: 21%;">
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">번호</th>
											<th class="text-center">소속</th>
											<th class="text-center">이름</th>
											<th class="text-center">퇴사여부</th>
											<th class="text-center">분석여부</th>
										</tr>
									</thead>
									<tbody id="resultDBBody">
										<tr><td class="text-center" colspan="5">결과가 없습니다.</td></tr>
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