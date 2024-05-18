package com.auction.response;

import lombok.Data;

@Data
public class CurrentAuctionResponse {

  private Long id;
  private String itemName;

  private Long lastBid;
}
