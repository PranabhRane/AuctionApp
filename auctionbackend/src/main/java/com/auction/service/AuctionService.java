package com.auction.service;

import com.auction.exception.BusinessException;
import com.auction.models.Auction;
import com.auction.response.AuctionResponse;
import com.auction.response.CurrentAuctionResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AuctionService {
  Auction addAuction(Auction auction) throws BusinessException;

  List<Auction> findAll();

  Auction getAuctionById(Long id);

  Map<String, Long> getAuctionsCountByStatus(LocalDate date);

  long getAuctionCountByCreationDate(LocalDate date);

  List<Auction> getAllAuctions();

  void updateAuctionStatusById(Long auctionId, String status);

  List<Auction> findAllActive();

  AuctionResponse mapToAuctionResponse(Auction auctionById);

  List<AuctionResponse> mapToAuctionResponseList(List<Auction> auctions);

  List<CurrentAuctionResponse> mapToCurrentAuctionResponseList(List<Auction> auctions);

  CurrentAuctionResponse mapToCurrentAuctionResponse(Auction auction);
}
