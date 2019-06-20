<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<sec:authentication property="principal.username" var="principalUsername" />
<sec:authentication property="principal.kname" var="principalKname" />
<sec:authentication property="principal.posLog" var="principalPosLog" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>이연제약 투표 시스템</title>
<!-- bootstrap css -->
<link rel="stylesheet" href="css/bootstrap.min.css" >
<link rel="stylesheet" href="css/fontawesome/css/fa-solid.css" >
<link rel="stylesheet" href="css/fontawesome/css/fontawesome.css" >
<style type="text/css">
@import url('https://fonts.googleapis.com/css?family=Nanum+Gothic');
body {
	font-family: 'Nanum Gothic', sans-serif;
}
.vote-bg-custom {
	width: 100wh;
	color: #fff;
	background: linear-gradient(-45deg, #EE7752, #E73C7E, #23A6D5, #23D5AB);
	background-size: 400% 400%;
	-webkit-animation: Gradient 15s ease infinite;
	-moz-animation: Gradient 15s ease infinite;
	animation: Gradient 15s ease infinite;
}

@-webkit-keyframes Gradient {
	0% {
		background-position: 0% 50%
	}
	50% {
		background-position: 100% 50%
	}
	100% {
		background-position: 0% 50%
	}
}

@-moz-keyframes Gradient {
	0% {
		background-position: 0% 50%
	}
	50% {
		background-position: 100% 50%
	}
	100% {
		background-position: 0% 50%
	}
}

@keyframes Gradient {
	0% {
		background-position: 0% 50%
	}
	50% {
		background-position: 100% 50%
	}
	100% {
		background-position: 0% 50%
	}
}

.btn-circle.btn-xl {
    width: 50px;
    height: 50px;
    padding: 10px 16px;
    border-radius: 35px;
    font-size: 20px;
    line-height: 1.33;
}

.btn-circle {
    width: 30px;
    height: 30px;
    padding: 6px 0px;
    border-radius: 15px;
    text-align: center;
    font-size: 12px;
    line-height: 1.42857;
}
</style>
<!-- jquery -->
<script type="text/javascript" src="js/jquery-3.3.1.min.js"></script>
<!-- popper -->
<script type="text/javascript" src="js/popper.js"></script>
<!-- bootstrap script -->
<script type="text/javascript" src="js/bootstrap.js"></script>
<!--[if lt IE 9]>
<script src="js/html5shiv.js"></script>
<script src="js/respond.min.js"></script>
<![endif]-->

<script type="text/javascript">

	var REPORT_LIST; 

	$(document).ready(function() {
		// 투표 목록 가져오기
		getVote();
	});
	
	// 배열 랜덤으로 섞기
	function shuffle(array) {
		var currentIndex = array.length, temporaryValue, randomIndex;
		while (0 !== currentIndex) {
			randomIndex = Math.floor(Math.random() * currentIndex);
			currentIndex -= 1;
			temporaryValue = array[currentIndex];
			array[currentIndex] = array[randomIndex];
			array[randomIndex] = temporaryValue;
		}
		return array;
	}

	// 투표 목록 가져오기
	function getVote() {
		var params = {
			auth : "reyon",
		}

		var request = $.ajax({
			url : "./voteGetAjax.json",
			type : "POST",
			timeout : 10000,
			data : params,
			dataType : "json",
			error : function(xhr, textStatus, errorThrown) {
				alert("시스템 오류가 발생했습니다.");
			},
			success : function(json) {
				if (json.resultCode == 0) {
					REPORT_LIST = shuffle(json.list);
					
					var htmlStr = '';
					for(var i=0; i<REPORT_LIST.length; i++){
						htmlStr += '<div class="row">';
						htmlStr += '<div class="col-sm-12">';
						htmlStr += '<div class="row">';
						htmlStr += '<div class="col-sm-2 text-center">';
						htmlStr += '<h2>'+(i+1)+'번</h2>';
						htmlStr += '</div>';
						htmlStr += '<div class="col-sm-10">';
						htmlStr += '<div class="form-group">';
						htmlStr += '<select class="form-control" id="report'+REPORT_LIST[i].voteSeq+'">';
						htmlStr += '<option value="">== 점수 선택 ==</option>';
						htmlStr += '<option value="1">1점</option>';
						htmlStr += '<option value="2">2점</option>';
						htmlStr += '<option value="3">3점</option>';
						htmlStr += '<option value="4">4점</option>';
						htmlStr += '<option value="5">5점</option>';
						htmlStr += '</select>';
						htmlStr += '</div>';
						htmlStr += '</div>';
						htmlStr += '</div>';
						/* htmlStr += '<h6 class="text-center">'+REPORT_LIST[i].voteRemarks+'</h6>'; */
						htmlStr += '<div><img src="'+REPORT_LIST[i].voteFilepath+REPORT_LIST[i].voteFilename+'" style="width: 100%;"></div>';
						htmlStr += '</div>';
						htmlStr += '</div>';
						htmlStr += '<div class="row">';
						htmlStr += '<div class="col-sm-12">';
						htmlStr += '<div class="vote-bg-custom" style="height: 10px; margin-top: 50px; margin-bottom: 50px;"></div>';
						htmlStr += '</div>';
						htmlStr += '</div>';
					}
					
					htmlStr += '<div class="row">';
					htmlStr += '<div class="col-sm-12 text-center">';
					htmlStr += '<button type="button" class="btn vote-bg-custom" onClick="javascript:addVote();">투표하기</button>';
					htmlStr += '</div>';
					htmlStr += '</div>';
					
					$("#main_container").html(htmlStr);
				} else if (json.resultCode == 1201) {
					alert(json.resultMsg);
					location.href="./login.do";
				} else {
					alert(json.resultMsg);
				}
			}
		});
	}
	
	// 모든 값 비교
	Array.prototype.allValuesSame = function() {
	    for(var i = 1; i < this.length; i++)
	    {
	        if(this[i] !== this[0])
	            return false;
	    }
	    return true;
	}

	// 투표 하기
	function addVote() {
		
		for(var i=0; i<REPORT_LIST.length; i++){
			var voteVal = $("#report"+REPORT_LIST[i].voteSeq).val();
			if(voteVal == ""){
				alert((i+1) + "번 항목의 점수를 선택해주세요.");
				$("#report"+REPORT_LIST[i].voteSeq).focus();
				return;
			}
		}
		
		var voteN01 = $("#report1").val();
		var voteN02 = $("#report2").val();
		var voteN03 = $("#report3").val();
		var voteN04 = $("#report4").val();
		var voteN05 = $("#report5").val();
		var voteN06 = $("#report6").val();
		var voteN07 = $("#report7").val();
		var voteN08 = $("#report8").val();
		var voteN09 = $("#report9").val();
		var voteN10 = $("#report10").val();
		var voteN11 = $("#report11").val();
		var voteN12 = $("#report12").val();
		var voteN13 = $("#report13").val();
		var voteN14 = $("#report14").val();
		var voteN15 = $("#report15").val();
		var voteN16 = $("#report16").val();
		var voteN17 = $("#report17").val();
		var voteN18 = $("#report18").val();
		var voteN19 = $("#report19").val();
		var voteN20 = $("#report20").val();
		var voteN21 = $("#report21").val();
		var voteN22 = $("#report22").val();
		var voteN23 = $("#report23").val();
		var voteN24 = $("#report24").val();
		var voteN25 = $("#report25").val();
		var voteN26 = $("#report26").val();
		var voteN27 = $("#report27").val();
		var voteN28 = $("#report28").val();
		var voteN29 = $("#report29").val();
		var voteN30 = $("#report30").val();
		var voteN31 = $("#report31").val();
		var voteN32 = $("#report32").val();
		var voteN33 = $("#report33").val();
		var voteN34 = $("#report34").val();
		var voteN35 = $("#report35").val();
		var voteN36 = $("#report36").val();
		var voteN37 = $("#report37").val();
		var voteN38 = $("#report38").val();
		var voteN39 = $("#report39").val();
		var voteN40 = $("#report40").val();
		var voteN41 = $("#report41").val();
		var voteN42 = $("#report42").val();
		var voteN43 = $("#report43").val();
		var voteN44 = $("#report44").val();
		var voteN45 = $("#report45").val();
		var voteN46 = $("#report46").val();
		var voteN47 = $("#report47").val();
		var voteN48 = $("#report48").val();
		
		var allValuesArr = [];
		allValuesArr.push(voteN01);
		allValuesArr.push(voteN02);
		allValuesArr.push(voteN03);
		allValuesArr.push(voteN04);
		allValuesArr.push(voteN05);
		allValuesArr.push(voteN06);
		allValuesArr.push(voteN07);
		allValuesArr.push(voteN08);
		allValuesArr.push(voteN09);
		allValuesArr.push(voteN10);
		allValuesArr.push(voteN11);
		allValuesArr.push(voteN12);
		allValuesArr.push(voteN13);
		allValuesArr.push(voteN14);
		allValuesArr.push(voteN15);
		allValuesArr.push(voteN16);
		allValuesArr.push(voteN17);
		allValuesArr.push(voteN18);
		allValuesArr.push(voteN19);
		allValuesArr.push(voteN20);
		allValuesArr.push(voteN21);
		allValuesArr.push(voteN22);
		allValuesArr.push(voteN23);
		allValuesArr.push(voteN24);
		allValuesArr.push(voteN25);
		allValuesArr.push(voteN26);
		allValuesArr.push(voteN27);
		allValuesArr.push(voteN28);
		allValuesArr.push(voteN29);
		allValuesArr.push(voteN30);
		allValuesArr.push(voteN31);
		allValuesArr.push(voteN32);
		allValuesArr.push(voteN33);
		allValuesArr.push(voteN34);
		allValuesArr.push(voteN35);
		allValuesArr.push(voteN36);
		allValuesArr.push(voteN37);
		allValuesArr.push(voteN38);
		allValuesArr.push(voteN39);
		allValuesArr.push(voteN40);
		allValuesArr.push(voteN41);
		allValuesArr.push(voteN42);
		allValuesArr.push(voteN43);
		allValuesArr.push(voteN44);
		allValuesArr.push(voteN45);
		allValuesArr.push(voteN46);
		allValuesArr.push(voteN47);
		allValuesArr.push(voteN48);
		
		if(allValuesArr.allValuesSame()){
			alert("재투표의 의미를 생각해주시고\n한번 더 심사숙고하여 점수를 부여해주세요.");
			return;
		}

		if (confirm("투표하시겠습니까?")) {
			var params = {
				auth : "reyon",
				voteN01 : voteN01,
				voteN02 : voteN02,
				voteN03 : voteN03,
				voteN04 : voteN04,
				voteN05 : voteN05,
				voteN06 : voteN06,
				voteN07 : voteN07,
				voteN08 : voteN08,
				voteN09 : voteN09,
				voteN10 : voteN10,
				voteN11 : voteN11,
				voteN12 : voteN12,
				voteN13 : voteN13,
				voteN14 : voteN14,
				voteN15 : voteN15,
				voteN16 : voteN16,
				voteN17 : voteN17,
				voteN18 : voteN18,
				voteN19 : voteN19,
				voteN20 : voteN20,
				voteN21 : voteN21,
				voteN22 : voteN22,
				voteN23 : voteN23,
				voteN24 : voteN24,
				voteN25 : voteN25,
				voteN26 : voteN26,
				voteN27 : voteN27,
				voteN28 : voteN28,
				voteN29 : voteN29,
				voteN30 : voteN30,
				voteN31 : voteN31,
				voteN32 : voteN32,
				voteN33 : voteN33,
				voteN34 : voteN34,
				voteN35 : voteN35,
				voteN36 : voteN36,
				voteN37 : voteN37,
				voteN38 : voteN38,
				voteN39 : voteN39,
				voteN40 : voteN40,
				voteN41 : voteN41,
				voteN42 : voteN42,
				voteN43 : voteN43,
				voteN44 : voteN44,
				voteN45 : voteN45,
				voteN46 : voteN46,
				voteN47 : voteN47,
				voteN48 : voteN48,
			}

			var request = $.ajax({
				url : './voteAddAjax.json',
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
					if (json.resultCode == 1) {
						alert("투표가 완료 되었습니다.");
						location.href = "./logout.do";
					} else if (json.resultCode == 1201) {
						alert(json.resultMsg);
						location.href="./login.do";
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
	
	function cfOpenMagnificPopup() {
		$.magnificPopup.open({
			items: {
				src: '#loadingPopup',
			},
			type: 'inline',
			removalDelay: 200,
			mainClass: 'my-mfp-slide-bottom',
			showCloseBtn: false,
			closeOnBgClick: false,
			enableEscapeKey: false,
		});
	}

	function cfCloseMagnificPopup() {
		$('#loadingPopup').magnificPopup('close');
	}
	
	function pageScrollTop() {
		$('html, body').animate({ scrollTop: 0 }, 700);
	}
	
	function pageScrollBottom() {
		$('html, body').animate({ scrollTop: $(document).height() }, 700);
	}
	
</script>

</head>
<body>

	<div class="jumbotron text-center vote-bg-custom" style="background-color: #55B359; margin-bottom: 0; color: #fff;">
		<h2>이연제약 CI 투표</h2>
		<p>${principalKname} ${principalPosLog}님 안녕하세요!</p>
		<p>각각의 CI에 점수를 선택하고, 하단의 투표하기 버튼을 눌러주세요.</p>
	</div>

	<div class="container" style="margin-top: 30px; margin-bottom: 30px;" id="main_container">
	</div>

	<div class="jumbotron text-center vote-bg-custom" style="background-color: #55B359; margin-bottom: 0; color: #fff;">
		<p>REYON PHARMACEUTICAL CO LTD. All rights reserved.</p>
		<p>
			ⓒ Copyright 2018
			<a href="./logout.do" style="color:#fff"> [로그아웃]</a>
			<c:if test="${principalUsername == '07071803' || principalUsername == '07031201' || principalUsername == '07031902' || principalUsername == '18021201'}">
				<a href="./admin.do" style="color:#fff"> [관리자]</a>
			</c:if>
		</p>
	</div>
	
	<!-- Primary circle button with ripple effect -->
	<button class="btn btn-circle btn-xl vote-bg-custom" style="position: fixed; bottom: 80px; right: 20px;" type="button" title="상단으로 이동" onClick="javascript:pageScrollTop();"><i class="fas fa-angle-up"></i></button>
	<button class="btn btn-circle btn-xl vote-bg-custom" style="position: fixed; bottom: 20px; right: 20px;" type="button" title="하단으로 이동" onClick="javascript:pageScrollBottom();"><i class="fas fa-angle-down"></i></button>
	
	<!--loading Popup start-->
	<%@ include file="/WEB-INF/views/loading.jsp"%>
	<!--loading Popup end--> 

</body>
</html>