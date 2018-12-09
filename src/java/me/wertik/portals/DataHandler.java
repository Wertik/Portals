package me.wertik.portals;

import me.wertik.portals.objects.DecoBlock;
import me.wertik.portals.objects.MainPortal;
import me.wertik.portals.objects.Portal;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataHandler {

    private YamlConfiguration portalsYaml;
    private File portalsFile;
    private Main plugin;

    private ConfigurationSection portals;
    private ConfigurationSection mainPortals;
    private ConfigurationSection decoBlocks;

    public DataHandler() {
        plugin = Main.getInstance();
    }

    public void loadYamls() {
        portalsFile = new File(plugin.getDataFolder() + "/portals.yml");

        if (!portalsFile.exists()) {
            portalsYaml = YamlConfiguration.loadConfiguration(portalsFile);
            portalsYaml.options().copyDefaults(true);
            try {
                portalsYaml.save(portalsFile);
            } catch (IOException e) {
                plugin.getServer().getConsoleSender().sendMessage(plugin.getDescription().getPrefix() + "§cCould not save the file §f" + portalsFile.getName() + "§c, that's bad tho.");
            }
            plugin.getServer().getConsoleSender().sendMessage(plugin.getDescription().getPrefix() + "§aGenerated default §f" + portalsFile.getName());
        } else
            portalsYaml = YamlConfiguration.loadConfiguration(portalsFile);

        if (portalsYaml.contains("portals"))
            portals = portalsYaml.getConfigurationSection("portals");
        else
            portals = portalsYaml.createSection("portals");

        if (portalsYaml.contains("main-portals"))
            mainPortals = portalsYaml.getConfigurationSection("main-portals");
        else
            mainPortals = portalsYaml.createSection("main-portals");

        if (portalsYaml.contains("deco-blocks"))
            decoBlocks = portalsYaml.getConfigurationSection("deco-blocks");
        else
            decoBlocks = portalsYaml.createSection("deco-blocks");
    }

    public List<String> getMainPortalNames() {
        return new ArrayList<>(mainPortals.getKeys(false));
    }

    public List<String> getPortalIDs() {
        return new ArrayList<>(portals.getKeys(false));
    }

    public void saveDecoBlock(String leadingTo, int id, DecoBlock decoBlock) {

        Location offsetLocation = decoBlock.getOffsetLocation();
        String material = decoBlock.getMaterial().toString();

        if (!decoBlocks.contains(leadingTo))
            decoBlocks.createSection(leadingTo);

        ConfigurationSection section = decoBlocks.createSection(leadingTo + "." + String.valueOf(id));

        saveLocation(portalsYaml, "deco-blocks." + leadingTo + "." + String.valueOf(id), offsetLocation);
        section.set("Material", material);
    }

    public DecoBlock loadDecoBlock(String leadingTo, int id) {

        Location offsetLocation = loadLocation(portalsYaml, "deco-blocks." + leadingTo + "." + String.valueOf(id));

        String materialName = portalsYaml.getString("deco-blocks." + leadingTo + "." + String.valueOf(id) + ".Material");
        Material material = Material.valueOf(materialName);

        return new DecoBlock(material, offsetLocation);
    }

    public List<String> getDecoBlockNames(String leadingTo) {
        return new ArrayList<>(portalsYaml.getConfigurationSection("deco-blocks." + leadingTo).getKeys(false));
    }

    public List<DecoBlock> loadDecoBlocksForPortal(String leadingTo) {

        List<DecoBlock> decoBlocks = new ArrayList<>();

        for (String blockID : getDecoBlockNames(leadingTo)) {
            decoBlocks.add(loadDecoBlock(leadingTo, Integer.valueOf(blockID)));
        }

        return decoBlocks;
    }

    public List<String> getDecoBlockPortalNames() {
        return new ArrayList<>(portalsYaml.getConfigurationSection("deco-blocks").getKeys(false));
    }

    public Portal loadPortal(int id) {

        ConfigurationSection section = portals.getConfigurationSection(String.valueOf(id));

        String leadingTo = section.getString("leading-to");
        Location portalBlock = loadLocation(portalsYaml, "portals." + String.valueOf(id) + ".portal-block");

        return new Portal(leadingTo, portalBlock);
    }

    public void savePortal(int id, Portal portal) {

        ConfigurationSection section = portals.createSection(String.valueOf(id));

        section.set("leading-to", portal.getLeadingTo());
        section.createSection("portal-block");

        saveLocation(portalsYaml, "portals." + String.valueOf(id) + ".portal-block", portal.getPortalBlock().getLocation());
    }

    public MainPortal loadMainPortal(String name) {
        Location enterLocation = loadLocation(portalsYaml, "main-portals." + name + ".enter-location");
        Location portalBlock = loadLocation(portalsYaml, "main-portals." + name + ".portal-block");

        return new MainPortal(name, enterLocation, portalBlock);
    }

    public void saveMainPortal(MainPortal mainPortal) {

        String name = mainPortal.getPortalName();
        Location portalBlock = mainPortal.getPortalBlock().getLocation();
        Location enterLocation = mainPortal.getEnterLocation();

        ConfigurationSection section;
        if (!mainPortals.contains(name))
            section = mainPortals.createSection(name);
        else
            section = mainPortals.getConfigurationSection(name);

        section.createSection("portal-block");
        section.createSection("enter-location");

        saveLocation(portalsYaml, "main-portals." + name + ".portal-block", portalBlock);
        saveLocation(portalsYaml, "main-portals." + name + ".enter-location", enterLocation);
    }

    public void saveLocation(YamlConfiguration yaml, String path, Location location) {

        ConfigurationSection section = yaml.getConfigurationSection(path).createSection("Location");

        section.set("X", location.getX());
        section.set("Y", location.getY());
        section.set("Z", location.getZ());
        section.set("World", location.getWorld().getName());
    }

    public Location loadLocation(YamlConfiguration yaml, String path) {
        ConfigurationSection section = yaml.getConfigurationSection(path + ".Location");

        return new Location(plugin.getServer().getWorld(section.getString("World")), section.getDouble("X"), section.getDouble("Y"), section.getDouble("Z"));
    }

    public void savePortalYaml() {
        try {
            portalsYaml.options().copyDefaults(true);
            portalsYaml.save(portalsFile);
        } catch (IOException e) {
            plugin.getServer().getConsoleSender().sendMessage(plugin.getDescription().getPrefix() + "§cCould not save the file §f" + portalsFile.getName() + "§c, that's bad tho.");
        }
    }
}
