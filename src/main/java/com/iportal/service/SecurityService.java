package com.iportal.service;

public interface SecurityService {
	String getCurrentUsername();
	
	boolean hasRole(String role);
//	boolean hasAccessToWarehouse(List<Long> allowedWarehouses, Long warehouseId);
}
