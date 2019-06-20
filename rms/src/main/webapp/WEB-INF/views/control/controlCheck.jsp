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
		<script type="text/javascript" src="/js/scripts.js?ver=20190619"></script> <!-- Update into server -->
		<script type="text/javascript" src="/js/jquery.autosize.min.js"></script>
		<script type="text/javascript" src="/js/jquery.placeholder.min.js"></script>
		<script type="text/javascript" src="/js/jquery.slimscroll.min.js"></script>
		<!-- common function -->
		<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
		<!-- page script -->
		<script type="text/javascript">
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu17");
			});
			
			function downloadFile(seq, filename) {
				location.href = "/control/controlFileDownload.do?dwAuth=reyon&dwSeq="
						+ seq + "&dwFilename=" + encodeURIComponent(filename);
			}
			
			// Check
			function goCheck() {
				var ryno = $('#ryno').val();
				var reqno = $("#reqno").val();
				var soldesc = $('#soldesc').val();
				var file1 = $('#file1')[0].files[0];
				var file2 = $('#file2')[0].files[0];
				
				if(confirm("처리 하시겠습니까?")) {
					if ( soldesc == "" || soldesc == null ) {
						alert('확인 내용을 간단히 적어주세요.');
						return;
					}
					if ( file2 == null ) {
						if ( ryno.length != 7 ) {
							console.log(reqno.length);
							alert('변경된 도안을 첨부해 주세요.');
							return;
						}
					}
					
					var formData = new FormData();
	
					formData.append("auth", "reyon");
					formData.append("reqno", reqno);
					formData.append("soldesc", soldesc);
					formData.append("file1", file1);
					formData.append("file2", file2);
						
					var request = $.ajax({
						url: '/control/controlCheckModifyAjax.json'
						, type : 'POST'
						, timeout: 30000
						, data : formData
						,contentType: false
						,processData: false
						, dataType : 'json'
						, beforeSend: function(xmlHttpRequest) {
							cfOpenMagnificPopup();
						}
						, error: function(xhr, textStatus, errorThrown) {
							alert("시스템 오류가 발생했습니다.");
						}
						, success : function(json) {
							if (json.resultCode == 1){
								alert("완료 되었습니다.");
								var url = "/control/controlList.do";
								var queStr = $("#queStr").val();
								if(queStr != ""){
									url += "?" + queStr;
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
				var url = "/control/controlList.do";
				var queStr = $("#queStr").val();
				if(queStr != ""){
					url += "?" + queStr;
				}
				location.href = url;
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
								<i class="fa fa-list"></i> 재고자산 통제 상세
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-bell-o"></i>재고자산 통제 관리</li>
								<li><i class="fa fa-file-text-o"></i>재고자산 통제 상세</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">재고자산 통제 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">품명</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.jpmnm }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">통제 내용</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.condesc }</p>
											</div>
										</div>										
										<div class="form-group">
											<label class="col-lg-2 control-label">요청서</label>
											<div class="col-lg-10">
												<p class="form-control-static"><a href="javascript:downloadFile('${file.reqno }','${file.reqfilename }');">${file.reqfilename}</a></p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">등록자</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.kname }</p>
											</div>
										</div>	
										<div class="form-group">
											<label class="col-lg-2 control-label">등록일시</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.insdate }</p>
											</div>
										</div>
									</form>
								</div>
							</section>
						</div>
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">재고자산 통제 검증</header>
								<div class="panel-body">
									<form class="form-horizontal" enctype="multipart/form-data">
										<div class="form-group">
											<label class="col-lg-2 control-label">확인 내용</label>
											<div class="col-lg-10">
												<input type="text" id="soldesc" class="form-control" maxlength="150">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">기존 도안</label>
											<div class="col-lg-10">
												<p class="form-control-static"><input type="file" name="multiFile[]" id="file1"></p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">변경된 도안</label>
											<div class="col-lg-10">
												<p class="form-control-static"><input type="file" name="multiFile[]" id="file2"></p>
											</div>
										</div>
										<div class="form-group">	
											<label class="col-lg-2 control-label">확인자</label>
											<div class="col-lg-10">
												<p class="form-control-static"><sec:authentication property="principal.kname" /></p>
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
						<input type="hidden" id="reqno" value="${info.reqno }">
						<input type="hidden" id="ryno" value="${info.ryno }">
						<input type="hidden" id="queStr" value="${queStr}" />
						<a class="btn btn-default" href="javascript:;" title="확인하기" style="margin-top: 7px;" onClick="javascript:goCheck();"><span class="glyphicon glyphicon-ok"></span>&nbsp;확인</a>	
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