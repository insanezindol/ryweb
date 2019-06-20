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
			cfLNBMenuSelect("lnbHr");
			cfLNBChildMenuSelect("lnbHolidayList");
			
			// 조회년도 불러오기
			getHolidayYYMM();
		});
		
		// 조회년도 불러오기
		function getHolidayYYMM() {
			var params = {
				auth : "reyon",
				type : "ONE"
			}
			
			var request = $.ajax({
				url: '/hr/getHolidayYYMMAjax.json'
				, type : 'POST'
				, timeout: 60000
				, data : params
				, dataType : 'json'
				, beforeSend: function(xmlHttpRequest) {
					cfOpenMagnificPopup();
				}
				, error: function(xhr, textStatus, errorThrown) {
					alert("시스템 오류가 발생했습니다.");
				}
				, success : function(json) {
					if (json.resultCode == 0){
						$("#yymm").empty();
						var list = json.list;
						var htmlStr = '';
						for(var i=0; i<list.length; i++){
							var yymm = list[i].yymm;
							htmlStr += '<option value="'+yymm+'">'+yymm+'년</option>';
						}
						$("#yymm").append(htmlStr);
						$('#yymm').selectpicker('refresh');
					}else if (json.resultCode == 1201) {
						alert(json.resultMsg);
						cfLogin();
					}else{
						alert(json.resultMsg);
					}
				}
				, complete : function() {
					cfCloseMagnificPopup();
				}
			});
		}
		
		// 조회
		function goSearch() {
			var yymm = $("#yymm").val();
			var sabun = $("#sabun").val();
			
			if (yymm == "") {
				alert("조회년도를 선택해 주세요.");
				return;
			}
			
			$("#resultDiv1").hide();
			$("#resultDiv2").hide();
			var params = {
				auth : "reyon",
				yymm : yymm,
				sabun : sabun,
			}
			
			var request = $.ajax({
				url: '/hr/getHolidayListAjax.json'
				, type : 'POST'
				, timeout: 60000
				, data : params
				, dataType : 'json'
				, beforeSend: function(xmlHttpRequest) {
					cfOpenMagnificPopup();
				}
				, error: function(xhr, textStatus, errorThrown) {
					alert("시스템 오류가 발생했습니다.");
				}
				, success : function(json) {
					if (json.resultCode == 0){
						var info = json.info;
						var list = json.list;
						$("#resultTbody1").empty();
						$("#resultTbody2").empty();
						$("#resultTitle1").text("나의 연차 정보 (" + yymm + "년)");
						$("#resultTitle2").text("나의 연차 사용 내역 (" + yymm + "년)");
						
						var htmlStr1 = '';
						htmlStr1 += '<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 0px;">';
						htmlStr1 += '<table class="table table-bordered" style="min-width: 830px; margin-bottom: 10px;">';
						htmlStr1 += '<colgroup>';
						htmlStr1 += '<col style="width: 14%;">';
						htmlStr1 += '<col style="width: 14%;">';
						htmlStr1 += '<col style="width: 13%;">';
						htmlStr1 += '<col style="width: 13%;">';
						htmlStr1 += '<col style="width: 13%;">';
						htmlStr1 += '<col style="width: 13%;">';
						htmlStr1 += '<col style="width: 13%;">';
						htmlStr1 += '<col style="width: 7%;">';
						htmlStr1 += '</colgroup>';
						htmlStr1 += '<tbody>';
						htmlStr1 += '<tr>';
						htmlStr1 += '<th class="bg-indigo align-center">소속</th>';
						htmlStr1 += '<th class="bg-indigo align-center">이름</th>';
						htmlStr1 += '<th class="bg-indigo align-center">발생연차</th>';
						htmlStr1 += '<th class="bg-indigo align-center">조정연차</th>';
						htmlStr1 += '<th class="bg-indigo align-center">연차합계</th>';
						htmlStr1 += '<th class="bg-indigo align-center">사용연차</th>';
						htmlStr1 += '<th class="bg-indigo align-center">잔여연차</th>';
						htmlStr1 += '<th class="bg-indigo align-center">내역</th>';
						htmlStr1 += '</tr>';
						htmlStr1 += '<tr>';
						htmlStr1 += '<td class="align-center">'+info.deptName+'</td>';
						htmlStr1 += '<td class="align-center">'+info.kname+'</td>';
						htmlStr1 += '<td class="align-center">'+info.dayPlus+'일</td>';
						htmlStr1 += '<td class="align-center">'+info.dayMinus+'일</td>';
						htmlStr1 += '<td class="align-center">'+info.dayTotal+'일</td>';
						htmlStr1 += '<td class="align-center">'+info.useDay+'일</td>';
						htmlStr1 += '<td class="align-center">'+info.remainDay+'일</td>';
						htmlStr1 += '<td class="align-center">';
						htmlStr1 += '<button type="button" class="btn bg-green waves-effect btn-xs waves-effect" onClick="javascript:historyHoliday(\''+info.sabun+'\',\''+info.kname+'\',\''+info.deptName+'\',\''+info.posLog+'\');"><i class="material-icons">history</i></button>';
						htmlStr1 += '</td>';
						htmlStr1 += '</tr>';
						htmlStr1 += '</tbody>';
						htmlStr1 += '</table>';
						htmlStr1 += '</div>';
						
						var htmlStr2 = '';
						htmlStr2 += '<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 0px;">';
						htmlStr2 += '<table class="table table-bordered" style="min-width: 830px; margin-bottom: 10px;">';
						htmlStr2 += '<colgroup>';
						htmlStr2 += '<col style="width: 10%;">';
						htmlStr2 += '<col style="width: 10%;">';
						htmlStr2 += '<col style="width: 18%;">';
						htmlStr2 += '<col style="width: 8%;">';
						htmlStr2 += '<col style="width: 8%;">';
						htmlStr2 += '<col style="width: 24%;">';
						htmlStr2 += '<col style="width: 12%;">';
						htmlStr2 += '<col style="width: 10%;">';
						htmlStr2 += '</colgroup>';
						htmlStr2 += '<tbody>';
						htmlStr2 += '<tr>';
						htmlStr2 += '<th class="bg-indigo align-center">순번</th>';
						htmlStr2 += '<th class="bg-indigo align-center">분류</th>';
						htmlStr2 += '<th class="bg-indigo align-center">일자/기간</th>';
						htmlStr2 += '<th class="bg-indigo align-center">휴가일수</th>';
						htmlStr2 += '<th class="bg-indigo align-center">차감여부</th>';
						htmlStr2 += '<th class="bg-indigo align-center">사유</th>';
						htmlStr2 += '<th class="bg-indigo align-center">업무인계자</th>';
						htmlStr2 += '<th class="bg-indigo align-center">상태</th>';
						htmlStr2 += '</tr>';
						if(list.length == 0){
							htmlStr2 += '<tr><td class="align-center" colspan="8">연차 사용 내역이 없습니다.</td></tr>';
						} else {
							for(var i=0; i<list.length; i++){
								htmlStr2 += '<tr>';
								htmlStr2 += '<td class="align-center">'+(i+1)+'</td>';
								htmlStr2 += '<td class="align-center">'+list[i].holidayGbnTxt+'</td>';
								htmlStr2 += '<td class="align-center">'+list[i].startdate+' ~ '+list[i].enddate+'</td>';
								htmlStr2 += '<td class="align-center">'+list[i].minusCnt+'</td>';
								htmlStr2 += '<td class="align-center">'+list[i].minusYn+'</td>';
								htmlStr2 += '<td class="align-center">'+list[i].reason+'</td>';
								htmlStr2 += '<td class="align-center">'+list[i].takeover+'</td>';
								htmlStr2 += '<td class="align-center">'+list[i].gwStatusTxt+'</td>';
								htmlStr2 += '</tr>';
							}
						}
						
						htmlStr2 += '</tbody>';
						htmlStr2 += '</table>';
						htmlStr2 += '</div>';
						
						$("#resultTbody1").append(htmlStr1);
						$("#resultDiv1").show();
						
						$("#resultTbody2").append(htmlStr2);
						$("#resultDiv2").show();
					}else if (json.resultCode == 1201) {
						alert(json.resultMsg);
						cfLogin();
					}else{
						alert(json.resultMsg);
					}
				}
				, complete : function() {
					cfCloseMagnificPopup();
				}
			});
		}
		
		// 휴가 증감 내역
		function historyHoliday(sabun, kname, deptName, posLog){
			var yymm = $("#yymm").val();
			
			$("#sabunTxt").text(sabun);
			$("#knameTxt").text(kname);
			$("#deptNameTxt").text(deptName);
			$("#posLogTxt").text(posLog);
			
			var params = {
				auth : "reyon",
				yymm : yymm,
				sabun : sabun,
			}
			
			var request = $.ajax({
				url: '/hr/getHolidayMasterDetailListAjax.json'
				, type : 'POST'
				, timeout: 60000
				, data : params
				, dataType : 'json'
				, beforeSend: function(xmlHttpRequest) {
					cfOpenMagnificPopup();
				}
				, error: function(xhr, textStatus, errorThrown) {
					alert("시스템 오류가 발생했습니다.");
				}
				, success : function(json) {
					if (json.resultCode == 0){
						var list = json.list;
						$("#holidayDetailTbody").empty();
						var htmlStr = '';
						if (list.length == 0) {
							htmlStr += '<tr>';
							htmlStr += '<td class="align-center" colspan="5">조회 내용이 없습니다.</td>';
							htmlStr += '</tr>';
						} else {
							for (var i=0; i<list.length; i++) {
								htmlStr += '<tr>';
								htmlStr += '<td class="align-center">'+list[i].addDayTypeTxt+'</td>';
								htmlStr += '<td class="align-center">'+list[i].addDay+'</td>';
								htmlStr += '<td class="align-center">'+list[i].addDayComment+'</td>';
								htmlStr += '<td class="align-center">'+list[i].regKname+'</td>';
								htmlStr += '<td class="align-center">'+list[i].regDate+'</td>';
								htmlStr += '</tr>';
							}
						}
						$("#holidayDetailTbody").append(htmlStr);
						$("#historyModal").modal("show");
					}else if (json.resultCode == 1201) {
						alert(json.resultMsg);
						cfLogin();
					}else{
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
				<input type="hidden" id="sabun" value="<sec:authentication property="principal.username" />" />
				
				<!-- title start -->
				<div class="block-header">
					<h2>연차 조회</h2>
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
											<label for="yymm">조회년도</label>
										</div>
										<div class="col-lg-10 col-md-10 col-sm-8 col-xs-7">
											<div class="form-group">
												<select id="yymm" class="form-control show-tick" data-show-subtext="true">
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

				<!-- result start -->
				<div class="row clearfix" id="resultDiv1" style="display:none;">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div class="card">
							<div class="header">
								<h2 id="resultTitle1">나의 연차 정보</h2>
							</div>
							<div class="body table-responsive" id="resultTbody1">
							</div>
						</div>
					</div>
				</div>
				<!-- result end -->
				
				<!-- result start -->
				<div class="row clearfix" id="resultDiv2" style="display:none;">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div class="card">
							<div class="header">
								<h2 id="resultTitle2">나의 연차 사용 내역</h2>
							</div>
							<div class="body table-responsive" id="resultTbody2">
							</div>
						</div>
					</div>
				</div>
				<!-- result end -->

				<!-- history modal start -->
				<div class="modal fade" id="historyModal" tabindex="-1" role="dialog">
	                <div class="modal-dialog modal-lg" role="document">
	                    <div class="modal-content">
	                        <div class="modal-header">
	                            <h4 class="modal-title" id="largeModalLabel">연차 내역 조회</h4>
	                        </div>
	                        <div class="modal-body">
	                        	<div class="body table-responsive">
                            		<table class="table table-bordered">
										<colgroup>
											<col style="width: 25%;">
											<col style="width: 25%;">
											<col style="width: 25%;">
											<col style="width: 25%;">
										</colgroup>
										<thead>
											<tr class="active">
												<th class="text-center">사번</th>
												<th class="text-center">부서명</th>
												<th class="text-center">직위</th>
												<th class="text-center">성명</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td class="text-center" id="sabunTxt"></td>
												<td class="text-center" id="deptNameTxt"></td>
												<td class="text-center" id="posLogTxt"></td>
												<td class="text-center" id="knameTxt"></td>
											</tr>
										</tbody>
									</table>
									
                            		<table class="table table-bordered">
										<colgroup>
											<col style="width: 15%;">
											<col style="width: 10%;">
											<col style="width: 40%;">
											<col style="width: 15%;">
											<col style="width: 20%;">
										</colgroup>
										<thead>
											<tr class="active">
												<th class="text-center">증가/차감</th>
												<th class="text-center">일수</th>
												<th class="text-center">사유</th>
												<th class="text-center">처리자</th>
												<th class="text-center">처리시간</th>
											</tr>
										</thead>
										<tbody id="holidayDetailTbody">
										</tbody>
									</table>
								</div>
	                        </div>
	                        <div class="modal-footer">
	                            <button type="button" class="btn btn-link waves-effect" data-dismiss="modal">확인</button>
	                        </div>
	                    </div>
	                </div>
	            </div>
	            <!-- history modal end -->

			</div>
		</section>
		<!-- contents end -->
		
		<!--loading Popup start-->
		<%@ include file="/WEB-INF/views/include/loading.jsp"%>
		<!--loading Popup end--> 

	</body>
</html>