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
<title>이연제약 관리 시스템</title>
<link href="/css/bootstrap.min.css" rel="stylesheet">
<link href="/css/bootstrap-theme.css?ver=20190619" rel="stylesheet">
<link href="/css/elegant-icons-style.css" rel="stylesheet" />
<link href="/css/font-awesome.min.css" rel="stylesheet" />
<!-- Custom styles -->
<link href="/css/style.css?ver=20190619" rel="stylesheet">
<link href="/css/style-responsive.css" rel="stylesheet" />
<link href="/css/jquery-ui-1.10.4.min.css" rel="stylesheet">
<link href="/css/slick.css" rel="stylesheet">
<link href="/css/slick-theme.css" rel="stylesheet">
<!-- javascripts -->
<script type="text/javascript" src="/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="/js/jquery-ui-1.10.4.min.js"></script>
<script type="text/javascript" src="/js/jquery-ui-1.9.2.custom.min.js"></script>
<!-- moment script -->
<script type="text/javascript" src="/js/moment.js"></script>
<script type="text/javascript" src="/js/moment-ko.js"></script>
<!-- bootstrap -->
<script type="text/javascript" src="/js/bootstrap.min.js"></script>
<!-- custom script for this page -->
<script type="text/javascript" src="/js/scripts.js?ver=20190619"></script>
<script type="text/javascript" src="/js/jquery.autosize.min.js"></script>
<script type="text/javascript" src="/js/jquery.placeholder.min.js"></script>
<script type="text/javascript" src="/js/jquery.slimscroll.min.js"></script>
<script type="text/javascript" src="/js/slick.js"></script>
<!-- common function -->
<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
<!-- 임시 css  -->
<style>
	@import url('https://fonts.googleapis.com/css?family=Do+Hyeon');
	
	html, body { 
		width: 100%; 
		height: 100%;
		background: white;
		white-space: nowrap;
		font-family: 'Do Hyeon', sans-serif;
	}

	.marquee {
	  width:100%;
	  overflow:hidden;
	  margin-bottom:10px;
	  position:relative;
	}
	
	.marquee p:after {
	  content:"";
	  white-space:nowrap;
	  padding-right:50px;
	}
	
	.marquee p {
	  margin:0;
	  padding-left:1700px;
	  display:inline-block;
	  white-space:nowrap;
	    -webkit-animation-name:marquee;
	    -webkit-animation-timing-function:linear;
	    -webkit-animation-duration:40s;
	    -webkit-animation-iteration-count:infinite;
	    -moz-animation-name:marquee;
	    -moz-animation-timing-function:linear;
	    -moz-animation-duration:40s;
	    -moz-animation-iteration-count:infinite;
	    -ms-animation-name:marquee;
	    -ms-animation-timing-function:linear;
	    -ms-animation-duration:40s;
	    -ms-animation-iteration-count:infinite;
	    -o-animation-name:marquee;
	    -o-animation-timing-function:linear;
	    -o-animation-duration:40s;
	    -o-animation-iteration-count:infinite;
	    animation-name:marquee;
	    animation-timing-function:linear;
	    animation-duration:40s;
	    animation-iteration-count:infinite;
	}
	@-webkit-keyframes marquee {
	  from   { -webkit-transform: translate(0%);}
	  99%,to { -webkit-transform: translate(-100%);}
	}
	@-moz-keyframes marquee {
	  from   { -moz-transform: translate(0%);}
	  99%,to { -moz-transform: translate(-100%);}
	}
	@-ms-keyframes marquee {
	  from   { -ms-transform: translate(0%);}
	  99%,to { -ms-transform: translate(-100%);}
	}
	@-o-keyframes marquee {
	  from   { -o-transform: translate(0%);}
	  99%,to { -o-transform: translate(-100%);}
	}
	@keyframes marquee {
	  from   { transform: translate(0%);}
	  99%,to { transform: translate(-100%);}
	}
	
	.marquee1 {
		position: relative;
		box-sizing: border-box;
		animation: marquee1 40s linear infinite;
		width:100%;
		overflow:hidden;
	}
	
	/* Make it move! */
	@keyframes marquee1 {
	    /* 0%   { top: 1100px }
	    100% { top: -1100px } */
	    from {
	    	transform: translateY(100%);
	    }
	    to {
		    transform: translateY(-100%);
	    }
	}

