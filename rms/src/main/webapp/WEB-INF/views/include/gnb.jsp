<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<sec:authentication property="principal.saupcode" var="principalSaupcode" />
<sec:authentication property="principal.deptCode" var="principalDeptCode" />
<sec:authentication property="principal.username" var="principalUsername" />
<sec:authentication property="principal.userRoleInfo" var="principalUserRoleInfo" />
<sec:authentication property="principal.capsUserRole" var="principalCapsUserRole" />
<sec:authentication property="principal.capsAdminRole" var="principalCapsAdminRole" />
<link href="/css/timetablejs.css" rel="stylesheet">
<link href="/css/bootstrap-datetimepicker.min.css" rel="stylesheet">
<link href="/css/weather-icons.css" rel="stylesheet">
<script type="text/javascript" src="/js/timetable.js"></script>
<script type="text/javascript" src="/js/bootstrap-datetimepicker.min.js"></script>

<script type="text/javascript">

	$(function() {
		// todo list
		getTodoListAjax();
		
		// datetimepicker
	    $("#timetableDatePicker").datetimepicker({
			format: "YYYY-MM-DD",
			sideBySide: true,
		});
		
		// set date
	    var today = new Date();
	    var dd = today.getDate();
	    var mm = today.getMonth()+1;
	    var yyyy = today.getFullYear();
	    if(dd<10){ dd='0'+dd; } 
	    if(mm<10){ mm='0'+mm; } 
	    var today = yyyy + "-" + mm + '-' + dd;
	    $("#timetableDate").val(today);
	    
	    // saupcode
	    var saupcode = getCookie("saupcode");
	    if("${principalUserRoleInfo}" == "ROLE_SUPERUSER" || "${principalUserRoleInfo}" == "ROLE_ADMIN"){
	    	if (saupcode != "") {
		    	selectSaup(saupcode);
		    } else {
		    	selectSaup("00");
		    }
	    }
	});
	
	// 진행 대기 내역 카운트 ajax
	function getTodoListAjax() {
		var params = {
			auth : "reyon"
		}
		
		var request = $.ajax({
			url: "/common/getTodoListAjax.json"
			, type : "POST"
			, timeout: 10000
			, data : params
			, dataType : "json"
			, error: function(xhr, textStatus, errorThrown) {
				alert("시스템 오류가 발생했습니다.");
			}
			, success : function(json) {
				if (json.resultCode == 0) {
					$("#notRegisterCntSpan").html(json.notRegisterCnt + "건");
					$("#notConfirmCntSpan").html(json.notConfirmCnt + "건");
					$("#notReturnCntSpan").html(json.notReturnCnt + "건");
					$("#notSumCntSpan").html(json.notSumCnt);
					
					if(json.notSumCnt != "0"){
						if (window.location.pathname.indexOf("main.do") > -1) {
							$("#notRegisterCntMainSpan").html(json.notRegisterCnt + "건");
							$("#notConfirmCntMainSpan").html(json.notConfirmCnt + "건");
							$("#notReturnCntMainSpan").html(json.notReturnCnt + "건");
							$("#mainModal").modal();
						}
					}
				} else {
					$("#notRegisterCntSpan").html("0건");
					$("#notConfirmCntSpan").html("0건");
					$("#notReturnCntSpan").html("0건");
					$("#notSumCntSpan").html("0");
				}
			}
		});
	}
	
	// 실시간 예약 현황
	function gnbOpenTimetable() {
		$("#gnbTimetableModal").modal();
		setTimeout(function(){ gnbSearchTimeTable(); }, 500);
	}
	
	// 예약 현황 조회
	function gnbSearchTimeTable() {
		var searchDate = $("#timetableDate").val();

		if ($("#timetableDate").val() == "") {
			alert("조회 일자를 입력해 주세요.");
			return;
		}
		
		var params = {
			auth : "reyon",
			searchDate : searchDate
		}

		var request = $.ajax({
			url : "/common/getGnbTimetableListAjax.json",
			type : "POST",
			timeout : 10000,
			data : params,
			dataType : "json",
			error : function(xhr, textStatus, errorThrown) {
				alert("시스템 오류가 발생했습니다.");
			},
			success : function(json) {
				if (json.resultCode == 0) {
					// 시간표 보여주기
					var totalRoomlist = json.totalRoomlist;
					var list = json.list;
					
					// 전체 회의실 리스트
					var totalRoomlistArr = [];
					for(var i=0; i<totalRoomlist.length; i++){
						var info = totalRoomlist[i];
						if(info.codeName != "미사용"){
							totalRoomlistArr.push(info.codeName);
						}
					}

					// 타임테이블 설정
					var timetable = new Timetable();   
					timetable.setScope(7, 20);
					timetable.addLocations(totalRoomlistArr);
					
					// 예약된 리스트 설정
					for(var i=0; i<list.length; i++){
						var info = list[i];
						if(info.type != "미사용"){
							timetable.addEvent(info.title, info.type, moment(info.start).toDate(), moment(info.end).toDate());
						}
					}
					
					// 타임테이블 렌더
					var renderer = new Timetable.Renderer(timetable);
					renderer.draw('#gnbTimetable');
				} else if (json.resultCode == 1201) {
					alert(json.resultMsg);
					cfLogin();
				} else {
					alert(json.resultMsg);
				}
			}
		});
	}
	
	// 사업코드 변경 버튼 이벤트
	function selectSaup(saupcode) {
		if(saupcode == "10") {
			deleteCookie("saupcode");
			$("#saupBtn00").attr("class", "btn btn-default");
			$("#saupBtn10").attr("class", "btn btn-custom");
			$("#saupBtn20").attr("class", "btn btn-default");
			$("#saupBtn30").attr("class", "btn btn-default");
			$("#saupBtn40").attr("class", "btn btn-default");
			$("#saupBtn50").attr("class", "btn btn-default");
			setCookie("saupcode", saupcode, 1);
		} else if(saupcode == "20") {
			deleteCookie("saupcode");
			$("#saupBtn00").attr("class", "btn btn-default");
			$("#saupBtn10").attr("class", "btn btn-default");
			$("#saupBtn20").attr("class", "btn btn-custom");
			$("#saupBtn30").attr("class", "btn btn-default");
			$("#saupBtn40").attr("class", "btn btn-default");
			$("#saupBtn50").attr("class", "btn btn-default");
			setCookie("saupcode", saupcode, 1);
		} else if(saupcode == "30") {
			deleteCookie("saupcode");
			$("#saupBtn00").attr("class", "btn btn-default");
			$("#saupBtn10").attr("class", "btn btn-default");
			$("#saupBtn20").attr("class", "btn btn-default");
			$("#saupBtn30").attr("class", "btn btn-custom");
			$("#saupBtn40").attr("class", "btn btn-default");
			$("#saupBtn50").attr("class", "btn btn-default");
			setCookie("saupcode", saupcode, 1);
		} else if(saupcode == "40") {
			deleteCookie("saupcode");
			$("#saupBtn00").attr("class", "btn btn-default");
			$("#saupBtn10").attr("class", "btn btn-default");
			$("#saupBtn20").attr("class", "btn btn-default");
			$("#saupBtn30").attr("class", "btn btn-default");
			$("#saupBtn40").attr("class", "btn btn-custom");
			$("#saupBtn50").attr("class", "btn btn-default");
			setCookie("saupcode", saupcode, 1);
		} else if(saupcode == "50") {
			deleteCookie("saupcode");
			$("#saupBtn00").attr("class", "btn btn-default");
			$("#saupBtn10").attr("class", "btn btn-default");
			$("#saupBtn20").attr("class", "btn btn-default");
			$("#saupBtn30").attr("class", "btn btn-default");
			$("#saupBtn40").attr("class", "btn btn-default");
			$("#saupBtn50").attr("class", "btn btn-custom");
			setCookie("saupcode", saupcode, 1);
		} else {
			$("#saupBtn00").attr("class", "btn btn-custom");
			$("#saupBtn10").attr("class", "btn btn-default");
			$("#saupBtn20").attr("class", "btn btn-default");
			$("#saupBtn30").attr("class", "btn btn-default");
			$("#saupBtn40").attr("class", "btn btn-default");
			$("#saupBtn50").attr("class", "btn btn-default");
			deleteCookie("saupcode");
		}
	}

