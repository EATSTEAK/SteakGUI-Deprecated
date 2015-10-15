package tk.itstake.steakgui.gui;

import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.MenuItem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.itemtask.ItemTask;

import java.util.ArrayList;

/**
 * Created by bexco on 2015-07-26.
 */
public class GUIItem {
    ItemStack ITEM = null;
    String PERMISSION = null;
    ArrayList<ItemTask> TASK = null;
    Menu MENU = null;
    public GUIItem(ItemStack item, String permission, ItemTask task) {
        ITEM = item;
        PERMISSION = permission;
        TASK = new ArrayList<>();
        TASK.add(task);
    }
    public GUIItem(ItemStack item, String permission, ArrayList<ItemTask> task) {
        ITEM = item;
        PERMISSION = permission;
        TASK = task;
    }

    public MenuItem getMenuItem(Menu menu, Player player) {
        MENU = menu;
        String displayname = null;
        if(ITEM.getItemMeta().hasDisplayName()) {
            displayname = SteakGUI.convertMessage(ITEM.getItemMeta().getDisplayName(), menu, player);
        }
        String[] lore = null;
        if(ITEM.getItemMeta().hasLore()) {
            Object[] objarray = ITEM.getItemMeta().getLore().toArray();
            lore = new String[objarray.length];
            int i = 0;
            for(Object obj:objarray) {
                lore[i] = SteakGUI.convertMessage((String)obj, menu, player);
                i++;
            }
        }
        CustomMenuItem mi = new CustomMenuItem(displayname, ITEM, lore);
        return mi;
    }

    public String getPermission() {
        return PERMISSION;
    }

    public void setPermission(String s) { PERMISSION = s; }

    public ItemStack getItemStack() {
        return ITEM;
    }

    public void setItemStack(ItemStack stack) { ITEM = stack; }

    public ArrayList<ItemTask> getTasks() {
        return TASK;
    }

    public ItemTask getTask(int index) {
        return TASK.get(index);
    }

    public void addTask(ItemTask task) {
        TASK.add(task);
    }

    public void delTask(int index) {
        TASK.remove(index);
    }

    public void setTask(int index, ItemTask task) {
        if(index >= TASK.size()) {
            TASK.add(task);
        } else {
            TASK.set(index, task);
        }
    }

    class CustomMenuItem extends MenuItem {

        public CustomMenuItem(String displayName, ItemStack icon, String... lore) {
            super(displayName, icon, lore);
        }

        @Override
         public void onItemClick(ItemClickEvent event) {
            for(ItemTask task:TASK) {
                try {
                    task.runTask(event, MENU);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            MENU.open(event.getPlayer());
        }

        @Override
        public ItemStack getFinalIcon(Player player) {
            ItemStack finalIcon = super.getFinalIcon(player);
            return finalIcon;
        }
    }
}
