package kr.co.reyonpharm.mapper.gwif;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.GroupwareExtInfo;

@Repository
public interface IttMapper_Gwif {

	GroupwareExtInfo selectGroupwareChitByGwif(GroupwareExtInfo param);
	
	int deleteGroupwareChitByRyacc(GroupwareExtInfo param);
}
