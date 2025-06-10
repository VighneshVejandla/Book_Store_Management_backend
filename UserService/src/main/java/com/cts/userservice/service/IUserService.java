package com.cts.userservice.service;

import java.util.List;

import com.cts.userservice.dto.PasswordDto;
import com.cts.userservice.dto.UserDto;
import com.cts.userservice.entity.User;


public interface IUserService {

	UserDto addUser(UserDto user);
	List<UserDto> viewAllUsers();
	UserDto getUserById(Long userId);
	UserDto updateUserById(Long userId, UserDto userDto);
	void deleteUserById(long userId);
	void deleteUserByIdPermenent(long userId);
	void recoverTheAccountByEmail(String email);
	void recoverTheAccountByUserId(Long userId);
	void changePassword(Long userId, PasswordDto userDto);

	List<User> getAllDeletedUsers();

}
