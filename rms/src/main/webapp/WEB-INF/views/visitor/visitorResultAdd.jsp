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
		<!-- summernote editor -->
		<link href="/css/summernote.css" rel="stylesheet">
		<script type="text/javascript" src="/js/summernote.js"></script>
		<script type="text/javascript" src="/js/summernote-ko-KR.js"></script>
		<!-- page script -->
		<script type="text/javascript">
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu04");
				
				// summernote editor loading
				$(".textnote").summernote({
					lang: 'ko-KR',
			        tabsize: 2,
			        height: 200,
			        toolbar: [
			            ['style', ['style']],
			            ['font', ['bold', 'underline', 'clear']],
			            ['fontname', ['fontname']],
			            ['color', ['color']],
			            ['para', ['ol', 'paragraph']],
			            ['table', ['table']],
			            ['view', ['undo', 'redo']],
			        ],
				});
				
				// 입력 자리수 체크
			    $('#meetingContents').on('summernote.change', function(we, contents, $editable) {
			    	cfChkByteBySummernote('meetingContents','4000','byteInfo1');
			    });
			    $('#decisionContents').on('summernote.change', function(we, contents, $editable) {
			    	cfChkByteBySummernote('decisionContents','4000','byteInfo2');
			    });
			    $('#planContents').on('summernote.change', function(we, contents, $editable) {
			    	cfChkByteBySummernote('planContents','4000','byteInfo3');
			    });
			    $('#issueContents').on('summernote.change', function(we, contents, $editable) {
			    	cfChkByteBySummernote('issueContents','4000','byteInfo4');
			    });
			});
			
			// 등록
			function goConfirm() {
				var seq = $("#seq").val();
				var meetingContents = $("#meetingContents").summernote("code");
				var decisionContents = $("#decisionContents").summernote("code");
				var planContents = $("#planContents").summernote("code");
				var issueContents = $("#issueContents").summernote("code");
				var resultDocumentFile1 = $("#resultDocumentFile1")[0].files[0];
				var resultDocumentFile2 = $("#resultDocumentFile2")[0].files[0];
				var resultDocumentFile3 = $("#resultDocumentFile3")[0].files[0];
				var resultDocumentFile4 = $("#resultDocumentFile4")[0].files[0];
				var resultDocumentFile5 = $("#resultDocumentFile5")[0].files[0];
				
				var meetingContentsLength = cfGetByteLength(meetingContents);
				var decisionContentsLength = cfGetByteLength(decisionContents);
				var planContentsLength = cfGetByteLength(planContents);
				var issueContentsLength = cfGetByteLength(issueContents);
				
				if(meetingContentsLength > 4000) {
					alert("[방문내용] 항목의 최대 길이를 초과하였습니다.\n수정 후 다시 시도해주세요.\n- 최대길이 : 4000 byte\n- 현재길이 : " + meetingContentsLength + " byte");
					return;
				}
				if(decisionContentsLength > 4000) {
					alert("[결정사항] 항목의 최대 길이를 초과하였습니다.\n수정 후 다시 시도해주세요.\n- 최대길이 : 4000 byte\n- 현재길이 : " + decisionContentsLength + " byte");
					return;
				}
				if(planContentsLength > 4000) {
					alert("[향후일정] 항목의 최대 길이를 초과하였습니다.\n수정 후 다시 시도해주세요.\n- 최대길이 : 4000 byte\n- 현재길이 : " + planContentsLength + " byte");
					return;
				}
				if(issueContentsLength > 4000) {
					alert("[특이사항] 항목의 최대 길이를 초과하였습니다.\n수정 후 다시 시도해주세요.\n- 최대길이 : 4000 byte\n- 현재길이 : " + issueContentsLength + " byte");
					return;
				}
				
				if ($("#resultDocumentFile1").get(0).files.length !== 0) {
					if($("#resultDocumentFile1")[0].files[0].size/1024/1024 > 10){
						alert("첨부파일은 10MB를 초과 할 수 없습니다.(첨부파일 1번)");
						return;
					}
				}
				if ($("#resultDocumentFile2").get(0).files.length !== 0) {
					if($("#resultDocumentFile2")[0].files[0].size/1024/1024 > 10){
						alert("첨부파일은 10MB를 초과 할 수 없습니다.(첨부파일 2번)");
						return;
					}
				}
				if ($("#resultDocumentFile3").get(0).files.length !== 0) {
					if($("#resultDocumentFile3")[0].files[0].size/1024/1024 > 10){
						alert("첨부파일은 10MB를 초과 할 수 없습니다.(첨부파일 3번)");
						return;
					}
				}
				if ($("#resultDocumentFile4").get(0).files.length !== 0) {
					if($("#resultDocumentFile4")[0].files[0].size/1024/1024 > 10){
						alert("첨부파일은 10MB를 초과 할 수 없습니다.(첨부파일 4번)");
						return;
					}
				}
				if ($("#resultDocumentFile5").get(0).files.length !== 0) {
					if($("#resultDocumentFile5")[0].files[0].size/1024/1024 > 10){
						alert("첨부파일은 10MB를 초과 할 수 없습니다.(첨부파일 5번)");
						return;
					}
				}
				
				if(confirm("등록하시겠습니까?")){
					var formData = new FormData();
					formData.append("auth", "reyon");
					formData.append("seq", seq);
					formData.append("meetingContents", meetingContents);
					formData.append("decisionContents", decisionContents);
					formData.append("planContents", planContents);
					formData.append("issueContents", issueContents);
					formData.append("resultDocumentFile1", resultDocumentFile1);
					formData.append("resultDocumentFile2", resultDocumentFile2);
					formData.append("resultDocumentFile3", resultDocumentFile3);
					formData.append("resultDocumentFile4", resultDocumentFile4);
					formData.append("resultDocumentFile5", resultDocumentFile5);
					
					var request = $.ajax({
						url: '/visitor/visitorResultAddAjax.json'
						, type : 'POST'
						, timeout: 0
						, data : formData
						, processData : false
						, contentType : false
						, beforeSend: function(xmlHttpRequest) {
							cfOpenMagnificPopup();
						}
						, error: function(xhr, textStatus, errorThrown) {
							alert("시스템 오류가 발생했습니다.");
						}
						, success : function(json) {
							if (json.resultCode == 1){
								alert("저장 완료 되었습니다.");
								var url = "/visitor/visitorView.do?meetingSeq=" + seq;
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
				var seq = $("#seq").val();
				var url = "/visitor/visitorView.do?meetingSeq=" + seq;
				var queStr = $("#queStr").val();
				if(queStr != ""){
					url += "&queStr=" + encodeURIComponent(queStr);
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
								<i class="fa fa-pencil"></i> 방문 결과 등록
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-address-card-o"></i>방문자 관리</li>
								<li><i class="fa fa-file-text-o"></i>방문자 상세</li>
								<li><i class="fa fa-pencil"></i>방문 결과 등록</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">방문자 상세 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">방문 업체명</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.visitCompany }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">방문자 성함</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.visitName }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">프로젝트</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.projectName }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">방문목적</label>
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
											<label class="col-lg-2 control-label">부서</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.deptName }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">담당자</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.regName }</p>
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
																${result.attendantName }<c:if test="${!status.last}">,</c:if>
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
																${result.attendantName }<c:if test="${!status.last}">,</c:if>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">등록일</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.meetingRegDate }</p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">수정일</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.meetingUpdDate }</p>
											</div>
										</div>
									</form>
								</div>
							</section>
						</div>
					</div>
					<!-- body top end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">방문 결과 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">방문내용<br><span id="byteInfo1">0</span>/4000Byte</label>
											<div class="col-lg-10">
												<div id="meetingContents" class="textnote"></div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">결정사항<br><span id="byteInfo2">0</span>/4000Byte</label>
											<div class="col-lg-10">
												<div id="decisionContents" class="textnote"></div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">향후일정<br><span id="byteInfo3">0</span>/4000Byte</label>
											<div class="col-lg-10">
												<div id="planContents" class="textnote"></div>
											</div>
										</div>
										<div class="form-group"> 
											<label class="col-lg-2 control-label">특이사항<br><span id="byteInfo4">0</span>/4000Byte</label>
											<div class="col-lg-10">
												<div id="issueContents" class="textnote"></div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">첨부파일 1번<br>(10MB 이하만 가능)</label>
											<div class="col-lg-10">
												<p class="form-control-static"><input type="file" id="resultDocumentFile1"></p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">첨부파일 2번<br>(10MB 이하만 가능)</label>
											<div class="col-lg-10">
												<p class="form-control-static"><input type="file" id="resultDocumentFile2"></p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">첨부파일 3번<br>(10MB 이하만 가능)</label>
											<div class="col-lg-10">
												<p class="form-control-static"><input type="file" id="resultDocumentFile3"></p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">첨부파일 4번<br>(10MB 이하만 가능)</label>
											<div class="col-lg-10">
												<p class="form-control-static"><input type="file" id="resultDocumentFile4"></p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">첨부파일 5번<br>(10MB 이하만 가능)</label>
											<div class="col-lg-10">
												<p class="form-control-static"><input type="file" id="resultDocumentFile5"></p>
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
						<a class="btn btn-default" href="javascript:;" title="등록하기" style="margin-top: 7px;" onClick="javascript:goConfirm();"><span class="icon_check"></span>&nbsp;등록</a>
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