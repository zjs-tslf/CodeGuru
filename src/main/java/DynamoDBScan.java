import java.util.Iterator;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
/**
 * Return all items in a DynamoDB Table
 * <p>
 * This code expects that you have AWS credentials set up per:
 * <a href="http://docs.aws.amazon.com/java-sdk/latest/developer-guide/setup-credentials.html">...</a>
 */
public class DynamoDBScan implements RequestHandler<Map<String,String>, String> {

    static final String tableName = "CodeGuru-MusicCollection";

    @Override
    public String handleRequest(Map<String,String> event, Context context)
    {
        return findAllItems();
    }

    private static String findAllItems() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-east-1").build();
        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable(tableName);

        ItemCollection<ScanOutcome> items = table.scan();

        Iterator<Item> iterator = items.iterator();
        StringBuilder itemsToReturn = new StringBuilder();
        while (iterator.hasNext()) {
            itemsToReturn.append(iterator.next().toJSONPretty());
        }

        return itemsToReturn.toString();
    }

}