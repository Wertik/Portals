package me.wertik.portals.handlers;

import me.wertik.portals.nbt.NBTEditor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PortalBlockHandler {

    public ItemStack createBlock(String leadingTo) {
        ItemStack portalBlock = new ItemStack(Material.SEA_LANTERN, 1);

        portalBlock = NBTEditor.writeNBT(portalBlock, "portalblock", leadingTo);

        ItemMeta itemMeta = portalBlock.getItemMeta();

        itemMeta.setDisplayName("§3Portal block leading to §f" + leadingTo);

        portalBlock.setItemMeta(itemMeta);

        return portalBlock;
    }

    public boolean isPortalBlock(ItemStack item) {
        return NBTEditor.hasNBTTag(item, "portalblock");
    }
}
