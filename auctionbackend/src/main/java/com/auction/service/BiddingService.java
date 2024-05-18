package com.auction.service;

import com.auction.exception.BusinessException;
import com.auction.models.Bid;

public interface BiddingService {
  Bid placeBid(Long auctionId, Long amount) throws BusinessException;

  Long findLastBidByAuctionId(Long id);
}
