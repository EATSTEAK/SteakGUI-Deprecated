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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.editor.ItemTaskEditor;
import tk.itstake.steakgui.editor.NewTaskSelector;
import tk.itstake.steakgui.gui.GUIItem;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.itemtask.ItemTask;
import tk.itstake.steakgui.util.ItemStackConverter;
import tk.itstake.steakgui.util.MenuFileHandler;
import tk.itstake.util.MessageHandler;

/**
 * Created by ITSTAKE on 2015-08-12.
 */
public class SellTaskEditor {
    public void show(Menu menu, Player player, String menuname, int slot, int task) {
        String title = menu.getTitle();
        if(title.length() > 10) {
            title = ChatColor.stripColor(SteakGUI.convertMessage(menu.getTitle(), menu, player)).substring(0, 11) + "..";
        }
        GUIItem slotItem = menu.getItemArray().get(slot);
        ItemTask edittask = slotItem.getTask(task);
        ItemMenu setting = new ItemMenu(ChatColor.translateAlternateColorCodes('&', "&4수정:&c" + title), ItemMenu.Size.FIVE_LINE, (JavaPlugin) Bukkit.getPluginManager().getPlugin("SteakGUI"));
        setting.setItem(0, new ItemTaskItem(menu, menuname, player, task, 0, 1,  slot, SteakGUI.convertMessage("&c펄미션 판매"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b판매할 수 있는 물품을 펄미션으로 설정합니다.")}));
        setting.setItem(1, new ItemTaskItem(menu, menuname, player, task, 1, 1,  slot, SteakGUI.convertMessage("&c아이템 판매"), Material.EMERALD, new String[]{SteakGUI.convertMessage("&b판매할 수 있는 물품을 아이템으로 설정합니다.")}));
        if(((String)edittask.getData()[0]).equals("permission")) {
            setting.setItem(9, new ItemTaskItem(menu, menuname, player, task, 2, 1,  slot, SteakGUI.convertMessage("&b펄미션 입력"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b판매하게 될 펄미션을 입력합니다.")}));
        } else {
            setting.setItem(9, new ItemTaskItem(menu, menuname, player, task, 3, 1,  slot, SteakGUI.convertMessage("&b아이템 선택"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b판매하게 될 아이템을 손에서 선택합니다.")}));
        }
        setting.setItem(18, new ItemTaskItem(menu, menuname, player, task, 4, 1,  slot, SteakGUI.convertMessage("&c펄미션 지불"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b아이템 판매시 펄미션을 지불합니다.")}));
        setting.setItem(19, new ItemTaskItem(menu, menuname, player, task, 5, 1,  slot, SteakGUI.convertMessage("&c아이템 지불"), Material.EMERALD, new String[]{SteakGUI.convertMessage("&b아이템 판매시 아이템을 지불합니다.")}));
        setting.setItem(20, new ItemTaskItem(menu, menuname, player, task, 6, 1,  slot, SteakGUI.convertMessage("&c돈 지불"), Material.IRON_INGOT, new String[]{SteakGUI.convertMessage("&b아이템 판매시 돈을 지불합니다.")}));
        if(((String)edittask.getData()[2]).equals("permission")) {
            setting.setItem(27, new ItemTaskItem(menu, menuname, player, task, 7, 1,  slot, SteakGUI.convertMessage("&b펄미션 입력"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b아이템 판매시 소모할 펄미션을 입력합니다.")}));
        } else if(((String)edittask.getData()[2]).equals("money")) {
            setting.setItem(27, new ItemTaskItem(menu, menuname, player, task, 8, 1,  slot, SteakGUI.convertMessage("&b돈 액수 입력"), Material.IRON_INGOT, new String[]{SteakGUI.convertMessage("&b아이템 판매시 소모할 돈 액수를 입력합니다.")}));
        } else {
            setting.setItem(27, new ItemTaskItem(menu, menuname, player, task, 9, 1,  slot, SteakGUI.convertMessage("&b아이템 선택"), Material.EMERALD, new String[]{SteakGUI.convertMessage("&b아이템 판매시 소모할 아이템을 선택합니다.")}));
        }
        setting.setItem(36, new ItemTaskItem(menu, menuname, player, task, 10, 1,  slot, SteakGUI.convertMessage("&b판매 성공 메시지 입력"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b판매에 성공하면 표시할 메시지를 입력합니다. 만약 '없음' 혹은 'none' 을 입력하면 메세지는 보이지 않습니다.")}));
        setting.setItem(37, new ItemTaskItem(menu, menuname, player, task, 11, 1,  slot, SteakGUI.convertMessage("&b돈 부족 메시지 입력"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b판매에 실패하면 표시할 메시지를 입력합니다. 만약 '없음' 혹은 'none' 을 입력하면 메세지는 보이지 않습니다.")}));
        setting.setItem(45, new ItemTaskItem(menu, menuname, player, task, 12, 1,  slot, SteakGUI.convertMessage("&b작업 종류 변경"), Material.ANVIL, new String[]{SteakGUI.convertMessage("&b작업 종류를 변경 합니다.")}));
        setting.setItem(46, new ItemTaskItem(menu, menuname, player, task, 13, 1,  slot, SteakGUI.convertMessage("&b작업 삭제"), Material.BARRIER, new String[]{SteakGUI.convertMessage("&b작업을 삭제합니다.")}));
        setting.setItem(47, new ItemTaskItem(menu, menuname, player, task, 99, 1, slot, SteakGUI.convertMessage("&c돌아가기"), Material.FEATHER, new String[]{SteakGUI.convertMessage("&c이전 매뉴로 돌아갑니다.")}));
        setting.open(player);
    }

    class ItemTaskItem extends MenuItem {
        int t = 0;
        int task = 0;
        int slot = 0;
        Menu menu = null;
        String menuname = null;
        Player player = null;
        public ItemTaskItem(Menu lmenu, String menuName, Player p, int ltask, int type, int amount, int s, String displayName, Material icon, String... lore) {
            super(displayName, new ItemStack(icon, amount), lore);
            slot = s;
            t = type;
            task = ltask;
            menu = lmenu;
            menuname = menuName;
            player = p;
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            ItemTask editTask = menu.getItemArray().get(slot).getTask(task);
            if(t == 0) {
                editTask.getData()[0] = "permission";
                new MessageHandler().sendMessage(event.getPlayer(), "&c이제 펄미션을 판매하게 됩니다.");
            } else if(t == 1) {
                editTask.getData()[0] = "item";
                new MessageHandler().sendMessage(event.getPlayer(), "&c이제 아이템을 판매하게 됩니다.");
            } else if(t == 2) {
                new MessageHandler().sendMessage(event.getPlayer(), "&a펄미션을 입력하세요.");
                player.setMetadata("permSell", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menuname, slot, task}));
                event.setWillClose(true);
            } else if(t == 3) {
                player.setMetadata("itemSell", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menuname, slot, task}));
                new MessageHandler().sendMessage(player, "&b판매할 아이템을 클릭해 주세요. 만약 판매할 아이템이 없으시다면 빈 슬롯을 클릭하세요.");
                event.setWillClose(true);
            } else if(t == 4) {
                editTask.getData()[2] = "permission";
                new MessageHandler().sendMessage(event.getPlayer(), "&c이제 펄미션으로 지불하게 됩니다.");
            } else if(t == 5) {
                editTask.getData()[2] = "money";
                new MessageHandler().sendMessage(event.getPlayer(), "&c이제 돈으로 지불하게 됩니다.");
            } else if(t == 6) {
                editTask.getData()[2] = "item";
                new MessageHandler().sendMessage(event.getPlayer(), "&c이제 아이템으로 지불하게 됩니다.");
            } else if(t == 7) {
                new MessageHandler().sendMessage(event.getPlayer(), "&a펄미션을 입력하세요.");
                player.setMetadata("permSellCost", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menuname, slot, task}));
            } else if(t == 8) {
                new MessageHandler().sendMessage(event.getPlayer(), "&a돈 액수를 입력하세요.(소숫점 포함) 예: 100.00");
                player.setMetadata("moneySellCost", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menuname, slot, task}));
            } else if(t == 9) {
                new MessageHandler().sendMessage(event.getPlayer(), "&a펄미션을 입력하세요.");
                player.setMetadata("itemSellCost", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menuname, slot, task}));
            } else if(t == 10) {
                new MessageHandler().sendMessage(event.getPlayer(), "&a판매 성공 메시지를 입력하세요.");
                player.setMetadata("sellComMsg", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menuname, slot, task}));
            } else if(t == 11) {
                new MessageHandler().sendMessage(event.getPlayer(), "&a판매 실패 메시지를 입력하세요.");
                player.setMetadata("sellFailMsg", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menuname, slot, task}));
            } else if(t == 12) {
                new NewTaskSelector().show(menu, player, menuname, slot, task);
            } else if(t == 13) {
                menu.getItemArray().get(slot).delTask(task);
            } else {
                new ItemTaskEditor().show(menu, player, menuname, slot);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if(e.getPlayer().hasMetadata("permSell")) {
            new MessageHandler().sendMessage(e.getPlayer(), "&a" + e.getMessage() + " 펄미션이 성공적으로 등록되었습니다!");
            Object[] metadata = (Object[]) e.getPlayer().getMetadata("permSell").get(0).value();
            Menu menu = MenuFileHandler.loadMenu((String) metadata[0]);
            menu.getItemArray().get((int)metadata[1]).getTask((int)metadata[2]).getData()[1] = e.getMessage();
            MenuFileHandler.saveMenu(menu, (String)metadata[0]);
            new SellTaskEditor().show(menu, e.getPlayer(), (String) metadata[0], (int) metadata[1], (int) metadata[2]);
            e.setCancelled(true);
            e.getPlayer().removeMetadata("permSell", Bukkit.getPluginManager().getPlugin("SteakGUI"));
        } else if(e.getPlayer().hasMetadata("permSellCost")) {
            new MessageHandler().sendMessage(e.getPlayer(), "&a" + e.getMessage() + " 펄미션이 성공적으로 등록되었습니다!");
            Object[] metadata = (Object[]) e.getPlayer().getMetadata("permSellCost").get(0).value();
            Menu menu = MenuFileHandler.loadMenu((String) metadata[0]);
            menu.getItemArray().get((int)metadata[1]).getTask((int)metadata[2]).getData()[3] = e.getMessage();
            MenuFileHandler.saveMenu(menu, (String)metadata[0]);
            new SellTaskEditor().show(menu, e.getPlayer(), (String) metadata[0], (int) metadata[1], (int) metadata[2]);
            e.setCancelled(true);
            e.getPlayer().removeMetadata("permSellCost", Bukkit.getPluginManager().getPlugin("SteakGUI"));
        } else if(e.getPlayer().hasMetadata("moneySellCost")) {
            new MessageHandler().sendMessage(e.getPlayer(), "&a" + e.getMessage() + " 원이 성공적으로 등록되었습니다!");
            Object[] metadata = (Object[]) e.getPlayer().getMetadata("moneySellCost").get(0).value();
            Menu menu = MenuFileHandler.loadMenu((String) metadata[0]);
            menu.getItemArray().get((int)metadata[1]).getTask((int)metadata[2]).getData()[3] = e.getMessage();
            MenuFileHandler.saveMenu(menu, (String)metadata[0]);
            new SellTaskEditor().show(menu, e.getPlayer(), (String) metadata[0], (int) metadata[1], (int) metadata[2]);
            e.setCancelled(true);
            e.getPlayer().removeMetadata("moneySellCost", Bukkit.getPluginManager().getPlugin("SteakGUI"));
        } else if(e.getPlayer().hasMetadata("SellComMsg")) {
            new MessageHandler().sendMessage(e.getPlayer(), "&a" + e.getMessage() + " 메세지가 성공적으로 등록되었습니다!");
            Object[] metadata = (Object[]) e.getPlayer().getMetadata("sellComMsg").get(0).value();
            Menu menu = MenuFileHandler.loadMenu((String) metadata[0]);
            menu.getItemArray().get((int)metadata[1]).getTask((int)metadata[2]).getData()[4] = e.getMessage();
            MenuFileHandler.saveMenu(menu, (String)metadata[0]);
            new SellTaskEditor().show(menu, e.getPlayer(), (String) metadata[0], (int) metadata[1], (int) metadata[2]);
            e.setCancelled(true);
            e.getPlayer().removeMetadata("sellComMsg", Bukkit.getPluginManager().getPlugin("SteakGUI"));
        } else if(e.getPlayer().hasMetadata("sellFailMsg")) {
            new MessageHandler().sendMessage(e.getPlayer(), "&a" + e.getMessage() + " 메세지가 성공적으로 등록되었습니다!");
            Object[] metadata = (Object[]) e.getPlayer().getMetadata("sellFailMsg").get(0).value();
            Menu menu = MenuFileHandler.loadMenu((String) metadata[0]);
            menu.getItemArray().get((int)metadata[1]).getTask((int)metadata[2]).getData()[5] = e.getMessage();
            MenuFileHandler.saveMenu(menu, (String)metadata[0]);
            new SellTaskEditor().show(menu, e.getPlayer(), (String) metadata[0], (int) metadata[1], (int) metadata[2]);
            e.setCancelled(true);
            e.getPlayer().removeMetadata("sellFailMsg", Bukkit.getPluginManager().getPlugin("SteakGUI"));
        }
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent e) {
        if(e.getPlayer().hasMetadata("itemSell")) {
            if(e.getItem() != null) {
                new MessageHandler().sendMessage(e.getPlayer(), "&b아이템을 성공적으로 가져왔습니다!");
                Object[] metadata = (Object[]) e.getPlayer().getMetadata("itemSell").get(0).value();
                Menu menu = MenuFileHandler.loadMenu((String) ((Object[]) e.getPlayer().getMetadata("itemSell").get(0).value())[0]);
                ItemStack stack = e.getItem();
                menu.getItemArray().get((int)metadata[1]).getTask((int)metadata[2]).getData()[2] = ItemStackConverter.convert(stack);
                e.setCancelled(true);
                new SellTaskEditor().show(menu, e.getPlayer(), (String) ((Object[]) e.getPlayer().getMetadata("itemSell").get(0).value())[0], (int) ((Object[]) e.getPlayer().getMetadata("itemSell").get(0).value())[1], (int) ((Object[]) e.getPlayer().getMetadata("itemSell").get(0).value())[2]);
                e.getPlayer().removeMetadata("itemSell", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            } else {
                new MessageHandler().sendMessage(e.getPlayer(), "&c아이템 가져오기가 취소되었습니다!");
                e.setCancelled(true);
                e.getPlayer().removeMetadata("itemSell", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            }
        } else if(e.getPlayer().hasMetadata("itemSellCost")) {
            if(e.getItem() != null) {
                new MessageHandler().sendMessage(e.getPlayer(), "&b아이템을 성공적으로 가져왔습니다!");
                Object[] metadata = (Object[]) e.getPlayer().getMetadata("itemSellCost").get(0).value();
                Menu menu = MenuFileHandler.loadMenu((String) ((Object[]) e.getPlayer().getMetadata("itemSellCost").get(0).value())[0]);
                ItemStack stack = e.getItem();
                menu.getItemArray().get((int)metadata[1]).getTask((int)metadata[2]).getData()[3] = ItemStackConverter.convert(stack);
                MenuFileHandler.saveMenu(menu, (String) ((Object[]) e.getPlayer().getMetadata("itemSellCost").get(0).value())[0]);
                e.setCancelled(true);
                new SellTaskEditor().show(menu, e.getPlayer(), (String) ((Object[]) e.getPlayer().getMetadata("itemSellCost").get(0).value())[0], (int) ((Object[]) e.getPlayer().getMetadata("itemSellCost").get(0).value())[1], (int) ((Object[]) e.getPlayer().getMetadata("itemSellCost").get(0).value())[2]);
                e.getPlayer().removeMetadata("itemSellCost", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            } else {
                new MessageHandler().sendMessage(e.getPlayer(), "&c아이템 가져오기가 취소되었습니다!");
                e.setCancelled(true);
                e.getPlayer().removeMetadata("itemSellCost", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            }
        }
    }
}
