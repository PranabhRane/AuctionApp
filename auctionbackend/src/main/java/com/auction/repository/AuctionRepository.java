package com.auction.repository;

import com.auction.models.Auction;
import com.auction.service.AuctionStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
  @Query(
    "SELECT a.auctioneer.id, a.auctioneer.username, COUNT(a.id) FROM Auction a WHERE a.creationDate BETWEEN :startOfDay AND :endOfDay GROUP BY a.auctioneer.id, a.auctioneer.username ORDER BY COUNT(a.id) DESC"
  )
  List<Object[]> findTopParticipantsByAuction(
    @Param("startOfDay") LocalDateTime startOfDay,
    @Param("endOfDay") LocalDateTime endOfDay,
    Pageable pageable
  );

  long countByStatusAndStartTimeAfterAndEndTimeBefore(
    @Param("status") String status,
    @Param("startTime") LocalDateTime startTime,
    @Param("endTime") LocalDateTime endTime
  );

  @Query("SELECT a,(SELECT MAX(b.amount) FROM Bid b WHERE b.auction=a) as lastBid FROM Auction a")
  List<Object[]> findAllAuctionAndLastBid();

  @Query("SELECT count(a) FROM Auction a WHERE DATE(a.creationDate)=DATE(:creationDate)")
  long getAuctionCountByCreationDate(@Param("creationDate") LocalDate creationDate);

  @Modifying
  @Transactional
  @Query("UPDATE  Auction a  SET a.status=:status WHERE a.id=:auctionId")
  void updateAuctionStatusById(@Param("auctionId") Long auctionId, @Param("status") String status);

  @Query(
    "SELECT a,(SELECT MAX(b.amount) FROM Bid b WHERE b.auction=a) as lastBid  FROM Auction a WHERE a.startTime <= :currentDate and a.endTime >= :currentDate"
  )
  List<Object[]> findAllAuctionActive(@Param("currentDate") LocalDateTime currentDate);
}
