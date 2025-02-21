package com.ra.service.report;


import com.ra.model.dto.response.*;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    List<BestSellerProductDto> getBestSellerProducts(LocalDate from, LocalDate to);
    List<MostLikedProductDto> getMostLikedProducts(LocalDate from, LocalDate to);
    List<RevenueByCategoryDto> getRevenueByCategory(LocalDate from, LocalDate to);
    List<TopSpendingResponseDto> getTopSpending(LocalDate from, LocalDate to);
    List<UserResponseDto> getNewAccountsThisMonth();
    Long countInvoicesOverTime(LocalDate from, LocalDate to);


}
