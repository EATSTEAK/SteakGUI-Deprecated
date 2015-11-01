/*
 * ItemTaskEditor.java
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
import tk.itstake.steakgui.menueditor.taskeditor.*;
import tk.itstake.steakgui.gui.GUIItem;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.itemtask.ItemTask;

/**
 * Created by ITSTAKE on 2015-08-12.
 */
public class ItemTaskEditor {
    public void show(Menu menu, Player player, int s) {
        String title = menu.getTitle();
        if(title.length() > 10) {
            title = ChatColor.stripColor(SteakGUI.convertMessage(menu.getTitle(), menu, player)).substring(0, 11) + "";
        }
        GUIItem slotItem = menu.getItemArray().get(s);
        ItemMenu setting = new ItemMenu(ChatColor.translateAlternateColorCodes('&', "&4수정:&c" + title), ItemMenu.Size.fit(slotItem.getTasks().size()+9), (JavaPlugin) Bukkit.getPluginManager().getPlugin("SteakGUI"));
        int i = 0;
        for(ItemTask task:slotItem.getTasks()) {
            setting.setItem(i, new ItemTaskItem(menu,  player, i, s, SteakGUI.convertMessage("&b작업 " + i), Material.PISTON_BASE, new String[]{SteakGUI.convertMessage("&c작업 종류: &f" + task.getType())}));
            i++;
        }
        setting.setItem(i, new ItemTaskItem(menu, player, i, s, SteakGUI.convertMessage("&b작업 추가"), Material.PISTON_STICKY_BASE, new String[]{SteakGUI.convertMessage("&c작업을 추가합니다.")}));
        setting.setItem(setting.getSize().getSize()-1, new ItemTaskItem(menu, player, 999, s, SteakGUI.convertMessage("&c돌아가기"), Material.FEATHER, new String[]{SteakGUI.convertMessage("&c이전 매뉴로 돌아갑니다.")}));
        setting.open(player);
    }

    class ItemTaskItem extends MenuItem {
        int task = 0;
        int slot = 0;
        Menu menu = null;
        Player player = null;
        public ItemTaskItem(Menu lmenu, Player p, int tasknum, int s, String displayName, Material icon, String... lore) {
            super(displayName, new ItemStack(icon, tasknum), lore);
            slot = s;
            task = tasknum;
            menu = lmenu;
            player = p;
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            if(task == 999) {
                new ItemEditor().show(menu, player, slot);
            } else if(menu.getItemArray().get(slot).getTasks().size() <= task) {
                new NewTaskSelector().show(menu, player, slot, task);
            } else {
                ItemTask editTask = menu.getItemArray().get(slot).getTask(task);
                if(editTask.getType().equals(ItemTask.COMMAND)) {
                    new CommandTaskEditor().show(menu, player, slot, task);
                } else if(editTask.getType().equals(ItemTask.OPEN_MENU)) {
                    new OpenMenuTaskEditor().show(menu, player, slot, task);
                }
                else if(editTask.getType().equals(ItemTask.BUY)) {
                    new BuyTaskEditor().show(menu, player,slot, task);
                }
                else if(editTask.getType().equals(ItemTask.SELL)) {
                    new SellTaskEditor().show(menu, player, slot, task);
                }
                else if(editTask.getType().equals(ItemTask.MESSAGE)) {
                    new MessageTaskEditor().show(menu, player, slot, task);
                }
                else if(editTask.getType().equals(ItemTask.GIVE)) {
                    new GiveTaskEditor().show(menu, player, slot, task);
                }
                else if(editTask.getType().equals(ItemTask.SOUND)) {
                    new SoundTaskEditor().show(menu, player, slot, task);
                }
                else if(editTask.getType().equals(ItemTask.BROADCAST)) {
                    new BroadcastTaskEditor().show(menu, player, slot, task);
                }
                else if(editTask.getType().equals(ItemTask.IF)) {
                    new IfTaskEditor().show(menu, player, slot, task);
                }

            }
        }
    }
}