</style>
<script>
	var newsArr = null;
	var sw = 0;

	$(function() {
		// 최초 한번 호출
		drawNotice();
		drawAlarm();
		
		// 300초마다 공지사항 갱신
		setInterval(function() {
			drawNotice();
			drawAlarm();
		}, 300000);
		
		// type effect
		var elements = document.getElementsByClassName('typewrite');
        for (var i=0; i<elements.length; i++) {
            var toRotate = elements[i].getAttribute('data-type');
            var period = elements[i].getAttribute('data-period');
            if (toRotate) {
              new TxtType(elements[i], JSON.parse(toRotate), period);
            }
        }
        // INJECT CSS
        var css = document.createElement("style");
        css.type = "text/css";
        css.innerHTML = ".typewrite > .wrap { border-right: 0.08em solid #FF0000}";
        document.body.appendChild(css);
        
        // slick effect
        $('.single-item').slick({
        	autoplay: true,
        	autoplaySpeed: 30000,
        	dots: false,
        	infinite: true,
        	speed: 500,
        	fade: true,
        	cssEase: 'linear',
        	prevArrow: false,
            nextArrow: false,
        });
        
     // slick effect(imageSlider)
        $('.SlideImage').slick({
        	autoplay: true,
        	autoplaySpeed: 10000,
        	dots: false,
        	infinite: true,
        	speed: 1000,
        	fade: true,
        	cssEase: 'linear',
        	prevArrow: false,
            nextArrow: false,
        });
     	$('.slick-slide, .slick-current, .slick-active').css("text-align", "center");
        
		//Google News API
			var request = $.ajax({
				url: 'https://newsapi.org/v2/top-headlines?country=kr&apiKey=95b92bc4bddb4e3a88037671ae7d190a'
				, type: 'GET'
				, dataType : 'json'
				, error: function(xhr, textStatus, errorThrown) {
					console.log("시스템 오류가 발생했습니다.");
				}
				, success : function(json) {
					newsArr = json.articles;
				}
				
			})
	});
	
	
	function drawNotice() {
		var params = {
			auth : "reyon",
		}
		
		var request = $.ajax({
			url: '/production/noticeGetAjax.json'
			, type : 'POST'
			, timeout: 30000
			, data : params
			, dataType : 'json'
			, error: function(xhr, textStatus, errorThrown) {
				console.log("시스템 오류가 발생했습니다.");
			}
			, success : function(json) {
				if (json.resultCode == 0) {
					var list = json.list;
					var newsList = null;
					var htmlStr = '';
					if ( list == "" ) {
						/* 공지사항 비어있을 때 출력할 곳 */
						/* $("#noticeDataDiv").empty();
						htmlStr += '<div class="marquee"><p>금일 공지가 없습니다.</p></div>';
						for(var i = 0 ; i<5 ; i++ ) {
							if( newsArr != null )
								htmlStr += '<div class="marquee"><p>'+newsArr[i].title+'</p></div>';
						} */
						if ( sw == 0 ) {
							$('.single-item').slick('slickRemove',0); 
							sw = 1;						
						}
					} else {
						if ( sw = 1 ) {
							$('.single-item').slick('slickAdd', 0);
							sw = 0;
						}
						$("#noticeDataDiv").empty();
						for(var i=0; i<list.length; i++){
							htmlStr += '<div class="marquee"><p>'+list[i].content+'</p></div>';
						}
					}
					$("#noticeDataDiv").append(htmlStr);
				} else {
					console.log(json.resultMsg);
				}
			}
		});
	}
	
	function drawAlarm() {
		var params = {
			auth : "reyon",
		}
		
		var request = $.ajax({
			url: '/production/alarmGetAjax.json'
			, type : 'POST'
			, timeout: 30000
			, data : params
			, dataType : 'json'
			, error: function(xhr, textStatus, errorThrown) {
				console.log("시스템 오류가 발생했습니다.");
			}
			, success : function(json) {
				if (json.resultCode == 0) {
					var delayCount = json.delayCount;
					var stockCount = json.stockCount;
					
					$("#delayCountTd").text(delayCount+" 건");
					$("#stockCountTd").text(stockCount+" 건");
					
					var list = json.list;
					$("#alertTbody").empty();
					var htmlStr = '';
					for(var i=0; i<list.length; i++){
						htmlStr += '<tr style="border-bottom: 1px solid #ccc;">';
						htmlStr += '<td class="text-center">'+list[i].jpmnm+'</td>';
						htmlStr += '<td class="text-center">'+list[i].gyugk+'</td>';
						htmlStr += '<td class="text-center">'+list[i].wolsu.format()+'</td>';
						htmlStr += '<td class="text-center">'+list[i].napdate+'</td>';
						htmlStr += '</tr>';
					}
					$("#alertTbody").append(htmlStr);
				} else {
					console.log(json.resultMsg);
				}
			}
		});
	}
	
	var TxtType = function(el, toRotate, period) {
        this.toRotate = toRotate;
        this.el = el;
        this.loopNum = 0;
        this.period = parseInt(period, 10) || 2000;
        this.txt = '';
        this.tick();
        this.isDeleting = false;
    };

    TxtType.prototype.tick = function() {
        var i = this.loopNum % this.toRotate.length;
        var fullTxt = this.toRotate[i];

        if (this.isDeleting) {
        	this.txt = fullTxt.substring(0, this.txt.length - 1);
        } else {
        	this.txt = fullTxt.substring(0, this.txt.length + 1);
        }

        this.el.innerHTML = '<span class="wrap">'+this.txt+'</span>';

        var that = this;
        var delta = 200 - Math.random() * 100;

        if (this.isDeleting) { delta /= 2; }

        if (!this.isDeleting && this.txt === fullTxt) {
	        delta = this.period;
	        this.isDeleting = true;
        } else if (this.isDeleting && this.txt === '') {
	        this.isDeleting = false;
	        this.loopNum++;
	        delta = 500;
        }

        setTimeout(function() {
        that.tick();
        }, delta);
    };
	
