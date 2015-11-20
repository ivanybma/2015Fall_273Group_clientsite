package com.spring.Entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReportEntity {

	private static final long serialVersionUID = -7700123117778133712L;
	private String rscpath;
	private String attpath;
	private String pmin;
	private String pmax;
	
	public ReportEntity(String rscpath, String attpath, String pmin, String pmax){
		
		this.pmin=pmin;
		this.pmax=pmax;
		this.rscpath=rscpath;
		this.attpath=attpath;
		
	}
	
	public ReportEntity(){
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
	
	public String getRscpath() {
		return rscpath;
	}


	public void setRscpath(String rscpath) {
		this.rscpath = rscpath;
	}


	public String getAttpath() {
		return attpath;
	}


	public void setAttpath(String attpath) {
		this.attpath = attpath;
	}

	public String getPmin() {
		return pmin;
	}

	public void setPmin(String pmin) {
		this.pmin = pmin;
	}

	public String getPmax() {
		return pmax;
	}

	public void setPmax(String pmax) {
		this.pmax = pmax;
	}




}
