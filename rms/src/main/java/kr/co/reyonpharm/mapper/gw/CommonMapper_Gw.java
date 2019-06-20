package kr.co.reyonpharm.mapper.gw;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.GwMailInfo;

@Repository
public interface CommonMapper_Gw {

	int insertGwMailInfo(GwMailInfo param);

}
