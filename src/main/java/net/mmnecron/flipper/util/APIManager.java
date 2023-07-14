package net.mmnecron.flipper.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.mmnecron.flipper.Flipper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.core.util.KeyValuePair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class APIManager {
    public static JsonObject items = new JsonObject();
    public class Auction {
        private String auctionID;
        private Integer price;
        private String itemID;

        public Auction(String auctionID, Integer price, String itemID) {
            this.auctionID = auctionID;
            this.price = price;
            this.itemID = itemID;
        }
    }

    public static class Request {
        public String baseURL = "https://api.hypixel.net/";
        public String path;
        public Collection<KeyValuePair> requestArguments = new ArrayList<KeyValuePair>();

        public void addRequestArgument(String key, String value){
            this.requestArguments.add(new KeyValuePair(key, value));
        }

        public void addRequestArguments(List<KeyValuePair> requestArguments){
            this.requestArguments.addAll(requestArguments);
        }

        public String buildURL() {
            StringBuilder requestURLBuilder = new StringBuilder(this.baseURL + this.path);
            for (KeyValuePair argument:this.requestArguments) {
                requestURLBuilder.append("?").append(argument.getKey()).append("=").append(argument.getValue());
            }
            return requestURLBuilder.toString();
        }
    }

    public static Request getRequest(String path) {
        Request newRequest = getAnonymousRequest(path);
        newRequest.addRequestArgument("key", Flipper.APIKEY);
        return newRequest;
    }

    public static Request getAnonymousRequest(String path) {
        Request newRequest = new Request();
        newRequest.path = path;
        return newRequest;
    }

    public static Runnable getRequest = new Runnable() {
        @Override
        public void run() {
            updateItemList();
        }
    };

    public static void updateItemList(){
        JsonObject responseObject = getItemList();
        if (responseObject.get("success").getAsBoolean()) {
            JsonArray itemsObject = responseObject.get("items").getAsJsonArray();
            for (JsonElement entry:itemsObject) {
                String itemID = entry.getAsJsonObject().get("id").getAsString();
                JsonObject item = new JsonObject();
                if (entry.getAsJsonObject().has("dungeon_item_conversion_cost")) {
                    item.add("conversion_cost", entry.getAsJsonObject().get("dungeon_item_conversion_cost"));
                }
                if (entry.getAsJsonObject().has("upgrade_costs")) {
                    item.add("upgrade_costs", entry.getAsJsonObject().get("upgrade_costs"));
                }
                items.add(itemID, item);
            }
        }
    }

    public static JsonObject getItemList() {
        Gson gson = new Gson();
        Request request = getAnonymousRequest("/resources/skyblock/items");
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpRequest = new HttpGet(request.buildURL());
        httpRequest.addHeader("content-type", "application/json");
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpRequest);
            String responseBody = EntityUtils.toString(response.getEntity());
            return gson.fromJson(responseBody, JsonObject.class);
        } catch (Exception e) {
            Flipper.LOGGER.info(e.getClass());
        }
        return new JsonObject();
    }
}