</script>
</head>
	<body>

		<div id="divBody" style="max-height: 1000px; min-height: 1000px;">
			<div class="single-item">
				<div id="noticeDiv">
					<div id="noticeDivHeader" style="height: 120px; color:#242D6E; text-align:center; font-size: 65pt; width:100%;">공지사항</div>
					<div id="noticeDataDiv" style="font-size: 70pt; color: #000; width:100%;"></div>
				</div>
				<div id="alertDiv">
					<div id="alertDivHeader" style="height: 120px; color:#242D6E; text-align:center; font-size: 65pt; position: absolute; z-index: 10; background-color: #fff; width:100%;">알림판</div>
					<div style="position: absolute; z-index: 10; background-color: #fff; width:100%; top:120px;">
						<table style="width:100%; font-size: 45pt; color: #000;">
							<colgroup>
								<col style="width: 30%;">
								<col style="width: 20%;">
								<col style="width: 30%;">
								<col style="width: 20%;">
							</colgroup>
							<tbody>
								<tr>
									<td class="text-right">▶ 성적승인 지연 : </td>
									<td class="text-left" id="delayCountTd" style="padding-left: 50px;"></td>
									<td class="text-right">▶ 재고월수 1개월 : </td>
									<td class="text-left" id="stockCountTd" style="padding-left: 50px;"></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div style="position: absolute; z-index: 10; background-color: #fff; top:207px;">
						<table style="width:100%; font-size: 40pt;">
							<colgroup>
								<col style="width: 40%;">
								<col style="width: 15%;">
								<col style="width: 15%;">
								<col style="width: 40%;">
							</colgroup>
							<tbody>
								<tr style="border-bottom: 1px solid #ccc;">
									<td class="text-center">품명</th>
									<td class="text-center">규격</th>
									<td class="text-center">재고월수</th>
									<td class="text-center">재고번호(입고예정)</th>
								</tr>
							</tbody>
						</table>
					</div>
					<div style="max-height: 720px; min-height: 720px; margin-top: 257px;">
						<table id="stockList" style="width:100%; font-size: 40pt;" class="marquee1">
							<colgroup>
								<col style="width: 40%;">
								<col style="width: 15%;">
								<col style="width: 15%;">
								<col style="width: 40%;">
							</colgroup>
							<tbody id="alertTbody"></tbody>
						</table>
					</div>
				</div>
				<div id="imageDiv">
					<div id="noticeDivHeader" style="height: 120px; color:#242D6E; text-align:center; font-size: 65pt; width:100%;">충주공장</div>
					<div id="slideImage" class="SlideImage">
						<img src="/img/1.png" style="max-width: 80%; height: 870px;" />
						<img src="/img/2.jpg" style="max-width: 80%; height: 870px;" />
						<img src="/img/3.jpg" style="max-width: 80%; height: 870px;" />
					</div>
				</div>
			</div>
		</div>

		<div id="divFooter" style="text-align: center; color:#FF0000; font-size:40pt;">
			<!-- <div id="reyonLogo" style="text-align: right;"><img src="/img/reyonLogo.jpg" style="width: 500px;"></div> -->
			<div class="typewrite" data-period="2000" data-type='["이연제약 주식회사", "BEST till 2023"]'>
				<span class="wrap"></span>
			</div>
		</div>
	
	</body>
</html>