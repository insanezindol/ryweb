<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<sec:authentication property="principal.username" var="principalUsername" />
<sec:authentication property="principal.kname" var="principalKname" />
<sec:authentication property="principal.posLog" var="principalPosLog" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<title>Reyon Poll System</title>
<link href="/poll/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="/poll/css/simple-line-icons.css" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Lato:300,400,700,300italic,400italic,700italic" rel="stylesheet" type="text/css">
<link href="/poll/css/landing-page.min.css" rel="stylesheet" type="text/css">

<style>
.bd-placeholder-img {
	font-size: 1.125rem;
	text-anchor: middle;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
}
@media ( min-width : 768px) {
	.bd-placeholder-img-lg {
		font-size: 3.5rem;
	}
}
.container {
	max-width: 960px;
}

/*
 * Custom translucent site header
 */
.site-header {
	background-color: rgba(0, 0, 0, .85);
	-webkit-backdrop-filter: saturate(180%) blur(20px);
	backdrop-filter: saturate(180%) blur(20px);
}

.site-header a {
	color: #999;
	transition: ease-in-out color .15s;
}

.site-header a:hover {
	color: #fff;
	text-decoration: none;
}

/*
 * Dummy devices (replace them with your own or something else entirely!)
 */
.product-device {
	position: absolute;
	right: 10%;
	bottom: -30%;
	width: 300px;
	height: 540px;
	background-color: #333;
	border-radius: 21px;
	-webkit-transform: rotate(30deg);
	transform: rotate(30deg);
}

.product-device::before {
	position: absolute;
	top: 10%;
	right: 10px;
	bottom: 10%;
	left: 10px;
	content: "";
	background-color: rgba(255, 255, 255, .1);
	border-radius: 5px;
}

.product-device-2 {
	top: -25%;
	right: auto;
	bottom: 0;
	left: 5%;
	background-color: #e5e5e5;
}

/*
 * Extra utilities
 */
.flex-equal>* {
	-ms-flex: 1;
	flex: 1;
}

@media ( min-width : 768px) {
	.flex-md-equal>* {
		-ms-flex: 1;
		flex: 1;
	}
}

.overflow-hidden {
	overflow: hidden;
}
</style>

<script type="text/javascript" src="/poll/js/jquery.min.js"></script>
<script type="text/javascript" src="/poll/js/popper.min.js"></script>
<script type="text/javascript" src="/poll/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/poll/js/bootstrap.bundle.js"></script>
<script type="text/javascript" src="/poll/js/raphael.min.js"></script>
<script type="text/javascript" src="/poll/js/morris.js"></script>
<script type="text/javascript" src="/poll/js/common.js"></script>
<script type="text/javascript">

	$(document).ready(function() {
		getPoll();
	});
	
	// 투표 정보 가져오기
	function getPoll() {
		var params = {
			auth : "reyon",
		}
		
		var request = $.ajax({
			url: "/poll/pollGetAjax.json"
			, type : "POST"
			, timeout: 10000
			, data : params
			, dataType : "json"
			, error: function(xhr, textStatus, errorThrown) {
				alert("시스템 오류가 발생했습니다.");
			}
			, success : function(json) {
				if (json.resultCode == 0){
					var statList = json.statList;
					var yesList = json.yesList;
					var noList = json.noList;
					
					$("#statListTbl").empty();
					$("#yesListTbl").empty();
					$("#noListTbl").empty();
					
					var statTag = '';
					var yesTag = '';
					var noTag = '';
					
					var dataArr;
					for (var i=0; i<statList.length; i++) {
						statTag += '<tr>';
						statTag += '<td class="text-center">'+statList[i].voteChoice+'</td>';
						statTag += '<td class="text-center">'+statList[i].cnt+'</td>';
						statTag += '</tr>';
					}
					
					if(statList.length == 3) {
						Morris.Bar({
				            element: "bar_chart",
				            data: [
				            	{
					                x: '1번 CI',
					                y: statList[0].cnt
					            }, {
				                    x: '2번 CI',
				                    y: statList[1].cnt
				                }],
				            xkey: 'x',
				            ykeys: 'y',
				            labels: 'Y',
				            barColors: ['rgb(233, 30, 99)'],
				        });
					}
					
					for (var i=0; i<yesList.length; i++) {
						yesTag += '<tr>';
						yesTag += '<td class="text-center">'+(i+1)+'</td>';
						yesTag += '<td class="text-center">'+yesList[i].deptName+'</td>';
						yesTag += '<td class="text-center">'+yesList[i].kname+' '+yesList[i].posLog+'</td>';
						yesTag += '<td class="text-center">'+yesList[i].voteDate+'</td>';
						yesTag += '<td class="text-center">'+yesList[i].voteChoice+'</td>';
						yesTag += '</tr>';
					}
					
					for (var i=0; i<noList.length; i++) {
						noTag += '<tr>';
						noTag += '<td class="text-center">'+(i+1)+'</td>';
						noTag += '<td class="text-center">'+noList[i].deptName+'</td>';
						noTag += '<td class="text-center">'+noList[i].kname+' '+noList[i].posLog+'</td>';
						noTag += '</tr>';
					}
					
					$("#statListTbl").append(statTag);
					$("#yesListTbl").append(yesTag);
					$("#noListTbl").append(noTag);
				}else if (json.resultCode == 1201) {
					alert(json.resultMsg);
					location.href="/poll/login.do";
				}else{
					alert(json.resultMsg);
				}
			}
		});
	}
	
