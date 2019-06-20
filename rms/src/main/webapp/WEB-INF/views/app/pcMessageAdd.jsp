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
		<!-- page script -->
		<script type="text/javascript">
		
		$(function() {
			// 진입 시 메뉴 선택
			cfLNBMenuSelect("menu09");
			
			// datetimepicker
			$('#sendDateTime').datetimepicker({
				format: 'YYYY-MM-DD HH:mm',
				sideBySide: true,
			});
			
			$("#jsGrid").jsGrid({
	               width: "100%",
	               height: "400px",
	               autoload : true,
	               inserting: false,
	               editing: false,
	               sorting: true,
	               paging: false,
	               controller : {
	                   loadData : function(filter) {
	                       var def = $.Deferred();
	                       $.ajax({
	                           url : '/common/getTotalUserListAjax.json?auth=reyon',
	                           type : 'GET',
	                           contentType : 'application/json; charset=utf-8',
	                           dataType : 'json'
	                       }).done(function(item) {
	                           def.resolve(item.resultMsg);
	                       });
	                       return def.promise();
	                   }
	               },
	               fields: [
						{
	                    	 headerTemplate: function() {return $("<input>").attr("type", "checkbox").attr("id", "selectAllCheckbox")},
	                    	 itemTemplate: function(_, item) {
							  	 return $("<input>").attr("type", "checkbox").attr("class", "singleCheckbox").prop("checked", $.inArray(item.sabun, selectedItemsSabun) > -1).on("change", function () {
							  	 	$(this).is(":checked") ? selectItem($(this).parent().next().text(),$(this).parent().next().next().next().text()) : unselectItem($(this).parent().next().text(),$(this).parent().next().next().next().text());
							  	 });
						 	 }, align: "center", width: 10, sorting: false
						},
						{ title: "사번", name: "sabun", type: "text", align: "center" },
						{ title: "부서", name: "deptName", type: "text", align: "center" },
						{ title: "이름", name: "kname", type: "text", align: "center" },
						{ title: "직급", name: "posLog", type: "text", align: "center" },
	               ]
	           });
	           
	           $("#selectAllCheckbox").change(function(item) {
		   			selectedItemsSabun = [];
		   			selectedItemsName = [];
		   			if(this.checked) {
		   				$('.singleCheckbox').each(function() {
		   					this.checked = true;   
		   					selectItem($(this).parent().next().text(),$(this).parent().next().next().next().text());
		   				});			         
		   			} else {
		   				$('.singleCheckbox').each(function() {
		   					this.checked = false;
		   					unselectItem($(this).parent().next().text(),$(this).parent().next().next().next().text());
		   				});  
		   				selectedItemsSabun = [];
		   				selectedItemsName = [];
		   			}
		   		});
		});
		
		// 체크박스 선택된 값 변수
		var selectedItemsSabun = [];
		var selectedItemsName = [];
		
		var selectItem = function(item1,item2) {
			selectedItemsSabun.push(item1);
			selectedItemsName.push(item2);
			if($(".singleCheckbox").length == $(".singleCheckbox:checked").length) {
				$("#selectAllCheckbox").prop("checked", true);
			} else {
				$("#selectAllCheckbox").prop("checked", false);
			}
		};
	     
		var unselectItem = function(item1, item2) {
			selectedItemsSabun = $.grep(selectedItemsSabun, function(i) {
				return i !== item1;
			});
			selectedItemsName = $.grep(selectedItemsName, function(i) {
				return i !== item2;
			});
			if(selectedItemsSabun.length == 0) {
				$('#selectAllCheckbox').attr('checked', false);
			}
			if(selectedItemsName.length == 0) {
				$('#selectAllCheckbox').attr('checked', false);
			}
			if($(".singleCheckbox").length == $(".singleCheckbox:checked").length) {
				$("#selectAllCheckbox").prop("checked", true);
			} else {
				$("#selectAllCheckbox").prop("checked", false);
			}
		};
		
		// 발송 등록
		function addMessage() {
			var sabunList = "";
			var nameList = "";
			for (var i=0; i<selectedItemsSabun.length; i++) {
				sabunList += selectedItemsSabun[i] + ",";
				nameList += selectedItemsName[i] + ",";
			}
			
			var contents = $("#contents").val();
			var sendDate = $("#sendDate").val();
			
			if (sabunList == "" || nameList == "") {
				alert("전송대상을 선택해 주세요.");
				return;
			}
			if (contents == "") {
				alert("내용을 입력해 주세요.");
				return;
			}
			if (sendDate == "") {
				alert("발송일시을 입력해 주세요.");
				return;
			}
			
			if(confirm("전송하시겠습니까?")){
				var params = {
					auth : "reyon",
					contents : contents,
					sendDate : sendDate,
					sabunList : sabunList,
				}
				
				var request = $.ajax({
					url: '/app/pcMessageAddAjax.json'
					, type : 'POST'
					, timeout: 60000 // 1분 대기
					, data : params
					, dataType : 'json'
					, beforeSend: function(xmlHttpRequest) {
						cfOpenMagnificPopup();
					}
					, error: function(xhr, textStatus, errorThrown) {
						alert("시스템 오류가 발생했습니다.");
					}
					, success : function(json) {
						if (json.resultCode == 0){
							alert("총 "+json.resultMsg+"건 저장 완료 되었습니다.");
							location.href = "/app/pcMessageList.do";
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
								<i class="fa fa-list"></i> PC 메시지 전송 목록
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-desktop"></i>PC 관리</li>
								<li><i class="fa fa-list"></i>PC 메시지 전송 목록</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- body top start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">메시지 전송 정보</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">내용</label>
											<div class="col-lg-10">
												<input type="text" id="contents" class="form-control">
											</div>
										</div>
										<div class="form-group">
											<label class="col-lg-2 control-label">발송일시</label>
											<div class="col-lg-10">
												<div class="input-group date" id="sendDateTime">
								                    <input type="text" id="sendDate" class="form-control" />
								                    <span class="input-group-addon">
								                        <span class="glyphicon glyphicon-calendar"></span>
								                    </span>
								                </div>
											</div>
										</div>
									</form>
								</div>
							</section>
						</div>
					</div>
					<!-- body top end -->
					
					<br>
					
					<!-- userlist start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel" style="margin-bottom: 5px;">
								<header class="panel-heading">메시지 전송 대상</header>
								<div class="panel-body">
									<div id="jsGrid"></div>
								</div>
						</div>
					</div>
					<!-- userlist end -->
					
					<!-- bottom button start -->
					<div class="btn-row text-right">
						<a class="btn btn-default" href="javascript:;" title="등록하기" style="margin-top: 7px;" onClick="javascript:addMessage();"><span class="icon_check"></span>&nbsp;등록</a>
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