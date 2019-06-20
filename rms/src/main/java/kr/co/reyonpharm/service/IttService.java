package kr.co.reyonpharm.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.gwif.IttMapper_Gwif;
import kr.co.reyonpharm.mapper.mssql.IttMapper_Mssql;
import kr.co.reyonpharm.mapper.ryacc.IttMapper_Ryacc;
import kr.co.reyonpharm.mapper.ryhr.IttMapper_Ryhr;
import kr.co.reyonpharm.mapper.ryxrs.IttMapper_Ryxrs;
import kr.co.reyonpharm.models.BtsInfo;
import kr.co.reyonpharm.models.CommonCodeInfo;
import kr.co.reyonpharm.models.GroupwareExtInfo;
import kr.co.reyonpharm.models.PageParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("ittService")
public class IttService {

	@Autowired
	private IttMapper_Ryxrs ittMapper_Ryxrs;

	@Autowired
	private IttMapper_Mssql ittMapper_Mssql;

	@Autowired
	private IttMapper_Ryhr ittMapper_Ryhr;
	
	@Autowired
	private IttMapper_Ryacc ittMapper_Ryacc;
	
	@Autowired
	private IttMapper_Gwif ittMapper_Gwif;

	// 제품 일련번호 이관 대상 조회
	@Transactional(value = "mssql_transactionManager")
	public List<BtsInfo> getBtsInfoListCnt(BtsInfo param) {
		return ittMapper_Mssql.selectBtsInfoListCnt(param);
	}

	// 제품 일련번호 이관 내역 조회
	@Transactional(value = "ryxrs_transactionManager")
	public List<BtsInfo> getMigrationInfoCnt(BtsInfo param) {
		return ittMapper_Ryxrs.selectMigrationInfoCnt(param);
	}

	// 제품 일련번호 이관
	@Transactional(value = "ryxrs_transactionManager")
	public Map<String, Integer> migrationProduct(String outdate) {
		log.info("outdate : " + outdate);
		Map<String, Integer> output = new Hashtable<String, Integer>();
		BtsInfo param = new BtsInfo();
		param.setOutdate(outdate);
		// 그날 데이터가 있을수도 있으니까 먼저 삭제
		int deleteCnt = ittMapper_Ryxrs.deleteBtsInfo(param);
		// MSSQL 리스트 조회
		List<BtsInfo> mssqlList = ittMapper_Mssql.selectBtsInfoList(param);
		int totalResultCnt = 0;
		for (int j = 0; j < mssqlList.size(); j++) {
			BtsInfo info = mssqlList.get(j);
			// ORACLE 등록
			int resultCnt = ittMapper_Ryxrs.insertBtsInfo(info);
			if (resultCnt == 1) {
				log.info("[SUC] " + info.getOutdate() + "\t" + info.getCustcode() + "\t" + info.getEcustcode() + "\t" + info.getItemcode() + "\t" + info.getOutqty() + "\t" + info.getOrderno() + "\t" + info.getSeq() + "\t" + info.getSerialno() + "\t" + info.getAgg1() + "\t" + info.getAgg2() + "\t" + info.getAgg4() + "\t" + info.getJumnNo() + "\t" + info.getJpmSeq());
				totalResultCnt++;
			} else {
				log.error("[ERR] " + info.getOutdate() + "\t" + info.getCustcode() + "\t" + info.getEcustcode() + "\t" + info.getItemcode() + "\t" + info.getOutqty() + "\t" + info.getOrderno() + "\t" + info.getSeq() + "\t" + info.getSerialno() + "\t" + info.getAgg1() + "\t" + info.getAgg2() + "\t" + info.getAgg4() + "\t" + info.getJumnNo() + "\t" + info.getJpmSeq());
			}
		}
		output.put("deleteSize", deleteCnt);
		output.put("originSize", mssqlList.size());
		output.put("copySize", totalResultCnt);
		return output;
	}

	// BTS ORACLE SELECT
	@Transactional(value = "ryxrs_transactionManager")
	public List<BtsInfo> getBtsInfo() {
		return ittMapper_Ryxrs.selectBtsInfo();
	}

	// BTS ORACLE INSERT
	@Transactional(value = "ryxrs_transactionManager")
	public int addBtsInfo(BtsInfo info) {
		return ittMapper_Ryxrs.insertBtsInfo(info);
	}

