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
		<!-- jsgrid -->
		<link rel="stylesheet" href="/css/jsgrid.min.css" />
		<link rel="stylesheet" href="/css/jsgrid-theme.min.css" />
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
		<!-- jsgrid -->
		<script type="text/javascript" src="/js/jsgrid.min.js"></script>
		<!-- common function -->
		<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
		<!-- page script -->
		<script type="text/javascript">
		
		$(function() {
			// 진입 시 메뉴 선택
			cfLNBMenuSelect("menu09");
			
			// datetimepicker
			$('#startDatetime').datetimepicker({
				format: 'YYYY-MM-DD HH:mm',
				sideBySide: true,
			});
			$('#endDatetime').datetimepicker({
				format: 'YYYY-MM-DD HH:mm',
				sideBySide: true,
				useCurrent: false,
			});
			$("#startDatetime").on("dp.change", function (e) {
	            $('#endDatetime').data("DateTimePicker").minDate(e.date);
	        });
	        $("#endDatetime").on("dp.change", function (e) {
	            $('#startDatetime').data("DateTimePicker").maxDate(e.date);
	        });
	        
	        // 값 설정
	        $("#startDate").val("${info.startDate }");
			$("#endDate").val("${info.endDate }");
			$("#reqComment").val("${info.reqComment }");
		});
		
		// 수정
		function goConfirm() {
			var reqSeq = $("#reqSeq").val();
			var username = $("#username").val();
			var runYn = $("#runYn").val();
			var status = $("#status").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var reqComment = $("#reqComment").val();
			
			if (startDate == "" || endDate == "") {
				alert("이용 시간을 입력해 주세요.");
				return;
			}
			if (reqComment == "") {
				alert("사유를 입력해 주세요.");
				return;
			}
			
			if(confirm("등록하시겠습니까?")){
				var params = {
					auth : "reyon",
					reqSeq : reqSeq,
					username : username,
					runYn : runYn,
					status : status,
					startDate : startDate,
					endDate : endDate,
					reqComment : reqComment,
				}
				
				var request = $.ajax({
					url: '/app/pcReleaseModifyAjax.json'
					, type : 'POST'
					, timeout: 60000 // 1분 대기
					, data : params
					, dataType : 'json'
					, beforeSend: function(xmlHttpRequest) {
						cfOpenMagnificPopup();
					}
					, error: function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					}
					, success : function(json) {
						if (json.resultCode == 1){
							alert("저장 완료 되었습니다.");
							var url = "/app/pcReleaseView.do?reqSeq=" + reqSeq;
							var queStr = $("#queStr").val();
							if(queStr != ""){
								url += "&queStr=" + encodeURIComponent(queStr);
							}
							location.href = url;
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
		}
		
		// 뒤로가기
		function goBack(){
			history.back();
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
								<i class="fa fa-pencil-square-o"></i> PC 종료 해제 수정
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-desktop"></i>PC 관리</li>
								<li><i class="fa fa-pencil-square-o"></i>PC 종료 해제 수정</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">PC 종료 해제 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">신청인</label>
											<div class="col-lg-10">
												<p class="form-control-static">
													<sec:authentication property="principal.username" />-<sec:authentication property="principal.kname" /> <span style="color: red;">(* 신청인 사번으로 등록된 회사 PC가 적용됩니다.)</span>
													<input type="hidden" id="username" value="<sec:authentication property="principal.username" />" />
													<input type="hidden" id="runYn" value="N" />
													<input type="hidden" id="status" value="01" />
												</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">이용 시간</label>
											<div class="col-lg-10">
												<div class="row" style="margin-top: 7px;">
													<div class="col-lg-5">
														<div class="input-group date" id="startDatetime">
										                    <input type="text" id="startDate" class="form-control" />
										                    <span class="input-group-addon">
										                        <span class="glyphicon glyphicon-calendar"></span>
										                    </span>
										                </div>
													</div>
													<div class="col-lg-2 text-center">~</div>
													<div class="col-lg-5">
														<div class="input-group date" id="endDatetime">
										                    <input type="text" id="endDate" class="form-control" />
										                    <span class="input-group-addon">
										                        <span class="glyphicon glyphicon-calendar"></span>
										                    </span>
										                </div>
													</div>
												</div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">사유</label>
											<div class="col-lg-10">
												<input type="text" id="reqComment" class="form-control" maxlength="60" placeholder="예) 야근으로 인한 PC 사용 신청" />
											</div>
										</div>
									</form>
								</div>
							</section>
						</div>
					</div>
					<!-- body top end -->
					
					<!-- bottom button start -->
					<div class="btn-row text-right">
						<input type="hidden" id="reqSeq" value="${info.reqSeq }" />
						<input type="hidden" id="queStr" value="${queStr}" />
						<a class="btn btn-default" href="javascript:;" title="수정하기" style="margin-top: 7px;" onClick="javascript:goConfirm();"><span class="icon_check"></span>&nbsp;수정</a>
						<a class="btn btn-default" href="javascript:;" title="이전으로" style="margin-top: 7px;" onClick="javascript:goBack();"><span class="arrow_back"></span>&nbsp;이전</a>
					</div>
					<!-- bottom button end -->
					
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