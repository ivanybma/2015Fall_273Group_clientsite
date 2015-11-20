package com.spring.Entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

public class BootstrapRsp {

	private static final long serialVersionUID = -7718619177799873712L;

	private String endpoint_client_name;
	private List<String> register_server_uri;
	private String model; 
	private String manufacturer;
	private List<String> optionalsrc;
	private String bootstrap_time_stamp;
	
    
	public String getEndpoint_client_name() {
		return endpoint_client_name;
	}
	public void setEndpoint_client_name(String endpoint_client_name) {
		this.endpoint_client_name = endpoint_client_name;
	}

	
	
	public List<String> getRegister_server_uri() {
		return register_server_uri;
	}
	public void setRegister_server_uri(List<String> register_server_uri) {
		this.register_server_uri = register_server_uri;
	}
	public List<String> getOptionalsrc() {
		return optionalsrc;
	}
	public void setOptionalsrc(List<String> optionalsrc) {
		this.optionalsrc = optionalsrc;
	}
	public String getBootstrap_time_stamp() {
		return bootstrap_time_stamp;
	}
	public void setBootstrap_time_stamp(String bootstrap_time_stamp) {
		this.bootstrap_time_stamp = bootstrap_time_stamp;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}









	
	
	
}
