<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>이연제약 내선번호</title>
		<link href="/css/bootstrap.min.css" rel="stylesheet">
		<link href="/css/bootstrap-theme.css?ver=20190619" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Noto+Sans+KR" rel="stylesheet">
		<style type="text/css">
		body {
			font-family: 'Noto Sans KR', sans-serif;
		}
		/* 글자가 긴경우 테이블에 짤리는 문제 때문에 수정함 */
		.table thead > tr > th,
		.table tbody > tr > th,
		.table tfoot > tr > th,
		.table thead > tr > td,
		.table tbody > tr > td,
		.table tfoot > tr > td {
			padding: 8px;
			line-height: 1.428571429;
			vertical-align: top;
			border-top: 1px solid #dddddd;
			white-space: normal;
			text-overflow: unset;
			overflow: unset;
		}
		</style>
		<!-- javascripts -->
		<script type="text/javascript" src="/js/jquery-1.8.3.min.js"></script>
		<!-- bootstrap -->
		<script type="text/javascript" src="/js/bootstrap.min.js"></script>
		<!-- common js -->
		<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
		<!-- page script -->
		<script type="text/javascript">
		$(function() {
			$("#searchText").on("keyup", function() {
				var value = $(this).val().toLowerCase();
				$("#bonsaTable tr").filter(function() {
					$(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
				});
				$("#jincheonTable tr").filter(function() {
					$(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
				});
				$("#chungjuTable tr").filter(function() {
					$(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
				});
			});
		});
		
		// 엑셀 다운로드
		function goExcelDownload(saupCode) {
			cfOpenMagnificPopup();
			setTimeout(cfCloseMagnificPopup, 1500);
			var form = document.createElement("form");
			form.name = "downForm";
			form.id = "downForm";
			form.method = "POST";
			form.action = "/phone/phoneExcelDownload.do";
			var auth = document.createElement("input");
			auth.type = "hidden";
			auth.name = "auth";
			auth.value = "reyon";
			var saup = document.createElement("input");
			saup.type = "hidden";
			saup.name = "saup";
			saup.value = saupCode;
			$(form).append(auth);
			$(form).append(saup);
			$("#container").append(form);
			form.submit();
			$("#downForm").remove();
		}
		</script>
	</head>
	
	<body>

	<div id="container" class="container" style="max-width:100%;">
		
		<div class="text-center"><h2>이연제약 내선번호 현황</h2></div>
		
		<div class="text-right" style="margin-left: 50px; margin-right: 50px;"><p>최종 업데이트 : ${info.regDate }</p></div>
		
		<div class="text-right" style="margin-bottom: 20px; margin-left: 50px; margin-right: 50px;">
			<a class="btn btn-primary" href="javascript:;" title="본사 내선 엑셀 다운로드" onClick="javascript:goExcelDownload('10');"><span class="icon_pencil"></span>&nbsp;본사 내선 엑셀 다운로드</a>
			<a class="btn btn-success" href="javascript:;" title="진천 내선 엑셀 다운로드" onClick="javascript:goExcelDownload('20');"><span class="icon_pencil"></span>&nbsp;진천 내선 엑셀 다운로드</a>
			<a class="btn btn-warning" href="javascript:;" title="충주 내선 엑셀 다운로드" onClick="javascript:goExcelDownload('30');"><span class="icon_pencil"></span>&nbsp;충주 내선 엑셀 다운로드</a>
		</div>
		
		<div style="margin-bottom: 20px; margin-left: 50px; margin-right: 50px;"><input class="form-control" id="searchText" type="text" placeholder="부서, 직급, 성명, 내선번호 검색"></div>
		
		<div class="row">
			<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
				<table class="table table-bordered table-striped">
					<thead>
						<tr>
							<th class="text-center" colspan="4">본사</th>
						</tr>
						<tr>
							<th class="text-center" colspan="4">서울특별시 강남구 영동대로 416 (대치동 코스모타워 8층)</th>
						</tr>
						<tr>
							<th class="text-center" colspan="4">개인별 직통번호 : 02-3407-5(내선번호)</th>
						</tr>
						<tr>
							<th class="text-center">부서명</th>
							<th class="text-center">직급</th>
							<th class="text-center">성명</th>
							<th class="text-center">내선번호</th>
						</tr>
					</thead>
					<tbody id="bonsaTable">
					<c:forEach var="result" items="${listBonsa}" varStatus="status">
						<tr>
							<td class="text-center">${result.viewDept }</td>
					<c:choose>
						<c:when test="${result.phoneType1 == '2' || result.phoneType1 == '3'}">
							<td class="text-center" colspan="3">
							<c:if test="${result.phonenum1 != null }">T : ${result.phonenum1 }</c:if>
							<c:if test="${result.faxnum1 != null }">F : ${result.faxnum1 }</c:if>
							</td>
						</c:when>
						<c:otherwise>
							<td class="text-center">${result.viewPosi }</td>
							<td class="text-center">${result.viewName }</td>
							<td class="text-center">${result.phonenum1 }</td>
						</c:otherwise>
					</c:choose>
							
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
				<table class="table table-bordered table-striped">
					<thead>
						<tr>
							<th class="text-center" colspan="4">진천</th>
						</tr>
						<tr>
							<th class="text-center" colspan="4">충청북도 진천군 덕산면 한삼로 69-10 (덕산농공단지)</th>
						</tr>
						<tr>
							<th class="text-center" colspan="4">개인별 직통번호 : 043-531-3(내선번호)</th>
						</tr>
						<tr>
							<th class="text-center">부서명</th>
							<th class="text-center">직급</th>
							<th class="text-center">성명</th>
							<th class="text-center">내선번호</th>
						</tr>
					</thead>
					<tbody id="jincheonTable">
					<c:forEach var="result" items="${listJincheon}" varStatus="status">
						<tr>
							<td class="text-center">${result.viewDept }</td>
					<c:choose>
						<c:when test="${result.phoneType2 == '2' || result.phoneType2 == '3'}">
							<td class="text-center" colspan="3">
							<c:if test="${result.phonenum2 != null }">T : ${result.phonenum2 }</c:if>
							<c:if test="${result.faxnum2 != null }">F : ${result.faxnum2 }</c:if>
							</td>
						</c:when>
						<c:otherwise>
							<td class="text-center">${result.viewPosi }</td>
							<td class="text-center">${result.viewName }</td>
							<td class="text-center">${result.phonenum2 }</td>
						</c:otherwise>
					</c:choose>
							
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
				<table class="table table-bordered table-striped">
					<thead>
						<tr>
							<th class="text-center" colspan="4">충주</th>
						</tr>
						<tr>
							<th class="text-center" colspan="4">충청북도 충주시 중앙탑면 원앙길 8-13 스타프라자 4층</th>
						</tr>
						<tr>
							<th class="text-center" colspan="4">개인별 직통번호 : 043-840-6(내선번호)</th>
						</tr>
						<tr>
							<th class="text-center">부서명</th>
							<th class="text-center">직급</th>
							<th class="text-center">성명</th>
							<th class="text-center">내선번호</th>
						</tr>
					</thead>
					<tbody id="chungjuTable">
					<c:forEach var="result" items="${listChungju}" varStatus="status">
						<tr>
							<td class="text-center">${result.viewDept }</td>
					<c:choose>
						<c:when test="${result.phoneType3 == '2' || result.phoneType3 == '3'}">
							<td class="text-center" colspan="3">
							<c:if test="${result.phonenum3 != null }">T : ${result.phonenum3 }</c:if>
							<c:if test="${result.faxnum3 != null }">F : ${result.faxnum3 }</c:if>
							</td>
						</c:when>
						<c:otherwise>
							<td class="text-center">${result.viewPosi }</td>
							<td class="text-center">${result.viewName }</td>
							<td class="text-center">${result.phonenum3 }</td>
						</c:otherwise>
					</c:choose>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
	<!--loading Popup start-->
	<%@ include file="/WEB-INF/views/include/loading.jsp"%>
	<!--loading Popup end--> 

</body>
</html>