<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>이연제약 관리 시스템</title>
		<link href="/css/bootstrap4.min.css" rel="stylesheet">
		<!-- full calendar css-->
  		<link href="/css/fullcalendar.min.css?ver=20190619" rel="stylesheet" />
  		<link href="/css/fullcalendar.print.min.css?ver=20190619" rel="stylesheet" media="print" />
  		<link href="https://fonts.googleapis.com/css?family=Noto+Sans+KR" rel="stylesheet">
  		<style type="text/css">
  		body { font-family: 'Noto Sans KR', sans-serif; }
  		</style>
  		<!-- slick slider -->
  		<link href="/css/slick.css" rel="stylesheet" />
  		<link href="/css/slick-theme.css" rel="stylesheet" />
  		<!-- timetable -->
  		<link href="/css/timetablejs-bpm.css" rel="stylesheet">
		<!-- javascripts -->
		<script type="text/javascript" src="/js/jquery-3.3.1.min.js"></script>
		<script type="text/javascript" src="/js/popper.min.js"></script>
		<script type="text/javascript" src="/js/jquery-migrate-1.2.1.min.js"></script>
		<script type="text/javascript" src="/js/slick.js"></script>
		<!-- moment script -->
		<script type="text/javascript" src="/js/moment.js"></script>
  		<script type="text/javascript" src="/js/moment-ko.js"></script>
  		<!-- full calendar script -->
		<script type="text/javascript" src="/js/fullcalendar.min.js?ver=20190619"></script>
		<script type="text/javascript" src="/js/fullcalendar-ko.js?ver=20190619"></script>
		<!-- timetable -->
		<script type="text/javascript" src="/js/timetable-bpm.js"></script>
		<!-- bootstrap -->
		<script type="text/javascript" src="/js/bootstrap4.min.js"></script>
		<!-- common function -->
		<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
		<!-- page script -->
		<script type="text/javascript">
		
			// DB 데이터 전역변수
			var EVT_DATA;
		
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu13");
				
				// 달력 데이터 가져오기
				getCalendarData();
				
				// 달력 그리기
				makeCalendarMonth();
				makeCalendarDay();
				
				// 60초마다 달력 갱신
				setInterval(function() {
					redrawTable();
				}, 60000);
				
				// slick start
				/* $('.your-class').slick({
					autoplay: true,
					autoplaySpeed: 5000,
					arrows: false,
					speed: 1500,
				}); */
				
				// timetable 최초 한번 그리기
				//drawTimeTable();
			});
			
			// 60초마다 달력 갱신
			function redrawTable() {
				getCalendarData();
				$('#calendar1').fullCalendar('destroy');
				$('#calendar2').fullCalendar('destroy');
				makeCalendarMonth();
				makeCalendarDay();
			}
			
			// 현재 날짜 포맷팅
			function formatDate(date) {
			    var d = new Date(date),
			        month = '' + (d.getMonth() + 1),
			        day = '' + d.getDate(),
			        year = d.getFullYear();
			    if (month.length < 2) month = '0' + month;
			    if (day.length < 2) day = '0' + day;
			    return [year, month, day].join('-');
			}
			
			// 데이터 가져오기
			function getCalendarData() {
				var date = new Date();
				var firstDay = new Date(date.getFullYear(), date.getMonth() - 1, 20);
				var lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 10);
				var startDate = formatDate(firstDay);
				var endDate = formatDate(lastDay);
				var params = {
						auth : "reyon",
						startDate : startDate,
						endDate : endDate,
					}
					
					var request = $.ajax({
						url: '/schedule/getScheduleTableListAjax.json'
						, type : 'POST'
						, timeout: 30000
						, data : params
						, dataType : 'json'
						, async : false
						, error: function(xhr, textStatus, errorThrown) {
							console.log("시스템 오류가 발생했습니다.");
						}
						, success : function(json) {
							if (json.resultCode == 0) {
								EVT_DATA = json.list
							} else {
								console.log(json.resultMsg);
							}
						}
					});
			}
			
			// 월별 달력 그리기
			function makeCalendarMonth() {
				$('#calendar1').fullCalendar({
					header : {
						left : '',
						center : 'title',
						right : 'month'
					},
					views : {
						month : { buttonText : 'month' },
					},
					defaultView: 'month',
					minTime: '07:00:00',
					maxTime: '20:00:00',
					allDaySlot: false,
					timeFormat: 'HH:mm',
					events : EVT_DATA,
					eventRender : function(event, element) {
				        element.attr('title', event.tooltip);
				    }
				});
			}
			
			// 일별 달력 그리기
			function makeCalendarDay() {
				$('#calendar2').fullCalendar({
					header : {
						left : '',
						center : 'title',
						right : 'agendaDay'
					},
					views : {
						agendaDay : { buttonText : 'day' },
					},
					defaultView: 'agendaDay',
					height: 786,
					minTime: '07:00:00',
					maxTime: '21:00:00',
					allDaySlot: false,
					timeFormat: 'HH:mm',
					events : EVT_DATA,
					eventRender : function(event, element) {
				        element.attr('title', event.tooltip);
				    }
				});
			}
			
			// timetable 최초 한번 그리기
			function drawTimeTable() {
				// set date
			    var today = new Date();
			    var dd = today.getDate();
			    var mm = today.getMonth()+1;
			    var yyyy = today.getFullYear();
			    if(dd<10){ dd='0'+dd; } 
			    if(mm<10){ mm='0'+mm; } 
			    var today = yyyy + "-" + mm + '-' + dd;
				
				var params = {
					auth : "reyon",
					searchDate : today
				}

				var request = $.ajax({
					url : "/schedule/getScheduleTimeTableListAjax.json",
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
							var list = json.list;
							
							// 전체 사용자 리스트
							var tempArr = [];
							for(var i=0; i<list.length; i++){
								tempArr.push(list[i].type);
							}
							
							// 사용자 리스트 중복 제거
							var totalRoomlistArr = [];
							$.each(tempArr, function(i, el){
							    if($.inArray(el, totalRoomlistArr) === -1) totalRoomlistArr.push(el);
							});

							// 타임테이블 설정
							var timetable = new Timetable();   
							timetable.setScope(7, 20);
							timetable.addLocations(totalRoomlistArr);
							
							// 예약된 리스트 설정
							for(var i=0; i<list.length; i++){
								var info = list[i];
								timetable.addEvent(info.title, info.type, moment(info.start).toDate(), moment(info.end).toDate());
							}
							
							// 타임 테이블 렌더
							var renderer = new Timetable.Renderer(timetable);
							renderer.draw('#mainTimetable');
						} else if (json.resultCode == 1201) {
							alert(json.resultMsg);
							cfLogin();
						} else {
							alert(json.resultMsg);
						}
					}
				});
			}
			
		</script>
	</head>
	
	<body>
		<div class="container-fluid">
			
			<div class="row" style="margin-top:50px;">
				<div id='calendar1' class='calendar col-md-8'></div>
				<div id='calendar2' class='calendar col-md-4'></div>
			</div>
			
			<!-- <div class="your-class" style="margin-top:50px;">
				<div>
					<div class="row">
						<div id='calendar1' class='calendar col-md-8'></div>
						<div id='calendar2' class='calendar col-md-4'></div>
					</div>
				</div>
				<div>
					<div id="mainTimetable" class="timetable"></div>
				</div>
			</div> -->
		</div>
	</body>
	
</html>