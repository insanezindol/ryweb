<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>이연제약 관리 시스템</title>
		<!-- Bootstrap CSS -->
		<link href="/css/bootstrap.min.css" rel="stylesheet">
		<link href="/css/bootstrap-theme.css?ver=20190619" rel="stylesheet">
		<link href="/css/elegant-icons-style.css" rel="stylesheet" />
		<link href="/css/font-awesome.min.css" rel="stylesheet" />
		<!-- Custom styles -->
		<link href="/css/style.css?ver=20190619" rel="stylesheet">
		<link href="/css/style-responsive.css" rel="stylesheet" />
		<link href="/css/jquery-ui-1.10.4.min.css" rel="stylesheet">
		<!-- full calendar css-->
  		<link href="/css/fullcalendar.min.css?ver=20190619" rel="stylesheet" />
  		<link href="/css/fullcalendar.print.min.css?ver=20190619" rel="stylesheet" media="print" />
  		<style type="text/css">.fc-content:hover{cursor: pointer;}</style>
		<!-- jquery script -->
		<script type="text/javascript" src="/js/jquery-1.8.3.min.js"></script>
		<script type="text/javascript" src="/js/jquery-ui-1.10.4.min.js"></script>
		<script type="text/javascript" src="/js/jquery-ui-1.9.2.custom.min.js"></script>
		<!-- bootstrap script -->
		<script type="text/javascript" src="/js/bootstrap.min.js"></script>
		<!-- nice scroll script -->
		<!-- <script type="text/javascript" src="/js/jquery.scrollTo.min.js"></script>
		<script type="text/javascript" src="/js/jquery.nicescroll.js"></script> -->
		<!-- moment script -->
  		<script type="text/javascript" src="/js/moment.js"></script>
		<!-- full calendar script -->
		<script type="text/javascript" src="/js/fullcalendar.min.js?ver=20190619"></script>
		<script type="text/javascript" src="/js/fullcalendar-ko.js?ver=20190619"></script>
		<!-- custom script for this page-->
		<script type="text/javascript" src="/js/scripts.js?ver=20190619"></script>
		<script type="text/javascript" src="/js/jquery.autosize.min.js"></script>
		<script type="text/javascript" src="/js/jquery.placeholder.min.js"></script>
		<script type="text/javascript" src="/js/jquery.slimscroll.min.js"></script>
		<!-- common function -->
		<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
		<!-- page script -->
		<script type="text/javascript">
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu01");
				
				// 달력 그리기
				makeCalendar();
				
				// 날씨 그리기
				makeWeather();
			});
			
			function changePannel(num){
				$("#pannelTitle").text("");
				$("#pannelBody1").hide();
				$("#pannelBody2").hide();
				$("#pannelBody3").hide();
				$("#pannelBody4").hide();
				if (num == "1") {
					$("#pannelTitle").text("금일 회의 리스트");
					$("#pannelBody1").show();
				} else if (num == "2") {
					$("#pannelTitle").text("금주 회의 리스트");
					$("#pannelBody2").show();
				} else if (num == "3") {
					$("#pannelTitle").text("금일 방문 리스트");
					$("#pannelBody3").show();
				} else if (num == "4") {
					$("#pannelTitle").text("금주 방문 리스트");
					$("#pannelBody4").show();
				}
			}
		
			function makeCalendar() {
				$('#calendar').fullCalendar({
					header : {
						left : 'prev,next',
						center : 'title',
						right : 'month,agendaWeek,agendaDay'
					},
					views : {
						agendaDay : {
							buttonText : 'Day'
						},
						agendaWeek : {
							buttonText : 'Week'
						},
						month : {
							buttonText : 'Month'
						}
					},
					defaultView: 'month',
					/* minTime: '08:00:00',
					maxTime: '20:00:00', */
					timeFormat: 'HH:mm',
					navLinks : true,
					eventLimit : true,
					events : function(start, end, timezone, callback) {
				        $.ajax({
				            url: "/common/getMainScheduleListAjax.json",
				            dataType : "json",
				            data: {
				                auth : "reyon",
				                startDate: start.format('YYYY-MM-DD'),
				                endDate: end.format('YYYY-MM-DD'),
				            },
				            success: function(json) {
				                callback(json.list);
				            }
				        });
				    },
					eventClick : function(calEvent, jsEvent, view) {
						var evtId = calEvent.id;
						var evtType = calEvent.type;
						if (evtType == "01") {
							location.href = "/meeting/meetingView.do?meetingSeq="+evtId;
						} else if (evtType == "02") {
							location.href = "/visitor/visitorView.do?meetingSeq="+evtId;
						}
					},
					eventRender : function(event, element) {
				        element.attr('title', event.tooltip);
				    }
				});
			}
			
			function makeWeather(){
				var params = {
					auth : "reyon"
				}
				
				var request = $.ajax({
					url: "/common/getWeatherAjax.json"
					, type : "POST"
					, timeout: 10000
					, data : params
					, dataType : "json"
					, error: function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					}
					, success : function(json) {
						if (json.resultCode == 0) {
							var seoul = json.resultMsg.seoul;
							var jincheon = json.resultMsg.jincheon;
							var chungju = json.resultMsg.chungju;
							// 서울 날씨
							$("#weatherSeoulIcon").attr("class", getWeatherIcon(seoul.weather[0].id));
							$("#weatherSeoulTemp").text((seoul.main.temp - 273.15).toFixed(1) + " °C");
							$("#weatherSeoulDesc").text(seoul.weather[0].description);
							$("#weatherSeoulWind").text("wind : " + seoul.wind.speed + " m/s");
							$("#weatherSeoulCloud").text("cloud : " + seoul.clouds.all +" %");
							// 진천 날씨
							$("#weatherJincheonIcon").attr("class", getWeatherIcon(jincheon.weather[0].id));
							$("#weatherJincheonTemp").text((jincheon.main.temp - 273.15).toFixed(1) + " °C");
							$("#weatherJincheonDesc").text(jincheon.weather[0].description);
							$("#weatherJincheonWind").text("wind : " + jincheon.wind.speed + " m/s");
							$("#weatherJincheonCloud").text("cloud : " + jincheon.clouds.all +" %");
							// 충주 날씨
							$("#weatherChungjuIcon").attr("class", getWeatherIcon(chungju.weather[0].id));
							$("#weatherChungjuTemp").text((chungju.main.temp - 273.15).toFixed(1) + " °C");
							$("#weatherChungjuDesc").text(chungju.weather[0].description);
							$("#weatherChungjuWind").text("wind : " + chungju.wind.speed + " m/s");
							$("#weatherChungjuCloud").text("cloud : " + chungju.clouds.all +" %");
						}
					}
				});
			}
			
			function getWeatherIcon(id){
				if (id == 200 || id == 201 || id == 202 || id == 210 || id == 211 || id == 212 || id == 221 || id == 230 || id == 231 || id == 232) {
					// Thunderstorm
					return "wi wi-thunderstorm";
				} else if (id == 300 || id == 301 || id == 302 || id == 310 || id == 311 || id == 312 || id == 313 || id == 314 || id == 321) {
					// Drizzle
					return "wi wi-raindrops";
				} else if (id == 500 || id == 501 || id == 502 || id == 503 || id == 504 || id == 511 || id == 520 || id == 521 || id == 522 || id == 531) {
					// Rain
					return "wi wi-rain";
				} else if (id == 600 || id == 601 || id == 602 || id == 611 || id == 612 || id == 615 || id == 616 || id == 620 || id == 621 || id == 622) {
					// Snow
					return "wi wi-snow";
				} else if (id == 701 || id == 711 || id == 721 || id == 731 || id == 741 || id == 751 || id == 761 || id == 762 || id == 771 || id == 781) {
					// Atmosphere
					return "wi wi-fog";
				} else if (id == 801 || id == 802 || id == 803 || id == 804) {
					// Clouds
					return "wi wi-cloudy";
				} else {
					// Clear (800)
					return "wi wi-day-sunny";
				}
			}
			
			function goTodo(num) {
				location.href="/toDo.do?pageNo=1&pageSize=10&searchType=meetingStatus&searchText="+num;
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
								<i class="fa fa-home"></i> 메인&nbsp;&nbsp;&nbsp;&nbsp;
								<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SUPERUSER')">
								<div class="btn-group">
									<button id="saupBtn00" class="btn btn-default" type="button" onClick="javascript:selectSaup('00');location.reload();">전체</button>
									<button id="saupBtn10" class="btn btn-default" type="button" onClick="javascript:selectSaup('10');location.reload();">본사</button>
									<button id="saupBtn20" class="btn btn-default" type="button" onClick="javascript:selectSaup('20');location.reload();">진천공장</button>
									<button id="saupBtn30" class="btn btn-default" type="button" onClick="javascript:selectSaup('30');location.reload();">충주공장</button>
									<button id="saupBtn40" class="btn btn-default" type="button" onClick="javascript:selectSaup('40');location.reload();">안양연구소</button>
									<button id="saupBtn50" class="btn btn-default" type="button" onClick="javascript:selectSaup('50');location.reload();">N-bio</button>
								</div>
								</sec:authorize>
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-home"></i>메인</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- weather start -->
					<div class="row">
						<div class="col-lg-4 col-md-12 col-sm-12 col-xs-12">
							<div class="info-box panel-gradient-bg">
								<i id="weatherSeoulIcon" class="" style="color: white; margin-left: 20px;"></i>
								<div class="title" style="float: right;">서울</div>
								<div id="weatherSeoulTemp" class="count" style="margin-top: 0px;"></div>
								<div id="weatherSeoulDesc" class="desc" style="margin-top: 0px;"></div>
								<div id="weatherSeoulWind" class="desc" style="margin-top: 0px;"></div>
								<div id="weatherSeoulCloud" class="desc" style="margin-top: 0px;"></div>
							</div>
						</div>
						<div class="col-lg-4 col-md-12 col-sm-12 col-xs-12">
							<div class="info-box panel-gradient-bg">
								<i id="weatherJincheonIcon" class="" style="color: white; margin-left: 20px;"></i>
								<div class="title" style="float: right;">진천</div>
								<div id="weatherJincheonTemp" class="count" style="margin-top: 0px;"></div>
								<div id="weatherJincheonDesc" class="desc" style="margin-top: 0px;"></div>
								<div id="weatherJincheonWind" class="desc" style="margin-top: 0px;"></div>
								<div id="weatherJincheonCloud" class="desc" style="margin-top: 0px;"></div>
							</div>
						</div>
						<div class="col-lg-4 col-md-12 col-sm-12 col-xs-12">
							<div class="info-box panel-gradient-bg">
								<i id="weatherChungjuIcon" class="" style="color: white; margin-left: 20px;"></i>
								<div class="title" style="float: right;">충주</div>
								<div id="weatherChungjuTemp" class="count" style="margin-top: 0px;"></div>
								<div id="weatherChungjuDesc" class="desc" style="margin-top: 0px;"></div>
								<div id="weatherChungjuWind" class="desc" style="margin-top: 0px;"></div>
								<div id="weatherChungjuCloud" class="desc" style="margin-top: 0px;"></div>
							</div>
						</div>
					</div>
					<!-- weather end -->
					
					<!-- statistics start -->
					<div class="row">
						<!-- banner start -->
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="row">
								<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
									<div class="info-box panel-grey-bg">
										<i class="fa fa-clock-o" style="margin-right: 5px;"></i>
										<div class="title">금일 회의</div>
										<div class="count"><a href="javascript:;" onClick="javascript:changePannel('1');" data-toggle="tooltip" title="금일 회의 리스트 보기" style="color:#fff; cursor:pointer;">${todayMeetListCnt }건</a></div>
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
									<div class="info-box panel-grey-bg">
										<i class="fa fa-calendar-minus-o" style="margin-right: 5px;"></i>
										<div class="title">금주 회의</div>
										<div class="count"><a href="javascript:;" onClick="javascript:changePannel('2');" data-toggle="tooltip" title="금주 회의 리스트 보기" style="color:#fff; cursor:pointer;">${weekMeetListCnt }건</a></div>
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
									<div class="info-box panel-grey-bg">
										<i class="fa fa-id-badge" style="margin-right: 5px;"></i>
										<div class="title">금일 방문</div>
										<div class="count"><a href="javascript:;" onClick="javascript:changePannel('3');" data-toggle="tooltip" title="금일 방문 리스트 보기" style="color:#fff; cursor:pointer;">${todayVisitorListCnt }건</a></div>
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
									<div class="info-box panel-grey-bg">
										<i class="fa fa-id-card-o" style="margin-right: 5px;"></i>
										<div class="title">금주 방문</div>
										<div class="count"><a href="javascript:;" onClick="javascript:changePannel('4');" data-toggle="tooltip" title="금주 방문 리스트 보기" style="color:#fff; cursor:pointer;">${weekVisitorListCnt }건</a></div>
									</div>
								</div>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="panel panel-default">
								<div class="panel-heading">
									<h2>
										<strong id="pannelTitle">금일 회의 리스트</strong>
									</h2>
								</div>
								<div class="panel-body" style="height: 275px; overflow-y: scroll;">
									<table class="table table-hover personal-task">
										<tbody id="pannelBody1" style="display:table-row-group;">
											<tr>
												<th>회의시간</th>
												<th>제목</th>
												<th>부서</th>
												<th>담당자</th>
											</tr>
											<c:choose>
											<c:when test="${fn:length(todayMeetList) == 0}">
											<tr style="letter-spacing: -1px">
												<td class="text-center" colspan="4">금일 회의가 없습니다.</td>
											</tr>
											</c:when>
											<c:otherwise>
											<c:forEach var="result" items="${todayMeetList}" varStatus="status">
											<tr>
												<td>${fn:substring(result.meetingStartDate,11,16) } ~ ${fn:substring(result.meetingEndDate,11,16) }</td>
												<td><a href="/meeting/meetingView.do?meetingSeq=${result.meetingSeq }" data-toggle="tooltip" title="회의 내용 상세보기">${result.meetingName }</a></td>
												<td>${result.deptName }</td>
												<td>${result.regName }</td>
											</tr>
											</c:forEach>
											</c:otherwise>
											</c:choose>
										</tbody>
										<tbody id="pannelBody2" style="display:none;">
											<tr>
												<th>회의일자</th>
												<th>제목</th>
												<th>부서</th>
												<th>담당자</th>
											</tr>
											<c:choose>
											<c:when test="${fn:length(weekMeetList) == 0}">
											<tr style="letter-spacing: -1px">
												<td class="text-center" colspan="4">금주 회의가 없습니다.</td>
											</tr>
											</c:when>
											<c:otherwise>
											<c:forEach var="result" items="${weekMeetList}" varStatus="status">
											<tr>
												<td>${fn:substring(result.meetingStartDate,0,10) }</td>
												<td><a href="/meeting/meetingView.do?meetingSeq=${result.meetingSeq }" data-toggle="tooltip" title="회의 내용 상세보기">${result.meetingName }</a></td>
												<td>${result.deptName }</td>
												<td>${result.regName }</td>
											</tr>
											</c:forEach>
											</c:otherwise>
											</c:choose>
										</tbody>
										<tbody id="pannelBody3" style="display:none;">
											<tr>
												<th>방문시간</th>
												<th>방문자</th>
												<th>방문목적</th>
												<th>접견인</th>
											</tr>
											<c:choose>
											<c:when test="${fn:length(todayVisitorList) == 0}">
											<tr style="letter-spacing: -1px">
												<td class="text-center" colspan="4">금일 방문이 없습니다.</td>
											</tr>
											</c:when>
											<c:otherwise>
											<c:forEach var="result" items="${todayVisitorList}" varStatus="status">
											<tr>
												<td>${fn:substring(result.meetingStartDate,11,16) } ~ ${fn:substring(result.meetingEndDate,11,16) }</td>
												<td>${result.visitCompany }(${result.visitName })</td>
												<td><a href="/visitor/visitorView.do?meetingSeq=${result.meetingSeq }" data-toggle="tooltip" title="방문 내용 상세보기">${result.meetingName }</a></td>
												<td>${result.deptName }(${result.regName })</td>
											</tr>
											</c:forEach>
											</c:otherwise>
											</c:choose>
										</tbody>
										<tbody id="pannelBody4" style="display:none;">
											<tr>
												<th>방문일자</th>
												<th>방문자</th>
												<th>방문목적</th>
												<th>접견인</th>
											</tr>
											<c:choose>
											<c:when test="${fn:length(weekVisitorList) == 0}">
											<tr style="letter-spacing: -1px">
												<td class="text-center" colspan="4">금주 방문이 없습니다.</td>
											</tr>
											</c:when>
											<c:otherwise>
											<c:forEach var="result" items="${weekVisitorList}" varStatus="status">
											<tr>
												<td>${fn:substring(result.meetingStartDate,0,10) }</td>
												<td>${result.visitCompany }(${result.visitName })</td>
												<td><a href="/visitor/visitorView.do?meetingSeq=${result.meetingSeq }" data-toggle="tooltip" title="방문 내용 상세보기">${result.meetingName }</a></td>
												<td>${result.deptName }(${result.regName })</td>
											</tr>
											</c:forEach>
											</c:otherwise>
											</c:choose>
										</tbody>
									</table>
								</div>
							</div>
						</div>
						<!-- banner end -->
						<!-- not finish start -->
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="panel panel-default">
								<div class="panel-heading">
									<h2>
										<strong>진행 대기 내역</strong>
									</h2>
								</div>
								<div class="panel-body" style="height: 275px; overflow-y: scroll;">
									<table class="table table-hover personal-task">
										<tbody>
											<tr>
												<th>회의일자</th>
												<th>제목</th>
												<th>담당자</th>
												<th>상태</th>
											</tr>
											<c:choose>
											<c:when test="${fn:length(standbyList) == 0}">
											<tr style="letter-spacing: -1px">
												<td class="text-center" colspan="4">진행 대기 내역이 없습니다.</td>
											</tr>
											</c:when>
											<c:otherwise>
											<c:forEach var="result" items="${standbyList}" varStatus="status">
											<tr>
												<td>${fn:substring(result.meetingStartDate,0,10) }</td>
												<td>${result.meetingName }</td>
												<td>${result.deptName }(${result.regName })</td>
												<td>
													<c:if test="${result.meetingStatus == '02' && result.meetingType == '01' }">
													<a href="/meeting/meetingResultAdd.do?meetingSeq=${result.meetingSeq }" data-toggle="tooltip" title="결과등록하기">결과미등록</a>
													</c:if>
													<c:if test="${result.meetingStatus == '02' && result.meetingType == '02' }">
													<a href="/visitor/visitorResultAdd.do?meetingSeq=${result.meetingSeq }" data-toggle="tooltip" title="결과등록하기">결과미등록</a>
													</c:if>
													<c:if test="${ (result.meetingStatus == '03' || result.meetingStatus == '04') && result.meetingType == '01' }">
													<a href="/meeting/meetingView.do?meetingSeq=${result.meetingSeq }" data-toggle="tooltip" title="결재대기">결재대기</a>
													</c:if>
													<c:if test="${ (result.meetingStatus == '03' || result.meetingStatus == '04') && result.meetingType == '02' }">
													<a href="/visitor/visitorView.do?meetingSeq=${result.meetingSeq }" data-toggle="tooltip" title="결재대기">결재대기</a>
													</c:if>
													<c:if test="${result.meetingStatus == '99' && result.meetingType == '01' }">
													<a href="/meeting/meetingView.do?meetingSeq=${result.meetingSeq }" data-toggle="tooltip" title="결재반려">결재반려</a>
													</c:if>
													<c:if test="${result.meetingStatus == '99' && result.meetingType == '02' }">
													<a href="/visitor/visitorView.do?meetingSeq=${result.meetingSeq }" data-toggle="tooltip" title="결재반려">결재반려</a>
													</c:if>
												</td>
											</tr>
											</c:forEach>
											</c:otherwise>
											</c:choose>
										</tbody>
									</table>
								</div>
							</div>
						</div>
						<!-- not finish end -->
					</div>
					<!-- statistics end -->
					
					<!-- schedule start -->
					<div class="row">
						<!-- calendar start -->
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<div class="panel panel-default">
								<div class="panel-heading">
									<h2>
										<strong>일정</strong>
									</h2>
								</div>
								<div class="panel-body">
									<div id="calendar"></div>
								</div>
							</div>
						</div>
						<!-- calendar end -->
					</div>
					<!-- schedule end -->
			
				</section>
				<!--overview end-->
				
			</section>
			<!--main content end-->
			
		</section>
		<!-- container section start -->
		
		<!-- modal popup start -->
		<div aria-hidden="true" aria-labelledby="mainModalLabel" role="dialog" tabindex="-1" id="mainModal" class="modal fade">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
						<h4 class="modal-title">진행 대기 내역</h4>
					</div>
					<div class="modal-body">
						<div class="alert alert-block alert-danger fade in text-center">진행 대기 내역이 있습니다. 확인해 주십시오.</div>
						<table class="table table-hover personal-task">
							<thead>
								<tr>
									<th class="text-center">항목</th>
									<th class="text-center">건수</th>
								</tr>
							</thead>
							<tbody>
								<tr onClick="javascript:goTodo('NR');" style="cursor:pointer;" >
									<td>결과미등록</td>
									<td id="notRegisterCntMainSpan"></td>
								</tr>
								<tr onClick="javascript:goTodo('NC');" style="cursor:pointer;" >
									<td>결재대기</td>
									<td id="notConfirmCntMainSpan"></td>
								</tr>
								<tr onClick="javascript:goTodo('RD');" style="cursor:pointer;" >
									<td>결재반려</td>
									<td id="notReturnCntMainSpan"></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<!-- modal popup end -->
		
	</body>
</html>