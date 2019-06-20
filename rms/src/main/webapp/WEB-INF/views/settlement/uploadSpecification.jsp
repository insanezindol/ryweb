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
		<link href="/css/jquery.dm-uploader.min.css" rel="stylesheet">
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
		<!-- jquery uploader -->
		<script type="text/javascript" src="/js/jquery.dm-uploader.min.js"></script>
		<!-- common function -->
		<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
		<!-- page script -->
		<script type="text/javascript">
			
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu11");
				
				// jquery uploader
				$("#drop-area").dmUploader({
					url : '/settlement/specificationUploadAjax.json',
					dataType : "json",
					extFilter: ["pdf"],
					maxFileSize: 10000000, // 10MB
					extraData: {
						"auth" : "reyon",
						"yymm": $("#yymm").val(),
						"status": "ING",
					},
					onNewFile : function(id, file) {
						multi_add_file(id, file);
					},
					onBeforeUpload : function(id) {
						multi_update_file_progress(id, 0, 'progress-bar-info', '업로드 준비중');
					},
					onUploadCanceled : function(id) {
						multi_update_file_progress(id, 0, 'progress-bar-warning', '업로드 취소');
					},
					onUploadProgress : function(id, percent) {
						multi_update_file_progress(id, percent, 'progress-bar-info', percent+'%');
					},
					onUploadSuccess : function(id, data) {
						if(data.resultCode == 0){
							multi_update_file_progress(id, 100, 'progress-bar-success', data.resultMsg);
						} else {
							multi_update_file_progress(id, 0, 'progress-bar-danger', data.resultMsg);
						}
					},
					onUploadError : function(id, xhr, status, message) {
						multi_update_file_progress(id, 0, 'progress-bar-danger', '업로드 실패');
					},
					onFallbackMode : function() {
						multi_update_file_progress(id, 0, 'progress-bar-danger', 'Plugin cant be used here, running Fallback callback');
					},
					onFileSizeError : function(file) {
						multi_update_file_progress(id, 0, 'progress-bar-danger', 'size excess limit');
					}
				});
			});
			
			function multi_add_file(id, file) {
				var tag = '<tr>';
				tag += '<td class="text-center">';
				tag += file.name;
				tag += '</td>';
				tag += '<td class="text-center">';
				tag += '<div class="progress progress-striped active" style="margin-bottom:0px;">';
				tag += '<div id="progress_'+id+'" class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%">';
				tag += '0%';
				tag += '</div>';
				tag += '</div>';
				tag += '</td>';
				tag += '</tr>';
				$("#uploadResultDiv").append(tag);
			}
			
			function multi_update_file_progress(id, percent, color, msg) {
				$("#progress_"+id).removeClass('progress-bar-info');
				$("#progress_"+id).removeClass('progress-bar-warning');
				$("#progress_"+id).removeClass('progress-bar-danger');
				$("#progress_"+id).removeClass('progress-bar-success');
				$("#progress_"+id).addClass(color);
				if (color == 'progress-bar-danger' || color == 'progress-bar-warning') {
					$("#progress_"+id).css('width', '100%');
					$("#progress_"+id).attr('aria-valuenow', '100');
					$("#progress_"+id).text('업로드 실패');
				} else if (color == 'progress-bar-success') {
					$("#progress_"+id).text('업로드 성공');
				} else {
					$("#progress_"+id).css('width', percent+'%');
					$("#progress_"+id).attr('aria-valuenow', percent);
					$("#progress_"+id).text(percent+'%');
				}
			}
			
			// 조회
			function goSearch() {
				var yymm = $("#yymm").val();
				var status = $("#status").val();

				var params = {
					auth : "reyon",
					yymm : yymm,
					status : status,
				}

				$("#uploadDiv").hide();
				$("#resultDiv").hide();
				
				var request = $.ajax({
					url : "/settlement/getSpecificationListAjax.json",
					type : "POST",
					timeout : 10000,
					data : params,
					dataType : "json",
					beforeSend: function(xmlHttpRequest) {
						cfOpenMagnificPopup();
					},
					error : function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					},
					success : function(json) {
						if (json.resultCode == 0) {
							$("#resultBody").empty();
							var list = json.list;
							var tag = '';
							if (list.length == 0) {
								tag += '<tr>';
								tag += '<td class="text-center" colspan="6">파일이 없습니다.</td>';
								tag += '</tr>';
							} else {
								for (var i = 0; i < list.length; i++) {
									tag += '<tr>';
									tag += '<td class="text-center">' + (i + 1) + '</td>';
									tag += '<td class="text-center">' + list[i].fileName + '</td>';
									tag += '<td class="text-center">' + list[i].fileByte + 'KB</td>';
									tag += '<td class="text-center">' + list[i].fileTime + '</td>';
									tag += '<td class="text-center"><a class="btn btn-primary btn-sm" href="javascript:;" onClick="javascript:goDownload(\'' + list[i].fileName + '\');"><span class="icon_check_alt2"></span>&nbsp;다운</a></td>';
									tag += '<td class="text-center"><a class="btn btn-danger btn-sm" href="javascript:;" onClick="javascript:goDelete(\'' + list[i].fileName + '\');"><span class="icon_close_alt2"></span>&nbsp;삭제</a></td>';
									tag += '</tr>';
								}
							}
							$("#resultBody").append(tag);
							$("#uploadDiv").show();
							$("#resultDiv").show();
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

			// 파일 다운로드
			function goDownload(fileName) {
				var form = document.createElement("form");
				form.name = "downForm";
				form.id = "downForm";
				form.method = "POST";
				form.action = "/settlement/specificationDownload.do";
				var element1 = document.createElement("input");
				element1.type = "hidden";
				element1.name = "auth";
				element1.value = "reyon";
				var element2 = document.createElement("input");
				element2.type = "hidden";
				element2.name = "yymm";
				element2.value = $("#yymm").val();
				var element3 = document.createElement("input");
				element3.type = "hidden";
				element3.name = "status";
				element3.value = $("#status").val();
				var element4 = document.createElement("input");
				element4.type = "hidden";
				element4.name = "fileName";
				element4.value = fileName;
				$(form).append(element1);
				$(form).append(element2);
				$(form).append(element3);
				$(form).append(element4);
				$("#container").append(form);
				form.submit();
				$("#downForm").remove();
			}

			// 파일 삭제
			function goDelete(fileName) {
				if (confirm("[" + fileName + "]\n정말로 삭제하시겠습니까?")) {
					var yymm = $("#yymm").val();
					var status = $("#status").val();

					var params = {
						auth : "reyon",
						yymm : yymm,
						status : status,
						fileName : fileName
					}

					var request = $.ajax({
						url : "/settlement/specificationDeleteAjax.json",
						type : "POST",
						timeout : 10000,
						data : params,
						dataType : "json",
						beforeSend: function(xmlHttpRequest) {
							cfOpenMagnificPopup();
						},
						error : function(xhr, textStatus, errorThrown) {
							alert("시스템 오류가 발생했습니다.");
						},
						success : function(json) {
							if (json.resultCode == 0) {
								alert("삭제가 완료 되었습니다.");
								goSearch();
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
								<i class="fa fa-upload"></i> 지급명세서 업로드
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-krw"></i>연말정산 관리</li>
								<li><i class="fa fa-upload"></i>지급명세서 업로드</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- search bar start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel">
								<header class="panel-heading">지급명세서 파일 조회</header>
								<div class="panel-body" id="searchPanel">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">귀속년도</label>
											<div class="col-lg-8">
												<select id="yymm" class="form-control">
													<option value="201812">2018년</option>
													<!-- <option value="201912">2019년</option> -->
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">상태</label>
											<div class="col-lg-8">
												<select id="status" class="form-control">
													<option value="ING">분석 대기</option>
													<option value="END">분석 완료</option>
												</select>
											</div>
											<div class="col-lg-2">
												<input type="text" style="display: none;" />
												<a class="btn btn-default" href="javascript:;" title="조회하기" onClick="javascript:goSearch();"><span class="icon_search"></span>&nbsp;조회</a>
											</div>
										</div>
										
									</form>
								</div>
							</section>
						</div>
					</div>
					<!-- search bar end -->
					
					<!-- upload table start -->
					<div class="row" id="uploadDiv" style="display:none;">
						<div class="col-lg-3">
							<section class="panel">
								<header class="panel-heading">지급명세서 파일 선택</header>
								<div class="panel-body" style="height: 190px;">
									<div id="drop-area">
										<div class="alert alert-success" style="height: 100px; text-align: center; padding-top: 20px; margin-bottom: 0px;"><i class="fa fa-file-pdf-o"></i><br>마우스로 PDF 파일을 끌어오세요.<br>파일이름이 동일한 경우 덮어쓰기 됩니다.</div>
										<label for="xlfile" id="xlfile-label"></label> <input type="file" id="xlfile" name="xlfile" style="display: none;" />
										<a class="btn btn-success btn-block" href="javascript:;" onClick="$('#xlfile-label').click();"><i class="fa fa-file-pdf-o"></i> PDF 찾아보기</a>
									</div>
								</div>
							</section>
						</div>
						<div class="col-lg-9">
							<section class="panel">
								<header class="panel-heading">지급명세서 업로드 결과</header>
								<div class="panel-body" style="height: 190px; overflow-y: scroll;">
									<table class="table table-bordered">
									<colgroup>
										<col style="width: 50%;">
										<col style="width: 50%;">
									</colgroup>
									<tbody id="uploadResultDiv">
									</tbody>
								</table>
								</div>
							</section>
						</div>
					</div>
					<!-- upload table end -->
					
					<!-- main table start -->
					<div class="row" id="resultDiv" style="display:none;">
						<div class="col-lg-12">
							<section class="panel">
								<table class="table table-bordered">
									<colgroup>
										<col style="width: 5%;">
										<col style="width: 45%;">
										<col style="width: 10%;">
										<col style="width: 20%;">
										<col style="width: 10%;">
										<col style="width: 10%;">
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">번호</th>
											<th class="text-center">파일이름</th>
											<th class="text-center">파일크기</th>
											<th class="text-center">수정한 날짜</th>
											<th class="text-center">다운로드</th>
											<th class="text-center">삭제</th>
										</tr>
									</thead>
									<tbody id="resultBody">
									</tbody>
								</table>
							</section>
						</div>
					</div>
					<!-- main table end -->
					
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