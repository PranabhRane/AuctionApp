package com.auction.service;

import com.auction.models.User;
import com.auction.repository.AuctionRepository;
import com.auction.repository.BidRepository;
import com.auction.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  BidRepository bidRepository;

  @Autowired
  AuctionRepository auctionRepository;

  @Autowired
  UserRepository userRepository;

  @Override
  public List<User> getTopParticipantsByAuction(LocalDate date) {
    List<Object[]> topParticipants = auctionRepository.findTopParticipantsByAuction(
      date.atStartOfDay(),
      date.atTime(LocalTime.MAX),
      PageRequest.of(0, 10)
    );
    return mapToUsers(topParticipants);
  }

  @Override
  public List<User> getTopParticipantsByBids(LocalDate date) {
    List<Object[]> topParticipants = bidRepository.findTopParticipantsByBids(
      date.atStartOfDay(),
      date.atTime(LocalTime.MAX),
      PageRequest.of(0, 10)
    );
    return mapToUsers(topParticipants);
  }

  @Override
  public Optional<User> getByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  private List<User> mapToUsers(List<Object[]> data) {
    List<User> users = new ArrayList<>();
    for (Object[] row : data) {
      User user = new User();
      user.setId((Long) row[0]);
      user.setUsername((String) row[1]);
      users.add(user);
    }
    return users;
  }

  public String getCurrentUserName() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
      return ((UserDetails) authentication.getPrincipal()).getUsername();
    }
    return null;
  }
}
