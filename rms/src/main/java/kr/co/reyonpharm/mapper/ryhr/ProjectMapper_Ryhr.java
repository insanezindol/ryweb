package kr.co.reyonpharm.mapper.ryhr;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.MeetingInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.ProjectInfo;

@Repository
public interface ProjectMapper_Ryhr {

	// project
	int selectProjectListCount(PageParam pageParam);

	List<ProjectInfo> selectProjectList(PageParam pageParam);

	ProjectInfo selectProject(ProjectInfo param);

	int insertProject(ProjectInfo info);

	int updateProject(ProjectInfo info);

	int deleteProject(ProjectInfo info);

	int selectMeetingCntByProject(MeetingInfo info);

	List<MeetingInfo> selectMeetingListByProject(MeetingInfo info);

}
