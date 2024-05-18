package com.auction.controllers;

import com.auction.exception.BusinessException;
import com.auction.models.Bid;
import com.auction.service.BiddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bids")
public class BidController {

  @Autowired
  BiddingService biddingService;

  @PostMapping("/{auctionId}/{amount}")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PARTICIPANT')")
  public ResponseEntity<Bid> placeBid(@PathVariable Long auctionId, @PathVariable Long amount) {
    try {
      var response = biddingService.placeBid(auctionId, amount);
      return ResponseEntity.ok(response);
    } catch (BusinessException e) {
      throw e;
    }
  }
}
