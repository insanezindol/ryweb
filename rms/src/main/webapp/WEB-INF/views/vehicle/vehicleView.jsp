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
		<script type="text/javascript" src="/js/pdfobject.js"></script>
		<!-- page script -->
		<script type="text/javascript">
		
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu08");
				// 미리보기 파일 로드
				if(${isPdf}){
					PDFObject.embed("/temp/${sampleFileName }", "#sampleView", {height: "600px"});
				}
			});
			
			// 계약 수정 페이지로 이동
			function modifyContents(seq){
				var url = "/vehicle/vehicleModify.do?vehicleSeq=" + seq;
				var queStr = $("#queStr").val();
				if(queStr != ""){
					url += "&queStr="+encodeURIComponent(queStr);
				}
				location.href = url;
			}
			
			// 계약 종료 처리
			function deleteContents(seq){
				if(confirm("계약 종료 처리 하시겠습니까?")){
					var params = {
						auth : "reyon",
						vehicleSeq : seq,
						status : "END"
					}
					
					var request = $.ajax({
						url: '/vehicle/vehicleDeleteAjax.json'
						, type : 'POST'
						, timeout: 30000
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
								alert("계약 종료 처리 되었습니다.");
								var url = "/vehicle/vehicleList.do";
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
			
			// 첨부파일 다운로드
			function downloadFile(seq, filename){
				location.href="/vehicle/vehicleFileDownload.do?dwAuth=reyon&dwSeq="+seq+"&dwFilename="+encodeURIComponent(filename);
			}
			
			// 뒤로가기
			function goBack(){
				var url = "/vehicle/vehicleList.do";
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
								<i class="fa fa-file-text-o"></i> 법인 차량 상세
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-car"></i>법인 차량 관리</li>
								<li><i class="fa fa-file-text-o"></i>법인 차량 상세</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">법인 차량 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">차량번호</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.vehicleNo }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">차량종류</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.vehicleType }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">분류 / 사용자</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.division } / ${info.username }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">지급구분</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.payment }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">렌트기간/매입일</label>
											<div class="col-lg-10">
												<p class="form-control-static">
													<c:choose>
														<c:when test="${info.payment == '보유' }">${info.rentStartDate }</c:when>
														<c:otherwise>${info.rentStartDate } ~ ${info.rentEndDate }</c:otherwise>
													</c:choose>
												</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">임차료/월 (보험료 포함)</label>
											<div class="col-lg-10">
												<p class="form-control-static"><fmt:formatNumber value="${info.rentMoney }" pattern="#,###" /></p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">보험기간</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.insuranceStartDate } ~ ${info.insuranceEndDate }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">보험료</label>
											<div class="col-lg-10">
												<p class="form-control-static"><fmt:formatNumber value="${info.insuranceMoney }" pattern="#,###" /></p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">상태</label>
											<div class="col-lg-10">
												<p class="form-control-static">
													<c:choose>
														<c:when test="${info.status == 'ING' }">
															<span class="label label-success">진행중</span>
														</c:when>
														<c:when test="${info.status == 'END' }">
															<span class="label label-danger">종료</span>
														</c:when>
													</c:choose>
												</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">비고</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.remarks }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">스캔파일 첨부파일</label>
											<div class="col-lg-10">
												<p class="form-control-static">
													<c:choose>
														<c:when test="${info.attachFilename == '' || info.attachFilename == null }">첨부파일이 없습니다.</c:when>
														<c:otherwise><a href="javascript:downloadFile('${info.vehicleSeq }','${info.attachFilename }');">${info.attachFilename }</a></c:otherwise>
													</c:choose>
												</p>
											</div>
										</div>
									<c:if test="${isPdf }">
										<div class="form-group">
											<label class="col-lg-2 control-label">미리보기</label>
											<div class="col-lg-10">
												<div id="sampleView"></div>
											</div>
										</div>
									</c:if>
										<div class="form-group">
											<label class="col-lg-2 control-label">등록인/등록시간</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.regName } / ${info.regDate }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">수정인/수정시간</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.updName } / ${info.updDate }</p>
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
						<input type="hidden" id="queStr" value="${queStr}" />
						<a class="btn btn-default" href="javascript:;" title="수정하기" style="margin-top: 7px;" onClick="javascript:modifyContents('${info.vehicleSeq }');"><span class="icon_pencil-edit"></span>&nbsp;수정</a>
						<a class="btn btn-default" href="javascript:;" title="계약종료하기" style="margin-top: 7px;" onClick="javascript:deleteContents('${info.vehicleSeq }');"><span class="icon_trash"></span>&nbsp;계약종료</a>
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