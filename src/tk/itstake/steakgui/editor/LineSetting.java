package tk.itstake.steakgui.editor;

import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.MenuItem;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.util.MenuFileHandler;
import tk.itstake.util.MessageHandler;

/**
 * Created by ITSTAKE on 2015-08-12.
 */
public class LineSetting {
    public void show(Menu menu, Player player, String menuname) {
        String title = menu.getTitle();
        if(title.length() > 10) {
            title = ChatColor.stripColor(SteakGUI.convertMessage(menu.getTitle(), menu, player)).substring(0, 11) + "..";
        }
        ItemMenu setting = new ItemMenu(ChatColor.translateAlternateColorCodes('&', "&4수정:&c" + title), ItemMenu.Size.THREE_LINE, (JavaPlugin) Bukkit.getPluginManager().getPlugin("SteakGUI"));
        setting.setItem(10, new MenuSettingItem(menu, menuname, player, 1, 1, SteakGUI.convertMessage("&b1줄"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b매뉴의 줄 수 1줄으로 설정합니다."), SteakGUI.convertMessage("&c현재 줄 수:" + menu.getSize()/9)}));
        setting.setItem(11, new MenuSettingItem(menu, menuname, player, 2, 2, SteakGUI.convertMessage("&b2줄"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b매뉴의 줄 수 2줄으로 설정합니다."), SteakGUI.convertMessage("&c현재 줄 수:" + menu.getSize()/9)}));
        setting.setItem(12, new MenuSettingItem(menu, menuname, player, 3, 3, SteakGUI.convertMessage("&b3줄"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b매뉴의 줄 수 3줄으로 설정합니다."), SteakGUI.convertMessage("&c현재 줄 수:" + menu.getSize()/9)}));
        setting.setItem(13, new MenuSettingItem(menu, menuname, player, 4, 4, SteakGUI.convertMessage("&b4줄"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b매뉴의 줄 수 4줄으로 설정합니다."), SteakGUI.convertMessage("&c현재 줄 수:" + menu.getSize()/9)}));
        setting.setItem(14, new MenuSettingItem(menu, menuname, player, 5, 5, SteakGUI.convertMessage("&b5줄"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b매뉴의 줄 수 5줄으로 설정합니다."), SteakGUI.convertMessage("&c현재 줄 수:" + menu.getSize()/9)}));
        setting.setItem(15, new MenuSettingItem(menu, menuname, player, 6, 6, SteakGUI.convertMessage("&b6줄"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b매뉴의 줄 수 6줄으로 설정합니다."), SteakGUI.convertMessage("&c현재 줄 수:" + menu.getSize()/9)}));
        setting.setItem(16, new MenuSettingItem(menu, menuname, player, 7, 1, SteakGUI.convertMessage("&c돌아가기"), Material.FEATHER, new String[]{SteakGUI.convertMessage("&c이전 매뉴로 돌아갑니다.")}));
        setting.open(player);
    }

    class MenuSettingItem extends MenuItem {
        int t = 0;
        Menu menu = null;
        String menuname = null;
        Player player = null;
        public MenuSettingItem(Menu lmenu, String menuName, Player p, int type, int amount, String displayName, Material icon, String... lore) {
            super(displayName, new ItemStack(icon, amount), lore);
            t = type;
            menu = lmenu;
            menuname = menuName;
            player = p;
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            if(t != 7) {
                menu.setSize(t * 9);
                MenuFileHandler.saveMenu(menu, menuname);
                new LineSetting().show(menu, player, menuname);
            } else {
                new MenuSetting().show(menu, player, menuname);
            }
        }
    }
}
