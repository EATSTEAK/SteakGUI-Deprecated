package tk.itstake.steakgui.gui;


import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.util.MessageHandler;

import java.util.HashMap;

/**
 * Created by bexco on 2015-07-26.
 */
public class Menu {
    String TITLE = "";
    String NAME = "";
    int SIZE = 9;
    HashMap<Integer, GUIItem> ITEM_ARRAY = null;
    JavaPlugin PLUGIN = null;
    public Menu(String name, String title, int size, HashMap<Integer, GUIItem> itemarray) {
        PLUGIN = (JavaPlugin)SteakGUI.p;
        TITLE = title;
        SIZE = size;
        ITEM_ARRAY = itemarray;
        NAME = name;
    }

    public Menu(String name, String title, HashMap<Integer, GUIItem> itemarray) {
        PLUGIN = (JavaPlugin)SteakGUI.p;
        TITLE = title;
        SIZE = itemarray.size();
        ITEM_ARRAY = itemarray;
        NAME = name;
    }

    public void setTitle(String title) {
        TITLE = title;
    }

    public void setSize(int size) {
        SIZE = size;
    }

    public void setItem(int slot, GUIItem item) {
        if(ITEM_ARRAY != null) {
            ITEM_ARRAY.put(slot, item);
        }
    }

    public boolean hasItem(int slot) {
        if(ITEM_ARRAY != null && ITEM_ARRAY.containsKey(slot) && ITEM_ARRAY.get(slot) != null) {
            return true;
        } else {
            return false;
        }
    }

    public void removeItem(int slot) {
        if(ITEM_ARRAY != null && ITEM_ARRAY.containsKey(slot)) {
            ITEM_ARRAY.remove(slot);
        }
    }

    public void setItemArray(HashMap<Integer, GUIItem> itemarray) {
        ITEM_ARRAY = itemarray;
    }

    public void open(final Player player) {
        final ItemMenu menu;
            menu = new ItemMenu(SteakGUI.convertMessage(TITLE, this, player), ItemMenu.Size.fit(Integer.parseInt(SteakGUI.convertMessage(SIZE + "", this, player))), PLUGIN);
            for(Integer key:ITEM_ARRAY.keySet()) {
                if(canInclude(player, ITEM_ARRAY.get(key))) {
                    if(key < ItemMenu.Size.fit(Integer.parseInt(SteakGUI.convertMessage(SIZE + "", this, player))).getSize()) {
                        menu.setItem(key, ITEM_ARRAY.get(key).getMenuItem(this, player));
                    }
                }
            }
            menu.open(player);
    }

    public void update(final Player player) {
        final ItemMenu menu = new ItemMenu(SteakGUI.convertMessage(TITLE, this, player), ItemMenu.Size.fit(SIZE), PLUGIN);
        for(Integer key:ITEM_ARRAY.keySet()) {
            if(canInclude(player, ITEM_ARRAY.get(key))) {
                menu.setItem(key, ITEM_ARRAY.get(key).getMenuItem(this, player));
            }
        }
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(SteakGUI.p, new Runnable() {
            @Override
            public void run() {
                menu.update(player);
            }
        }, 2);
    }


    public boolean canInclude(Player player, GUIItem item) {
        if(item.getPermission().equals("") || player.hasPermission(item.getPermission())) {
            return true;
        } else {
            return false;
        }
    }

    public String getTitle() {
        return TITLE;
    }

    public int getSize() {
        return SIZE;
    }

    public HashMap<Integer, GUIItem> getItemArray() {
        return ITEM_ARRAY;
    }

    public String getName() { return NAME; }

}
