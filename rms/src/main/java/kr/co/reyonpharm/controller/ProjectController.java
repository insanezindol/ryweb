package kr.co.reyonpharm.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import kr.co.reyonpharm.handler.CustomException;
import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.handler.CustomExceptionHandler;
import kr.co.reyonpharm.models.AttendantInfo;
import kr.co.reyonpharm.models.ConfirmInfo;
import kr.co.reyonpharm.models.DeptInfo;
import kr.co.reyonpharm.models.MeetingInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.ProjectInfo;
import kr.co.reyonpharm.models.TakeOverInfo;
import kr.co.reyonpharm.service.CommonService;
import kr.co.reyonpharm.service.MainService;
import kr.co.reyonpharm.service.ProjectService;
import kr.co.reyonpharm.util.CommonUtils;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/project/")
public class ProjectController extends CustomExceptionHandler {

	@Autowired
	MainService mainService;

	@Autowired
	ProjectService projectService;

	@Autowired
	CommonService commonService;

	/* 프로젝트 관리 메뉴 */

	// 프로젝트 관리 목록
	@RequestMapping(value = "projectList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView projectList(HttpServletRequest request, ModelAndView mav) {
		log.info("projectList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		int pageNo = StringUtil.reqNullCheckIntVal(request, "pageNo");
		int pageSize = StringUtil.reqNullCheckIntVal(request, "pageSize");
		//String searchType = StringUtil.reqNullCheck(request, "searchType");
		//String searchText = StringUtil.reqNullCheckHangulUTF8(request, "searchText");
		
		String s_projectName = StringUtil.reqNullCheckHangulUTF8(request, "s_projectName");
		String s_deptCode = StringUtil.reqNullCheckHangulUTF8(request, "s_deptCode");
		String s_regName = StringUtil.reqNullCheckHangulUTF8(request, "s_regName");
		String s_status = StringUtil.reqNullCheckHangulUTF8(request, "s_status");
		
		String queStr = StringUtil.nullCheck(request.getQueryString());

		// 사업장 코드 선택 했을경우
		String saupCookie = CommonUtils.getCookiesByRequest(request, "saupcode");

		// 로그인 정보
		String sabun = SpringSecurityUtil.getUsername();
		String deptCode = SpringSecurityUtil.getDeptCode();
		String respon = SpringSecurityUtil.getRespon();
		String roleName = SpringSecurityUtil.getRoleName();

		PageParam pageParam = new PageParam();
		pageParam.setPageNo(pageNo == 0 ? 1 : pageNo);
		pageParam.setPageSize(pageSize == 0 ? 15 : pageSize);
		pageParam.setS_projectName(s_projectName);
		pageParam.setS_deptCode(s_deptCode);
		pageParam.setS_regName(s_regName);
		pageParam.setS_status(s_status);

		if (!roleName.equals("ROLE_SUPERUSER") && !roleName.equals("ROLE_ADMIN")) {
			if (roleName.equals("ROLE_JINCHEONADMIN")) {
				// SUPER USER 이지만 본인 사업장만 권한이 있으면 해당 사업장만 보이도록 변경
				pageParam.setSaupCode("20");
			} else {
				// 결재자 정보
				ConfirmInfo confirmInfoParam = new ConfirmInfo();
				confirmInfoParam.setSabun(sabun);
				List<ConfirmInfo> confirmPersonList = commonService.getConfirmInfoList(confirmInfoParam);

				// 받은 인수 리스트
				TakeOverInfo receiveParam = new TakeOverInfo();
				receiveParam.setReceiveSabun(sabun);
				receiveParam.setStatus("AA");
				List<TakeOverInfo> receiveList = commonService.getTakeoverList(receiveParam);
				List<String> sabunList = new ArrayList<String>();
				for (int i = 0; i < receiveList.size(); i++) {
					sabunList.add(receiveList.get(i).getGiveSabun());
				}
				sabunList.add(sabun);

				// 권한 부여
				if (respon.equals("10") || respon.equals("20")) {
					// 본부장이거나 팀장이면
					DeptInfo deptInfo = new DeptInfo();
					deptInfo.setDeptParco(deptCode);
					List<DeptInfo> deptInfoList = commonService.getTotalDeptList(deptInfo);
					List<String> deptCodeList = new ArrayList<String>();
					for (int i = 0; i < deptInfoList.size(); i++) {
						deptCodeList.add(deptInfoList.get(i).getDeptCode());
					}
					for (int i = 0; i < confirmPersonList.size(); i++) {
						deptCodeList.add(confirmPersonList.get(i).getDeptCode());
					}
					deptCodeList.add(deptCode);
					// pageParam.setAttSabun(sabun);
					pageParam.setAttSabunList(sabunList);
					pageParam.setDeptCodeList(deptCodeList);
				} else {
					if (confirmPersonList.size() != 0) {
						// 결재자
						List<String> deptCodeList = new ArrayList<String>();
						for (int i = 0; i < confirmPersonList.size(); i++) {
							deptCodeList.add(confirmPersonList.get(i).getDeptCode());
						}
						deptCodeList.add(deptCode);
						// pageParam.setAttSabun(sabun);
						pageParam.setAttSabunList(sabunList);
						pageParam.setDeptCodeList(deptCodeList);
					} else {
						// 팀원
						// pageParam.setAttSabun(sabun);
						// pageParam.setSabun(sabun);
						pageParam.setSabunList(sabunList);
					}
				}
			}
		}

		// 사업장 코드 설정
		if (!saupCookie.equals("")) {
			pageParam.setSaupCode(saupCookie);
		}

		try {
			pageParam.setQueStr(URLEncoder.encode(queStr, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		int listCnt = projectService.getProjectListCount(pageParam);
		pageParam.setTotalCount(listCnt);

		List<ProjectInfo> list = projectService.getProjectList(pageParam);

		mav.addObject("pageParam", pageParam);
		mav.addObject("list", list);
		mav.setViewName("project/projectList");
		return mav;
	}

	// 프로젝트 관리 상세
	@RequestMapping(value = "projectView.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView projectView(HttpServletRequest request, ModelAndView mav) {
		log.info("projectView.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String seq = StringUtil.reqNullCheck(request, "projectSeq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(seq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		// 로그인 정보
		String sabun = SpringSecurityUtil.getUsername();
		String deptCode = SpringSecurityUtil.getDeptCode();
		String respon = SpringSecurityUtil.getRespon();
		String roleName = SpringSecurityUtil.getRoleName();

		ProjectInfo param = new ProjectInfo();
		param.setProjectSeq(seq);
		ProjectInfo info = projectService.getProject(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		AttendantInfo attParam = new AttendantInfo();
		attParam.setMeetingSeq(seq);
		attParam.setAttendantType("03");
		List<AttendantInfo> attList1 = mainService.getAttendantList(attParam);
		attParam.setAttendantType("04");
		List<AttendantInfo> attList2 = mainService.getAttendantList(attParam);

		// 권한 부여
		boolean isPassFlag = false;
		boolean isMine = false;

		for (int i = 0; i < attList1.size(); i++) {
			AttendantInfo tmp = attList1.get(i);
			if (sabun.equals(tmp.getAttendantSabun())) {
				isPassFlag = true;
				break;
			}
		}

		for (int i = 0; i < attList2.size(); i++) {
			AttendantInfo tmp = attList2.get(i);
			if (sabun.equals(tmp.getAttendantSabun())) {
				isPassFlag = true;
				break;
			}
		}

		if (!roleName.equals("ROLE_SUPERUSER") && !roleName.equals("ROLE_ADMIN")) {
			if (respon.equals("10") || respon.equals("20")) {
				// 본부장이거나 팀장이면
				DeptInfo deptInfo = new DeptInfo();
				deptInfo.setDeptParco(deptCode);
				List<DeptInfo> deptInfoList = commonService.getTotalDeptList(deptInfo);

				for (int i = 0; i < deptInfoList.size(); i++) {
					if (deptInfoList.get(i).getDeptCode().equals(info.getDeptCode())) {
						isPassFlag = true;
						break;
					}
				}
			} else {
				// 팀원
			}
		} else {
			isPassFlag = true;
		}

		// 결재자이면 PASS
		ConfirmInfo confirmInfoParam = new ConfirmInfo();
		confirmInfoParam.setDeptCode(info.getDeptCode());
		List<ConfirmInfo> confirmPersonList = commonService.getConfirmInfoList(confirmInfoParam);

		ConfirmInfo confirmPerson = new ConfirmInfo();
		for (int i = 0; i < confirmPersonList.size(); i++) {
			confirmPerson = confirmPersonList.get(i);
			if (confirmPerson.getSabun().equals(sabun)) {
				isPassFlag = true;
			}
		}

		// 본인인지 확인
		if (sabun.equals(info.getRegSabun())) {
			isMine = true;
			isPassFlag = true;
		}

		// 받은 인수 리스트
		TakeOverInfo receiveParam = new TakeOverInfo();
		receiveParam.setReceiveSabun(sabun);
		receiveParam.setStatus("AA");
		List<TakeOverInfo> receiveList = commonService.getTakeoverList(receiveParam);
		List<String> sabunList = new ArrayList<String>();
		sabunList.add(sabun);
		for (int i = 0; i < receiveList.size(); i++) {
			sabunList.add(receiveList.get(i).getGiveSabun());
			if (receiveList.get(i).getGiveSabun().equals(info.getRegSabun())) {
				isMine = true;
				isPassFlag = true;
			}
		}

		for (int i = 0; i < attList1.size(); i++) {
			AttendantInfo tmp = attList1.get(i);
			for (int j = 0; j < receiveList.size(); j++) {
				if (receiveList.get(j).getGiveSabun().equals(tmp.getAttendantSabun())) {
					isPassFlag = true;
					break;
				}
			}
		}

		for (int i = 0; i < attList2.size(); i++) {
			AttendantInfo tmp = attList2.get(i);
			for (int j = 0; j < receiveList.size(); j++) {
				if (receiveList.get(j).getGiveSabun().equals(tmp.getAttendantSabun())) {
					isPassFlag = true;
					break;
				}
			}
		}

		if (!isPassFlag) {
			throw new CustomException(CustomExceptionCodes.NOT_AUTHORIZED);
		}

		// 프로젝트에 관련된 회의
		MeetingInfo meetingParam = new MeetingInfo();
		meetingParam.setProjectSeq(seq);

		// 권한 부여
		if (!roleName.equals("ROLE_SUPERUSER") && !roleName.equals("ROLE_ADMIN")) {
			if (respon.equals("10") || respon.equals("20")) {
				// 본부장이거나 팀장이면
				DeptInfo deptInfo = new DeptInfo();
				deptInfo.setDeptParco(deptCode);
				List<DeptInfo> deptInfoList = commonService.getTotalDeptList(deptInfo);
				List<String> deptCodeList = new ArrayList<String>();
				for (int i = 0; i < deptInfoList.size(); i++) {
					deptCodeList.add(deptInfoList.get(i).getDeptCode());
				}
				// meetingParam.setAttSabun(sabun);
				meetingParam.setAttSabunList(sabunList);
				meetingParam.setDeptCodeList(deptCodeList);
			} else {
				// 팀원
				// meetingParam.setAttSabun(sabun);
				// meetingParam.setRegSabun(sabun);
				meetingParam.setSabunList(sabunList);
			}
		}

		List<MeetingInfo> meetingList = projectService.getMeetingListByProject(meetingParam);

		mav.addObject("seq", seq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.addObject("attList1", attList1);
		mav.addObject("attList2", attList2);
		mav.addObject("isMine", isMine);
		mav.addObject("meetingList", meetingList);
		mav.setViewName("project/projectView");
		return mav;
	}

	// 프로젝트 관리 등록
	@RequestMapping(value = "projectAdd.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView projectAdd(HttpServletRequest request, ModelAndView mav) {
		log.info("projectAdd.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("project/projectAdd");
		return mav;
	}

	// 프로젝트 관리 수정
	@RequestMapping(value = "projectModify.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView projectModify(HttpServletRequest request, ModelAndView mav) {
		log.info("projectModify.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String seq = StringUtil.reqNullCheck(request, "projectSeq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(seq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		String sabun = SpringSecurityUtil.getUsername();

		ProjectInfo param = new ProjectInfo();
		param.setProjectSeq(seq);
		ProjectInfo info = projectService.getProject(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		AttendantInfo attParam = new AttendantInfo();
		attParam.setMeetingSeq(seq);
		attParam.setAttendantType("03");
		List<AttendantInfo> attList1 = mainService.getAttendantList(attParam);
		attParam.setAttendantType("04");
		List<AttendantInfo> attList2 = mainService.getAttendantList(attParam);

		// 받은 인수 리스트
		TakeOverInfo receiveParam = new TakeOverInfo();
		receiveParam.setReceiveSabun(sabun);
		receiveParam.setStatus("AA");
		List<TakeOverInfo> receiveList = commonService.getTakeoverList(receiveParam);

		// 내가 등록한 회의 인지 검사
		boolean isMine = false;
		if (sabun.equals(info.getRegSabun())) {
			isMine = true;
		}

		// 인수인계 받은 회의 인지 검사
		for (int i = 0; i < receiveList.size(); i++) {
			if (receiveList.get(i).getGiveSabun().equals(info.getRegSabun())) {
				isMine = true;
				break;
			}
		}

		if (!isMine) {
			throw new CustomException(CustomExceptionCodes.NOT_AUTHORIZED);
		}

		mav.addObject("seq", seq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.addObject("attList1", attList1);
		mav.addObject("attList2", attList2);
		mav.setViewName("project/projectModify");
		return mav;
	}

	// 프로젝트 관리 등록 액션
	@RequestMapping(value = "projectAddAjax.json")
	public ModelAndView projectAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("projectAddAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String projectName = StringUtil.reqNullCheckHangulUTF8(request, "projectName");
				String status = StringUtil.reqNullCheck(request, "status");
				String attDeptCode1 = StringUtil.reqNullCheck(request, "attDeptCode1");
				String attSabun1 = StringUtil.reqNullCheck(request, "attSabun1");
				String attDeptCode2 = StringUtil.reqNullCheck(request, "attDeptCode2");
				String attSabun2 = StringUtil.reqNullCheck(request, "attSabun2");
				String projectStartDate = StringUtil.reqNullCheck(request, "projectStartDate");
				String projectEndDate = StringUtil.reqNullCheck(request, "projectEndDate");

				if ("".equals(projectName) || "".equals(status)) {
					log.error("projectName : " + projectName + ", status : " + status);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					String saupCode = SpringSecurityUtil.getSaupcode();
					String deptCode = SpringSecurityUtil.getDeptCode();
					String deptName = SpringSecurityUtil.getDeptName();
					String regSabun = SpringSecurityUtil.getUsername();
					String regName = SpringSecurityUtil.getKname();

					ProjectInfo info = new ProjectInfo();
					info.setProjectName(projectName);
					info.setStatus(status);
					info.setSaupCode(saupCode);
					info.setDeptCode(deptCode);
					info.setDeptName(deptName);
					info.setRegSabun(regSabun);
					info.setRegName(regName);
					info.setProjectStartDate(projectStartDate);
					info.setProjectEndDate(projectEndDate);

					int resultCnt = projectService.addProject(info);
					log.debug("resultCnt : " + resultCnt);
					if (resultCnt == 0) {
						mav.addObject("resultCode", CustomExceptionCodes.NOT_AFFECTED.getId());
						mav.addObject("resultMsg", CustomExceptionCodes.NOT_AFFECTED.getMsg());
					} else {
						String meetingSeq = String.valueOf(info.getCurrseq());

						String[] attDeptCode1Arr = attDeptCode1.split(",");
						String[] attSabun1Arr = attSabun1.split(",");
						String[] attDeptCode2Arr = attDeptCode2.split(",");
						String[] attSabun2Arr = attSabun2.split(",");

						AttendantInfo attendantInfo = new AttendantInfo();
						attendantInfo.setMeetingSeq(meetingSeq);
						attendantInfo.setRegSabun(regSabun);

						int orderSeq1 = 1;
						for (int i = 0; i < attDeptCode1Arr.length; i++) {
							// 참여자
							if (!attSabun1Arr[i].equals("") && !attDeptCode1Arr[i].equals("")) {
								attendantInfo.setAttendantSabun(attSabun1Arr[i]);
								attendantInfo.setAttendantType("03");
								attendantInfo.setAttendantDept(attDeptCode1Arr[i]);
								attendantInfo.setOrderSeq(String.valueOf(orderSeq1++));
								attendantInfo.setHiddenGb("N");
								mainService.addAttendant(attendantInfo);
							}
						}

						int orderSeq2 = 1;
						for (int i = 0; i < attDeptCode2Arr.length; i++) {
							// 참고인
							if (!attSabun2Arr[i].equals("") && !attDeptCode2Arr[i].equals("")) {
								attendantInfo.setAttendantSabun(attSabun2Arr[i]);
								attendantInfo.setAttendantType("04");
								attendantInfo.setAttendantDept(attDeptCode2Arr[i]);
								attendantInfo.setOrderSeq(String.valueOf(orderSeq2++));
								attendantInfo.setHiddenGb("N");
								mainService.addAttendant(attendantInfo);
							}
						}

						mav.addObject("resultCode", resultCnt);
						mav.addObject("resultMsg", "success");
					}
				}
			} else {
				log.error("auth : " + auth);
				mav.addObject("resultCode", CustomExceptionCodes.INVALID_PARAMETER.getId());
				mav.addObject("resultMsg", CustomExceptionCodes.INVALID_PARAMETER.getMsg());
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
			mav.addObject("resultCode", CustomExceptionCodes.SYSTEM_ERROR.getId());
			mav.addObject("resultMsg", CustomExceptionCodes.SYSTEM_ERROR.getMsg());
		}
		return mav;
	}

	// 프로젝트 관리 수정 액션
	@RequestMapping(value = "projectModifyAjax.json")
	public ModelAndView projectModifyAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("projectModifyAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String seq = StringUtil.reqNullCheck(request, "seq");
				String projectName = StringUtil.reqNullCheckHangulUTF8(request, "projectName");
				String status = StringUtil.reqNullCheck(request, "status");
				String attDeptCode1 = StringUtil.reqNullCheck(request, "attDeptCode1");
				String attSabun1 = StringUtil.reqNullCheck(request, "attSabun1");
				String attDeptCode2 = StringUtil.reqNullCheck(request, "attDeptCode2");
				String attSabun2 = StringUtil.reqNullCheck(request, "attSabun2");
				String projectStartDate = StringUtil.reqNullCheck(request, "projectStartDate");
				String projectEndDate = StringUtil.reqNullCheck(request, "projectEndDate");

				if ("".equals(seq) || "".equals(projectName) || "".equals(status)) {
					log.error("seq : " + seq + ", projectName : " + projectName + ", status : " + status);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					// 권한 부여
					String sabun = SpringSecurityUtil.getUsername();
					String name = SpringSecurityUtil.getKname();

					ProjectInfo info = new ProjectInfo();
					info.setProjectSeq(seq);
					info.setProjectName(projectName);
					info.setStatus(status);
					info.setUpdSabun(sabun);
					info.setUpdName(name);
					info.setProjectStartDate(projectStartDate);
					info.setProjectEndDate(projectEndDate);

					int resultCnt = projectService.modifyProject(info);

					if (resultCnt == 0) {
						log.error("resultCnt : " + resultCnt);
						mav.addObject("resultCode", CustomExceptionCodes.NOT_AUTHORIZED.getId());
						mav.addObject("resultMsg", CustomExceptionCodes.NOT_AUTHORIZED.getMsg());
					} else {
						// 참여자, 참고인 삭제
						AttendantInfo attendantInfo = new AttendantInfo();
						attendantInfo.setMeetingSeq(seq);
						attendantInfo.setRegSabun(sabun);
						attendantInfo.setAttendantType("project");
						mainService.deleteAttendant(attendantInfo);

						String[] attDeptCode1Arr = attDeptCode1.split(",");
						String[] attSabun1Arr = attSabun1.split(",");
						String[] attDeptCode2Arr = attDeptCode2.split(",");
						String[] attSabun2Arr = attSabun2.split(",");

						int orderSeq1 = 1;
						for (int i = 0; i < attDeptCode1Arr.length; i++) {
							// 참여자
							if (!attSabun1Arr[i].equals("") && !attDeptCode1Arr[i].equals("")) {
								attendantInfo.setAttendantSabun(attSabun1Arr[i]);
								attendantInfo.setAttendantType("03");
								attendantInfo.setAttendantDept(attDeptCode1Arr[i]);
								attendantInfo.setOrderSeq(String.valueOf(orderSeq1++));
								attendantInfo.setHiddenGb("N");
								mainService.addAttendant(attendantInfo);
							}
						}

						int orderSeq2 = 1;
						for (int i = 0; i < attDeptCode2Arr.length; i++) {
							// 참고인
							if (!attSabun2Arr[i].equals("") && !attDeptCode2Arr[i].equals("")) {
								attendantInfo.setAttendantSabun(attSabun2Arr[i]);
								attendantInfo.setAttendantType("04");
								attendantInfo.setAttendantDept(attDeptCode2Arr[i]);
								attendantInfo.setOrderSeq(String.valueOf(orderSeq2++));
								attendantInfo.setHiddenGb("N");
								mainService.addAttendant(attendantInfo);
							}
						}

						mav.addObject("resultCode", resultCnt);
						mav.addObject("resultMsg", "success");
					}
				}
			} else {
				log.error("auth : " + auth);
				mav.addObject("resultCode", CustomExceptionCodes.INVALID_PARAMETER.getId());
				mav.addObject("resultMsg", CustomExceptionCodes.INVALID_PARAMETER.getMsg());
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
			mav.addObject("resultCode", CustomExceptionCodes.SYSTEM_ERROR.getId());
			mav.addObject("resultMsg", CustomExceptionCodes.SYSTEM_ERROR.getMsg());
		}
		return mav;
	}

	// 프로젝트 관리 삭제 액션
	@RequestMapping(value = "projectDeleteAjax.json")
	public ModelAndView projectDeleteAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("projectDeleteAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String seq = StringUtil.reqNullCheck(request, "seq");

				if ("".equals(seq)) {
					log.error("seq : " + seq);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					MeetingInfo mInfo = new MeetingInfo();
					mInfo.setProjectSeq(seq);
					int mCnt = projectService.getMeetingCntByProject(mInfo);

					if (mCnt != 0) {
						log.error("mCnt : " + mCnt);
						mav.addObject("resultCode", CustomExceptionCodes.IMPOSSIBLE_DELETE_PROJECT.getId());
						mav.addObject("resultMsg", CustomExceptionCodes.IMPOSSIBLE_DELETE_PROJECT.getMsg());
					} else {
						AttendantInfo attInfo = new AttendantInfo();
						attInfo.setMeetingSeq(seq);
						attInfo.setAttendantType("project");
						int attResultCnt = mainService.deleteAttendant(attInfo);
						log.info("attResultCnt : " + attResultCnt);

						ProjectInfo info = new ProjectInfo();
						info.setProjectSeq(seq);
						int resultCnt = projectService.deleteProject(info);

						if (resultCnt == 0) {
							log.error("resultCnt : " + resultCnt);
							mav.addObject("resultCode", CustomExceptionCodes.NOT_AUTHORIZED.getId());
							mav.addObject("resultMsg", CustomExceptionCodes.NOT_AUTHORIZED.getMsg());
						} else {
							mav.addObject("resultCode", resultCnt);
							mav.addObject("resultMsg", "success");
						}
					}
				}
			} else {
				log.error("auth : " + auth);
				mav.addObject("resultCode", CustomExceptionCodes.INVALID_PARAMETER.getId());
				mav.addObject("resultMsg", CustomExceptionCodes.INVALID_PARAMETER.getMsg());
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
			mav.addObject("resultCode", CustomExceptionCodes.SYSTEM_ERROR.getId());
			mav.addObject("resultMsg", CustomExceptionCodes.SYSTEM_ERROR.getMsg());
		}
		return mav;
	}

}
