package kr.co.reyonpharm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.ryhr.MainMapper_Ryhr;
import kr.co.reyonpharm.models.PollInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("mainService")
public class MainService {

	@Autowired
	private MainMapper_Ryhr mainMapper_Ryhr;

	@Transactional(value = "ryhr_transactionManager")
	public List<PollInfo> selectPoll(PollInfo param) {
		return mainMapper_Ryhr.selectPoll(param);
	}
	
	@Transactional(value = "ryhr_transactionManager")
	public int insertPoll(PollInfo param) {
		return mainMapper_Ryhr.insertPoll(param);
	}
	
	@Transactional(value = "ryhr_transactionManager")
	public List<PollInfo> selectStatistics() {
		return mainMapper_Ryhr.selectStatistics();
	}

}
