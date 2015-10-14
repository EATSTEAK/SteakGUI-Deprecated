package tk.itstake.steakgui.editor;

import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.MenuItem;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.util.MenuFileHandler;

/**
 * Created by ITSTAKE on 2015-08-12.
 */
public class MenuEditor {

    public void show(Menu menu, Player p) {
        String title = menu.getTitle();
        if(title.length() > 10) {
            title = ChatColor.stripColor(SteakGUI.convertMessage(menu.getTitle(), menu, p)).substring(0, 11) + "";
        }
        ItemMenu menuEditor = new ItemMenu(ChatColor.translateAlternateColorCodes('&', "&4수정:&c" + title), ItemMenu.Size.fit(menu.getSize()), (JavaPlugin) Bukkit.getPluginManager().getPlugin("SteakGUI"));
        for(Integer key:menu.getItemArray().keySet()) {
            ItemStack item = menu.getItemArray().get(key).getItemStack();
            String[] lorearray = new String[0];
            if(item.getItemMeta().getLore() != null) {
                lorearray = new String[item.getItemMeta().getLore().size()];
                int i = 0;
                for (String lore : item.getItemMeta().getLore()) {
                    lorearray[i] = SteakGUI.convertMessage(lore);
                    i++;
                }
            } else {

            }
            if(key < ItemMenu.Size.fit(menu.getSize()).getSize()) {
                menuEditor.setItem(key, new MenuEditorItem(menu, p, key, item.getItemMeta().getDisplayName(), item, lorearray));
            }
        }
        menuEditor.open(p);
    }


    class MenuEditorItem extends MenuItem {
        int t = 0;
        Menu menu = null;
        Player player = null;
        public MenuEditorItem(Menu lmenu,  Player p, int slot, String displayName, ItemStack icon, String... lore) {
            super(displayName, icon, lore);
            t = slot;
            menu = lmenu;
            player = p;
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            if(menu.hasItem(event.getEvent().getSlot())) {
                new ItemEditor().show(menu, player, event.getEvent().getSlot());
            }
        }
    }
}
