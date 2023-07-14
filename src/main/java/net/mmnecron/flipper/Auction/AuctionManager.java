package net.mmnecron.flipper.Auction;

import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.mmnecron.flipper.Flipper;
import net.mmnecron.flipper.util.APIManager;
import net.mmnecron.flipper.util.APIManager.*;
import net.mmnecron.flipper.util.ChatColourUtil;
import net.mmnecron.flipper.util.ResolveItemBytes;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class AuctionManager {
    public static JsonObject lowestBin = new JsonObject();

    public static final Runnable getRequest = new Runnable() {
        @Override
        public void run() {
            UpdateAuctions();
        }
    };
    public static void UpdateAuctions() {
        JsonObject responseObject = getAH(0);
        if (responseObject.get("success").getAsBoolean()) {
            JsonArray currentPageAuctions = responseObject.get("auctions").getAsJsonArray();
            int totalPages = responseObject.get("totalPages").getAsInt();
            for (JsonElement auction : currentPageAuctions) {
                boolean isBin = auction.getAsJsonObject().get("bin").getAsBoolean();
                if (isBin) {
                    String itemBytes = auction.getAsJsonObject().get("item_bytes").getAsString();
                    NBTTagCompound itemNBT = null;
                    try {
                        itemNBT = CompressedStreamTools.readCompressed(new ByteArrayInputStream(Base64.getDecoder().decode(itemBytes)));
                    } catch (Exception e) {
                        Flipper.LOGGER.info(e.getClass());
                    }
                    if (itemNBT == null) {
                        continue;
                    }
                    String itemID = createResolveItemBytes()
                            .withNBTCompound(itemNBT)
                            .resolveInternalName();
                    long itemPrice = auction.getAsJsonObject().get("starting_bid").getAsLong();
                    if (lowestBin.has(itemID)) {
                        int currentLBIN = lowestBin.get(itemID).getAsInt();
                        if (itemPrice < currentLBIN) {
                            lowestBin.remove(itemID);
                            lowestBin.addProperty(itemID, itemPrice);
                        }
                    } else {
                        lowestBin.addProperty(itemID, itemPrice);
                    }
                }
            }

            for (int page = 0; page < totalPages; page++) {
                responseObject = getAH(page);
                if (responseObject.get("success").getAsBoolean()) {
                    currentPageAuctions = responseObject.get("auctions").getAsJsonArray();
                    totalPages = responseObject.get("totalPages").getAsInt();
                    for (JsonElement auction : currentPageAuctions) {
                        boolean isBin = auction.getAsJsonObject().get("bin").getAsBoolean();
                        if (isBin) {
                            String itemBytes = auction.getAsJsonObject().get("item_bytes").getAsString();
                            NBTTagCompound itemNBT = null;
                            try {
                                itemNBT = CompressedStreamTools.readCompressed(new ByteArrayInputStream(Base64.getDecoder().decode(itemBytes)));
                            } catch (Exception e) {
                                Flipper.LOGGER.info("Auction Manager Error : " + e.getClass().getName());
                            }
                            if (itemNBT == null) {
                                continue;
                            }
                            String itemID = createResolveItemBytes()
                                    .withNBTCompound(itemNBT)
                                    .resolveInternalName();
                            long itemPrice = auction.getAsJsonObject().get("starting_bid").getAsLong();
                            if (lowestBin.has(itemID)) {
                                int currentLBIN = lowestBin.get(itemID).getAsInt();
                                if (itemPrice < currentLBIN) {
                                    lowestBin.remove(itemID);
                                    lowestBin.addProperty(itemID, itemPrice);
                                }
                            } else {
                                lowestBin.addProperty(itemID, itemPrice);
                            }
                        }
                    }
                }
            }
            EntityPlayer player = Minecraft.getMinecraft().player;
            if (player != null) {
                player.sendMessage(new TextComponentString(ChatColourUtil.formatString("&r&6&l[Flipper] &r&2Updated BIN")));
            }
        }
    }

    public static JsonObject getAH(int page) {
        Gson gson = new Gson();
        Request request = APIManager.getAnonymousRequest("/skyblock/auctions");
        request.addRequestArgument("page", String.valueOf(page));
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

    public JsonObject getJsonFromItemBytes(String item_bytes) {
        try {
            NBTTagCompound tag = CompressedStreamTools.readCompressed(new ByteArrayInputStream(Base64.getDecoder().decode(item_bytes)));
            //System.out.println(tag.toString());
            return getJsonFromNBTEntry(tag.getTagList("i", 10).getCompoundTagAt(0));
        } catch (Exception e) {
            return null;
        }
    }

    public JsonObject getJsonFromNBTEntry(NBTTagCompound tag) {
        if (tag.getKeySet().size() == 0) return null;
        return null;
    }

    public static ResolveItemBytes createResolveItemBytes() {
        return new ResolveItemBytes();
    }
}