package me.wertik.portals.objects;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class MainPortal {

    private String portalName;
    private Location enterLocation;
    private Location portalBlock;

    public MainPortal(String portalName, Location enterLocation, Location portalBlock) {
        this.portalName = portalName;
        this.enterLocation = enterLocation;
        this.portalBlock = portalBlock;
    }

    public String getPortalName() {
        return portalName;
    }

    public Location getEnterLocation() {
        return enterLocation;
    }

    public Block getPortalBlock() {
        return portalBlock.getBlock();
    }

    public void rename(String portalName) {
        this.portalName = portalName;
    }

    public void setEnterLocation(Location enterLocation) {
        this.enterLocation = enterLocation;
    }

    public void setPortalBlock(Block portalBlock) {
        this.portalBlock = portalBlock.getLocation();
    }
}
