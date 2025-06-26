package com.cts.service;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.List;
import java.util.stream.Collectors;
import com.cts.dto.*;
import com.cts.exception.*;
import com.cts.feign.CartServiceClient;
import com.cts.feign.UserClient;
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

	@Autowired
	UserClient userClient;

	@Override
	public PaymentInfoDTO paymentDetails(Long PaymentId){
		Payment details = paymentRepository.findById(PaymentId).orElseThrow(()-> new UserIdException("Payment ID " + PaymentId + " not found"));
        return modelMapper.map(details, PaymentInfoDTO.class);
	}

	@Override
	public Double processPayment(Integer userId) {
		//double amount = cartServiceClient.calculateTotalPrice(userId);
		ResponseEntity<Double> demo = cartServiceClient.calculateTotalPrice(userId);
        //return "Processing payment of " + amount.getTotalPrice() + " for user ID: " + userId;
		return demo.getBody();
}

	@Override
	public PaymentResponseDto initiatePayment(Long userId, InitiatePaymentDTO initiatePaymentDTO) {

		Double amount = processPayment(Math.toIntExact(userId));
		if (amount <=0)
			throw new AmountException("No items in cart, aborting payment operations");

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
		paymentRepository.save(payment);

		String upiUri = generateUpiUri(payment);
		logger.info("Payment initiated successfully with ID: " + payment.getPaymentId());
		PaymentDTO saved = modelMapper.map(payment, PaymentDTO.class);

		return new PaymentResponseDto(saved.getPaymentId(), upiUri, saved.getCreatedAt(), saved.getStatus(),
				saved.getUpiId(), saved.getUserId(), saved.getAmount());
	}

	@Override
	public void updatePaymentStatusToSuccess(Long paymentId) {
		// Retrieve payment from the database
		Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);

		if (optionalPayment.isEmpty())
			throw new PaymentException("Payment ID " + paymentId + " not found in database!");

		Payment payment = optionalPayment.get();
		Long userIdentity = payment.getUserId();
		Duration timeElapsed = Duration.between(payment.getCreatedAt(), LocalDateTime.now());
		long secondsElapsed = timeElapsed.toSeconds();

		// Update status only if it's currently "PENDING"
		if (!"PENDING".equals(payment.getStatus())) {
			throw new StatusChangeException("Payment status must be Pending to update, found: " + payment.getStatus());
		}

		if (secondsElapsed > 60){
			logger.warning("Timeout detected: " + secondsElapsed + " seconds");
			paymentRepository.delete(payment);
			throw new TransactionTimeException("Transaction timeout: time exceeded beyond 60 seconds");

		}
		payment.setStatus("SUCCESS");
		payment.setUpdatedAt(LocalDateTime.now());
		ResponseEntity<String> cartDelete = cartServiceClient.clearCart(Math.toIntExact(userIdentity));
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

	@Override
	public List<PaymentInfoDTO> getAllPaymentDetails(Long userId) {
		UserToPaymentDTO userRoleCheck = userClient.viewUserById(userId).getBody();
		System.out.println(userRoleCheck.getRole());
		if (userRoleCheck.getRole().equals("admin")){
			List<Payment> paymentInfoDTOS = paymentRepository.findAll();
			return paymentInfoDTOS.stream().map((payment) -> modelMapper.map(payment, PaymentInfoDTO.class)).collect(Collectors.toList());
		}
		return null;
	}


}
