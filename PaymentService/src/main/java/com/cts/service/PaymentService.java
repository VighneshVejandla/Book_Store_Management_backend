package com.cts.service;

import com.cts.dto.InitiatePaymentDTO;
import com.cts.dto.PaymentInfoDTO;
import com.cts.dto.PaymentRequestDto;
import com.cts.dto.PaymentResponseDto;

public interface PaymentService {
	PaymentResponseDto initiatePayment(Long userId, InitiatePaymentDTO initiatePaymentDTO);

	void updatePaymentStatusToSuccess(Long paymentId);

	Double processPayment(Integer userId);

	String viewPaymentStatus(Long paymentId);

	void cancelPayment(Long paymentId);

	PaymentInfoDTO paymentDetails(Long paymentId);


}
