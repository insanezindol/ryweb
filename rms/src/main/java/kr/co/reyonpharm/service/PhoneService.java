package kr.co.reyonpharm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.ryhr.PhoneMapper_Ryhr;
import kr.co.reyonpharm.models.PhoneInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("phoneService")
public class PhoneService {

	@Autowired
	private PhoneMapper_Ryhr phoneMapper_Ryhr;

	@Transactional(value = "ryhr_transactionManager")
	public List<PhoneInfo> getPhoneInfoBookList(PhoneInfo param) {
		return phoneMapper_Ryhr.selectPhoneInfoBookList(param);
	}
	
	@Transactional(value = "ryhr_transactionManager")
	public List<PhoneInfo> getPhoneInfoList(PhoneInfo param) {
		return phoneMapper_Ryhr.selectPhoneInfoList(param);
	}

	@Transactional(value = "ryhr_transactionManager")
	public int addPhoneInfo(PhoneInfo param) {
		return phoneMapper_Ryhr.insertPhoneInfo(param);
	}

	@Transactional(value = "ryhr_transactionManager")
	public int modifyPhoneInfo(PhoneInfo info) {
		return phoneMapper_Ryhr.updatePhoneInfo(info);
	}

	@Transactional(value = "ryhr_transactionManager")
	public int deletePhoneInfo(PhoneInfo info) {
		return phoneMapper_Ryhr.deletePhoneInfo(info);
	}

	@Transactional(value = "ryhr_transactionManager")
	public PhoneInfo getPhoneInfoLastUpdateTime() {
		return phoneMapper_Ryhr.selectPhoneInfoLastUpdateTime();
	}

	@Transactional(value = "ryhr_transactionManager")
	public int modifyPhoneOrderInfo(PhoneInfo info) {
		return phoneMapper_Ryhr.updatePhoneOrderInfo(info);
	}
	
}