</script>
</head>

<body>

	<nav class="site-header sticky-top py-1">
		<div class="container d-flex flex-column flex-md-row justify-content-between">
			<a class="py-2" href="/poll/admin.do">REYON POLL SYSTEM</a>
			<a class="py-2" href="/poll/logout.do">Logout</a>
		</div>
	</nav>

	<div class="position-relative overflow-hidden p-3 p-md-5 m-md-3 text-center bg-light">
		<div class="col-md-5 p-lg-5 mx-auto my-5">
			<h1 class="display-4 font-weight-normal">이연제약 CI 투표</h1>
			<p class="lead font-weight-normal">관리자 메뉴</p>
		</div>
	</div>
	
	<div class="d-md-flex flex-md-equal w-100 my-md-3 pl-md-3">
		<div class="bg-light mr-md-3 pt-3 px-3 pt-md-5 px-md-5 text-center overflow-hidden">
			<div>
				<p class="lead">투표 결과 통계</p>
			</div>
			<div>
				<table class="table table-bordered">
					<colgroup>
						<col style="width: 50%;">
						<col style="width: 50%;">
					</colgroup>
					<thead>
						<tr>
							<th class="text-center" style="vertical-align: middle;">선택CI번호</th>
							<th class="text-center" style="vertical-align: middle;">투표인원수</th>
						</tr>
					</thead>
					<tbody id="statListTbl">
					</tbody>
				</table>
			</div>
		</div>
		<div class="bg-light mr-md-3 pt-3 px-3 pt-md-5 px-md-5 text-center overflow-hidden">
			<div>
				 <div id="bar_chart" class="graph"></div>
			</div>
		</div>
	</div>

	<div class="d-md-flex flex-md-equal w-100 my-md-3 pl-md-3">
		<div class="bg-light mr-md-3 pt-3 px-3 pt-md-5 px-md-5 text-center overflow-hidden">
			<div>
				<p class="lead">투표 완료 직원</p>
			</div>
			<div>
				<table class="table table-bordered">
					<colgroup>
						<col style="width: 10%;">
						<col style="width: 20%;">
						<col style="width: 20%;">
						<col style="width: 20%;">
						<col style="width: 10%;">
					</colgroup>
					<thead>
						<tr>
							<th class="text-center" style="vertical-align: middle;">순번</th>
							<th class="text-center" style="vertical-align: middle;">소속</th>
							<th class="text-center" style="vertical-align: middle;">이름</th>
							<th class="text-center" style="vertical-align: middle;">시간</th>
							<th class="text-center" style="vertical-align: middle;">결과</th>
						</tr>
					</thead>
					<tbody id="yesListTbl">
					</tbody>
				</table>
			</div>
		</div>
		<div class="bg-light mr-md-3 pt-3 px-3 pt-md-5 px-md-5 text-center overflow-hidden"> 
			<div>
				<p class="lead">투표 미완료 직원</p>
			</div>
			<div>
				<table class="table table-bordered">
					<colgroup>
						<col style="width: 20%;">
						<col style="width: 40%;">
						<col style="width: 40%;">
					</colgroup>
					<thead>
						<tr>
							<th class="text-center" style="vertical-align: middle;">순번</th>
							<th class="text-center" style="vertical-align: middle;">소속</th>
							<th class="text-center" style="vertical-align: middle;">이름</th>
						</tr>
					</thead>
					<tbody id="noListTbl">
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<footer class="container py-5">
		<div class="row">
			<div class="col-12 col-md">
				<small class="d-block mb-3 text-muted">&copy; 2019 REYON PHARMACEUTICAL CO LTD</small>
			</div>
		</div>
	</footer>

</body>

</html>