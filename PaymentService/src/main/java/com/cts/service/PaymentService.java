package com.cts.service;

import com.cts.dto.*;

import java.util.List;

public interface PaymentService {
	PaymentResponseDto initiatePayment(Long userId, InitiatePaymentDTO initiatePaymentDTO);

	void updatePaymentStatusToSuccess(Long paymentId);

	Double processPayment(Integer userId);

	String viewPaymentStatus(Long paymentId);

	void cancelPayment(Long paymentId);

	PaymentInfoDTO paymentDetails(Long paymentId);

	List<PaymentInfoDTO> getAllPaymentDetails(Long userId);

	ProfileToPaymentDTO getProfile(Long userId);




}
