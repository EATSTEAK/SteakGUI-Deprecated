package tk.itstake.steakgui.editor;

import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.MenuItem;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.editor.taskeditor.*;
import tk.itstake.steakgui.gui.GUIItem;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.itemtask.ItemTask;
import tk.itstake.steakgui.util.MenuFileHandler;
import tk.itstake.util.MessageHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ITSTAKE on 2015-10-11.
 */
public class ItemStackEditor implements Listener {
    public void show(Menu menu, Player player, int s) {
        String title = menu.getTitle();
        if(title.length() > 10) {
            title = ChatColor.stripColor(SteakGUI.convertMessage(menu.getTitle(), menu, player)).substring(0, 11) + "";
        }
        GUIItem slotItem = menu.getItemArray().get(s);
        ItemStack stack = slotItem.getItemStack();
        int menuSize = 9;
        List<String> lores = null;
        if(stack.getItemMeta().getLore() != null) {
            lores = stack.getItemMeta().getLore();
            menuSize = lores.size() + 9;
        }
        ItemMenu setting = new ItemMenu(ChatColor.translateAlternateColorCodes('&', "&4수정:&c" + title), ItemMenu.Size.fit(menuSize), (JavaPlugin) Bukkit.getPluginManager().getPlugin("SteakGUI"));
        int i = 1;
        if(stack.getItemMeta().getDisplayName() != null) {
            setting.setItem(0, new ItemStackEditItem(menu, player, -1, s, SteakGUI.convertMessage("&b이름 수정"), Material.PISTON_STICKY_BASE, new String[]{SteakGUI.convertMessage("&c아이템의 이름을 수정합니다"), SteakGUI.convertMessage("&b현재 이름:&f " + stack.getItemMeta().getDisplayName())}));
        } else {
            setting.setItem(0, new ItemStackEditItem(menu, player, -1, s, SteakGUI.convertMessage("&b이름 수정"), Material.PISTON_STICKY_BASE, new String[]{SteakGUI.convertMessage("&c아이템의 이름을 수정합니다"), SteakGUI.convertMessage("&b현재 이름:&f 없음")}));
        }
        System.out.println(stack.getItemMeta().getLore());
        if(lores != null) {
            for (String lore : lores) {
                setting.setItem(i, new ItemStackEditItem(menu, player, i, s, SteakGUI.convertMessage("&b" + i + "번째 로어"), Material.PISTON_BASE, new String[]{SteakGUI.convertMessage(lore)}));
                i++;
            }
        }
        setting.setItem(i, new ItemStackEditItem(menu, player, i, s, SteakGUI.convertMessage("&b새 로어 추가"), Material.PISTON_STICKY_BASE, new String[]{SteakGUI.convertMessage("&c" + i + "번째 로어를 추가합니다.")}));
        setting.setItem(setting.getSize().getSize()-1, new ItemStackEditItem(menu, player, 999, s, SteakGUI.convertMessage("&c돌아가기"), Material.FEATHER, new String[]{SteakGUI.convertMessage("&c이전 매뉴로 돌아갑니다.")}));
        setting.open(player);
    }

