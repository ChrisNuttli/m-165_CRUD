package ch.bbw.christiannuttli;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.connection.TransportSettings;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.Document;
import org.bson.conversions.Bson;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> jokesCollection;

    static void main() {
        connectClient();
        printJokeId2();
    }

    static void connectClient() {
        Dotenv dotenv = Dotenv.load();
        String protocol = dotenv.get("MONGO_PROTOCOL");
        String username = dotenv.get("MONGO_INITDB_ROOT_USERNAME");
        String password = dotenv.get("MONGO_INITDB_ROOT_PASSWORD");
        String host = dotenv.get("MONGO_HOST");
        String connectionString = "";

        if (protocol.equals("mongodb")) {
            String port = dotenv.get("MONGO_PORT");
            if (port == null) {
                throw new IllegalArgumentException("MongoDB port must be specified, when the protocol is 'mongodb'");
            }
            connectionString = String.format("%s://%s:%s@%s:%s", protocol, username, password, host, port);
        }
        else {
            connectionString = String.format("%s://%s:%s@%s", protocol, username, password, host);
        }

        String databaseName = dotenv.get("MONGO_DATABASE");
        if (databaseName == null) {
            throw new IllegalArgumentException("No Database name was Specified!");
        }

        client =  MongoClients.create(connectionString);
        database = client.getDatabase(databaseName);
        jokesCollection = database.getCollection("jokes");
    }

    static void printJokeId2() {
        Bson query = Filters.eq("id", 2);
        Document doc = jokesCollection.find(query).first();
        if (doc == null) {
            throw new RuntimeException("Joke with id '2' could not be found!");
        }

        System.out.printf("Aufgabe 1 (Joke mit ID 2):\n%s\n", doc.get("text").toString());
    }
}
