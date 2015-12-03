package com.spring.Controller;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jws.WebResult;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebEndpoint;

import org.bson.BasicBSONObject;
import org.bson.Document;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCursor;
import com.mongodb.operation.bootstrap_process;
import com.mongodb.operation.mongoChange;
import com.mongodb.operation.register_process;
import com.spring.Entity.AttributeEntity;
import com.spring.Entity.BootstrapMsg;
import com.spring.Entity.BootstrapRsp;
import com.spring.Entity.ObjRscDesMap;
import com.spring.Entity.RegisterRsp;
import com.spring.Entity.RegisterMsg;
import com.spring.Entity.ReportEntity;
import com.spring.Entity.ResourceCommand;
import com.spring.Entity.ResourceLayout;


@Controller
//@RestController  ---------> this is to define the whole class to be rendered as json
public class Client_jsonController {

	private bootstrap_process btpro = new bootstrap_process();
	private String uri,urial,uriat,urider,uriupd, endpoint_client_name_txt;
	private register_process rgpro = new register_process();
	private update_lwm2mRunnable update_job = new update_lwm2mRunnable();
	Thread update_thread = new Thread(update_job,"update_job");
	private ArrayList<ResourceCommand> rsc_cmds = new ArrayList<ResourceCommand>();
	private ArrayList<ReportEntity> rpt_pnds = new ArrayList<ReportEntity>();
	private Map<String,String> att_lst_rpt_time = new HashMap<String,String> ();
	private Map<String,AttributeEntity> att_mx_rpt_time = new HashMap<String,AttributeEntity> ();
	
    @RequestMapping(value = "/")    //show up the device main panel
    public String genNewDevice(Model model)
    {
    
        return "device_panel";
    }
	
	
    @RequestMapping(value="/rslst",method= RequestMethod.GET)  //show up the device resource full list(no attribute is shown up here)
    @ResponseBody
    public String rslst(){

    	
		BasicDBObject fields = new BasicDBObject().append("_id", 0);	
		Document rtv = btpro.find(fields);
		String epn = rtv.get("endpoint_client_name").toString();
		
		Document obj_src_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("object_list");
		Iterator<String> xp = obj_src_d.keySet().iterator();
		ArrayList<ResourceLayout> tstmod = new ArrayList<ResourceLayout>();
		ResourceLayout rl=null;
		
//below logic is cumbersome, as we need to show up all the resource under different object id and different object
		//instance, it needs to loop to check level by level to get all the  element within each level and then search
		// with this element to get the value(this kind of data extraction is developed basing on the standard of 
		// 3 tiers resource management (obj id/obj inst/resource)
		while(xp.hasNext()) //loop the object id level
		{
			String obj_id_doc = xp.next().toString();
			
			Document obj_ist_doc = (Document) obj_src_d.get(obj_id_doc);
			Iterator<String> obj_ist_doc_itor = obj_ist_doc.keySet().iterator();
			
			while(obj_ist_doc_itor.hasNext())  //loop the object instance level
			{
				String obj_ist = obj_ist_doc_itor.next().toString();
				Document obj_ist_obj = (Document) obj_ist_doc.get(obj_ist);
				Iterator<String> rsc_id_itor = obj_ist_obj.keySet().iterator();
					while(rsc_id_itor.hasNext())	//loop the resource id level
					{	
						String rsc_id = rsc_id_itor.next().toString();
						if(rsc_id.equals("11"))
							continue;
						rl = new ResourceLayout();
						rl.setObjid(obj_id_doc);
						rl.setObjiddes(ObjRscDesMap.descHelper.get(obj_id_doc));
						rl.setObjist(obj_ist);
						rl.setRscid(rsc_id);
						rl.setRsciddes(ObjRscDesMap.descHelper.get(obj_id_doc+"/"+rsc_id));
						rl.setRscval(obj_ist_obj.get(rsc_id).toString());
						tstmod.add(rl);
						//prepare all the resource into arraylist for data transmission to device ui
					}
			}
		}

		//as the device ui will parse data in json format, here we use objectmapper to change our arraylist containing
		//all our resource into a json format data
			ObjectMapper mapper = new ObjectMapper();
			String output=null;
			try {
				output = mapper.writeValueAsString(tstmod);
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
			}
		return output;
    	
    }
    
