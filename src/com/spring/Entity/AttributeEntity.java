package com.spring.Entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AttributeEntity {

	private static final long serialVersionUID = -7900001117778133712L;
	private String attpath;
	private String pmax;
	private String lstupd;
	
	public AttributeEntity(String attpath, String pmax, String lstupd){
		
		this.pmax=pmax;
		this.attpath=attpath;
		this.lstupd=lstupd;
	}
	
	public AttributeEntity(){
	}
	
	
	public int timediff(String curtime, String lstrpttime)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date date1=new Date();
		try {
			date1 = format.parse(lstrpttime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date date2 = new Date();
		try {
			date2 = format.parse(curtime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long difference = date2.getTime() - date1.getTime();
		return (int)difference/1000;
	}


	public String getAttpath() {
		return attpath;
	}


	public void setAttpath(String attpath) {
		this.attpath = attpath;
	}


	public String getPmax() {
		return pmax;
	}

	public void setPmax(String pmax) {
		this.pmax = pmax;
	}

	public String getLstupd() {
		return lstupd;
	}

	public void setLstupd(String lstupd) {
		this.lstupd = lstupd;
	}




}
