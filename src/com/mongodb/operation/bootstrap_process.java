package com.mongodb.operation;

import java.util.ArrayList;
import java.util.Arrays;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.util.JSON;
import com.spring.Entity.BootstrapRsp;

public class bootstrap_process {

		
		public Document find(BasicDBObject fields){
			
			
			MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

			MongoDatabase database = mongoClient.getDatabase("device_a");
			MongoCollection<Document> rsc_collection = database.getCollection("current_resources");
			//Document doc = reg_collection.find().projection(fields).first();
			Document doc = rsc_collection.find().projection(fields).first();
			
			mongoClient.close();
			return doc;
		}
		
		public void update(BootstrapRsp btresult){
			
			MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

			MongoDatabase database = mongoClient.getDatabase("device_a");
			MongoCollection<Document> rsc_collection = database.getCollection("current_resources");
			
			ObjectMapper mapper = new ObjectMapper();
			String jsonmsg=null;
			System.out.println("bootstrap response from server to client");
			try {
				System.out.println(mapper.writeValueAsString(btresult));
				jsonmsg=mapper.writeValueAsString(btresult);
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
			}
			System.out.println(jsonmsg);
			Document myDoc = Document.parse(jsonmsg);
			myDoc.remove("endpoint_client_name");
			String rgst_server = ((ArrayList<String>)myDoc.get("register_server_uri")).get(0);
			System.out.println(rgst_server);
			rsc_collection.updateOne(new Document("endpoint_client_name",btresult.getEndpoint_client_name()), 
					new Document("$set", new Document("object_list.0.0.0",rgst_server)));
			rsc_collection.updateOne(new Document("endpoint_client_name",btresult.getEndpoint_client_name()), 
					new Document("$set", new Document("object_list.0.0.1","false")));
			rsc_collection.updateOne(new Document("endpoint_client_name",btresult.getEndpoint_client_name()), 
					new Document("$set", new Document("object_list.3.0.0",myDoc.getString("model"))));
			rsc_collection.updateOne(new Document("endpoint_client_name",btresult.getEndpoint_client_name()), 
					new Document("$set", new Document("object_list.3.0.1",myDoc.getString("manufacturer"))));
			rsc_collection.updateOne(new Document("endpoint_client_name",btresult.getEndpoint_client_name()), 
					new Document("$set", new Document("bootstrap_time_stamp",myDoc.getString("bootstrap_time_stamp"))));
			//rsc_collection.updateOne(new Document("endpoint_client_name",btresult.getEndpoint_client_name()), new Document("$set",myDoc));
			//the replace operation can use the whole document
			mongoClient.close();
			
		}

		

	
}