</script>

<header class="header header-bg">
	<div class="toggle-nav">
		<div class="icon-reorder tooltips" data-original-title="네비게이션" data-placement="bottom">
			<i class="icon_menu"></i>
		</div>
	</div>

	<!--logo start-->
	<a href="javascript:;" onClick="javascript:cfGoMain();" class="logo">이연제약 관리 시스템</a>
	<!--logo end-->

	<div class="top-nav notification-row">
		<!-- notificatoin dropdown start-->
		<ul class="nav pull-right top-menu">
			<!-- task reservation start -->
			<li id="task_reservation_bar" class="dropdown" title="실시간 예약 현황">
				<a href="javascript:;" onClick="javascript:gnbOpenTimetable();">
					<i class="icon_calendar"></i>
				</a>
			</li>
			<!-- task reservation end -->
			<!-- alert notification start-->
			<li id="alert_notificatoin_bar" class="dropdown" title="알람 내역 현황" style="left: -2px;">
				<a data-toggle="dropdown" class="dropdown-toggle" href="#">
					<i class="icon_info_alt"></i> <span class="badge bg-custom" id="notSumCntSpan"></span>
				</a>
				<ul class="dropdown-menu extended notification">
					<div class="notify-arrow notify-arrow-blue" style="right: 11px;"></div>
					<li><p class="black">진행 대기건이 있습니다.</p></li>
					<li><a href="/toDo.do?pageNo=1&pageSize=15&searchType=meetingStatus&searchText=NR"><span class="label label-danger"><i class="icon_book_alt"></i></span> 결과 미등록 <span class="small italic pull-right" id="notRegisterCntSpan"></span></a></li>
					<li><a href="/toDo.do?pageNo=1&pageSize=15&searchType=meetingStatus&searchText=NC"><span class="label label-success"><i class="icon_dislike"></i></span> 결재대기 <span class="small italic pull-right" id="notConfirmCntSpan"></span></a></li>
					<li><a href="/toDo.do?pageNo=1&pageSize=15&searchType=meetingStatus&searchText=RD"><span class="label label-warning"><i class="icon_globe"></i></span> 반려 <span class="small italic pull-right" id="notReturnCntSpan"></span></a></li>
					<li><a href="/toDo.do">대기건 전체 보기</a></li>
				</ul>
			</li>
			<!-- alert notification end-->
			<!-- user login dropdown start-->
			<li class="dropdown">
				<a data-toggle="dropdown" class="dropdown-toggle" href="#">
					<span class="username"><sec:authentication property="principal.kname" /></span>
					<b class="caret"></b>
				</a>
				<ul class="dropdown-menu extended logout">
					<!-- <div class="log-arrow-up"></div> -->
					<div class="notify-arrow notify-arrow-blue" style="right: 11px;"></div>
					<li class="eborder-top"><a href="/myPage.do"><i class="icon_profile"></i> 내 정보</a></li>
					<li><a href="/toDo.do"><i class="icon_clock_alt"></i> 진행 대기 내역</a></li>
					<li><a href="/takeOver.do"><i class="icon_gift_alt"></i> 인수인계 관리</a></li>
					<li><a href="/logout.do"><i class="icon_key_alt"></i> 로그아웃</a></li>
				</ul>
			</li>
			<!-- user login dropdown end -->
		</ul>
		<!-- notificatoin dropdown end-->
	</div>
</header>

<!-- modal popup start -->
<div aria-hidden="true" aria-labelledby="gnbTimetableModalLabel" role="dialog" tabindex="-1" id="gnbTimetableModal" class="modal fade">
	<div class="modal-dialog" style="width: 70%;">
		<div class="modal-content">
			<div class="modal-header">
				<button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
				<h4 class="modal-title">실시간 예약 현황</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal" role="form">
					<div class="form-group">
						<label for="searchText" class="col-lg-2 control-label">조회 일자</label>
						<div class="col-lg-8">
			                <div class="input-group date" id="timetableDatePicker">
			                    <input type="text" id="timetableDate" class="form-control" placeholder="조회 일자" />
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>
						</div>
						<div class="col-lg-2">
							<a class="btn btn-default" href="javascript:;" title="실시간 예약 현황 조회 하기" onClick="javascript:gnbSearchTimeTable();"><span class="icon_search"></span>&nbsp;조회</a>
						</div>
					</div>
				</form>
				<div id="gnbTimetable" class="timetable" style="margin-top: 20px;"></div>
			</div>
		</div>
	</div>
</div>
<!-- modal popup end -->