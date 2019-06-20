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
			    
			    // 조회일 변경시 이벤트
				$("#standardDatetime").on("dp.change", function(e){
					// 최초 달력 버튼 클릭하면 변경 이벤트가 발생되어 validation 추가함 
					if($("#standardDate").val() != e.date._i){
						$("#routeBody").empty();
				    	getSalesUserInfo();
					}
				});
				
				// VWORLD MAP API 호출
				loadVwordMap();
				
				// 직원 정보 가져오기
				getSalesUserInfo();
				
				// 시작콜, 종료콜 변경 이벤트
				$("input[name=callType]").change(function() {
					$("#humanX").empty();
					$("#humanY").empty();
					$("#hospitalX").empty();
					$("#hospitalY").empty();
					$("#humanXText").val("");
					$("#humanYText").val("");
					$("#hospitalXText").val("");
					$("#hospitalYText").val("");
					$("#sfaSalesNoText").val("");
					$("#sabunText").val("");
					getSalesUserInfo();
				});
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
				//apiMap.setCenterAndZoom(lon, lat, 14);
				
				if (typeof imgurl == 'string') {
					var rotate = marker.events.element.id.toString();
					rotate += '_innerImage';
					$("#"+rotate).height('53px');
					$("#"+rotate).width('35px');
				}
			}

			// 직원 정보 가져오기
			function getSalesUserInfo() {
				if (apiMap != null) {
					apiMap.initAll();
				}
				
				var standardDate = $("#standardDate").val();
				if (standardDate == "") {
					alert("조회일을 입력해 주세요.");
					return;
				}

				var params = {
					auth : "reyon",
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
							$("#btnChange").hide();
							var list = json.list;
							var tag = '';
							if (list.length == 0) {
								tag += '<tr><td colspan="3">인원이 없습니다.</td></tr>';
							} else {
								for (var i = 0; i < list.length; i++) {
									tag += '<tr id=\"tr'+list[i].empNo+'\" onClick="javascript:getSalesRouteInfo(\'' + list[i].empNo + '\');">';
									tag += '<td>';
									if(list[i].deptNm != null){
										tag += list[i].deptNm;
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].empNo != null){
										tag += list[i].empNo;
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].empNm != null){
										tag += list[i].empNm;
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
			function getSalesRouteInfo(empNo) {
				if (apiMap != null) {
					apiMap.initAll();
				}
				
				// 테이블에 색상 입히기
				$("#userTable tr").removeClass("row-selected");
				$("#tr"+empNo).addClass("row-selected");
				
				var standardDate = $("#standardDate").val();
				if (standardDate == "") {
					alert("조회일을 입력해 주세요.");
					return;
				}
				
				var callType = $(":input:radio[name=callType]:checked").val();

				var params = {
					auth : "reyon",
					empNo : empNo,
					standardDate : standardDate,
				}

				var request = $.ajax({
					url : '/sfe/salesRouteChangeRoute.json',
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
							$("#humanX").empty();
							$("#humanY").empty();
							$("#hospitalX").empty();
							$("#hospitalY").empty();
							$("#humanXText").val("");
							$("#humanYText").val("");
							$("#hospitalXText").val("");
							$("#hospitalYText").val("");
							$("#sfaSalesNoText").val("");
							$("#sabunText").val(empNo);
							$("#btnChange").hide();
							var list = json.list;
							var tag = '';
							if (list.length == 0) {
								tag += '<tr><td colspan="7" class="text-center">결과가 존재하지 않습니다.</td></tr>';
							} else {
								for (var i = 0; i < list.length; i++) {
									if(callType == 1) {
										tag += '<tr id=\"tr'+list[i].sfaSalesNo+'\" onClick="javascript:getRouteMap(\'' + list[i].sfaSalesNo + '\',' + list[i].gpsStartNum1 + ',' + list[i].gpsStartNum2 + ',\'' + list[i].sfaCustNm + '\');">';	
									} else if(callType == 2) {
										tag += '<tr id=\"tr'+list[i].sfaSalesNo+'\" onClick="javascript:getRouteMap(\'' + list[i].sfaSalesNo + '\',' + list[i].gpsEndNum1 + ',' + list[i].gpsEndNum2 + ',\'' + list[i].sfaCustNm + '\');">';										
									}	
									tag += '<td>';
									if(list[i].sfaCustNm != null){
										tag += list[i].sfaCustNm;
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].gpsStartNum1 != null){
										tag += list[i].gpsStartNum1;
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].gpsStartNum2 != null){
										tag += list[i].gpsStartNum2;
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].startStatus != null){
										tag += list[i].startStatus;
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].gpsEndNum1 != null){
										tag += list[i].gpsEndNum1;
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].gpsEndNum2 != null){
										tag += list[i].gpsEndNum2;
									}
									tag += '</td>';
									tag += '<td>';
									if(list[i].endStatus != null){
										tag += list[i].endStatus;
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
			
			// 맵에서 해당 위치의 마커와 줌하는 함수
			function getRouteMap(sfaSalesNo, gpsEndNum1, gpsEndNum2, message){
				if (apiMap != null) {
					apiMap.initAll();
				}
				
				// 테이블에 색상 입히기
				$("#routeTable tr").removeClass("row-selected");
				$("#tr"+sfaSalesNo).addClass("row-selected");
				
				if(gpsEndNum1 != null && gpsEndNum2 != null){
					var epsg3857 = "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs";
					var epsg4326 = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";
					var entX = Number(gpsEndNum2);
					var entY = Number(gpsEndNum1);
					var projection = proj4(epsg4326, epsg3857, [entX, entY]);
					var lon = projection[0];
					var lat = projection[1];
					addMarker(lon, lat, '담당자', '/img/pin/pin-run.png');
					apiMap.setCenterAndZoom(lon, lat, 16);
					$("#humanX").text(gpsEndNum1);
					$("#humanY").text(gpsEndNum2);
					$("#humanXText").val(gpsEndNum1);
					$("#humanYText").val(gpsEndNum2);
					$("#sfaSalesNoText").val(sfaSalesNo);
				}
				
				var params = {
					auth : "reyon",
					sfaSalesNo : sfaSalesNo,
				}

				var request = $.ajax({
					url : '/sfe/salesRouteChangePosition.json',
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
							var info = json.info;
							var gpsEndNum1 = info.gpsLatitude;
							var gpsEndNum2 = info.gpsLongitude;
							var epsg3857 = "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs";
							var epsg4326 = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";
							var entX = Number(gpsEndNum2);
							var entY = Number(gpsEndNum1);
							var projection = proj4(epsg4326, epsg3857, [entX, entY]);
							var lon = projection[0];
							var lat = projection[1];
							$("#hospitalX").text(gpsEndNum1);
							$("#hospitalY").text(gpsEndNum2);
							$("#hospitalXText").val(gpsEndNum1);
							$("#hospitalYText").val(gpsEndNum2);
							$("#btnChange").show();
							addMarker(lon, lat, message, '/img/pin/pin-cross.png');
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
			
			// 좌표 변경 함수
			function goChange() {
				var sfaSalesNo = $("#sfaSalesNoText").val();
				var humanX = $("#humanXText").val();
				var humanY = $("#humanYText").val();
				var empNo = $("#sabunText").val();
				
				if(sfaSalesNo == "" || humanX == "" || humanY == ""){
					alert("담당자 좌표 정보가 없어 거래처 좌표를 변경할 수 없습니다.\n담당자 시작콜 발생 이후 실행 가능합니다.");
					return;
				}
				if(empNo == ""){
					alert("선택한 사번 정보가 없습니다.");
					return;
				}
				
				if (confirm("거래처 좌표를 담당자 좌표로 변경합니다.\n정말로 변경하시겠습니까?")) {
					var params = {
						auth : "reyon",
						sfaSalesNo : sfaSalesNo,
						humanX : humanX,
						humanY : humanY,
					}

					var request = $.ajax({
						url : '/sfe/salesRouteChangeAccount.json',
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
								alert("거래처 좌표를 담당자 좌표로 변경 완료 하였습니다.");
								getSalesRouteInfo(empNo);
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
								<i class="fa fa-mobile"></i> 기준 좌표 변경
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-briefcase"></i>SFE 관리</li>
								<li><i class="fa fa-mobile"></i>기준 좌표 변경</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">기준 좌표 변경</header>
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
													<div class="col-lg-2 control-label">변경기준</div>
													<div class="col-lg-10">
														<label class="checkbox-inline">
															<input type="radio" name="callType" id="callType1" value="1" checked> 시작콜
														</label>
														<label class="checkbox-inline">
															<input type="radio" name="callType" id="callType2" value="2"> 종료콜
														</label>
													</div>
												</div>
												<div class="row">
													<div class="col-lg-12" style="height: 330px; overflow-y: scroll;">
														<table id="userTable" class="table table-hover personal-task">
															<thead>
																<tr>
																	<th class="text-center">부서</th>
																	<th class="text-center">사번</th>
																	<th class="text-center">이름</th>
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
																	<th class="text-center">거래처</th>
																	<th class="text-center">시작위도</th>
																	<th class="text-center">시작경도</th>
																	<th class="text-center">상태</th>
																	<th class="text-center">종료위도</th>
																	<th class="text-center">종료경도</th>
																	<th class="text-center">상태</th>
																</tr>
															</thead>
															<tbody id="routeBody">
															</tbody>
														</table>
													</div>
												</div>
											</div>
											<div class="col-lg-6">
												<div class="row">
													<div class="col-lg-2 control-label">거래처 좌표
														<input type="hidden" id="hospitalXText">
														<input type="hidden" id="hospitalYText">
														<input type="hidden" id="humanXText">
														<input type="hidden" id="humanYText">
														<input type="hidden" id="sfaSalesNoText">
														<input type="hidden" id="sabunText">
													</div>
													<div class="col-lg-4"><p class="form-control-static" id="hospitalX"></p></div>
													<div class="col-lg-4"><p class="form-control-static" id="hospitalY"></p></div>
													<div class="col-lg-2"><a class="btn btn-default" id="btnChange" href="javascript:;" title="변경" onClick="javascript:goChange();" style="display:none;"><span class="icon_check"></span>&nbsp;변경</a></div>
												</div>
												<div class="row" style="margin-bottom: 10px;">
													<div class="col-lg-2 control-label">담당자 좌표</div>
													<div class="col-lg-4"><p class="form-control-static" id="humanX"></p></div>
													<div class="col-lg-4"><p class="form-control-static" id="humanY"></p></div>
												</div>
												<div class="row">
													<div id="vmap" style="width: 100%; height: 650px; left: 0px; top: 0px"></div>
												</div>
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