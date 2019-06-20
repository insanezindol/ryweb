package kr.co.reyonpharm.mapper.ryhr;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.CustomUserDetails;
import kr.co.reyonpharm.models.UserInfo;

@Repository
public interface LoginMapper_Ryhr {

	CustomUserDetails selectUserInfo(UserInfo param);

}
