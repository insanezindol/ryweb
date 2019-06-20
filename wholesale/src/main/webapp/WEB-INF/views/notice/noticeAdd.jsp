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
			});
			
			// 등록
			function goConfirm() {
				var title = $("#title").val();
				var contents = $("#contents").summernote("code");
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
				
				if(confirm("등록하시겠습니까?")){
					var formData = new FormData();
					formData.append("auth", "reyon");
					formData.append("title", title);
					formData.append("contents", contents);
					formData.append("resultDocumentFile", resultDocumentFile);
					
					var request = $.ajax({
						url: '/notice/noticeAddAjax.json'
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
								alert("등록 완료 되었습니다.");
								location.href="/notice/noticeList.do";
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
								<i class="fa fa-pencil"></i> 공지사항 등록
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>도매관리</li>
								<li><i class="fa fa-book"></i>공지사항</li>
								<li><i class="fa fa-pencil"></i>공지사항 등록</li>
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
												<input type="text" id="title" class="form-control" maxlength="30" placeholder="제목을 입력하세요.">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">본문<br><span id="byteInfo">0</span>/4000Byte</label>
											<div class="col-lg-10">
												<div id="contents" class="textnote"></div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">첨부파일</label>
											<div class="col-lg-10">
												<p class="form-control-static"><input type="file" id="resultDocumentFile"></p>
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
						<a class="btn btn-default" href="javascript:;" title="등록하기" style="margin-top: 7px;" onClick="javascript:goConfirm();"><i class="fa fa-check"></i>&nbsp;등록</a>
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