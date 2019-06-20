package kr.co.reyonpharm.mapper.ryhr;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.PhoneInfo;

@Repository
public interface PhoneMapper_Ryhr {

	List<PhoneInfo> selectPhoneInfoBookList(PhoneInfo param);
	
	List<PhoneInfo> selectPhoneInfoList(PhoneInfo param);

	int insertPhoneInfo(PhoneInfo param);

	int updatePhoneInfo(PhoneInfo info);

	int deletePhoneInfo(PhoneInfo info);

	PhoneInfo selectPhoneInfoLastUpdateTime();

	int updatePhoneOrderInfo(PhoneInfo info);

}
