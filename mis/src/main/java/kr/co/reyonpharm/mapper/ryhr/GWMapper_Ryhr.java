package kr.co.reyonpharm.mapper.ryhr;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.UserInfo;

@Repository
public interface GWMapper_Ryhr {

	UserInfo selectUserInfo(UserInfo param);

}
