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
			cfLNBMenuSelect("lnbSales");
			cfLNBChildMenuSelect("lnbSalesPerformanceView");
		});
		
		// 조회
		function goSearch() {
			var yyyymm = $("#yyyymm").val();
			
			if (yyyymm == "") {
				alert("조회 연월을 선택해 주세요.");
				return;
			}
			
			$("#resultDiv").hide();
			
			var params = {
				auth : "reyon",
				yyyymm : yyyymm,
			}
			
			var request = $.ajax({
				url: '/sales/getSalesPerformanceInfoAjax.json'
				, type : 'POST'
				, timeout: 60000
				, data : params
				, dataType : 'json'
				, beforeSend: function(xmlHttpRequest) {
					cfOpenMagnificPopup();
				}
				, error: function(xhr, textStatus, errorThrown) {
					console.log("시스템 오류가 발생했습니다.");
				}
				, success : function(json) {
					if (json.resultCode == 0) {
						$("#resultDiv").show();
						
						var list = json.list;
						if(list.length != 0){
							$("#chart_title").html("판매/수금 목표대비 실적 (단위 : 억)<small>기준일자 : "+list[0].gijunDate.substring(0,4)+"-"+list[0].gijunDate.substring(4,6)+"-"+list[0].gijunDate.substring(6,8)+"</small>");
							$("#bar_chart").empty();
							var chart = Morris.Bar({
								element : 'bar_chart',
								data : list,
								xkey : 'deptNm',
								ykeys : [ 'pmokAmt', 'saleAmt', 'smokAmt', 'sukmAmt' ],
								labels : [ '판매목표', '판매실적', '수금목표', '수금실적' ],
								barColors : [ 'rgb(244, 67, 54)', 'rgb(156, 39, 176)', 'rgb(0, 150, 136)', 'rgb(139, 195, 74)' ],
								gridTextSize : 15,
								gridTextFamily : 'Nanum Gothic',
								resize : true,
							});
							
							
							$("#resultTbody").empty();
							var htmlStr = '';
							htmlStr += '<div class="row clearfix">';
							htmlStr += '<table class="table table-bordered" style="min-width: 950px;">';
							htmlStr += '<colgroup>';
							htmlStr += '<col style="width: *;">';
							htmlStr += '<col style="width: 10%;">';
							htmlStr += '<col style="width: 10%;">';
							htmlStr += '<col style="width: 10%;">';
							htmlStr += '<col style="width: 10%;">';
							htmlStr += '<col style="width: 10%;">';
							htmlStr += '<col style="width: 10%;">';
							htmlStr += '<col style="width: 10%;">';
							htmlStr += '<col style="width: 12%;">';
							htmlStr += '</colgroup>';
							htmlStr += '<tbody>';
							htmlStr += '<tr>';
							htmlStr += '<th class="bg-indigo align-center">본부명</th>';
							htmlStr += '<th class="bg-indigo align-center">판매목표</th>';
							htmlStr += '<th class="bg-indigo align-center">판매실적</th>';
							htmlStr += '<th class="bg-indigo align-center">%</th>';
							htmlStr += '<th class="bg-indigo align-center">전월판매실적</th>';
							htmlStr += '<th class="bg-indigo align-center">수금목표</th>';
							htmlStr += '<th class="bg-indigo align-center">수금실적</th>';
							htmlStr += '<th class="bg-indigo align-center">%</th>';
							htmlStr += '<th class="bg-indigo align-center">전월수금실적</th>';
							htmlStr += '</tr>';
							for(var i=0; i<list.length; i++){
								htmlStr += '<tr>';
								htmlStr += '<td class="align-center">'+list[i].deptNm+'</td>';
								htmlStr += '<td class="align-right">'+list[i].pmokAmt+'</td>';
								htmlStr += '<td class="align-right">'+list[i].saleAmt+'</td>';
								htmlStr += '<td class="align-right">'+list[i].pmokRate+'</td>';
								htmlStr += '<td class="align-right">'+list[i].preSaleAmt+'</td>';
								htmlStr += '<td class="align-right">'+list[i].smokAmt+'</td>';
								htmlStr += '<td class="align-right">'+list[i].sukmAmt+'</td>';
								htmlStr += '<td class="align-right">'+list[i].smokRate+'</td>';
								htmlStr += '<td class="align-right">'+list[i].preSukmAmt+'</td>';
								htmlStr += '</tr>';
							}
							htmlStr += '</tbody>';
							htmlStr += '</table>';
							htmlStr += '</div>';
							$("#resultTbody").append(htmlStr);
						}
					} else if (json.resultCode == 1201) {
						alert(json.resultMsg);
						cfLogin();
					} else {
						alert(json.resultMsg);
					}
				}
				, complete : function() {
					cfCloseMagnificPopup();
				}
			});
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

		<!-- contents start -->
		<section class="content">
			<div class="container-fluid">
				<!-- title start -->
				<div class="block-header">
					<h2>판매/수금 목표대비 실적 조회</h2>
				</div>
				<!-- title end -->

				<!-- search start -->
				<div class="row clearfix">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div class="card">
							<div class="header">
								<h2>조회</h2>
							</div>
							<div class="body">
								<form class="form-horizontal">
									<div class="row clearfix">
										<div class="col-lg-2 col-md-2 col-sm-4 col-xs-5 form-control-label">
											<label for="yyyymm">조회 연월</label>
										</div>
										<div class="col-lg-10 col-md-10 col-sm-8 col-xs-7">
											<div class="form-group">
												<select id="yyyymm" class="form-control show-tick" data-show-subtext="true">
											<c:forEach var="result" items="${totalDateList}" varStatus="status">
												<option value="${result.yyyymm }">${fn:substring(result.yyyymm,0,4) }년 ${fn:substring(result.yyyymm,4,6) }월</option>
											</c:forEach>
			                                    </select>
											</div>
										</div>
									</div>
									<div class="row clearfix">
										<div class="col-lg-offset-2 col-md-offset-2 col-sm-offset-4 col-xs-offset-5">
											<button type="button" class="btn bg-indigo m-t-15 waves-effect" onClick="javascript:goSearch();">
												<i class="material-icons">search</i> <span>조회</span>
											</button>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
				<!-- search end -->

				<!-- 판매/수금 CHART -->
	            <div class="row clearfix" id="resultDiv" style="display:none;">
	                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	                    <div class="card">
	                        <div class="header">
	                            <h2 id="chart_title">판매/수금 목표대비 실적</h2>
	                        </div>
	                        <div class="body">
	                            <div id="bar_chart" class="graph"></div>
	                            <div class="body table-responsive" id="resultTbody"></div>
	                        </div>
	                    </div>
	                </div>
	            </div>
	            <!--// 판매/수금 CHART -->

			</div>
		</section>
		<!-- contents end -->
		
		<!--loading Popup start-->
		<%@ include file="/WEB-INF/views/include/loading.jsp"%>
		<!--loading Popup end--> 

	</body>
</html>