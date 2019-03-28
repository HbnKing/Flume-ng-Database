import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * @author wangheng
 * @create 2019-02-12 下午3:36
 * @desc
 *
 * 测试类 是否可以执行
 **/
public class TestMongo {

    public static void main(String[] args) {

        MongoClient mongoClient = new MongoClient( new MongoClientURI("mongodb://192.168.3.171:27017/"));

// Select the MongoDB database and collection to open the change stream against

        MongoDatabase db = mongoClient.getDatabase("test");

        MongoCollection<Document> collection = db.getCollection("a");
        collection.insertOne(new Document("name","wh"));

// Create $match pipeline stage.
       /* List<Bson> pipeline = singletonList(Aggregates.match(Filters.or(
                Document.parse("{'fullDocument.username': 'alice'}"),
                Filters.in("operationType", asList("delete")))));
*/
// Create the change stream cursor, passing the pipeline to the
// collection.watch() method





      /*  MongoCursor<ChangeStreamDocument<Document>> iterator = collection.watch().fullDocument(FullDocument.DEFAULT).iterator();
        System.out.println("_____________");


        while (iterator.hasNext()){

            ChangeStreamDocument<Document> next = iterator.next();

            System.out.println(next.getOperationType().getValue());
            System.out.println(next.getFullDocument());
            System.out.println(next);
        }*/
    }
}
