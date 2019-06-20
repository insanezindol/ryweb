<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<aside>
	<div id="sidebar" class="nav-collapse ">
		<!-- sidebar menu start-->
		<ul class="sidebar-menu">
			
			<li id="menu01" class="">
				<a class="" href="/main.do">
					<i class="fa fa-laptop"></i> <span>메인</span>
				</a>
			</li>
			
			<li id="menu02" class="">
				<a class="" href="/notice/noticeList.do">
					<i class="fa fa-book"></i> <span>공지사항</span>
				</a>
			</li>
			
			<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SUPERUSER')">
			<li id="menu98" class="sub-menu">
				<a href="javascript:;" class="">
					<i class="fa fa-cog"></i> <span>관리자</span>
					<span class="menu-arrow arrow_carrot-right"></span>
				</a>
				<ul class="sub">
					<li><a class="" href="/admin/userList.do">사용자 관리</a></li>
					<li><a class="" href="/admin/loginLogList.do">접속 로그 관리</a></li>
				</ul>
			</li>
			</sec:authorize>
			
			<li id="menu99" class="sub-menu">
				<a href="javascript:;" class="">
					<i class="fa fa-id-card-o"></i> <span>MY PAGE</span>
					<span class="menu-arrow arrow_carrot-right"></span>
				</a>
				<ul class="sub">
					<li><a class="" href="/myPage.do">마이 페이지</a></li>
					<li><a class="" href="/logout.do">로그아웃</a></li>
				</ul>
			</li>
			
		</ul>
		<!-- sidebar menu end-->
	</div>
</aside>