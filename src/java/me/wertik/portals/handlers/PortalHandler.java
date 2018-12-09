package me.wertik.portals.handlers;

import me.wertik.portals.DataHandler;
import me.wertik.portals.Main;
import me.wertik.portals.objects.DecoBlock;
import me.wertik.portals.objects.MainPortal;
import me.wertik.portals.objects.Portal;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PortalHandler {

    private List<Portal> activePortals;
    private List<MainPortal> mainPortals;
    //               Playername, Portal
    private HashMap<String, Portal> editor;
    //               leadingTo, Material, Location
    private HashMap<String, List<DecoBlock>> decoBlocks;
    private HashMap<String, Location> returnLocations;

    private DataHandler dataHandler;
    private Main plugin;

    public void addReturn(String name, Location location) {
        returnLocations.put(name, location);
    }

    public Location getReturn(String name) {
        return returnLocations.get(name);
    }

    public PortalHandler() {
        activePortals = new ArrayList<>();
        mainPortals = new ArrayList<>();
        editor = new HashMap<>();
        decoBlocks = new HashMap<>();
        returnLocations = new HashMap<>();

        plugin = Main.getInstance();
        dataHandler = plugin.getDataHandler();
    }

    public boolean isMainPortalBlock(Block block) {
        for (MainPortal mainPortal : mainPortals) {
            if (mainPortal.getPortalBlock().equals(block))
                return true;
        }
        return false;
    }

    public MainPortal getMainPortal(Block block) {
        for (MainPortal mainPortal : mainPortals) {
            if (mainPortal.getPortalBlock().equals(block))
                return mainPortal;
        }
        return null;
    }

    public void addBlock(String name, Location portalLocation, Block block) {

        Location blockLocation = block.getLocation();
        Material blockMaterial = block.getType();

        Location location = blockLocation.subtract(portalLocation);

        List<DecoBlock> blocks;

        if (decoBlocks.containsKey(name))
            blocks = decoBlocks.get(name);
        else
            blocks = new ArrayList<>();

        blocks.add(new DecoBlock(blockMaterial, location));

        decoBlocks.put(name, blocks);
    }

    public void placeDecoBlocks(String name, Location portalLocation) {

        if (getDecoBlocks(name) != null)
            for (DecoBlock decoBlock : getDecoBlocks(name)) {

                Location offset = decoBlock.getOffsetLocation();

                Location finalLocation = new Location(portalLocation.getWorld(), portalLocation.getX() + offset.getX(), portalLocation.getY() + offset.getY(), portalLocation.getZ() + offset.getZ());

                finalLocation.getWorld().getBlockAt(finalLocation).setType(decoBlock.getMaterial());
            }
    }

    public List<DecoBlock> getDecoBlocks(String name) {
        return decoBlocks.get(name);
    }

    public void loadPortals() {
        // Main portals
        mainPortals.clear();

        if (dataHandler.getMainPortalNames() != null)
            for (String portalName : dataHandler.getMainPortalNames()) {
                MainPortal mainPortal = dataHandler.loadMainPortal(portalName);
                mainPortals.add(mainPortal);
            }

        // Portals
        activePortals.clear();

        if (dataHandler.getPortalIDs() != null)
            for (String portalID : dataHandler.getPortalIDs()) {
                int id = Integer.valueOf(portalID);
                Portal portal = dataHandler.loadPortal(id);
                activePortals.add(portal);
            }
    }

    public void loadDecoBlocks() {
        // ;)
        decoBlocks.clear();

        for (String blockName : dataHandler.getDecoBlockPortalNames()) {
            decoBlocks.put(blockName, dataHandler.loadDecoBlocksForPortal(blockName));
        }
    }

    public void saveDecoBlocks() {
        for (String leadingTo : decoBlocks.keySet()) {
            int i = 0;
            for (DecoBlock decoBlock : decoBlocks.get(leadingTo)) {
                dataHandler.saveDecoBlock(leadingTo, i, decoBlock);
                i++;
            }
        }

        dataHandler.savePortalYaml();
    }

    public void savePortals() {
        //  Main portals

        for (MainPortal mainPortal : mainPortals) {
            dataHandler.saveMainPortal(mainPortal);
        }

        // Portals

        int i = 0;
        for (Portal portal : activePortals) {
            dataHandler.savePortal(i, portal);
            i++;
        }

        dataHandler.savePortalYaml();
    }

    public void initializeMainPortal(MainPortal mainPortal) {
        mainPortals.add(mainPortal);
    }

    public MainPortal getMainPortal(String name) {
        for (MainPortal mainPortal : mainPortals) {
            if (mainPortal.getPortalName().equals(name))
                return mainPortal;
        }
        return null;
    }

    public void initializePortal(Portal portal) {
        activePortals.add(portal);
    }

    public Portal getPortal(Block block) {
        for (Portal portal : activePortals) {
            if (portal.getPortalBlock().equals(block))
                return portal;
        }
        return null;
    }

    public boolean isBlockPortal(Block block) {
        for (Portal portal : activePortals) {
            if (portal.getPortalBlock().equals(block))
                return true;
        }
        return false;
    }

    // Portal == leading to
    public void startEditor(String playerName, Portal portal) {
        editor.put(playerName, portal);
    }

    public void stopEditor(String playerName) {
        editor.remove(playerName);
    }

    public HashMap<String, Portal> getEditor() {
        return editor;
    }
}
