package kr.co.reyonpharm.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.handler.CustomExceptionHandler;
import kr.co.reyonpharm.models.ComingInfo;
import kr.co.reyonpharm.models.CommonCodeInfo;
import kr.co.reyonpharm.models.ConfirmInfo;
import kr.co.reyonpharm.models.DateTimePickerInfo;
import kr.co.reyonpharm.models.DeptInfo;
import kr.co.reyonpharm.models.MeetingInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.ProjectInfo;
import kr.co.reyonpharm.models.TakeOverInfo;
import kr.co.reyonpharm.service.CommonService;
import kr.co.reyonpharm.service.ProjectService;
import kr.co.reyonpharm.util.CommonUtils;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@Controller
@RequestMapping("/common/")
public class CommonController extends CustomExceptionHandler {

	@Autowired
	CommonService commonService;

	@Autowired
	ProjectService projectService;

	// 전체 사용자 리스트
	@RequestMapping(value = "getTotalUserListAjax.json")
	public ModelAndView getTotalUserListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getTotalUserListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				JSONArray list = commonService.getTotalUserListToJson();
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", list);
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

	// 전체 부서 리스트
	@RequestMapping(value = "getTotalDeptListAjax.json")
	public ModelAndView getTotalDeptListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getTotalDeptListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				JSONArray list = commonService.getTotalDeptListToJson();
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", list);
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
	
	// 전체 날씨 리스트
	@RequestMapping(value = "getWeatherAjax.json")
	public ModelAndView getWeatherAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getWeatherAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				JSONObject object = commonService.getWeatherToJson();
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", object);
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

	// 현재 진행 프로젝트 리스트
	@RequestMapping(value = "getActiveProjectListAjax.json")
	public ModelAndView getActiveProjectListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getActiveProjectListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				int pageNo = StringUtil.reqNullCheckIntVal(request, "pageNo");
				int pageSize = StringUtil.reqNullCheckIntVal(request, "pageSize");
				String s_projectName = StringUtil.reqNullCheckHangulUTF8(request, "s_projectName");

				// 로그인 정보
				String sabun = SpringSecurityUtil.getUsername();
				String deptCode = SpringSecurityUtil.getDeptCode();
				String respon = SpringSecurityUtil.getRespon();
				String roleName = SpringSecurityUtil.getRoleName();

				PageParam pageParam = new PageParam();
				pageParam.setPageNo(pageNo == 0 ? 1 : pageNo);
				pageParam.setPageSize(pageSize == 0 ? 15 : pageSize);
				pageParam.setS_projectName(s_projectName);

