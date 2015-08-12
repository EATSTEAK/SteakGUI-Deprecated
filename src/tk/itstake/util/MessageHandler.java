package tk.itstake.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Created by bexco on 2015-07-26.
 * This class is Handling
 */
public class MessageHandler {

    public void sendConsoleMessage(String message) {
        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public boolean sendMessage(Player player, String message) {
        if(player.isOnline()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        } else {
            return false;
        }
    }
    public boolean sendMessage(CommandSender sender, String message) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
    }
}
