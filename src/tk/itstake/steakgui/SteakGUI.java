/*
 * SteakGUI.java
 *
 * Copyright (c) 2015 ITSTAKE
 *
 * This program is free software: you can redistribute it and/or modify
 *
 * it under the terms of the GNU General Public License as published by
 *
 * the Free Software Foundation, either version 3 of the License, or
 *
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package tk.itstake.steakgui;

import ninja.amp.ampmenus.MenuListener;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import tk.itstake.steakgui.command.MainCommand;
import tk.itstake.steakgui.menueditor.ItemEditor;
import tk.itstake.steakgui.menueditor.ItemStackEditor;
import tk.itstake.steakgui.menueditor.MenuSetting;
import tk.itstake.steakgui.menueditor.taskeditor.*;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.util.UpdateChecker;
import tk.itstake.steakgui.util.VaultHooker;
import tk.itstake.steakgui.variable.VariableConverter;
import tk.itstake.util.ConfigHandler;
import tk.itstake.util.LanguageHandler;
import tk.itstake.util.MessageHandler;

import java.util.ArrayList;

/**
 * Created by bexco on 2015-07-24.
 */
public class SteakGUI extends JavaPlugin implements Listener {

    MessageHandler mh = new MessageHandler();
    private ArrayList<String> pluginList = new ArrayList<>();
    public void addToPluginList(String name) {
        pluginList.add(name);
    }

    public ArrayList<String> getPluginList() {
        return pluginList;
    }
    public static LanguageHandler lh = new LanguageHandler();
    public static Plugin p = null;
    @Override
    public void onEnable() {
        p = this;
        if(!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }
        ConfigHandler.loadConfig();
        lh.languageLoad();
        mh.sendConsoleMessage(lh.getLanguage("console.onenable", new String[]{this.getDescription().getVersion()}));
        MenuListener.getInstance().register(this);
        VaultHooker hooker = new VaultHooker();
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new MenuSetting(), this);
        getServer().getPluginManager().registerEvents(new ItemEditor(), this);
        getServer().getPluginManager().registerEvents(new CommandTaskEditor(), this);
        getServer().getPluginManager().registerEvents(new OpenMenuTaskEditor(), this);
        getServer().getPluginManager().registerEvents(new MessageTaskEditor(), this);
        getServer().getPluginManager().registerEvents(new SoundTaskEditor(), this);
        getServer().getPluginManager().registerEvents(new BroadcastTaskEditor(), this);
        getServer().getPluginManager().registerEvents(new ItemStackEditor(), this);
        getServer().getPluginManager().registerEvents(new BuyTaskEditor(), this);
        getServer().getPluginManager().registerEvents(new SellTaskEditor(), this);
        getServer().getPluginManager().registerEvents(new GiveTaskEditor(), this);
        getServer().getPluginManager().registerEvents(new TakeTaskEditor(), this);
        UpdateChecker update = new UpdateChecker();
        update.updateCheck();
        getServer().getPluginManager().registerEvents(update, this);
    }

    @Override
    public void onDisable() {
        mh.sendConsoleMessage(lh.getLanguage("console.ondisable", new String[]{this.getDescription().getVersion()}));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return MainCommand.runCmd(sender, cmd, label, args);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if(!event.isCancelled()) {
            if (event.getPlayer().hasMetadata("SGCmd")) {
                event.getPlayer().performCommand(event.getMessage().substring(1));
                event.getPlayer().removeMetadata("SGCmd", this);
            }
        }
    }

    public static String convertMessage(String message) {
        return convertMessage(message, null, null);
    }
    public static String convertMessage(String message, Menu menu, Player player) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        if(menu != null && player != null){
            message = VariableConverter.convert(message, menu, player);
        }
        return message;
    }

}
