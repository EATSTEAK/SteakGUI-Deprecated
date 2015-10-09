package tk.itstake.steakgui;

import ninja.amp.ampmenus.MenuListener;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import tk.itstake.steakgui.command.MainCommand;
import tk.itstake.steakgui.editor.ItemEditor;
import tk.itstake.steakgui.editor.MenuSetting;
import tk.itstake.steakgui.editor.taskeditor.*;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.util.UpdateChecker;
import tk.itstake.steakgui.util.VaultHooker;
import tk.itstake.steakgui.variable.VariableConverter;
import tk.itstake.util.Blacklist;
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
        lh.languageLoad();
        mh.sendConsoleMessage(lh.getLanguage("console.onenable", new String[]{this.getDescription().getVersion()}));
        MenuListener.getInstance().register(this);
        new VaultHooker();
        Blacklist.init();
        getServer().getPluginManager().registerEvents(new MenuSetting(), this);
        getServer().getPluginManager().registerEvents(new ItemEditor(), this);
        getServer().getPluginManager().registerEvents(new CommandTaskEditor(), this);
        getServer().getPluginManager().registerEvents(new OpenMenuTaskEditor(), this);
        getServer().getPluginManager().registerEvents(new MessageTaskEditor(), this);
        getServer().getPluginManager().registerEvents(new SoundTaskEditor(), this);
        getServer().getPluginManager().registerEvents(new BroadcastTaskEditor(), this);
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

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e)
    {
        if (Blacklist.contains(e.getPlayer())) {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage(Blacklist.kick(e.getPlayer()));
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
