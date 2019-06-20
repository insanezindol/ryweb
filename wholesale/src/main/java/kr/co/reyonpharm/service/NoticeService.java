package kr.co.reyonpharm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.ryxrs.WholesaleMapper_Ryxrs;
import kr.co.reyonpharm.models.NoticeInfo;
import kr.co.reyonpharm.models.PageParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("noticeService")
public class NoticeService {

	@Autowired
	private WholesaleMapper_Ryxrs wholesaleMapper_Ryxrs;

	// 공지사항 리스트 카운트
	@Transactional(value = "ryxrs_transactionManager")
	public int getNoticeInfoListCount(PageParam pageParam) {
		return wholesaleMapper_Ryxrs.selectNoticeInfoListCount(pageParam);
	}

	// 공지사항 리스트
	@Transactional(value = "ryxrs_transactionManager")
	public List<NoticeInfo> getNoticeInfoList(PageParam pageParam) {
		return wholesaleMapper_Ryxrs.selectNoticeInfoList(pageParam);
	}

	// 공지사항 조회
	@Transactional(value = "ryxrs_transactionManager")
	public NoticeInfo getNoticeInfo(NoticeInfo info) {
		return wholesaleMapper_Ryxrs.selectNoticeInfo(info);
	}
	
	// 공지사항 등록
	@Transactional(value = "ryxrs_transactionManager")
	public int addNoticeInfo(NoticeInfo info) {
		return wholesaleMapper_Ryxrs.insertNoticeInfo(info);
	}
	
	// 공지사항 첨부파일 수정
	@Transactional(value = "ryxrs_transactionManager")
	public int modifyNoticeInfoWithFile(NoticeInfo info) {
		return wholesaleMapper_Ryxrs.updateNoticeInfoWithFile(info);
	}
	
	// 공지사항 수정
	@Transactional(value = "ryxrs_transactionManager")
	public int modifyNoticeInfo(NoticeInfo info) {
		return wholesaleMapper_Ryxrs.updateNoticeInfo(info);
	}

	// 공지사항 삭제
	@Transactional(value = "ryxrs_transactionManager")
	public int deleteNoticeInfo(NoticeInfo info) {
		return wholesaleMapper_Ryxrs.deleteNoticeInfo(info);
	}

}
