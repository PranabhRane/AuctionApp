package com.auction.models;

import com.auction.service.AuctionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auction {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotEmpty(message = "Please enter itemName")
  private String itemName;

  @NotNull(message = "Please enter reservedPrice")
  private double reservedPrice;

  @NotNull(message = "Please enter startTime")
  private LocalDateTime startTime;

  @NotNull(message = "Please enter startTime")
  private LocalDateTime endTime;

  private String status;

  private LocalDateTime creationDate;

  @Transient
  private Long lastBid;

  @ManyToOne(fetch = FetchType.EAGER)
  private User auctioneer;
}
