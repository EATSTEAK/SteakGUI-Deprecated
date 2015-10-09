package tk.itstake.steakgui.editor.taskeditor;

import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.MenuItem;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
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
public class CommandTaskEditor implements Listener {
    public void show(Menu menu, Player player, int slot, int task) {
        String title = menu.getTitle();
        if(title.length() > 10) {
            title = ChatColor.stripColor(SteakGUI.convertMessage(menu.getTitle(), menu, player)).substring(0, 11) + "";
        }
        GUIItem slotItem = menu.getItemArray().get(slot);
        ItemTask edittask = slotItem.getTask(task);
        ItemMenu setting = new ItemMenu(ChatColor.translateAlternateColorCodes('&', "&4수정:&c" + title), ItemMenu.Size.THREE_LINE, (JavaPlugin) Bukkit.getPluginManager().getPlugin("SteakGUI"));
        setting.setItem(0, new ItemTaskItem(menu, player, task, 0, 1,  slot, SteakGUI.convertMessage("&b플레이어 권한"), Material.BUCKET, new String[]{SteakGUI.convertMessage("&b플레이어 권한으로 명령어를 실행합니다.")}));
        setting.setItem(1, new ItemTaskItem(menu, player, task, 1, 1,  slot, SteakGUI.convertMessage("&b요피 권한"), Material.WATER_BUCKET, new String[]{SteakGUI.convertMessage("&b요피 권한으로 명령어를 실행합니다.")}));
        setting.setItem(2, new ItemTaskItem(menu, player, task, 2, 1,  slot, SteakGUI.convertMessage("&b콘솔 권한"), Material.LAVA_BUCKET, new String[]{SteakGUI.convertMessage("&b콘솔 권한으로 명령어를 실행합니다.")}));
        setting.setItem(9, new ItemTaskItem(menu, player, task, 3, 1,  slot, SteakGUI.convertMessage("&b명령어 입력"), Material.COMMAND, new String[]{SteakGUI.convertMessage("&b실행할 명령어를 입력합니다.")}));
        setting.setItem(18, new ItemTaskItem(menu, player, task, 4, 1,  slot, SteakGUI.convertMessage("&b작업 종류 변경"), Material.ANVIL, new String[]{SteakGUI.convertMessage("&b작업 종류를 변경합니다.")}));
        setting.setItem(19, new ItemTaskItem(menu, player, task, 5, 1,  slot, SteakGUI.convertMessage("&b작업 삭제"), Material.NETHER_BRICK_ITEM, new String[]{SteakGUI.convertMessage("&b작업을 삭제합니다.")}));
        setting.setItem(20, new ItemTaskItem(menu, player, task, 99, 1, slot, SteakGUI.convertMessage("&c돌아가기"), Material.FEATHER, new String[]{SteakGUI.convertMessage("&c이전 매뉴로 돌아갑니다.")}));
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
                new MessageHandler().sendMessage(event.getPlayer(), "&a권한이 'player' 로 변경되었습니다.");
                editTask.getData()[0] = "player";
                MenuFileHandler.saveMenu(menu);
            } else if(t == 1) {
                new MessageHandler().sendMessage(event.getPlayer(), "&a권한이 'op' 로 변경되었습니다.");
                editTask.getData()[0] = "op";
                MenuFileHandler.saveMenu(menu);
            } else if(t == 2) {
                new MessageHandler().sendMessage(event.getPlayer(), "&a권한이 'console' 로 변경되었습니다.");
                editTask.getData()[0] = "console";
                MenuFileHandler.saveMenu(menu);
            } else if(t == 3) {
                new MessageHandler().sendMessage(event.getPlayer(), "&a명령어를 입력하세요.(/ 필요)");
                player.setMetadata("cmdSet", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menu.getName(), slot, task}));
                event.setWillClose(true);
            } else if(t == 4) {
                new NewTaskSelector().show(menu, player, slot, task);
            } else if(t == 5) {
                menu.getItemArray().get(slot).delTask(task);
                new ItemTaskEditor().show(menu, player, slot);
            } else {
                new ItemTaskEditor().show(menu, player, slot);
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if(e.getPlayer().hasMetadata("cmdSet")) {
            new MessageHandler().sendMessage(e.getPlayer(), "&a" + e.getMessage() + " 명령어가 성공적으로 등록되었습니다!");
            Object[] metadata = (Object[]) e.getPlayer().getMetadata("cmdSet").get(0).value();
            Menu menu = MenuFileHandler.loadMenu((String)metadata[0]);
            menu.getItemArray().get((int)metadata[1]).getTask((int)metadata[2]).getData()[1] = e.getMessage();
            MenuFileHandler.saveMenu(menu);
            new CommandTaskEditor().show(menu, e.getPlayer(), (int) metadata[1], (int) metadata[2]);
            e.setCancelled(true);
            e.getPlayer().removeMetadata("cmdSet", Bukkit.getPluginManager().getPlugin("SteakGUI"));
        }
    }
}
