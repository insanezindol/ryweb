package kr.co.reyonpharm.mapper.ryhr;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.ReportInfo;
import kr.co.reyonpharm.models.VoteInfo;

@Repository
public interface MainMapper_Ryhr {

	List<ReportInfo> selectReportList();

	VoteInfo selectVote(VoteInfo param);
	
	int insertVote(VoteInfo param);

	List<VoteInfo> selectNotParticipationList();

	List<VoteInfo> selectParticipationList();

	List<ReportInfo> selectVoteStatistics();

}
