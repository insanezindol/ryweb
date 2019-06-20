package kr.co.reyonpharm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.reyonpharm.mapper.ryhr.MainMapper_Ryhr;
import kr.co.reyonpharm.models.ReportInfo;
import kr.co.reyonpharm.models.VoteInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("mainService")
public class MainService {

	@Autowired
	private MainMapper_Ryhr mainMapper_Ryhr;

	public List<ReportInfo> getReportList() {
		return mainMapper_Ryhr.selectReportList();
	}
	
	public VoteInfo getVote(VoteInfo param) {
		return mainMapper_Ryhr.selectVote(param);
	}

	public int addVote(VoteInfo param) {
		return mainMapper_Ryhr.insertVote(param);
	}

	public List<VoteInfo> getNotParticipationList() {
		return mainMapper_Ryhr.selectNotParticipationList();
	}

	public List<VoteInfo> getParticipationList() {
		return mainMapper_Ryhr.selectParticipationList();
	}

	public List<ReportInfo> getVoteStatistics() {
		return mainMapper_Ryhr.selectVoteStatistics();
	}

}
