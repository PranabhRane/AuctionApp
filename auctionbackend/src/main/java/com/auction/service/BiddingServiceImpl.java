package com.auction.service;

import com.auction.exception.BusinessException;
import com.auction.models.Bid;
import com.auction.repository.BidRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BiddingServiceImpl implements BiddingService {

  @Autowired
  AuctionService itemService;

  @Autowired
  BidRepository bidRepository;

  @Override
  public Bid placeBid(Long auctionId, Long amount) throws BusinessException {
    var auction = itemService.getAuctionById(auctionId);
    if (auction.getEndTime().isBefore(LocalDateTime.now())) {
      throw new BusinessException("Bidding for this auction has ended");
    }
    Long latestBid = bidRepository.findLastBidByAuctionId(auctionId);
    if (latestBid != null && latestBid >= amount) {
      throw new BusinessException("Your Bid Amount should be higher then current highest bid");
    }
    var bid = new Bid();
    bid.setAmount(amount);
    bid.setBidTime(LocalDateTime.now());
    bid.setAuction(auction);
    return bidRepository.save(bid);
  }

  @Override
  public Long findLastBidByAuctionId(Long id) {
    return bidRepository.findLastBidByAuctionId(id);
  }
}
