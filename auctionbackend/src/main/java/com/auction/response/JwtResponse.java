package com.auction.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtResponse {

  private String token;
  private String type = "Bearer";
  private String username;
  private List<String> roles;
}
