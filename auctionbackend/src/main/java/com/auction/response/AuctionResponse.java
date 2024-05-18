package com.auction.response;

import com.auction.service.AuctionStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AuctionResponse {

  private Long id;
  private String itemName;
  private double reservedPrice;
  private String startTime;
  private String endTime;
  private String status;
  private Long lastBid;
}
