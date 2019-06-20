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
		<link href="/css/jquery-ui-1.12.1.min.css" rel="stylesheet">
		<style type="text/css">
		.label-container {
			position: fixed;
			bottom: 48px;
			right: 105px;
			display: table;
			visibility: hidden;
		}
		.label-text {
			color: #FFF;
			background: rgba(51, 51, 51, 0.5);
			display: table-cell;
			vertical-align: middle;
			padding: 10px;
			border-radius: 3px;
		}
		.label-arrow {
			display: table-cell;
			vertical-align: middle;
			color: #333;
			opacity: 0.5;
		}
		.float {
			position: fixed;
			width: 60px;
			height: 60px;
			bottom: 40px;
			right: 40px;
			background: linear-gradient(40deg,#ff6ec4,#7873f5)!important;
			color: #FFF;
			border-radius: 50px;
			text-align: center;
			box-shadow: 2px 2px 3px #999;
			z-index: 10;
		}
		.my-float {
			font-size: 24px;
			margin-top: 18px;
		}
		a.float+div.label-container {
			visibility: hidden;
			opacity: 0;
			transition: visibility 0s, opacity 0.5s ease;
			z-index: 10;
		}
		a.float:hover+div.label-container {
			visibility: visible;
			opacity: 1;
		}
		</style>
		<!-- javascripts -->
		<script type="text/javascript" src="/js/jquery-1.8.3.min.js"></script>
		<script type="text/javascript" src="/js/jquery-ui-1.12.1.min.js"></script>
		<script type="text/javascript" src="/js/jquery-ui-1.12.1.custom.min.js"></script>
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
		<!-- common function -->
		<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
		<!-- page script -->
		<script type="text/javascript">
		
		$(function() {
			// 진입 시 메뉴 선택
			cfLNBMenuSelect("menu14");
			
			// 안내 모달 팝업
			$("#descModal").modal();
			
			// 테이블 drag and drop
			$("#bonsaTbl tbody").sortable({ helper: fixHelper, placeholder: "ui-state-highlight" }).disableSelection();
			$("#jincheonTbl tbody").sortable({ helper: fixHelper, placeholder: "ui-state-highlight" }).disableSelection();
			$("#chungjuTbl tbody").sortable({ helper: fixHelper, placeholder: "ui-state-highlight" }).disableSelection();
		});
		
		// jquery sortable width fix helper
		var fixHelper = function(e, ui) {  
			ui.children().each(function() {  
				$(this).width($(this).width());  
			});  
			return ui;  
		};
		
		// 변경사항 저장 모달 팝업
		function goSaveModal(){
			$("input:checkbox[id='saupChk1']").prop("checked", false);
			$("input:checkbox[id='saupChk2']").prop("checked", false);
			$("input:checkbox[id='saupChk3']").prop("checked", false);
			$("#saveModal").modal();
		}
		
		// 변경사항 저장
		function goSave(){
			var arrBonsa = [];
			if($("input:checkbox[id='saupChk1']").is(":checked")){
				$("#bonsaTable tr").each(function() {
					arrBonsa.push(this.id);
				});
			}
			var arrJincheon = [];
			if($("input:checkbox[id='saupChk2']").is(":checked")){
				$("#jincheonTable tr").each(function() {
					arrJincheon.push(this.id);
				});
			}
			var arrChungju = [];
			if($("input:checkbox[id='saupChk3']").is(":checked")){
				$("#chungjuTable tr").each(function() {
					arrChungju.push(this.id);
				});
			}
			
			var params = {
				auth : "reyon",
				arrBonsa : arrBonsa.toString(),
				arrJincheon : arrJincheon.toString(),
				arrChungju : arrChungju.toString(),
			}

			var request = $.ajax({
				url : "/phone/phonenumberOrderModifyAjax.json",
				type : "POST",
				timeout : 60000,
				data : params,
				dataType : "json",
				beforeSend : function(xmlHttpRequest) {
					cfOpenMagnificPopup();
				},
				error : function(xhr, textStatus, errorThrown) {
					alert("시스템 오류가 발생했습니다.");
				},
				success : function(json) {
					if (json.resultCode == 0) {
						alert("저장 완료 되었습니다.");
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
								<i class="fa fa-pencil"></i> 내선번호 순서편집
							</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i>관리 시스템</li>
								<li><i class="fa fa-phone"></i>내선번호 관리</li>
								<li><i class="fa fa-pencil"></i>내선번호 순서편집</li>
							</ol>
						</div>
					</div>
					<!-- title end -->
	
					<!-- data start -->
					<div class="row">
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<table id="bonsaTbl" class="table table-bordered table-striped">
								<thead>
									<tr>
										<th class="text-center" colspan="3">본사</th>
									</tr>
									<tr>
										<th class="text-center">부서명</th>
										<th class="text-center">성명</th>
										<th class="text-center">내선번호</th>
									</tr>
								</thead>
								<tbody id="bonsaTable">
								<c:forEach var="result" items="${listBonsa}" varStatus="status">
									<tr id="${result.phoneSeq }">
										<td class="text-center">${result.viewDept }</td>
								<c:choose>
									<c:when test="${result.phoneType1 == '2' || result.phoneType1 == '3'}">
										<td class="text-center" colspan="2">
										<c:if test="${result.phonenum1 != null }">T : ${result.phonenum1 }</c:if>
										<c:if test="${result.faxnum1 != null }">F : ${result.faxnum1 }</c:if>
										</td>
									</c:when>
									<c:otherwise>
										<td class="text-center">${result.viewName } ${result.viewPosi }</td>
										<td class="text-center">${result.phonenum1 }</td>
									</c:otherwise>
								</c:choose>
									</tr>
								</c:forEach>
								</tbody>
							</table>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<table id="jincheonTbl" class="table table-bordered table-striped">
								<thead>
									<tr>
										<th class="text-center" colspan="3">진천</th>
									</tr>
									<tr>
										<th class="text-center">부서명</th>
										<th class="text-center">성명</th>
										<th class="text-center">내선번호</th>
									</tr>
								</thead>
								<tbody id="jincheonTable">
								<c:forEach var="result" items="${listJincheon}" varStatus="status">
									<tr id="${result.phoneSeq }">
										<td class="text-center">${result.viewDept }</td>
								<c:choose>
									<c:when test="${result.phoneType2 == '2' || result.phoneType2 == '3'}">
										<td class="text-center" colspan="2">
										<c:if test="${result.phonenum2 != null }">T : ${result.phonenum2 }</c:if>
										<c:if test="${result.faxnum2 != null }">F : ${result.faxnum2 }</c:if>
										</td>
									</c:when>
									<c:otherwise>
										<td class="text-center">${result.viewName } ${result.viewPosi }</td>
										<td class="text-center">${result.phonenum2 }</td>
									</c:otherwise>
								</c:choose>
									</tr>
								</c:forEach>
								</tbody>
							</table>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<table id="chungjuTbl" class="table table-bordered table-striped">
								<thead>
									<tr>
										<th class="text-center" colspan="3">충주</th>
									</tr>
									<tr>
										<th class="text-center">부서명</th>
										<th class="text-center">성명</th>
										<th class="text-center">내선번호</th>
									</tr>
								</thead>
								<tbody id="chungjuTable">
								<c:forEach var="result" items="${listChungju}" varStatus="status">
									<tr id="${result.phoneSeq }">
										<td class="text-center">${result.viewDept }</td>
								<c:choose>
									<c:when test="${result.phoneType3 == '2' || result.phoneType3 == '3'}">
										<td class="text-center" colspan="2">
										<c:if test="${result.phonenum3 != null }">T : ${result.phonenum3 }</c:if>
										<c:if test="${result.faxnum3 != null }">F : ${result.faxnum3 }</c:if>
										</td>
									</c:when>
									<c:otherwise>
										<td class="text-center">${result.viewName } ${result.viewPosi }</td>
										<td class="text-center">${result.phonenum3 }</td>
									</c:otherwise>
								</c:choose>
									</tr>
								</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
					<!-- data end -->
					
					<!-- bottom button start -->
					<a href="javascript:;" class="float" onClick="javascript:goSaveModal();">
						<i class="fa fa-floppy-o my-float"></i>
					</a>
					<div class="label-container">
						<div class="label-text">현재 상태 저장</div>
						<i class="fa fa-play label-arrow"></i>
					</div>
					<!-- bottom button end -->
					
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
						<h4 class="modal-title">내선번호 순서편집 안내</h4>
					</div>
					<div class="modal-body">
						내선변호 순서편집은 내선번호 현황에 보여지는 순서를 설정하는 메뉴 입니다.<br><br>
						[내선번호 순서편집 방법]<br>
						1. 해당직원을 드래그앤드롭하여 눌러 순서변경<br>
						2. 우측하단 <span style="color:red;">저장버튼</span>을 클릭<br>
						3. 본사/진천/충주 중 순서를 적용할 사업장 체크<br>
						4. [저장] 클릭<br><br>
						※ 여러명이 동시에 수정하는 경우 <span style="color:red;">비정상적으로 반영 될 수 있기 때문에</span> 유의하시기 바랍니다.  
					</div>
					<div class="modal-footer">
						<button data-dismiss="modal" class="btn btn-success" type="button">확인</button>
					</div>
				</div>
			</div>
		</div>
		
		<div aria-hidden="true" aria-labelledby="saveModalLabel" role="dialog" tabindex="-1" id="saveModal" class="modal fade">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
						<h4 class="modal-title">내선번호 순서편집 저장 사업장 확인</h4>
					</div>
					<div class="modal-body">
						<p>내선번호 순서를 적용 할 사업장을 선택 후 저장 버튼을 클릭해주세요.</p>
						<form class="form-horizontal" role="form">
							<div class="form-group">
								<div class="col-lg-12">
									<label class="checkbox-inline"><input type="checkbox" name="saupChk" id="saupChk1" value="10"> 본사</label>
                      				<label class="checkbox-inline"><input type="checkbox" name="saupChk" id="saupChk2" value="20"> 진천</label>
                      				<label class="checkbox-inline"><input type="checkbox" name="saupChk" id="saupChk3" value="30"> 충주</label>
								</div>
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<button data-dismiss="modal" class="btn btn-success" type="button" onClick="javascript:goSave();">저장</button>
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