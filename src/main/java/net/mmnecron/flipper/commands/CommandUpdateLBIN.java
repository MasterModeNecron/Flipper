package net.mmnecron.flipper.commands;

import com.google.common.collect.Lists;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.mmnecron.flipper.Auction.AuctionManager;
import net.mmnecron.flipper.Flipper;

import java.util.List;

public class CommandUpdateLBIN extends CommandBase {
    private final List<String> aliases = Lists.newArrayList(Flipper.MODID, "updatelbin");
    @Override
    public String getName() {
        return "updatelbin";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "updatelbin";
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        AuctionManager.UpdateAuctions();
        sender.sendMessage(new TextComponentString("Updated LBIN"));
    }
}
