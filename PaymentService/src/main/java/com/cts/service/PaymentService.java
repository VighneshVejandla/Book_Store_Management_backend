package com.cts.service;

import com.cts.dto.PaymentRequestDto;
import com.cts.dto.PaymentResponseDto;

public interface PaymentService {
	PaymentResponseDto initiatePayment(PaymentRequestDto requestDto);

	void updatePaymentStatusToSuccess(Long paymentId);

	String viewPaymentStatus(Long paymentId);

	void cancelPayment(Long paymentId);
}
