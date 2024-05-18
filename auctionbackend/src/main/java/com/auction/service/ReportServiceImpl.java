package com.auction.service;

import com.auction.models.User;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

  @Autowired
  AuctionService itemService;

  @Autowired
  UserService userService;

  @Override
  @Cacheable("generateDailyReport")
  public byte[] generateDailyReport(LocalDate date) throws IOException {
    try (Workbook workbook = new XSSFWorkbook()) {
      Sheet sheet = workbook.createSheet("Daily Report");

      // Create headers
      Row headerRow = sheet.createRow(0);
      headerRow.createCell(0).setCellValue("Auction Items added today");
      headerRow.createCell(1).setCellValue("Auctions Completed and status");
      headerRow.createCell(2).setCellValue("Top 10 Participants");
      headerRow.createCell(3).setCellValue("Top 10 Auctioneers");

      // Fetch data
      long auctionCount = itemService.getAuctionCountByCreationDate(date);
      Map<String, Long> auctionsByStatus = itemService.getAuctionsCountByStatus(date);
      List<User> topParticipants = userService.getTopParticipantsByBids(date);
      List<User> topAuctioneers = userService.getTopParticipantsByAuction(date);
      // Populate data
      int rowCount = 1;
      Row dataRow = sheet.createRow(1);
      dataRow.createCell(0).setCellValue(auctionCount);
      dataRow.createCell(1).setCellValue(auctionsByStatus.toString());
      for (int i = 0; i < Math.min(10, topParticipants.size()); i++) {
        Row dataRowAuctioneer = sheet.createRow(rowCount++);
        dataRowAuctioneer.createCell(2).setCellValue(topParticipants.get(i).getUsername());
      }
      rowCount = 1;
      for (int i = 0; i < Math.min(10, topAuctioneers.size()); i++) {
        Row dataRowAutioneer = sheet.createRow(rowCount++);
        dataRowAutioneer.createCell(3).setCellValue(topAuctioneers.get(i).getUsername());
      }

      // Write to byte array
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      workbook.write(outputStream);
      return outputStream.toByteArray();
    }
  }
}
