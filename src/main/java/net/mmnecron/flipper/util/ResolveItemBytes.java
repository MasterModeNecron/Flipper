package net.mmnecron.flipper.util;

import net.minecraft.nbt.NBTTagCompound;
import net.mmnecron.flipper.Flipper;

import java.util.Locale;

public class ResolveItemBytes {
    private NBTTagCompound compound;
    public ResolveItemBytes withNBTCompound(NBTTagCompound compound) {
        this.compound = compound;
        return this;
    }
    public String resolveInternalName() {
        String resolvedName;
        NBTTagCompound tag = compound.getTagList("i", 10).getCompoundTagAt(0);
        tag = tag.getCompoundTag("tag");
        resolvedName = tag.getCompoundTag("ExtraAttributes").getString("id");
        if (resolvedName.isEmpty()) { return null; }
        return resolvedName.toUpperCase(Locale.ROOT).replace(":", "-");
    }
}
