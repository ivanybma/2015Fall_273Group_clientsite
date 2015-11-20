package com.mongodb.operation;

import java.util.ArrayList;
import java.util.Arrays;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class mongoChange {
	
	
	public void update_source(ArrayList<String> srclst, String type){
		
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		MongoDatabase database = mongoClient.getDatabase("device_a");
		MongoCollection<Document> rsc_collection = database.getCollection("current_resources");
		
		BasicDBObject fields = new BasicDBObject().append("_id", 0);	
		Document doc = rsc_collection.find().projection(fields).first();

		for(int i=0; i<srclst.size(); i++)
		{
			String[] srcdetail = srclst.get(i).split("!");

				System.out.println("object_list."+srcdetail[0] + ":" +srcdetail[1]);
				rsc_collection.updateOne(new Document("endpoint_client_name",doc.get("endpoint_client_name").toString()), 
					new Document("$set", new Document(type+"."+srcdetail[0],srcdetail[1])));
		}
		
		mongoClient.close();
		
	}
	
	public void delete_source(ArrayList<String> srclst, String type){
		
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		MongoDatabase database = mongoClient.getDatabase("device_a");
		MongoCollection<Document> rsc_collection = database.getCollection("current_resources");
		
		
		BasicDBObject fields = new BasicDBObject().append("_id", 0);	
		Document doc = rsc_collection.find().projection(fields).first();

		for(int i=0; i<srclst.size(); i++)
		{
			String[] srcdetail = srclst.get(i).split("!");

				System.out.println("object_list."+srcdetail[0]);
				rsc_collection.updateOne(new Document("endpoint_client_name",doc.get("endpoint_client_name").toString()), 
					new Document("$unset", new Document(type+"."+srcdetail[0],"")));
		}
		
		mongoClient.close();
		
	}
	
	
}
