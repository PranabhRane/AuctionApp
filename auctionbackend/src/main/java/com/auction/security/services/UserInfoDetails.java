package com.auction.security.services;

import com.auction.models.User;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserInfoDetails implements org.springframework.security.core.userdetails.UserDetails {

  private final String name;
  private final String password;
  private final List<GrantedAuthority> authorities;

  public UserInfoDetails(User user) {
    name = user.getUsername();
    password = user.getPassword();
    authorities = user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return name;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
