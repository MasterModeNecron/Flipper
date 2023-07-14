package net.mmnecron.flipper.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.mmnecron.flipper.Auction.AuctionManager;
import net.mmnecron.flipper.Flipper;
import net.mmnecron.flipper.bazaar.BazaarManager;

import java.util.ArrayList;
import java.util.Map;

@Mod.EventBusSubscriber()
public class ToolTipHandler {
    public static void TooltipEvent(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        NBTTagCompound itemNBT = itemStack.getTagCompound();
        if (itemNBT == null) { return; }
        if (itemNBT.hasKey("ExtraAttributes")) {
            NBTTagCompound attributes = itemNBT.getCompoundTag("ExtraAttributes");
            int recombed = attributes.getInteger("rarity_upgrades");
            int potatoCount = attributes.getInteger("hot_potato_count");
            String modifier = attributes.getString("modifier");
            int artOfWar = attributes.getInteger("art_of_war_count");
            int stars = attributes.getInteger("upgrade_level");
            int recomPrice = 0;
            int potatoBookPrice = 0;
            int modifierPrice = 0;
            int artOfWarPrice = 0;
            int starPrice = 0;
            int enchantPrices = 0;
            String itemID = attributes.getString("id");
            long itemPrice = 0;
            // calculating base item cost
            if (AuctionManager.lowestBin.has(itemID)) {
                itemPrice = AuctionManager.lowestBin.get(itemID).getAsLong();
            }
            // calculating enchantment costs
            if (attributes.hasKey("enchantments")) {
                NBTTagCompound enchantmentsTag = attributes.getCompoundTag("enchantments");
                if (enchantmentsTag.getKeySet().size() > 0) {
                    for (String key : enchantmentsTag.getKeySet()) {
                        String enchant = "ENCHANTMENT_" + key.toUpperCase() + "_" + enchantmentsTag.getInteger(key);
                        if (BazaarManager.instantBuy.has(enchant)) {
                            enchantPrices += BazaarManager.instantBuy.get(enchant).getAsInt();
                        }
                    }
                }
            }
            // calculating recom cost
            if (BazaarManager.instantBuy.has("RECOMBOBULATOR_3000")) {
                recomPrice = recombed * BazaarManager.instantBuy.get("RECOMBOBULATOR_3000").getAsInt();
            }
            // calculating hot potato cost
            if (BazaarManager.instantBuy.has("HOT_POTATO_BOOK")) {
                int potatoBookCount = Math.min(potatoCount, 10);
                potatoBookPrice += potatoBookCount * BazaarManager.instantBuy.get("HOT_POTATO_BOOK").getAsInt();
            }
            // calculating fuming potato cost
            if (BazaarManager.instantBuy.has("FUMING_POTATO_BOOK")) {
                int fumingBookCount = 0;
                if (potatoCount > 10) {
                    fumingBookCount = potatoCount-10;
                }
                potatoBookPrice += fumingBookCount * BazaarManager.instantBuy.get("FUMING_POTATO_BOOK").getAsInt();
            }
            // calculating reforge cost

            // calculating art of war cost
            if (BazaarManager.instantBuy.has("THE_ART_OF_WAR")) {
                artOfWarPrice = artOfWar * BazaarManager.instantBuy.get("THE_ART_OF_WAR").getAsInt();
            }
            // calculating stars cost
            if (APIManager.items.has(itemID)) {
                JsonObject item = (JsonObject) APIManager.items.get(itemID);
                if (item.has("upgrade_costs")) {
                    String essence = "ESSENCE_" + item.get("upgrade_costs").getAsJsonArray().get(0).getAsJsonArray().get(0).getAsJsonObject().get("essence_type");
                    essence = essence.replaceAll("\"", "");
                    int essenceCount = 0;
                    for (JsonElement entry : item.get("upgrade_costs").getAsJsonArray()) {
                        essenceCount += entry.getAsJsonArray().get(0).getAsJsonObject().get("amount").getAsInt();
                    }
                    if (BazaarManager.instantBuy.has(essence)) {
                        starPrice = essenceCount * BazaarManager.instantBuy.get(essence).getAsInt();
                    }
                }
            }
            if (!itemID.equals("")) {
                if (itemPrice > 0) {
                    long totalPrice = itemPrice + enchantPrices + recomPrice + potatoBookPrice + modifierPrice + artOfWarPrice + starPrice;
                    event.getToolTip().add(ChatColourUtil.formatString("&r&l&6Item Cost &r&2: " + totalPrice));
                }
                if (BazaarManager.instantBuy.has(itemID)) {
                    event.getToolTip().add(ChatColourUtil.formatString("&r&l&6Bazaar Instant Buy &r&2: " + BazaarManager.instantBuy.get(itemID)));
                }
                event.getToolTip().add(ChatColourUtil.formatString("&r&l&6Price Breakdown&r&2 :"));
                if (AuctionManager.lowestBin.has(itemID)) {
                    event.getToolTip().add(ChatColourUtil.formatString("&r&l&6Lowest BIN &r&2: " + AuctionManager.lowestBin.get(itemID)));
                }
                event.getToolTip().add(ChatColourUtil.formatString("&r&l&6Enchants &r&2: " + enchantPrices));
                event.getToolTip().add(ChatColourUtil.formatString("&r&l&6Recom &r&2: " + recomPrice));
                event.getToolTip().add(ChatColourUtil.formatString("&r&l&6Hot Potato/Fumings&r&2: " + potatoBookPrice));
                event.getToolTip().add(ChatColourUtil.formatString("&r&l&6Reforge &r&2: " + modifierPrice));
                event.getToolTip().add(ChatColourUtil.formatString("&r&l&6Art Of War/Peace &r&2: " + artOfWarPrice));
                event.getToolTip().add(ChatColourUtil.formatString("&r&l&6Stars &r&2: " + starPrice));
                event.getToolTip().add(ChatColourUtil.formatString("&r&8" + itemID));
            }
        }
    }
}
