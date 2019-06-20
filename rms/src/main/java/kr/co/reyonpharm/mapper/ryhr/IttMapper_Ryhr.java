package kr.co.reyonpharm.mapper.ryhr;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.CommonCodeInfo;
import kr.co.reyonpharm.models.GroupwareExtInfo;
import kr.co.reyonpharm.models.PageParam;

@Repository
public interface IttMapper_Ryhr {

	int selectCommonCodeListCount(PageParam param);

	List<CommonCodeInfo> selectCommonCodeList(PageParam param);

	CommonCodeInfo selectCommonCodeInfo(CommonCodeInfo param);

	int insertCommonCodeInfo(CommonCodeInfo param);

	int updateCommonCodeInfo(CommonCodeInfo param);

	int deleteCommonCodeInfo(CommonCodeInfo param);

	int selectGroupwareExtListCount(PageParam param);

	List<GroupwareExtInfo> selectGroupwareExtList(PageParam param);

	GroupwareExtInfo selectGroupwareExt(GroupwareExtInfo param);

	int insertGroupwareExt(GroupwareExtInfo param);

	int updateGroupwareExt(GroupwareExtInfo param);

	int deleteGroupwareExt(GroupwareExtInfo param);

}
