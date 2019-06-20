package kr.co.reyonpharm.mapper.ryhr;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.ContractInfo;
import kr.co.reyonpharm.models.PageParam;

@Repository
public interface RealestateMapper_Ryhr {

	// contract
	int selectContractListCount(PageParam pageParam);

	List<ContractInfo> selectContractList(PageParam pageParam);

	ContractInfo selectContract(ContractInfo param);

	int insertContract(ContractInfo param);

	int updateFileContract(ContractInfo param);

	int updateContract(ContractInfo param);

	int deleteContract(ContractInfo param);

	List<ContractInfo> selectContractStatisticsList(ContractInfo param);

	List<ContractInfo> selectTotalContractStatisticsList(ContractInfo param);

}
