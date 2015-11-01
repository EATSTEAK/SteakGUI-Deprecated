package tk.itstake.steakgui.menueditor.taskeditor;

import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.MenuItem;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.gui.GUIItem;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.itemtask.ItemTask;
import tk.itstake.steakgui.util.MenuFileHandler;
import tk.itstake.util.MessageHandler;

import java.util.ArrayList;

/**
 * Created by ITSTAKE on 2015-10-11.
 */
public class TaskClickTypeEditor {

    public void show(Menu menu, Player player, int slot, int task) {
        String title = menu.getTitle();
        if(title.length() > 10) {
            title = ChatColor.stripColor(SteakGUI.convertMessage(menu.getTitle(), menu, player)).substring(0, 11) + "";
        }
        GUIItem slotItem = menu.getItemArray().get(slot);
        ItemTask edittask = slotItem.getTask(task);
        ArrayList<ClickType> clickTypes = edittask.getClickType();
        /*
        ClickType.CONTROL_DROP;
        ClickType.DOUBLE_CLICK;
        ClickType.DROP;
        ClickType.LEFT;
        ClickType.MIDDLE;
        ClickType.NUMBER_KEY;
        ClickType.RIGHT;
        ClickType.SHIFT_LEFT;
        ClickType.SHIFT_RIGHT;
        */
        String clickType = "";
        if(clickTypes != null) {
            for(ClickType ct:clickTypes) {
                if (clickType.equals("")) {
                    clickType = changeClickType(ct);
                } else {
                    clickType = "," + changeClickType(ct);
                }
            }
        }
        ItemStack nestack = new ItemStack(Material.PAPER, 1);
        ItemStack estack = new ItemStack(Material.PAPER, 1);
        ItemStack bstack = new ItemStack(Material.FEATHER, 1);
        estack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        ItemMenu setting = new ItemMenu(ChatColor.translateAlternateColorCodes('&', "&4수정:&c" + title), ItemMenu.Size.FOUR_LINE, (JavaPlugin) Bukkit.getPluginManager().getPlugin("SteakGUI"));
        if(clickTypes == null) {
            setting.setItem(11, new ClickTypeEditItem(menu, player, task, slot, 1, estack, SteakGUI.convertMessage("&b모두"), new String[]{SteakGUI.convertMessage("&b모든 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b모두")}));
            setting.setItem(12, new ClickTypeEditItem(menu, player, task, slot, 2, nestack, SteakGUI.convertMessage("&b왼쪽 클릭"), new String[]{SteakGUI.convertMessage("&b왼쪽 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b모두")}));
            setting.setItem(13, new ClickTypeEditItem(menu, player, task, slot, 3, nestack, SteakGUI.convertMessage("&b오른쪽 클릭"), new String[]{SteakGUI.convertMessage("&b오른쪽 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b모두")}));
            setting.setItem(14, new ClickTypeEditItem(menu, player, task, slot, 4, nestack, SteakGUI.convertMessage("&b중간 클릭(휠 클릭)"), new String[]{SteakGUI.convertMessage("&b중간 클릭(휠 클릭) 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b모두")}));
            setting.setItem(15, new ClickTypeEditItem(menu, player, task, slot, 5, nestack, SteakGUI.convertMessage("&b쉬프트+왼쪽 클릭"), new String[]{SteakGUI.convertMessage("&b쉬프트+왼쪽 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b모두")}));
            setting.setItem(20, new ClickTypeEditItem(menu, player, task, slot, 6, nestack, SteakGUI.convertMessage("&b쉬프트+오른쪽 클릭"), new String[]{SteakGUI.convertMessage("&b쉬프트+오른쪽 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b모두")}));
            setting.setItem(21, new ClickTypeEditItem(menu, player, task, slot, 7, nestack, SteakGUI.convertMessage("&b버리기 키"), new String[]{SteakGUI.convertMessage("&b버리기 키 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b모두")}));
            setting.setItem(22, new ClickTypeEditItem(menu, player, task, slot, 8, nestack, SteakGUI.convertMessage("&b컨트롤+버리기 키"), new String[]{SteakGUI.convertMessage("&b컨트롤+버리기 키 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b모두")}));
            setting.setItem(23, new ClickTypeEditItem(menu, player, task, slot, 9, nestack, SteakGUI.convertMessage("&b숫자 키"), new String[]{SteakGUI.convertMessage("&b숫자 키 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b모두")}));
            setting.setItem(24, new ClickTypeEditItem(menu, player, task, slot, 10, nestack, SteakGUI.convertMessage("&b더블 클릭"), new String[]{SteakGUI.convertMessage("&b더블 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b모두")}));
        } else {
            setting.setItem(11, new ClickTypeEditItem(menu, player, task, slot, 1, nestack, SteakGUI.convertMessage("&b모두"), new String[]{SteakGUI.convertMessage("&b모든 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b" + clickType)}));
            if(clickTypes.contains(ClickType.LEFT)) {
                setting.setItem(12, new ClickTypeEditItem(menu, player, task, slot, 2, estack, SteakGUI.convertMessage("&b왼쪽 클릭"), new String[]{SteakGUI.convertMessage("&b왼쪽 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b" + clickType)}));
            } else {
                setting.setItem(12, new ClickTypeEditItem(menu, player, task, slot, 2, nestack, SteakGUI.convertMessage("&b왼쪽 클릭"), new String[]{SteakGUI.convertMessage("&b왼쪽 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b" + clickType)}));
            }
            if(clickTypes.contains(ClickType.RIGHT)) {
                setting.setItem(13, new ClickTypeEditItem(menu, player, task, slot, 3, estack, SteakGUI.convertMessage("&b오른쪽 클릭"), new String[]{SteakGUI.convertMessage("&b오른쪽 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b" + clickType)}));
            } else {
                setting.setItem(13, new ClickTypeEditItem(menu, player, task, slot, 3, nestack, SteakGUI.convertMessage("&b오른쪽 클릭"), new String[]{SteakGUI.convertMessage("&b오른쪽 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b" + clickType)}));
            }
            if(clickTypes.contains(ClickType.MIDDLE)) {
                setting.setItem(14, new ClickTypeEditItem(menu, player, task, slot, 4, estack, SteakGUI.convertMessage("&b중간 클릭(휠 클릭)"), new String[]{SteakGUI.convertMessage("&b중간 클릭(휠 클릭) 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b" + clickType)}));
            } else {
                setting.setItem(14, new ClickTypeEditItem(menu, player, task, slot, 4, nestack, SteakGUI.convertMessage("&b중간 클릭(휠 클릭)"), new String[]{SteakGUI.convertMessage("&b중간 클릭(휠 클릭) 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b" + clickType)}));
            }
            if(clickTypes.contains(ClickType.SHIFT_LEFT)) {
                setting.setItem(15, new ClickTypeEditItem(menu, player, task, slot, 5, estack, SteakGUI.convertMessage("&b쉬프트+왼쪽 클릭"), new String[]{SteakGUI.convertMessage("&b쉬프트+왼쪽 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b" + clickType)}));
            } else {
                setting.setItem(15, new ClickTypeEditItem(menu, player, task, slot, 5, nestack, SteakGUI.convertMessage("&b쉬프트+왼쪽 클릭"), new String[]{SteakGUI.convertMessage("&b쉬프트+왼쪽 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b" + clickType)}));
            }
            if(clickTypes.contains(ClickType.SHIFT_RIGHT)) {
                setting.setItem(20, new ClickTypeEditItem(menu, player, task, slot, 6, estack, SteakGUI.convertMessage("&b쉬프트+오른쪽 클릭"), new String[]{SteakGUI.convertMessage("&b쉬프트+오른쪽 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b" + clickType)}));
            } else {
                setting.setItem(20, new ClickTypeEditItem(menu, player, task, slot, 6, nestack, SteakGUI.convertMessage("&b쉬프트+오른쪽 클릭"), new String[]{SteakGUI.convertMessage("&b쉬프트+오른쪽 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b" + clickType)}));
            }
            if(clickTypes.contains(ClickType.DROP)) {
                setting.setItem(21, new ClickTypeEditItem(menu, player, task, slot, 7, estack, SteakGUI.convertMessage("&b버리기 키"), new String[]{SteakGUI.convertMessage("&b버리기 키 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b" + clickType)}));
            } else {
                setting.setItem(21, new ClickTypeEditItem(menu, player, task, slot, 7, nestack, SteakGUI.convertMessage("&b버리기 키"), new String[]{SteakGUI.convertMessage("&b버리기 키 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b" + clickType)}));
            }
            if(clickTypes.contains(ClickType.CONTROL_DROP)) {
                setting.setItem(22, new ClickTypeEditItem(menu, player, task, slot, 8, estack, SteakGUI.convertMessage("&b컨트롤+버리기 키"), new String[]{SteakGUI.convertMessage("&b컨트롤+버리기 키 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b" + clickType)}));
            } else {
                setting.setItem(22, new ClickTypeEditItem(menu, player, task, slot, 8, nestack, SteakGUI.convertMessage("&b컨트롤+버리기 키"), new String[]{SteakGUI.convertMessage("&b컨트롤+버리기 키 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b" + clickType)}));
            }
            if(clickTypes.contains(ClickType.NUMBER_KEY)) {
                setting.setItem(23, new ClickTypeEditItem(menu, player, task, slot, 9, estack, SteakGUI.convertMessage("&b숫자 키"), new String[]{SteakGUI.convertMessage("&b숫자 키 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b" + clickType)}));
            } else {
                setting.setItem(23, new ClickTypeEditItem(menu, player, task, slot, 9, nestack, SteakGUI.convertMessage("&b숫자 키"), new String[]{SteakGUI.convertMessage("&b숫자 키 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b" + clickType)}));
            }
            if(clickTypes.contains(ClickType.DOUBLE_CLICK)) {
                setting.setItem(24, new ClickTypeEditItem(menu, player, task, slot, 10, estack, SteakGUI.convertMessage("&b더블 클릭"), new String[]{SteakGUI.convertMessage("&b더블 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b" + clickType)}));
            } else {
                setting.setItem(24, new ClickTypeEditItem(menu, player, task, slot, 10, nestack, SteakGUI.convertMessage("&b더블 클릭"), new String[]{SteakGUI.convertMessage("&b더블 클릭 방식을 인식합니다."), SteakGUI.convertMessage("&c현재 인식하는 클릭 방식: &b" + clickType)}));
            }

        }
        setting.setItem(35, new ClickTypeEditItem(menu, player, task, slot, 999, bstack, SteakGUI.convertMessage("&c돌아가기"), new String[]{SteakGUI.convertMessage("&c이전 매뉴로 돌아갑니다.")}));
        setting.open(player);
    }

