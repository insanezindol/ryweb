package kr.co.reyonpharm.models;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 5129954728321130514L;

	private String username;
	private String kname;
	private String fname;
	private String deptCode;
	private String deptName;
	private String password;
	private String saupcode;
	private String originSaupcode;
	private String saupname;
	private String insadept;
	private String insaname;
	private String retireDay;
	private String workType;
	private String accCode;
	private String grade;
	private String respon;
	private String positCd;
	private String posLog;
	private String reyonMail;
	private String superUser;
	private String contractUser;
	private String gender;
	private String roleType;

	private List<CustomGrantedAuthority> authorities;
	private final boolean accountNonExpired;
	private final boolean accountNonLocked;
	private final boolean credentialsNonExpired;
	private final boolean enabled;

	public CustomUserDetails() {
		this.accountNonExpired = true;
		this.accountNonLocked = true;
		this.credentialsNonExpired = true;
		this.enabled = true;
	}

	@Override
	public int hashCode() {
		return username.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof CustomUserDetails) {
			return username.equals(((CustomUserDetails) o).username);
		}
		return false;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

}
