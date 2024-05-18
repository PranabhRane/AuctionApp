package com.auction.service;

import static com.auction.service.AuctionStatus.CREATED;

import com.auction.exception.BusinessException;
import com.auction.models.Auction;
import com.auction.repository.AuctionRepository;
import com.auction.repository.BidRepository;
import com.auction.repository.UserRepository;
import com.auction.response.AuctionResponse;
import com.auction.response.CurrentAuctionResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuctionServiceImpl implements AuctionService {

  @Autowired
  AuctionRepository auctionRepository;

  @Autowired
  BidRepository bidRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserService userService;

  @Override
  public Auction addAuction(Auction auction) throws BusinessException {
    if (auction.getStartTime().isBefore(LocalDateTime.now())) {
      throw new BusinessException("Start date should not be past time");
    }
    if (auction.getEndTime().isBefore(auction.getStartTime())) {
      throw new BusinessException("End date is before start date");
    }
    if (auction.getEndTime().minusMinutes(5).isBefore(auction.getStartTime())) {
      throw new BusinessException("Minimum duration of hours should be 5 minutes");
    }
    auction.setStatus(CREATED.toString());
    auction.setCreationDate(LocalDateTime.now());
    auction.setAuctioneer(userRepository.findByUsername(userService.getCurrentUserName()).orElse(null));
    return auctionRepository.save(auction);
  }

  @Override
  public List<Auction> findAll() {
    return transformToAutionList(auctionRepository.findAllAuctionAndLastBid());
  }

  @Override
  public Auction getAuctionById(Long id) {
    var auction = auctionRepository.findById(id).orElseThrow();
    var lastBid = bidRepository.findLastBidByAuctionId(id);
    if (lastBid != null) {
      auction.setLastBid(lastBid);
    }
    return auction;
  }

  public Map<String, Long> getAuctionsCountByStatus(LocalDate date) {
    // Fetch number of auctions with different statuses on the given date
    Map<String, Long> auctionsByStatus = new HashMap<>();
    auctionsByStatus.put(
      AuctionStatus.SUCCESS.toString(),
      auctionRepository.countByStatusAndStartTimeAfterAndEndTimeBefore(
        AuctionStatus.SUCCESS.toString(),
        date.atTime(LocalTime.MIN),
        date.atTime(LocalTime.MAX)
      )
    );
    auctionsByStatus.put(
      AuctionStatus.FAILURE.toString(),
      auctionRepository.countByStatusAndStartTimeAfterAndEndTimeBefore(
        AuctionStatus.FAILURE.toString(),
        date.atTime(LocalTime.MIN),
        date.atTime(LocalTime.MAX)
      )
    );
    return auctionsByStatus;
  }

  @Override
  public long getAuctionCountByCreationDate(LocalDate date) {
    return auctionRepository.getAuctionCountByCreationDate(date);
  }

  @Override
  public List<Auction> getAllAuctions() {
    return auctionRepository.findAll();
  }

  @Override
  public void updateAuctionStatusById(Long auctionId, String status) {
    auctionRepository.updateAuctionStatusById(auctionId, status);
  }

  @Override
  public List<Auction> findAllActive() {
    return transformToAutionList(auctionRepository.findAllAuctionActive(LocalDateTime.now()));
  }

  public List<Auction> transformToAutionList(List<Object[]> objects) {
    return objects
      .stream()
      .map(row -> {
        Auction auction = (Auction) row[0];
        Long bidAmount = (Long) row[1];
        auction.setLastBid(bidAmount);
        return auction;
      })
      .toList();
  }

  public AuctionResponse mapToAuctionResponse(Auction auction) {
    var auctionResponse = new AuctionResponse();
    auctionResponse.setId(auction.getId());
    auctionResponse.setItemName(auction.getItemName());
    auctionResponse.setLastBid(auction.getLastBid());
    auctionResponse.setReservedPrice(auction.getReservedPrice());
    auctionResponse.setStatus(auction.getStatus());
    auctionResponse.setStartTime(convertDateToString(auction.getStartTime()));
    auctionResponse.setEndTime(convertDateToString(auction.getEndTime()));
    return auctionResponse;
  }

  public List<AuctionResponse> mapToAuctionResponseList(List<Auction> auctions) {
    return auctions.stream().map(this::mapToAuctionResponse).toList();
  }

  @Override
  public List<CurrentAuctionResponse> mapToCurrentAuctionResponseList(List<Auction> auctions) {
    return auctions.stream().map(this::mapToCurrentAuctionResponse).toList();
  }

  public CurrentAuctionResponse mapToCurrentAuctionResponse(Auction auction) {
    var auctionResponse = new CurrentAuctionResponse();
    auctionResponse.setId(auction.getId());
    auctionResponse.setItemName(auction.getItemName());
    auctionResponse.setLastBid(auction.getLastBid());
    return auctionResponse;
  }

  private String convertDateToString(LocalDateTime dateTime) {
    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return dateTime.format(formatter);
  }
}
