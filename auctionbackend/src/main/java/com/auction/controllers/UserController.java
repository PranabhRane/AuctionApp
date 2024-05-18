package com.auction.controllers;

import com.auction.models.Bid;
import com.auction.models.User;
import com.auction.request.AuthRequest;
import com.auction.response.JwtResponse;
import com.auction.response.MessageResponse;
import com.auction.security.services.JwtService;
import com.auction.security.services.UserInfoService;
import com.auction.service.UserService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin
public class UserController {

  @Autowired
  private UserInfoService service;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private UserService userService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @PostMapping("addNewUser")
  public String addNewUser(@RequestBody User user) {
    return service.addUser(user);
  }

  @PostMapping("login")
  public ResponseEntity<JwtResponse> login(@RequestBody AuthRequest authRequest) {
    try {
      Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
      );
      if (authentication.isAuthenticated()) {
        var user = userService.getByUsername(authRequest.getUsername()).orElse(new User());
        var token = jwtService.generateToken(authRequest.getUsername());
        var jwtResponse = new JwtResponse();
        jwtResponse.setToken(token);
        jwtResponse.setRoles(user.getRoles());
        jwtResponse.setUsername(user.getUsername());
        return ResponseEntity.ok(jwtResponse);
      } else {
        throw new UsernameNotFoundException("invalid user request ");
      }
    } catch (Exception e) {
      throw new UsernameNotFoundException("invalid user request ");
    }
  }
}
