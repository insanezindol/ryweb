<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<sec:authentication property="principal.gender" var="principalGender" />
<section>
	<aside id="leftsidebar" class="sidebar">
		<div class="user-info">
			<div class="image">
				<img src="/images/user${principalGender }.png" width="48" height="48" />
			</div>
			<div class="info-container">
				<div class="name" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					<sec:authentication property="principal.kname" />
					<sec:authentication property="principal.posLog" />
				</div>
				<div class="email">
					<sec:authentication property="principal.deptName" />
				</div>
			</div>
		</div>

		<div class="menu">
			<ul class="list">
				<li class="header">NAVIGATION</li>
				<!-- <li id="lnbMain"><a href="/main.do"> <i class="material-icons">home</i> <span>메인</span></a></li> -->
				<li id="lnbHr">
					<a href="javascript:;" class="menu-toggle"> <i class="material-icons">person</i> <span>인력정보</span></a>
					<ul class="ml-menu">
						<!-- <li id="lnbSalaryView"><a href="/hr/salaryView.do">급여명세서 조회</a></li> -->
						<!-- <li id="lnbSalaryView"><a href="/hr/salaryView.do">임시메뉴</a></li> -->
						<li id="lnbHolidayList"><a href="/hr/holidayList.do">연차관리</a></li>
						<li id="lnbOvertimeList"><a href="/hr/overtimeList.do">초과근무관리</a></li>
					<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_ITT','ROLE_INSA')">
						<li id="lnbHolidayMasterList"><a href="/hr/holidayMasterList.do">연차 마스터 관리</a></li>
						<li id="lnbOvertimeMasterList"><a href="/hr/overtimeMasterList.do">초과근무 내역 관리</a></li>
					</sec:authorize>
						<!-- <li id="lnbOvertimeList"><a href="/hr/overtimeList.do">초과근무관리</a></li> -->
					</ul>
				</li>
				<!-- <li id="lnbSales">
					<a href="javascript:;" class="menu-toggle"> <i class="material-icons">local_hospital</i> <span>영업정보</span></a>
					<ul class="ml-menu">
						<li id="lnbSalesPerformanceView"><a href="/sales/salesPerformanceView.do">판매/수금 목표대비 실적 조회</a></li>
					</ul>
				</li> -->
			</ul>
		</div>

		<div class="legal">
			<div class="copyright">
				<a href="javascript:;">&copy; REYON PHARMACEUTICAL CO LTD.</a>
			</div>
			<div class="version">
				<b>Version : </b>1.0
			</div>
		</div>
	</aside>
	<aside id="rightsidebar" class="right-sidebar">
		<div class="tab-content">
			<div role="tabpanel" class="tab-pane fade in active in active" id="settings">
				<ul class="demo-choose-skin">
					<li onClick="javascript:location.href='/myPage.do';">
						<div class="light-blue"></div> <span>MyPage</span>
					</li>
					<li onClick="javascript:location.href='/logout.do';">
						<div class="blue"></div> <span>Louout</span>
					</li>
				</ul>
			</div>
		</div>
	</aside>
</section>