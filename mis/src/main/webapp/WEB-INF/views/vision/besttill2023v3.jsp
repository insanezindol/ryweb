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
			
			var options = {
			    height: "800px",
			    pdfOpenParams: { scrollbar: '1', toolbar: '0', statusbar: '0', messages: '0', navpanes: '0', view: 'FitH' },
			};
			PDFObject.embed("/images/vision/besttill2023.pdf", "#reyonView", options);
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
			<div id="reyonView" style="width: 100%;"></div>
		</div>
	</div>

</body>
</html>