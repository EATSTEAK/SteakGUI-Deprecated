/*
 * EditorMain.java
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

package tk.itstake.steakgui.menueditor;

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
