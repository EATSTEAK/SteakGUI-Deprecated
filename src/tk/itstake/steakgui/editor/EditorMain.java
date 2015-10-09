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
import sun.plugin2.message.Message;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.itemtask.ItemTask;
import tk.itstake.util.MessageHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ITSTAKE on 2015-08-08.
 */
public class EditorMain {
    public void show(Menu menu, Player p) {
        String title = menu.getTitle();
        if(title.length() > 10) {
            title = ChatColor.stripColor(SteakGUI.convertMessage(menu.getTitle(), menu, p)).substring(0, 11) + "";
        }
        ItemMenu editormain = new ItemMenu(ChatColor.translateAlternateColorCodes('&', "&4수정:&c" + title), ItemMenu.Size.THREE_LINE, (JavaPlugin)Bukkit.getPluginManager().getPlugin("SteakGUI"));
        ItemStack menusettingicon = new ItemStack(Material.CHEST, 1);
        String[] lorearray = new String[]{SteakGUI.convertMessage("&c매뉴 자체의 설정을 합니다."), SteakGUI.convertMessage("&c(매뉴 내부 아이템 설정 제외)")};
        EditorMainItem menusettingitem = new EditorMainItem(menu, p, 0, SteakGUI.convertMessage("&b매뉴 설정"), menusettingicon, lorearray);
        ItemStack menuitemicon = new ItemStack(Material.WORKBENCH, 1);
        String[] lorearray2 = new String[]{SteakGUI.convertMessage("&c매뉴 아이템 설정을 합니다.")};
        EditorMainItem menuitemedititem = new EditorMainItem(menu, p, 1, SteakGUI.convertMessage("&b매뉴 아이템 설정"), menuitemicon, lorearray2);
        editormain.setItem(12, menusettingitem);
        editormain.setItem(14, menuitemedititem);
        editormain.open(p);
    }

    class EditorMainItem extends MenuItem {
        int t = 0;
        Menu menu = null;
        Player player = null;
        public EditorMainItem(Menu lmenu, Player p, int type, String displayName, ItemStack icon, String... lore) {
            super(displayName, icon, lore);
            t = type;
            menu = lmenu;
            player = p;
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            if(t == 0) {
                new MenuSetting().show(menu, player);
            } else if(t == 1) {
                new MenuEditor().show(menu, player);
            } else {
                event.setWillClose(true);
            }
        }
    }
}
