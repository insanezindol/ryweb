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
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<meta name="theme-color" content="#2196F3">
	<title>BEST till 2023</title>
	<!-- import total css -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
	<style>
	body {
		-webkit-font-smoothing: subpixel-antialiased;
		-webkit-touch-callout: none;
		-webkit-user-select: none;
		-khtml-user-select: none;
		-ms-user-select: none;
		-moz-user-select: none;
		user-select: none;
	}
	.main-img-body {
		background: url('/images/vision/bg2.jpg') no-repeat center center fixed;
		-webkit-background-size: cover;
		-moz-background-size: cover;
		-o-background-size: cover;
		background-size: cover;
	}
	.carousel-control-prev-icon {
		background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' fill='%23f00' viewBox='0 0 8 8'%3e%3cpath d='M5.25 0l-4 4 4 4 1.5-1.5-2.5-2.5 2.5-2.5-1.5-1.5z'/%3e%3c/svg%3e")
	}
	.carousel-control-next-icon {
		background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' fill='%23f00' viewBox='0 0 8 8'%3e%3cpath d='M2.75 0l-1.5 1.5 2.5 2.5-2.5 2.5 1.5 1.5 4-4-4-4z'/%3e%3c/svg%3e")
	}
	</style>
	<!-- Global site tag (gtag.js) - Google Analytics -->
	<script async src="https://www.googletagmanager.com/gtag/js?id=UA-116158631-2"></script>
	<script>
	  window.dataLayer = window.dataLayer || [];
	  function gtag(){dataLayer.push(arguments);}
	  gtag('js', new Date());
	  gtag('config', 'UA-116158631-2');
	</script>
	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
	<script type="text/javascript" src="/js/pdfobject.js"></script>
	<script type="text/javascript">
		$(function() {
			$(document).bind("contextmenu", function(e) {
				return false;
			});
			
			/* var options = {
			    height: "800px",
			    pdfOpenParams: { scrollbar: '1', toolbar: '0', statusbar: '0', messages: '0', navpanes: '0', view: 'FitH' },
			};
			PDFObject.embed("/images/vision/besttill2023.pdf", "#reyonView", options); */
		});
		
		$(document)[0].oncontextmenu = function() { return false; }
		$(document).mousedown(function(e) {
			if( e.button == 2 ) {
					alert('오른쪽 버튼은 사용할 수 없습니다.');
					return false;
			} else {
					return true;
			}
		});
		
		$(".pdfobject").mousedown(function(e) {
			if( e.button == 2 ) {
					alert('오른쪽 버튼은 사용할 수 없습니다.');
					return false;
			} else {
					return true;
			}
		});
		
	</script>
	</head>

<body class="main-img-body" oncontextmenu="return false" onselectstart="return false" ondragstart="return false">
	<div class="container">
		<div class="row" style="padding-top: 80px;">
			<div id="reyonView" style="width: 100%;">
				<object data="/images/vision/besttill2023.pdf#scrollbar=1&toolbar=0&statusbar=0&messages=0&navpanes=0&view=FitH" type="application/pdf" width="100%" height="800px">
					<p>
						<div id="reyonIndicators" class="carousel slide" data-ride="carousel" data-keyboard="true" data-interval="false">
							<ol class="carousel-indicators">
								<li data-target="#reyonIndicators" data-slide-to="0" class="active"></li>
								<li data-target="#reyonIndicators" data-slide-to="1"></li>
								<li data-target="#reyonIndicators" data-slide-to="2"></li>
								<li data-target="#reyonIndicators" data-slide-to="3"></li>
								<li data-target="#reyonIndicators" data-slide-to="4"></li>
								<li data-target="#reyonIndicators" data-slide-to="5"></li>
								<li data-target="#reyonIndicators" data-slide-to="6"></li>
								<li data-target="#reyonIndicators" data-slide-to="7"></li>
								<li data-target="#reyonIndicators" data-slide-to="8"></li>
								<li data-target="#reyonIndicators" data-slide-to="9"></li>
								<li data-target="#reyonIndicators" data-slide-to="10"></li>
								<li data-target="#reyonIndicators" data-slide-to="11"></li>
								<li data-target="#reyonIndicators" data-slide-to="12"></li>
								<li data-target="#reyonIndicators" data-slide-to="13"></li>
								<li data-target="#reyonIndicators" data-slide-to="14"></li>
							</ol>
							<div class="carousel-inner">
								<div class="carousel-item active">
									<img class="d-block w-100 theloup" id="img01" src="/images/vision/besttill2023-01.jpg">
								</div>
								<div class="carousel-item">
									<img class="d-block w-100 theloup" id="img02" src="/images/vision/besttill2023-02.jpg">
								</div>
								<div class="carousel-item">
									<img class="d-block w-100 theloup" id="img03" src="/images/vision/besttill2023-03.jpg">
								</div>
								<div class="carousel-item">
									<img class="d-block w-100 theloup" id="img04" src="/images/vision/besttill2023-04.jpg">
								</div>
								<div class="carousel-item">
									<img class="d-block w-100 theloup" id="img05" src="/images/vision/besttill2023-05.jpg">
								</div>
								<div class="carousel-item">
									<img class="d-block w-100 theloup" id="img06" src="/images/vision/besttill2023-06.jpg">
								</div>
								<div class="carousel-item">
									<img class="d-block w-100 theloup" id="img07" src="/images/vision/besttill2023-07.jpg">
								</div>
								<div class="carousel-item">
									<img class="d-block w-100 theloup" id="img08" src="/images/vision/besttill2023-08.jpg">
								</div>
								<div class="carousel-item">
									<img class="d-block w-100 theloup" id="img09" src="/images/vision/besttill2023-09.jpg">
								</div>
								<div class="carousel-item">
									<img class="d-block w-100 theloup" id="img10" src="/images/vision/besttill2023-10.jpg">
								</div>
								<div class="carousel-item">
									<img class="d-block w-100 theloup" id="img11" src="/images/vision/besttill2023-11.jpg">
								</div>
								<div class="carousel-item">
									<img class="d-block w-100 theloup" id="img12" src="/images/vision/besttill2023-12.jpg">
								</div>
								<div class="carousel-item">
									<img class="d-block w-100 theloup" id="img13" src="/images/vision/besttill2023-13.jpg">
								</div>
								<div class="carousel-item">
									<img class="d-block w-100 theloup" id="img14" src="/images/vision/besttill2023-14.jpg">
								</div>
								<div class="carousel-item">
									<img class="d-block w-100 theloup" id="img15" src="/images/vision/besttill2023-15.jpg">
								</div>
							</div>
							<a class="carousel-control-prev" href="#reyonIndicators" role="button" data-slide="prev">
								<span class="carousel-control-prev-icon" aria-hidden="true"></span>
								<span class="sr-only">이전</span>
							</a>
							<a class="carousel-control-next" href="#reyonIndicators" role="button" data-slide="next">
								<span class="carousel-control-next-icon" aria-hidden="true"></span>
								<span class="sr-only">다음</span>
							</a>
						</div>
					</p>
				</object>
			</div>
		</div>
	</div>

</body>
</html>