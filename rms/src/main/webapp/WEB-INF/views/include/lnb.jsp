<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<aside>
	<div id="sidebar" class="nav-collapse ">
		<!-- sidebar menu start-->
		<ul class="sidebar-menu">
			<li id="menu01" class="">
				<a class="" href="/main.do">
					<i class="icon_house_alt"></i> <span>메인</span>
				</a>
			</li>
			<li id="menu02" class="">
				<a class="" href="/project/projectList.do">
					<i class="icon_toolbox_alt"></i> <span>프로젝트 관리</span>
				</a>
			</li>
			<li id="menu03" class="">
				<a class="" href="/meeting/meetingList.do">
					<i class="icon_calendar"></i> <span>회의 관리</span>
				</a>
			</li>
			<li id="menu04" class="">
				<a class="" href="/visitor/visitorList.do">
					<i class="icon_id"></i> <span>방문자 관리</span>
				</a>
			</li>
			<c:if test="${principalSaupcode == '10' }">
			<li id="menu05" class="">
				<a class="" href="/ticket/ticketList.do">
					<i class="icon_cone_alt"></i> <span>주차권 관리</span>
				</a>
			</li>
			</c:if>
			<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SUPERUSER','ROLE_ITT','ROLE_CHONGMU')">
			<li id="menu06" class="">
				<a class="" href="/coming/comingList.do">
					<i class="icon_contacts_alt"></i> <span>출입자 관리</span>
				</a>
			</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SUPERUSER','ROLE_ITT','ROLE_CHONGMU')">
			<li id="menu07" class="">
				<a class="" href="/realestate/realestateList.do">
					<i class="icon_building"></i> <span>부동산 계약 관리</span>
				</a>
			</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SUPERUSER','ROLE_ITT','ROLE_CHONGMU')">
			<li id="menu08" class="">
				<a class="" href="/vehicle/vehicleList.do">
					<i class="icon_globe"></i> <span>법인 차량 관리</span>
				</a>
			</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SUPERUSER','ROLE_ITT','ROLE_SALESMGR')">
			<li id="menu10" class="sub-menu">
				<a href="javascript:;" class="">
					<i class="icon_briefcase"></i> <span>SFE 관리</span>
					<span class="menu-arrow arrow_carrot-right"></span>
				</a>
				<ul class="sub">
					<li><a class="" href="/sfe/salesRouteActivity.do">활동 경로</a></li>
					<li><a class="" href="/sfe/salesRouteChange.do">기준 좌표 변경</a></li>
					<li><a class="" href="/sfe/salesTrendList.do">콜매출 Trend</a></li>
					<li><a class="" href="/sfe/salesAccountList.do">거래처 방문 활동</a></li>
					<li><a class="" href="/sfe/salesPointList.do">지점별 방문 활동</a></li>
					<li><a class="" href="/sfe/salesActivityList.do">활동 내역 조회</a></li>
					<li><a class="" href="/sfe/salesProfitList.do">방문 매출 현황</a></li>
				</ul>
			</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_ITT','ROLE_BPM')">
			<li id="menu13" class="">
				<a class="" href="/schedule/scheduleList.do">
					<i class="icon_easel"></i> <span>일정 관리</span>
				</a>
			</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SUPERUSER','ROLE_ITT','ROLE_CHONGMU','ROLE_JINCHEONMGR')">
			<li id="menu14" class="sub-menu">
				<a href="javascript:;" class="">
					<i class="icon_phone"></i> <span>내선번호 관리</span>
					<span class="menu-arrow arrow_carrot-right"></span>
				</a>
				<ul class="sub">
					<li><a class="" href="/phone/phonenumberBook.do">내선번호 편집</a></li>
					<li><a class="" href="/phone/phonenumberOrder.do">내선번호 순서편집</a></li>
					<li><a class="" href="/phone/phonenumberList.do" target="_blank">내선번호 현황</a></li>
				</ul>
			</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SUPERUSER','ROLE_ITT','ROLE_JINCHEONMGR')">
			<li id="menu15" class="">
				<a class="" href="/punctuality/punctualityList.do">
					<i class="icon_datareport"></i> <span>진천공장 근태 조회</span>
				</a>
			</li>
			<li id="menu16" class="">
				<a class="" href="/production/productionList.do">
					<i class="icon_info_alt"></i> <span>진천공장 알림 관리</span>
				</a>
			</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SUPERUSER','ROLE_ITT')">
			<li id="menu17" class="">
				<a class="" href="/control/controlList.do">
					<i class="glyphicon glyphicon-ok"></i> <span>재고자산 통제 관리</span>
				</a>
			</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_ITT','ROLE_INSA')">
			<li id="menu11" class="sub-menu">
				<a href="javascript:;" class="">
					<i class="icon_wallet"></i> <span>연말정산 관리</span>
					<span class="menu-arrow arrow_carrot-right"></span>
				</a>
				<ul class="sub">
					<li><a class="" href="/settlement/downloadMasterData.do">기초데이터 다운로드</a></li>
					<li><a class="" href="/settlement/uploadDeclaration.do">공제신고서 업로드</a></li>
					<li><a class="" href="/settlement/analyzeDeclaration.do">공제신고서 분석</a></li>
					<li><a class="" href="/settlement/uploadSpecification.do">지급명세서 업로드</a></li>
					<li><a class="" href="/settlement/analyzeSpecification.do">지급명세서 분석</a></li>
				</ul>
			</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_ADMIN')">
			<li id="menu09" class="sub-menu">
				<a href="javascript:;" class="">
					<i class="icon_desktop"></i> <span>PC 관리</span>
					<span class="menu-arrow arrow_carrot-right"></span>
				</a>
				<ul class="sub">
					<li><a class="" href="/app/pcOnOffList.do">PC ON/OFF 보고</a></li>
					<li><a class="" href="/app/pcMessageList.do">PC 메시지 전송</a></li>
					<li><a class="" href="/app/pcReleaseList.do">PC 종료 해제</a></li>
				</ul>
			</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SUPERUSER','ROLE_ITT')">
			<li id="menu99" class="sub-menu">
				<a href="javascript:;" class="">
					<i class="icon_star_alt"></i> <span>정보관리팀</span>
					<span class="menu-arrow arrow_carrot-right"></span>
				</a>
				<ul class="sub">
					<li><a class="" href="/itt/manualMigrationProduct.do">제품 일련번호 이관</a></li>
					<li><a class="" href="/itt/encryptView.do">암호화 관리</a></li>
					<li><a class="" href="/itt/groupwareExtList.do">그룹웨어 외부접속</a></li>
					<li><a class="" href="/itt/groupwareChit.do">전표 전자결재 초기화</a></li>
					<sec:authorize access="hasAnyRole('ROLE_ADMIN')">
					<li><a class="" href="/itt/commonCodeList.do">공통 코드 관리</a></li>
					<li><a class="" href="/dev/alarmList.do">푸시 알림 전송</a></li>
					<li><a class="" href="/dev/photoView.do">사진 조회</a></li>
					<li><a class="" href="/dev/map.do">지도 테스트</a></li>
					</sec:authorize>
				</ul>
			</li>
			</sec:authorize>
			<li id="menu12" class="sub-menu">
				<a href="javascript:;" class="">
					<i class="icon_id-2"></i> <span>MY PAGE</span>
					<span class="menu-arrow arrow_carrot-right"></span>
				</a>
				<ul class="sub">
					<li><a class="" href="/myPage.do">내 정보</a></li>
					<li><a class="" href="/toDo.do">진행 대기 내역</a></li>
					<li><a class="" href="/takeOver.do">인수인계 관리</a></li>
					<li><a class="" href="/logout.do">로그아웃</a></li>
				</ul>
			</li>
		</ul>
		<!-- sidebar menu end-->
	</div>
</aside>