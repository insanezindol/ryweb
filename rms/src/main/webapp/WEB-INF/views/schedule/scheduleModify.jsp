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
				cfLNBMenuSelect("menu13");
				
				// datetimepicker
				$('#startDatetime').datetimepicker({
					format: 'YYYY-MM-DD HH:mm',
					sideBySide: true,
					stepping: 10,
				});
				$('#endDatetime').datetimepicker({
					format: 'YYYY-MM-DD HH:mm',
					sideBySide: true,
					stepping: 10,
				});
				
				// 값 설정
				$("#scheduleType").val("${info.scheduleType}").prop("selected", true);
				$("#scheduleName").val("${info.scheduleName }");
				$("#startDate").val("${info.scheduleStarttime }");
				$("#endDate").val("${info.scheduleEndtime }");
				$("#scheduleRemark").val("${info.scheduleRemark }");
				
				// 대상 직원 검색
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
							
							var options1 = {
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
										var deptCode = $("#attendee1").getSelectedItemData().deptCode;
										var deptName = $("#attendee1").getSelectedItemData().deptName;
										var sabun = $("#attendee1").getSelectedItemData().sabun;
										var name = $("#attendee1").getSelectedItemData().kname;
										
										$("#attendee1").val("");
										
										if ($("#attendee1Tr" + sabun).length > 0) {
											alert("이미 등록된 직원 입니다.");
										} else {
											if ($("#attendee1Tr0").length > 0) {
												$("#attendee1Tr0").remove();
											}
											var tag = '<tr id="attendee1Tr'+sabun+'">';
											tag += '<td>';
											tag += deptName;
											tag += '<input type="hidden" name="deptCode1[]" value="'+deptCode+'">';
											tag += '</td>';
											tag += '<td>';
											tag += name;
											tag += '<input type="hidden" name="sabun1[]" value="'+sabun+'">';
											tag += '</td>';
											tag += '<td>';
											tag += '<input type="button" class="btn btn-danger btn-sm" value="삭제" onClick="javascript:delAttendee1Tr(\'' + sabun + '\');" />';
											tag += '</td>';
											tag += '</tr>';
											$("#attendee1Tbody").append(tag);
										}
									},
								}
							};
							
							$("#attendee1").easyAutocomplete(options1);
						}else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
						}else{
							alert(json.resultMsg);
						}
					}
				});
			}
			
			// 참석자 삭제
			function delAttendee1Tr(name) {
				$("#attendee1Tr"+name).remove();
				
				if($("#attendee1Tbody tr").length < 1) {
					var tag = '<tr id="attendee1Tr0">';
					tag += '<td colspan="3">';
					tag += '대상 직원을 추가해 주세요.';
					tag += '</td>';
					tag += '</tr>';
					$("#attendee1Tbody").append(tag);
				}
			}
			
			// 등록
			function goConfirm() {
				var scheduleSeq = $("#scheduleSeq").val();
				var scheduleType = $("#scheduleType").val();
				var scheduleName = $("#scheduleName").val();
				var scheduleStarttime = $("#startDate").val();
				var scheduleEndtime = $("#endDate").val();
				var scheduleRemark = $("#scheduleRemark").val();
				var scheduleStatus = "01";
				
				var attDeptCode1 = "";
				var attSabun1 = "";
				$('input[name^="deptCode1"]').each(function() {
					attDeptCode1 += $(this).val() + ",";
				});
				$('input[name^="sabun1"]').each(function() {
					attSabun1 += $(this).val() + ",";
				});
				
				if (scheduleType == "") {
					alert("종류를 입력해 주세요.");
					return;
				}
				if (scheduleName == "") {
					alert("목적을 입력해 주세요.");
					return;
				}
				if (scheduleStarttime == "" || scheduleEndtime == "") {
					alert("일시를 입력해 주세요.");
					return;
				}
				if (attDeptCode1 == "" || attSabun1 == "") {
					alert("대상 직원을 입력해 주세요.");
					return;
				}
				
				if(confirm("수정하시겠습니까?")){
					var params = {
						auth : "reyon",
						scheduleSeq : scheduleSeq,
						scheduleType : scheduleType,
						scheduleName : scheduleName,
						scheduleStarttime : scheduleStarttime,
						scheduleEndtime : scheduleEndtime,
						scheduleRemark : scheduleRemark,
						scheduleStatus : scheduleStatus,
						attDeptCode1 : attDeptCode1,
						attSabun1 : attSabun1,
					}
					
					var request = $.ajax({
						url: '/schedule/scheduleModifyAjax.json'
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
							if (json.resultCode == 1) {
								alert("저장 완료 되었습니다.");
								var url = "/schedule/scheduleView.do?scheduleSeq="+scheduleSeq;
								var queStr = $("#queStr").val();
								if(queStr != ""){
									url += "&queStr=" + encodeURIComponent(queStr);
								}
								location.href = url;
							} else if (json.resultCode == 1201) {
								alert(json.resultMsg);
								cfLogin();
							} else {
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
								<i class="fa fa-pencil"></i> 일정 등록
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-flask"></i>일정 관리</li>
								<li><i class="fa fa-pencil"></i>일정 등록</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">일정 상세 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">종류</label>
											<div class="col-lg-10">
												<select id="scheduleType" class="form-control">
													<option value="10">휴가</option>
													<option value="20">출장</option>
													<option value="30">교육</option>
													<option value="40">외근</option>
													<option value="50">기타</option>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">목적</label>
											<div class="col-lg-10">
												<input type="text" id="scheduleName" class="form-control" maxlength="50">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">일시</label>
											<div class="col-lg-8">
												<div class="row">
													<div class="col-lg-5">
														<div class="input-group date" id="startDatetime">
										                    <input type="text" id="startDate" class="form-control" />
										                    <span class="input-group-addon">
										                        <span class="glyphicon glyphicon-calendar"></span>
										                    </span>
										                </div>
													</div>
													<div class="col-lg-1 text-center">~</div>
													<div class="col-lg-5">
														<div class="input-group date" id="endDatetime">
										                    <input type="text" id="endDate" class="form-control" />
										                    <span class="input-group-addon">
										                        <span class="glyphicon glyphicon-calendar"></span>
										                    </span>
										                </div>
													</div>
												</div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">상세 내용</label>
											<div class="col-lg-10">
												<input type="text" id="scheduleRemark" class="form-control" maxlength="100">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">대상 직원</label>
											<div class="col-lg-10">
												<input type="text" id="attendee1" class="form-control" maxlength="10">
												<table class="table table-bordered table-hover personal-task">
													<colgroup>
														<col style="width:35%" />
														<col style="width:35%" />
														<col style="width:30%" />
													</colgroup>
													<thead>
														<tr>
															<th class="text-center">부서</th>
															<th class="text-center">이름</th>
															<th class="text-center">대상 직원 삭제</th>
														</tr>
													</thead>
													<tbody id="attendee1Tbody">
														<c:choose>
															<c:when test="${fn:length(attList1) == 0}">
																<tr id="attendee1Tr0"><td colspan="3">대상 직원을 추가해 주세요.</td></tr>
															</c:when>
															<c:otherwise>
																<c:forEach var="result" items="${attList1}">
																	<tr id="attendee1Tr${result.attendantSabun}">
																		<td>${result.attendantDeptName}<input type="hidden" name="deptCode1[]" value="${result.attendantDept}"></td>
																		<td>${result.attendantName}<input type="hidden" name="sabun1[]" value="${result.attendantSabun}"></td>
																		<td><input type="button" class="btn btn-danger btn-sm" value="삭제" onClick="javascript:delAttendee1Tr('${result.attendantSabun}');" /></td>
																	</tr>
																</c:forEach>
															</c:otherwise>
														</c:choose>
													</tbody>
												</table>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">부서</label>
											<div class="col-lg-10">
												<p class="form-control-static"><sec:authentication property="principal.deptName" /></p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">수정인</label>
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
						<input type="hidden" id="scheduleSeq" value="${info.scheduleSeq }" />
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