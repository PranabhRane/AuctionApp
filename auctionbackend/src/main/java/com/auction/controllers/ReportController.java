package com.auction.controllers;

import com.auction.service.ReportService;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/report")
public class ReportController {

  @Autowired
  private ReportService reportService;

  @GetMapping("/generate/{date}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<ByteArrayResource> generateDailyReport(@PathVariable LocalDate date) throws IOException {
    try {
      if (!date.isBefore(LocalDate.now())) {
        throw new Exception("Date should be before current data");
      }
      var format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      var formattedDate = date.format(format);
      byte[] reportBytes = reportService.generateDailyReport(date);
      // Set the response headers
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
      headers.setContentDispositionFormData("attachment", "auction_report_" + formattedDate + ".xlsx");

      // Return the file as response
      return new ResponseEntity<>(new ByteArrayResource(reportBytes), headers, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
