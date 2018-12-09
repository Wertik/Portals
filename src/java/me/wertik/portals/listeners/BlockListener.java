package me.wertik.portals.listeners;

import me.wertik.portals.Main;
import me.wertik.portals.handlers.PortalBlockHandler;
import me.wertik.portals.handlers.PortalHandler;
import me.wertik.portals.nbt.NBTEditor;
import me.wertik.portals.nbt.NBTUtils;
import me.wertik.portals.objects.MainPortal;
import me.wertik.portals.objects.Portal;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BlockListener implements Listener {

    private Main plugin;
    private PortalBlockHandler portalBlockHandler;
    private PortalHandler portalHandler;

    public BlockListener() {
        plugin = Main.getInstance();
        portalBlockHandler = plugin.getPortalBlockHandler();
        portalHandler = plugin.getPortalHandler();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {

        if (portalBlockHandler.isPortalBlock(e.getItemInHand())) {
            ItemStack portalItem = e.getItemInHand();

            Portal portal = new Portal(NBTUtils.strip(NBTEditor.getNBT(portalItem, "portalblock")), e.getBlockPlaced().getLocation());

            portalHandler.initializePortal(portal);

            if (!portalHandler.getEditor().containsKey(e.getPlayer().getName()))
                portalHandler.placeDecoBlocks(portal.getLeadingTo(), portal.getPortalBlock().getLocation());

            e.getPlayer().sendMessage("§bPortal initialized.");
        }

        if (portalHandler.getEditor().containsKey(e.getPlayer().getName())) {

            Portal portal = portalHandler.getEditor().get(e.getPlayer().getName());

            portalHandler.addBlock(portal.getLeadingTo(), portal.getPortalBlock().getLocation(), e.getBlockPlaced());
            e.getPlayer().sendMessage("§bAdding block.");
        }
    }

    // Todo add teleport check

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (portalHandler.isBlockPortal(e.getClickedBlock())) {
            Portal portal = portalHandler.getPortal(e.getClickedBlock());
            if (portalHandler.getMainPortal(portal.getLeadingTo()) != null)
                portal.teleport(e.getPlayer());
            else
                e.getPlayer().sendMessage("§cThat portal does not lead anywhere.");
        } else if (portalHandler.isMainPortalBlock(e.getClickedBlock())) {
            MainPortal mainPortal = portalHandler.getMainPortal(e.getClickedBlock());

            e.getPlayer().teleport(portalHandler.getReturn(mainPortal.getPortalName()));
            e.getPlayer().sendMessage("§bSending you back. :)");
        }
    }
}
