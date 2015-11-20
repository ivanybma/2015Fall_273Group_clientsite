package com.spring.Entity;

import java.util.List;


public class RegisterRsp {

	private static final long serialVersionUID = -7788120117798133712L;
	private String endpoint_client_name;
	private Integer return_code;
	public String getEndpoint_client_name() {
		return endpoint_client_name;
	}
	public void setEndpoint_client_name(String endpoint_client_name) {
		this.endpoint_client_name = endpoint_client_name;
	}
	public Integer getReturn_code() {
		return return_code;
	}
	public void setReturn_code(Integer return_code) {
		this.return_code = return_code;
	}


	
}
