package com.spring.Entity;

import java.util.HashMap;
import java.util.Map;

public class ObjRscDesMap {
	static public Map<String, String> descHelper = new HashMap<String, String>();
	static public Map<String, String> att_full_lst = new HashMap<String, String>();
    static {
        descHelper.put("0", "LWM2M Security");
        descHelper.put("1", "LWM2M Server");
        descHelper.put("2", "Access Control");
        descHelper.put("3", "Device");
        descHelper.put("4", "Connectivity Monitoring");
        descHelper.put("5", "Firmware");
        descHelper.put("6", "Location");
        descHelper.put("7", "Connectivity Statistics");
        
        descHelper.put("0/0", "LWM2M Server URI");
        descHelper.put("0/1", "Bootstrap Server");
        descHelper.put("0/2", "Security Mode");
        descHelper.put("0/3", "Public Key or Identity");
        descHelper.put("0/4", "Server Public Key or Identity");
        descHelper.put("0/5", "Secret Key");
        descHelper.put("0/6", "SMS Security Mode");
        descHelper.put("0/7", "SMS Binding KeyParameters");
        descHelper.put("0/8", "SMS Binding Secret Key(s)");
        descHelper.put("0/9", "LWM2M Server SMS Number");
        descHelper.put("0/10", "Short Server ID");
        descHelper.put("0/11", "Client Hold Off Time");
        
        descHelper.put("1/0", "Short Server ID");
        descHelper.put("1/1", "Lifetime");
        descHelper.put("1/2", "Default Minimum Period");
        descHelper.put("1/3", "Default Maximum Period");
        descHelper.put("1/4", "Disable");
        descHelper.put("1/5", "Disable Timeout");
        descHelper.put("1/6", "Notification Storing When Disabled or Offline");
        descHelper.put("1/7", "Binding");
        descHelper.put("1/8", "Registration Update Trigger");
        
        descHelper.put("2/0", "Object ID");
        descHelper.put("2/1", "Object Instance ID");
        descHelper.put("2/2", "ACL");
        descHelper.put("2/3", "Access Control Owner");
        
        descHelper.put("3/0", "Manufacturer");
        descHelper.put("3/1", "Model Number");
        descHelper.put("3/2", "Serial Number");
        descHelper.put("3/3", "Firmware Version");
        descHelper.put("3/4", "Reboot");
        descHelper.put("3/5", "Factory Reset");
        descHelper.put("3/6", "Available Power Sources");
        descHelper.put("3/7", "Power Source Voltage");
        descHelper.put("3/8", "Power Source Current");
        descHelper.put("3/9", "Battery Level");
        descHelper.put("3/10", "Memory Free");
        descHelper.put("3/11", "Error Code");
        descHelper.put("3/12", "Reset Error Code");
        descHelper.put("3/13", "Current Time");
        descHelper.put("3/14", "UTC Offset");
        descHelper.put("3/15", "Timezone");
        descHelper.put("3/16", "Supported Binding and Modes");
        descHelper.put("3/17", "DeviceType");
        descHelper.put("3/18", "Hardware Version");
        descHelper.put("3/19", "Software Version");
        descHelper.put("3/20", "Battery Status");
        descHelper.put("3/21", "Memory Total");
        
        descHelper.put("4/0", "Network Bearer");
        descHelper.put("4/1", "Available Network Bearer");
        descHelper.put("4/2", "Radio Signal Strength");
        descHelper.put("4/3", "Link Quality");
        descHelper.put("4/4", "IP Addresses");
        descHelper.put("4/5", "Router IP Addresse");
        descHelper.put("4/6", "Link Utilization");
        descHelper.put("4/7", "APN");
        descHelper.put("4/8", "Cell ID");
        descHelper.put("4/9", "SMNC");
        descHelper.put("4/10", "SMCC");
        
        descHelper.put("5/0", "Package");
        descHelper.put("5/1", "Package URI");
        descHelper.put("5/2", "Update");
        descHelper.put("5/3", "State");
        descHelper.put("5/4", "Update Supported Objects");
        descHelper.put("5/5", "Update Result");
        descHelper.put("5/6", "PkgName");
        descHelper.put("5/7", "PkgVersion");
        
        descHelper.put("6/0", "Latitude");
        descHelper.put("6/1", "Longitude");
        descHelper.put("6/2", "Altitude");
        descHelper.put("6/3", "Uncertainty");
        descHelper.put("6/4", "Velocity");
        descHelper.put("6/5", "Timestamp");
        
        descHelper.put("7/0", "SMSTx Counter");
        descHelper.put("7/1", "SMS Rx Counter");
        descHelper.put("7/2", "Tx Data");
        descHelper.put("7/3", "Rx Data");
        descHelper.put("7/4", "Max Message Size");
        descHelper.put("7/5", "Average Message Size");
        descHelper.put("7/6", "StartOrReset");
        
        att_full_lst.put("pmin", "Minimum Period");
        att_full_lst.put("pmax", "Maximum Period");
        att_full_lst.put("gt", "Greater Than");
        att_full_lst.put("lt", "Less Than");
        att_full_lst.put("st", "Step");
        att_full_lst.put("cancel", "Cancel");

    }
    
    static public String pathbuild(String exist, String newcontent )
    {
    	String rst="";
    	
    	if(exist==null)
    		exist = "<>";
    	rst = exist.substring(0,exist.indexOf(">"))+"/"+newcontent+">";
    	return rst;
    }
	
}
