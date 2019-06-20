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
			cfLNBMenuSelect("menu14");
			
			// jsgrid
			loadData("");
			
			// 안내 모달 팝업
			$("#descModal").modal();
			
			// 검색어 입력영역에 엔터키 이벤트 바인드
	        $("#searchText").keydown(function (key) {
			    if (key.keyCode == 13) goSearch();
			});
		});
		
		function goSearch() {
			var searchText = $("#searchText").val();
			loadData(searchText);
		}
		
		// jsgrid
		function loadData(searchText) {
			var grid = $("#jsGrid").jsGrid({
			       width: "100%",
			       height: "610px",
			       filtering: false,
			       editing: true,
			       autoload: true,
			       paging: false,
			       inserting: false,
			       sorting: true,
			       controller : {
			           loadData : function(filter) {
			               var def = $.Deferred();
			               $.ajax({
			                   url : '/phone/phonenumberGetAjax.json?auth=reyon&searchText='+searchText,
			                   type : 'GET',
			                   contentType : 'application/json; charset=utf-8',
			                   dataType : 'json'
			               }).done(function(item) {
			                   def.resolve(item.list);
			               });
			               return def.promise();
			           }
			       },
			       rowClick: function(args) {
			    	   showModal("Edit", args.item);
			       },
			       headerRowRenderer: function() {
			           var $result = $("<tr>").height(0)
				           .append($("<th>").width(0))
				           .append($("<th>").width(120))
				           .append($("<th>").width(120))
				           .append($("<th>").width(90))
				           .append($("<th>").width(100))
				           .append($("<th>").width(90))
				           .append($("<th>").width(120))
				           .append($("<th>").width(120))
				           .append($("<th>").width(100))
				           .append($("<th>").width(120))
				           .append($("<th>").width(120))
				           .append($("<th>").width(120))
				           .append($("<th>").width(120))
				           .append($("<th>").width(120))
				           .append($("<th>").width(120))
				           .append($("<th>").width(120))
				           .append($("<th>").width(120))
				           .append($("<th>").width(120));
			           
			           $result = $result.add(
			        		   $("<tr>").attr("class", "jsgrid-header-row")
							   .append($("<th>").attr("rowspan", 2).attr("class", "jsgrid-header-cell jsgrid-align-center jsgrid-header-sortable").text(""))
							   .append($("<th>").attr("rowspan", 2).attr("class", "jsgrid-header-cell jsgrid-align-center jsgrid-header-sortable").text("부서명"))
							   .append($("<th>").attr("rowspan", 2).attr("class", "jsgrid-header-cell jsgrid-align-center jsgrid-header-sortable").text("사번"))
							   .append($("<th>").attr("rowspan", 2).attr("class", "jsgrid-header-cell jsgrid-align-center jsgrid-header-sortable").text("성명"))
							   .append($("<th>").attr("rowspan", 2).attr("class", "jsgrid-header-cell jsgrid-align-center jsgrid-header-sortable").text("직급"))
							   .append($("<th>").attr("rowspan", 2).attr("class", "jsgrid-header-cell jsgrid-align-center jsgrid-header-sortable").text("관리대상"))
							   .append($("<th>").attr("rowspan", 2).attr("class", "jsgrid-header-cell jsgrid-align-center jsgrid-header-sortable").text("노출부서명"))
							   .append($("<th>").attr("rowspan", 2).attr("class", "jsgrid-header-cell jsgrid-align-center jsgrid-header-sortable").text("노출직급"))
							   .append($("<th>").attr("rowspan", 2).attr("class", "jsgrid-header-cell jsgrid-align-center jsgrid-header-sortable").text("노출성명"))
							   .append($("<th>").attr("colspan", 3).attr("class", "jsgrid-header-cell jsgrid-align-center jsgrid-header-sortable").css("border-bottom","1px solid #e9e9e9").text("본사"))
							   .append($("<th>").attr("colspan", 3).attr("class", "jsgrid-header-cell jsgrid-align-center jsgrid-header-sortable").css("border-bottom","1px solid #e9e9e9").text("진천"))
							   .append($("<th>").attr("colspan", 3).attr("class", "jsgrid-header-cell jsgrid-align-center jsgrid-header-sortable").css("border-bottom","1px solid #e9e9e9").text("충주"))
							   );
			           
			           $result = $result.add(
			        		   	$("<tr>").attr("class", "jsgrid-header-row")
							   .append($("<th>").attr("class", "jsgrid-header-cell jsgrid-align-center jsgrid-header-sortable").text("구분"))
							   .append($("<th>").attr("class", "jsgrid-header-cell jsgrid-align-center jsgrid-header-sortable").text("내선"))
							   .append($("<th>").attr("class", "jsgrid-header-cell jsgrid-align-center jsgrid-header-sortable").text("팩스"))
							   .append($("<th>").attr("class", "jsgrid-header-cell jsgrid-align-center jsgrid-header-sortable").text("구분"))
							   .append($("<th>").attr("class", "jsgrid-header-cell jsgrid-align-center jsgrid-header-sortable").text("내선"))
							   .append($("<th>").attr("class", "jsgrid-header-cell jsgrid-align-center jsgrid-header-sortable").text("팩스"))
							   .append($("<th>").attr("class", "jsgrid-header-cell jsgrid-align-center jsgrid-header-sortable").text("구분"))
							   .append($("<th>").attr("class", "jsgrid-header-cell jsgrid-align-center jsgrid-header-sortable").text("내선"))
							   .append($("<th>").attr("class", "jsgrid-header-cell jsgrid-align-center jsgrid-header-sortable").text("팩스"))
							   );
			           
			           return $result;
			       },
			       fields: [
			    	    { name: "phoneSeq", type: "text", visible: false, width: 0 },
			    	    { name: "deptName", type: "text", align: "center", width: 120, editing:false },
						{ name: "username", type: "text", align: "center", width: 120, editing:false },
						{ name: "kname", type: "text", align: "center", width: 90, editing:false },
						{ name: "posLog", type: "text", align: "center", width: 100, editing:false },
						{ name: "isSave", type: "checkbox", width: 90, sorting: false },
						{ name: "viewDept", type: "text", align: "center", width: 120 },
						{ name: "viewPosi", type: "text", align: "center", width: 120 },
						{ name: "viewName", type: "text", align: "center", width: 100 },
						{ name: "phoneType1", type: "select", align: "center", width: 120, items: [{Name: '', Id: '' }, {Name: '일반내선', Id: '1' }, { Name: '기타전화번호', Id: '2' }, { Name: '기타팩스', Id: '3' }], valueField: 'Id', textField: 'Name' },
						{ name: "phonenum1", type: "text", align: "center", width: 120 },
						{ name: "faxnum1", type: "text", align: "center", width: 120 },
						{ name: "phoneType2", type: "select", align: "center", width: 120, items: [{Name: '', Id: '' }, {Name: '일반내선', Id: '1' }, { Name: '기타전화번호', Id: '2' }, { Name: '기타팩스', Id: '3' }], valueField: 'Id', textField: 'Name' },
						{ name: "phonenum2", type: "text", align: "center", width: 120 },
						{ name: "faxnum2", type: "text", align: "center", width: 120 },
						{ name: "phoneType3", type: "select", align: "center", width: 120, items: [{Name: '', Id: '' }, {Name: '일반내선', Id: '1' }, { Name: '기타전화번호', Id: '2' }, { Name: '기타팩스', Id: '3' }], valueField: 'Id', textField: 'Name' },
						{ name: "phonenum3", type: "text", align: "center", width: 120 },
						{ name: "faxnum3", type: "text", align: "center", width: 120 },
			       ]
			   });
		}
		
		function openNewPopup() {
			// 신규 등록
			
			// load default data
			deptName = "";
			posLog = "";
			kname = "";
	    	$("#deptName").text("");
	    	$("#username").text("");
	    	$("#kname").text("");
	    	$("#posLog").text("");
	    	
	    	// set default data
	    	$("#phoneSeq").val("");
	    	$("#sabun").val("");
	    	
    		// 기존 등록 값 삭제
    		$("input:checkbox[id='isSave']").prop("checked", false);
    		$("#viewDept").val("");
    		$("#viewPosi").val("");
    		$("#viewName").val("");
    		$("#phoneType1").val("");
    		$("#phonenum1").val("");
    		$("#faxnum1").val("");
    		$("#phoneType2").val("");
    		$("#phonenum2").val("");
    		$("#faxnum2").val("");
    		$("#phoneType3").val("");
    		$("#phonenum3").val("");
    		$("#faxnum3").val("");
    		// 비활성화
    		$("#viewDept").attr("disabled","disabled");
    		$("#viewPosi").attr("disabled","disabled");
    		$("#viewName").attr("disabled","disabled");
    		$("#phoneType1").attr("disabled","disabled");
    		$("#phonenum1").attr("disabled","disabled");
    		$("#faxnum1").attr("disabled","disabled");
    		$("#phoneType2").attr("disabled","disabled");
    		$("#phonenum2").attr("disabled","disabled");
    		$("#faxnum2").attr("disabled","disabled");
    		$("#phoneType3").attr("disabled","disabled");
    		$("#phonenum3").attr("disabled","disabled");
    		$("#faxnum3").attr("disabled","disabled");
    		
    		// open modal
	    	$("#editModal").modal();
		}
	 	
		// global variable
		var phoneSeq;
    	var deptName;
    	var username;
    	var kname;
    	var posLog;
    	var isSave;
    	var viewDept;
    	var viewPosi;
    	var viewName;
    	var phoneType1;
    	var phonenum1;
    	var faxnum1;
    	var phoneType2;
    	var phonenum2;
    	var faxnum2;
    	var phoneType3;
    	var phonenum3;
    	var faxnum3;
		
	    function showModal(dialogType, client) {
	    	// set global data
	    	phoneSeq = client.phoneSeq;
	    	deptName = client.deptName;
	    	username = client.username;
	    	kname = client.kname;
	    	posLog = client.posLog;
	    	isSave = client.isSave;
	    	viewDept = client.viewDept;
	    	viewPosi = client.viewPosi;
	    	viewName = client.viewName;
	    	phoneType1 = client.phoneType1;
	    	phonenum1 = client.phonenum1;
	    	faxnum1 = client.faxnum1;
	    	phoneType2 = client.phoneType2;
	    	phonenum2 = client.phonenum2;
	    	faxnum2 = client.faxnum2;
	    	phoneType3 = client.phoneType3;
	    	phonenum3 = client.phonenum3;
	    	faxnum3 = client.faxnum3;
	    	
	    	// load default data
	    	$("#deptName").text(deptName);
	    	$("#username").text(username);
	    	$("#kname").text(kname);
	    	$("#posLog").text(posLog);
	    	
	    	// set default data
	    	$("#phoneSeq").val(phoneSeq);
	    	$("#sabun").val(username);
	    	
	    	if (phoneSeq == null || phoneSeq == "") {
	    		// 신규 등록
	    		// 기존 등록 값 삭제
	    		$("input:checkbox[id='isSave']").prop("checked", false);
	    		$("#viewDept").val("");
	    		$("#viewPosi").val("");
	    		$("#viewName").val("");
	    		$("#phoneType1").val("");
	    		$("#phonenum1").val("");
	    		$("#faxnum1").val("");
	    		$("#phoneType2").val("");
	    		$("#phonenum2").val("");
	    		$("#faxnum2").val("");
	    		$("#phoneType3").val("");
	    		$("#phonenum3").val("");
	    		$("#faxnum3").val("");
	    		// 비활성화
	    		$("#viewDept").attr("disabled","disabled");
	    		$("#viewPosi").attr("disabled","disabled");
	    		$("#viewName").attr("disabled","disabled");
	    		$("#phoneType1").attr("disabled","disabled");
	    		$("#phonenum1").attr("disabled","disabled");
	    		$("#faxnum1").attr("disabled","disabled");
	    		$("#phoneType2").attr("disabled","disabled");
	    		$("#phonenum2").attr("disabled","disabled");
	    		$("#faxnum2").attr("disabled","disabled");
	    		$("#phoneType3").attr("disabled","disabled");
	    		$("#phonenum3").attr("disabled","disabled");
	    		$("#faxnum3").attr("disabled","disabled");
	    	} else {
	    		// 기존 등록 수정
	    		// 기존 등록 값 설정
	    		$("input:checkbox[id='isSave']").prop("checked", true);
	    		$("#viewDept").val(viewDept);
	    		$("#viewPosi").val(viewPosi);
	    		$("#viewName").val(viewName);
	    		$("#phoneType1").val(phoneType1);
	    		$("#phonenum1").val(phonenum1);
	    		$("#faxnum1").val(faxnum1);
	    		$("#phoneType2").val(phoneType2);
	    		$("#phonenum2").val(phonenum2);
	    		$("#faxnum2").val(faxnum2);
	    		$("#phoneType3").val(phoneType3);
	    		$("#phonenum3").val(phonenum3);
	    		$("#faxnum3").val(faxnum3);
	    		// 활성화
	    		$("#viewDept").removeAttr("disabled");
	    		$("#viewPosi").removeAttr("disabled");
	    		$("#viewName").removeAttr("disabled");
	    		$("#phoneType1").removeAttr("disabled");
	    		$("#phonenum1").removeAttr("disabled");
	    		$("#faxnum1").removeAttr("disabled");
	    		$("#phoneType2").removeAttr("disabled");
	    		$("#phonenum2").removeAttr("disabled");
	    		$("#faxnum2").removeAttr("disabled");
	    		$("#phoneType3").removeAttr("disabled");
	    		$("#phonenum3").removeAttr("disabled");
	    		$("#faxnum3").removeAttr("disabled");
	    	}
	    	
	    	// open modal
	    	$("#editModal").modal();
	    }
	    
	    // 체크박스 눌렀을때
	    function fillData(){
	    	var phoneSeq = $("#phoneSeq").val();
	    	if ($("input:checkbox[id='isSave']").is(":checked") == false) {
	    		// 비활성화
	    		$("#viewDept").attr("disabled","disabled");
	    		$("#viewPosi").attr("disabled","disabled");
	    		$("#viewName").attr("disabled","disabled");
	    		$("#phoneType1").attr("disabled","disabled");
	    		$("#phonenum1").attr("disabled","disabled");
	    		$("#faxnum1").attr("disabled","disabled");
	    		$("#phoneType2").attr("disabled","disabled");
	    		$("#phonenum2").attr("disabled","disabled");
	    		$("#faxnum2").attr("disabled","disabled");
	    		$("#phoneType3").attr("disabled","disabled");
	    		$("#phonenum3").attr("disabled","disabled");
	    		$("#faxnum3").attr("disabled","disabled");
	    		// 값 비우기
	    		$("#viewDept").val("");
	    		$("#viewPosi").val("");
	    		$("#viewName").val("");
	    		$("#phoneType1").val("");
	    		$("#phonenum1").val("");
	    		$("#faxnum1").val("");
	    		$("#phoneType2").val("");
	    		$("#phonenum2").val("");
	    		$("#faxnum2").val("");
	    		$("#phoneType3").val("");
	    		$("#phonenum3").val("");
	    		$("#faxnum3").val("");
	    	} else {
	    		// 활성화
	    		$("#viewDept").removeAttr("disabled");
	    		$("#viewPosi").removeAttr("disabled");
	    		$("#viewName").removeAttr("disabled");
	    		$("#phoneType1").removeAttr("disabled");
	    		$("#phonenum1").removeAttr("disabled");
	    		$("#faxnum1").removeAttr("disabled");
	    		$("#phoneType2").removeAttr("disabled");
	    		$("#phonenum2").removeAttr("disabled");
	    		$("#faxnum2").removeAttr("disabled");
	    		$("#phoneType3").removeAttr("disabled");
	    		$("#phonenum3").removeAttr("disabled");
	    		$("#faxnum3").removeAttr("disabled");
	    		// 값 채우기
	    		$("#viewDept").val(deptName);
	    		$("#viewPosi").val(posLog);
	    		$("#viewName").val(kname);
	    	}
	    }
		
		// 변경사항 저장
		function goSave(){
			var phoneSeq = $("#phoneSeq").val();
			
			if (phoneSeq == "") {
				// 신규 등록
				if ($("input:checkbox[id='isSave']").is(":checked") == true) {
					var sabun = $("#sabun").val();
					var viewDept = $("#viewDept").val();
		    		var viewPosi = $("#viewPosi").val();
		    		var viewName = $("#viewName").val();
		    		var phoneType1 = $("#phoneType1").val();
		    		var phonenum1 = $("#phonenum1").val();
		    		var faxnum1 = $("#faxnum1").val();
		    		var phoneType2 = $("#phoneType2").val();
		    		var phonenum2 = $("#phonenum2").val();
		    		var faxnum2 = $("#faxnum2").val();
		    		var phoneType3 = $("#phoneType3").val();
		    		var phonenum3 = $("#phonenum3").val();
		    		var faxnum3 = $("#faxnum3").val();
		    		
					var params = {
						auth : "reyon",
						sabun : sabun,
						viewDept : viewDept,
						viewPosi : viewPosi,
						viewName : viewName,
						phoneType1 : phoneType1,
						phonenum1 : phonenum1,
						faxnum1 : faxnum1,
						phoneType2 : phoneType2,
						phonenum2 : phonenum2,
						faxnum2 : faxnum2,
						phoneType3 : phoneType3,
						phonenum3 : phonenum3,
						faxnum3 : faxnum3,
					}

					var request = $.ajax({
						url : "/phone/phonenumberAddAjax.json",
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
							if (json.resultCode == 1) {
								alert("저장 완료 되었습니다.");
								$("#editModal").modal("hide");
								goSearch();
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
				} else {
					alert("관리대상을 체크해주세요.");
				}
			} else {
				// 기존 등록 수정
				if ($("input:checkbox[id='isSave']").is(":checked") == true) {
					var phoneSeq = $("#phoneSeq").val();
					var sabun = $("#sabun").val();
					var viewDept = $("#viewDept").val();
		    		var viewPosi = $("#viewPosi").val();
		    		var viewName = $("#viewName").val();
		    		var phoneType1 = $("#phoneType1").val();
		    		var phonenum1 = $("#phonenum1").val();
		    		var faxnum1 = $("#faxnum1").val();
		    		var phoneType2 = $("#phoneType2").val();
		    		var phonenum2 = $("#phonenum2").val();
		    		var faxnum2 = $("#faxnum2").val();
		    		var phoneType3 = $("#phoneType3").val();
		    		var phonenum3 = $("#phonenum3").val();
		    		var faxnum3 = $("#faxnum3").val();
		    		
					var params = {
						auth : "reyon",
						phoneSeq : phoneSeq,
						sabun : sabun,
						viewDept : viewDept,
						viewPosi : viewPosi,
						viewName : viewName,
						phoneType1 : phoneType1,
						phonenum1 : phonenum1,
						faxnum1 : faxnum1,
						phoneType2 : phoneType2,
						phonenum2 : phonenum2,
						faxnum2 : faxnum2,
						phoneType3 : phoneType3,
						phonenum3 : phonenum3,
						faxnum3 : faxnum3,
					}
	
					var request = $.ajax({
						url : "/phone/phonenumberModifyAjax.json",
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
							if (json.resultCode == 1) {
								alert("수정 완료 되었습니다.");
								$("#editModal").modal("hide");
								goSearch();
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
				} else {
					if (confirm("관리대상을 해제 하시겠습니까?")) {
						var phoneSeq = $("#phoneSeq").val();
			    		
						var params = {
							auth : "reyon",
							phoneSeq : phoneSeq,
						}
		
						var request = $.ajax({
							url : "/phone/phonenumberDeleteAjax.json",
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
								if (json.resultCode == 1) {
									alert("관리대상 해제 완료 되었습니다.");
									$("#editModal").modal("hide");
									goSearch();
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
				}
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
								<i class="fa fa-pencil"></i> 내선번호 편집&nbsp;<a class="btn btn-default btn-xs" href="javascript:;" title="기타번호등록" onClick="javascript:openNewPopup();"><span class="icon_plus_alt2"></span>&nbsp;기타번호등록</a>
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-phone"></i>내선번호 관리</li>
								<li><i class="fa fa-pencil"></i>내선번호 편집</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
					
					<!-- search bar start -->
					<div class="row">
						<div class="col-lg-12">
							<section class="panel">
								<header class="panel-heading">검색</header>
								<div class="panel-body">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-lg-2 control-label">부서명/사번/성명 검색</label>
											<div class="col-lg-8">
												<input type="text" id="searchText" class="form-control" >
											</div>
											<div class="col-lg-2">
												<input type="text" style="display: none;" />
												<a class="btn btn-default" href="javascript:;" title="검색하기" onClick="javascript:goSearch();"><span class="icon_search"></span>&nbsp;검색</a>
											</div>
										</div>
									</form>
								</div>
							</section>
						</div>
					</div>
					<!-- search bar end -->
					
					<!-- grid start -->
					<div class="row">
						<div class="col-lg-12">
							<div id="jsGrid"></div>
						</div>
					</div>
					<!-- grid end -->
					
				</section>
				<!--overview end-->
				
			</section>
			<!--main content end-->
			
		</section>
		<!-- container section start -->
		
		<!-- modal popup start -->
		<div aria-hidden="true" aria-labelledby="descModalLabel" role="dialog" tabindex="-1" id="descModal" class="modal fade">
			<div class="modal-dialog" style="width: 40%;">
				<div class="modal-content">
					<div class="modal-header">
						<button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
						<h4 class="modal-title">내선번호 편집 안내</h4>
					</div>
					<div class="modal-body">
						내선번호 편집은 내선번호 현황에 보여지는 내선번호를 등록/수정/삭제하는 메뉴 입니다.<br><br>
						&nbsp;<span style="color:red;">1. 직원 내선 번호 등록</span><br>
						&nbsp;&nbsp;1) 해당 직원을 클릭<br>
						&nbsp;&nbsp;2) [관리대상] 체크<br>
						&nbsp;&nbsp;3) 노출부서명, 노출직급, 노출성명에 자동으로 들어가는 값 확인 (수정가능)<br>
						&nbsp;&nbsp;4) 본사/진천/충주 中 해당되는 사업장만 [일반내선] 선택 후 일반내선 부분에 내선번호 입력<br>
						&nbsp;&nbsp;5) [저장] 버튼 클릭<br><br>
						&nbsp;<span style="color:red;">2. 직원 외 번호 등록</span><br>
						&nbsp;&nbsp;1) 상단 [기타번호등록] 버튼 클릭<br>
						&nbsp;&nbsp;2) [관리대상] 체크<br>
						&nbsp;&nbsp;3) 노출부서명, 노출직급, 노출성명에 값 입력<br>
						&nbsp;&nbsp;4) 본사/진천/충주 中 해당되는 사업장만 [기타전화번호/기타팩스] 선택 후 해당부분에 번호 입력<br>
						&nbsp;&nbsp;5) [저장] 버튼 클릭<br><br>
						&nbsp;<span style="color:red;">3. 번호 수정</span><br>
						&nbsp;&nbsp;1) 수정할 내선번호를 클릭<br>
						&nbsp;&nbsp;2) 노출부서명, 노출직급, 노출성명, 내선번호에 수정할 값 입력<br>
						&nbsp;&nbsp;3) [저장] 버튼 클릭<br><br>
						&nbsp;<span style="color:red;">4. 번호 삭제</span><br>
						&nbsp;&nbsp;1) 삭제할 내선번호를 클릭<br>
						&nbsp;&nbsp;2) [관리대상] 체크해제<br>
						&nbsp;&nbsp;3) [저장] 버튼 클릭<br>
						&nbsp;&nbsp;&nbsp;<span style="color:red;">※ 퇴사자는 3개월동안만 노출됩니다.</span>
					</div>
					<div class="modal-footer">
						<button data-dismiss="modal" class="btn btn-success" type="button">확인</button>
					</div>
				</div>
			</div>
		</div>
		
		<div aria-hidden="true" aria-labelledby="editModalLabel" role="dialog" tabindex="-1" id="editModal" class="modal fade">
			<div class="modal-dialog" style="width: 60%;">
				<div class="modal-content">
					<div class="modal-header">
						<button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
						<h4 class="modal-title">내선번호 상세정보</h4>
					</div>
					<div class="modal-body">
						<table class="table table-hover personal-task">
							<colgroup>
								<col style="width: 15%;">
								<col style="width: 15%;">
								<col style="width: 20%;">
								<col style="width: 50%;">
							</colgroup>
							<thead>
								<tr>
									<th colspan="2" class="text-center">기본정보</th>
									<th class="text-center">항목</th>
									<th class="text-center">값</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td class="text-center">사번</th>
									<td class="text-center" id="username"></td>
									<td class="text-center">관리대상</th>
									<td class="text-center"><input type="checkbox" id="isSave" onClick="javascript:fillData();"> <label for="isSave">내선번호에 관리 하는 경우 체크</label></td>
								</tr>
								<tr>
									<td class="text-center">부서명</th>
									<td class="text-center" id="deptName"></td>
									<td class="text-center">노출부서명</th>
									<td class="text-center"><input type="text" id="viewDept" class="form-control" placeholder="내선번호에 노출되는 부서명"></td>
								</tr>
								<tr>
									<td class="text-center">직급</th>
									<td class="text-center" id="posLog"></td>
									<td class="text-center">노출직급</th>
									<td class="text-center"><input type="text" id="viewPosi" class="form-control" placeholder="내선번호에 노출되는 직급"></td>
								</tr>
								<tr>
									<td class="text-center">성명</th>
									<td class="text-center" id="kname"></td>
									<td class="text-center">노출성명</th>
									<td class="text-center"><input type="text" id="viewName" class="form-control" placeholder="내선번호에 노출되는 성명"></td>
								</tr>
								<tr>
									<td class="text-center"></th>
									<td class="text-center"></td>
									<td class="text-center">본사</th>
									<td class="text-center form-inline">
										<select id="phoneType1" class="form-control" style="width: 32%;">
											<option value="">== 구분 ==</option>
											<option value="1">일반내선</option>
											<option value="2">기타전화번호</option>
											<option value="3">기타팩스</option>
										</select>
										<input type="text" id="phonenum1" class="form-control" placeholder="일반내선/기타전화번호" style="width: 33%;">
										<input type="text" id="faxnum1" class="form-control" placeholder="기타팩스" style="width: 33%;">
									</td>
								</tr>
								<tr>
									<td class="text-center"></th>
									<td class="text-center"></td>
									<td class="text-center">진천</th>
									<td class="text-center form-inline">
										<select id="phoneType2" class="form-control" style="width: 32%;">
											<option value="">== 구분 ==</option>
											<option value="1">일반내선</option>
											<option value="2">기타전화번호</option>
											<option value="3">기타팩스</option>
										</select>
										<input type="text" id="phonenum2" class="form-control" placeholder="일반내선/기타전화번호" style="width: 33%;">
										<input type="text" id="faxnum2" class="form-control" placeholder="기타팩스" style="width: 33%;">
									</td>
								</tr>
								<tr>
									<td class="text-center"></th>
									<td class="text-center"></td>
									<td class="text-center">충주</th>
									<td class="text-center form-inline">
										<select id="phoneType3" class="form-control" style="width: 32%;">
											<option value="">== 구분 ==</option>
											<option value="1">일반내선</option>
											<option value="2">기타전화번호</option>
											<option value="3">기타팩스</option>
										</select>
										<input type="text" id="phonenum3" class="form-control" placeholder="일반내선/기타전화번호" style="width: 33%;">
										<input type="text" id="faxnum3" class="form-control" placeholder="기타팩스" style="width: 33%;">
									</td>
								</tr>
								<tr>
									<td colspan="4">
										<input type="hidden" id="phoneSeq">
										<input type="hidden" id="sabun">
										<a class="btn btn-default" href="javascript:;" title="저장하기" style="margin-top: 7px;" onClick="javascript:goSave();"><span class="icon_check"></span>&nbsp;저장</a>
									</td>
								</tr>
							</tbody>
						</table>
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