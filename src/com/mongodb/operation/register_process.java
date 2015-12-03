package com.mongodb.operation;

import java.util.Arrays;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class register_process {

	
	public Document getresource(BasicDBObject fields){
		
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		MongoDatabase database = mongoClient.getDatabase("device_a");
		
		MongoCollection<Document> collection = database.getCollection("current_resources");
		
		Document doc = collection.find().projection(fields).first();
		System.out.println(doc.toJson());
		
		mongoClient.close();
		return doc;
	}
	
}
