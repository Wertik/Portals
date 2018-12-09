package me.wertik.portals.commands;

import me.wertik.portals.Main;
import me.wertik.portals.handlers.PortalBlockHandler;
import me.wertik.portals.handlers.PortalHandler;
import me.wertik.portals.objects.MainPortal;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    private PortalHandler portalHandler;
    private PortalBlockHandler portalBlockHandler;

    public Commands() {
        portalHandler = Main.getInstance().getPortalHandler();
        portalBlockHandler = Main.getInstance().getPortalBlockHandler();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        Player p = (Player) sender;

        if (args[0].equalsIgnoreCase("create")) {
            String name = args[1];

            MainPortal mainPortal = new MainPortal(name, null, null);

            portalHandler.initializeMainPortal(mainPortal);
            p.sendMessage("§bCreated main portal §f" + name);
        } else if(args[0].equalsIgnoreCase("setEnter")) {
            String name = args[1];

            MainPortal mainPortal = portalHandler.getMainPortal(name);

            mainPortal.setEnterLocation(p.getLocation());
            p.sendMessage("§bLocation set.");
        } else if(args[0].equalsIgnoreCase("setBlock")) {
            String name = args[1];

            MainPortal mainPortal = portalHandler.getMainPortal(name);

            mainPortal.setPortalBlock(p.getTargetBlock(null, 100));
            p.sendMessage("§bPortal block set. :)");
        } else if (args[0].equalsIgnoreCase("getBlock")) {
            p.getInventory().addItem(portalBlockHandler.createBlock(args[1]));
            p.sendMessage("§bGiven block leading to §f" + args[1]);
        } else if (args[0].equalsIgnoreCase("startEditor")) {
            portalHandler.startEditor(p.getName(), portalHandler.getPortal(p.getTargetBlock(null, 100)));
            p.sendMessage("§bEditor started.");
        } else if (args[0].equalsIgnoreCase("stopEditor")) {
            portalHandler.stopEditor(p.getName());
            p.sendMessage("§bEditor stopped.");
        }


        return false;
    }
}
