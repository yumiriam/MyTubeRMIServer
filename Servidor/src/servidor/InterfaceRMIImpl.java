package servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import teste.InterfaceRMI;
import teste.RMIFileBean;

public class InterfaceRMIImpl extends UnicastRemoteObject implements InterfaceRMI {

	protected InterfaceRMIImpl() throws RemoteException {
		super();
	}

	private static final long serialVersionUID = 1L;

	@Override
	public RMIFileBean rescueFile(String id) throws RemoteException {
		// TODO Auto-generated method stub
		String host = "localhost";
		DBObject myDoc = null;
		RMIFileBean fb = null;
		try {
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
				
				fb.setData((byte[])myDoc.get("data"));
				fb.setName(name);
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
		// TODO Auto-generated method stub
		boolean r = false;
		String host = "localhost";
		DBObject myDoc = null;
		try{
			MongoClient mongoClient = new MongoClient(host);

			DB db = mongoClient.getDB( "sdfinal" );
			
			DBCollection coll = db.getCollection("videos");
			
			BasicDBObject query = new BasicDBObject("_id", id);

			DBCursor cursor = coll.find(query);

	        try {
				if(cursor.hasNext()){
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

}
