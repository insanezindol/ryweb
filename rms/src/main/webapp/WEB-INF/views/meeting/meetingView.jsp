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
				cfLNBMenuSelect("menu03");
			});
			
			// 회의 수정
			function modifyMeeting(){
				var seq = $("#seq").val();
				var url = "/meeting/meetingModify.do?meetingSeq=" + seq;
				var queStr = $("#queStr").val();
				if(queStr != ""){
					url += "&queStr="+encodeURIComponent(queStr);
				}
				location.href = url;
			}

			// 회의 삭제
			function deleteMeeting(){
				var seq = $("#seq").val();
				
				if(confirm("삭제 하시겠습니까?")){
					var params = {
						auth : "reyon",
						seq : seq
					}
					
					var request = $.ajax({
						url: '/meeting/meetingDeleteAjax.json'
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
								alert("삭제 완료 되었습니다.");
								var url = "/meeting/meetingList.do";
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
				var url = "/meeting/meetingList.do";
				var queStr = $("#queStr").val();
				if(queStr != ""){
					url += "?" + queStr;
				}
				location.href = url;
			}
			
			// 첨부파일 다운로드
			function downloadFile(seq, filename){
				location.href="/meeting/meetingFileDownload.do?dwAuth=reyon&dwSeq="+seq+"&dwFilename="+encodeURIComponent(filename);
			}

			// 결과 등록
			function addResult(){
				var seq = $("#seq").val();
				var url = "/meeting/meetingResultAdd.do?meetingSeq=" + seq;
				var queStr = $("#queStr").val();
				if(queStr != ""){
					url += "&queStr="+encodeURIComponent(queStr);
				}
				location.href = url;
			}

			// 결과 수정
			function modifyResult(){
				var seq = $("#seq").val();
				var url = "/meeting/meetingResultModify.do?meetingSeq=" + seq;
				var queStr = $("#queStr").val();
				if(queStr != ""){
					url += "&queStr="+encodeURIComponent(queStr);
				}
				location.href = url;
			}

			// 결과 삭제
			function deleteResult(){
				var seq = $("#seq").val();
				
				if(confirm("삭제 하시겠습니까?")){
					var params = {
						auth : "reyon",
						seq : seq
					}
					
					var request = $.ajax({
						url: '/meeting/meetingResultDeleteAjax.json'
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
								alert("삭제 완료 되었습니다.");
								location.reload();
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
			
			// 상신, 반려, 결재하기
			function confirmResult(meetingStatus){
				var seq = $("#seq").val();
				var comment = "";
				var alertText = "";
				if (meetingStatus == "04") {
					comment = $("#sactionComment").val();
					alertText = "상신";
				} else if (meetingStatus == "99") {
					comment = $("#confirmComment").val();
					alertText = "반려";
				} else if (meetingStatus == "AA") {
					comment = $("#confirmComment").val();
					alertText = "결재";
				}
				
				if(confirm(alertText + " 하시겠습니까?")){
					var params = {
						auth : "reyon",
						seq : seq,
						comment : comment,
						meetingStatus : meetingStatus,
					}
					
					var request = $.ajax({
						url: '/meeting/meetingResultConfirmAjax.json'
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
								alert(alertText + " 완료 되었습니다.");
								location.reload();
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
			
			// 엑셀 다운로드
			function downloadExcel() {
				var form = document.createElement("form");
				form.name = "downForm";
				form.id = "downForm";
				form.method = "POST";
				form.action = "/meeting/meetingExcelDownload.do";
				var seq = document.createElement("input");
				seq.type = "hidden";
				seq.name = "seq";
				seq.value = $("#seq").val();
				$(form).append(seq);
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
								<i class="fa fa-file-text-o"></i> 회의 상세
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-calendar"></i>회의 관리</li>
								<li><i class="fa fa-file-text-o"></i>회의 상세</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">회의 상세 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">프로젝트</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.projectName }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">제목</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.meetingName }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">일시</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.meetingStartDate } ~ ${info.meetingEndDate }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">장소</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.codeName }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">담당자</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.deptName } ${info.updName }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">참석자</label>
											<div class="col-lg-10">
												<p class="form-control-static">
													<c:choose>
														<c:when test="${fn:length(attList1) == 0}">등록된 참석자가 없습니다.</c:when>
														<c:otherwise>
															<c:forEach var="result" items="${attList1}" varStatus="status">
																${result.attendantDeptName} ${result.attendantName }<c:if test="${!status.last}">,</c:if>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">참고인</label>
											<div class="col-lg-10">
												<p class="form-control-static">
													<c:choose>
														<c:when test="${fn:length(attList2) == 0}">등록된 참고인이 없습니다.</c:when>
														<c:otherwise>
															<c:forEach var="result" items="${attList2}" varStatus="status">
																${result.attendantDeptName} ${result.attendantName }<c:if test="${!status.last}">,</c:if>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">등록인 / 등록시간</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.regName } / ${info.meetingRegDate }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">수정인 / 수정시간</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.updName } / ${info.meetingUpdDate }</p>
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
						<input type="hidden" id="seq" name="seq" value="${seq }">
						<input type="hidden" id="queStr" value="${queStr}" />
						<c:if test="${isMine && (info.meetingStatus == '01' || info.meetingStatus == '02' || info.meetingStatus == '05') }">
							<a class="btn btn-default" href="javascript:;" title="수정하기" style="margin-top: 7px;" onClick="javascript:modifyMeeting();"><span class="icon_pencil-edit"></span>&nbsp;수정</a>
							<a class="btn btn-default" href="javascript:;" title="삭제하기" style="margin-top: 7px;" onClick="javascript:deleteMeeting();"><span class="icon_trash"></span>&nbsp;삭제</a>
						</c:if>
						<c:if test="${info.meetingStatus == 'AA' }">
							<a class="btn btn-default" href="javascript:;" title="회의록 다운로드 하기" style="margin-top: 7px;" onClick="javascript:downloadExcel();"><span class="icon_download"></span>&nbsp;회의록 다운로드</a>
						</c:if>
						<a class="btn btn-default" href="javascript:;" title="이전으로" style="margin-top: 7px;" onClick="javascript:goBack();"><span class="arrow_back"></span>&nbsp;이전</a>
					</div>
					<!-- bottom button end -->
					
					<c:if test="${info.meetingStatus != '01' && info.meetingStatus != '05' && info.meetingStatus != 'BB'}">
						<!-- body top start -->
						<div class="row">
							<div class="col-lg-12">
								<section class="panel" style="margin-bottom: 5px;">
									<header class="panel-heading">회의 결과 정보</header>
									<div class="panel-body">
										<form class="form-horizontal">
											<div class="form-group">
												<label class="col-lg-2 control-label">회의 내용</label>
												<div class="col-lg-10">
													<p class="form-control-static"><c:choose><c:when test="${info.meetingContents == '' || info.meetingContents == null }">미등록</c:when><c:otherwise>${info.meetingContents }</c:otherwise></c:choose></p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-2 control-label">결정사항</label>
												<div class="col-lg-10">
													<p class="form-control-static"><c:choose><c:when test="${info.decisionContents == '' || info.decisionContents == null }">미등록</c:when><c:otherwise>${info.decisionContents }</c:otherwise></c:choose></p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-2 control-label">향후일정</label>
												<div class="col-lg-10">
													<p class="form-control-static"><c:choose><c:when test="${info.planContents == '' || info.planContents == null }">미등록</c:when><c:otherwise>${info.planContents }</c:otherwise></c:choose></p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-2 control-label">특이사항</label>
												<div class="col-lg-10">
													<p class="form-control-static"><c:choose><c:when test="${info.issueContents == '' || info.issueContents == null }">미등록</c:when><c:otherwise>${info.issueContents }</c:otherwise></c:choose></p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-2 control-label">첨부파일1</label>
												<div class="col-lg-10">
													<p class="form-control-static">
														<c:choose><c:when test="${info.attachFilename1 == '' || info.attachFilename1 == null }">미등록</c:when><c:otherwise><a href="javascript:downloadFile('${info.meetingSeq }','${info.attachFilename1 }');">${info.attachFilename1 }</a></c:otherwise></c:choose>
													</p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-2 control-label">첨부파일2</label>
												<div class="col-lg-10">
													<p class="form-control-static">
														<c:choose><c:when test="${info.attachFilename2 == '' || info.attachFilename2 == null }">미등록</c:when><c:otherwise><a href="javascript:downloadFile('${info.meetingSeq }','${info.attachFilename2 }');">${info.attachFilename2 }</a></c:otherwise></c:choose>
													</p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-2 control-label">첨부파일3</label>
												<div class="col-lg-10">
													<p class="form-control-static">
														<c:choose><c:when test="${info.attachFilename3 == '' || info.attachFilename3 == null }">미등록</c:when><c:otherwise><a href="javascript:downloadFile('${info.meetingSeq }','${info.attachFilename3 }');">${info.attachFilename3 }</a></c:otherwise></c:choose>
													</p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-2 control-label">첨부파일4</label>
												<div class="col-lg-10">
													<p class="form-control-static">
														<c:choose><c:when test="${info.attachFilename4 == '' || info.attachFilename4 == null }">미등록</c:when><c:otherwise><a href="javascript:downloadFile('${info.meetingSeq }','${info.attachFilename4 }');">${info.attachFilename4 }</a></c:otherwise></c:choose>
													</p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-2 control-label">첨부파일5</label>
												<div class="col-lg-10">
													<p class="form-control-static">
														<c:choose><c:when test="${info.attachFilename5 == '' || info.attachFilename5 == null }">미등록</c:when><c:otherwise><a href="javascript:downloadFile('${info.meetingSeq }','${info.attachFilename5 }');">${info.attachFilename5 }</a></c:otherwise></c:choose>
													</p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-2 control-label">등록일</label>
												<div class="col-lg-10">
													<p class="form-control-static"><c:choose><c:when test="${info.meetingResultRegDate == '' || info.meetingResultRegDate == null }">미등록</c:when><c:otherwise>${info.meetingResultRegDate }</c:otherwise></c:choose></p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-2 control-label">수정일</label>
												<div class="col-lg-10">
													<p class="form-control-static"><c:choose><c:when test="${info.meetingResultUpdDate == '' || info.meetingResultUpdDate == null }">미등록</c:when><c:otherwise>${info.meetingResultUpdDate }</c:otherwise></c:choose></p>
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
							<c:if test="${isMine }">
								<c:if test="${info.meetingStatus == '02' }">
									<a class="btn btn-default" href="javascript:;" title="결과 등록하기" style="margin-top: 7px;" onClick="javascript:addResult();"><span class="icon_pencil"></span>&nbsp;결과 등록</a>
								</c:if>
								<c:if test="${info.meetingStatus == '03' || info.meetingStatus == '99' }">
									<a class="btn btn-default" href="javascript:;" title="결과 수정하기" style="margin-top: 7px;" onClick="javascript:modifyResult();"><span class="icon_pencil-edit"></span>&nbsp;결과 수정</a>
								</c:if>
								<c:if test="${info.meetingStatus == '03' }">
									
									<a class="btn btn-default" href="javascript:;" title="결과 삭제하기" style="margin-top: 7px;" onClick="javascript:deleteResult();"><span class="icon_trash"></span>&nbsp;결과 삭제</a>
								</c:if>
							</c:if>
						</div>
						<!-- bottom button end -->
					</c:if>
					
					<!-- approval start -->
					<c:if test="${info.meetingStatus == '03' || info.meetingStatus == '04' || info.meetingStatus == '99' || info.meetingStatus == 'AA'}">
						<div class="row">
							<div class="col-lg-12">
								<section class="panel" style="margin-bottom: 5px;">
									<header class="panel-heading">결재</header>
									<div class="panel-body">
										<table class="table table-bordered personal-task">
											<tbody>
												<tr>
													<th style="width: 20%;">구분</th>
													<th>담당자</th>
													<th>부서장</th>
												</tr>
												<tr>
													<th>성명</th>
													<td>
														<c:choose>
															<c:when test="${info.sactionName != '' && info.sactionName != null && (info.meetingStatus == '04' || info.meetingStatus == '99' ||info.meetingStatus == 'AA') }">
																${info.sactionName }
															</c:when>
															<c:otherwise>
																${info.updName }
															</c:otherwise>
														</c:choose>
													</td>
													<td>
														<c:choose>
															<c:when test="${ info.returnName != '' && info.returnName != null && info.meetingStatus == '99' }">
																${info.returnName }
															</c:when>
															<c:when test="${ info.confirmName != '' && info.confirmName != null && info.meetingStatus == 'AA' }">
																${info.confirmName }
															</c:when>
															<c:otherwise>
																${confirmPerson.kname }
															</c:otherwise>
														</c:choose>
													</td>
												</tr>
												<tr style="height: 130px;">
													<th style="vertical-align: middle!important;">결재</th>
													<td style="vertical-align: middle!important;">
														<c:choose>
															<c:when test="${info.sactionDate != '' && info.sactionDate != null && (info.meetingStatus == '04' || info.meetingStatus == '99' ||info.meetingStatus == 'AA') }">
																<img src="/img/confirm.png" style="width: 100px;" />
															</c:when>
															<c:otherwise>
																결재 대기중입니다.
															</c:otherwise>
														</c:choose>
													</td>
													<td style="vertical-align: middle!important;">
														<c:choose>
															<c:when test="${ info.returnDate != '' && info.returnDate != null && info.meetingStatus == '99' }">
																<img src="/img/return.png" style="width: 100px;" />
															</c:when>
															<c:when test="${ info.confirmDate != '' && info.confirmDate != null && info.meetingStatus == 'AA' }">
																<img src="/img/confirm.png" style="width: 100px;" />
															</c:when>
															<c:otherwise>
																결재 대기중입니다.
															</c:otherwise>
														</c:choose>
													</td>
												</tr>
												<tr>
													<th>날짜</th>
													<td>
														<c:if test="${info.sactionDate != '' && info.sactionDate != null && (info.meetingStatus == '04' || info.meetingStatus == '99' ||info.meetingStatus == 'AA') }">
															${fn:substring(info.sactionDate,0,10) }
														</c:if>
													</td>
													<td>
														<c:if test="${ info.returnDate != '' && info.returnDate != null && info.meetingStatus == '99' }">
															${fn:substring(info.returnDate,0,10) }
														</c:if>
														<c:if test="${ info.confirmDate != '' && info.confirmDate != null && info.meetingStatus == 'AA' }">
															${fn:substring(info.confirmDate,0,10) }
														</c:if>
													</td>
												</tr>
												<tr>
													<c:choose>
														<c:when test="${!isMine && !isMineDept && info.meetingStatus == '03'}">
															<th>진행내역</th>
															<td>결재 대기중입니다.</td>
															<td>결재 대기중입니다.</td>
														</c:when>
														<c:otherwise>
															<th>코멘트</th>
															<td>
																<c:if test="${ info.sactionComment != '' && info.sactionComment != null && (info.meetingStatus == '04' || info.meetingStatus == '99' ||info.meetingStatus == 'AA') }">
																	${info.sactionComment }
																</c:if>
																<c:if test="${isMine && (info.meetingStatus == '03' || info.meetingStatus == '99') }">
													                <div class="input-group">
													                    <input type="text" id="sactionComment" class="form-control" placeholder="상신 코멘트" maxlength="70" />
													                    <span class="input-group-addon" onClick="javascript:confirmResult('04');">
																			<span class="input-group-text" style="cursor: pointer;"> 상신하기 </span>
													                    </span>
													                </div>
																</c:if>
															</td>
															<td>
																<c:if test="${isMineDept && info.meetingStatus == '04' }">
																	<div class="input-group">
													                    <input type="text" id="confirmComment" class="form-control" placeholder="반려, 결재 코멘트" maxlength="70" />
													                    <span class="input-group-addon" onClick="javascript:confirmResult('99');">
																			<span class="input-group-text" style="cursor: pointer;"> 반려하기 </span>
													                    </span>
													                    <span class="input-group-addon" onClick="javascript:confirmResult('AA');">
																			<span class="input-group-text" style="cursor: pointer;"> 결재하기 </span>
													                    </span>
													                </div>
																</c:if>
																<c:if test="${ info.returnComment != '' && info.returnComment != null && info.meetingStatus == '99' }">
																	${info.returnComment }
																</c:if>
																<c:if test="${ info.confirmComment != '' && info.confirmComment != null && info.meetingStatus == 'AA' }">
																	${info.confirmComment }
																</c:if>
															</td>
														</c:otherwise>
													</c:choose>
												</tr>
											</tbody>
										</table>
									</div>
								</section>
							</div>
						</div>
					</c:if>
					<!-- approval end -->
					
				</section>
				<!--overview end-->
				
			</section>
			<!--main content end-->
			
		</section>
		<!-- container section start -->
		
		<!--loading Popup start-->
		<%@ include file="/WEB-INF/views/include/loading.jsp"%>
		<!--loading Popup end--> 
		
		<br/>
		
	</body>
</html>