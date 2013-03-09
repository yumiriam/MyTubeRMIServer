package com.milissa.rmiserver;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import com.milissa.rmiclient.InterfaceRMI;
import com.milissa.rmiclient.RMIFileBean;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class ServidorRMI implements InterfaceRMI {

	public ServidorRMI() {
		
	}
	
	@Override
	public RMIFileBean rescueFile(String id) throws RemoteException {
		String host = "localhost";
		DBObject myDoc = null;
		RMIFileBean fb = null;
		try{
			MongoClient mongoClient = new MongoClient(host);

			DB db = mongoClient.getDB( "sdfinal" );
			
			DBCollection coll = db.getCollection("videos");
			
			BasicDBObject query = new BasicDBObject("_id", id);

			DBCursor cursor = coll.find(query);

	        try {
	        	if(cursor.hasNext())
				    myDoc = cursor.next();
				fb = new RMIFileBean();
				// { "_id" : "cone123456" , "name" : "teste" , "description" : "lala no mudo dos poneis"}
				String name = (String)myDoc.get("name");
				
				fb.setName(name);
				fb.setData((byte[])myDoc.get("data"));
	        } finally {
	            cursor.close();
	        }
			
		} catch (Exception e) {
			System.out.println(e);
		}
		return fb;
	}

	@Override
	public boolean saveFile(RMIFileBean fb, String id) throws RemoteException {
		boolean r = false;
		String host = "localhost";
		DBObject myDoc = null;
		try {
			MongoClient mongoClient = new MongoClient(host);

			DB db = mongoClient.getDB( "sdfinal" );
			
			DBCollection coll = db.getCollection("videos");
			
			BasicDBObject query = new BasicDBObject("_id", id);

			DBCursor cursor = coll.find(query);

	        try {
				if(cursor.hasNext()) {
				    myDoc = cursor.next();
					myDoc.put("name", fb.getName());
					myDoc.put("data", fb.getData());
					
					if (coll.update(query, myDoc) != null)
						r = true;
				}
	        } finally {
	            cursor.close();
	        }
		} catch (Exception e) {
			System.out.println(e);
		}
		return r;
	}
	
	public static void main(String[] args) {
        try {
            ServidorRMI obj = new ServidorRMI();
            InterfaceRMI stub = (InterfaceRMI) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("InterfaceRMI", stub);

            System.err.println("Servidor pronto");
        } catch (Exception e) {
            System.err.println("Erro de servidor: " + e.toString());
            e.printStackTrace();
        }
	}
}