	// BTS ORACLE DELETE
	@Transactional(value = "ryxrs_transactionManager")
	public int deleteBtsInfo(BtsInfo info) {
		return ittMapper_Ryxrs.deleteBtsInfo(info);
	}

	// BTS MSSQL SELECT
	@Transactional(value = "mssql_transactionManager")
	public List<BtsInfo> getBtsInfoList(BtsInfo param) {
		return ittMapper_Mssql.selectBtsInfoList(param);
	}

	// 공통코드 전체 카운트
	@Transactional(value = "ryhr_transactionManager")
	public int getCommonCodeListCount(PageParam param) {
		return ittMapper_Ryhr.selectCommonCodeListCount(param);
	}

	// 공통코드 전체 리스트
	@Transactional(value = "ryhr_transactionManager")
	public List<CommonCodeInfo> getCommonCodeList(PageParam param) {
		return ittMapper_Ryhr.selectCommonCodeList(param);
	}

	// 공통코드 상세 조회
	@Transactional(value = "ryhr_transactionManager")
	public CommonCodeInfo getCommonCodeInfo(CommonCodeInfo param) {
		return ittMapper_Ryhr.selectCommonCodeInfo(param);
	}

	// 공통코드 추가
	@Transactional(value = "ryhr_transactionManager")
	public int addCommonCodeInfo(CommonCodeInfo param) {
		return ittMapper_Ryhr.insertCommonCodeInfo(param);
	}

	// 공통코드 수정
	@Transactional(value = "ryhr_transactionManager")
	public int modifyCommonCodeInfo(CommonCodeInfo param) {
		return ittMapper_Ryhr.updateCommonCodeInfo(param);
	}

	// 공통코드 삭제
	@Transactional(value = "ryhr_transactionManager")
	public int deleteCommonCodeInfo(CommonCodeInfo param) {
		return ittMapper_Ryhr.deleteCommonCodeInfo(param);
	}

	// 그룹웨어 접속 전체 카운트
	@Transactional(value = "ryhr_transactionManager")
	public int getGroupwareExtListCount(PageParam param) {
		return ittMapper_Ryhr.selectGroupwareExtListCount(param);
	}

	// 그룹웨어 접속 전체 리스트
	public List<GroupwareExtInfo> getGroupwareExtList(PageParam param) {
		return ittMapper_Ryhr.selectGroupwareExtList(param);
	}
	
	// 그룹웨어 접속 상세 조회
	@Transactional(value = "ryhr_transactionManager")
	public GroupwareExtInfo getGroupwareExt(GroupwareExtInfo param) {
		return ittMapper_Ryhr.selectGroupwareExt(param);
	}

	// 그룹웨어 접속 추가
	@Transactional(value = "ryhr_transactionManager")
	public int addGroupwareExt(GroupwareExtInfo param) {
		return ittMapper_Ryhr.insertGroupwareExt(param);
	}

	// 그룹웨어 접속 수정
	@Transactional(value = "ryhr_transactionManager")
	public int modifyGroupwareExt(GroupwareExtInfo param) {
		return ittMapper_Ryhr.updateGroupwareExt(param);
	}

	// 그룹웨어 접속 삭제
	@Transactional(value = "ryhr_transactionManager")
	public int deleteGroupwareExt(GroupwareExtInfo param) {
		return ittMapper_Ryhr.deleteGroupwareExt(param);
	}

	// 전표 상태 조회
	public GroupwareExtInfo getGroupwareChitByRyacc(GroupwareExtInfo param) {
		return ittMapper_Ryacc.selectGroupwareChitByRyacc(param);
	}
	
	// 그룹웨어 연동 상태 조회
	public GroupwareExtInfo getGroupwareChitByGwif(GroupwareExtInfo param) {
		return ittMapper_Gwif.selectGroupwareChitByGwif(param);
	}
	
	// 전표 테이블 상태값 업데이트
	public int setGroupwareChitByRyacc(GroupwareExtInfo param) {
		return ittMapper_Ryacc.updateGroupwareChitByRyacc(param);
	}

	// 그룹웨어 연동 데이터 삭제
	public int setGroupwareChitByGwif(GroupwareExtInfo param) {
		return ittMapper_Gwif.deleteGroupwareChitByRyacc(param);
	}

}
