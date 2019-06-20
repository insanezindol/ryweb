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
		<!-- jsgrid -->
		<script type="text/javascript" src="/js/jsgrid.min.js"></script>
		<!-- custom script for this page-->
		<script type="text/javascript" src="/js/scripts.js?ver=20190619"></script>
		<script type="text/javascript" src="/js/jquery.autosize.min.js"></script>
		<script type="text/javascript" src="/js/jquery.placeholder.min.js"></script>
		<script type="text/javascript" src="/js/jquery.slimscroll.min.js"></script>
		<script type="text/javascript" src="/js/jquery.easy-autocomplete.min.js"></script>
		<!-- common function -->
		<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
		<!-- proj4 api -->
		<script type="text/javascript" src="/js/proj4.js"></script>
		<!-- summernote editor -->
		<link href="/css/summernote.css" rel="stylesheet">
		<script type="text/javascript" src="/js/summernote.js"></script>
		<script type="text/javascript" src="/js/summernote-ko-KR.js"></script>
		<!-- vworld map api -->
		<script type="text/javascript" src="http://map.vworld.kr/js/vworldMapInit.js.do?apiKey=CCF38AFF-389D-35DB-A32A-1683474BEF12"></script>
		<!-- page script -->
		<script type="text/javascript">
		
			$(function() {
				// 진입 시 메뉴 선택
				cfLNBMenuSelect("menu07");
				
				// datetimepicker
				$('#startDatetime').datetimepicker({
					format: 'YYYY-MM-DD',
					sideBySide: true,
				});
				$('#endDatetime').datetimepicker({
					format: 'YYYY-MM-DD',
					sideBySide: true,
					useCurrent: false,
				});
				$("#startDatetime").on("dp.change", function (e) {
		            $('#endDatetime').data("DateTimePicker").minDate(e.date);
		        });
		        $("#endDatetime").on("dp.change", function (e) {
		            $('#startDatetime').data("DateTimePicker").maxDate(e.date);
		        });
		        
		     	// 검색어 입력영역에 엔터키 이벤트 바인드
				$("#keyword").keydown(function (key) {
				    if (key.keyCode == 13) getAddr();
				});
		     	
				// 용도 구분 변경 시
				$("#division").change(function() { 
					var division = $(this).val();
					$("#officeDiv").hide();
					$("#personDiv").hide();
					if (division == "사무실용") {
						$("#officeDiv").show();
					} else {
						$("#personDiv").show();
					}
				});
				
				// 용도 구분 변경 시
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
				
				// 지급구분이 보유인 경우 계약종료일자는 2099-12-31, readonly으로 세팅
				$("#payment").change(function() { 
					var payment = $(this).val();
					if(payment == "보유"){
						$("#endDate").val("2099-12-31");
						$("#endDate").prop("readonly", true);
					} else {
						$("#endDate").prop("readonly", false);
					}
				});
				
				// 첨부파일 조건 변경 시
				$("#attFileType").change(function() { 
					var attFileType = $("#attFileType").val();
					$("#attFileTypeP01").hide();
					$("#attFileTypeP02").hide();
					$("#attFileTypeP03").hide();
					$("#attFileTypeP"+attFileType).show();
				});
				
				// 불러온 값 설정
				$("#saupGubun").val("${info.saupGubun}").prop("selected", true);
				$("#division").val("${info.division}").prop("selected", true);
				$("#divisionText").text("${info.division }");
				$("#username").val("${info.username }");
				$("#startDate").val("${info.startDate }");
				$("#endDate").val("${info.endDate }");
				$("#status").val("${info.status}").prop("selected", true);
				$("#roadAddr").val("${info.roadAddr }");
				$("#jibunAddr").val("${info.jibunAddr }");
				$("#zipno").val("${info.zipno }");
				$("#sinm").val("${info.sinm }");
				$("#positionX").val("${info.positionX }");
				$("#positionY").val("${info.positionY }");
				$("#detailAddr").val("${info.detailAddr }");
				$("#payment").val("${info.payment}").prop("selected", true);
				$("#deposit").val("${info.deposit }");
				$("#rent").val("${info.rent }");
				$("#administrativeExpenses").val("${info.administrativeExpenses }");
				
				// 지급구분이 보유인 경우 계약종료일자는 readonly 세팅
				if( $("#payment").val() == "보유" ){
					$("#endDate").prop("readonly", true);
				}
				
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
				
				// VWORLD MAP API 호출
				loadVwordMap();
			});
			
			// VWORLD MAP API 호출
			var apiMap;
			var marker;
			function loadVwordMap() {
				var lon = "${info.positionX }";
				var lat = "${info.positionY }";
				var message = "도로명 주소 : ${info.roadAddr } ${info.detailAddr }<br>지번 주소 : ${info.jibunAddr } ${info.detailAddr }";
				
				vworld.showMode = false; 
				vworld.init("vmap", "map-first", function() {
					apiMap = this.vmap;
					apiMap.setBaseLayer(apiMap.vworldBaseMap);
					apiMap.setControlsType({"simpleMap":true});
					apiMap.addVWORLDControl("zoomBar");
					marker = new vworld.Marker(lon, lat, message, "");
				    marker.setZindex(3);
				    apiMap.addMarker(marker);
				    apiMap.setCenterAndZoom(lon, lat, 16);
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
			
			// 수정
			function goConfirm() {
				var contractSeq = $("#contractSeq").val();
				var saupGubun = $("#saupGubun").val();
				var division = $("#division").val();
				var username = $("#username").val();
				var startDate = $("#startDate").val();
				var endDate = $("#endDate").val();
				var status = $("#status").val();
				var roadAddr = $("#roadAddr").val();
				var jibunAddr = $("#jibunAddr").val();
				var detailAddr = $("#detailAddr").val();
				var sinm = $("#sinm").val();
				var positionX = $("#positionX").val();
				var positionY = $("#positionY").val();
				var zipno = $("#zipno").val();
				var payment = $("#payment").val();
				var deposit = $("#deposit").val();
				var rent = $("#rent").val();
				var administrativeExpenses = $("#administrativeExpenses").val();
				//var remarks = $("#remarks").val();
				var remarks = $("#remarks").summernote("code");
				var documentFile = $("#documentFile")[0].files[0];
				var attFileType = $("#attFileType").val();
				
				if (saupGubun == "") {
					alert("지점구분을 선택해 주세요.");
					return;
				}
				if (division == "") {
					alert("용도구분을 선택해 주세요.");
					return;
				}
				if (username == "") {
					alert("사용자를 입력해 주세요.");
					return;
				}
				if (startDate == "" || endDate == "") {
					alert("계약기간/매입일을 입력해 주세요.");
					return;
				}
				if (status == "") {
					alert("상태를 입력해 주세요.");
					return;
				}
				if (roadAddr == "" || jibunAddr == "" || sinm == "" || zipno == "") {
					alert("소재지를 입력해 주세요.");
					return;
				}
				if (payment == "") {
					alert("지급구분을 선택해 주세요.");
					return;
				}
				if(cfGetByteLength(remarks) > 4000) {
					alert("[비고] 항목의 최대 길이를 초과하였습니다.\n수정 후 다시 시도해주세요.\n- 최대길이 : 4000 byte\n- 현재길이 : " + cfGetByteLength(remarks) + " byte");
					return;
				}
				if(positionX == "" || positionY == "" ) {
					alert("좌표정보가 없습니다. 정보관리팀으로 문의 바랍니다.");
					return;
				}
				
				// 금액란이 비어있는경우 0으로 초기화
				if (deposit == "") {
					deposit = 0;
				}
				if (rent == "") {
					rent = 0;
				}
				if (administrativeExpenses == "") {
					administrativeExpenses = 0;
				}
				
				if ($("#documentFile").get(0).files.length !== 0) {
					if($("#documentFile")[0].files[0].size/1024/1024 > 20){
						alert("첨부파일은 20MB를 초과 할 수 없습니다.");
						return;
					}
				}
				
				//remarks = remarks.replace(/\n/g, "<br>");
				
				if (confirm("수정하시겠습니까?")) {
					var formData = new FormData();
					formData.append("auth", "reyon");
					formData.append("contractSeq", contractSeq);
					formData.append("saupGubun", saupGubun);
					formData.append("division", division);
					formData.append("username", username);
					formData.append("startDate", startDate);
					formData.append("endDate", endDate);
					formData.append("status", status);
					formData.append("roadAddr", roadAddr);
					formData.append("jibunAddr", jibunAddr);
					formData.append("detailAddr", detailAddr);
					formData.append("sinm", sinm);
					formData.append("positionX", positionX);
					formData.append("positionY", positionY);
					formData.append("zipno", zipno);
					formData.append("payment", payment);
					formData.append("deposit", deposit);
					formData.append("rent", rent);
					formData.append("administrativeExpenses", administrativeExpenses);
					formData.append("remarks", remarks);
					formData.append("documentFile", documentFile);
					formData.append("attFileType", attFileType);
					
					var request = $.ajax({
						url: '/realestate/realestateModifyAjax.json'
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
								var url = "/realestate/realestateView.do?contractSeq=" + contractSeq;
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
			
			// 도로명 주소 검색 팝업
			function openAddrModal(){
				$("#addrModal").modal();
			}
			
			// 도로명 주소 검색 API
			function getAddr(){
				var keyword = $("#keyword").val();
				
				if (keyword == "") {
					alert("소재지를 입력해 주세요.");
					return;
				}
				
				var params = {
					currentPage : 1,
					countPerPage : 100,
					resultType : "json",
					confmKey : "U01TX0FVVEgyMDE4MDUxNjE0MDEyMjEwNzg4MDQ=",
					keyword : keyword,
				}
				
				$.ajax({
					 url :"http://www.juso.go.kr/addrlink/addrLinkApiJsonp.do"
					,type:"post"
					,data:params
					,dataType:"jsonp"
					,crossDomain:true
					, beforeSend: function(xmlHttpRequest) {
						cfOpenMagnificPopup();
					}
					,success:function(jsonStr){
						$("#list").html("");
						var errCode = jsonStr.results.common.errorCode;
						var errDesc = jsonStr.results.common.errorMessage;
						if(errCode != "0"){
							alert(errCode+"="+errDesc);
						}else{
							if(jsonStr != null){
								makeListJson(jsonStr);
							}
						}
					}
				    ,error: function(xhr,status, error){
				    	alert("주소 연동 에러 발생");
				    }
				    , complete : function() {
						cfCloseMagnificPopup();
					}
				});
			}
			
			// 주소 검색 결과를 보여줌
			function makeListJson(jsonStr){
				var grid = $("#jsGrid").jsGrid({
				       width: "100%",
				       height: "400px", 
				       autoload: false,
				       inserting: false,
				       editing: false,
				       paging: false,
				       data: jsonStr.results.juso,
				       fields: [
							{ title: "우편번호", name: "zipNo", type: "text", align: "center" },
							{ title: "도로명주소", name: "roadAddr", type: "text", align: "center" },
							{ title: "지번주소", name: "jibunAddr", type: "text", align: "center" },
				       ],
						rowClick: function(args) {
							var data = args.item;
							$("#roadAddr").val(data.roadAddr);
							$("#jibunAddr").val(data.jibunAddr);
							$("#zipno").val(data.zipNo);
							$("#sinm").val(data.siNm);
							// 좌표 연동 호출
							getXYPosition(data);
						}
				   });
			}
			
			// 좌표 연동 후 좌표 변환
			function getXYPosition(data){
				var params = {
						confmKey : "U01TX0FVVEgyMDE4MTEwNzE2MzQ0OTEwODI4NTg=",
						admCd : data.admCd,
						rnMgtSn : data.rnMgtSn,
						udrtYn : data.udrtYn,
						buldMnnm : data.buldMnnm,
						buldSlno : data.buldSlno,
						resultType : "json",
					}
					
					$.ajax({
						 url :"http://www.juso.go.kr/addrlink/addrCoordApiJsonp.do"
						,type:"post"
						,data:params
						,dataType:"jsonp"
						,crossDomain:true
						, beforeSend: function(xmlHttpRequest) {
							cfOpenMagnificPopup();
						}
						,success:function(jsonStr){
							var epsg5179 = "+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +units=m +no_defs";
							var epsg3857 = "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs";
							var entX = Number(jsonStr.results.juso[0].entX);
							var entY = Number(jsonStr.results.juso[0].entY);
							var projection = proj4(epsg5179, epsg3857, [entX, entY]);
							var positionX = projection[0];
							var positionY = projection[1];
							$("#positionX").val(positionX);
							$("#positionY").val(positionY);
							
							// 해당좌표로 지도 이동 및 마커 생성
							apiMap.initAll();
						    marker = new vworld.Marker(positionX, positionY, "도로명 주소 : " + data.roadAddr + "<br>지번 주소 : " + data.jibunAddr, "");
						    marker.setZindex(3);
						    apiMap.addMarker(marker);
						    apiMap.setCenterAndZoom(positionX, positionY, 16);
							
							$("#addrModal").modal("hide");
						}
					    ,error: function(xhr,status, error){
					    	alert("좌표 연동 에러 발생");
					    }
					    , complete : function() {
							cfCloseMagnificPopup();
						}
					});
			}
			
			// 첨부파일 다운로드
			function downloadFile(seq, filename){
				location.href="/realestate/realestateFileDownload.do?dwAuth=reyon&dwSeq="+seq+"&dwFilename="+encodeURIComponent(filename);
			}
			
			// 뒤로가기
			function goBack(){
				history.back();
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
								<i class="fa fa-pencil-square-o"></i> 부동산 계약 수정
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-building-o"></i>부동산 계약 관리</li>
								<li><i class="fa fa-pencil-square-o"></i>부동산 계약 수정</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">부동산 계약 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">지점구분</label>
											<div class="col-lg-10">
												<select id="saupGubun" class="form-control">
													<option value="본사">본사</option>
													<option value="진천공장">진천공장</option>
													<option value="충주공장">충주공장</option>
													<option value="연구소">연구소</option>
													<option value="영업지점">영업지점</option>
												</select>
											</div>
										</div>
										<div class="form-group" id="userAddDiv" style="display:none;">
											<label class="col-lg-2 control-label">용도 / 사용자</label>
											<div class="col-lg-2">
												<select id="division" class="form-control">
													<option value="사무실용">사무실용</option>
													<option value="개인용">개인용</option>
												</select>
											</div>
											<div class="col-lg-8">
												<select id="officeDiv" class="form-control">
													<option value="">== 사용자 선택 ==</option>
													<option value="본사">본사</option>
													<option value="진천공장">진천공장</option>
													<option value="충주공장">충주공장</option>
													<option value="서울대연구소">서울대연구소</option>
													<option value="인천팀">인천팀</option>
													<option value="대전팀">대전팀</option>
													<option value="부산팀">부산팀</option>
													<option value="대구팀">대구팀</option>
													<option value="광주팀">광주팀</option>
													<option value="전주팀">전주팀</option>
													<option value="창원팀">창원팀</option>
													<option value="원주팀">원주팀</option>
												</select>
												<input type="text" id="personDiv" class="form-control" maxlength="10" style="display:none;">
											</div>
										</div>
										<div class="form-group" id="userRemoveDiv">
											<label class="col-lg-2 control-label">용도 / 사용자</label>
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
													<option value="월세">월세</option>
													<option value="연납">연납</option>
													<option value="전세">전세</option>
													<option value="보유">보유</option>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">계약기간/매입일</label>
											<div class="col-lg-10">
												<div class="row" style="margin-top: 7px;">
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
											<label class="col-lg-2 control-label">상태</label>
											<div class="col-lg-10">
												<select id="status" class="form-control">
													<option value="ING">진행중</option>
													<option value="END">종료</option>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">소재지</label>
											<div class="col-lg-5">
												<input type="text" id="roadAddr" class="form-control" placeholder="도로명 주소" readonly>
												<input type="hidden" id="jibunAddr" />
												<input type="hidden" id="zipno" />
												<input type="hidden" id="sinm" />
												<input type="hidden" id="positionX" />
												<input type="hidden" id="positionY" />
											</div>
											<div class="col-lg-3">
												<input type="text" id="detailAddr" class="form-control" maxlength="50" placeholder="나머지 주소" />
											</div>
											<div class="col-lg-2">
												<a class="btn btn-default" href="javascript:;" title="주소 검색 팝업 열기" onClick="javascript:openAddrModal();"><span class="icon_search"></span>&nbsp;주소 검색</a>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">지도</label>
											<div class="col-lg-10">
												<div id="vmap" style="width: 100%; height: 500px; left: 0px; top: 0px"></div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">보증금/매입금</label>
											<div class="col-lg-10">
												<input type="number" id="deposit" class="form-control">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">임대료</label>
											<div class="col-lg-10">
												<input type="number" id="rent" class="form-control">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">관리비</label>
											<div class="col-lg-10">
												<input type="number" id="administrativeExpenses" class="form-control">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">비고</label>
											<div class="col-lg-10">
												<div id="remarks">${info.remarks }</div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">첨부파일 변경 항목</label>
											<div class="col-lg-10">
												<select id="attFileType" class="form-control" style="margin-bottom: 10px;">
													<option value="01">첨부파일 변경없음</option>
													<option value="02">새로운 첨부파일 사용</option>
													<option value="03">현재 첨부파일 삭제</option>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">첨부파일(계약서)</label>
											<div class="col-lg-10">
												<p class="form-control-static" id="attFileTypeP01" style="display:block;">
													<c:choose>
														<c:when test="${info.attachFilename == '' || info.attachFilename == null }">첨부파일이 없습니다.</c:when>
														<c:otherwise><a href="javascript:downloadFile('${info.contractSeq }','${info.attachFilename }');">${info.attachFilename }</a></c:otherwise>
													</c:choose>
												</p>
												<p class="form-control-static" id="attFileTypeP02" style="display:none;"><input type="file" id="documentFile"></p>
												<p class="form-control-static" id="attFileTypeP03" style="display:none;">현재 등록 파일이 삭제됩니다.</p>
												
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">등록인</label>
											<div class="col-lg-10">
												<p class="form-control-static">${info.regName }</p>
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
						<input type="hidden" id="contractSeq" value="${info.contractSeq }" />
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
		<div aria-hidden="true" aria-labelledby="addrModalLabel" role="dialog" tabindex="-1" id="addrModal" class="modal fade">
			<div class="modal-dialog" style="width: 70%;">
				<div class="modal-content">
					<div class="modal-header">
						<button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
						<h4 class="modal-title">도로명 주소 검색 (검색결과 최대 100건)</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal" role="form">
							<div class="form-group">
								<div class="col-lg-12">
									<div class="input-group">
										<input type="text" style="display: none;" />
					                    <input type="text" id="keyword" class="form-control" placeholder="검색어 입력" />
					                    <span class="input-group-addon" onClick="javascript:getAddr();">
					                        <span class="glyphicon glyphicon-search" style="cursor: pointer;"></span>
					                    </span>
					                </div>
								</div>
							</div>
						</form>
						<div id="jsGrid" style="height:400px; width:100%;"></div>
						<div style="height:20px;"></div>
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