    @RequestMapping(value="/rslst/{objid}",method= RequestMethod.GET)
    @ResponseBody
    public String get_by_objid(@PathVariable("objid") String objid){
    	
    	BasicDBObject fields = new BasicDBObject().append("_id", 0);	
		Document rtv = btpro.find(fields);
		String epn = rtv.get("endpoint_client_name").toString();
		
		Document obj_src_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("object_list");
		Document obj_id = (Document) obj_src_d.get(objid);
		if(obj_id==null)
			return null;
		Iterator<String> xp = obj_id.keySet().iterator();
		ArrayList<ResourceLayout> tstmod = new ArrayList<ResourceLayout>();
		ResourceLayout rl=null;
		while(xp.hasNext()) //loop the object instance level
		{
			String obj_ist = xp.next().toString();
			
			Document obj_rsc_doc = (Document) obj_id.get(obj_ist);

			Iterator<String> rsc_id_itor = obj_rsc_doc.keySet().iterator();
					while(rsc_id_itor.hasNext())	//loop the resource id level
					{
						String rsc_id = rsc_id_itor.next().toString();
						rl = new ResourceLayout();
						rl.setObjid(objid);
						rl.setObjiddes(ObjRscDesMap.descHelper.get(objid));
						rl.setObjist(obj_ist);
						rl.setRscid(rsc_id);
						rl.setRsciddes(ObjRscDesMap.descHelper.get(objid+"/"+rsc_id));
						rl.setRscval(obj_rsc_doc.get(rsc_id).toString());
						tstmod.add(rl);
					}
		}

			ObjectMapper mapper = new ObjectMapper();
			String output=null;
			try {
				output = mapper.writeValueAsString(tstmod);
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
			}
		return output;
    	
    }
    
    @RequestMapping(value="/rslst/{objid}/{objist}",method= RequestMethod.GET)
    @ResponseBody
    public String get_by_objist(@PathVariable("objid") String objid,@PathVariable("objist") String objist){

    	BasicDBObject fields = new BasicDBObject().append("_id", 0);	
		Document rtv = btpro.find(fields);
		
		Document obj_src_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("object_list");
		Document obj_id = (Document) obj_src_d.get(objid);
		if(obj_id==null)
			return null;
		Document obj_id_ist = (Document) obj_id.get(objist);
		if(obj_id_ist==null)
			return null;
		Iterator<String> xp = obj_id_ist.keySet().iterator();
		ArrayList<ResourceLayout> tstmod = new ArrayList<ResourceLayout>();
		ResourceLayout rl=null;
		while(xp.hasNext()) //loop the object resource 
		{
			String rsc_id = xp.next().toString();
			rl = new ResourceLayout();
			rl.setObjid(objid);
			rl.setObjiddes(ObjRscDesMap.descHelper.get(objid));
			rl.setObjist(objist);
			rl.setRscid(rsc_id);
			rl.setRsciddes(ObjRscDesMap.descHelper.get(objid+"/"+rsc_id));
			rl.setRscval(obj_id_ist.get(rsc_id).toString());
			tstmod.add(rl);
		}

			ObjectMapper mapper = new ObjectMapper();
			String output=null;
			try {
				output = mapper.writeValueAsString(tstmod);
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
			}
		return output;
    	
    }
    
    @RequestMapping(value="/rslst/{objid}/{objist}/{rscid}",method= RequestMethod.GET)
    @ResponseBody
    public String get_by_srcid(@PathVariable("objid") String objid,@PathVariable("objist") String objist,
    		@PathVariable("rscid") String rscid){
		
		Document obj_src_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("object_list");
		Document obj_id = (Document) obj_src_d.get(objid);
		if(obj_id==null)
			return null;
		Document obj_id_ist = (Document) obj_id.get(objist);
		if(obj_id_ist==null)
			return null;
		if(obj_id_ist.get(rscid)==null)
			return null;
		String obj_id_ist_rsc = obj_id_ist.get(rscid).toString();
		
		ArrayList<ResourceLayout> tstmod = new ArrayList<ResourceLayout>();
		ResourceLayout rl=null;

			rl = new ResourceLayout();
			rl.setObjid(objid);
			rl.setObjiddes(ObjRscDesMap.descHelper.get(objid));
			rl.setObjist(objist);
			rl.setRscid(rscid);
			rl.setRsciddes(ObjRscDesMap.descHelper.get(objid+"/"+rscid));
			rl.setRscval(obj_id_ist_rsc);
			tstmod.add(rl);

			ObjectMapper mapper = new ObjectMapper();
			String output=null;
			try {
				output = mapper.writeValueAsString(tstmod);
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
			}
		return output;
    }
    
