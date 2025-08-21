package com.iportal.service;

import com.iportal.dto.user.ChangePasswordRequest;

public interface UserService{
	boolean changePassword(String username, ChangePasswordRequest request);
	
	void banAccount(String username);
	
//	UserResponse getUser(String username);
//	List<String> getAllUsername();
//	List<UserResponse> getAllUsers();
//	UserResponse updateUserByUser(UserRequest userRequest);
//	UserResponse updateUserByManager(UserManagerRequest userManagerRequest);
//	boolean deleteUserByUsername(String username);
	
	void unbanAccount(String username);

	
	
//	List<WarehouseUserResponse> getWerehouseUser(String username);
//	UserResponse getUserInfomation();
	
}

