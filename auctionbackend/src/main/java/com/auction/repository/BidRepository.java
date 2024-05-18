package com.auction.repository;

import com.auction.models.Bid;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BidRepository extends JpaRepository<Bid, Long> {
  @Query("SELECT MAX(b.amount) FROM Bid b where b.auction.id=:auctionId")
  Long findLastBidByAuctionId(@Param("auctionId") Long auctionId);

  @Query(
    "SELECT b.bidder.id, b.bidder.username, COUNT(b.id) FROM Bid b WHERE b.bidTime BETWEEN :startOfDay AND :endOfDay GROUP BY b.bidder.id, b.bidder.username ORDER BY COUNT(b.id) DESC"
  )
  List<Object[]> findTopParticipantsByBids(
    @Param("startOfDay") LocalDateTime startOfDay,
    @Param("endOfDay") LocalDateTime endOfDay,
    Pageable pageable
  );
}