    @RequestMapping(value="/discover/{objid}",method= RequestMethod.GET)
    @ResponseBody
    public String discover_by_objid(@PathVariable("objid") String objid){
    	
    	BasicDBObject fields = new BasicDBObject().append("_id", 0);	
		Document rtv = btpro.find(fields);
		
		Document obj_att_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("attribute_lst");
		Document obj_src_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("object_list");		
		Document obj_id = (Document) obj_src_d.get(objid);
		if(obj_id==null)
			return null;
		String output="",att_lst="", obj_tag="", rsc_lst="";
		
		obj_tag = ObjRscDesMap.pathbuild(null, objid);
		
		Document obj_id_att = (Document) obj_att_d.get(objid);
		if(obj_id_att!=null){
			Iterator<String> attito = obj_id_att.keySet().iterator();
			while(attito.hasNext()){
				String att_po = attito.next().toString();
				
				if(ObjRscDesMap.att_full_lst.containsKey(att_po)){
					att_lst = att_lst+att_po+"="+obj_id_att.get(att_po).toString()+";";
				}
			}
			
			if(!att_lst.equals(""))
				att_lst = att_lst.substring(0, att_lst.length()-1);
		}

		Iterator<String> xp = obj_id.keySet().iterator();

		while(xp.hasNext()) //loop the object instance level
		{
			String obj_ist = xp.next().toString();
			String cur_src_path = ObjRscDesMap.pathbuild(obj_tag, obj_ist);
			Document obj_rsc_doc = (Document) obj_id.get(obj_ist);

			Iterator<String> rsc_id_itor = obj_rsc_doc.keySet().iterator();
					while(rsc_id_itor.hasNext())	//loop the resource id level
					{
						String rsc_id = rsc_id_itor.next().toString();
						rsc_lst = rsc_lst + ObjRscDesMap.pathbuild(cur_src_path, rsc_id)+",";
					}
		}
		output = null;
		if(!att_lst.equals(""))
			output = obj_tag + ";" + att_lst+"," + rsc_lst;
		else
			output = obj_tag + "," + rsc_lst;
		System.out.println(output);
		return output;
    	
    }
    
    @RequestMapping(value="/discover/{objid}/{objist}/{rscid}",method= RequestMethod.GET)
    @ResponseBody
    public String discover_by_srcid(@PathVariable("objid") String objid,@PathVariable("objist") String objist,
    		@PathVariable("rscid") String rscid){
    	
    	BasicDBObject fields = new BasicDBObject().append("_id", 0);	
		Document rtv = btpro.find(fields);
		
		Document obj_att_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("attribute_lst");
		Document obj_src_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("object_list");		
		Document obj_id = (Document) obj_src_d.get(objid);
		if(obj_id==null)
			return null;
		String output="",att_lst="", obj_tag="", rsc_lst="";
		
		obj_tag = ObjRscDesMap.pathbuild(null, objid);
		
		Document obj_id_att = (Document) obj_att_d.get(objid);
		if(obj_id_att!=null)
	{	
		Document src_id_att = (Document) obj_id_att.get(rscid);
		if(src_id_att!=null){
			Iterator<String> attito = src_id_att.keySet().iterator();
			while(attito.hasNext()){
				String att_po = attito.next().toString();
				
				if(ObjRscDesMap.att_full_lst.containsKey(att_po)){
					att_lst = att_lst+att_po+"="+src_id_att.get(att_po).toString()+";";
				}
			}
			
			if(!att_lst.equals(""))
				att_lst = att_lst.substring(0, att_lst.length()-1);
		}
    }

			String cur_src_path = ObjRscDesMap.pathbuild(obj_tag, objist);
			Document obj_rsc_doc = (Document) obj_id.get(objist);
			if(obj_rsc_doc!=null&&obj_rsc_doc.get(rscid)!=null)
				rsc_lst = ObjRscDesMap.pathbuild(cur_src_path, rscid)+";";

			output = rsc_lst+att_lst;

		System.out.println(output);
		return output;
    	
    }
    
    @RequestMapping(value="/write/{objid}/{objist}/{rscid}/{rscval}",method= RequestMethod.GET)
    @ResponseBody
    public String write_by_srcid(@PathVariable("objid") String objid,@PathVariable("objist") String objist,
    		@PathVariable("rscid") String rscid,@PathVariable("rscval") String rscval){
    	
    	BasicDBObject fields = new BasicDBObject().append("_id", 0);	
		Document rtv = btpro.find(fields);
		
		Document obj_src_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("object_list");		
		Document obj_id = (Document) obj_src_d.get(objid);
		if(obj_id==null)
			return null;
		ArrayList<String> newwrite = new ArrayList<String>();
		String pdrcd = objid+"."+objist+"."+rscid+"!"+rscval;
		newwrite.add(pdrcd);
		
   		mongoChange mongoopr = new mongoChange();
		mongoopr.update_source(newwrite,"object_list");

		return objid+"/" + objist +"/" + rscid +" = " + rscval + " is ready";
    	
    }
    
    
    @RequestMapping(value="/updategroup",method= RequestMethod.POST)
    @ResponseBody
    public String write_by_srcid(@RequestBody String gpdtl,HttpServletRequest request){
		
    	System.out.println("u r in client server: "+gpdtl);
   		mongoChange mongoopr = new mongoChange();
   		Document myDoc = Document.parse(gpdtl);
   		String srcpath = "object_list.4.0.11";
		mongoopr.groupupdate(srcpath,myDoc);

		return "group update is done";
    	
    }
    
    
	
