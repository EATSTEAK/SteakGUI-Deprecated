package tk.itstake.steakgui.itemtask;


import ninja.amp.ampmenus.events.ItemClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.util.*;
import tk.itstake.util.BukkitUtil;
import tk.itstake.util.MessageHandler;
import tk.itstake.util.LanguageHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bexco on 2015-07-26.
 */
public class ItemTask {
    public static String COMMAND = "command";
    public static String OPEN_MENU = "open";
    public static String BUY = "buy";
    public static String SELL = "sell";
    public static String MESSAGE = "message";
    public static String GIVE = "give";
    public static String SOUND = "sound";
    public static String BROADCAST = "broadcast";
    public static String CLOSE = "close";
    public static String IF = "if";
    String TYPE = "";
    Object[] DATA = null;
    ClickType CLICKTYPE = null;
    private MessageHandler mh = new MessageHandler();
    private LanguageHandler lh = new LanguageHandler();
    public ItemTask(String type, Object[] data, ClickType clickType) {
        TYPE = type;
        DATA = data;
        CLICKTYPE = clickType;
    }

    public ItemTask(String type, Object[] data) {
        TYPE = type;
        DATA = data;
        CLICKTYPE = null;
    }

    public void runTask(ItemClickEvent event, Menu MENU) throws Exception {
        if(CLICKTYPE == null || CLICKTYPE.equals(event.getClick())) {
            if (TYPE.equals(COMMAND) && DATA.length == 2) {
                String permission = (String)DATA[0];
                String command = (String)DATA[1];
                if (permission.equals("op") && !event.getPlayer().isOp()) {
                    event.getPlayer().setOp(true);
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerCommandPreprocessEvent(event.getPlayer(), command));
                    event.getPlayer().performCommand(command.substring(1));
                    event.getPlayer().setOp(false);
                } else if (permission.equals("console")) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.substring(1));
                    Bukkit.getServer().getPluginManager().callEvent(new ServerCommandEvent(Bukkit.getConsoleSender(), command));
                } else {
                    event.getPlayer().performCommand(command.substring(1));
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerCommandPreprocessEvent(event.getPlayer(), command));
                }
            } else if (TYPE.equals(OPEN_MENU) && DATA.length == 1) {
                String menuname = (String)DATA[0];
                Menu openmenu = MenuFileHandler.loadMenu(menuname);
                openmenu.open(event.getPlayer());
            } else if (TYPE.equals(BUY) && DATA.length == 6) {
                String type = (String)DATA[0];
                Object json = DATA[1];
                String costtype = (String)DATA[2];
                Object cost = DATA[3];
                String buycompletemsg = (String)DATA[4];
                String nomoneymsg = (String)DATA[5];
                String noslotmsg = (String)DATA[6];
                if (type.equals("item")) {
                    JSONObject jo = (JSONObject) json;
                    ItemStack item = ItemStackConverter.convert(jo);
                    if(costtype.equals("money")) {
                        if (VaultHooker.economy.getBalance(event.getPlayer()) >= Double.parseDouble((String)cost)) {
                            if (event.getPlayer().getInventory().firstEmpty() != -1) {
                                event.getPlayer().getInventory().addItem(item);
                                VaultHooker.economy.withdrawPlayer(event.getPlayer(), Double.parseDouble((String)cost));
                                mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(buycompletemsg, MENU, event.getPlayer()));
                            } else {
                                mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(noslotmsg, MENU, event.getPlayer()));
                            }
                        } else {
                            mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(nomoneymsg, MENU, event.getPlayer()));
                        }
                    } else if(costtype.equals("item")) {
                        JSONObject jo2 = (JSONObject) cost;
                        ItemStack item2 = ItemStackConverter.convert(jo2);
                        if (event.getPlayer().getInventory().contains(item2)) {
                            if (event.getPlayer().getInventory().firstEmpty() != -1) {
                                event.getPlayer().getInventory().addItem(item);
                                event.getPlayer().getInventory().remove(item2);
                                mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(buycompletemsg, MENU, event.getPlayer()));
                            } else {
                                mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(noslotmsg, MENU, event.getPlayer()));
                            }
                        } else {
                            mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(nomoneymsg, MENU, event.getPlayer()));
                        }
                    } else if(costtype.equals("permission")) {
                        if(event.getPlayer().hasPermission((String)cost)) {
                            if (event.getPlayer().getInventory().firstEmpty() != -1) {
                                event.getPlayer().getInventory().addItem(item);
                                VaultHooker.permission.playerRemove(event.getPlayer(), (String)cost);
                                mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(buycompletemsg, MENU, event.getPlayer()));
                            } else {
                                mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(noslotmsg, MENU, event.getPlayer()));
                            }
                        }
                    }
                } else if (type.equals("permission")) {
                    if(!event.getPlayer().hasPermission((String)json)) {
                        if (costtype.equals("money")) {
                            if (VaultHooker.economy.getBalance(event.getPlayer()) >= Double.parseDouble((String)cost)) {
                                VaultHooker.permission.playerAdd(event.getPlayer(), (String)json);
                                mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(buycompletemsg, MENU, event.getPlayer()));
                            } else {
                                mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(nomoneymsg, MENU, event.getPlayer()));
                            }
                        } else if (costtype.equals("money")) {
                            JSONObject jo2 = (JSONObject) cost;
                            ItemStack item2 = ItemStackConverter.convert(jo2);
                            if (event.getPlayer().getInventory().contains(item2)) {
                                event.getPlayer().getInventory().remove(item2);
                                VaultHooker.permission.playerAdd(event.getPlayer(), (String)json);
                                mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(buycompletemsg, MENU, event.getPlayer()));
                            }
                        } else if(costtype.equals("permission")) {
                            if(event.getPlayer().hasPermission((String)json)) {
                                VaultHooker.permission.playerRemove(event.getPlayer(), (String)cost);
                                VaultHooker.permission.playerAdd(event.getPlayer(), (String)json);
                                mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(buycompletemsg, MENU, event.getPlayer()));
                            }
                        }
                    } else {
                        mh.sendMessage(event.getPlayer(), lh.getLanguage("existpermission"));
                    }
                }
            } else if (TYPE.equals(SELL) && DATA.length == 5) {
                String type = (String)DATA[0];
                Object sellitem = (String)DATA[1];
                String costtype = (String)DATA[2];
                Object cost = (String)DATA[3];
                String sellcompletemsg = (String)DATA[4];
                String sellfailedmsg = (String)DATA[5];
                if (type.equals("item")) {
                    JSONObject jo = (JSONObject) sellitem;
                    ItemStack item = ItemStackConverter.convert(jo);
                    if(event.getPlayer().getInventory().contains(item)) {
                        mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(sellcompletemsg, MENU, event.getPlayer()));
                        event.getPlayer().getInventory().removeItem(item);
                        if(costtype.equals("money")) {
                            VaultHooker.economy.depositPlayer(event.getPlayer(), Double.parseDouble((String)cost));
                        } else if(costtype.equals("item")) {
                            JSONParser jp2 = new JSONParser();
                            JSONObject jo2 = (JSONObject) cost;
                            ItemStack item2 = ItemStackConverter.convert(jo2);
                            event.getPlayer().getInventory().addItem(item2);
                        } else if(costtype.equals("permission")) {
                            VaultHooker.permission.playerAdd(event.getPlayer(), (String)cost);
                        }
                    } else {
                        mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(sellfailedmsg, MENU, event.getPlayer()));
                    }
                } else if (type.equals("permission")) {
                    if(event.getPlayer().hasPermission((String)sellitem)) {
                        if(costtype.equals("money")) {
                            VaultHooker.economy.depositPlayer(event.getPlayer(), Double.parseDouble((String)cost));
                            mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(sellcompletemsg, MENU, event.getPlayer()));
                            VaultHooker.permission.playerRemove(event.getPlayer(), (String)sellitem);
                        } else if(costtype.equals("item")) {
                            JSONObject jo = (JSONObject)cost;
                            ItemStack item = ItemStackConverter.convert(jo);
                            event.getPlayer().getInventory().addItem(item);
                            mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(sellcompletemsg, MENU, event.getPlayer()));
                            VaultHooker.permission.playerRemove(event.getPlayer(), (String)sellitem);
                        } else if(costtype.equals("permission")) {
                            if(!event.getPlayer().hasPermission((String)cost)) {
                                VaultHooker.permission.playerAdd(event.getPlayer(), (String)cost);
                                mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(sellcompletemsg, MENU, event.getPlayer()));
                                VaultHooker.permission.playerRemove(event.getPlayer(), (String)sellitem);
                            } else {
                                mh.sendMessage(event.getPlayer(), lh.getLanguage("existpermission"));
                            }
                        }
                    } else {
                        mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(sellfailedmsg, MENU, event.getPlayer()));
                    }
                }
            } else if (TYPE.equals(MESSAGE) && DATA.length == 1) {
                String message = (String)DATA[0];
                new MessageHandler().sendMessage(event.getPlayer(), SteakGUI.convertMessage(message, MENU, event.getPlayer()));
            } else if (TYPE.equals(GIVE) && DATA.length == 2) {
                String type = (String)DATA[0];
                Object json = DATA[1];
                if (type.equals("item")) {
                    JSONObject jo = (JSONObject) json;
                    ItemStack additem = ItemStackConverter.convert(jo);
                    if(additem.getItemMeta().getDisplayName() != null) {
                        additem.getItemMeta().setDisplayName(SteakGUI.convertMessage(additem.getItemMeta().getDisplayName(), MENU, event.getPlayer()));
                    }
                    if(additem.getItemMeta().getLore().size() > 0) {
                        List<String> lorel = additem.getItemMeta().getLore();
                        int i = 0;
                        for(String lore:lorel) {
                            lorel.add(i, SteakGUI.convertMessage(lore, MENU, event.getPlayer()));
                        }
                        additem.getItemMeta().setLore(lorel);
                    }
                    event.getPlayer().getInventory().addItem();
                } else if (type.equals("permission")) {
                    VaultHooker.permission.playerAdd(event.getPlayer(), (String)json);
                } else if (type.equals("money")) {
                    VaultHooker.economy.depositPlayer(event.getPlayer(), Double.parseDouble((String)json));
                } else if (type.equals("exp")) {
                    if (isNum((String)json)) {
                        event.getPlayer().giveExp(Integer.parseInt((String)json));
                    } else {
                        throw new Exception("Input String is Not Number");
                    }
                } else if (type.equals("level")) {
                    if (isNum((String)json)) {
                        event.getPlayer().giveExpLevels(Integer.parseInt((String)json));
                    } else {
                        throw new Exception("Input String is Not Number");
                    }
                }
            } else if (TYPE.equals(SOUND) && DATA.length == 3) {
                String sound = (String)DATA[0];
                String volume = (String)DATA[1];
                String pitch = (String)DATA[2];
                if (isFloat(volume) && isFloat(pitch)) {
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.valueOf(sound), Float.parseFloat(volume), Float.parseFloat(pitch));
                } else {
                    throw new Exception("Input String is Not Float");
                }
            } else if (TYPE.equals(BROADCAST) && DATA.length == 1) {
                String message = (String)DATA[0];
                for (Player player : BukkitUtil.allPlayers()) {
                    new MessageHandler().sendMessage(player, SteakGUI.convertMessage(message, MENU, event.getPlayer()));
                }
            } else if (TYPE.equals(CLOSE)) {
                event.setWillClose(true);
            } else if(TYPE.equals(IF) && DATA.length == 4) {
                String first = SteakGUI.convertMessage((String)DATA[0]);
                String sec = SteakGUI.convertMessage((String)DATA[1]);
                if(first.equals(sec)) {
                    JSONArray jo = (JSONArray) DATA[3];
                    for(Object task:jo) {
                        JSONObject json = (JSONObject)task;
                        ItemTaskConverter.convert(json).runTask(event, MENU);
                    }
                } else {
                    JSONArray jo = (JSONArray) DATA[4];
                    for(Object task:jo) {
                        JSONObject json = (JSONObject)task;
                        ItemTaskConverter.convert(json).runTask(event, MENU);
                    }
                }
            } else {
                SteakGUIPlugin.runTask(TYPE, DATA, event, MENU);
            }
        }
    }



    private boolean isNum(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String getType() {
        return TYPE;
    }

    public Object[] getData() {
        return DATA;
    }

    public String getClickType() {
        if(CLICKTYPE != null) {
            return CLICKTYPE.toString();
        } else {
            return "ALL";
        }
    }
}
