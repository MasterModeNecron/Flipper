package net.mmnecron.flipper;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.mmnecron.flipper.Auction.AuctionManager;
import net.mmnecron.flipper.commands.CommandUpdateLBIN;
import net.mmnecron.flipper.util.ToolTipHandler;
import org.apache.logging.log4j.Logger;

import java.rmi.registry.RegistryHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Mod(modid = Flipper.MODID, name = Flipper.NAME, version = Flipper.VERSION)
public class Flipper
{
    public static final String MODID = "flipper";
    public static final String NAME = "Flipper";
    public static final String VERSION = "0.0.1";

    public static String APIKEY;
    public static Logger LOGGER;

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LOGGER = event.getModLog();
    }

    @EventHandler
    public void init(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandUpdateLBIN());
    }

    public Flipper() {

        MinecraftForge.EVENT_BUS.register(this);
        ScheduledFuture<?> getHandler =
                scheduler.scheduleAtFixedRate(AuctionManager.getRequest, 0, 1, TimeUnit.MINUTES);
    }
    @SubscribeEvent
    public void ItemTooltipEvent(ItemTooltipEvent event) { ToolTipHandler.TooltipEvent(event); }
}