	@RequestMapping(value="/sendmsg/{newmsg}",method= RequestMethod.GET)
	@ResponseBody
	public String changeactivegp(@PathVariable("newmsg") String newmsg){
		
		System.out.println("msg is now in client server");
		RestTemplate restTemplate;
		Document obj_src_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("object_list");
		Document obj_id_ist = (Document) obj_src_d.get("0");
		Document security_d = (Document) obj_id_ist.get("0");
		String epn = btpro.find(new BasicDBObject().append("_id", 0)).get("endpoint_client_name").toString();
		
		
		String datetime = (new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())).toString();
		String reg_uri = security_d.get("0").toString()+"/newmsg/"+epn+"/"+newmsg+"/"+datetime;
		
		restTemplate = new RestTemplate();		
		System.out.println(reg_uri);
		String output = restTemplate.getForObject(reg_uri, String.class);

		return output;
	}
	
    
    
	
	@RequestMapping(value="/rtvgpmsg",method= RequestMethod.GET)
	@ResponseBody
	public String rtvgpmsg(){
		
		RestTemplate restTemplate;
		Document obj_src_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("object_list");
		Document obj_id_ist = (Document) obj_src_d.get("0");
		Document security_d = (Document) obj_id_ist.get("0");
		String epn = btpro.find(new BasicDBObject().append("_id", 0)).get("endpoint_client_name").toString();
		
		String reg_uri = security_d.get("0").toString()+"/rtvgpmsg/"+epn;
		
		restTemplate = new RestTemplate();		
		System.out.println(reg_uri);
		String output = restTemplate.getForObject(reg_uri, String.class);

		return output;
	}
	
    
    @RequestMapping(value="/writeattr/{objid}/{rscid}/{att}/{attv}",method= RequestMethod.GET)
    @ResponseBody
    public String writeattr_by_srcid(@PathVariable("objid") String objid,@PathVariable("rscid") String rscid,
    		@PathVariable("att") String att, @PathVariable("attv") String attv){
    	
    	BasicDBObject fields = new BasicDBObject().append("_id", 0);	
		Document rtv = btpro.find(fields);
		
		Document obj_src_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("attribute_lst");		
		Document obj_id = (Document) obj_src_d.get(objid);
		if(obj_id==null)
			return null;
		ArrayList<String> newwrite = new ArrayList<String>();
		String pdrcd = objid+"."+rscid+"."+ att +"!"+attv;
		newwrite.add(pdrcd);
		
   		mongoChange mongoopr = new mongoChange();
		mongoopr.update_source(newwrite,"attribute_lst");

		return objid+"/" + rscid +"/" + att +" = " + attv + " is ready";
    	
    }
    
    @RequestMapping(value="/writeattr/{objid}/{att}/{attv}",method= RequestMethod.GET)
    @ResponseBody
    public String writeattr_by_objid(@PathVariable("objid") String objid,
    		@PathVariable("att") String att, @PathVariable("attv") String attv){
    	
    	BasicDBObject fields = new BasicDBObject().append("_id", 0);	
		Document rtv = btpro.find(fields);
		
		Document obj_src_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("attribute_lst");		
		Document obj_id = (Document) obj_src_d.get(objid);
		if(obj_id==null)
			return null;
		ArrayList<String> newwrite = new ArrayList<String>();
		String pdrcd = objid+"."+ att +"!"+attv;
		newwrite.add(pdrcd);
		
   		mongoChange mongoopr = new mongoChange();
		mongoopr.update_source(newwrite,"attribute_lst");

		return objid +"/" + att +" = " + attv + " is ready";
    	
    }
    
    
    @RequestMapping(value="/execute/{objid}/{objist}/{rscid}/{cmd}",method= RequestMethod.GET)
    @ResponseBody
    public String execute_command(@PathVariable("objid") String objid,@PathVariable("objist") String objist,
    		@PathVariable("rscid") String rscid,@PathVariable("cmd") String cmd){
    	
    	BasicDBObject fields = new BasicDBObject().append("_id", 0);	
		Document rtv = btpro.find(fields);
		
		Document obj_src_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("object_list");		
		Document obj_id = (Document) obj_src_d.get(objid);
		if(obj_id==null)
			return null;
		Document obj_ist_id = (Document) obj_id.get(objist);
		if(obj_ist_id==null)
			return null;
		String obj_ist_rsc_val = obj_ist_id.get(rscid).toString();
		if(obj_ist_rsc_val==null)
			return null;
		ResourceCommand cmdunit = new ResourceCommand();
		cmdunit.setCmd(cmd);
		cmdunit.setObjid(objid);
		cmdunit.setObjist(objist);
		cmdunit.setRscid(rscid);
		rsc_cmds.add(cmdunit);

		return objid+"/" + objist +"/" + rscid +" command: "+cmd+" ran.";
		
    }
    
    
    @RequestMapping(value="/cmdchange",method= RequestMethod.GET)
    @ResponseBody
    public String cmdchange(){

    	ArrayList<ResourceCommand> tmp=new ArrayList<ResourceCommand>(rsc_cmds); 
    	rsc_cmds.clear();
    	
			ObjectMapper mapper = new ObjectMapper();
			String output=null;
			try {
				output = mapper.writeValueAsString(tmp);
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
			}

    	
		return output;
    	
    }
    
    
    @RequestMapping(value="/createinstance/{objid}/{rscid}/{newvalue}",method= RequestMethod.GET)
    @ResponseBody
    public String createinstance(@PathVariable("objid") String objid,@PathVariable("newvalue") String newvalue,
    		@PathVariable("rscid") String rscid){
    	
    	BasicDBObject fields = new BasicDBObject().append("_id", 0);	
		Document rtv = btpro.find(fields);
		
		Document obj_src_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("object_list");		
		Document obj_id = (Document) obj_src_d.get(objid);
		if(obj_id==null)
			return null;
		
		Iterator<String> obj_id_itor = obj_id.keySet().iterator();
		Integer curmx = -1;
		while(obj_id_itor.hasNext())  //loop the object instance level
		{
			 curmx = Math.max(curmx, Integer.parseInt(obj_id_itor.next().toString()));
		}
		curmx++;
		ArrayList<String> newwrite = new ArrayList<String>();
		String pdrcd = objid+"."+String.valueOf(curmx)+"."+rscid+"!"+newvalue;
		newwrite.add(pdrcd);
		
   		mongoChange mongoopr = new mongoChange();
		mongoopr.update_source(newwrite,"object_list");

		return "ok";
		
    }
    
    @RequestMapping(value="/deleteinst/{objid}/{objist}",method= RequestMethod.GET)
    @ResponseBody
    public String deleteinst(@PathVariable("objid") String objid,@PathVariable("objist") String objist){
    	
    	BasicDBObject fields = new BasicDBObject().append("_id", 0);	
		Document rtv = btpro.find(fields);
		
		Document obj_src_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("object_list");		
		Document obj_id = (Document) obj_src_d.get(objid);
		if(obj_id==null)
			return null;
		Document obj_ist_id = (Document) obj_id.get(objist);
		if(obj_ist_id==null)
			return null;

		ArrayList<String> newwrite = new ArrayList<String>();
		String pdrcd = objid+"."+objist;
		newwrite.add(pdrcd);
		
   		mongoChange mongoopr = new mongoChange();
		mongoopr.delete_source(newwrite,"object_list");

		return "deleted";
		
    }
    

    @RequestMapping(value="/update",method= RequestMethod.POST)
    @ResponseBody
    public String update(@RequestBody ArrayList<String> obj) {

    		ReportEntity rpt;
          	for(int i=0; i<obj.size(); i++){
          		System.out.println((obj.get(i)));

    			String[] srcdetail = obj.get(i).split("!");

          		String rscpath="", attpath="",pmin=null,pmax=null,tmppth=srcdetail[0];
          		
          		rscpath = "object_list."+srcdetail[0] + ":" +srcdetail[1];
          		attpath	= "attribute_lst."+ tmppth.substring(0, tmppth.indexOf("."))+"."+tmppth.substring(tmppth.lastIndexOf(".")+1);
          		
        		Document obj_att_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("attribute_lst");
        		
        		Document att_obj_id = (Document)obj_att_d.get(tmppth.substring(0, tmppth.indexOf(".")));
        		if(att_obj_id!=null)
        		{
        			String pmindoc=(String)att_obj_id.get("pmin"),pmaxdoc=(String)att_obj_id.get("pmax"), 
        					canceldoc = (String)att_obj_id.get("cancel");
        			if(canceldoc==null || !canceldoc.equals("cancel"))
        			{
        				
        				if(pmindoc!=null)
        					pmin = pmindoc.toString();
        				if(pmaxdoc!=null)
        					pmax = pmaxdoc.toString();
        				Document att_src_id = (Document)att_obj_id.get(tmppth.substring(tmppth.lastIndexOf(".")+1));
        				if(att_src_id!=null)
        				{
        					canceldoc = (String)att_src_id.get("cancel");
        					if(canceldoc==null || !canceldoc.equals("cancel"))
        					{
        						pmindoc=(String)att_src_id.get("pmin");
        						pmaxdoc=(String)att_src_id.get("pmax");
        						if(pmindoc!=null)
        							pmin = pmindoc.toString();
        						if(pmaxdoc!=null)
        							pmax = pmaxdoc.toString();
        					
        					}
        				}
        		
        			}
        			else
        			{
        				if(pmindoc!=null)
        					pmin = pmindoc.toString();
        				if(pmaxdoc!=null)
        					pmax = pmaxdoc.toString();
        				Document att_src_id = (Document)att_obj_id.get(tmppth.substring(tmppth.lastIndexOf(".")+1));
        				if(att_src_id!=null)
        				{
        					canceldoc = (String)att_src_id.get("cancel");
        					if(canceldoc==null || !canceldoc.equals("cancel"))
        					{
        						pmindoc=(String)att_src_id.get("pmin");
        						pmaxdoc=(String)att_src_id.get("pmax");
        						if(pmindoc!=null)
        							pmin = pmindoc.toString();
        						if(pmaxdoc!=null)
        							pmax = pmaxdoc.toString();
        					
        					}
        					else
        					{
        						pmax=null;
        						pmin=null;
        					}
        				}
        			}
        			
        		}
        		if(pmax!=null||pmin!=null)
        		{
        			System.out.println("pmax="+pmax + " pmin="+pmin+" rscpath="+rscpath);
        			rpt = new ReportEntity(rscpath,attpath,pmin,pmax);
        			rpt_pnds.add(rpt);
        			if(!att_lst_rpt_time.containsKey(attpath))
        				att_lst_rpt_time.put(attpath, "0");   //initialize the report record timer
        		}
          		
          		
          	}
          	
    		mongoChange mongoopr = new mongoChange();
    		
    		mongoopr.update_source(obj,"object_list");
				
			return "ok";
  
}
    
    
	@RequestMapping(value="/bootstrap",method= RequestMethod.POST)
