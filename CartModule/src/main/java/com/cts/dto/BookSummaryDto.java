package com.cts.dto;

import lombok.Data;

@Data
public class BookSummaryDto {
    private Long bookId;
    private String title;
    private Double price;
}
