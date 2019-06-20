package kr.co.reyonpharm.mapper.gwif;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.ApproInfo;

@Repository
public interface HRMapper_Gwif {

	int insertApproInfo(ApproInfo param);

}
