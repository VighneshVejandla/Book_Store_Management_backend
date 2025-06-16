package com.cts.dto;

import lombok.Data;

@Data
public class BookSummaryDto {
    private Integer bookId;
    private String title;
    private Double price;
}