    class ItemStackEditItem extends MenuItem {
        int loren = 0;
        int slot = 0;
        Menu menu = null;
        Player player = null;
        public ItemStackEditItem(Menu lmenu, Player p, int lorenum, int s, String displayName, Material icon, String... lore) {
            super(displayName, new ItemStack(icon, lorenum), lore);
            slot = s;
            loren = lorenum;
            menu = lmenu;
            player = p;
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            if(loren == -1) {
                event.getPlayer().setMetadata("ItemStackTitleEdit",new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menu.getName(), slot}));
                new MessageHandler().sendMessage(event.getPlayer(), SteakGUI.convertMessage("&a아이템의 이름을 입력하세요. 변수 및 색상코드도 사용하실 수 있습니다. 이름 삭제를 원하신다면 '삭제' 혹은 'remove' 를 입력해 주세요."));
                event.setWillClose(true);
            } else if(loren == 999) {
                new ItemEditor().show(menu, player, slot);
            } else {
                event.getPlayer().setMetadata("ItemStackLoreEdit", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menu.getName(), slot, loren}));
                new MessageHandler().sendMessage(event.getPlayer(), SteakGUI.convertMessage("&a아이템의 로어를 입력하세요. 변수 및 색상코드도 사용하실 수 있습니다. 로어 삭제를 원한다면 '삭제' 혹은 'remove' 를 입력해 주세요."));
                event.setWillClose(true);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if(e.getPlayer().hasMetadata("ItemStackTitleEdit")) {
            if(e.getMessage().equals("삭제") || e.getMessage().equals("remove")) {
                new MessageHandler().sendMessage(e.getPlayer(), "&a제목이 삭제되었습니다! 만약 로어를 삭제하지 않고 로어 자체에 삭제 혹은 remove 라고 쓰고 싶으시다면 컬러 코드를 이용해 주세요.");
                Object[] metadata = (Object[]) e.getPlayer().getMetadata("ItemStackTitleEdit").get(0).value();
                Menu menu = MenuFileHandler.loadMenu((String) metadata[0], true);
                ItemStack stack = menu.getItemArray().get((int)metadata[1]).getItemStack();
                ItemMeta meta = (ItemMeta)stack.getItemMeta();
                meta.setDisplayName(null);
                stack.setItemMeta(meta);
                menu.getItemArray().get((int) metadata[1]).setItemStack(stack);
                MenuFileHandler.saveMenu(menu);
                new ItemStackEditor().show(MenuFileHandler.loadMenu((String) metadata[0], true), e.getPlayer(), (int) metadata[1]);
                e.setCancelled(true);
                e.getPlayer().removeMetadata("ItemStackTitleEdit", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            } else {
                new MessageHandler().sendMessage(e.getPlayer(), e.getMessage() + "&a 를 제목으로 설정하였습니다!");
                Object[] metadata = (Object[]) e.getPlayer().getMetadata("ItemStackTitleEdit").get(0).value();
                Menu menu = MenuFileHandler.loadMenu((String) metadata[0], true);
                ItemStack stack = menu.getItemArray().get((int) metadata[1]).getItemStack();
                ItemMeta meta = (ItemMeta) stack.getItemMeta();
                meta.setDisplayName(e.getMessage());
                stack.setItemMeta(meta);
                menu.getItemArray().get((int) metadata[1]).setItemStack(stack);
                MenuFileHandler.saveMenu(menu);
                new ItemStackEditor().show(MenuFileHandler.loadMenu((String) metadata[0], true), e.getPlayer(), (int) metadata[1]);
                e.setCancelled(true);
                e.getPlayer().removeMetadata("ItemStackTitleEdit", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            }
        } else if(e.getPlayer().hasMetadata("ItemStackLoreEdit")) {
            if(e.getMessage().equals("삭제") || e.getMessage().equals("remove")) {
                new MessageHandler().sendMessage(e.getPlayer(), "&a로어가 삭제되었습니다! 만약 로어를 삭제하지 않고 로어 자체에 삭제 혹은 remove 라고 쓰고 싶으시다면 컬러 코드를 이용해 주세요.");
                Object[] metadata = (Object[]) e.getPlayer().getMetadata("ItemStackLoreEdit").get(0).value();
                Menu menu = MenuFileHandler.loadMenu((String) metadata[0], true);
                ItemStack stack = menu.getItemArray().get((int)metadata[1]).getItemStack();
                ItemMeta meta = (ItemMeta)stack.getItemMeta();
                List<String> lore = meta.getLore();
                lore.remove(((int) metadata[2]) - 1);
                meta.setLore(lore);
                stack.setItemMeta(meta);
                menu.getItemArray().get((int)metadata[1]).setItemStack(stack);
                MenuFileHandler.saveMenu(menu);
                new ItemStackEditor().show(MenuFileHandler.loadMenu((String) metadata[0], true), e.getPlayer(), (int) metadata[1]);
                e.setCancelled(true);
                e.getPlayer().removeMetadata("ItemStackLoreEdit", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            } else {
                new MessageHandler().sendMessage(e.getPlayer(), e.getMessage() + "&a 를 로어로 설정하였습니다!");
                Object[] metadata = (Object[]) e.getPlayer().getMetadata("ItemStackLoreEdit").get(0).value();
                Menu menu = MenuFileHandler.loadMenu((String) metadata[0], true);
                ItemStack stack = menu.getItemArray().get((int)metadata[1]).getItemStack();
                ItemMeta meta = stack.getItemMeta();
                List<String> lore = meta.getLore();
                if(lore != null) {
                    lore.set(((int) metadata[2]) - 1, e.getMessage());
                } else {
                    ArrayList<String> lores = new ArrayList<>();
                    lores.add(((int) metadata[2]) - 1, e.getMessage());
                    lore = lores;
                }
                meta.setLore(lore);
                stack.setItemMeta(meta);
                menu.getItemArray().get((int) metadata[1]).setItemStack(stack);
                MenuFileHandler.saveMenu(menu);
                new ItemStackEditor().show(MenuFileHandler.loadMenu((String) metadata[0], true), e.getPlayer(), (int) metadata[1]);
                e.setCancelled(true);
                e.getPlayer().removeMetadata("ItemStackLoreEdit", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            }
        }
    }
}
