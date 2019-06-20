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
		<!-- custom script for this page-->
		<script type="text/javascript" src="/js/scripts.js?ver=20190619"></script>
		<script type="text/javascript" src="/js/jquery.autosize.min.js"></script>
		<script type="text/javascript" src="/js/jquery.placeholder.min.js"></script>
		<script type="text/javascript" src="/js/jquery.slimscroll.min.js"></script>
		<script type="text/javascript" src="/js/jquery.easy-autocomplete.min.js"></script>
		<!-- jsgrid -->
		<script type="text/javascript" src="/js/jsgrid.min.js"></script>
		<!-- common function -->
		<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
		<!-- page script -->
		<script type="text/javascript">
		
		$(function() {
			// 진입 시 메뉴 선택
			cfLNBMenuSelect("menu99");
			
			// 직원 리스트 ajax
			getTotalUserListAjax();
			
			// datetimepicker
			$('#startDatetime').datetimepicker({
				format: 'YYYY-MM-DD HH',
				sideBySide: true,
			});
			$('#endDatetime').datetimepicker({
				format: 'YYYY-MM-DD HH',
				sideBySide: true,
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
									var deptCode = $("#kname").getSelectedItemData().deptCode;
									var deptName = $("#kname").getSelectedItemData().deptName;
									var sabun = $("#kname").getSelectedItemData().sabun;
									var name = $("#kname").getSelectedItemData().kname;
									$("#sabun").val(sabun);
								},
							}
						};
						$("#kname").easyAutocomplete(options1);
					} else if (json.resultCode == 1201) {
						alert(json.resultMsg);
						cfLogin();
					} else {
						alert(json.resultMsg);
					}
				}
			});
		}
		
		// 등록
		function goConfirm() {
			var sabun = $("#sabun").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var accessIngType = $("#accessIngType").val();
			var accessEndType = $("#accessEndType").val();
			var reqComment = $("#reqComment").val();
			
			if (sabun == "") {
				alert("신청인을 입력해 주세요.");
				return;
			}
			if (startDate == "") {
				alert("시작시간을 입력해 주세요.");
				return;
			}
			if (endDate == "") {
				alert("종료시간을 입력해 주세요.");
				return;
			}
			if (accessIngType == "") {
				alert("해당시간 중 적용 타입 입력해 주세요.");
				return;
			}
			if (accessEndType == "") {
				alert("해당시간 후 적용 타입을 입력해 주세요.");
				return;
			}
			
			if(confirm("등록하시겠습니까?")){
				var params = {
					auth : "reyon",
					sabun : sabun,
					startDate : startDate,
					endDate : endDate,
					accessIngType : accessIngType,
					accessEndType : accessEndType,
					reqComment : reqComment,
				}
				
				var request = $.ajax({
					url: '/itt/groupwareExtAddAjax.json'
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
							location.href = "/itt/groupwareExtList.do";
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
								<i class="fa fa-pencil"></i> 그룹웨어 외부접속 등록
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-star-o"></i>정보관리팀</li>
								<li><i class="fa fa-users"></i>그룹웨어 외부접속</li>
								<li><i class="fa fa-pencil"></i>그룹웨어 외부접속 등록</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">신청인</label>
											<div class="col-lg-10">
												<input type="text" id="kname" class="form-control" maxlength="50" />
												<input type="hidden" id="sabun" class="form-control" />
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">시작시간</label>
											<div class="col-lg-10">
												<div class="row">
													<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
														<div class="input-group date" id="startDatetime">
										                    <input type="text" id="startDate" class="form-control" />
										                    <span class="input-group-addon">
										                        <span class="glyphicon glyphicon-calendar"></span>
										                    </span>
										                </div>
													</div>
												</div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">종료시간</label>
											<div class="col-lg-10">
												<div class="row">
													<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
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
											<label class="col-lg-2 control-label">해당시간 중 적용 타입</label>
											<div class="col-lg-10">
												<select id="accessIngType" class="form-control">
													<option value="1" selected>모든 IP 접속허용</option>
													<option value="2">접속관리 IP만 허용</option>
													<option value="3">모든 IP 접속거부</option>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">해당시간 후 적용 타입</label>
											<div class="col-lg-10">
												<select id="accessEndType" class="form-control">
													<option value="1">모든 IP 접속허용</option>
													<option value="2" selected>접속관리 IP만 허용</option>
													<option value="3">모든 IP 접속거부</option>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">사유</label>
											<div class="col-lg-10">
												<input type="text" id="reqComment" class="form-control" maxlength="100" />
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