package com.auction.security.services;

import com.auction.models.User;
import com.auction.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService implements UserDetailsService {

  @Autowired
  private UserRepository repository;

  @Autowired
  private PasswordEncoder encoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> userDetail = repository.findByUsername(username);

    // Converting userDetail to UserDetails
    return userDetail
      .map(UserInfoDetails::new)
      .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
  }

  public String addUser(User user) {
    user.setPassword(encoder.encode(user.getPassword()));
    repository.save(user);
    return "User Added Successfully";
  }
}
