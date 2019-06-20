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
		<link href="/css/jquery-ui-1.12.1.min.css" rel="stylesheet">
		<link href="/css/easy-autocomplete.min.css" rel="stylesheet">
		<!-- full calendar css-->
  		<link href="/css/fullcalendar.min.css?ver=20190619" rel="stylesheet" />
  		<link href="/css/fullcalendar.print.min.css?ver=20190619" rel="stylesheet" media="print" />
		<!-- javascripts -->
		<script type="text/javascript" src="/js/jquery-1.8.3.min.js"></script>
		<script type="text/javascript" src="/js/jquery-ui-1.12.1.min.js"></script>
		<!-- moment script -->
		<script type="text/javascript" src="/js/moment.js"></script>
		<script type="text/javascript" src="/js/moment-ko.js"></script>
		<!-- bootstrap -->
		<script type="text/javascript" src="/js/bootstrap.min.js"></script>
		<!-- full calendar script -->
		<script type="text/javascript" src="/js/fullcalendar.min.js?ver=20190619"></script>
		<script type="text/javascript" src="/js/fullcalendar-ko.js?ver=20190619"></script>
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
				cfLNBMenuSelect("menu04");
				
				// datetimepicker
				$("#meetingDatePicker").datetimepicker({
					format: 'YYYY-MM-DD',
					sideBySide: true,
				});
		        $("#startTimePicker").datetimepicker({
		        	format: 'HH:mm',
		        	stepping: 30,
		        	minDate: moment({h:7}),
		        	maxDate: moment({h:20}),
				});
		        $("#endTimePicker").datetimepicker({
		        	format: 'HH:mm',
		        	stepping: 30,
		        	minDate: moment({h:7}),
		        	maxDate: moment({h:20}),
				});
		        
		     	// set default value
		        $("#startTime").val("${fn:substring(info.meetingStartDate,11,16) }");
				$("#endTime").val("${fn:substring(info.meetingEndDate,11,16) }");
				if("${info.meetingStatus }" == "05") {
					$('input:checkbox[id="confirmType"]').attr("checked", true);
				}

		     	// datepicker change event
		        $("#meetingDatePicker").on("dp.change", function (e) {
		        	$("#meetingPlace").val("");
					$("#meetingPlaceText").text("미선택");
		        });
		        $("#startTimePicker").on("dp.change", function (e) {
		        	$("#meetingPlace").val("");
					$("#meetingPlaceText").text("미선택");
		        });
		        $("#endTimePicker").on("dp.change", function (e) {
		        	$("#meetingPlace").val("");
					$("#meetingPlaceText").text("미선택");
		        });
		        
		    	// 참석자 검색
				getTotalUserListAjax();
		     	
				// 장소 검색
		    	getCommonCodeListAjax();
				
				// 검색어 입력영역에 엔터키 이벤트 바인드
				$("#searchText").keydown(function (key) {
				    if (key.keyCode == 13) searchProjectList();
				});
			});
			
			// 프로젝트 리스트 모달팝업 오픈
			function openProjectList() {
				$("#projectSeq").val("");
				$("#projectName").val("");
		        getActiveProjectListAjax();
				$("#projectModal").modal();
			}
			
			// 프로젝트 리스트 모달팝업 검색
			function searchProjectList() {
				var searchText = $("#searchText").val();
				pageParam.searchText = searchText;
				getActiveProjectListAjax();
			}
			
			// 프로젝트 리스트 모달팝업 페이지 이동
			function goProjectList(no) {
				pageParam.pageNo = no;
				getActiveProjectListAjax();
			}
			
			// 검색된 프로젝트 이름 선택
			function choiceProject(projectSeq, projectName){
				$("#projectSeq").val(projectSeq);
				$("#projectName").val(projectName);
				$("#projectModal").modal("hide");
			}
			
			// 페이징 및 검색 파라메터 선언
			var pageParam = {
				pageNo : ""
				, pageSize : 15
				, searchType: "projectName"
				, searchText : ""
			}
			
			// 프로젝트 리스트 ajax
			function getActiveProjectListAjax() {
				var params = {
					auth : "reyon",
					pageNo : pageParam.pageNo,
					pageSize : pageParam.pageSize,
					s_projectName : pageParam.searchText,
				}
				
				var request = $.ajax({
					url: "/common/getActiveProjectListAjax.json"
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
							$("#projectTblTbody").empty();
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
									tag += '<td class="text-center">'+totalList[i].projectSeq+'</td>';
									tag += '<td class="text-center"><a href="javascript:choiceProject(\''+totalList[i].projectSeq+'\',\''+totalList[i].projectName+'\');">'+totalList[i].projectName+'</a></td>';
									tag += '<td class="text-center">'+totalList[i].deptName+'</td>';
									tag += '<td class="text-center">'+totalList[i].regName+'</td>';
									if(totalList[i].status == "01"){
										tag += '<td class="text-center">진행전</td>';
									} else if(totalList[i].status == "02"){
										tag += '<td class="text-center">진행중</td>';
									} else if(totalList[i].status == "03"){
										tag += '<td class="text-center">중지</td>';
									} else if(totalList[i].status == "04"){
										tag += '<td class="text-center">완료</td>';
									}
									tag += '</tr>';
								}
							}
							// 페이징 처리
							$('#pagingDiv').html(cfGetPagingHtml(pageParam.totalCount, pageParam.pageNo, pageParam.pageSize, 'goProjectList'));
							$("#projectTblTbody").append(tag);
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
										
										if ($("#attendee1Tr" + sabun).length > 0 || $("#attendee2Tr" + sabun).length > 0) {
											alert("이미 등록된 참석자/참고인 입니다.");
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
							
							var options2 = {
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
										var deptCode = $("#attendee2").getSelectedItemData().deptCode;
										var deptName = $("#attendee2").getSelectedItemData().deptName;
										var sabun = $("#attendee2").getSelectedItemData().sabun;
										var name = $("#attendee2").getSelectedItemData().kname;
										
										$("#attendee2").val("");
										
										if ($("#attendee1Tr" + sabun).length > 0 || $("#attendee2Tr" + sabun).length > 0) {
											alert("이미 등록된 참석자/참고인 입니다.");
										} else {
											if ($("#attendee2Tr0").length > 0) {
												$("#attendee2Tr0").remove();
											}
											var tag = '<tr id="attendee2Tr'+sabun+'">';
											tag += '<td>';
											tag += deptName;
											tag += '<input type="hidden" name="deptCode2[]" value="'+deptCode+'">';
											tag += '</td>';
											tag += '<td>';
											tag += name;
											tag += '<input type="hidden" name="sabun2[]" value="'+sabun+'">';
											tag += '</td>';
											tag += '<td>';
											tag += '<input type="button" class="btn btn-danger btn-sm" value="삭제" onClick="javascript:delAttendee2Tr(\'' + sabun + '\');" />';
											tag += '</td>';
											tag += '</tr>';
											$("#attendee2Tbody").append(tag);
										}
									},
								}
							};
							
							$("#attendee1").easyAutocomplete(options1);
							$("#attendee2").easyAutocomplete(options2);
						}else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
						}else{
							alert(json.resultMsg);
						}
					}
				});
			}
			
			// 장소 리스트 ajax
			function getCommonCodeListAjax() {
				var params = {
					auth : "reyon",
					grpCode : "AG",
				}
				
				var request = $.ajax({
					url: "/common/getCommonCodeListAjax.json"
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
							var totalList = json.resultMsg;
							var tag = '';
							for(var i=0; i<totalList.length; i++){
								tag += '<option value="'+totalList[i].code+'">'+totalList[i].codeName+'</option>';
							}
							$("#meetingPlace").append(tag);
						}else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
						}else{
							alert(json.resultMsg);
						}
					}
				});
			}
			
			// 예약 현황 조회
			function getTimetableListAjax() {
				var meetingSeq = $("#seq").val();
				var searchDate = $("#meetingDate").val();
				var meetingStartDate = $("#meetingDate").val() + " " + $("#startTime").val();
				var meetingEndDate = $("#meetingDate").val() + " " + $("#endTime").val();

				if ($("#meetingDate").val() == "") {
					alert("방문일자 입력해 주세요.");
					return;
				}
				if ($("#startTime").val() == "") {
					alert("시작시간을 입력해 주세요.");
					return;
				}
				if ($("#endTime").val() == "") {
					alert("종료시간을 입력해 주세요.");
					return;
				}
				if (meetingStartDate >= meetingEndDate) {
					alert("종료일시는 시작일시보다 미래시간으로 설정해야 합니다.");
					return;
				}
				
				var params = {
					auth : "reyon",
					meetingSeq : meetingSeq,
					searchDate : searchDate,
					meetingStartDate : meetingStartDate,
					meetingEndDate : meetingEndDate
				}

				var request = $.ajax({
					url : "/common/getTimetableListAjax.json",
					type : "POST",
					timeout : 10000,
					data : params,
					dataType : "json",
					beforeSend : function(xmlHttpRequest) {
						cfOpenMagnificPopup();
					},
					error : function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					},
					success : function(json) {
						if (json.resultCode == 0) {
							$("#meetingPlace").val("");
							$("#meetingPlaceText").text("");
							$("#placeTextArea").hide();
							$("#placeTimetableArea").show();
							var totalRoomlist = json.totalRoomlist;
							var availableRoomlist = json.availableRoomlist;
							var list = json.list;
							
							// 전체 회의실 리스트
							var totalRoomlistArr = [];
							for(var i=0; i<totalRoomlist.length; i++){
								var info = totalRoomlist[i];
								totalRoomlistArr.push(info.codeName);
							}

							// 타임테이블 설정
							var timetable = new Timetable();   
							timetable.setScope(7, 20);
							timetable.addLocations(totalRoomlistArr);
							
							// 예약가능한 리스트 설정
							for(var i=0; i<availableRoomlist.length; i++){
								var info = availableRoomlist[i];
								var options = {
									class: 'vip-only',
									onClick: function(event, timetable, clickEvent) {
										if(confirm("\"" + event.location + "\" 선택 하시겠습니까?")){
											for(var i=0; i<totalRoomlist.length; i++){
												var info = totalRoomlist[i];
												if(info.codeName == event.location){
													$("#meetingPlace").val(info.code);
													$("#meetingPlaceText").text(info.codeName);
													$("#placeTextArea").show();
													$("#placeTimetableArea").hide();
													break;
												}
											}
										}
									}  
								};
								timetable.addEvent('예약가능 (Click)', info.codeName, moment(meetingStartDate).toDate(), moment(meetingEndDate).toDate(), options);
							}
							
							// 예약된 리스트 설정
							for(var i=0; i<list.length; i++){
								var info = list[i];
								timetable.addEvent(info.title, info.type, moment(info.start).toDate(), moment(info.end).toDate());
							}
							
							// 타임테이블 렌더
							var renderer = new Timetable.Renderer(timetable);
							renderer.draw('#mainTimetable');
						} else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
						} else {
							alert(json.resultMsg);
							cfCloseMagnificPopup();
						}
					},
					complete : function() {
						cfCloseMagnificPopup();
					}
				});
			}
			
			// 참석자 삭제
			function delAttendee1Tr(name) {
				$("#attendee1Tr"+name).remove();
				
				if($("#attendee1Tbody tr").length < 1) {
					var tag = '<tr id="attendee1Tr0">';
					tag += '<td colspan="3">';
					tag += '참석자를 추가해 주세요.';
					tag += '</td>';
					tag += '</tr>';
					$("#attendee1Tbody").append(tag);
				}
			}
			
			// 참고인 삭제
			function delAttendee2Tr(name) {
				$("#attendee2Tr"+name).remove();
				
				if($("#attendee2Tbody tr").length < 1) {
					var tag = '<tr id="attendee2Tr0">';
					tag += '<td colspan="3">';
					tag += '참고인을 추가해 주세요.';
					tag += '</td>';
					tag += '</tr>';
					$("#attendee2Tbody").append(tag);
				}
			}
			
			// 등록
			function goConfirm() {
				var visitCompany = $("#visitCompany").val();
				var visitName = $("#visitName").val();
				var meetingSeq = $("#seq").val();
				var projectSeq = $("#projectSeq").val();
				var meetingName = $("#meetingName").val();
				var meetingStartDate = $("#meetingDate").val() + " " + $("#startTime").val();
				var meetingEndDate = $("#meetingDate").val() + " " + $("#endTime").val();
				var meetingPlace = $("#meetingPlace").val();
				var sendMailFlag = "off";
				if($('input:checkbox[id="sendMailFlag"]').is(":checked")){
					sendMailFlag = "on";
				}
				var meetingType = "02";
				var meetingStatus = "01";
				if($('input:checkbox[id="confirmType"]').is(":checked")){
					meetingStatus = "05";
				}
				var attDeptCode1 = "";
				var attSabun1 = "";
				var attDeptCode2 = "";
				var attSabun2 = "";
				$('input[name^="deptCode1"]').each(function() {
					attDeptCode1 += $(this).val() + ",";
				});
				$('input[name^="sabun1"]').each(function() {
					attSabun1 += $(this).val() + ",";
				});
				$('input[name^="deptCode2"]').each(function() {
					attDeptCode2 += $(this).val() + ",";
				});
				$('input[name^="sabun2"]').each(function() {
					attSabun2 += $(this).val() + ",";
				});
				
				if (visitCompany == "") {
					alert("방문 업체명을 입력해 주세요.");
					return;
				}
				
				if (visitName == "") {
					alert("방문자 성함을 입력해 주세요.");
					return;
				}
				
				if (meetingName == "") {
					alert("제목을 입력해 주세요.");
					return;
				}
				
				if ($("#meetingDate").val() == "") {
					alert("방문일자 입력해 주세요.");
					return;
				}
				
				if ($("#startTime").val() == "") {
					alert("시작시간을 입력해 주세요.");
					return;
				}
				
				if ($("#endTime").val() == "") {
					alert("종료시간을 입력해 주세요.");
					return;
				}
				
				if (meetingPlace == "") {
					alert("장소를 선택해 주세요.");
					return;
				}
				
				if (meetingStartDate >= meetingEndDate) {
					alert("종료일시는 시작일시보다 미래시간으로 설정해야 합니다.");
					return;
				}
				
				if(confirm("수정하시겠습니까?")){
					var params = {
						auth : "reyon",
						visitCompany : visitCompany,
						visitName : visitName,
						meetingSeq : meetingSeq,
						projectSeq : projectSeq,
						meetingName : meetingName,
						meetingStartDate : meetingStartDate,
						meetingEndDate : meetingEndDate,
						meetingPlace : meetingPlace,
						meetingType : meetingType,
						meetingStatus : meetingStatus,
						attDeptCode1 : attDeptCode1,
						attSabun1 : attSabun1,
						attDeptCode2 : attDeptCode2,
						attSabun2 : attSabun2,
						sendMailFlag : sendMailFlag,
					}
					
					var request = $.ajax({
						url: '/visitor/visitorModifyAjax.json'
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
								var url = "/visitor/visitorView.do?meetingSeq=" + meetingSeq;
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
								<i class="fa fa-pencil-square-o"></i> 방문자 수정
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-address-card-o"></i>방문자 관리</li>
								<li><i class="fa fa-pencil-square-o"></i>방문자 수정</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel">
								<header class="panel-heading">방문자 상세 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">방문 업체명</label>
											<div class="col-lg-10">
												<input type="text" id="visitCompany" class="form-control" maxlength="30" value="${info.visitCompany}">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">방문자 성함</label>
											<div class="col-lg-10">
												<input type="text" id="visitName" class="form-control" maxlength="70" value="${info.visitName}">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">프로젝트 이름</label>
											<div class="col-lg-10">
												<input type="text" id="projectName" class="form-control" onClick="javascript:openProjectList();" value="${info.projectName}" readonly>
												<input type="hidden" id="projectSeq" value="${info.projectSeq}" />
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">방문목적</label>
											<div class="col-lg-10">
												<input type="text" id="meetingName" class="form-control" maxlength="50" value="${info.meetingName }">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">방문일시</label>
											<div class="col-lg-3">
												<div class="input-group date" id="meetingDatePicker">
								                    <input type="text" id="meetingDate" class="form-control" placeholder="방문날짜" value="${fn:substring(info.meetingStartDate,0,10) }" />
								                    <span class="input-group-addon">
								                        <span class="glyphicon glyphicon-calendar"></span>
								                    </span>
								                </div>
											</div>
											<div class="col-lg-3">
												<div class="input-group date" id="startTimePicker">
								                    <input type="text" id="startTime" class="form-control" placeholder="시작시간" />
								                    <span class="input-group-addon">
								                        <span class="glyphicon glyphicon-time"></span>
								                    </span>
								                </div>
											</div>
											<div class="col-lg-3">
												<div class="input-group date" id="endTimePicker">
								                    <input type="text" id="endTime" class="form-control" placeholder="종료시간" />
								                    <span class="input-group-addon">
								                        <span class="glyphicon glyphicon-time"></span>
								                    </span>
								                </div>
											</div>
											<div class="col-lg-1">
												<a class="btn btn-default" href="javascript:;" title="조회하기" onClick="javascript:getTimetableListAjax();"><span class="icon_search"></span>&nbsp;조회</a>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">장소</label>
											<div class="col-lg-10" id="placeTextArea">
												<p class="form-control-static" id="meetingPlaceText">${info.codeName }</p>
												<input type="hidden" id="meetingPlace" value="${info.meetingPlace }" />
											</div>
											<div class="col-lg-10" id="placeTimetableArea" style="display: none;">
												<div id="mainTimetable" class="timetable"></div>
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
															<th class="text-center">참석자 삭제</th>
														</tr>
													</thead>
													<tbody id="attendee1Tbody">
														<c:choose>
															<c:when test="${fn:length(attList1) == 0}">
																<tr id="attendee1Tr0"><td colspan="3">참석자를 추가해 주세요.</td></tr>
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
											<label class="col-lg-2 control-label">참고인</label>
											<div class="col-lg-10">
												<input type="text" id="attendee2" class="form-control" maxlength="10">
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
															<th class="text-center">참석자 삭제</th>
														</tr>
													</thead>
													<tbody id="attendee2Tbody">
														<c:choose>
															<c:when test="${fn:length(attList2) == 0}">
																<tr id="attendee2Tr0"><td colspan="3">참고인을 추가해 주세요.</td></tr>
															</c:when>
															<c:otherwise>
																<c:forEach var="result" items="${attList2}">
																	<tr id="attendee2Tr${result.attendantSabun}">
																		<td>${result.attendantDeptName}<input type="hidden" name="deptCode2[]" value="${result.attendantDept}"></td>
																		<td>${result.attendantName}<input type="hidden" name="sabun2[]" value="${result.attendantSabun}"></td>
																		<td><input type="button" class="btn btn-danger btn-sm" value="삭제" onClick="javascript:delAttendee2Tr('${result.attendantSabun}');" /></td>
																	</tr>
																</c:forEach>
															</c:otherwise>
														</c:choose>
													</tbody>
												</table>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">결과 미등록 방문</label>
											<div class="col-lg-10">
												<div class="checkbox">
													<label><input type="checkbox" id="confirmType">별도의 방문 결과가 없는 경우 선택 (방문결과등록, 결재 없음)</label>
												</div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">알림 메일 발송</label>
											<div class="col-lg-10">
												<div class="checkbox">
													<label><input type="checkbox" id="sendMailFlag" checked>참석자, 참고인에게 회의 수정 안내 메일 발송 <span class="glyphicon glyphicon-info-sign icon_info" title="참석자와 참고인에게 수정한 기본정보를 메일로 발송합니다."></span></label>
												</div>
											</div>
										</div>
									</form>
								</div>
							</section>
						</div>
					</div>
					<!-- body end -->
					
					<!-- bottom button start -->
					<div class="btn-row text-right">
						<input type="hidden" id="seq" value="${info.meetingSeq }">
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
		
		<!-- modal popup start -->
		<div aria-hidden="true" aria-labelledby="projectModalLabel" role="dialog" tabindex="-1" id="projectModal" class="modal fade">
			<div class="modal-dialog" style="width: 70%;">
				<div class="modal-content">
					<div class="modal-header">
						<button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
						<h4 class="modal-title">프로젝트 선택</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal" role="form">
							<div class="form-group">
								<label for="searchText" class="col-lg-2 control-label">프로젝트 이름</label>
								<div class="col-lg-10">
									<div class="input-group">
										<input type="text" style="display: none;" />
					                    <input type="text" id="searchText" class="form-control" />
					                    <span class="input-group-addon" onClick="javascript:searchProjectList();">
					                        <span class="glyphicon glyphicon-search" style="cursor: pointer;"></span>
					                    </span>
					                </div>
								</div>
							</div>
						</form>
						<div style="height:20px;"></div>
						<table class="table table-bordered">
							<colgroup>
								<col style="width: 10%;">
								<col style="width: 45%;">
								<col style="width: 15%;">
								<col style="width: 15%;">
								<col style="width: 15%;">
							</colgroup>
							<thead>
								<tr>
									<th class="text-center">관리번호</th>
									<th class="text-center">프로젝트 이름</th>
									<th class="text-center">부서</th>
									<th class="text-center">담당자</th>
									<th class="text-center">상태</th>
								</tr>
							</thead>
							<tbody id="projectTblTbody">
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