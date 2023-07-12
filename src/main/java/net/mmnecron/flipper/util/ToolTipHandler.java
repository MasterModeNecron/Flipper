package net.mmnecron.flipper.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.mmnecron.flipper.Auction.AuctionManager;
import net.mmnecron.flipper.Flipper;

@Mod.EventBusSubscriber()
public class ToolTipHandler {
    public static void TooltipEvent(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        NBTTagCompound itemNBT = itemStack.getTagCompound();
        if (itemNBT == null) { return; }
        if (itemNBT.hasKey("ExtraAttributes")) {
            String itemID = itemNBT.getCompoundTag("ExtraAttributes").getString("id");
            if (!itemID.equals("")) {
                event.getToolTip().add(itemID);
                if (AuctionManager.lowestBin.has(itemID)) {
                    event.getToolTip().add("Lowest Bin : " + AuctionManager.lowestBin.get(itemID).getAsInt());
                }
            }
        }
    }
}