//	public BootstrapRsp bootstrap_request(@RequestBody BootstrapMsg evd){
	public @ResponseBody  String bootstrap_request(){
		
		System.out.println("web_client_bootstrap");
		Document obj_src_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("object_list");
		Document obj_id_ist  = (Document) obj_src_d.get("0");
		Document security_d = (Document) obj_id_ist.get("0");
		
		BasicDBObject fields = new BasicDBObject().append("_id", 0);	
		BootstrapMsg btmsg = new BootstrapMsg();
		btmsg.setEndpoint_client_name(btpro.find(fields).get("endpoint_client_name").toString());
		String reg_uri = null;
		RestTemplate restTemplate;
		
		uriat = security_d.get("0").toString();
		
		if(security_d.get("1").toString().equals("true"))
		{
			//show the bootstrap request json msg
			ObjectMapper mapper = new ObjectMapper();
			System.out.println(uriat);
			System.out.println("bootstrap request msg from device to server:");
			try {
				System.out.println(mapper.writeValueAsString(btmsg));
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
			}
		
			System.out.println(btmsg.getEndpoint_client_name());
			restTemplate = new RestTemplate();		
			URI targetUrl= UriComponentsBuilder.fromUriString(uriat)
			    .queryParam("endpoint_client_name", btmsg.getEndpoint_client_name())
			    .build()
			    .toUri();
			BootstrapRsp btresult = restTemplate.postForObject(targetUrl,btmsg.getEndpoint_client_name(),BootstrapRsp.class);

			List<String> register_uri = btresult.getRegister_server_uri();
			if(register_uri.size()>0)
				reg_uri = register_uri.get(0).toString();

			btpro.update(btresult);
			return "Bootstrap Time Stamp: "+ btresult.getBootstrap_time_stamp();
		}
		else
		{
		return "Last Bootstrap Time: "+ 
				btpro.find(new BasicDBObject().append("_id", 0)).getString("bootstrap_time_stamp");
		}
		
	}
	
	@RequestMapping(value="/register",method= RequestMethod.POST)
	public @ResponseBody String register_request(){
		RestTemplate restTemplate;
		Document obj_src_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("object_list");
		Document obj_id_ist = (Document) obj_src_d.get("0");
		Document security_d = (Document) obj_id_ist.get("0");
		
		String reg_uri = security_d.get("0").toString();
		//-----------------register
		String full_rsc = rgpro.getresource(new BasicDBObject().append("_id", 0)).toJson();
		restTemplate = new RestTemplate();		
		restTemplate.postForObject( reg_uri, full_rsc, RegisterRsp.class);
//-------------------kick off update
		
		//Thread update_thread = new Thread(update_job,"update_job");
		if(!update_thread.isAlive())
		update_thread.start();
		

		
		return "ok";
	}
	
	@RequestMapping(value="/deregister",method= RequestMethod.POST)
	@ResponseBody
	public String deregister(@RequestParam(value="endpoint_client_name", defaultValue="default")String endpoint_client_name){
    	update_job.stopupdate();
    	
    	

		BasicDBObject fields = new BasicDBObject().append("_id", 0);	
		Document rtv = btpro.find(fields);
		
		String epn = rtv.get("endpoint_client_name").toString();
		
		Document obj_src_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("object_list");
		Document obj_id_ist = (Document) obj_src_d.get("0");
		Document security_d = (Document) obj_id_ist.get("0");

		String urireg = security_d.get("0").toString();

		System.out.println(urireg);

		urider = urireg+"/deregister";
		
		RestTemplate restTemplate = new RestTemplate();		
		URI targetUrl= UriComponentsBuilder.fromUriString(urider)
			    .queryParam("endpoint_client_name", epn)
			    .build()
			    .toUri();
      restTemplate.postForObject(targetUrl, epn,String.class);

		return "ok";
	}
	
	@RequestMapping(value="/sleep",method= RequestMethod.POST)
	@ResponseBody
	public String sleep(){
		update_job.stopupdate();
		return "sleep done";
	}
	
	public void refreshatt_cotrol(){
		
		att_mx_rpt_time.clear();
    	
		Document obj_att_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("attribute_lst");
		if(obj_att_d!=null)
		{
			Iterator<String> attobjid_ito = obj_att_d.keySet().iterator();
			
			while(attobjid_ito.hasNext())   //loop all obj id
			{	
				String att_obj_id = attobjid_ito.next().toString();
				Document obj_id_att = (Document) obj_att_d.get(att_obj_id);
				if(obj_id_att!=null)
				{
					Iterator<String> attito = obj_id_att.keySet().iterator();
					while(attito.hasNext())	//loop all sub-element(src id / obj lvl pmax) under current obj id
					{
						String att_po = attito.next().toString();
						if(att_po.equals("pmax"))
						{
							if(obj_id_att.get("cancel")==null||!obj_id_att.get("cancel").toString().equals("cancel"))
							{
								AttributeEntity attelement = new AttributeEntity(att_obj_id,obj_id_att.get(att_po).toString(),"0");
							
								att_mx_rpt_time.put(att_obj_id,attelement);
							}
						}
						else if(!ObjRscDesMap.att_full_lst.containsKey(att_po))
						{
							Document obj_rsc_att = (Document) obj_id_att.get(att_po);
							Iterator<String> rsc_attito = obj_rsc_att.keySet().iterator();
							while(rsc_attito.hasNext())
							{
								String rsc_att_po = rsc_attito.next().toString();
								if(rsc_att_po.equals("pmax"))
								{
									if(obj_rsc_att.get("cancel")==null||!obj_rsc_att.get("cancel").toString().equals("cancel"))
									{
										AttributeEntity attelement = new AttributeEntity(att_obj_id+"."+att_po,obj_rsc_att.get(rsc_att_po).toString(),"0");
										att_mx_rpt_time.put(att_obj_id+"."+att_po, attelement);
									}
								}
							}
							
						}
					}
			
				}
			}
			
			
				
		}
		
	}

	@RequestMapping(value="/activate",method= RequestMethod.POST)
	@ResponseBody
	public String activate(){
		
		refreshatt_cotrol();
		//Thread update_thread = new Thread(update_job,"update_job");
		if(!update_thread.isAlive())
		update_thread.start();
		return "active done";
	}

    class update_lwm2mRunnable implements Runnable {
        private volatile boolean keepRunning;

        public update_lwm2mRunnable() {
        }

        public void run() {
          threadupdate();
        }

        public void threadupdate() {
          long normalSleepTime = 1000;

          keepRunning = true;

          while (keepRunning) {
            try {
              Thread.sleep(normalSleepTime);
            } catch (InterruptedException x) {
              // ignore
            }
            if(keepRunning==false)
            	break;
            
            
          //  refreshatt_cotrol();  cannot refresh here as it will wash out the latest rpt time
            
            String tmptime = (new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())).toString();
			BasicDBObject fields = new BasicDBObject().append("_id", 0);	
			Document rtv = btpro.find(fields);
			String epn = rtv.get("endpoint_client_name").toString();
	
			Document obj_src_d = (Document) btpro.find(new BasicDBObject().append("_id", 0)).get("object_list");
			Document obj_id_ist = (Document) obj_src_d.get("0");
			Document security_d = (Document) obj_id_ist.get("0");

			String urireg="";
		
			urireg = security_d.get("0").toString();
			uriupd = urireg+"/singleupdate";
			RestTemplate restTemplate = new RestTemplate();	
		
	    		if(rpt_pnds.size()!=0)
	    		{
					
	    			for(int i=0; i<rpt_pnds.size();i++)
	    			{	
	    				String lstrpt_time = att_lst_rpt_time.get(rpt_pnds.get(i).getAttpath());
						String obj_rsc = (new Document().append("endpoint_client_name", epn).
    							append("src_detail", rpt_pnds.get(i).getRscpath())).toJson();
						
	    				if(!lstrpt_time.equals("0"))
	    				{
	    					Integer timediff = rpt_pnds.get(i).timediff(tmptime,lstrpt_time);
	    					System.out.println("current time "+tmptime + " lst time="+lstrpt_time+ "different: "+String.valueOf(timediff));
	    					if(timediff>=Integer.parseInt(rpt_pnds.get(i).getPmin()))
	    						{
	    							restTemplate.postForObject(uriupd, obj_rsc, String.class);
	    							att_lst_rpt_time.put(rpt_pnds.get(i).getAttpath(), tmptime);
	    							att_mx_rpt_time.get(rpt_pnds.get(i).getAttpath().substring(rpt_pnds.get(i).getAttpath().indexOf(".")+1)).setLstupd(tmptime);
	    							rpt_pnds.remove(i);
	    						}
	    				}
	    				else
	    				{
	    					restTemplate.postForObject(uriupd, obj_rsc, String.class);
	    					att_lst_rpt_time.put(rpt_pnds.get(i).getAttpath(), tmptime);
							att_mx_rpt_time.get(rpt_pnds.get(i).getAttpath().substring(rpt_pnds.get(i).getAttpath().indexOf(".")+1)).setLstupd(tmptime);
							rpt_pnds.remove(i);
	    				}
	    				

	    				
	    			}
	    	
	    		}
	    		
	    		
	    		for (Map.Entry<String, AttributeEntity> entry : att_mx_rpt_time.entrySet())
	       		{
	    			String obj_id="", rsc_id="";
	    			
	    			if(entry.getKey().toString().contains("."))
	    				{
	    					obj_id = entry.getKey().toString().substring(0, entry.getKey().toString().indexOf("."));
	    					rsc_id = entry.getKey().toString().substring(entry.getKey().toString().lastIndexOf(".")+1);
	    				}
	    			else
	    				obj_id = entry.getKey().toString();

	    				String doflag="";
	    			if(entry.getValue().getLstupd().equals("0"))
	    			{
	    				doflag="y";
	    			}
	    			else
	    			{
	    				Integer timediff = entry.getValue().timediff(tmptime,entry.getValue().getLstupd());
	    				
	    				if(timediff>Integer.valueOf(entry.getValue().getPmax()))
	    					doflag="y";	
	    			}
	        		
	    			
	    			if(doflag.equals("y")){
	    				
	    				
	    				Document obj_ist_doc = (Document) obj_src_d.get(obj_id);
						Iterator<String> obj_ist_doc_itor = obj_ist_doc.keySet().iterator();
						
						while(obj_ist_doc_itor.hasNext())  //loop the object instance level
						{
							String obj_ist = obj_ist_doc_itor.next().toString();
							Document obj_ist_obj = (Document) obj_ist_doc.get(obj_ist);
							
							if(rsc_id.equals(""))
							{
							Iterator<String> rsc_id_itor = obj_ist_obj.keySet().iterator();
								while(rsc_id_itor.hasNext())	//loop the resource id level
								{
									rsc_id = rsc_id_itor.next().toString();
									String src_detail = "object_list."+obj_id+"."+obj_ist+"."+rsc_id+":"+obj_ist_obj.get(rsc_id).toString();
									String obj_rsc = (new Document().append("endpoint_client_name", epn).
			    							append("src_detail", src_detail)).toJson();
									System.out.println(obj_rsc);
									
									restTemplate.postForObject(uriupd, obj_rsc, String.class);
								}
								entry.getValue().setLstupd(tmptime);
							}
							else
							{
								
								if(!entry.getValue().getLstupd().equals("0"))
								{
									Integer timediff = entry.getValue().timediff(tmptime,entry.getValue().getLstupd());
								
									if(timediff>Integer.valueOf(entry.getValue().getPmax()))
									{
										String src_detail = "object_list."+obj_id+"."+obj_ist+"."+rsc_id+":"+obj_ist_obj.get(rsc_id).toString();
										String obj_rsc = (new Document().append("endpoint_client_name", epn).
												append("src_detail", src_detail)).toJson();
										System.out.println(obj_rsc);
										restTemplate.postForObject(uriupd, obj_rsc, String.class);
										entry.getValue().setLstupd(tmptime);
									}
								}
								else
								{
									String src_detail = "object_list."+obj_id+"."+obj_ist+"."+rsc_id+":"+obj_ist_obj.get(rsc_id).toString();
									String obj_rsc = (new Document().append("endpoint_client_name", epn).
											append("src_detail", src_detail)).toJson();
									System.out.println(obj_rsc);
									restTemplate.postForObject(uriupd, obj_rsc, String.class);
									entry.getValue().setLstupd(tmptime);
								}
								
							}
						}
				
					
	    				
	    				
	    			}
	    			
	     		}
	    		
	// group msg update request   Start		
	    		
	    		
	    		
	    		
	// group msg update request   End
          }
        }

        public void stopupdate() {
          keepRunning = false;
        }

      }
	
}
