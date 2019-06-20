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
		<!-- custom script for this page-->
		<script type="text/javascript" src="/js/scripts.js?ver=20190619"></script>
		<script type="text/javascript" src="/js/jquery.autosize.min.js"></script>
		<script type="text/javascript" src="/js/jquery.placeholder.min.js"></script>
		<script type="text/javascript" src="/js/jquery.slimscroll.min.js"></script>
		<script type="text/javascript" src="/js/jquery.easy-autocomplete.min.js"></script>
		<!-- common function -->
		<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
		<!-- page script -->
		<script type="text/javascript">
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu06");
				
				// datetimepicker
				$('#visitStartDatePicker').datetimepicker({
					format: 'YYYY-MM-DD HH:mm',
					sideBySide: true,
				});
				
				// 참석자 검색
				getTotalUserListAjax();
			});
			
			// 직원 리스트 ajax
			function getTotalUserListAjax() {
				var params = {
					auth : "reyon"
				}
				
				var request = $.ajax({
					url: "/common/getTotalUserListAjax.json"
					, type : "POST"
					, timeout: 10000
					, data : params
					, dataType : "json"
					, error: function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					}
					, success : function(json) {
						if (json.resultCode == 0){
							var totalList = json.resultMsg;
							
							var options = {
								placeholder : "이름 입력",
								data : totalList,
								getValue : "kname",
								template : {
									type : "custom",
									method : function(value, item) {
										return value + " " + item.posLog + " - " + item.deptName;
									}
								},
								list : {
									match : {
										enabled : true
									},
									onChooseEvent : function() {
										var deptCode = $("#meetPerson").getSelectedItemData().deptCode;
										var deptName = $("#meetPerson").getSelectedItemData().deptName;
										var sabun = $("#meetPerson").getSelectedItemData().sabun;
										var name = $("#meetPerson").getSelectedItemData().kname;
										
										$("#meetPerson").val("");
										
										if ($("#meetPersonTr1").length > 0) {
											alert("접견인은 대표자 한 명만 입력 가능합니다.");
										} else {
											$("#meetPerson").hide();
											$("#meetPersonTable").show();
											
											if ($("#meetPersonTr0").length > 0) {
												$("#meetPersonTr0").remove();
											}
											var tag = '<tr id="meetPersonTr1">';
											tag += '<td>';
											tag += deptName;
											tag += '<input type="hidden" id="meetDeptCode" value="'+deptCode+'">';
											tag += '<input type="hidden" id="meetDeptName" value="'+deptName+'">';
											tag += '</td>';
											tag += '<td>';
											tag += name;
											tag += '<input type="hidden" id="meetSabun" value="'+sabun+'">';
											tag += '<input type="hidden" id="meetName" value="'+name+'">';
											tag += '</td>';
											tag += '<td>';
											tag += '<input type="button" class="btn btn-danger btn-sm" value="삭제" onClick="javascript:delMeetPerson();" />';
											tag += '</td>';
											tag += '</tr>';
											$("#meetPersonTbody").append(tag);
										}
									},
								}
							};
							
							$("#meetPerson").easyAutocomplete(options);
						} else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
						} else {
							alert(json.resultMsg);
						}
					}
				});
			}
			
			// 접견인 삭제
			function delMeetPerson() {
				$("#meetPersonTr1").remove();
				var tag = '<tr id="meetPersonTr0">';
				tag += '<td colspan="3">';
				tag += '접견인을 추가해 주세요.';
				tag += '<input type="hidden" id="meetDeptCode" value="" >';
				tag += '<input type="hidden" id="meetDeptName" value="" >';
				tag += '<input type="hidden" id="meetSabun" value="" >';
				tag += '<input type="hidden" id="meetName" value="" >';
				tag += '</td>';
				tag += '</tr>';
				$("#meetPersonTbody").append(tag);
				$("#meetPersonTable").hide();
				$("#meetPerson").show();
			}
			
			// 수정하기
			function goConfirm() {
				var visitSeq = $("#seq").val();
				var visitCompany = $("#visitCompany").val();
				var visitName = $("#visitName").val();
				var visitStartDate = $("#visitStartDate").val();
				var meetDeptCode = $("#meetDeptCode").val();
				var meetDeptName = $("#meetDeptName").val();
				var meetSabun = $("#meetSabun").val();
				var meetName = $("#meetName").val();
				
				if (visitCompany == "") {
					alert("출입 업체명을 입력해 주세요.");
					return;
				}
				if (visitName == "") {
					alert("출입자 성함을 입력해 주세요.");
					return;
				}
				if (visitStartDate == "") {
					alert("출입시간을 입력해 주세요.");
					return;
				}
				if (meetDeptCode == "" || meetDeptName == "" || meetSabun == "" || meetName == "") {
					alert("접견인을 입력해 주세요.");
					return;
				}
				
				if(confirm("수정하시겠습니까?")){
					var params = {
						auth : "reyon",
						visitSeq : visitSeq,
						visitCompany : visitCompany,
						visitName : visitName,
						visitStartDate : visitStartDate,
						meetDeptCode : meetDeptCode,
						meetDeptName : meetDeptName,
						meetSabun : meetSabun,
						meetName : meetName,
					}
					
					var request = $.ajax({
						url: '/coming/comingModifyAjax.json'
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
								alert("저장 완료 되었습니다.");
								var url = "/coming/comingView.do?visitSeq="+visitSeq;
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
								<i class="fa fa-pencil-square-o"></i> 출입자 수정
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-address-card-o"></i>출입자 관리</li>
								<li><i class="fa fa-pencil-square-o"></i>출입자 수정</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">출입자 상세 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">출입 업체명</label>
											<div class="col-lg-10">
												<input type="text" id="visitCompany" class="form-control" maxlength="30" value="${info.visitCompany }">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">출입자 성함</label>
											<div class="col-lg-10">
												<input type="text" id="visitName" class="form-control" maxlength="70" value="${info.visitName }">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">출입 시간</label>
											<div class="col-lg-10">
												<div class="row">
													<div class="col-lg-4 col-md-6 col-sm-12 col-xs-12">
														<div class="input-group date" id="visitStartDatePicker">
										                    <input type="text" id="visitStartDate" class="form-control" value="${info.visitStartDate }" />
										                    <span class="input-group-addon">
										                        <span class="glyphicon glyphicon-calendar"></span>
										                    </span>
										                </div>
													</div>
												</div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">접견인</label>
											<div class="col-lg-10">
												<input type="text" id="meetPerson" class="form-control" maxlength="10" style="display:none;">
												<table class="table table-bordered table-hover personal-task" id="meetPersonTable">
													<colgroup>
														<col style="width:35%" />
														<col style="width:35%" />
														<col style="width:30%" />
													</colgroup>
													<thead>
														<tr>
															<th class="text-center">부서</th>
															<th class="text-center">이름</th>
															<th class="text-center">접견인 삭제</th>
														</tr>
													</thead>
													<tbody id="meetPersonTbody">
														<tr id="meetPersonTr1">
															<td>
																${info.meetDeptName }
																<input type="hidden" id="meetDeptCode" value="${info.meetDeptCode }">
																<input type="hidden" id="meetDeptName" value="${info.meetDeptName }">
															</td>
															<td>
																${info.meetName }
																<input type="hidden" id="meetSabun" value="${info.meetSabun }">
																<input type="hidden" id="meetName" value="${info.meetName }">
															</td>
															<td>
																<input type="button" class="btn btn-danger btn-sm" value="삭제" onClick="javascript:delMeetPerson();" />
															</td>
														</tr>
													</tbody>
												</table>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">등록인</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.regDeptName } (${info.regName })</p>
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
						<input type="hidden" id="seq" value="${info.visitSeq }">
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