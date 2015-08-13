package tk.itstake.steakgui.editor;

import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.MenuItem;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.gui.GUIItem;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.itemtask.ItemTask;
import tk.itstake.steakgui.util.MenuFileHandler;
import tk.itstake.util.MessageHandler;

import java.util.Arrays;

/**
 * Created by ITSTAKE on 2015-08-12.
 */
public class ItemEditor implements Listener {
    public void show(Menu menu, Player player, String menuname, int slot) {
        String title = menu.getTitle();
        if(title.length() > 10) {
            title = ChatColor.stripColor(SteakGUI.convertMessage(menu.getTitle(), menu, player)).substring(0, 11) + "..";
        }
        ItemStack slotItem = menu.getItemArray().get(slot).getItemStack();
        ItemMenu setting = new ItemMenu(ChatColor.translateAlternateColorCodes('&', "&4수정:&c" + title), ItemMenu.Size.THREE_LINE, (JavaPlugin) Bukkit.getPluginManager().getPlugin("SteakGUI"));
        setting.setItem(9, new ItemEditorItem(menu, menuname, player, 0, slot, SteakGUI.convertMessage("&b손에서 아이템 가져오기"), Material.HOPPER, new String[]{SteakGUI.convertMessage("&b손에서 아이템을 가져옵니다.")}));
        setting.setItem(11, new ItemEditorItem(menu, menuname, player, 1, slot, SteakGUI.convertMessage("&b아이템 삭제"), Material.NETHER_BRICK_ITEM, new String[]{SteakGUI.convertMessage("&c아이템을 삭제합니다.")}));
        String permission = menu.getItemArray().get(slot).getPermission();
        if(permission.equals("")) {
            permission = "없음";
        }
        setting.setItem(13, new ItemEditorItem(menu, menuname, player, 2, slot, SteakGUI.convertMessage("&b펄미션 설정"), Material.REDSTONE_TORCH_ON, new String[]{SteakGUI.convertMessage("&c현재 설정된 펄미션:" + permission), SteakGUI.convertMessage("&b어떤 펄미션이 있어야 보일지 설정합니다."), SteakGUI.convertMessage("&3<클릭> 으로 펄미션 설정"), SteakGUI.convertMessage("&2<버리기 키> 로 설정된 펄미션 삭제")}));
        setting.setItem(15, new ItemEditorItem(menu, menuname, player, 3, slot, SteakGUI.convertMessage("&c작업 설정"), Material.REDSTONE_BLOCK, new String[]{SteakGUI.convertMessage("&c이 아이템의 작업을 설정합니다.")}));
        setting.setItem(17, new ItemEditorItem(menu, menuname, player, 4, slot, SteakGUI.convertMessage("&c돌아가기"), Material.FEATHER, new String[]{SteakGUI.convertMessage("&c이전 매뉴로 돌아갑니다.")}));
        setting.open(player);
    }

