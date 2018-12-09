package me.wertik.portals;

import me.wertik.portals.commands.Commands;
import me.wertik.portals.handlers.PortalBlockHandler;
import me.wertik.portals.handlers.PortalHandler;
import me.wertik.portals.listeners.BlockListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private ConfigLoader configLoader;
    private DataHandler dataHandler;
    private PortalHandler portalHandler;
    private PortalBlockHandler portalBlockHandler;

    public static Main getInstance() {return instance;}

    @Override
    public void onEnable() {
        info("§bEnabling portals. No joke this time.");
        info("§f-----------------");

        instance = this;
        configLoader = new ConfigLoader();
        dataHandler = new DataHandler();
        portalBlockHandler = new PortalBlockHandler();
        portalHandler = new PortalHandler();
        info("§aClasses registered");

        getCommand("portals").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        info("§aListeners and Commands registered");

        configLoader.loadYamls();
        dataHandler.loadYamls();
        info("§aYamls loaded");

        portalHandler.loadPortals();
        portalHandler.loadDecoBlocks();
        info("§aPortals loaded");

        info("§f-----------------");
        info("§bDone...");
    }

    @Override
    public void onDisable() {
        info("§bDisabling portals.");
        info("§f-----------------");

        portalHandler.savePortals();
        portalHandler.saveDecoBlocks();
        info("§aPortals saved");

        info("§f-----------------");
        info("§bDone...");
    }

    private void info(String msg) {
        getServer().getConsoleSender().sendMessage("§f[Portals] " + msg);
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    public PortalHandler getPortalHandler() {
        return portalHandler;
    }

    public PortalBlockHandler getPortalBlockHandler() {
        return portalBlockHandler;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }
}
