package net.gamingg.galaxyjail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 *
 * @author Tenkitsune, GamingG
 */
public class GalaxyJail extends JavaPlugin implements Listener
{
    List<UUID> frozenPlayers;

    @Override
    public void onEnable()
    {
        getLogger().info("Plugin Enabled!");
        
        frozenPlayers = new ArrayList<UUID>();
        
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(cmd.getName().equalsIgnoreCase("freeze") || cmd.getName().equalsIgnoreCase("jail"))
        {
            return onFreezeCommand(sender, cmd, label, args);
        }

        if(cmd.getName().equalsIgnoreCase("unfreeze") || cmd.getName().equalsIgnoreCase("unjail"))
        {
            return onUnfreezeCommand(sender, cmd, label, args);
        }
        
        if(cmd.getName().equalsIgnoreCase("jailcheck"))
        {
            return onJailcheckCommand(sender, cmd, label, args);
        }

        return false;
    }

    private boolean onFreezeCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!sender.hasPermission("jail.command.jail"))
            return false;
            
        if(args.length < 1)
            return false;

        Player player = getPlayerByName(args[0]);

        if(player == null)
        {
            sender.sendMessage("Invalid player specified");
            return false;
        }

        freezePlayer(player);

        return true;
    }

    private boolean onUnfreezeCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!sender.hasPermission("jail.command.unjail"))
            return false;
        
        if(args.length < 1)
            return false;

        Player player = getPlayerByName(args[0]);

        if(player == null)
        {
            sender.sendMessage("Invalid player specified");
            return false;
        }

        unfreezePlayer(player);

        return true;
    }
    
    private boolean onJailcheckCommand(CommandSender sender, Command cmd, String label, String[] args) 
    {
        if(!sender.hasPermission("jail.command.jailcheck"))
            return false;
        
        sender.sendMessage("Jailed players: " + frozenPlayers.toString());
        
        return true;
    }

    private Player getPlayerByName(String name)
    {
        return Bukkit.getServer().getPlayer(name);
    }


    private void freezePlayer(Player player) 
    {
        frozenPlayers.add(player.getUniqueId());
    }

    private void unfreezePlayer(Player player) 
    {
        frozenPlayers.remove(player.getUniqueId());
    }
    
    private boolean playerIsFrozen(Player player) 
    {
        return frozenPlayers.contains(player.getUniqueId());
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if(playerIsFrozen(event.getPlayer()))
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage("You are jailed, do not move!");
        }
    }


}
