package me.wertik.portals.objects;

import me.wertik.portals.Main;
import me.wertik.portals.handlers.PortalHandler;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Portal {

    private PortalHandler portalHandler;

    private String leadingTo;
    private Location portalBlock;

    public Portal(String leadingTo, Location portalBlock) {
        this.leadingTo = leadingTo;
        this.portalBlock = portalBlock;

        portalHandler = Main.getInstance().getPortalHandler();
    }

    public void teleport(Player p) {
        MainPortal mainPortal = portalHandler.getMainPortal(leadingTo);

        portalHandler.addReturn(leadingTo, p.getLocation());

        p.teleport(mainPortal.getEnterLocation());
        p.sendMessage("§bTeleporting to §f" + mainPortal.getPortalName());
    }

    public String getLeadingTo() {
        return leadingTo;
    }

    public Block getPortalBlock() {
        return portalBlock.getBlock();
    }
}
