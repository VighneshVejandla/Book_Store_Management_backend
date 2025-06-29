package com.cts.userservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cts.userservice.dto.AuthDto;
import com.cts.userservice.dto.UserRoleDto;
import com.cts.userservice.exception.InvalidRoleException;
import com.cts.userservice.exception.UserNotFoundByEmailException;
import com.cts.userservice.exception.UserNotFoundByIdException;
import com.cts.userservice.feignclient.CartFeignClient;
import com.cts.userservice.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.userservice.dto.PasswordDto;
import com.cts.userservice.dto.UserDto;
import com.cts.userservice.entity.User;
import com.cts.userservice.exception.EmailAlreadyExistsException;
import com.cts.userservice.repository.UserRepository;

@Service
public class UserServiceImplement implements IUserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ProfileRepository profileRepository;

	@Autowired
	@Qualifier("userServiceModelMapper")
	ModelMapper modelMapper;

	@Autowired
	CartFeignClient cartFeignClient;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public UserDto addUser(UserDto userDto) {

	 	userRepository.findByEmail(userDto.getEmail())
				.ifPresent((user)-> {throw new EmailAlreadyExistsException("Email Already Exists");});

		User newUser = modelMapper.map(userDto, User.class);
		String rawPassword = userDto.getPassword();
		String passwordRegex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~])(?=.*[^\\s]).{8,}$";
		if (!rawPassword.matches(passwordRegex)) {
			throw new IllegalArgumentException("Password must have at least 8 characters, including an alphabet, a number, and a special character, with no whitespace.");
		}

		String encodedPassword = passwordEncoder.encode(rawPassword);
		newUser.setPassword(encodedPassword);

		newUser.setRole("user");
		newUser.setCreatedDate(LocalDateTime.now());
		newUser.setUpdatedDate(LocalDateTime.now());
		newUser.setDeleted(false);
		User saveUser = userRepository.save(newUser);

		try{
			cartFeignClient.createCart(newUser.getUserId());
		}catch (Exception e){
			throw new IllegalArgumentException("failed to create Cart: " + e.getMessage());
		}

		return modelMapper.map(saveUser, UserDto.class);
	}

//---this gives the users only who has the isDelete as the false---

	@Override
	public List<UserDto> viewAllUsers() {

		List<User> users = userRepository.findAll();
		List<UserDto> userDtos = new ArrayList<>();

		for (User user : users) {
			if (!user.isDeleted()) {
				userDtos.add(modelMapper.map(user, UserDto.class));
			}
		}

		return userDtos;
	}

	@Override
	public UserDto getUserById(Long userId) {
		User optionalUser = userRepository.findById(userId)
				.filter(u -> !u.isDeleted()) // Filter out deleted users
				.orElseThrow(() -> new UserNotFoundByIdException("User", "id", userId));
		return modelMapper.map(optionalUser, UserDto.class);
	}

	@Override
	public AuthDto getUserByEmail(String email) {
		User optionalUser = userRepository.findByEmail(email)
				.filter(u -> !u.isDeleted()) // Filter out deleted users
				.orElseThrow(() -> new UserNotFoundByEmailException("User", "id", email));
		return modelMapper.map(optionalUser, AuthDto.class);
	}

	@Override
	public UserDto updateUserById(Long userId, UserDto userDto) {

		User updateUser = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundByIdException("User", "Id", userId));

		updateUser.setName(userDto.getName());
		updateUser.setEmail(userDto.getEmail());

//		String password = userDto.getPassword();
//		updateUser.setPassword(password);

		String encodedPassword = passwordEncoder.encode(userDto.getPassword());
		updateUser.setPassword(encodedPassword);

		updateUser.setUpdatedDate(LocalDateTime.now());

		User saveUser = userRepository.save(updateUser);

		return modelMapper.map(saveUser, UserDto.class);
	}

	@Override
	public void deleteUserById(long userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundByIdException("User", "Id", userId));

		user.setDeleted(true);
		userRepository.save(user);

		Optional.ofNullable(profileRepository.findByUserUserId(userId))
				.ifPresent(profile -> {
					profile.setDeleted(true);
					profileRepository.save(profile);
				});

		try{
			cartFeignClient.clearCart(userId);
		}catch (Exception e){
			throw new RuntimeException("Cart Deletion failed: " + e.getMessage());
		}
	}

	@Override
	public void deleteUserByIdPermenent(long userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundByIdException("User", "Id", userId));
		userRepository.delete(user);

	}

	@Override
	public void recoverTheAccountByEmail(String email) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundByEmailException("User", "Email", email));

		user.setDeleted(false);
		userRepository.save(user);
		Optional.ofNullable(profileRepository.findByUserUserId(user.getUserId()))
				.ifPresent(profile -> {
					profile.setDeleted(false);
					profileRepository.save(profile);
				});
	}

	@Override
	public void recoverTheAccountByUserId(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundByIdException("User", "UserId", userId));

		user.setDeleted(false);
		userRepository.save(user);
		Optional.ofNullable(profileRepository.findByUserUserId(user.getUserId()))
				.ifPresent(profile -> {
					profile.setDeleted(false);
					profileRepository.save(profile);
				});
	}

	@Override
	@Transactional
	public void changePassword(Long userId, PasswordDto passwordDto) {
		System.out.println("The password fetched:"+passwordDto.getPassword());
		User updateUser = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundByIdException("User", "Id", userId));

		String password = passwordDto.getPassword();
		updateUser.setPassword(password);
		updateUser.setUpdatedDate(LocalDateTime.now());

		User saveUser = userRepository.save(updateUser);
		
		modelMapper.map(saveUser, PasswordDto.class);
	}

	public List<User> getAllDeletedUsers() {
		return userRepository.findByIsDeletedTrue();
	}


	@Override
	public UserRoleDto updateRoleById(Long userId, UserRoleDto userRoleDto) {

		User updateUser = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundByIdException("User", "Id", userId));

		// Manual validation for role
		String newRole = userRoleDto.getRole();
		if (!"user".equalsIgnoreCase(newRole) && !"admin".equalsIgnoreCase(newRole)) {
			throw new InvalidRoleException("Invalid role provided. Role must be 'user' or 'admin'.");
		}

		updateUser.setRole(newRole); // Assuming setRole takes String
		updateUser.setUpdatedDate(LocalDateTime.now());

		User saveUser = userRepository.save(updateUser);

		return modelMapper.map(saveUser, UserRoleDto.class);
	}

}



