package tk.itstake.steakgui.editor.taskeditor;

import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.MenuItem;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.editor.ItemTaskEditor;
import tk.itstake.steakgui.editor.NewTaskSelector;
import tk.itstake.steakgui.gui.GUIItem;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.itemtask.ItemTask;
import tk.itstake.steakgui.util.MenuFileHandler;
import tk.itstake.util.MessageHandler;

/**
 * Created by ITSTAKE on 2015-08-12.
 */
public class IfTaskEditor {
    public void show(Menu menu, Player player, int slot, int task) {
        String title = menu.getTitle();
        if(title.length() > 10) {
            title = ChatColor.stripColor(SteakGUI.convertMessage(menu.getTitle(), menu, player)).substring(0, 11) + "";
        }
        GUIItem slotItem = menu.getItemArray().get(slot);
        ItemTask edittask = slotItem.getTask(task);
        ItemMenu setting = new ItemMenu(ChatColor.translateAlternateColorCodes('&', "&4����:&c" + title), ItemMenu.Size.TWO_LINE, (JavaPlugin) Bukkit.getPluginManager().getPlugin("SteakGUI"));
        setting.setItem(0, new ItemTaskItem(menu, player, task, 0, 1,  slot, SteakGUI.convertMessage("&cIF���� GUI ������ ���� ������ ������ �����Դϴ�."), Material.PAPER, new String[]{SteakGUI.convertMessage("&b�� �������� Ŭ���ϸ� �ߴ� ��ũ�� �̿��Ͽ� �ڼ��� �˾ƺ�����.")}));
        setting.setItem(9, new ItemTaskItem(menu, player, task, 1, 1,  slot, SteakGUI.convertMessage("&b�۾� ���� ����"), Material.ANVIL, new String[]{SteakGUI.convertMessage("&b�۾� ������ ���� �մϴ�.")}));
        setting.setItem(10, new ItemTaskItem(menu, player, task, 2, 1,  slot, SteakGUI.convertMessage("&b�۾� ����"), Material.NETHER_BRICK_ITEM, new String[]{SteakGUI.convertMessage("&b�۾��� �����մϴ�.")}));
        setting.setItem(11, new ItemTaskItem(menu, player, task, 99, 1, slot, SteakGUI.convertMessage("&c���ư���"), Material.FEATHER, new String[]{SteakGUI.convertMessage("&c���� �Ŵ��� ���ư��ϴ�.")}));
        setting.open(player);
    }

    class ItemTaskItem extends MenuItem {
        int t = 0;
        int task = 0;
        int slot = 0;
        Menu menu = null;
        Player player = null;
        public ItemTaskItem(Menu lmenu, Player p, int ltask, int type, int amount, int s, String displayName, Material icon, String... lore) {
            super(displayName, new ItemStack(icon, amount), lore);
            slot = s;
            t = type;
            task = ltask;
            menu = lmenu;
            player = p;
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            ItemTask editTask = menu.getItemArray().get(slot).getTask(task);
            if(t == 0) {
                new MessageHandler().sendMessage(event.getPlayer(), "&ahttp://wiki.itstake.tk/index.php?title=SteakGUI/�۾�");
                event.setWillClose(true);
            } else if(t == 1) {
                new NewTaskSelector().show(menu, player, slot, task);
            } else if(t == 2) {
                menu.getItemArray().get(slot).delTask(task);
                new ItemTaskEditor().show(menu, player, slot);
            } else {
                new ItemTaskEditor().show(menu, player, slot);
            }
        }
    }
}
