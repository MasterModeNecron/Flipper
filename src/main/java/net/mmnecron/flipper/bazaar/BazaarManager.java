package net.mmnecron.flipper.bazaar;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.mmnecron.flipper.Flipper;
import net.mmnecron.flipper.util.APIManager;
import net.mmnecron.flipper.util.ChatColourUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import java.util.Map;

public class BazaarManager {
    public static JsonObject instantBuy = new JsonObject();

    public static final Runnable getRequest = new Runnable() {
        @Override
        public void run() {
            UpdateBz();
        }
    };

    public static void UpdateBz() {
        JsonObject responseObject = GetBz();
        if (responseObject.get("success").getAsBoolean()){
            JsonObject products = (JsonObject) responseObject.get("products");
            for (Map.Entry<String, JsonElement> entry :products.entrySet()) {
                String key = entry.getKey();
                JsonArray buySummary = products.get(key).getAsJsonObject().get("buy_summary").getAsJsonArray();
                if (buySummary.size() > 0) {
                    int instantBuyPrice = buySummary.get(0).getAsJsonObject().get("pricePerUnit").getAsInt();
                    instantBuy.addProperty(key, instantBuyPrice);
                }
            }
            EntityPlayer player = Minecraft.getMinecraft().player;
            if (player != null) {
                player.sendMessage(new TextComponentString(ChatColourUtil.formatString("&r&6&l[Flipper] &r&2Updated Bazaar")));
            }
        }
    }

    public static JsonObject GetBz() {
        Gson gson = new Gson();
        APIManager.Request request = APIManager.getAnonymousRequest("/skyblock/bazaar");
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpRequest = new HttpGet(request.buildURL());
        httpRequest.addHeader("content-type", "application/json");
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpRequest);
            String responseBody = EntityUtils.toString(response.getEntity());
            return gson.fromJson(responseBody, JsonObject.class);
        } catch (Exception e) {
            Flipper.LOGGER.info("Bazaar Manager Error : " + e.getClass().getName());
        }
        return new JsonObject();
    }
}
