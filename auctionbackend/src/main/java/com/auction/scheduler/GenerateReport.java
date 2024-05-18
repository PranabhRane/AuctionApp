package com.auction.scheduler;

import com.auction.service.ReportService;
import java.io.IOException;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GenerateReport {

  @Autowired
  ReportService reportService;

  @Scheduled(cron = "0 0 0 * * ?")
  public void generateReport() {
    try {
      reportService.generateDailyReport(LocalDate.now());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
