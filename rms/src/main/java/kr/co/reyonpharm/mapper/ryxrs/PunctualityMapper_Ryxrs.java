package kr.co.reyonpharm.mapper.ryxrs;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.PunctualityInfo;

@Repository
public interface PunctualityMapper_Ryxrs {

	void selectAdtCapsList(Map param);

	List<PunctualityInfo> selectPunctualityList(PunctualityInfo param);

}
