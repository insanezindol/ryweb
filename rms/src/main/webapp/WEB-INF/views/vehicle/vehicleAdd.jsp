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
		<!-- summernote editor -->
		<link href="/css/summernote.css" rel="stylesheet">
		<script type="text/javascript" src="/js/summernote.js"></script>
		<script type="text/javascript" src="/js/summernote-ko-KR.js"></script>
		<!-- page script -->
		<script type="text/javascript">
		
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu08");
				
				// datetimepicker
				$('#rentStartDatetime').datetimepicker({
					format: 'YYYY-MM-DD',
					sideBySide: true,
				});
				$('#rentEndDatetime').datetimepicker({
					format: 'YYYY-MM-DD',
					sideBySide: true,
					useCurrent: false,
				});
				$("#rentStartDatetime").on("dp.change", function (e) {
		            $('#rentEndDatetime').data("DateTimePicker").minDate(e.date);
		        });
		        $("#rentEndDatetime").on("dp.change", function (e) {
		            $('#rentStartDatetime').data("DateTimePicker").maxDate(e.date);
		        });
		        
		        $('#insuranceStartDatetime').datetimepicker({
					format: 'YYYY-MM-DD',
					sideBySide: true,
				});
				$('#insuranceEndDatetime').datetimepicker({
					format: 'YYYY-MM-DD',
					sideBySide: true,
					useCurrent: false,
				});
				$("#insuranceStartDatetime").on("dp.change", function (e) {
		            $('#insuranceEndDatetime').data("DateTimePicker").minDate(e.date);
		        });
		        $("#insuranceEndDatetime").on("dp.change", function (e) {
		            $('#insuranceStartDatetime').data("DateTimePicker").maxDate(e.date);
		        });
		        
				// 용도 구분 변경 시
				$("#division").change(function() { 
					var division = $(this).val();
					$("#officeDiv").hide();
					$("#personDiv").hide();
					if (division == "회사") {
						$("#officeDiv").show();
					} else {
						$("#personDiv").show();
					}
				});
				
				$("#officeDiv").change(function() { 
					var office = $(this).val();
					if(office != ""){
						$("#divisionText").text($("#division").val());
						$("#username").val(office);
						$("#userAddDiv").hide();
						$("#userRemoveDiv").show();
					}
				});
				
				// 참석자 검색
				getTotalUserListAjax();
				
				// 지급구분이 보유인 경우 계약종료일자는 2099년으로 세팅
				$("#payment").change(function() { 
					var payment = $(this).val();
					if(payment == "보유"){
						$("#rentEndDate").val("2099-12-31");
						$("#rentEndDate").prop("readonly", true);
					} else {
						$("#rentEndDate").val("");
						$("#rentEndDate").prop("readonly", false);
					}
				});
				
				// summernote editor loading
				$("#remarks").summernote({
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
										var name = $("#personDiv").getSelectedItemData().kname;
										var posLog = $("#personDiv").getSelectedItemData().posLog;
										$("#divisionText").text($("#division").val());
										$("#personDiv").val("");
										$("#username").val(name + " " + posLog);
										$("#userAddDiv").hide();
										$("#userRemoveDiv").show();
									},
								}
							};
							
							$("#personDiv").easyAutocomplete(options);
						} else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
						} else {
							alert(json.resultMsg);
						}
					}
				});
			}
			
			// 사용자 삭제
			function removeUser() {
				$("#username").val("");
				$("#userAddDiv").show();
				$("#userRemoveDiv").hide();
				$(".easy-autocomplete").css("width", "100%");
			}
			
			// 등록
			function goConfirm() {
				var vehicleNo = $("#vehicleNo").val();
				var vehicleType = $("#vehicleType").val();
				var division = $("#division").val();
				var username = $("#username").val();
				var payment = $("#payment").val();
				var rentStartDate = $("#rentStartDate").val();
				var rentEndDate = $("#rentEndDate").val();
				var rentMoney = $("#rentMoney").val();
				var insuranceStartDate = $("#insuranceStartDate").val();
				var insuranceEndDate = $("#insuranceEndDate").val();
				var insuranceMoney = $("#insuranceMoney").val();
				var status = $("#status").val();
				var remarks = $("#remarks").summernote("code");
				var documentFile = $("#documentFile")[0].files[0];
				
				if (vehicleNo == "") {
					alert("차량번호를 선택해 주세요.");
					return;
				}
				if (vehicleType == "") {
					alert("차량종류를 선택해 주세요.");
					return;
				}
				if (division == "") {
					alert("분류를 선택해 주세요.");
					return;
				}
				if (username == "") {
					alert("사용자를 입력해 주세요.");
					return;
				}
				if (payment == "") {
					alert("지급구분을 입력해 주세요.");
					return;
				}
				if (rentStartDate == "" || rentEndDate == "") {
					alert("렌트기간/매입일을 입력해 주세요.");
					return;
				}
				if (status == "") {
					alert("상태를 입력해 주세요.");
					return;
				}
				if(cfGetByteLength(remarks) > 4000) {
					alert("[비고] 항목의 최대 길이를 초과하였습니다.\n수정 후 다시 시도해주세요.\n- 최대길이 : 4000 byte\n- 현재길이 : " + cfGetByteLength(remarks) + " byte");
					return;
				}
				
				// 금액란이 비어있는경우 0으로 초기화
				if (rentMoney == "") {
					rentMoney = 0;
				}
				if (insuranceMoney == "") {
					insuranceMoney = 0;
				}
				
				if ($("#documentFile").get(0).files.length !== 0) {
					if($("#documentFile")[0].files[0].size/1024/1024 > 20){
						alert("첨부파일은 20MB를 초과 할 수 없습니다.");
						return;
					}
				}
				
				if (confirm("등록하시겠습니까?")) {
					var formData = new FormData();
					formData.append("auth", "reyon");
					formData.append("vehicleNo", vehicleNo);
					formData.append("vehicleType", vehicleType);
					formData.append("division", division);
					formData.append("username", username);
					formData.append("payment", payment);
					formData.append("rentStartDate", rentStartDate);
					formData.append("rentEndDate", rentEndDate);
					formData.append("rentMoney", rentMoney);
					formData.append("insuranceStartDate", insuranceStartDate);
					formData.append("insuranceEndDate", insuranceEndDate);
					formData.append("insuranceMoney", insuranceMoney);
					formData.append("status", status);
					formData.append("remarks", remarks);
					formData.append("documentFile", documentFile);
					
					var request = $.ajax({
						url: '/vehicle/vehicleAddAjax.json'
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
								location.href="/vehicle/vehicleList.do";
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
								<i class="fa fa-pencil"></i> 법인 차량 등록
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-car"></i>법인 차량 관리</li>
								<li><i class="fa fa-pencil"></i>법인 차량 등록</li>
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
												<input type="text" id="vehicleNo" class="form-control" maxlength="25">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">차량종류</label>
											<div class="col-lg-10">
												<input type="text" id="vehicleType" class="form-control" maxlength="25">
											</div>
										</div>
										<div class="form-group" id="userAddDiv">
											<label class="col-lg-2 control-label">분류 / 사용자</label>
											<div class="col-lg-2">
												<select id="division" class="form-control">
													<option value="개인">개인</option>
													<option value="회사">회사</option>
												</select>
											</div>
											<div class="col-lg-8">
												<input type="text" id="personDiv" class="form-control" maxlength="10" > 
												<select id="officeDiv" class="form-control" style="display:none;">
													<option value="">== 사용자 선택 ==</option>
													<option value="본사">본사</option>
													<option value="진천공장">진천공장</option>
													<option value="충주공장">충주공장</option>
												</select>
											</div>
										</div>
										<div class="form-group" id="userRemoveDiv" style="display: none;">
											<label class="col-lg-2 control-label">분류 / 사용자</label>
											<div class="col-lg-2">
												<p class="form-control-static" id="divisionText"></p>
											</div>
											<div class="col-lg-6">
												<input type="text" id="username" class="form-control" readonly>
											</div>
											<div class="col-lg-2">
												<a class="btn btn-default" href="javascript:;" title="변경하기" onClick="javascript:removeUser();"><span class="icon_close_alt2"></span>&nbsp;변경</a>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">지급구분</label>
											<div class="col-lg-10">
												<select id="payment" class="form-control">
													<option value="렌트">렌트</option>
													<option value="리스">리스</option>
													<option value="보유">보유</option>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">렌트기간/매입일</label>
											<div class="col-lg-10">
												<div class="row" style="margin-top: 7px;">
													<div class="col-lg-5">
														<div class="input-group date" id="rentStartDatetime">
										                    <input type="text" id="rentStartDate" class="form-control" />
										                    <span class="input-group-addon">
										                        <span class="glyphicon glyphicon-calendar"></span>
										                    </span>
										                </div>
													</div>
													<div class="col-lg-1 text-center">~</div>
													<div class="col-lg-5">
														<div class="input-group date" id="rentEndDatetime">
										                    <input type="text" id="rentEndDate" class="form-control" />
										                    <span class="input-group-addon">
										                        <span class="glyphicon glyphicon-calendar"></span>
										                    </span>
										                </div>
													</div>
												</div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">임차료/월 (보험료 포함)</label>
											<div class="col-lg-10">
												<input type="number" id="rentMoney" class="form-control">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">보험기간 (선택입력)</label>
											<div class="col-lg-10">
												<div class="row" style="margin-top: 7px;">
													<div class="col-lg-5">
														<div class="input-group date" id="insuranceStartDatetime">
										                    <input type="text" id="insuranceStartDate" class="form-control" />
										                    <span class="input-group-addon">
										                        <span class="glyphicon glyphicon-calendar"></span>
										                    </span>
										                </div>
													</div>
													<div class="col-lg-1 text-center">~</div>
													<div class="col-lg-5">
														<div class="input-group date" id="insuranceEndDatetime">
										                    <input type="text" id="insuranceEndDate" class="form-control" />
										                    <span class="input-group-addon">
										                        <span class="glyphicon glyphicon-calendar"></span>
										                    </span>
										                </div>
													</div>
												</div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">보험료 (선택입력)</label>
											<div class="col-lg-10">
												<input type="number" id="insuranceMoney" class="form-control">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">비고 (선택입력)</label>
											<div class="col-lg-10">
												<div id="remarks"></div> 
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">스캔파일 첨부파일 (선택입력)</label>
											<div class="col-lg-10">
												<p class="form-control-static"><input type="file" id="documentFile"></p>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">상태</label>
											<div class="col-lg-10">
												<select id="status" class="form-control">
													<option value="ING">진행중</option>
													<option value="END">종료</option>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">등록인</label>
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