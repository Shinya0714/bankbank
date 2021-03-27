package com.example.demo.systemUser;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class SystemUserDetails implements UserDetails {

	private SystemUser systemUser;

	public SystemUserDetails(SystemUser systemUser) {
		this.systemUser = systemUser;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList(systemUser.getRole().split(","));
	}

	@Override
	public String getPassword() {
		return systemUser.getPassword();
	}

	@Override
	public String getUsername() {
		return systemUser.getName();
	}

	@Override
	// アカウントが有効期限内であるか
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	// アカウントがロックされていないか
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	//	資格情報が有効期限内であるか
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	// 有効なアカウントであるか
	public boolean isEnabled() {
		return true;
	}


}
