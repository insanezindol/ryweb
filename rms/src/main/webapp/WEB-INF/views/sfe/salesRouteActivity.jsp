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
		<!-- jsgrid -->
		<link rel="stylesheet" href="/css/jsgrid.min.css" />
		<link rel="stylesheet" href="/css/jsgrid-theme.min.css" />
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
		<!-- jsgrid -->
		<script type="text/javascript" src="/js/jsgrid.min.js"></script>
		<!-- common function -->
		<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
		<!-- proj4 api -->
		<script type="text/javascript" src="/js/proj4.js"></script>
		<!-- vworld map api -->
		<script type="text/javascript" src="http://map.vworld.kr/js/vworldMapInit.js.do?apiKey=CCF38AFF-389D-35DB-A32A-1683474BEF12"></script>
		<!-- page script -->
		<script type="text/javascript">
		
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu10");

				// datetimepicker
				$('#standardDatetime').datetimepicker({
					format: 'YYYY-MM-DD',
					sideBySide: true,
				});
				var today = new Date();
			    var dd = today.getDate();
			    var mm = today.getMonth()+1;
			    var yyyy = today.getFullYear();
			    if(dd<10){ dd='0'+dd; } 
			    if(mm<10){ mm='0'+mm; } 
			    var today = yyyy + "-" + mm + '-' + dd;
			    $("#standardDate").val(today);
			    $("#standardDatetime").on("dp.change", function(e){ 
			    	$("#userBody").empty();
			    	$("#routeBody").empty();
			    	$("#deptTable tr").removeClass("row-selected");
				});
				
				// VWORLD MAP API 호출
				loadVwordMap();
				
				// 부서 정보 가져오기
				getSalesDeptInfo();
			});

			// VWORLD MAP 전역 변수
			var apiMap;
			var mControl;
			var marker;

			// VWORLD MAP API 호출
			function loadVwordMap() {
				vworld.showMode = false;
				vworld.init("vmap", "map-first", function() {
					apiMap = this.vmap;
					apiMap.setBaseLayer(apiMap.vworldBaseMap);
					apiMap.setControlsType({
						"simpleMap" : true
					});
					apiMap.addVWORLDControl("zoomBar");
					apiMap.setCenterAndZoom(14144847.813857475, 4509954.011272858, 16);
				});
			}

			// 마커 추가 함수
			function addMarker(lon, lat, message, imgurl) {
				marker = new vworld.Marker(lon, lat, message, "");
				if (typeof imgurl == 'string') {
					marker.setIconImage(imgurl);
				}
				marker.setZindex(3);
				apiMap.addMarker(marker);
				apiMap.setCenterAndZoom(lon, lat, 14);
				
				if (typeof imgurl == 'string') {
					var rotate = marker.events.element.id.toString();
					rotate += '_innerImage';
					$("#"+rotate).height('53px');
					$("#"+rotate).width('35px');
				}
			}

			// 부서 정보 가져오기
			function getSalesDeptInfo() {
				var params = {
					auth : "reyon",
				}

				var request = $.ajax({
					url : '/sfe/salesDeptAjax.json',
					type : 'POST',
					timeout : 30000,
					data : params,
					dataType : 'json',
					beforeSend : function(xmlHttpRequest) {
						cfOpenMagnificPopup();
					},
					error : function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					},
					success : function(json) {
						if (json.resultCode == 0) {
							var list = json.list;
							var tag = '';
							for (var i = 0; i < list.length; i++) {
								tag += '<tr id=\"tr'+list[i].DEPT_CD+'\" onClick=\"javascript:getSalesUserInfo(\'' + list[i].DEPT_CD + '\');\">';
								tag += '<td>';
								tag += list[i].DEPT_CD;
								tag += '</td>';
								tag += '<td>';
								tag += list[i].DEPT_NM;
								tag += '</td>';
								tag += '</tr>';
							}
							$("#deptBody").append(tag);
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

			// 직원 정보 가져오기
			function getSalesUserInfo(deptNo) {
				apiMap.initAll();
				
				// 테이블에 색상 입히기
				$("#deptTable tr").removeClass("row-selected");
				$("#tr"+deptNo).addClass("row-selected");
				
				var standardDate = $("#standardDate").val();
				if (standardDate == "") {
					alert("조회일을 입력해 주세요.");
					return;
				}

				var params = {
					auth : "reyon",
					deptNo : deptNo,
					standardDate : standardDate,
				}

				var request = $.ajax({
					url : '/sfe/salesRouteActivityUser.json',
					type : 'POST',
					timeout : 30000,
					data : params,
					dataType : 'json',
					beforeSend : function(xmlHttpRequest) {
						cfOpenMagnificPopup();
					},
					error : function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					},
					success : function(json) {
						if (json.resultCode == 0) {
							$("#userBody").empty();
							$("#routeBody").empty();
							var list = json.list;
							var tag = '';
							if (list.length == 0) {
								tag += '<tr><td colspan="3">인원이 없습니다.</td></tr>';
							} else {
								for (var i = 0; i < list.length; i++) {
									tag += '<tr id=\"tr'+list[i].empNo+'\" onClick="javascript:getSalesRouteInfo(\'' + list[i].deptNo + '\',\'' + list[i].empNo + '\',\'' + list[i].gubun1 + '\');">';
									tag += '<td>';
									if(list[i].empNm != null){
										tag += list[i].empNm;
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].visitSalesCnt != null){
										tag += list[i].visitSalesCnt;
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].planSalesCnt != null){
										tag += list[i].planSalesCnt;
									}
									tag += '</td>';
									tag += '</tr>';
								}
							}
							$("#userBody").append(tag);
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

			// 경로 정보 가져오기
			function getSalesRouteInfo(deptNo, empNo, gubun) {
				apiMap.initAll();
				
				// 테이블에 색상 입히기
				$("#userTable tr").removeClass("row-selected");
				$("#tr"+empNo).addClass("row-selected");
				
				var standardDate = $("#standardDate").val();
				if (standardDate == "") {
					alert("조회일을 입력해 주세요.");
					return;
				}

				var params = {
					auth : "reyon",
					deptNo : deptNo,
					empNo : empNo,
					gubun : gubun,
					startDate : standardDate,
					endDate : standardDate,
					cust : null,
				}

				var request = $.ajax({
					url : '/sfe/salesRouteActivityRoute.json',
					type : 'POST',
					timeout : 30000,
					data : params,
					dataType : 'json',
					beforeSend : function(xmlHttpRequest) {
						cfOpenMagnificPopup();
					},
					error : function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					},
					success : function(json) {
						if (json.resultCode == 0) {
							$("#routeBody").empty();
							var list = json.list;
							var tag = '';
							if (list.length == 0) {
								tag += '<tr><td colspan="14" class="text-center">결과가 존재하지 않습니다.</td></tr>';
							} else {
								for (var i = 0; i < list.length; i++) {
									drawMarker((i+1), list[i].GPS_END_NUM1, list[i].GPS_END_NUM2, list[i].SFA_CUST_NM);
									tag += '<tr id=\"tr'+(i+1)+'\" onClick="javascript:getRouteMap(\'' + (i+1) + '\',' + list[i].GPS_END_NUM1 + ',' + list[i].GPS_END_NUM2 + ',\'' + list[i].SFA_CUST_NM + '\');">';
									tag += '<td>' + (i+1) + '</td>';
									tag += '<td>';
									if(list[i].SFA_CUST_NM != null){
										tag += list[i].SFA_CUST_NM;
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].S_DATE != null){
										tag += list[i].S_DATE;
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].CUS_GBN_NM != null){
										tag += list[i].CUS_GBN_NM;
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].VISIT_DATE != null){
										tag += list[i].VISIT_DATE;
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].ST_DATE != null){
										tag += list[i].ST_DATE;
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].ED_DATE != null){
										tag += list[i].ED_DATE;
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].ACTIVITY_DESC != null){
										tag += list[i].ACTIVITY_DESC;
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].GPS_END_NUM1 != null){
										tag += list[i].GPS_END_NUM1;
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].GPS_END_NUM2 != null){
										tag += list[i].GPS_END_NUM2;
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].STATUS != null){
										tag += cfCtStatus(list[i].STATUS);
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].IMITATION_GPS != null){
										tag += cfCtImigps(list[i].IMITATION_GPS);
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].LUTING_YN != null){
										tag += cfCtLuting(list[i].LUTING_YN);
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].PLAN_GUBUN != null){
										tag += cfCtPlan(list[i].PLAN_GUBUN);
									}
									tag += '</td>';
									tag += '</tr>';
								}
							}
							$("#routeBody").append(tag);
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
			
			// 맵에서 해당 위치로 줌하는 함수
			function getRouteMap(rk, gpsEndNum1, gpsEndNum2, message){
				// 테이블에 색상 입히기
				$("#routeTable tr").removeClass("row-selected");
				$("#tr"+rk).addClass("row-selected");

				if(gpsEndNum1 != null && gpsEndNum2 != null){
					var epsg3857 = "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs";
					var epsg4326 = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";
					var entX = Number(gpsEndNum2);
					var entY = Number(gpsEndNum1);
					var projection = proj4(epsg4326, epsg3857, [entX, entY]);
					var lon = projection[0];
					var lat = projection[1];
					apiMap.setCenterAndZoom(lon, lat, 16);
				}
			}
			
			// 좌표변환 후 마커 추가 함수를 호출하는 함수
			function drawMarker(rk, gpsEndNum1, gpsEndNum2, message){
				if(gpsEndNum1 != null && gpsEndNum2 != null){
					var epsg3857 = "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs";
					var epsg4326 = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";
					var entX = Number(gpsEndNum2);
					var entY = Number(gpsEndNum1);
					var projection = proj4(epsg4326, epsg3857, [entX, entY]);
					var lon = projection[0];
					var lat = projection[1];
					addMarker(lon, lat, message, '/img/pin/pin'+rk+'.png');
				}
			}
		</script>
		<style> body { font-family: 'Noto Sans KR', sans-serif !important;} .row-selected td { background-color:#308dcc !important; color:#fff !important; }</style>
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
								<i class="fa fa-mobile"></i> 활동 경로
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-briefcase"></i>SFE 관리</li>
								<li><i class="fa fa-mobile"></i>활동 경로</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">활동 경로</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<div class="col-lg-6">
												<div class="row">
													<div class="col-lg-2 control-label">조회일</div>
													<div class="col-lg-10">
														<div class="input-group date" id="standardDatetime">
										                    <input type="text" id="standardDate" class="form-control" />
										                    <span class="input-group-addon">
										                        <span class="glyphicon glyphicon-calendar"></span>
										                    </span>
										                </div>
													</div>
												</div>
												<div class="row">
													<div class="col-lg-6" style="height: 330px; overflow-y: scroll;">
														<table id="deptTable" class="table table-hover personal-task">
															<thead>
																<tr>
																	<th class="text-center">부서번호</th>
																	<th class="text-center">부서이름</th>
																</tr>
															</thead>
															<tbody id="deptBody">
															</tbody>
														</table>
													</div>
													<div class="col-lg-6" style="height: 330px; overflow-y: scroll;">
														<table id="userTable" class="table table-hover personal-task">
															<thead>
																<tr>
																	<th class="text-center">이름</th>
																	<th class="text-center">방문</th>
																	<th class="text-center">계획</th>
																</tr>
															</thead>
															<tbody id="userBody">
															</tbody>
														</table>
													</div>
												</div>
												<div class="row">
													<div class="col-lg-12" style="height: 330px; overflow-x: scroll; overflow-y: scroll;">
														<table id="routeTable" class="table table-hover personal-task" style="width: auto;">
															<thead>
																<tr>
																	<th class="text-center">No</th>
																	<th class="text-center">거래처</th>
																	<th class="text-center">개시일</th>
																	<th class="text-center">구분</th>
																	<th class="text-center">활동일</th>
																	<th class="text-center">시작일</th>
																	<th class="text-center">종료일</th>
																	<th class="text-center">활동</th>
																	<th class="text-center">경도</th>
																	<th class="text-center">위도</th>
																	<th class="text-center">결과</th>
																	<th class="text-center">모의</th>
																	<th class="text-center">루팅</th>
																	<th class="text-center">계획구분</th>
																</tr>
															</thead>
															<tbody id="routeBody">
															</tbody>
														</table>
													</div>
												</div>
											</div>
											<div class="col-lg-6">
												<div id="vmap" style="width: 100%; height: 700px; left: 0px; top: 0px"></div>
											</div>
										</div>
									</form>
								</div>
							</section>
						</div>
					</div>
					<!-- body top end -->
					
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