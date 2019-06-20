package kr.co.reyonpharm.mapper.ryacc;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.GroupwareExtInfo;

@Repository
public interface IttMapper_Ryacc {

	GroupwareExtInfo selectGroupwareChitByRyacc(GroupwareExtInfo param);

	int updateGroupwareChitByRyacc(GroupwareExtInfo param);

}
