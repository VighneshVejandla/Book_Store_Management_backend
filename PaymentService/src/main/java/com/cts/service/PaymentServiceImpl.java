package com.cts.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.dto.PaymentRequestDto;
import com.cts.dto.PaymentResponseDto;
import com.cts.entity.Payment;
import com.cts.exception.AmountException;
import com.cts.exception.PaymentException;
import com.cts.exception.StatusChangeException;
import com.cts.exception.UserIdException;
import com.cts.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {

	private static final Logger logger = Logger.getLogger(PaymentServiceImpl.class.getName());

	@Autowired
	private PaymentRepository paymentRepository;

	@Override
	public PaymentResponseDto initiatePayment(PaymentRequestDto requestDto) {
		if (requestDto.getUserId() < 1 || requestDto.getUserId() == null)
			throw new UserIdException("User ID is Invalid, Should be a positive integer");

		if (requestDto.getAmount() <= 0)
			throw new AmountException("Amount should be greater than 0");
		Payment payment = new Payment();
		payment.setPaymentId(generateUniquePaymentId());
		payment.setUpiId(requestDto.getUpiId());
		payment.setUserId(requestDto.getUserId());
		payment.setAmount(requestDto.getAmount());
		payment.setCreatedAt(LocalDateTime.now());
		payment.setOrderId(requestDto.getOrderId());
		payment.setStatus("PENDING");

		payment = paymentRepository.save(payment);

		String upiUri = generateUpiUri(requestDto);
		logger.info("Payment initiated successfully with ID: " + payment.getPaymentId());

		return new PaymentResponseDto(payment.getPaymentId(), upiUri, payment.getCreatedAt(), payment.getStatus(),
				payment.getUpiId(), payment.getUserId(), payment.getOrderId(), payment.getAmount());
	}

	@Override
	public void updatePaymentStatusToSuccess(Long paymentId) {
		// Retrieve payment from the database
		Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);

		if (optionalPayment.isEmpty()) {
			throw new PaymentException("Payment ID " + paymentId + " not found in database!");
		}

		Payment payment = optionalPayment.get();

		// Update status only if it's currently "PENDING"
		if (!"PENDING".equals(payment.getStatus())) {
			throw new StatusChangeException("Payment status must be Pending to update, found: " + payment.getStatus());
		}

		payment.setStatus("SUCCESS");
		paymentRepository.save(payment); // Persist the updated payment status

		logger.info("Payment status updated to SUCCESS for ID: " + paymentId);
	}

	@Override
	public String viewPaymentStatus(Long paymentId) {
		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new PaymentException("Payment not found for ID: " + paymentId));

		logger.info("Viewed payment status for ID: " + paymentId + ", Status: " + payment.getStatus());
		return "Payment ID: " + paymentId + ", Status: " + payment.getStatus();
	}

	private Long generateUniquePaymentId() {
		Long newId;

		do {
			newId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
		} while (paymentRepository.existsByPaymentId(newId));

		return newId;
	}

	private String generateUpiUri(PaymentRequestDto dto) {
		return "upi://pay?pa=" + URLEncoder.encode(dto.getUpiId(), StandardCharsets.UTF_8) + "&pn="
				+ URLEncoder.encode(String.valueOf(dto.getUserId()), StandardCharsets.UTF_8) + "&am=" + dto.getAmount()
				+ "&cu=INR";
	}

	@Override
	public void cancelPayment(Long paymentId) {
		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new PaymentException("Payment not found"));
		if (!"PENDING".equals(payment.getStatus())) {
			throw new PaymentException("Only pending payments can be cancelled");
		}
		payment.setStatus("CANCELLED");
		paymentRepository.save(payment);
	}

}
