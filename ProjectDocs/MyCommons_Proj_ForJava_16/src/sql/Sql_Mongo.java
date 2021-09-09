/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.Arrays;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author KOCMOC
 */
public class Sql_Mongo {

    private static MongoClient mongoClient;
    private static final String DATABASE_NAME = "test";
    private static final String COLLECTION_NAME = "people";

    public Sql_Mongo() {
        mongoClient = new MongoClient(new MongoClientURI("mongodb://10.87.0.145:27017")) {
        };
    }

    /**
     * Inserts a person with two addresses to the Collection named "people" -
     * without any changes to the Collection
     */
    public void insertExampleB() {
        //
        List<Integer> books = Arrays.asList(27464, 747854);
        ///
        Document person = new Document("_id", "2")
                .append("name", "Jo Bloggs")
                .append("address",
                        new Document("street", "123 Fake St")
                                .append("city", "Faketon")
                                .append("state", "MA")
                                .append("zip", 12345))
                .append("address2", new Document("street", "Johan Kocksgatan 10")
                        .append("city", "Moscow")
                        .append("state", "RF")
                        .append("zip", 21645))
                .append("books", books);
        //
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        //
        MongoCollection collection = database.getCollection(COLLECTION_NAME); // Here the Collection "people" is created, if it was already present in this case it's chosen
        //
        collection.insertOne(person);
    }

    /**
     * Inserts a person with one address to the Collection named "people"
     */
    public void insertExampleA() {
        //
        List<Integer> books = Arrays.asList(27464, 747854);
        //
        Document person = new Document("_id", "1")
                .append("name", "Jo Bloggs")
                .append("address",
                        new Document("street", "123 Fake St")
                                .append("city", "Faketon")
                                .append("state", "MA")
                                .append("zip", 12345))
                .append("books", books);
        //
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        //
        MongoCollection collection = database.getCollection(COLLECTION_NAME); // Here the Collection "people" is created, if it was already present in this case it's chosen
        //
        collection.insertOne(person);
    }

    public void searchExample() {
        //
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        //
        MongoCollection collection = database.getCollection(COLLECTION_NAME);
        //
        BasicDBObject query = new BasicDBObject("_id", "2");
        FindIterable cursor = collection.find(query);
        //
        //
        for (Object object : cursor) {
            //
            Document doc = (Document) object;
            //
            System.out.println("Name: " + doc.get("name"));
            //
            Document address = (Document) doc.get("address2"); // OBS! Address i an Object also!!
            System.out.println("Zip: " + address.get("zip"));
            //
            System.out.println("Obj: " + object);
            //
        }
        //
    }

    public static void main(String[] args) {
        //
        Sql_Mongo mongo = new Sql_Mongo();
        //
//        mongo.insertExampleA();
        //
//        mongo.insertExampleB();
        //
        mongo.searchExample();
        //
    }

}
