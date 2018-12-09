package me.wertik.portals.objects;

import org.bukkit.Location;
import org.bukkit.Material;

public class DecoBlock {

    private Material material;
    private Location offsetLocation;

    public DecoBlock(Material material, Location offsetLocation) {
        this.offsetLocation = offsetLocation;
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public Location getOffsetLocation() {
        return offsetLocation;
    }
}
