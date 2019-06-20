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
<script type="text/javascript" src="/poll/js/common.js"></script>
<script type="text/javascript">

	$(document).ready(function() {
		
	});
	
	// 투표하기
	function goPoll() {
		var voteChoice = $('input[name="voteChoice"]:checked').val();
		
		if (voteChoice === undefined) {
			alert("CI를 선택해 주세요.");
			return;
		}
		
		if (voteChoice == "") {
			alert("CI를 선택해 주세요.");
			return;
		}
		
		var confirmStr = "";
		if(voteChoice == "1") {
			confirmStr = "Concept A를 선택하셨습니다.\n";
		} else {
			confirmStr = "Concept B를 선택하셨습니다.\n";
		}
		
		if (confirm(confirmStr + "투표하시겠습니까?")) {
			var params = {
				auth : "reyon",
				voteChoice : voteChoice,
			}
			
			var request = $.ajax({
				url: "/poll/pollAddAjax.json"
				, type : "POST"
				, timeout: 10000
				, data : params
				, dataType : "json"
				, async : false
				, error: function(xhr, textStatus, errorThrown) {
					alert("시스템 오류가 발생했습니다.");
				}
				, success : function(json) {
					if (json.resultCode == 1){
						alert("투표가 완료 되었습니다.");
						location.href = "/poll/logout.do";
					}else if (json.resultCode == 1201) {
						alert(json.resultMsg);
						location.href="/poll/login.do";
					}else{
						alert(json.resultMsg);
					}
				}
			});
		}
	}
	
	// 모달 팝업
	function modalPopup(imgSrc) {
		$("#modalBody").empty();
		$("#modalBody").html('<img src="/poll/images/'+imgSrc+'.JPG" style="width: 100%;">');
		$("#mainModal").modal();
	}
	
</script>
</head>

