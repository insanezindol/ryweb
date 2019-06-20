<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<sec:authentication property="principal.username" var="principalUsername" />
<sec:authentication property="principal.saupName" var="principalSaupName" />
<sec:authentication property="principal.userRole" var="principalUserRole" />
<sec:authentication property="principal.useYn" var="principalUseYn" />
<sec:authentication property="principal.regDate" var="principalRegDate" />
<link href="/css/timetablejs.css" rel="stylesheet">
<link href="/css/bootstrap-datetimepicker.min.css" rel="stylesheet">
<link href="/css/weather-icons.css" rel="stylesheet">
<script type="text/javascript" src="/js/timetable.js"></script>
<script type="text/javascript" src="/js/bootstrap-datetimepicker.min.js"></script>

<script type="text/javascript">

	$(function() {
		
	});

</script>

<header class="header header-bg">
	<div class="toggle-nav">
		<div class="icon-reorder tooltips" data-original-title="네비게이션" data-placement="bottom">
			<i class="fa fa-bars"></i>
		</div>
	</div>

	<!--logo start-->
	<a href="javascript:;" onClick="javascript:cfGoMain();" class="logo">이연제약 도매관리 시스템</a>
	<!--logo end-->

	<div class="top-nav notification-row">
		<!-- notificatoin dropdown start-->
		<ul class="nav pull-right top-menu">
			<!-- user login dropdown start-->
			<li class="dropdown">
				<a data-toggle="dropdown" class="dropdown-toggle" href="#">
					<span class="username">${principalSaupName }</span>
					<b class="caret"></b>
				</a>
				<ul class="dropdown-menu extended logout">
					<!-- <div class="log-arrow-up"></div> -->
					<div class="notify-arrow notify-arrow-blue" style="right: 11px;"></div>
					<li class="eborder-top"><a href="/myPage.do"><i class="fa fa-user-circle-o"></i> 마이 페이지</a></li>
					<li><a href="/logout.do"><i class="fa fa-sign-out"></i> 로그아웃</a></li>
				</ul>
			</li>
			<!-- user login dropdown end -->
		</ul>
		<!-- notificatoin dropdown end-->
	</div>
</header>
