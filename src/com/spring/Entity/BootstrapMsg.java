package com.spring.Entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

public class BootstrapMsg {

	private static final long serialVersionUID = -7788610077799873452L;
	private String endpoint_client_name;
	public String getEndpoint_client_name() {
		return endpoint_client_name;
	}
	public void setEndpoint_client_name(String endpoint_client_name) {
		this.endpoint_client_name = endpoint_client_name;
	}
}
