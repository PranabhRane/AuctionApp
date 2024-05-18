package com.auction.scheduler;

import com.auction.models.Auction;
import com.auction.service.AuctionService;
import com.auction.service.AuctionStatus;
import com.auction.service.BiddingService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AuctionScheduler {

  @Autowired
  AuctionService itemService;

  @Autowired
  BiddingService biddingService;

  @Scheduled(fixedRate = 60000)
  public void checkAuctionStatus() {
    List<Auction> auctions = itemService.getAllAuctions();
    var currentTime = LocalDateTime.now();
    for (Auction auction : auctions) {
      if (
        currentTime.isAfter(auction.getEndTime()) &&
        (
          !Arrays
            .asList(AuctionStatus.SUCCESS.toString(), AuctionStatus.FAILURE.toString())
            .contains(auction.getStatus())
        )
      ) {
        Long latestBid = biddingService.findLastBidByAuctionId(auction.getId());
        if (latestBid != null && latestBid >= auction.getReservedPrice()) {
          itemService.updateAuctionStatusById(auction.getId(), AuctionStatus.SUCCESS.toString());
        } else {
          itemService.updateAuctionStatusById(auction.getId(), AuctionStatus.FAILURE.toString());
        }
      }
    }
  }
}
