package kr.co.reyonpharm.mapper.ryhr;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.PollInfo;

@Repository
public interface MainMapper_Ryhr {

	List<PollInfo> selectPoll(PollInfo param);

	int insertPoll(PollInfo param);

	List<PollInfo> selectStatistics();

}
