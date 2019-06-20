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
				cfLNBMenuSelect("menu99");
				
				// VWORLD MAP API 호출
				loadVwordMap();
			});
			
			var apiMap;
			var mControl; //마커이벤트변수
			var marker;
			
			// VWORLD MAP API 호출
			function loadVwordMap() {
				vworld.showMode = false; 
				vworld.init("vmap", "map-first", function() {
					apiMap = this.vmap;
					apiMap.setBaseLayer(apiMap.vworldBaseMap);
					apiMap.setControlsType({"simpleMap":true});
					apiMap.addVWORLDControl("zoomBar");
				});
			}
			
			function addMarkingEvent() {
				apiMap.initAll();
				var pointOptions = { persist : true };
				if (mControl == null) {
					mControl = new OpenLayers.Control.Measure(
							OpenLayers.Handler.Point, {
								handlerOptions : pointOptions
							});
					mControl.events.on({
						"measure" : mClick
					});
					apiMap.addControl(mControl);
				}
				apiMap.init();
				mControl.activate();
			}
			
			function mClick(event) {
				apiMap.init();  
				var temp = event.geometry;  
				var pos = new OpenLayers.LonLat(temp.x, temp.y);
				addMarker(pos.lon, pos.lat, "새로운 위치", null);
				convertPosition(pos);
			}
			
			function convertPosition(pos){
				$("#espg3857txt").text("EPSG-3857 : " + pos.lat + " / " + pos.lon);
				var epsg3857 = "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs";
				var epsg5179 = "+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +units=m +no_defs";
				var epsg4326 = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";
				var entX = Number(pos.lon);
				var entY = Number(pos.lat);
				var projection = proj4(epsg3857, epsg4326, [entX, entY]);
				var positionX = projection[0];
				var positionY = projection[1];
				$("#espg4326txt").text("EPSG-4326 : " + positionY + " / " + positionX);
			}
			
			function addMarker(lon, lat, message, imgurl) {
				marker = new vworld.Marker(lon, lat, message, "");
				if (typeof imgurl == 'string') {
					marker.setIconImage(imgurl);
				}
				marker.setZindex(3);
				apiMap.addMarker(marker);
				apiMap.setCenterAndZoom(lon, lat, 16);
			}
			
		</script>
		<style> body { font-family: 'Noto Sans KR', sans-serif !important;} </style>
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
								<i class="fa fa-map-o"></i> 지도
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-star-o"></i>정보관리팀</li>
								<li><i class="fa fa-map-o"></i>지도</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<br>
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">VMAP</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<div class="col-lg-8">
												<div id="vmap" style="width: 100%; height: 500px; left: 0px; top: 0px"></div>
											</div>
											<div class="col-lg-2">
												<p class="form-control-static" id="espg3857txt"></p>
											</div>
											<div class="col-lg-2">
												<p class="form-control-static" id="espg4326txt"></p>
											</div>
										</div>
										<div class="form-group">
											<div class="col-lg-12">
												<a href="javascript:;" onClick="javascript:addMarkingEvent();">마커 입력</a>
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