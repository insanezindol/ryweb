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
		<!-- vworld map api -->
		<script type="text/javascript" src="http://map.vworld.kr/js/vworldMapInit.js.do?apiKey=CCF38AFF-389D-35DB-A32A-1683474BEF12"></script>
		<!-- page script -->
		<script type="text/javascript">
		
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu07");
				
				// VWORLD MAP API 호출
				loadVwordMap();
				
				// 미리보기 파일 로드
				if(${isPdf}){
					PDFObject.embed("/temp/${sampleFileName }", "#sampleView", {height: "600px"});
				}
			});
			
			// VWORLD MAP API 호출
			function loadVwordMap() {
				var apiMap;
				var marker;
				var lon = "${info.positionX }";
				var lat = "${info.positionY }";
				var message = "도로명 주소 : ${info.roadAddr } ${info.detailAddr }<br>지번 주소 : ${info.jibunAddr } ${info.detailAddr }";
				
				vworld.showMode = false; 
				vworld.init("vmap", "map-first", function() {
					apiMap = this.vmap;
					apiMap.setBaseLayer(apiMap.vworldBaseMap);
					apiMap.setControlsType({"simpleMap":true});
					apiMap.addVWORLDControl("zoomBar");
					marker = new vworld.Marker(lon, lat, message, "");
				    marker.setZindex(3);
				    apiMap.addMarker(marker);
				    apiMap.setCenterAndZoom(lon, lat, 16);
				});
			}

			// 계약 수정 페이지로 이동
			function modifyContents(seq) {
				var url = "/realestate/realestateModify.do?contractSeq=" + seq;
				var queStr = $("#queStr").val();
				if (queStr != "") {
					url += "&queStr=" + encodeURIComponent(queStr);
				}
				location.href = url;
			}

			// 계약 종료 처리
			function deleteContents(seq) {
				if (confirm("계약 종료 처리 하시겠습니까?")) {
					var params = {
						auth : "reyon",
						contractSeq : seq,
						status : "END"
					}

					var request = $.ajax({
						url : '/realestate/realestateDeleteAjax.json',
						type : 'POST',
						timeout : 30000,
						data : params,
						dataType : 'json',
						beforeSend : function(xmlHttpRequest) {
							cfOpenMagnificPopup();
						},
						error : function(xhr, textStatus, errorThrown) {
							alert("시스템 오류가 발생했습니다.");
						},
						success : function(json) {
							if (json.resultCode == 1) {
								alert("계약 종료 처리 되었습니다.");
								var url = "/realestate/realestateList.do";
								var queStr = $("#queStr").val();
								if (queStr != "") {
									url += "?" + queStr;
								}
								location.href = url;
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
			}

			// 첨부파일 다운로드
			function downloadFile(seq, filename) {
				location.href = "/realestate/realestateFileDownload.do?dwAuth=reyon&dwSeq="
						+ seq + "&dwFilename=" + encodeURIComponent(filename);
			}

			// 뒤로가기
			function goBack() {
				var url = "/realestate/realestateList.do";
				var queStr = $("#queStr").val();
				if (queStr != "") {
					url += "?" + queStr;
				}
				location.href = url;
			}
		</script>
		<style> body { font-family: 'Noto Sans KR', sans-serif !important;} </style>
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
								<i class="fa fa-file-text-o"></i> 부동산 계약 상세
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-building-o"></i>부동산 계약 관리</li>
								<li><i class="fa fa-file-text-o"></i>부동산 계약 상세</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">부동산 계약 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">계약구분</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.saupGubun }</p>
											</div>
										</div>
										
										<div class="form-group">
											<label class="col-lg-2 control-label">용도 / 사용자</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.division } / ${info.username }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">계약기간</label>
											<div class="col-lg-10">
												<p class="form-control-static">
													<c:choose>
														<c:when test="${info.payment == '보유' }">${info.startDate }</c:when>
														<c:otherwise>${info.startDate } ~ ${info.endDate }</c:otherwise>
													</c:choose>
												</p>
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
											<label class="col-lg-2 control-label">소재지(도로명)</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.roadAddr } ${info.detailAddr }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">소재지(지번)</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.jibunAddr } ${info.detailAddr }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">지도</label>
											<div class="col-lg-10">
												<div id="vmap" style="width: 100%; height: 500px; left: 0px; top: 0px"></div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">지불방법</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.payment }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">보증금</label>
											<div class="col-lg-10">
												<p class="form-control-static"><fmt:formatNumber value="${info.deposit }" pattern="#,###" /></p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">임대료</label>
											<div class="col-lg-10">
												<p class="form-control-static"><fmt:formatNumber value="${info.rent }" pattern="#,###" /></p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">관리비</label>
											<div class="col-lg-10">
												<p class="form-control-static"><fmt:formatNumber value="${info.administrativeExpenses }" pattern="#,###" /></p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">합계 (임대료+관리비)</label>
											<div class="col-lg-10">
												<p class="form-control-static"><fmt:formatNumber value="${info.rent + info.administrativeExpenses }" pattern="#,###" /></p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">비고</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.remarks }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">첨부파일</label>
											<div class="col-lg-10">
												<p class="form-control-static">
													<c:choose>
														<c:when test="${info.attachFilename == '' || info.attachFilename == null }">첨부파일이 없습니다.</c:when>
														<c:otherwise><a href="javascript:downloadFile('${info.contractSeq }','${info.attachFilename }');">${info.attachFilename }</a></c:otherwise>
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
						<a class="btn btn-default" href="javascript:;" title="수정하기" style="margin-top: 7px;" onClick="javascript:modifyContents('${info.contractSeq }');"><span class="icon_pencil-edit"></span>&nbsp;수정</a>
						<a class="btn btn-default" href="javascript:;" title="임대종료하기" style="margin-top: 7px;" onClick="javascript:deleteContents('${info.contractSeq }');"><span class="icon_trash"></span>&nbsp;임대종료</a>
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