    private String changeClickType(ClickType ct) {
        if(ct.equals(ClickType.CONTROL_DROP)) {
            return "컨트롤+버리기 키";
        } else if(ct.equals(ClickType.DOUBLE_CLICK)) {
            return "더블 클릭";
        } else if(ct.equals(ClickType.DROP)) {
            return "버리기 키";
        } else if(ct.equals(ClickType.LEFT)) {
            return "왼쪽 클릭";
        } else if(ct.equals(ClickType.MIDDLE)) {
            return "중간 클릭";
        } else if(ct.equals(ClickType.NUMBER_KEY)) {
            return "숫자 키";
        } else if(ct.equals(ClickType.RIGHT)) {
            return "오른쪽 클릭";
        } else if(ct.equals(ClickType.SHIFT_LEFT)) {
            return "쉬프트+왼쪽 클릭";
        } else if(ct.equals(ClickType.SHIFT_RIGHT)) {
            return "쉬프트+오른쪽 클릭";
        } else {
            return "알 수 없음";
        }
    }


    class ClickTypeEditItem extends MenuItem {
        int task = 0;
        int slot = 0;
        int type = 0;
        Menu menu = null;
        Player player = null;
        public ClickTypeEditItem(Menu lmenu, Player p, int ltask, int lslot, int ltype, ItemStack stack, String displayName, String[] lore) {
            super(displayName, stack, lore);
            slot = lslot;
            type = ltype;
            task = ltask;
            menu = lmenu;
            player = p;
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            ItemTask editTask = menu.getItemArray().get(slot).getTask(task);
            ArrayList<ClickType> clickType = editTask.getClickType();
            if(type == 1) {
                if(clickType == null) {
                    new MessageHandler().sendMessage(event.getPlayer(), "&a다른 클릭 방식을 선택하셔야 이 클릭 방식을 끄실 수 있습니다.");
                } else {
                    editTask.setClickType(null);
                    MenuFileHandler.saveMenu(menu);
                    new TaskClickTypeEditor().show(MenuFileHandler.loadMenu(menu.getName()), event.getPlayer(), slot, task);
                }
            } else if(type == 2) {
                if(clickType == null) {
                    clickType = new ArrayList<ClickType>();
                    clickType.add(ClickType.LEFT);
                } else if(clickType.contains(ClickType.LEFT)) {
                    clickType.remove(ClickType.LEFT);
                } else {
                    clickType.add(ClickType.LEFT);
                }
                editTask.setClickType(clickType);
                MenuFileHandler.saveMenu(menu);
                new TaskClickTypeEditor().show(MenuFileHandler.loadMenu(menu.getName()), event.getPlayer(), slot, task);
            } else if(type == 3) {
                if(clickType == null) {
                    clickType = new ArrayList<ClickType>();
                    clickType.add(ClickType.RIGHT);
                } else if(clickType.contains(ClickType.RIGHT)) {
                    clickType.remove(ClickType.RIGHT);
                } else {
                    clickType.add(ClickType.RIGHT);
                }
                editTask.setClickType(clickType);
                MenuFileHandler.saveMenu(menu);
                new TaskClickTypeEditor().show(MenuFileHandler.loadMenu(menu.getName()), event.getPlayer(), slot, task);
            } else if(type == 4) {
                if(clickType == null) {
                    clickType = new ArrayList<ClickType>();
                    clickType.add(ClickType.MIDDLE);
                } else if(clickType.contains(ClickType.MIDDLE)) {
                    clickType.remove(ClickType.MIDDLE);
                } else {
                    clickType.add(ClickType.MIDDLE);
                }
                editTask.setClickType(clickType);
                MenuFileHandler.saveMenu(menu);
                new TaskClickTypeEditor().show(MenuFileHandler.loadMenu(menu.getName()), event.getPlayer(), slot, task);
            } else if(type == 5) {
                if(clickType == null) {
                    clickType = new ArrayList<ClickType>();
                    clickType.add(ClickType.SHIFT_LEFT);
                } else if(clickType.contains(ClickType.SHIFT_LEFT)) {
                    clickType.remove(ClickType.SHIFT_LEFT);
                } else {
                    clickType.add(ClickType.SHIFT_LEFT);
                }
                editTask.setClickType(clickType);
                MenuFileHandler.saveMenu(menu);
                new TaskClickTypeEditor().show(MenuFileHandler.loadMenu(menu.getName()), event.getPlayer(), slot, task);
            } else if(type == 6) {
                if(clickType == null) {
                    clickType = new ArrayList<ClickType>();
                    clickType.add(ClickType.SHIFT_RIGHT);
                } else if(clickType.contains(ClickType.SHIFT_RIGHT)) {
                    clickType.remove(ClickType.SHIFT_RIGHT);
                } else {
                    clickType.add(ClickType.SHIFT_RIGHT);
                }
                editTask.setClickType(clickType);
                MenuFileHandler.saveMenu(menu);
                new TaskClickTypeEditor().show(MenuFileHandler.loadMenu(menu.getName()), event.getPlayer(), slot, task);
            } else if(type == 7) {
                if(clickType == null) {
                    clickType = new ArrayList<ClickType>();
                    clickType.add(ClickType.DROP);
                } else if(clickType.contains(ClickType.DROP)) {
                    clickType.remove(ClickType.DROP);
                } else {
                    clickType.add(ClickType.DROP);
                }
                editTask.setClickType(clickType);
                MenuFileHandler.saveMenu(menu);
                new TaskClickTypeEditor().show(MenuFileHandler.loadMenu(menu.getName()), event.getPlayer(), slot, task);
            } else if(type == 8) {
                if(clickType == null) {
                    clickType = new ArrayList<ClickType>();
                    clickType.add(ClickType.CONTROL_DROP);
                } else if(clickType.contains(ClickType.CONTROL_DROP)) {
                    clickType.remove(ClickType.CONTROL_DROP);
                } else {
                    clickType.add(ClickType.CONTROL_DROP);
                }
                editTask.setClickType(clickType);
                MenuFileHandler.saveMenu(menu);
                new TaskClickTypeEditor().show(MenuFileHandler.loadMenu(menu.getName()), event.getPlayer(), slot, task);
            } else if(type == 9) {
                if(clickType == null) {
                    clickType = new ArrayList<ClickType>();
                    clickType.add(ClickType.NUMBER_KEY);
                } else if(clickType.contains(ClickType.NUMBER_KEY)) {
                    clickType.remove(ClickType.NUMBER_KEY);
                } else {
                    clickType.add(ClickType.NUMBER_KEY);
                }
                editTask.setClickType(clickType);
                MenuFileHandler.saveMenu(menu);
                new TaskClickTypeEditor().show(MenuFileHandler.loadMenu(menu.getName()), event.getPlayer(), slot, task);
            } else if(type == 10) {
                if(clickType == null) {
                    clickType = new ArrayList<ClickType>();
                    clickType.add(ClickType.DOUBLE_CLICK);
                } else if(clickType.contains(ClickType.DOUBLE_CLICK)) {
                    clickType.remove(ClickType.DOUBLE_CLICK);
                } else {
                    clickType.add(ClickType.DOUBLE_CLICK);
                }
                editTask.setClickType(clickType);
                MenuFileHandler.saveMenu(menu);
                new TaskClickTypeEditor().show(MenuFileHandler.loadMenu(menu.getName()), event.getPlayer(), slot, task);
            } else {
                taskOpener(menu, event.getPlayer(), slot, task);
            }
        }
    }

    private void taskOpener(Menu menu, Player player, int slot, int task) {
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
