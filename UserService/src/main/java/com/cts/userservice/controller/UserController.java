package com.cts.userservice.controller;

import java.util.List;

import com.cts.userservice.dto.*;
import com.cts.userservice.entity.User;
import com.cts.userservice.feignclient.CartFeignClient;
import com.cts.userservice.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.cts.userservice.service.IUserService;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	IUserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CartFeignClient cartFeignClient;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

	@PostMapping("/adduser")
	public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
		return new ResponseEntity<UserDto>(userService.addUser(userDto), HttpStatus.OK);
	}

	@GetMapping("/viewallusers")
	public ResponseEntity<List<UserDto>> viewAllUsers() {
		return new ResponseEntity<List<UserDto>>(userService.viewAllUsers(), HttpStatus.OK);
	}

	@GetMapping("/viewuserbyid/{userId}")
	public ResponseEntity<UserDto> viewUserById(@PathVariable Long userId) {
		return new ResponseEntity<UserDto>(userService.getUserById(userId), HttpStatus.OK);
	}

	@GetMapping("/viewuserbyemail/{email}")
	public ResponseEntity<AuthDto> viewUserByEmail(@PathVariable String email) {
		return new ResponseEntity<AuthDto>(userService.getUserByEmail(email), HttpStatus.OK);
	}

	@PutMapping("/updateuser/{userId}")
	public ResponseEntity<UserDto> updateUserById(@PathVariable Long userId, @RequestBody UserDto user) {
		return new ResponseEntity<UserDto>(userService.updateUserById(userId, user), HttpStatus.OK);
	}

	@PutMapping("/updaterole/{userId}")
	public ResponseEntity<UserRoleDto> updateRoleById(@PathVariable Long userId, @RequestBody UserRoleDto user) {
		return new ResponseEntity<UserRoleDto>(userService.updateRoleById(userId, user), HttpStatus.OK);
	}

	@DeleteMapping("/deleteuser/{userId}")
	public ResponseEntity<String> deleteUserById(@PathVariable Long userId) {
		userService.deleteUserById(userId);
		return new ResponseEntity<String>("User deleted successfully", HttpStatus.OK);
	}

	@DeleteMapping("/deleteusertotally/{userId}")
	public ResponseEntity<String> deleteUserByIdPermenent(@PathVariable Long userId) {
		userService.deleteUserByIdPermenent(userId);
		return new ResponseEntity<String>("User deleted successfully from the database", HttpStatus.OK);
	}

	@PutMapping("/recoveraccountbyemail/{email}")
	public ResponseEntity<String> recoverTheAccountByEmail(@PathVariable String email) {
		userService.recoverTheAccountByEmail(email);
		return new ResponseEntity<String>("Account Recovered Successfully", HttpStatus.OK);
	}

	@PutMapping("/recoveraccountbyuserid/{userId}")
	public ResponseEntity<String> recoverTheAccountByUserId(@PathVariable Long userId) {
		userService.recoverTheAccountByUserId(userId);
		return new ResponseEntity<String>("Account Recovered Successfully", HttpStatus.OK);
	}

	@PutMapping("/changepassword/{userId}")
	public ResponseEntity<String> changePassword(@Valid @PathVariable Long userId, @RequestBody PasswordDto password) {
		userService.changePassword(userId, password);
		return new ResponseEntity<String>("Password Changed Successfully", HttpStatus.OK);
	}

	@GetMapping("/getalldeletedusers")
	public ResponseEntity<List<User>>  getAllDeletedUsers(){
		return new ResponseEntity<List<User>>(userService.getAllDeletedUsers(), HttpStatus.OK);
	}

	@GetMapping("/viewcartitems/{userId}")
	public ResponseEntity<List<CartItemDTO>> viewCartItems(@PathVariable Long userId){
		List<CartItemDTO> cartItems = cartFeignClient.getCartItems(userId);
		return new ResponseEntity<>(cartItems, HttpStatus.OK);
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthDto> authenticate(@RequestBody LoginRequest loginRequest) {
		User user = userRepository.findByEmail(loginRequest.getEmail())
				.orElseThrow(() -> new RuntimeException("Invalid email or password"));

		if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			throw new RuntimeException("Invalid email or password");
		}

		return new ResponseEntity<>(modelMapper.map(user, AuthDto.class), HttpStatus.OK);
	}

}
