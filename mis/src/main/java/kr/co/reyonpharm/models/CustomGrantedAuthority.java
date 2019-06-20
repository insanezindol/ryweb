package kr.co.reyonpharm.models;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
public class CustomGrantedAuthority implements GrantedAuthority {

	private static final long serialVersionUID = 1L;
	private String name; // ROLE 명
	// private List<Privilege> privileges; //ROLE 권한리스트

	@Override
	public String getAuthority() {
		return this.name;
	}

}
