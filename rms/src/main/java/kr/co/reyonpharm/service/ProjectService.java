package kr.co.reyonpharm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.ryhr.ProjectMapper_Ryhr;
import kr.co.reyonpharm.models.MeetingInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.ProjectInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("projectService")
public class ProjectService {

	@Autowired
	private ProjectMapper_Ryhr projectMapper_Ryhr;

	// 프로젝트 리스트 전체 건수 조회
	@Transactional(value = "ryhr_transactionManager")
	public int getProjectListCount(PageParam pageParam) {
		return projectMapper_Ryhr.selectProjectListCount(pageParam);
	}

	// 프로젝트 리스트 조회
	@Transactional(value = "ryhr_transactionManager")
	public List<ProjectInfo> getProjectList(PageParam pageParam) {
		return projectMapper_Ryhr.selectProjectList(pageParam);
	}

	// 프로젝트 정보 조회
	@Transactional(value = "ryhr_transactionManager")
	public ProjectInfo getProject(ProjectInfo param) {
		return projectMapper_Ryhr.selectProject(param);
	}

	// 프로젝트 등록
	@Transactional(value = "ryhr_transactionManager")
	public int addProject(ProjectInfo info) {
		return projectMapper_Ryhr.insertProject(info);
	}

	// 프로젝트 수정
	@Transactional(value = "ryhr_transactionManager")
	public int modifyProject(ProjectInfo info) {
		return projectMapper_Ryhr.updateProject(info);
	}

	// 프로젝트 삭제
	@Transactional(value = "ryhr_transactionManager")
	public int deleteProject(ProjectInfo info) {
		return projectMapper_Ryhr.deleteProject(info);
	}

	// 프로젝트에 관련된 회의 건수
	@Transactional(value = "ryhr_transactionManager")
	public int getMeetingCntByProject(MeetingInfo info) {
		return projectMapper_Ryhr.selectMeetingCntByProject(info);
	}

	// 프로젝트에 관련된 회의 리스트
	@Transactional(value = "ryhr_transactionManager")
	public List<MeetingInfo> getMeetingListByProject(MeetingInfo info) {
		return projectMapper_Ryhr.selectMeetingListByProject(info);
	}

}