    class ItemEditorItem extends MenuItem {
        int t = 0;
        Menu menu = null;
        String menuname = null;
        Player player = null;
        int s = 0;
        public ItemEditorItem(Menu lmenu, String menuName, Player p, int type, int slot, String displayName, Material icon, String... lore) {
            super(displayName, new ItemStack(icon), lore);
            t = type;
            s = slot;
            menu = lmenu;
            menuname = menuName;
            player = p;
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            if(t == 0) {
                player.setMetadata("itemChange", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menuname, s}));
                new MessageHandler().sendMessage(player, "&b변경할 아이템을 클릭해 주세요. 만약 변경할 아이템이 없으시다면 빈 슬롯을 클릭하세요.");
                event.setWillClose(true);
            } else if(t == 1) {
                menu.removeItem(s);
                new MessageHandler().sendMessage(player, "&c아이템이 성공적으로 삭제되었습니다!");
            } else if(t == 2) {
                if(!(event.getClick().equals(ClickType.DROP) || event.getClick().equals(ClickType.CONTROL_DROP))) {
                    player.setMetadata("permSet", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menuname, s}));
                    new MessageHandler().sendMessage(player, "&b펄미션을 입력해 주세요. 취소하시려면 'cancel' 혹은 '취소' 를 입력하세요.");
                    event.setWillClose(true);
                } else {
                    new MessageHandler().sendMessage(player, "&c펄미션이 삭제되었습니다.");
                    menu.getItemArray().get(s).setPermission("");
                    new ItemEditor().show(menu, player, menuname, s);
                }
            } else if(t == 3) {
                new ItemTaskEditor().show(menu, player, menuname, s);
            } else if(t == 4) {
                new MenuEditor().show(menu, player, menuname);
            }
        }
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent e) {
        if(e.getPlayer().hasMetadata("itemChange")) {
            if(e.getItem() != null) {
                new MessageHandler().sendMessage(e.getPlayer(), "&b아이템을 성공적으로 가져왔습니다!");
                Menu menu = MenuFileHandler.loadMenu((String) ((Object[]) e.getPlayer().getMetadata("itemChange").get(0).value())[0]);
                ItemStack stack = e.getItem();
                ItemMeta itemmeta = stack.getItemMeta();
                if (itemmeta.getDisplayName() == null) {
                    itemmeta.setDisplayName(SteakGUI.lh.getLanguage("menu.nodisplayname"));
                }
                if (itemmeta.getLore() == null) {
                    itemmeta.setLore(Arrays.asList(SteakGUI.lh.getLanguage("menu.nolore").split("\n")));
                }
                stack.setItemMeta(itemmeta);
                menu.setItem((int) ((Object[]) e.getPlayer().getMetadata("itemChange").get(0).value())[1], new GUIItem(stack, "", new ItemTask(ItemTask.MESSAGE, new String[]{SteakGUI.lh.getLanguage("menu.noitemtask")})));
                MenuFileHandler.saveMenu(menu, (String) ((Object[]) e.getPlayer().getMetadata("itemChange").get(0).value())[0]);
                e.setCancelled(true);
                new ItemEditor().show(menu, e.getPlayer(),(String) ((Object[]) e.getPlayer().getMetadata("itemChange").get(0).value())[0], (int) ((Object[]) e.getPlayer().getMetadata("itemChange").get(0).value())[1]);
                e.getPlayer().removeMetadata("itemChange", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            } else {
                new MessageHandler().sendMessage(e.getPlayer(), "&c아이템 가져오기가 취소되었습니다!");
                e.setCancelled(true);
                e.getPlayer().removeMetadata("itemChange", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if(e.getPlayer().hasMetadata("permSet")) {
            if(!e.getMessage().equals("cancel") && !e.getMessage().equals("취소")) {
                new MessageHandler().sendMessage(e.getPlayer(), "&b펄미션이 " + e.getMessage() + " 로 설정되었습니다.");
                e.setCancelled(true);
                Menu targetmenu = MenuFileHandler.loadMenu((String)((Object[])e.getPlayer().getMetadata("permSet").get(0).value())[0]);
                targetmenu.getItemArray().get((int)((Object[])e.getPlayer().getMetadata("permSet").get(0).value())[1]).setPermission(e.getMessage());
                MenuFileHandler.saveMenu(targetmenu, (String) ((Object[]) e.getPlayer().getMetadata("permSet").get(0).value())[0]);
                new ItemEditor().show(targetmenu, e.getPlayer(),(String)((Object[])e.getPlayer().getMetadata("permSet").get(0).value())[0], (int)((Object[])e.getPlayer().getMetadata("permSet").get(0).value())[1]);
                e.getPlayer().removeMetadata("permSet", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            } else {
                new MessageHandler().sendMessage(e.getPlayer(), "&c취소되었습니다.");
                e.setCancelled(true);
                new MenuSetting().show(MenuFileHandler.loadMenu(e.getPlayer().getMetadata("permSet").get(0).asString()), e.getPlayer(), e.getPlayer().getMetadata("permSet").get(0).asString());
                e.getPlayer().removeMetadata("permSet", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            }
        }
    }
}
