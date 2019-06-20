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
	<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Lato:400,300,300italic,400italic,600,600italic,700,700italic,800,800italic">
	<link rel="stylesheet" href="https://fonts.googleapis.com/earlyaccess/nanumgothic.css">
	<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
	<link rel="stylesheet" href="/plugins/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" href="/plugins/node-waves/waves.css">
	<link rel="stylesheet" href="/plugins/animate-css/animate.css">
	<link rel="stylesheet" href="/plugins/sweetalert/sweetalert.css">
    <link rel="stylesheet" href="/plugins/bootstrap-material-datetimepicker/css/bootstrap-material-datetimepicker.css">
    <link rel="stylesheet" href="/plugins/waitme/waitMe.css">
    <link rel="stylesheet" href="/plugins/bootstrap-select/css/bootstrap-select.css">
	<link rel="stylesheet" href="/plugins/morrisjs/morris.css">
	<link rel="stylesheet" href="/css/materialize.css">
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
	<!-- SweetAlert Plugin Js -->
    <script src="/plugins/sweetalert/sweetalert.min.js"></script>
	<!-- Autosize Plugin Js -->
    <script src="/plugins/autosize/autosize.js"></script>
	<!-- Moment Plugin Js -->
    <script src="/plugins/momentjs/moment.js"></script>
    <script src="/plugins/momentjs/moment-ko.js"></script>
    <!-- Bootstrap Material Datetime Picker Plugin Js -->
    <script src="/plugins/bootstrap-material-datetimepicker/js/bootstrap-material-datetimepicker.js"></script>
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
	
		$(function() {
			// 진입 시 메뉴 선택
			cfLNBMenuSelect("lnbHr");
			cfLNBChildMenuSelect("lnbOvertimeMasterList");
			
			// calandar
			$("#standarddate").bootstrapMaterialDatePicker({
				lang : 'ko', weekStart : 0, time: false
			});
			
			// 엔터키 바인드
			$("#deptName").keydown(function (key) {
			    if (key.keyCode == 13) goSearch();
			});
			$("#kname").keydown(function (key) {
			    if (key.keyCode == 13) goSearch();
			});
		});
		
		// 조회
		function goSearch() {
			var yymmdd = $("#standarddate").val().replace(/-/gi,"");
			var deptName = $("#deptName").val();
			var kname = $("#kname").val();
			
			var firstday = getFirstday(yymmdd);
			var lastday = getLastday(yymmdd);
			
			if (yymmdd == "") {
				alert("기준일자를 선택해 주세요.");
				return;
			}
			
			var params = {
				auth : "reyon",
				yymmdd : yymmdd,
				deptName : deptName,
				kname : kname,
			}
			
			$("#resultDiv").hide();
			var request = $.ajax({
				url: '/hr/getOvertimeMasterListAjax.json'
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
						$("#resultTbody").empty();
						$("#resultTitle").text("직원 초과근무 정보 (" + firstday + " ~ " + lastday + ")");
						
						var htmlStr = '';
						htmlStr += '<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 0px;">';
						htmlStr += '<table class="table table-bordered" style="min-width: 830px; margin-bottom: 10px;">';
						htmlStr += '<colgroup>';
						htmlStr += '<col style="width: 10%;">';
						htmlStr += '<col style="width: 20%;">';
						htmlStr += '<col style="width: 15%;">';
						htmlStr += '<col style="width: 15%;">';
						htmlStr += '<col style="width: 15%;">';
						htmlStr += '<col style="width: 15%;">';
						htmlStr += '<col style="width: 10%;">';
						htmlStr += '</colgroup>';
						htmlStr += '<tbody>';
						htmlStr += '<tr>';
						htmlStr += '<th class="bg-indigo align-center">순번</th>';
						htmlStr += '<th class="bg-indigo align-center">소속</th>';
						htmlStr += '<th class="bg-indigo align-center">이름</th>';
						htmlStr += '<th class="bg-indigo align-center">직위</th>';
						htmlStr += '<th class="bg-indigo align-center">초과 근무 시간</th>';
						htmlStr += '<th class="bg-indigo align-center">신청 가능 시간</th>';
						htmlStr += '<th class="bg-indigo align-center">메뉴</th>';
						htmlStr += '</tr>';
						if (list.length == 0) {
							htmlStr += '<tr>';
							htmlStr += '<td class="align-center" colspan="9">조회 내용이 없습니다.</td>';
							htmlStr += '</tr>';
						} else {
							for(var i=0; i<list.length; i++){
								htmlStr += '<tr>';
								htmlStr += '<td class="align-center">'+(i+1)+'</td>';
								htmlStr += '<td class="align-center">'+list[i].deptName+'</td>';
								htmlStr += '<td class="align-center">'+list[i].kname+'</td>';
								htmlStr += '<td class="align-center">'+list[i].posLog+'</td>';
								htmlStr += '<td class="align-center">'+list[i].workingMinute+'분</td>';
								htmlStr += '<td class="align-center">'+list[i].remainMinute+'분</td>';
								htmlStr += '<td class="align-center">';
								htmlStr += '<button type="button" class="btn bg-green waves-effect btn-xs waves-effect" onClick="javascript:historyOvertime(\''+list[i].sabun+'\',\''+list[i].kname+'\',\''+list[i].deptName+'\',\''+list[i].posLog+'\',\''+list[i].workingMinute+'\',\''+list[i].remainMinute+'\');"><i class="material-icons">history</i></button>';
								htmlStr += '</td>';
								htmlStr += '</tr>';
							}
						}
						htmlStr += '</tbody>';
						htmlStr += '</table>';
						htmlStr += '</div>';
						
						$("#resultTbody").append(htmlStr);
						$("#resultDiv").show();
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
		
		// 초과근무 내역
		function historyOvertime(sabun, kname, deptName, posLog, workingMinute, remainMinute){
			var yymmdd = $("#standarddate").val().replace(/-/gi,"");
			
			var firstday = getFirstday(yymmdd);
			var lastday = getLastday(yymmdd);
			
			$("#sabunTxt").text(sabun);
			$("#knameTxt").text(kname);
			$("#deptNameTxt").text(deptName);
			$("#posLogTxt").text(posLog);
			$("#workingMinuteTxt").text(workingMinute+"분");
			$("#remainMinuteTxt").text(remainMinute+"분");
			
			var params = {
				auth : "reyon",
				yymmdd : yymmdd,
				sabun : sabun,
			}
			
			var request = $.ajax({
				url: '/hr/getOvertimeListAjax.json'
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
						$("#overtimeDetailTbody").empty();
						var htmlStr = '';
						if(list.length == 0){
							htmlStr += '<tr><td class="align-center" colspan="6">초과근무신청 내역이 없습니다.</td></tr>';
						} else {
							for(var i=0; i<list.length; i++){
								htmlStr += '<tr>';
								htmlStr += '<td class="align-center">'+(i+1)+'</td>';
								htmlStr += '<td class="align-center">'+list[i].overtimeGbnTxt+'</td>';
								htmlStr += '<td class="align-center">'+list[i].startdate+' ~ '+list[i].enddate+'</td>';
								htmlStr += '<td class="align-center">'+list[i].workingMinute+'분</td>';
								htmlStr += '<td class="align-center">'+list[i].reason+'</td>';
								htmlStr += '<td class="align-center">'+list[i].gwStatusTxt+'</td>';
								htmlStr += '</tr>';
							}
						}
						$("#overtimeDetailTbody").append(htmlStr);
						$("#historyModal").modal("show");
					}else{
						alert(json.resultMsg);
					}
				}
				, complete : function() {
					cfCloseMagnificPopup();
				}
			});
		}
		
		// 금주 월요일 가져오기
		function getFirstday(d) {			
			var curr = new Date(parseInt(d.substring(0,4), 10),parseInt(d.substring(4,6), 10)-1,parseInt(d.substring(6,8), 10));
			curr.setHours(0,0,0,0);
			var first = curr.getDate() - curr.getDay() + 1;
			var firstday = new Date(curr.setDate(first));
			return cfLeadingZeros(firstday.getFullYear(), 4) + "-" + cfLeadingZeros(firstday.getMonth()+1, 2) + "-"  + cfLeadingZeros(firstday.getDate(), 2);
		}
		
		// 차주 일요일 가져오기
		function getLastday(d) {
			var curr = new Date(parseInt(d.substring(0,4), 10),parseInt(d.substring(4,6), 10)-1,parseInt(d.substring(6,8), 10));
			curr.setHours(23,59,59,0);
			var last = curr.getDate() - curr.getDay() + 7;
			var lastday = new Date(curr.setDate(last));
			return cfLeadingZeros(lastday.getFullYear(), 4) + "-" + cfLeadingZeros(lastday.getMonth()+1, 2) + "-"  + cfLeadingZeros(lastday.getDate(), 2);
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
					<h2>초과근무 내역 관리</h2>
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
											<label for="yymm">기준일자 <span style="color:red;">(필수)</span></label>
										</div>
										<div class="col-lg-10 col-md-10 col-sm-8 col-xs-7">
											<div class="form-group">
												<div class="form-line">
													<input type="text" id="standarddate" class="form-control" placeholder="기준일자 (선택일자 기준으로 해당 주의 초과 근무 내역 검색)">
												</div>
											</div>
										</div>
									</div>
									<div class="row clearfix">
										<div class="col-lg-2 col-md-2 col-sm-4 col-xs-5 form-control-label">
											<label for="deptName">소속</label>
										</div>
										<div class="col-lg-10 col-md-10 col-sm-8 col-xs-7">
	                                        <div class="form-group">
	                                            <div class="form-line">
	                                                <input type="text" id="deptName" class="form-control" placeholder="소속 입력 (EQUAL 검색)">
	                                            </div>
	                                        </div>
	                                    </div>
									</div>
									<div class="row clearfix">
										<div class="col-lg-2 col-md-2 col-sm-4 col-xs-5 form-control-label">
											<label for="kname">이름</label>
										</div>
										<div class="col-lg-10 col-md-10 col-sm-8 col-xs-7">
											<div class="form-group">
	                                            <div class="form-line">
	                                                <input type="text" id="kname" class="form-control" placeholder="이름 입력 (LIKE 검색)">
	                                            </div>
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
				<div class="row clearfix" id="resultDiv" style="display:none;">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div class="card">
							<div class="header">
								<h2 id="resultTitle">직원 초과근무 정보</h2>
							</div>
							<div class="body table-responsive" id="resultTbody">
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
	                            <h4 class="modal-title">초과근무 내역 조회</h4>
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
											<col style="width: 40%;">
											<col style="width: 30%;">
											<col style="width: 30%;">
										</colgroup>
										<tbody>
											<tr class="active">
												<th class="text-center" rowspan="2" style="vertical-align: middle;">현재초과근무</th>
												<th class="text-center">초과근무시간</th>
												<th class="text-center">신청가능시간</th>
											</tr>
											<tr>
												<td class="text-center" id="workingMinuteTxt"></td>
												<td class="text-center" id="remainMinuteTxt"></td>
											</tr>
										</tbody>
									</table>
									
                            		<table class="table table-bordered">
										<colgroup>
											<col style="width: 8%;">
											<col style="width: 10%;">
											<col style="width: 24%;">
											<col style="width: 10%;">
											<col style="width: 40%;">
											<col style="width: 8%;">
										</colgroup>
										<thead>
											<tr class="active">
												<th class="bg-indigo align-center">순번</th>
												<th class="bg-indigo align-center">분류</th>
												<th class="bg-indigo align-center">일자/시간</th>
												<th class="bg-indigo align-center">근무시간</th>
												<th class="bg-indigo align-center">사유</th>
												<th class="bg-indigo align-center">상태</th>
											</tr>
										</thead>
										<tbody id="overtimeDetailTbody">
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