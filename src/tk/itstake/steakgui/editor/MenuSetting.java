package tk.itstake.steakgui.editor;

import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.MenuItem;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
public class MenuSetting implements Listener {
    public void show(Menu menu, Player p, String menuName) {
        String title = menu.getTitle();
        if(title.length() > 10) {
            title = ChatColor.stripColor(SteakGUI.convertMessage(menu.getTitle(), menu, p)).substring(0, 11) + "..";
        }
        ItemMenu setting = new ItemMenu(ChatColor.translateAlternateColorCodes('&', "&4수정:&c" + title), ItemMenu.Size.THREE_LINE, (JavaPlugin) Bukkit.getPluginManager().getPlugin("SteakGUI"));
        setting.setItem(10, new MenuSettingItem(menu, menuName, p, 0, SteakGUI.convertMessage("&b매뉴 제목 수정"), Material.NAME_TAG, new String[]{SteakGUI.convertMessage("&b매뉴 제목을 수정합니다."), SteakGUI.convertMessage("&c현재 제목:" + menu.getTitle())}));
        setting.setItem(12, new MenuSettingItem(menu, menuName, p, 1, SteakGUI.convertMessage("&b매뉴 줄 수 수정"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b매뉴의 줄 수를 수정합니다."), SteakGUI.convertMessage("&c현재 줄 수:" + menu.getSize()/9)}));
        setting.setItem(14, new MenuSettingItem(menu, menuName, p, 2, SteakGUI.convertMessage("&b체스트에서 가져오기"), Material.ENDER_CHEST, new String[]{SteakGUI.convertMessage("&b체스트에서 매뉴의 아이템을 가져옵니다."), SteakGUI.convertMessage("&3<클릭> 으로 원래 아이템을 지우지 않고 가져옴"), SteakGUI.convertMessage("&2<쉬프트+클릭> 으로 원래 아이템도 지우고 가져옴")}));
        setting.setItem(16, new MenuSettingItem(menu, menuName, p, 3, SteakGUI.convertMessage("&c돌아가기"), Material.FEATHER, new String[]{SteakGUI.convertMessage("&c이전 매뉴로 돌아갑니다.")}));
        setting.open(p);
    }

    class MenuSettingItem extends MenuItem {
        int t = 0;
        Menu menu = null;
        String menuname = null;
        Player player = null;
        public MenuSettingItem(Menu lmenu, String menuName, Player p, int type, String displayName, Material icon, String... lore) {
            super(displayName, new ItemStack(icon), lore);
            t = type;
            menu = lmenu;
            menuname = menuName;
            player = p;
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            if(t == 0) {
                player.setMetadata("titleEdit", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), menuname));
                new MessageHandler().sendMessage(player, "&b제목을 입력해 주세요.(16자 이하) 취소하시려면 'cancel' 혹은 '취소' 를 입력하세요.");
                event.setWillClose(true);
            } else if(t == 1) {
                new LineSetting().show(menu, player, menuname);
            } else if(t == 2) {
                if(!event.getClick().isShiftClick()) {
                    player.setMetadata("importChest", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), menuname));
                    new MessageHandler().sendMessage(player, "&b가져올 체스트를 선택해 주세요.");
                    event.setWillClose(true);
                } else {
                    player.setMetadata("importChestForce", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), menuname));
                    new MessageHandler().sendMessage(player, "&c가져올 체스트를 선택해 주세요.(덮어쓰기 모드)");
                    event.setWillClose(true);
                }
            } else if(t == 3) {
                new EditorMain().show(menu, player, menuname);
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if(e.getPlayer().hasMetadata("titleEdit")) {
            if(e.getMessage().length() <= 16 && !e.getMessage().equals("cancel") && !e.getMessage().equals("취소")) {
                new MessageHandler().sendMessage(e.getPlayer(), "&b매뉴 이름이 &r" + e.getMessage() + "&b 으로 저장되었습니다.");
                e.setCancelled(true);
                Menu targetmenu = MenuFileHandler.loadMenu(e.getPlayer().getMetadata("titleEdit").get(0).asString());
                targetmenu.setTitle(e.getMessage());
                MenuFileHandler.saveMenu(targetmenu, e.getPlayer().getMetadata("titleEdit").get(0).asString());
                new MenuSetting().show(targetmenu, e.getPlayer(), e.getPlayer().getMetadata("titleEdit").get(0).asString());
                e.getPlayer().removeMetadata("titleEdit", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            } else if(e.getMessage().equals("cancel") || e.getMessage().equals("취소")) {
                new MessageHandler().sendMessage(e.getPlayer(), "&c취소되었습니다.");
                e.setCancelled(true);
                new MenuSetting().show(MenuFileHandler.loadMenu(e.getPlayer().getMetadata("titleEdit").get(0).asString()), e.getPlayer(), e.getPlayer().getMetadata("titleEdit").get(0).asString());
                e.getPlayer().removeMetadata("titleEdit", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            } else {
                new MessageHandler().sendMessage(e.getPlayer(), "&c매뉴 이름이 너무 깁니다!");
                e.setCancelled(true);
                new MenuSetting().show(MenuFileHandler.loadMenu(e.getPlayer().getMetadata("titleEdit").get(0).asString()), e.getPlayer(), e.getPlayer().getMetadata("titleEdit").get(0).asString());
                e.getPlayer().removeMetadata("titleEdit", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            }
        }
    }

    @EventHandler
    public void onClickBlock(PlayerInteractEvent e) {
        if(e.hasBlock()) {
            if(e.getPlayer().hasMetadata("importChest")) {
                if (e.getClickedBlock().getType().equals(Material.CHEST)) {
                    Chest chest = (Chest) e.getClickedBlock().getState();
                    loadFromChest(MenuFileHandler.loadMenu(e.getPlayer().getMetadata("importChest").get(0).asString()), e.getPlayer(),  e.getPlayer().getMetadata("importChest").get(0).asString(), chest, false);
                    e.setCancelled(true);
                    e.getPlayer().removeMetadata("importChest", Bukkit.getPluginManager().getPlugin("SteakGUI"));
                } else {
                    new MessageHandler().sendMessage(e.getPlayer(), "&c클릭한 블럭이 체스트가 아닙니다!");
                    e.setCancelled(true);
                    new MenuSetting().show(MenuFileHandler.loadMenu(e.getPlayer().getMetadata("importChest").get(0).asString()), e.getPlayer(), e.getPlayer().getMetadata("importChest").get(0).asString());
                    e.getPlayer().removeMetadata("importChest", Bukkit.getPluginManager().getPlugin("SteakGUI"));
                }
            } else if(e.getPlayer().hasMetadata("importChestForce")) {
                if (e.getClickedBlock().getType().equals(Material.CHEST)) {
                    Chest chest = (Chest) e.getClickedBlock().getState();
                    loadFromChest(MenuFileHandler.loadMenu(e.getPlayer().getMetadata("importChestForce").get(0).asString()), e.getPlayer(), e.getPlayer().getMetadata("importChestForce").get(0).asString(), chest, true);
                    e.setCancelled(true);
                    e.getPlayer().removeMetadata("importChestForce", Bukkit.getPluginManager().getPlugin("SteakGUI"));
                } else {
                    new MessageHandler().sendMessage(e.getPlayer(), "&c클릭한 블럭이 체스트가 아닙니다!");
                    e.setCancelled(true);
                    new MenuSetting().show(MenuFileHandler.loadMenu(e.getPlayer().getMetadata("importChestForce").get(0).asString()), e.getPlayer(), e.getPlayer().getMetadata("importChestForce").get(0).asString());
                    e.getPlayer().removeMetadata("importChestForce", Bukkit.getPluginManager().getPlugin("SteakGUI"));
                }
            }
        }
    }

    public void loadFromChest(Menu menu, Player p, String menuName, Chest chest, boolean rewrite) {
        int i = 0;
        for(ItemStack s:chest.getInventory().getContents()) {
            ItemStack stack = s;
            if(rewrite) {
                if(stack != null) {
                    ItemMeta itemmeta = stack.getItemMeta();
                    if (itemmeta.getDisplayName() == null) {
                        itemmeta.setDisplayName(SteakGUI.lh.getLanguage("menu.nodisplayname"));
                    }
                    if (itemmeta.getLore() == null) {
                        itemmeta.setLore(Arrays.asList(SteakGUI.lh.getLanguage("menu.nolore").split("\n")));
                    }
                    stack.setItemMeta(itemmeta);
                    menu.setItem(i, new GUIItem(stack, "", new ItemTask(ItemTask.MESSAGE, new String[]{SteakGUI.lh.getLanguage("menu.noitemtask")})));
                } else {
                    menu.removeItem(i);
                }
            } else {
                if(stack != null && !menu.hasItem(i)) {
                    ItemMeta itemmeta = stack.getItemMeta();
                    if (itemmeta.getDisplayName() == null) {
                        itemmeta.setDisplayName(SteakGUI.lh.getLanguage("menu.nodisplayname"));
                    }
                    if (itemmeta.getLore() == null) {
                        itemmeta.setLore(Arrays.asList(SteakGUI.lh.getLanguage("menu.nolore").split("\n")));
                    }
                    stack.setItemMeta(itemmeta);
                    menu.setItem(i, new GUIItem(stack, "", new ItemTask(ItemTask.MESSAGE, new String[]{SteakGUI.lh.getLanguage("menu.noitemtask")})));
                }
            }
            i++;
        }
        MenuFileHandler.saveMenu(menu, menuName);
        new MessageHandler().sendMessage(p, "&b체스트에서 가져오기가 완료되었습니다.");
    }
}