				// 권한 부여
				if (!roleName.equals("ROLE_SUPERUSER") && !roleName.equals("ROLE_ADMIN")) {
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

				int listCnt = projectService.getProjectListCount(pageParam);
				pageParam.setTotalCount(listCnt);

				List<ProjectInfo> list = projectService.getProjectList(pageParam);

				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", list);
				mav.addObject("pageParam", pageParam);
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

	// 주차권 미등록 된 방문자 리스트
	@RequestMapping(value = "getActiveTicketListAjax.json")
	public ModelAndView getActiveTicketListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getActiveTicketListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				int pageNo = StringUtil.reqNullCheckIntVal(request, "pageNo");
				int pageSize = StringUtil.reqNullCheckIntVal(request, "pageSize");
				String searchType = StringUtil.reqNullCheck(request, "searchType");
				String searchText = StringUtil.reqNullCheckHangulUTF8(request, "searchText");
				String visitSeq = StringUtil.reqNullCheck(request, "visitSeq");

				// 로그인 정보
				String sabun = SpringSecurityUtil.getUsername();
				String deptCode = SpringSecurityUtil.getDeptCode();
				String respon = SpringSecurityUtil.getRespon();
				String roleName = SpringSecurityUtil.getRoleName();

				PageParam pageParam = new PageParam();
				pageParam.setPageNo(pageNo == 0 ? 1 : pageNo);
				pageParam.setPageSize(pageSize == 0 ? 15 : pageSize);
				pageParam.setSearchType(searchType);
				pageParam.setSearchText(searchText);
				pageParam.setMeetingType("02");
				pageParam.setVisitSeq(visitSeq);

				// 권한 부여
				if (!roleName.equals("ROLE_SUPERUSER") && !roleName.equals("ROLE_ADMIN") && !roleName.equals("ROLE_CHONGMU")) {
					// SUPER USER가 아니면서 총무팀이 아니면
					if (respon.equals("10") || respon.equals("20")) {
						// 본부장이거나 팀장이면
						DeptInfo deptInfo = new DeptInfo();
						deptInfo.setDeptParco(deptCode);
						List<DeptInfo> deptInfoList = commonService.getTotalDeptList(deptInfo);

						List<String> deptCodeList = new ArrayList<String>();
						for (int i = 0; i < deptInfoList.size(); i++) {
							deptCodeList.add(deptInfoList.get(i).getDeptCode());
						}

						pageParam.setDeptCodeList(deptCodeList);
						pageParam.setAttSabun(sabun);
					} else {
						// 팀원
						pageParam.setSabun(sabun);
						pageParam.setAttSabun(sabun);
					}
				}

				int listCnt = commonService.getActiveTicketListCount(pageParam);
				pageParam.setTotalCount(listCnt);

				List<ComingInfo> list = commonService.getActiveTicketList(pageParam);

				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", list);
				mav.addObject("pageParam", pageParam);
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

	// 방문목적을 보기 위한 전체 방문 리스트 조회
	@RequestMapping(value = "getActiveVisitorListAjax.json")
	public ModelAndView getActiveVisitorListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getActiveVisitorListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				int pageNo = StringUtil.reqNullCheckIntVal(request, "pageNo");
				int pageSize = StringUtil.reqNullCheckIntVal(request, "pageSize");
				String searchType = StringUtil.reqNullCheck(request, "searchType");
				String searchText = StringUtil.reqNullCheckHangulUTF8(request, "searchText");

				PageParam pageParam = new PageParam();
				pageParam.setPageNo(pageNo == 0 ? 1 : pageNo);
				pageParam.setPageSize(pageSize == 0 ? 15 : pageSize);
				pageParam.setSearchType(searchType);
				pageParam.setSearchText(searchText);
				pageParam.setMeetingType("02");

				int listCnt = commonService.getActiveVisitorListCount(pageParam);
				pageParam.setTotalCount(listCnt);

				List<MeetingInfo> list = commonService.getActiveVisitorList(pageParam);

				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", list);
				mav.addObject("pageParam", pageParam);
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

	// 공통코드 리스트
	@RequestMapping(value = "getCommonCodeListAjax.json")
	public ModelAndView getCommonCodeListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getCommonCodeListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String grpCode = StringUtil.reqNullCheck(request, "grpCode");
				if ("".equals(grpCode)) {
					log.error("grpCode : " + grpCode);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					// 로그인 정보
					String saupCode = SpringSecurityUtil.getSaupcode();
					String roleName = SpringSecurityUtil.getRoleName();

					CommonCodeInfo param = new CommonCodeInfo();
					param.setGrpCode(grpCode);
					param.setQueType("01");
					if (!roleName.equals("ROLE_SUPERUSER") && !roleName.equals("ROLE_ADMIN") && !roleName.equals("ROLE_CHONGMU")) {
						// SUPER USER가 아니면서 총무팀이 아니면
						param.setItem1(saupCode);
					}

					List<CommonCodeInfo> list = commonService.getCommonCodeList(param);
					mav.addObject("resultCode", 0);
					mav.addObject("resultMsg", list);
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

	// 스케쥴 리스트
	@RequestMapping(value = "getTimetableListAjax.json")
	public ModelAndView getTimetableListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getTimetableListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String searchDate = StringUtil.reqNullCheck(request, "searchDate");
				String meetingStartDate = StringUtil.reqNullCheck(request, "meetingStartDate");
				String meetingEndDate = StringUtil.reqNullCheck(request, "meetingEndDate");
				String meetingSeq = StringUtil.reqNullCheck(request, "meetingSeq");
				if ("".equals(searchDate)) {
					log.error("searchDate : " + searchDate);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					// 로그인 정보
					String sabun = SpringSecurityUtil.getUsername();
					String saupCode = SpringSecurityUtil.getSaupcode();
					String roleName = SpringSecurityUtil.getRoleName();

					// 권한 부여

					// 공통코드 회의실 리스트 조회
					CommonCodeInfo totalParam = new CommonCodeInfo();
					totalParam.setGrpCode("AG");
					totalParam.setQueType("01");
					if (!roleName.equals("ROLE_SUPERUSER") && !roleName.equals("ROLE_ADMIN")) {
						totalParam.setItem1(saupCode);
					}
					List<CommonCodeInfo> totalRoomlist = commonService.getCommonCodeList(totalParam);

					// 예약가능한 회의실 리스트 조회
					MeetingInfo availableParam = new MeetingInfo();
					availableParam.setMeetingStartDate(meetingStartDate);
					availableParam.setMeetingEndDate(meetingEndDate);
					if (!roleName.equals("ROLE_SUPERUSER") && !roleName.equals("ROLE_ADMIN")) {
						availableParam.setSaupCode(saupCode);
					}
					if (!"".equals(meetingSeq)) {
						availableParam.setMeetingSeq(meetingSeq);
					}
					List<CommonCodeInfo> availableRoomlist = commonService.getAvailableRoomList(availableParam);

					// 해당일 예약 리스트 조회
					String meetingStartDay = searchDate + " 00:00";
					String meetingEndDay = searchDate + " 23:59";

					MeetingInfo param = new MeetingInfo();
					if (!roleName.equals("ROLE_SUPERUSER") && !roleName.equals("ROLE_ADMIN")) {
						param.setSaupCode(saupCode);
					}
					param.setMeetingStartDate(meetingStartDay);
					param.setMeetingEndDate(meetingEndDay);
					if (!"".equals(meetingSeq)) {
						param.setMeetingSeq(meetingSeq);
					}
					List<DateTimePickerInfo> list = commonService.getTimetableList(param);

					mav.addObject("resultCode", 0);
					mav.addObject("resultMsg", "success");
					mav.addObject("list", list);
					mav.addObject("totalRoomlist", totalRoomlist);
					mav.addObject("availableRoomlist", availableRoomlist);
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

	// 스케쥴 리스트
	@RequestMapping(value = "getGnbTimetableListAjax.json")
	public ModelAndView getGnbTimetableListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getGnbTimetableListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String searchDate = StringUtil.reqNullCheck(request, "searchDate");
				if ("".equals(searchDate)) {
					log.error("searchDate : " + searchDate);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					// 로그인 정보
					String saupCode = SpringSecurityUtil.getSaupcode();
					String roleName = SpringSecurityUtil.getRoleName();

					// 권한 부여

					// 공통코드 회의실 리스트 조회
					CommonCodeInfo totalParam = new CommonCodeInfo();
					totalParam.setGrpCode("AG");
					totalParam.setQueType("01");
					if (!roleName.equals("ROLE_SUPERUSER") && !roleName.equals("ROLE_ADMIN")) {
						totalParam.setItem1(saupCode);
					}
					List<CommonCodeInfo> totalRoomlist = commonService.getCommonCodeList(totalParam);

					// 해당일 예약 리스트 조회
					String meetingStartDay = searchDate + " 00:00";
					String meetingEndDay = searchDate + " 23:59";

					MeetingInfo param = new MeetingInfo();
					if (!roleName.equals("ROLE_SUPERUSER") && !roleName.equals("ROLE_ADMIN")) {
						param.setSaupCode(saupCode);
					}
					param.setMeetingStartDate(meetingStartDay);
					param.setMeetingEndDate(meetingEndDay);
					List<DateTimePickerInfo> list = commonService.getTimetableList(param);

					mav.addObject("resultCode", 0);
					mav.addObject("resultMsg", "success");
					mav.addObject("list", list);
					mav.addObject("totalRoomlist", totalRoomlist);
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

	// 진행 대기 내역 리스트
	@RequestMapping(value = "getTodoListAjax.json")
	public ModelAndView getTodoListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getTodoListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {

				// 로그인 정보
				String sabun = SpringSecurityUtil.getUsername();
				String deptCode = SpringSecurityUtil.getDeptCode();

				// 공통 파라미터
				PageParam pageParam = new PageParam();

				// 결재자 정보
				ConfirmInfo confirmInfoParam = new ConfirmInfo();
				confirmInfoParam.setSabun(sabun);
				List<ConfirmInfo> confirmPersonList = commonService.getConfirmInfoList(confirmInfoParam);

				if (confirmPersonList.size() != 0) {
					// 결재자
					List<String> deptCodeList = new ArrayList<String>();
					for (int i = 0; i < confirmPersonList.size(); i++) {
						deptCodeList.add(confirmPersonList.get(i).getDeptCode());
					}
					deptCodeList.add(deptCode);
					pageParam.setSabun(sabun);
					pageParam.setDeptCodeList(deptCodeList);
				} else {
					// 그 외 인원 (팀원, 본부장, SUPERUSER)
					pageParam.setSabun(sabun);
				}

				List<MeetingInfo> list = commonService.getGnbTodoList(pageParam);

				String notRegisterCnt = "0";
				String notConfirmCnt = "0";
				String notReturnCnt = "0";
				String notSumCnt = "0";
				for (int i = 0; i < list.size(); i++) {
					MeetingInfo info = list.get(i);
					if (info.getMeetingStatus().equals("02")) {
						notRegisterCnt = info.getCnt();
					} else if (info.getMeetingStatus().equals("03") || info.getMeetingStatus().equals("04")) {
						notConfirmCnt = info.getCnt();
					} else if (info.getMeetingStatus().equals("99")) {
						notReturnCnt = info.getCnt();
					}
				}
				notSumCnt = String.valueOf(Integer.parseInt(notRegisterCnt) + Integer.parseInt(notConfirmCnt) + Integer.parseInt(notReturnCnt));

				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("notRegisterCnt", notRegisterCnt);
				mav.addObject("notConfirmCnt", notConfirmCnt);
				mav.addObject("notReturnCnt", notReturnCnt);
				mav.addObject("notSumCnt", notSumCnt);
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

	// 메인 달력 스케쥴 리스트
	@RequestMapping(value = "getMainScheduleListAjax.json")
	public ModelAndView getMainScheduleListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getMainScheduleListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				// 사업장 코드 선택 했을경우
				String saupCookie = CommonUtils.getCookiesByRequest(request, "saupcode");

				String startDate = StringUtil.reqNullCheck(request, "startDate");
				String endDate = StringUtil.reqNullCheck(request, "endDate");
				if ("".equals(startDate) || "".equals(endDate)) {
					log.error("startDate : " + startDate + ", endDate : " + endDate);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					// 로그인 정보
					String sabun = SpringSecurityUtil.getUsername();
					String deptCode = SpringSecurityUtil.getDeptCode();
					String respon = SpringSecurityUtil.getRespon();
					String roleName = SpringSecurityUtil.getRoleName();

					// 이달 예약 리스트 조회
					PageParam pageParam = new PageParam();
					pageParam.setSaupCode(saupCookie);
					pageParam.setStartDate(startDate);
					pageParam.setEndDate(endDate);

					// 권한 부여
					// SUPER USER 인지 검사
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
							pageParam.setAttSabun(sabun);
							pageParam.setDeptCodeList(deptCodeList);
						} else {
							// 팀원
							pageParam.setAttSabun(sabun);
							pageParam.setSabun(sabun);
						}
					}

					List<DateTimePickerInfo> list = commonService.getMainScheduleList(pageParam);

					mav.addObject("resultCode", 0);
					mav.addObject("resultMsg", "success");
					mav.addObject("list", list);
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
	
	// 회의/방문자 관리로 보내기
	@RequestMapping(value = "changeMeetingTypeAjax.json")
	public ModelAndView changeMeetingTypeAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("changeMeetingTypeAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String meetingType = StringUtil.reqNullCheck(request, "meetingType");
			String meetingSeq = StringUtil.reqNullCheck(request, "seq");
			if (auth.equals("reyon")) {
				MeetingInfo param = new MeetingInfo();
				param.setMeetingSeq(meetingSeq);
				param.setMeetingType(meetingType);
				int cnt = commonService.changeMeetingType(param);
				if (cnt == 1) {
					mav.addObject("resultCode", 0);
					mav.addObject("resultMsg", "success");
				} else {
					log.error("meetingSeq : " + meetingSeq + ", meetingType : " + meetingType);
					mav.addObject("resultCode", -1);
					mav.addObject("resultMsg", "업데이트 실패");
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
