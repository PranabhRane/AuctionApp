package com.auction.controllers;

import com.auction.exception.BusinessException;
import com.auction.models.Auction;
import com.auction.response.AuctionResponse;
import com.auction.response.CurrentAuctionResponse;
import com.auction.service.AuctionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auction")
public class AuctionController {

  @Autowired
  AuctionService auctionService;

  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('AUCTIONEER')")
  public ResponseEntity<Auction> addAuctionItem(@RequestBody Auction auction) {
    try {
      var response = auctionService.addAuction(auction);
      return ResponseEntity.ok(response);
    } catch (BusinessException e) {
      throw e;
    }
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PARTICIPANT')")
  public ResponseEntity<CurrentAuctionResponse> getCurrentAuctionItem(@PathVariable Long id) {
    var response = auctionService.mapToCurrentAuctionResponse(auctionService.getAuctionById(id));
    return ResponseEntity.ok(response);
  }

  @GetMapping
  @PreAuthorize("hasAuthority('ADMIN') or  hasAuthority('AUCTIONEER')")
  public ResponseEntity<List<AuctionResponse>> getAllAuctionItems() {
    var response = auctionService.mapToAuctionResponseList(auctionService.findAll());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/active")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PARTICIPANT')")
  public ResponseEntity<List<CurrentAuctionResponse>> getAllCurrentAuctions() {
    var response = auctionService.mapToCurrentAuctionResponseList(auctionService.findAllActive());
    return ResponseEntity.ok(response);
  }
}
