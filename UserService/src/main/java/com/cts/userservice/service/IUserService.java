package com.cts.userservice.service;

import java.util.List;

import com.cts.userservice.dto.*;
import com.cts.userservice.entity.User;


public interface IUserService {

	UserDto addUser(UserDto user);
	List<AdminResponseDto> viewAllUsers();
	UserDto getUserById(Long userId);
	AuthDto getUserByEmail(String email);
	UserDto updateUserById(Long userId, UserDto userDto);
	void deleteUserById(long userId);
	void deleteUserByIdPermenent(long userId);
	void recoverTheAccountByEmail(String email);
	void recoverTheAccountByUserId(Long userId);
	void changePassword(Long userId, PasswordDto userDto);

	List<User> getAllDeletedUsers();
	UserRoleDto updateRoleById(Long userId, UserRoleDto userroleDto);

}
