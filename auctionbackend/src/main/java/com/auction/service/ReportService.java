package com.auction.service;

import java.io.IOException;
import java.time.LocalDate;

public interface ReportService {
  byte[] generateDailyReport(LocalDate date) throws IOException;
}
