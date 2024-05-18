package com.auction.service;

import com.auction.models.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {
  public List<User> getTopParticipantsByAuction(LocalDate date);

  public List<User> getTopParticipantsByBids(LocalDate date);

  Optional<User> getByUsername(String username);

  String getCurrentUserName();
}
