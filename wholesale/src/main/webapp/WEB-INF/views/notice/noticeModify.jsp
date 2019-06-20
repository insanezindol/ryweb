<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>이연제약 도매관리 시스템</title>
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
				cfLNBMenuSelect("menu02");
				
				// summernote editor loading
				$(".textnote").summernote({
					lang: 'ko-KR',
			        tabsize: 2,
			        height: 400,
			        placeholder: '본문을 입력하세요.',
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
			    $('#contents').on('summernote.change', function(we, contents, $editable) {
			    	cfChkByteBySummernote('contents','4000','byteInfo');
			    });
				
			 	// 바이트 기본 설정
				$("#byteInfo").text(cfGetByteLength($("#contents").summernote("code")));
			 	
				// 첨부파일 조건 변경 시
				$("#attFileType").change(function() { 
					var attFileType = $("#attFileType").val();
					$("#attFileTypePAreaA").hide();
					$("#attFileTypePAreaB").hide();
					$("#attFileTypePAreaC").hide();
					$("#attFileTypePArea"+attFileType).show();
				});
			});
			
			// 수정하기
			function goConfirm() {
				var seq = $("#seq").val();
				var title = $("#title").val();
				var contents = $("#contents").summernote("code");
				var attFileType = $("#attFileType").val();
				var resultDocumentFile = $("#resultDocumentFile")[0].files[0];
				
				if (title == "") {
					alert("제목을 입력해 주세요.");
					return;
				}
				if (contents == "") {
					alert("본문을 입력해 주세요.");
					return;
				}
				
				var contentsLength = cfGetByteLength(contents);
				if(contentsLength > 4000) {
					alert("[본문] 항목의 최대 길이를 초과하였습니다.\n수정 후 다시 시도해주세요.\n- 최대길이 : 4000 byte\n- 현재길이 : " + contentsLength + " byte");
					return;
				}
				
				if ($("#resultDocumentFile").get(0).files.length !== 0) {
					if($("#resultDocumentFile")[0].files[0].size > 10240000){
						alert("첨부파일은 10MB를 초과 할 수 없습니다.");
						return;
					}
				}
				
				if(confirm("수정하시겠습니까?")){
					var formData = new FormData();
					formData.append("auth", "reyon");
					formData.append("noticeSeq", seq);
					formData.append("title", title);
					formData.append("contents", contents);
					formData.append("attFileType", attFileType);
					formData.append("resultDocumentFile", resultDocumentFile);
					
					var request = $.ajax({
						url: '/notice/noticeModifyAjax.json'
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
								alert("수정 완료 되었습니다.");
								var url = "/notice/noticeView.do?seq="+seq;
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
			
			// 첨부파일 다운로드
			function downloadFile(seq){
				location.href="/notice/noticeFileDownload.do?seq="+seq;
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
								<i class="fa fa-pencil-square-o"></i> 공지사항 수정
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>도매관리</li>
								<li><i class="fa fa-book"></i>공지사항</li>
								<li><i class="fa fa-pencil-square-o"></i>공지사항 수정</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">공지사항 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">제목</label>
											<div class="col-lg-10">
												<input type="text" id="title" class="form-control" maxlength="30" placeholder="제목을 입력하세요." value="${info.title }">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">본문<br><span id="byteInfo">0</span>/4000Byte</label>
											<div class="col-lg-10">
												<div id="contents" class="textnote">${info.contents }</div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">첨부파일 변경 항목</label>
											<div class="col-lg-10">
												<select id="attFileType" class="form-control" style="margin-bottom: 10px;">
													<option value="A">첨부파일 변경없음</option>
													<option value="B">새로운 첨부파일 사용</option>
													<option value="C">현재 첨부파일 삭제</option>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">첨부파일</label>
											<div class="col-lg-10">
												<p class="form-control-static" id="attFileTypePAreaA" style="display:block;">
													<c:choose>
														<c:when test="${info.attachFilename == '' || info.attachFilename == null }">첨부파일이 없습니다.</c:when>
														<c:otherwise><a href="javascript:downloadFile('${info.noticeSeq }');">${info.attachFilename }</a></c:otherwise>
													</c:choose>
												</p>
												<p class="form-control-static" id="attFileTypePAreaB" style="display:none;"><input type="file" id="resultDocumentFile"></p>
												<p class="form-control-static" id="attFileTypePAreaC" style="display:none;">현재 등록 파일이 삭제됩니다.</p>
												
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
						<input type="hidden" id="seq" value="${info.noticeSeq }">
						<input type="hidden" id="queStr" value="${queStr}" />
						<a class="btn btn-default" href="javascript:;" title="수정하기" style="margin-top: 7px;" onClick="javascript:goConfirm();"><i class="fa fa-check"></i>&nbsp;수정</a>
						<a class="btn btn-default" href="javascript:;" title="이전으로" style="margin-top: 7px;" onClick="javascript:goBack();"><i class="fa fa-arrow-circle-o-left"></i>&nbsp;이전</a>
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