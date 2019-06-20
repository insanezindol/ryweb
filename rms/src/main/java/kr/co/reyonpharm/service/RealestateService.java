package kr.co.reyonpharm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.ryhr.RealestateMapper_Ryhr;
import kr.co.reyonpharm.models.ContractInfo;
import kr.co.reyonpharm.models.PageParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("realestateService")
public class RealestateService {

	@Autowired
	private RealestateMapper_Ryhr realestateMapper_Ryhr;

	// 부동산 계약관리 전체 카운트
	@Transactional(value = "ryhr_transactionManager")
	public int getContractListCount(PageParam pageParam) {
		return realestateMapper_Ryhr.selectContractListCount(pageParam);
	}

	// 부동산 계약관리 리스트
	@Transactional(value = "ryhr_transactionManager")
	public List<ContractInfo> getContractList(PageParam pageParam) {
		return realestateMapper_Ryhr.selectContractList(pageParam);
	}

	// 부동산 계약관리 상세
	@Transactional(value = "ryhr_transactionManager")
	public ContractInfo getContract(ContractInfo param) {
		return realestateMapper_Ryhr.selectContract(param);
	}

	// 부동산 계약관리 등록
	@Transactional(value = "ryhr_transactionManager")
	public int addContract(ContractInfo param) {
		return realestateMapper_Ryhr.insertContract(param);
	}

	// 부동산 계약관리 파일 등록
	@Transactional(value = "ryhr_transactionManager")
	public int modifyFileContract(ContractInfo param) {
		return realestateMapper_Ryhr.updateFileContract(param);
	}

	// 부동산 계약관리 수정
	@Transactional(value = "ryhr_transactionManager")
	public int modifyContract(ContractInfo param) {
		return realestateMapper_Ryhr.updateContract(param);
	}

	// 부동산 계약관리 종료 처리
	@Transactional(value = "ryhr_transactionManager")
	public int deleteContract(ContractInfo param) {
		return realestateMapper_Ryhr.deleteContract(param);
	}

	// 부동산 계약관리 통계
	@Transactional(value = "ryhr_transactionManager")
	public List<ContractInfo> getContractStatisticsList(ContractInfo param) {
		return realestateMapper_Ryhr.selectContractStatisticsList(param);
	}

	// 부동산 계약관리 통계
	@Transactional(value = "ryhr_transactionManager")
	public List<ContractInfo> getTotalContractStatisticsList(ContractInfo param) {
		return realestateMapper_Ryhr.selectTotalContractStatisticsList(param);
	}

}
