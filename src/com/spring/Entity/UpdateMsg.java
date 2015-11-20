package com.spring.Entity;

import java.util.List;


public class UpdateMsg {

	private static final long serialVersionUID = -7788110101748133712L;
	private String endpoint_client_name;
	public String getEndpoint_client_name() {
		return endpoint_client_name;
	}
	public void setEndpoint_client_name(String endpoint_client_name) {
		this.endpoint_client_name = endpoint_client_name;
	}
	public String getFull_device_src() {
		return full_device_src;
	}
	public void setFull_device_src(String full_device_src) {
		this.full_device_src = full_device_src;
	}
	private String full_device_src;

	
}
