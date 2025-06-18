package com.cts.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import com.cts.dto.*;
import com.cts.exception.*;
import com.cts.feign.CartServiceClient;
import com.netflix.discovery.converters.Auto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cts.entity.Payment;
import com.cts.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {

	private static final Logger logger = Logger.getLogger(PaymentServiceImpl.class.getName());

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	CartServiceClient cartServiceClient;

	@Override
	public PaymentInfoDTO paymentDetails(Long PaymentId){
		Payment details = paymentRepository.findById(PaymentId).orElseThrow(()-> new UserIdException("Payment ID " + PaymentId + " not found"));
		PaymentInfoDTO paymentInfoDTO = modelMapper.map(details, PaymentInfoDTO.class);
		paymentInfoDTO.setStatus(details.getStatus());
		paymentInfoDTO.setAmount(details.getAmount());
		paymentInfoDTO.setPaymentId(details.getPaymentId());
		paymentInfoDTO.setCreatedAt(details.getCreatedAt());
		paymentInfoDTO.setUserId(details.getUserId());
		return paymentInfoDTO;
	}

	@Override
	public Double processPayment(Integer userId) {
		//double amount = cartServiceClient.calculateTotalPrice(userId);
		ResponseEntity<CartToPaymentDTO> demo = cartServiceClient.calculateTotalPrice(userId);
		CartToPaymentDTO amount = demo.getBody();
        //return "Processing payment of " + amount.getTotalPrice() + " for user ID: " + userId;
		return amount.getTotalPrice();
}

	@Override
	public PaymentResponseDto initiatePayment(Long userId, InitiatePaymentDTO initiatePaymentDTO) {

		if (paymentRepository.existsByUserIdAndStatus(Math.toIntExact(userId), "PENDING")){
			throw new UserIdException("User ID " + userId + " already initiated a pending payment");
		}
		Payment payment = new Payment();
		payment.setPaymentId(generateUniquePaymentId());
		payment.setUpiId(initiatePaymentDTO.getUpiId());
		payment.setUserId(userId);
		payment.setAmount(processPayment(Math.toIntExact(userId)));
		payment.setCreatedAt(LocalDateTime.now());
		payment.setUpdatedAt(LocalDateTime.now());
		payment.setStatus("PENDING");
		payment = paymentRepository.save(payment);

		String upiUri = generateUpiUri(payment);
		logger.info("Payment initiated successfully with ID: " + payment.getPaymentId());

		return new PaymentResponseDto(payment.getPaymentId(), upiUri, payment.getCreatedAt(), payment.getStatus(),
				payment.getUpiId(), payment.getUserId(), payment.getAmount());
	}

	@Override
	public void updatePaymentStatusToSuccess(Long paymentId) {
		// Retrieve payment from the database
		Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);

		if (optionalPayment.isEmpty()) {
			throw new PaymentException("Payment ID " + paymentId + " not found in database!");
		}

		Payment payment = optionalPayment.get();
		Duration timeElapsed = Duration.between(payment.getCreatedAt(), LocalDateTime.now());
		long secondsElapsed = timeElapsed.toSeconds();

		if (secondsElapsed > 30){
			logger.warning("Timeout detected: " + secondsElapsed + " seconds");
			paymentRepository.delete(payment);
			throw new TransactionTimeException("Transaction timeout: time exceeded beyond 30 seconds");

		}

		// Update status only if it's currently "PENDING"
		if (!"PENDING".equals(payment.getStatus())) {
			throw new StatusChangeException("Payment status must be Pending to update, found: " + payment.getStatus());
		}

		payment.setStatus("SUCCESS");
		payment.setUpdatedAt(LocalDateTime.now());
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

	private String generateUpiUri(Payment dto) {
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
