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
		<link href="/css/easy-autocomplete.min.css" rel="stylesheet">
		<!-- javascripts -->
		<script type="text/javascript" src="/js/jquery-1.8.3.min.js"></script>
		<script type="text/javascript" src="/js/jquery-ui-1.10.4.min.js"></script>
		<script type="text/javascript" src="/js/jquery-ui-1.9.2.custom.min.js"></script>
		<!-- moment script -->
		<script type="text/javascript" src="/js/moment.js"></script>
  		<script type="text/javascript" src="/js/moment-ko.js"></script>
		<!-- bootstrap -->
		<script type="text/javascript" src="/js/bootstrap.min.js"></script>
		<!-- common function -->
		<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
		<!-- page script -->
		<script type="text/javascript">
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu17");
				
				//키보드 이벤트(검색)
				$("#jpmnm").keydown(function (key) {
				    if (key.keyCode == 13) goSearch();
				});
				
				
			});
			
			//원부자재 마스터 검색
			function goSearch() {
				//검색 parameter 설정하고 goUrl();
				var params = {
						auth : "reyon",
						jpmnm : $('#jpmnm').val()
				}
				$.ajax({
					url: "/control/controlSelect.json"
					, type : 'POST'
					, data : params
					, dataType : 'json'
						, error: function(xhr, textStatus, errorThrown) {
							alert("시스템 오류가 발생했습니다.");
						}
						, success : function(json) {
							var htmlStr = '';
							var list = json.controlList;
							$('#controlTbody').empty();
							for( var i = 0 ; i<list.length ; i++ ) {
								htmlStr += '<tr style="border-bottom: 1px solid #ccc;">';
								htmlStr += '<td class="text-center">'+list[i].ryno+'</td>';
								htmlStr += '<td class="text-center"><a href=javascript:getBOM("'+list[i].ryno+'");>'+list[i].jpmnm+'</a></td>';
								htmlStr += '<td class="text-center">'+list[i].gyugk+'</td>';
								htmlStr += '<td class="text-center">'+list[i].jpmgbn+'</td>';
								htmlStr += '</tr>';
							}
							$('#controlTbody').append(htmlStr);
						}
				})
				return;
			}
			
			function getBOM(ryno) {
				$('#code').empty();
				$('#code').append("선택");
				
				var params = {
						auth : "reyon",
						jpmcd : ryno
				}
				
				$.ajax({
					url: "/control/BOMSelect.json"
					, type : 'POST'
					, data : params
					, dataType : 'json'
						, error: function(xhr, textStatus, errorThrown) {
							alert("시스템 오류가 발생했습니다.");
						}
						, success : function(json) {
							var htmlStr = '';
							var list = json.controlList;
							$('#controlTbody').empty();
							for( var i = 0 ; i<list.length ; i++ ) {
								htmlStr += '<tr style="border-bottom: 1px solid #ccc;">';
								htmlStr += '<td class="text-center"><input type="checkbox" class="form-control" name="ryno" value="'+list[i].ryno+'"/></td>';
								htmlStr += '<td class="text-center">'+list[i].jpmnm+'</a></td>';
								htmlStr += '<td class="text-center">'+list[i].gyugk+'</td>';
								htmlStr += '<td class="text-center">'+list[i].jpmgbn+'</td>';
								htmlStr += '</tr>';
							}
							$('#controlTbody').append(htmlStr);
							
							//체크박스 이벤트 >> 요청서 아니 왜 여기서 해야 되냐 ㅡㅡ 레이아웃 진짜 ;;;;;
							$('input:checkbox[name=ryno]').change(function(){
					        	htmlStr = "";
					        	$('#order').empty();
					        	$("input[name=ryno]:checked").each(function(i, elements) {
					        		htmlStr += '<div class="form-group">';
						        	htmlStr += '<label class="col-lg-2 control-label">'+list[$(elements).index('input[name=ryno]')].jpmnm+'</label>';
						        	htmlStr += '<div class="col-lg-8">';
						        	htmlStr += '<input type="text" name="content" class="form-control" maxlength="100">';
						        	htmlStr += '</div>';
						        	htmlStr += '<div class="col-lg-2">';
						        	htmlStr += '<input type="file" name="multiFile[]" class="form-control-static">';
						        	htmlStr += '</div>';
						        	htmlStr += '</div>';
					        	});
					        	$('#order').append(htmlStr);
						    });
						}
				})
			}
			
			// 등록
			function goConfirm() {
				var ryno = new Array();
				$('input:checkbox[name=ryno]:checked').each(function () {
					ryno.push($(this).val());
				});
				var contents = new Array();
				$('input:text[name=content]').each(function () {
					contents.push($(this).val());
				});
				var passIndex = [];
				
				if (ryno == "") {
					alert("자제를 선택해주세요.");
					return;
				}
				contents.some( function (value, index) {
					if( value == "" || value == null ) {
						alert("변경 요청 사항을 모두 적어주세요.");
						return true;
					}
					if ( contents.length-1 == index ) {
						if(confirm("등록하시겠습니까?")){
							var formData = new FormData();
							formData.append("auth", "reyon");
							formData.append("ryno", ryno);
							formData.append("contents", contents);
							$('input[name^="multiFile"]').each(function(idx) {
								if( $(this)[0].files[0] == null || $(this)[0].files[0] == "" ) //어떻게 처리하지...
									passIndex.push(idx);
								formData.append("multiFile"+idx, $(this)[0].files[0]);
							});
							formData.append("passIndex", passIndex);
							
							var request = $.ajax({
								url: '/control/controlAddAjax.json'
								, type : 'POST'
								, timeout: 3000
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
										location.href="/control/controlList.do";
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
				});
			};
			
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
								<i class="fa fa-list"></i> 재고자산 통제 관리
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-bell-o"></i>재고자산 통제 관리</li>
								<li><i class="fa fa-pencil"></i>자재 통제 등록</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">자재 통제 상세 정보</header>
								<div class="panel-body">
									<form id="fileForm" class="form-horizontal" enctype="multipart/form-data">
										<div class="form-group">
											<label class="col-lg-2 control-label">제품 이름</label>
											<div class="col-lg-10">
												<input type="text" id="jpmnm" class="form-control" maxlength="150">
											</div>
										</div>
										<div class="form-group">
											<div class="col-lg-2"></div>
											<div class="col-lg-10">
												<table class="table table-bordered">
													<colgroup>
														<col style="width: 20%;">
														<col style="width: 40%;">
														<col style="width: 20%;">
														<col style="width: 20%;">
													</colgroup>
													<thead>
														<tr>
															<th class="text-center" id="code">코드</th>
															<th class="text-center">품명</th>
															<th class="text-center">규격</th>
															<th class="text-center">구분</th>
														</tr>
													</thead>
													<tbody id="controlTbody">
													</tbody>
												</table>
											</div>
										</div>
										<div class="form-group" id="order">
											<div class="col-lg-12">
												<label class="col-lg-2 control-label">요청서</label>
												<div class="col-lg-10">
													<input type="text" class="form-control" value="BOM을 선택해서 파일을 올려주세요" readonly>
												</div>
											</div>
										</div>
										<div class="form-group">
											<div class="col-lg-12">
												<label class="col-lg-2 control-label">통제자</label>
												<div class="col-lg-10">
													<p class="form-control-static"><sec:authentication property="principal.kname" /></p>
												</div>
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