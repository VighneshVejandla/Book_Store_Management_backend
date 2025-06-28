package com.cts.controller;

import java.util.List;
import java.util.logging.Logger;

import com.cts.dto.InitiatePaymentDTO;
import com.cts.dto.PaymentInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.dto.PaymentRequestDto;
import com.cts.dto.PaymentResponseDto;
import com.cts.service.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

	private static final Logger logger = Logger.getLogger(PaymentController.class.getName());

	@Autowired
	private PaymentService paymentService;

	@PostMapping("/initiate/{userId}")
	public PaymentResponseDto initiatePayment(@PathVariable Long userId, @RequestBody InitiatePaymentDTO initiatePaymentDTO) {
		logger.info("Received request to initiate payment");
		return paymentService.initiatePayment(userId, initiatePaymentDTO);
	}

	@PutMapping("/updatestatus/{paymentId}")
	public String updatePaymentStatusToSuccess(@PathVariable Long paymentId) {
		logger.info("Received request to update payment status for ID: " + paymentId);
		paymentService.updatePaymentStatusToSuccess(paymentId);
		return "SUCCESS";
	}

	@GetMapping("/paymentdetails/{paymentId}")
	public ResponseEntity<PaymentInfoDTO> getPaymentDetails(@PathVariable Long paymentId){
		logger.info("Received request for Payment Details for Payment ID: " + paymentId);
		return ResponseEntity.ok(paymentService.paymentDetails(paymentId));
	}

	@GetMapping("/amount/{userId}")
	public ResponseEntity<Double> viewAmount(@PathVariable Integer userId){
		return ResponseEntity.ok(paymentService.processPayment(userId));
	}

	@GetMapping("/viewstatus/{paymentId}")
	public String viewPaymentStatus(@PathVariable Long paymentId) {
		logger.info("Received request to view payment status for ID: " + paymentId);
		return paymentService.viewPaymentStatus(paymentId);
	}

	@PutMapping("/payments/{paymentId}/cancel")
	public ResponseEntity<String> cancelPayment(@PathVariable Long paymentId) {
		paymentService.cancelPayment(paymentId);
		return ResponseEntity.ok("Payment cancelled successfully.");
	}

	@GetMapping("/getallpaymentdetails/{userId}")
	public List<PaymentInfoDTO> getAll(@PathVariable Long userId){

		return paymentService.getAllPaymentDetails(userId);
	}

}