<body>

	<nav class="site-header sticky-top py-1">
		<div class="container d-flex flex-column flex-md-row justify-content-between">
			<a class="py-2" href="/poll/main.do">REYON POLL SYSTEM</a>
			<a class="py-2" href="/poll/logout.do">Logout</a>
		</div>
	</nav>

	<div class="position-relative overflow-hidden p-3 p-md-5 m-md-3 text-center bg-light">
		<div class="mx-auto my-5">
			<h1 class="display-4 font-weight-normal">이연제약 CI 투표</h1>
			<p class="lead font-weight-normal">${principalKname} 님 안녕하세요!<br>아래 두 개의 CI 중 하나를 선택하고,<br>하단의 투표하기 버튼을 클릭해주세요.</p>
		</div>
	</div>

	<div class="d-md-flex flex-md-equal w-100 my-md-3 pl-md-3">
		<div class="bg-light mr-md-3 pt-3 px-3 pt-md-5 px-md-5 text-center overflow-hidden">
			<div class="my-3 py-3">
				<h2 class="display-5">Concept A CI</h2>
			</div>
			<div><img src="/poll/images/A0101.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0101');"></div>
			<div><img src="/poll/images/A0102.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0102');"></div>
			<div><img src="/poll/images/A0103.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0103');"></div>
			<div><img src="/poll/images/A0104.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0104');"></div>
			<div><img src="/poll/images/A0105.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0105');"></div>
			<div><img src="/poll/images/A0106.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0106');"></div>
			<div><img src="/poll/images/A0107.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0107');"></div>
			<div><img src="/poll/images/A0108.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0108');"></div>
			<div><img src="/poll/images/A0109.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0109');"></div>
			<div><img src="/poll/images/A0110.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0110');"></div>
			<div><img src="/poll/images/A0111.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0111');"></div>
			<div><img src="/poll/images/A0112.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0112');"></div>
			<div><img src="/poll/images/A0113.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0113');"></div>
			<div><img src="/poll/images/A0114.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0114');"></div>
			<div><img src="/poll/images/A0115.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0115');"></div>
			<div><img src="/poll/images/A0116.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0116');"></div>
			<div><img src="/poll/images/A0117.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0117');"></div>
			<div><img src="/poll/images/A0118.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0118');"></div>
			<div><img src="/poll/images/A0119.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0119');"></div>
			<div><img src="/poll/images/A0120.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0120');"></div>
			<div><img src="/poll/images/A0121.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0121');"></div>
			<div><img src="/poll/images/A0122.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0122');"></div>
			<div><img src="/poll/images/A0123.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0123');"></div>
			<div><img src="/poll/images/A0124.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0124');"></div>
			<div><img src="/poll/images/A0125.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0125');"></div>
			<div><img src="/poll/images/A0126.JPG" style="width: 100%;" onClick="javascript:modalPopup('A0126');"></div>
		</div>
		<div class="bg-light mr-md-3 pt-3 px-3 pt-md-5 px-md-5 text-center overflow-hidden"> 
			<div class="my-3 p-3">
				<h2 class="display-5">Concept B CI</h2>
			</div>
			<div><img src="/poll/images/B0201.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0201');"></div>
			<div><img src="/poll/images/B0202.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0202');"></div>
			<div><img src="/poll/images/B0203.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0203');"></div>
			<div><img src="/poll/images/B0204.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0204');"></div>
			<div><img src="/poll/images/B0205.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0205');"></div>
			<div><img src="/poll/images/B0206.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0206');"></div>
			<div><img src="/poll/images/B0207.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0207');"></div>
			<div><img src="/poll/images/B0208.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0208');"></div>
			<div><img src="/poll/images/B0209.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0209');"></div>
			<div><img src="/poll/images/B0210.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0210');"></div>
			<div><img src="/poll/images/B0211.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0211');"></div>
			<div><img src="/poll/images/B0212.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0212');"></div>
			<div><img src="/poll/images/B0213.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0213');"></div>
			<div><img src="/poll/images/B0214.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0214');"></div>
			<div><img src="/poll/images/B0215.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0215');"></div>
			<div><img src="/poll/images/B0216.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0216');"></div>
			<div><img src="/poll/images/B0217.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0217');"></div>
			<div><img src="/poll/images/B0218.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0218');"></div>
			<div><img src="/poll/images/B0219.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0219');"></div>
			<div><img src="/poll/images/B0220.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0220');"></div>
			<div><img src="/poll/images/B0221.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0221');"></div>
			<div><img src="/poll/images/B0222.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0222');"></div>
			<div><img src="/poll/images/B0223.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0223');"></div>
			<div><img src="/poll/images/B0224.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0224');"></div>
			<div><img src="/poll/images/B0225.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0225');"></div>
			<div><img src="/poll/images/B0226.JPG" style="width: 100%;" onClick="javascript:modalPopup('B0226');"></div>
		</div>
	</div>
	
	<div class="d-md-flex flex-md-equal w-100 my-md-3 pl-md-3">
	
	  <div class="bg-dark mr-md-3 pt-3 px-3 px-md-5 text-center text-white overflow-hidden">
	    <div class="my-3 py-3">
	      <h2 class="display-5"><input type="radio" id="poll1" name="voteChoice" value="1" style="font-size:17px; width:23px; height:23px;"><label for="poll1">&nbsp;&nbsp;Concept A CI</label></h2>
	    </div>
	  </div>
	  
	  <div class="bg-dark mr-md-3 pt-3 px-3 px-md-5 text-center text-white overflow-hidden">
	    <div class="my-3 p-3">
	      <h2 class="display-5"><input type="radio" id="poll2" name="voteChoice" value="2" style="font-size:17px; width:23px; height:23px;"><label for="poll2">&nbsp;&nbsp;Concept B CI</label></h2>
	    </div>
	  </div>
	  
	</div>
	
	<div class="d-md-flex flex-md-equal w-100 my-md-3 pl-md-3">
		<div class="mr-md-3 px-3 px-md-5 text-center overflow-hidden">
			<div class="my-3 py-3">
				<a class="btn btn-outline-secondary" href="javascript:goPoll();">투표하기</a>
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
	
	<!-- modal popup start -->
	<div aria-hidden="true" aria-labelledby="mainModalLabel" role="dialog" tabindex="-1" id="mainModal" class="modal fade">
		<div class="modal-dialog" style="max-width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
				</div>
				<div class="modal-body" id="modalBody">
				</div>
			</div>
		</div>
	</div>
	<!-- modal popup end -->

</body>

</html>