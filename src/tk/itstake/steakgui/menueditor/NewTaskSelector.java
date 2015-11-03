/*
 * NewTaskSelector.java
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
import org.json.simple.JSONArray;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.menueditor.taskeditor.*;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.itemtask.ItemTask;
import tk.itstake.steakgui.util.MenuFileHandler;

/**
 * Created by ITSTAKE on 2015-08-12.
 */
public class NewTaskSelector {
    public void show(Menu menu, Player player, int slot, int task) {
        String title = menu.getTitle();
        if(title.length() > 10) {
            title = ChatColor.stripColor(SteakGUI.convertMessage(menu.getTitle(), menu, player)).substring(0, 11) + "";
        }
        ItemMenu setting = new ItemMenu(ChatColor.translateAlternateColorCodes('&', "&4수정:&c" + title), ItemMenu.Size.FOUR_LINE, (JavaPlugin) Bukkit.getPluginManager().getPlugin("SteakGUI"));
        setting.setItem(11, new ItemTaskItem(menu, player, "command", task, slot, SteakGUI.convertMessage("&b명령어"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b명령어를 실행합니다.")}));
        setting.setItem(12, new ItemTaskItem(menu, player, "open", task, slot, SteakGUI.convertMessage("&b다른 매뉴 열기"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b다른 매뉴를 엽니다.")}));
        setting.setItem(13, new ItemTaskItem(menu, player, "buy", task, slot, SteakGUI.convertMessage("&b구매"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b아이템을 구매할 수 있도록 합니다.")}));
        setting.setItem(14, new ItemTaskItem(menu, player, "sell", task, slot, SteakGUI.convertMessage("&b판매"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b아이템을 판매할 수 있도록 합니다.")}));
        setting.setItem(15, new ItemTaskItem(menu, player, "message", task, slot, SteakGUI.convertMessage("&b메시지 보내기"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b메시지를 보냅니다.")}));
        setting.setItem(20, new ItemTaskItem(menu, player, "give", task, slot, SteakGUI.convertMessage("&b아이템 주기"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b아이템을 줍니다.")}));
        setting.setItem(21, new ItemTaskItem(menu, player, "sound", task, slot, SteakGUI.convertMessage("&b소리 내기"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b소리를 냅니다.")}));
        setting.setItem(22, new ItemTaskItem(menu, player, "broadcast", task, slot, SteakGUI.convertMessage("&b방송 하기"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b모두에게 메시지를 보냅니다.")}));
        setting.setItem(23, new ItemTaskItem(menu, player, "close", task, slot, SteakGUI.convertMessage("&b매뉴 닫기"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b해당 매뉴를 닫습니다.")}));
        setting.setItem(24, new ItemTaskItem(menu, player, "take", task, slot, SteakGUI.convertMessage("&b아이템 뺏기"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b아이템을 뺏습니다."), SteakGUI.convertMessage("&c조건문 에디터는 다음 버전에 지원될 예정입니다.")}));
        setting.setItem(31, new ItemTaskItem(menu, player, "update", task, slot, SteakGUI.convertMessage("&b매뉴 업데이트"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b매뉴를 새로운 내용으로 업데이트 합니다.")}));
        setting.open(player);
    }

    class ItemTaskItem extends MenuItem {
        int task = 0;
        int slot = 0;
        Menu menu = null;
        Player player = null;
        String tasktype;
        public ItemTaskItem(Menu lmenu, Player p, String tt, int tasknum, int s, String displayName, Material icon, String... lore) {
            super(displayName, new ItemStack(icon, tasknum), lore);
            slot = s;
            task = tasknum;
            menu = lmenu;
            player = p;
            tasktype = tt;
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            if(tasktype.equals(ItemTask.COMMAND)) {
                ItemTask editTask = new ItemTask(ItemTask.COMMAND, new String[]{"", ""});
                menu.getItemArray().get(slot).setTask(task, editTask);
                new CommandTaskEditor().show(menu, player, slot, task);
            } else if(tasktype.equals(ItemTask.OPEN_MENU)) {
                ItemTask editTask = new ItemTask(tasktype, new String[]{""});
                menu.getItemArray().get(slot).setTask(task, editTask);
                new OpenMenuTaskEditor().show(menu, player, slot, task);
            }
            else if(tasktype.equals(ItemTask.BUY)) {
                ItemTask editTask = new ItemTask(tasktype, new Object[]{"","","","","","",""});
                menu.getItemArray().get(slot).setTask(task, editTask);
                new BuyTaskEditor().show(menu, player, slot, task);
            }
            else if(tasktype.equals(ItemTask.SELL)) {
                ItemTask editTask = new ItemTask(tasktype, new Object[]{"","","","","",""});
                menu.getItemArray().get(slot).setTask(task, editTask);
                new SellTaskEditor().show(menu, player, slot, task);
            }
            else if(tasktype.equals(ItemTask.MESSAGE)) {
                ItemTask editTask = new ItemTask(tasktype, new String[]{""});
                menu.getItemArray().get(slot).setTask(task, editTask);
                new MessageTaskEditor().show(menu, player, slot, task);
            }
            else if(tasktype.equals(ItemTask.GIVE)) {
                ItemTask editTask = new ItemTask(tasktype, new Object[]{"",""});
                menu.getItemArray().get(slot).setTask(task, editTask);
                new GiveTaskEditor().show(menu, player, slot, task);
            }
            else if(tasktype.equals(ItemTask.SOUND)) {
                ItemTask editTask = new ItemTask(tasktype, new String[]{"","",""});
                menu.getItemArray().get(slot).setTask(task, editTask);
                new SoundTaskEditor().show(menu, player, slot, task);
            }
            else if(tasktype.equals(ItemTask.BROADCAST)) {
                ItemTask editTask = new ItemTask(tasktype, new String[]{""});
                menu.getItemArray().get(slot).setTask(task, editTask);
                new BroadcastTaskEditor().show(menu, player, slot, task);
            } else if(tasktype.equals(ItemTask.CLOSE)) {
                ItemTask editTask = new ItemTask(tasktype, new String[]{""});
                menu.getItemArray().get(slot).setTask(task, editTask);
                new CloseMenuTaskEditor().show(menu, player, slot, task);
            }
            else if(tasktype.equals(ItemTask.IF)) {
                ItemTask editTask = new ItemTask(tasktype, new Object[]{"","", new JSONArray(), new JSONArray()});
                menu.getItemArray().get(slot).setTask(task, editTask);
                new IfTaskEditor().show(menu, player, slot, task);
            } else if(tasktype.equals(ItemTask.TAKE)) {
                ItemTask editTask = new ItemTask(tasktype, new Object[]{"",""});
                menu.getItemArray().get(slot).setTask(task, editTask);
                new TakeTaskEditor().show(menu, player, slot, task);
            } else if(tasktype.equals(ItemTask.UPDATE)) {
                ItemTask editTask = new ItemTask(tasktype, new String[]{""});
                menu.getItemArray().get(slot).setTask(task, editTask);
                new UpdateMenuTaskEditor().show(menu, player, slot, task);
            }
            MenuFileHandler.saveMenu(menu);
        }
    }
}
