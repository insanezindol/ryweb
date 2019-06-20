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
		
			var TYPE_FLAG = 0;
			var DEPT_TOTAL_LIST;
			
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu05");
				
				// plus minus counting button
				$('.btn-number').click(function(e){
				    e.preventDefault();
				    
				    fieldName = $(this).attr('data-field');
				    type      = $(this).attr('data-type');
				    var input = $("input[name='"+fieldName+"']");
				    var currentVal = parseInt(input.val());
				    if (!isNaN(currentVal)) {
				        if(type == 'minus') {
				            if(currentVal > input.attr('min')) {
				                input.val(currentVal - 1).change();
				            } 
				            if(parseInt(input.val()) == input.attr('min')) {
				                $(this).attr('disabled', true);
				            }
				        } else if(type == 'plus') {
				            if(currentVal < input.attr('max')) {
				                input.val(currentVal + 1).change();
				            }
				            if(parseInt(input.val()) == input.attr('max')) {
				                $(this).attr('disabled', true);
				            }
				        }
				    } else {
				        input.val(0);
				    }
				});
				
				$('.input-number').focusin(function(){
				   $(this).data('oldValue', $(this).val());
				});
				
				$('.input-number').change(function() {
				    minValue =  parseInt($(this).attr('min'));
				    maxValue =  parseInt($(this).attr('max'));
				    valueCurrent = parseInt($(this).val());
				    
				    name = $(this).attr('name');
				    if(valueCurrent >= minValue) {
				        $(".btn-number[data-type='minus'][data-field='"+name+"']").removeAttr('disabled')
				    } else {
				        alert('최소개수는 0개 입니다.');
				        $(this).val($(this).data('oldValue'));
				    }
				    if(valueCurrent <= maxValue) {
				        $(".btn-number[data-type='plus'][data-field='"+name+"']").removeAttr('disabled')
				    } else {
				        alert('최대개수는 999개 입니다.');
				        $(this).val($(this).data('oldValue'));
				    }
				});
				
				$(".input-number").keydown(function (e) {
			        if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 190]) !== -1 || (e.keyCode == 65 && e.ctrlKey === true) || (e.keyCode >= 35 && e.keyCode <= 39)) {
			                 return;
			        }
			        if ( (e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105) ) {
			            e.preventDefault();
			        }
			    });
				
				// 검색어 입력영역에 엔터키 이벤트 바인드
				$("#searchText").keydown(function (key) {
				    if (key.keyCode == 13) searchEvt();
				});
				
				// 지급 구분 변경 시
				$("#isWebSale").change(function() { 
					var isWebSale = $("#isWebSale").val();
					
					$("#cardBox").hide();
					$("#webSaleBox").hide();
					
					if(isWebSale == "N") {
						$("#cardBox").show();
						$("#webSalePrice").val("0");
					} else if(isWebSale == "Y") {
						$("#webSaleBox").show();
						$("#countHour2").val("0");
						$("#countHour3").val("0");
						$("#countHour4").val("0");
						$("#countHour6").val("0");
						$("#countHour24").val("0");
					}
				});
				
				// 부서 정보 가져오기
				getTotalDeptListAjax();
				
				// 기본 본인 부서 설정
				$("#refDeptCode").val("${deptCode }").prop("selected", true);
				
				// 지급구분에 따른 지급목록
				if($("#isWebSale").val() == "N") {
					$("#cardBox").show();
				} else if($("#isWebSale").val() == "Y") {
					$("#webSaleBox").show();
				}
				
				// GATEWAY 통해서 넘어왔을경우 설정
				if("${map.visitSeq }" != ""){
					$("#visitSeq").val("${map.visitSeq }");
				}
				if("${map.visitCompany }" != ""){
					$("#visitCompany").val("${map.visitCompany }");
				}
				if("${map.visitName }" != ""){
					$("#visitName").val("${map.visitName }");
				}
				if("${map.meetDeptCode }" != ""){
					$("#refDeptCode").val("${map.meetDeptCode }").prop("selected", true);
				}
			});
			
			// 부서 리스트 ajax
			function getTotalDeptListAjax() {
				var params = {
					auth : "reyon"
				}
				
				var request = $.ajax({
					url: "/common/getTotalDeptListAjax.json"
					, type : "POST"
					, timeout: 10000
					, data : params
					, dataType : "json"
					, async : false
					, error: function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					}
					, success : function(json) {
						if (json.resultCode == 0){
							DEPT_TOTAL_LIST = json.resultMsg;
							var totalList = json.resultMsg;
							var tag = '<option value="">== 부서 선택 ==</option>';
							for(var i=0; i<totalList.length; i++){
								tag += '<option value="'+totalList[i].deptCode+'">'+totalList[i].deptParcoName+' - '+totalList[i].deptName+'</option>';
							}
							$("#refDeptCode").append(tag);
						}else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
						}else{
							alert(json.resultMsg);
						}
					}
				});
			}
			
			// 검색 이벤트
			function searchEvt() {
				if (TYPE_FLAG == 1) {
					searchComingList();
		    	} else if (TYPE_FLAG == 2) { 
		    		searchVisitorList();		    		
		    	}
			}
			
			// 출입자 리스트 모달팝업 오픈
			function openComingList() {
				TYPE_FLAG = 1;
				pageParam.pageNo = "";
				pageParam.searchText = "";
				$("#searchText").val("");
				
				$("#visitSeq").val("");
				$("#visitCompany").val("");
				$("#visitName").val("");
				$("#refDeptCode").val("");
				getActiveTicketListAjax();
				$("#meetingModal").modal();
			}
			
			// 방문자 리스트 모달팝업 오픈
			function openVisitorList() {
				TYPE_FLAG = 2;
				pageParam.pageNo = "";
				pageParam.searchText = "";
				$("#searchText").val("");
				
				$("#visitPurpose").val("");
				getActiveVisitorListAjax();
				$("#meetingModal").modal();
			}
			
			// 출입자 리스트 모달팝업 검색
			function searchComingList() {
				var searchText = $("#searchText").val();
				pageParam.searchText = searchText;
				getActiveTicketListAjax();
			}
			
			// 방문자 리스트 모달팝업 검색
			function searchVisitorList() {
				var searchText = $("#searchText").val();
				pageParam.searchText = searchText;
				getActiveVisitorListAjax();
			}
			
			// 출입자 리스트 모달팝업 페이지 이동
			function goComingList(no) {
				pageParam.pageNo = no;
				getActiveTicketListAjax();
			}
			
			// 방문자 리스트 모달팝업 페이지 이동
			function goVisitorList(no) {
				pageParam.pageNo = no;
				getActiveVisitorListAjax();
			}
			
			// 검색된 출입자 이름 선택
			function choiceComing(visitSeq, visitCompany, visitName, meetDeptCode, meetDeptName, visitStartDate){
				$("#visitSeq").val(visitSeq);
				$("#visitCompany").val(visitCompany);
				$("#visitName").val(visitName);
				$("#refDeptCode").val(meetDeptCode).prop("selected", true);
				$("#meetingModal").modal("hide");
			}
			
			// 검색된 방문목적 선택
			function choiceVisitor(meetingName){
				$("#visitPurpose").val(meetingName);
				$("#meetingModal").modal("hide");
			}
			
			// 페이징 및 검색 파라메터 선언
			var pageParam = {
				pageNo : ""
				, pageSize : 15
				, searchType: "title"
				, searchText : ""
			}
			
			// 출입자 리스트 ajax
			function getActiveTicketListAjax() {
				$("#comingTbl").hide();
				$("#visitorTbl").hide();
				var params = {
					auth : "reyon",
					pageNo : pageParam.pageNo,
					pageSize : pageParam.pageSize,
					searchType : pageParam.searchType,
					searchText : pageParam.searchText,
				}
				
				var request = $.ajax({
					url: "/common/getActiveTicketListAjax.json"
					, type : "POST"
					, timeout: 10000
					, data : params
					, dataType : "json"
					, beforeSend: function(xmlHttpRequest) {
						cfOpenMagnificPopup();
					}
					, error: function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					}
					, success : function(json) {
						if (json.resultCode == 0){
							$("#comingTblTbody").empty();
							var totalList = json.list;
							var pageParam = json.pageParam;
							var tag = '';
							if (totalList.length == 0) {
								tag += '<tr>';
								tag += '<td colspan="4" class="text-center">결과가 없습니다.</td>';
								tag += '</tr>';
							} else {
								for(var i=0; i<totalList.length; i++){
									tag += '<tr>';
									tag += '<td class="text-center">'+totalList[i].visitCompany+'</td>';
									tag += '<td class="text-center"><a href="javascript:choiceComing(\''+totalList[i].visitSeq+'\',\''+totalList[i].visitCompany+'\',\''+totalList[i].visitName+'\',\''+totalList[i].meetDeptCode+'\',\''+totalList[i].meetDeptName+'\',\''+totalList[i].visitStartDate+'\');">'+totalList[i].visitName+'</a></td>';
									tag += '<td class="text-center">'+totalList[i].visitStartDate+'</td>';
									tag += '<td class="text-center">'+totalList[i].meetDeptName+'('+totalList[i].meetName+')</td>';
									tag += '</tr>';
								}
							}
							// 페이징 처리
							$('#pagingDiv').html(cfGetPagingHtml(pageParam.totalCount, pageParam.pageNo, pageParam.pageSize, 'goComingList'));
							$("#comingTblTbody").append(tag);
							$("#comingTbl").show();
							cfCloseMagnificPopup();
						}else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
							cfCloseMagnificPopup();
						}else{
							alert(json.resultMsg);
							cfCloseMagnificPopup();
						}
					}
				});
			}
			
			// 방문자 리스트 ajax
			function getActiveVisitorListAjax() {
				$("#comingTbl").hide();
				$("#visitorTbl").hide();
				var params = {
					auth : "reyon",
					pageNo : pageParam.pageNo,
					pageSize : pageParam.pageSize,
					searchType : pageParam.searchType,
					searchText : pageParam.searchText,
				}
				
				var request = $.ajax({
					url: "/common/getActiveVisitorListAjax.json"
					, type : "POST"
					, timeout: 10000
					, data : params
					, dataType : "json"
					, beforeSend: function(xmlHttpRequest) {
						cfOpenMagnificPopup();
					}
					, error: function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					}
					, success : function(json) {
						if (json.resultCode == 0){
							$("#visitorTblTbody").empty();
							var totalList = json.list;
							var pageParam = json.pageParam;
							var tag = '';
							if (totalList.length == 0) {
								tag += '<tr>';
								tag += '<td colspan="5" class="text-center">결과가 없습니다.</td>';
								tag += '</tr>';
							} else {
								for(var i=0; i<totalList.length; i++){
									tag += '<tr>';
									tag += '<td class="text-center">'+totalList[i].visitCompany+'</td>';
									tag += '<td class="text-center">'+totalList[i].visitName+'</td>';
									tag += '<td class="text-center"><a href="javascript:choiceVisitor(\''+totalList[i].meetingName+'\');">'+totalList[i].meetingName+'</a></td>';
									tag += '<td class="text-center">'+totalList[i].meetingStartDate+'~'+totalList[i].meetingEndDate+'</td>';
									tag += '<td class="text-center">'+totalList[i].deptName+'('+totalList[i].regName+')</td>';
									tag += '</tr>';
								}
							}
							// 페이징 처리
							$('#pagingDiv').html(cfGetPagingHtml(pageParam.totalCount, pageParam.pageNo, pageParam.pageSize, 'goVisitorList'));
							$("#visitorTblTbody").append(tag);
							$("#visitorTbl").show();
							cfCloseMagnificPopup();
						}else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
							cfCloseMagnificPopup();
						}else{
							alert(json.resultMsg);
							cfCloseMagnificPopup();
						}
					}
				});
			}
			
			// 등록
			function goConfirm() {
				var visitSeq = $("#visitSeq").val();
				var visitCompany = $("#visitCompany").val();
				var visitName = $("#visitName").val();
				var visitPurpose = $("#visitPurpose").val();
				var refDeptCode = $("#refDeptCode").val();
				var refDeptName = "";
				for(var i=0; i<DEPT_TOTAL_LIST.length; i++){
					if (DEPT_TOTAL_LIST[i].deptCode == refDeptCode) {
						refDeptName = DEPT_TOTAL_LIST[i].deptName;
						break;
					}
				}
				var countHour2 = $("#countHour2").val();
				var countHour3 = $("#countHour3").val();
				var countHour4 = $("#countHour4").val();
				var countHour6 = $("#countHour6").val();
				var countHour24 = $("#countHour24").val();
				var isWebSale = $("#isWebSale").val();
				var webSalePrice = $("#webSalePrice").val();
				
				if (visitCompany == "") {
					alert("방문 업체명(본부명)을 입력해 주세요.");
					return;
				}
				if (visitName == "") {
					alert("방문자 성함(사용자 성함)을 입력해 주세요.");
					return;
				}
				if (visitPurpose == "") {
					alert("방문목적을 입력해 주세요.");
					return;
				}
				if (refDeptCode == "" || refDeptName == "") {
					alert("담당부서를 입력해 주세요.");
					return;
				}
				if(isWebSale == "N") {
					if (countHour2 + countHour3 + countHour4 + countHour6 + countHour24 == 0) {
						alert("주차권 수량은 1장이상 입력해 주세요.");
						return;
					}
				}
				
				if(confirm("등록하시겠습니까?")){
					var params = {
						auth : "reyon",
						visitSeq : visitSeq,
						visitCompany : visitCompany,
						visitName : visitName,
						visitPurpose : visitPurpose,
						refDeptCode : refDeptCode,
						refDeptName : refDeptName,
						countHour2 : countHour2,
						countHour3 : countHour3,
						countHour4 : countHour4,
						countHour6 : countHour6,
						countHour24 : countHour24,
						isWebSale : isWebSale,
						webSalePrice : webSalePrice,
					}
					
					var request = $.ajax({
						url: '/ticket/ticketAddAjax.json'
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
								location.href="/ticket/ticketList.do";
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
								<i class="fa fa-pencil"></i> 주차권 지급 등록
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-car"></i>주차권 관리</li>
								<li><i class="fa fa-pencil"></i>주차권 지급 등록</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">주차권 지급 상세 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<c:if test="${isGeneralMagager }">
										<div class="form-group">
											<label class="col-lg-2 control-label">출입자 선택</label>
											<div class="col-lg-10">
												<input type="button" class="btn btn-default" onClick="javascript:openComingList();" value="출입자 정보 선택" />
												<input type="button" class="btn btn-default" onClick="javascript:openVisitorList();" value="방문 목적 선택" />
											</div>
										</div>
										</c:if>
										<div class="form-group">
											<label class="col-lg-2 control-label">방문 업체명(본부명)</label>
											<div class="col-lg-10">
												<input type="hidden" id="visitSeq" />
												<input type="text" id="visitCompany" class="form-control" maxlength="30">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">방문자 성함(사용자 성함)</label>
											<div class="col-lg-10">
												<input type="text" id="visitName" class="form-control" maxlength="70">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">방문목적</label>
											<div class="col-lg-10">
												<input type="text" id="visitPurpose" class="form-control" maxlength="50">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">담당부서</label>
											<div class="col-lg-10">
												<select id="refDeptCode" class="form-control" style="margin-top: 7px; margin-bottom: 10px;"></select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">지급구분</label>
											<div class="col-lg-10">
												<select id="isWebSale" class="form-control" style="margin-top: 7px; margin-bottom: 10px;">
													<option value="N">주차권 카드</option>
													<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SUPERUSER','ROLE_ITT','ROLE_CHONGMU')"><option value="Y">KT&G 웹할인</option></sec:authorize>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">지급목록</label>
											<div class="col-lg-10">
												<div class="row" id="cardBox" style="display:none;">
													<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
														<table class="table table-bordered">
															<tbody>
																<tr>
																	<th class="text-center">항목</th>
																	<th class="text-center">수량</th>
																</tr>
																<tr>
																	<td class="text-center">2시간 이용권</td>
																	<td class="text-center">
																		<div class="input-group">
																			<span class="input-group-btn">
																				<button type="button" class="btn btn-default btn-number" disabled="disabled" data-type="minus" data-field="quant[1]" style="padding: 9px 12px;">
																					<span class="glyphicon glyphicon-minus"></span>
																				</button>
																			</span> <input type="text" name="quant[1]" id="countHour2" class="form-control input-number" value="0" min="0" max="999" style="height: 40px;"> <span class="input-group-btn">
																				<button type="button" class="btn btn-default btn-number" data-type="plus" data-field="quant[1]" style="padding: 9px 12px;">
																					<span class="glyphicon glyphicon-plus"></span>
																				</button>
																			</span>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td class="text-center">3시간 이용권</td>
																	<td class="text-center">
																		<div class="input-group">
																			<span class="input-group-btn">
																				<button type="button" class="btn btn-default btn-number" disabled="disabled" data-type="minus" data-field="quant[2]" style="padding: 9px 12px;">
																					<span class="glyphicon glyphicon-minus"></span>
																				</button>
																			</span> <input type="text" name="quant[2]" id="countHour3" class="form-control input-number" value="0" min="0" max="999" style="height: 40px;"> <span class="input-group-btn">
																				<button type="button" class="btn btn-default btn-number" data-type="plus" data-field="quant[2]" style="padding: 9px 12px;">
																					<span class="glyphicon glyphicon-plus"></span>
																				</button>
																			</span>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td class="text-center">4시간 이용권</td>
																	<td class="text-center">
																		<div class="input-group">
																			<span class="input-group-btn">
																				<button type="button" class="btn btn-default btn-number" disabled="disabled" data-type="minus" data-field="quant[3]" style="padding: 9px 12px;">
																					<span class="glyphicon glyphicon-minus"></span>
																				</button>
																			</span> <input type="text" name="quant[3]" id="countHour4" class="form-control input-number" value="0" min="0" max="999" style="height: 40px;"> <span class="input-group-btn">
																				<button type="button" class="btn btn-default btn-number" data-type="plus" data-field="quant[3]" style="padding: 9px 12px;">
																					<span class="glyphicon glyphicon-plus"></span>
																				</button>
																			</span>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td class="text-center"><span style="text-decoration:line-through;">6시간 이용권</span><br><span style="color: red;">(2019년 05월 종료) <span class="glyphicon glyphicon-info-sign icon_info" title="2019년 5월부로 KT&G 주차권(6시간 이용권) 생산 중단"></span></span></td>
																	<td class="text-center">
																		<div class="input-group">
																			<span class="input-group-btn">
																				<button type="button" class="btn btn-default btn-number" disabled="disabled" data-type="minus" data-field="quant[4]" style="padding: 9px 12px;" disabled>
																					<span class="glyphicon glyphicon-minus"></span> =
																				</button>
																			</span> <input type="text" name="quant[4]" id="countHour6" class="form-control input-number" value="0" min="0" max="999" style="height: 40px;" disabled> <span class="input-group-btn">
																				<button type="button" class="btn btn-default btn-number" data-type="plus" data-field="quant[4]" style="padding: 9px 12px;" disabled>
																					<span class="glyphicon glyphicon-plus"></span>
																				</button>
																			</span>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td class="text-center">종일권</td>
																	<td class="text-center">
																		<div class="input-group">
																			<span class="input-group-btn">
																				<button type="button" class="btn btn-default btn-number" disabled="disabled" data-type="minus" data-field="quant[7]" style="padding: 9px 12px;">
																					<span class="glyphicon glyphicon-minus"></span>
																				</button>
																			</span> <input type="text" name="quant[7]" id="countHour24" class="form-control input-number" value="0" min="0" max="999" style="height: 40px;"> <span class="input-group-btn">
																				<button type="button" class="btn btn-default btn-number" data-type="plus" data-field="quant[7]" style="padding: 9px 12px;">
																					<span class="glyphicon glyphicon-plus"></span>
																				</button>
																			</span>
																		</div>
																	</td>
																</tr>
															</tbody>
														</table>
													</div>
												</div>
												<div class="row" id="webSaleBox" style="display:none;">
													<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
														<table class="table table-bordered">
															<tbody>
																<tr>
																	<th class="text-center">항목</th>
																	<th class="text-center">금액</th>
																</tr>
																<tr>
																	<td class="text-center">KT&G 웹할인</td>
																	<td class="text-center">
																		<input type="number" name="webSalePrice" id="webSalePrice" class="form-control" value="0">
																	</td>
																</tr>
															</tbody>
														</table>
													</div>
												</div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">등록부서</label>
											<div class="col-lg-10">
												<p class="form-control-static"><sec:authentication property="principal.deptName" /></p>
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
		
		<!-- modal popup start -->
		<div aria-hidden="true" aria-labelledby="meetingModalLabel" role="dialog" tabindex="-1" id="meetingModal" class="modal fade">
			<div class="modal-dialog" style="width: 70%;">
				<div class="modal-content">
					<div class="modal-header">
						<button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
						<h4 class="modal-title">정보 선택</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal" role="form">
							<div class="form-group">
								<div class="col-lg-12">
									<div class="input-group">
										<input type="text" style="display: none;" />
					                    <input type="text" id="searchText" class="form-control" placeholder="방문 업체명 or 방문자 성함 or 접견 부서 or 접견인" />
					                    <span class="input-group-addon" onClick="javascript:searchEvt();">
					                        <span class="glyphicon glyphicon-search" style="cursor: pointer;"></span>
					                    </span>
					                </div>
								</div>
							</div>
						</form>
						<div style="height:20px;"></div>
						<table class="table table-bordered" id="comingTbl" style="display: none;">
							<colgroup>
								<col style="width: 25%;">
								<col style="width: 25%;">
								<col style="width: 25%;">
								<col style="width: 25%;">
							</colgroup>
							<thead>
								<tr>
									<th class="text-center">방문업체명</th>
									<th class="text-center">방문자성함</th>
									<th class="text-center">방문일시</th>
									<th class="text-center">접견인</th>
								</tr>
							</thead>
							<tbody id="comingTblTbody">
							</tbody>
						</table>
						<table class="table table-bordered" id="visitorTbl" style="display: none;">
							<colgroup>
								<col style="width: 20%;">
								<col style="width: 20%;">
								<col style="width: 20%;">
								<col style="width: 20%;">
								<col style="width: 20%;">
							</colgroup>
							<thead>
								<tr>
									<th class="text-center">방문업체명</th>
									<th class="text-center">방문자성함</th>
									<th class="text-center">방문목적</th>
									<th class="text-center">방문일시</th>
									<th class="text-center">접견인</th>
								</tr>
							</thead>
							<tbody id="visitorTblTbody">
							</tbody>
						</table>
						<!-- page navigation start -->
						<div class="btn-row text-center" id="pagingDiv"></div>
						<!-- page navigation end -->
					</div>
				</div>
			</div>
		</div>
		<!-- modal popup end -->
		
		<!--loading Popup start-->
		<%@ include file="/WEB-INF/views/include/loading.jsp"%>
		<!--loading Popup end--> 
		
	</body>
</html>