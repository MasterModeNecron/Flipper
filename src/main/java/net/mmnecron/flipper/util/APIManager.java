package net.mmnecron.flipper.util;

import net.mmnecron.flipper.Flipper;
import org.apache.logging.log4j.core.util.KeyValuePair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class APIManager {
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
        public String baseURL = "https://api.hypixel.net/skyblock/";
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
}
