package com.cts.dto;
import jakarta.persistence.Column;
import lombok.Data;
@Data
public class ProfileToPaymentDTO {
    private Long profileId;
    private String bio;
    @Column(length = 15)
    private String phoneNumber;
    private String address;
}
