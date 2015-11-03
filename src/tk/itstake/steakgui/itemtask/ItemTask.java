/*
 * ItemTask.java
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

package tk.itstake.steakgui.itemtask;


import ninja.amp.ampmenus.events.ItemClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.util.*;
import tk.itstake.util.BukkitUtil;
import tk.itstake.util.LanguageHandler;
import tk.itstake.util.MessageHandler;

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
    public static String TAKE = "take";
    public static String SOUND = "sound";
    public static String BROADCAST = "broadcast";
    public static String CLOSE = "close";
    public static String UPDATE = "update";
    public static String IF = "if";
    String TYPE = "";
    Object[] DATA = null;
    ArrayList<ClickType> CLICKTYPE = null;
    private MessageHandler mh = new MessageHandler();
    private LanguageHandler lh = new LanguageHandler();
    public ItemTask(String type, Object[] data, ArrayList<ClickType> clickType) {
        TYPE = type;
        DATA = data;
        CLICKTYPE = clickType;
    }

    public ItemTask(String type, Object[] data) {
        TYPE = type;
        DATA = data;
        CLICKTYPE = null;
    }

    public void runTask(final ItemClickEvent event, final Menu MENU) throws Exception {
        JSONParser parser = new JSONParser();
        int i = 0;
        for(Object data:DATA) {
            DATA[i] = SteakGUI.convertMessage((String)data, MENU, event.getPlayer());
            i++;
        }
        if(CLICKTYPE == null || CLICKTYPE.contains(event.getClick())) {
            if (TYPE.equals(COMMAND) && DATA.length == 2) {
                String permission = (String)DATA[0];
                String command = (String)DATA[1];
                event.getPlayer().setMetadata("SGCmd", new FixedMetadataValue(Bukkit.getServer().getPluginManager().getPlugin("SteakGUI"), SteakGUI.convertMessage(command, MENU, event.getPlayer())));
                if (permission.equals("op") && !event.getPlayer().isOp()) {
                    event.getPlayer().setOp(true);
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerCommandPreprocessEvent(event.getPlayer(), SteakGUI.convertMessage(command, MENU, event.getPlayer())));
                    event.getPlayer().setOp(false);
                } else if (permission.equals("console")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.substring(1));
                } else {
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerCommandPreprocessEvent(event.getPlayer(), SteakGUI.convertMessage(command, MENU, event.getPlayer())));
                }
            } else if (TYPE.equals(OPEN_MENU) && DATA.length == 1) {
                final String menuname = (String)DATA[0];
                event.setWillClose(true);
                Bukkit.getServer().getScheduler().runTaskLater(SteakGUI.p, new Runnable() {
                    @Override
                    public void run() {
                        Menu openmenu = MenuFileHandler.loadMenu(SteakGUI.convertMessage(menuname, MENU, event.getPlayer()));
                        openmenu.open(event.getPlayer());
                    }
                }, 2);

            } else if (TYPE.equals(BUY) && DATA.length == 7) {
                String type = (String)DATA[0];
                String json = (String)DATA[1];
                String costtype = (String)DATA[2];
                String cost = (String)DATA[3];
                String buycompletemsg = (String)DATA[4];
                String nomoneymsg = (String)DATA[5];
                String noslotmsg = (String)DATA[6];
                if (type.equals("item")) {
                    JSONObject jo = null;
                    try {
                        jo = (JSONObject) parser.parse(json);
                    } catch(Exception e) {
                        mh.sendMessage(event.getPlayer(), lh.getLanguage("menu.wrongsetting"));
                        e.printStackTrace();
                        return;
                    }
                    ItemStack item = ItemStackConverter.convert(jo);
                    if(costtype.equals("money")) {
                        if (VaultHooker.economy.getBalance(event.getPlayer().getName()) >= parseCost((String) cost)) {
                            if (event.getPlayer().getInventory().firstEmpty() != -1) {
                                event.getPlayer().getInventory().addItem(item);
                                VaultHooker.economy.withdrawPlayer(event.getPlayer().getName(), parseCost((String) cost));
                                if(!buycompletemsg.equals("")) {
                                    mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(buycompletemsg, MENU, event.getPlayer()));
                                }
                            } else {
                                if(!noslotmsg.equals("")) {
                                    mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(noslotmsg, MENU, event.getPlayer()));
                                }
                            }
                        } else {
                            if(!nomoneymsg.equals("")) {
                                mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(nomoneymsg, MENU, event.getPlayer()));
                            }
                        }
                    } else if(costtype.equals("item")) {
                        JSONObject jo2 = null;
                        try {
                            jo2 = (JSONObject) parser.parse(cost);
                        } catch(Exception e) {
                            mh.sendMessage(event.getPlayer(), lh.getLanguage("menu.wrongsetting"));
                            e.printStackTrace();
                            return;
                        }
                        ItemStack item2 = ItemStackConverter.convert(jo2);
                        if (BukkitUtil.hasItem(event.getPlayer().getInventory(), item2)) {
                            if (event.getPlayer().getInventory().firstEmpty() != -1) {
                                event.getPlayer().getInventory().setContents(BukkitUtil.removeItem(event.getPlayer().getInventory(), item2).getContents());
                                event.getPlayer().getInventory().addItem(item);
                                if(!buycompletemsg.equals("")) {
                                    mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(buycompletemsg, MENU, event.getPlayer()));
                                }
                            } else {
                                if(!noslotmsg.equals("")) {
                                    mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(noslotmsg, MENU, event.getPlayer()));
                                }
                            }
                        } else {
                            if(!nomoneymsg.equals("")) {
                                mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(nomoneymsg, MENU, event.getPlayer()));
                            }
                        }
                    } else if(costtype.equals("permission")) {
                        if(event.getPlayer().hasPermission((String)cost)) {
                            if (event.getPlayer().getInventory().firstEmpty() != -1) {
                                event.getPlayer().getInventory().addItem(item);
                                VaultHooker.permission.playerRemove(event.getPlayer(), (String)cost);
                                if(!buycompletemsg.equals("")) {
                                    mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(buycompletemsg, MENU, event.getPlayer()));
                                }
                            } else {
                                if(!noslotmsg.equals("")) {
                                    mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(noslotmsg, MENU, event.getPlayer()));
                                }
                            }
                        } else {
                            if(!nomoneymsg.equals("")) {
                                mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(nomoneymsg, MENU, event.getPlayer()));
                            }
                        }
                    }
                } else if (type.equals("permission")) {
                    if(!event.getPlayer().hasPermission((String)json)) {
                        if (costtype.equals("money")) {
                            if (VaultHooker.economy.getBalance(event.getPlayer().getName()) >= parseCost((String) cost)) {
                                VaultHooker.permission.playerAdd(event.getPlayer(), (String)json);
                                if(!buycompletemsg.equals("")) {
                                    mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(buycompletemsg, MENU, event.getPlayer()));
                                }
                            } else {
                                if(!nomoneymsg.equals("")) {
                                    mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(nomoneymsg, MENU, event.getPlayer()));
                                }
                            }
                        } else if (costtype.equals("item")) {
                            JSONObject jo2 = null;
                            try {
                                jo2 = (JSONObject) parser.parse(cost);
                            } catch(Exception e) {
                                mh.sendMessage(event.getPlayer(), lh.getLanguage("menu.wrongsetting"));
                                e.printStackTrace();
                                return;
                            }
                            ItemStack item2 = ItemStackConverter.convert(jo2);
                            if (BukkitUtil.hasItem(event.getPlayer().getInventory(), item2)) {
                                event.getPlayer().getInventory().setContents(BukkitUtil.removeItem(event.getPlayer().getInventory(), item2).getContents());
                                VaultHooker.permission.playerAdd(event.getPlayer(), (String)json);
                                if(!buycompletemsg.equals("")) {
                                    mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(buycompletemsg, MENU, event.getPlayer()));
                                }
                            }
                        } else if(costtype.equals("permission")) {
                            if(event.getPlayer().hasPermission((String)cost)) {
                                VaultHooker.permission.playerRemove(event.getPlayer(), (String)cost);
                                VaultHooker.permission.playerAdd(event.getPlayer(), (String)json);
                                if(!buycompletemsg.equals("")) {
                                    mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(buycompletemsg, MENU, event.getPlayer()));
                                }
                            } else {
                                if(!nomoneymsg.equals("")) {
                                    mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(nomoneymsg, MENU, event.getPlayer()));
                                }
                            }
                        }
                    } else {
                        mh.sendMessage(event.getPlayer(), lh.getLanguage("existpermission"));
                    }
                }
            } else if (TYPE.equals(SELL) && DATA.length == 6) {
                String type = (String)DATA[0];
                String sellitem = (String)DATA[1];
                String costtype = (String)DATA[2];
                String cost = (String)DATA[3];
                String sellcompletemsg = (String)DATA[4];
                String sellfailedmsg = (String)DATA[5];
                if (type.equals("item")) {
                    JSONObject jo = null;
                    try {
                        jo = (JSONObject) parser.parse(sellitem);
                    } catch(Exception e) {
                        mh.sendMessage(event.getPlayer(), lh.getLanguage("menu.wrongsetting"));
                        e.printStackTrace();
                        return;
                    }
                    ItemStack item = ItemStackConverter.convert(jo);
                    if(BukkitUtil.hasItem(event.getPlayer().getInventory(), item)) {
                        event.getPlayer().getInventory().setContents(BukkitUtil.removeItem(event.getPlayer().getInventory(), item).getContents());
                        if(costtype.equals("money")) {
                            VaultHooker.economy.depositPlayer(event.getPlayer().getName(), parseCost((String) cost));
                            if(!sellcompletemsg.equals("")) {
                                mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(sellcompletemsg, MENU, event.getPlayer()));
                            }
                        } else if(costtype.equals("item")) {
                            JSONParser jp2 = new JSONParser();
                            JSONObject jo2 = null;
                            try {
                                jo2 = (JSONObject) parser.parse(cost);
                            } catch(Exception e) {
                                mh.sendMessage(event.getPlayer(), lh.getLanguage("menu.wrongsetting"));
                                e.printStackTrace();
                                return;
                            }
                            ItemStack item2 = ItemStackConverter.convert(jo2);
                            event.getPlayer().getInventory().addItem(item2);
                            if(!sellcompletemsg.equals("")) {
                                mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(sellcompletemsg, MENU, event.getPlayer()));
                            }
                        } else if(costtype.equals("permission")) {
                            VaultHooker.permission.playerAdd(event.getPlayer(), (String)cost);
                            if(!sellcompletemsg.equals("")) {
                                mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(sellcompletemsg, MENU, event.getPlayer()));
                            }
                        }
                    } else {
                        if(!sellfailedmsg.equals("")) {
                            mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(sellfailedmsg, MENU, event.getPlayer()));
                        }
                    }
                } else if (type.equals("permission")) {
                    if(event.getPlayer().hasPermission((String)sellitem)) {
                        if(costtype.equals("money")) {
                            VaultHooker.economy.depositPlayer(event.getPlayer().getName(), parseCost((String) cost));
                            VaultHooker.permission.playerRemove(event.getPlayer(), (String)sellitem);
                        } else if(costtype.equals("item")) {
                            JSONObject jo = null;
                            try {
                                jo = (JSONObject) parser.parse(cost);
                            } catch(Exception e) {
                                mh.sendMessage(event.getPlayer(), lh.getLanguage("menu.wrongsetting"));
                                e.printStackTrace();
                                return;
                            }
                            ItemStack item = ItemStackConverter.convert(jo);
                            event.getPlayer().getInventory().addItem(item);
                            VaultHooker.permission.playerRemove(event.getPlayer(), (String)sellitem);
                        } else if(costtype.equals("permission")) {
                            if(!event.getPlayer().hasPermission((String)cost)) {
                                VaultHooker.permission.playerAdd(event.getPlayer(), (String)cost);
                                VaultHooker.permission.playerRemove(event.getPlayer(), (String)sellitem);
                            }
                        }
                        if(!sellcompletemsg.equals("")) {
                            mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(sellcompletemsg, MENU, event.getPlayer()));
                        }
                    } else {
                        if(!sellfailedmsg.equals("")) {
                            mh.sendMessage(event.getPlayer(), SteakGUI.convertMessage(sellfailedmsg, MENU, event.getPlayer()));
                        }
                    }
                }
            } else if (TYPE.equals(MESSAGE) && DATA.length == 1) {
                String message = (String)DATA[0];
                new MessageHandler().sendMessage(event.getPlayer(), SteakGUI.convertMessage(message, MENU, event.getPlayer()));
            } else if (TYPE.equals(GIVE) && DATA.length == 2) {
                String type = (String)DATA[0];
                String json = (String)DATA[1];
                if (type.equals("item")) {
                    JSONObject jo = null;
                    try {
                        jo = (JSONObject) parser.parse(json);
                    } catch(Exception e) {
                        mh.sendMessage(event.getPlayer(), lh.getLanguage("menu.wrongsetting"));
                        e.printStackTrace();
                        return;
                    }
                    ItemStack additem = ItemStackConverter.convert(jo);
                    if(additem.getItemMeta().getDisplayName() != null) {
                        additem.getItemMeta().setDisplayName(SteakGUI.convertMessage(additem.getItemMeta().getDisplayName(), MENU, event.getPlayer()));
                    }
                    if(additem.getItemMeta().getLore() != null && additem.getItemMeta().getLore().size() > 0) {
                        List<String> lorel = additem.getItemMeta().getLore();
                        i = 0;
                        for(String lore:lorel) {
                            lorel.add(i, SteakGUI.convertMessage(lore, MENU, event.getPlayer()));
                        }
                        additem.getItemMeta().setLore(lorel);
                    }
                    event.getPlayer().getInventory().addItem(additem);
                } else if (type.equals("permission")) {
                    VaultHooker.permission.playerAdd(event.getPlayer(), (String)json);
                } else if (type.equals("money")) {
                    VaultHooker.economy.depositPlayer(event.getPlayer().getName(), parseCost((String)json));
                } else if (type.equals("exp")) {
                    if (isNum((String)json)) {
                        event.getPlayer().giveExp(Integer.parseInt((String) json));
                    } else {
                        throw new Exception("Input String is Not Number");
                    }
                } else if (type.equals("level")) {
                    if (isNum((String) json)) {
                        event.getPlayer().giveExpLevels(Integer.parseInt((String) json));
                    } else {
                        throw new Exception("Input String is Not Number");
                    }
                }
            } else if (TYPE.equals(TAKE) && DATA.length == 2) {
                String type = (String)DATA[0];
                String json = (String)DATA[1];
                if (type.equals("item")) {
                    JSONObject jo = null;
                    try {
                        jo = (JSONObject) parser.parse(json);
                    } catch(Exception e) {
                        mh.sendMessage(event.getPlayer(), lh.getLanguage("menu.wrongsetting"));
                        e.printStackTrace();
                        return;
                    }
                    ItemStack removeitem = ItemStackConverter.convert(jo);
                    if(removeitem.getItemMeta().getDisplayName() != null) {
                        removeitem.getItemMeta().setDisplayName(SteakGUI.convertMessage(removeitem.getItemMeta().getDisplayName(), MENU, event.getPlayer()));
                    }
                    if(removeitem.getItemMeta().getLore() != null && removeitem.getItemMeta().getLore().size() > 0) {
                        List<String> lorel = removeitem.getItemMeta().getLore();
                        i = 0;
                        for(String lore:lorel) {
                            lorel.add(i, SteakGUI.convertMessage(lore, MENU, event.getPlayer()));
                        }
                        removeitem.getItemMeta().setLore(lorel);
                    }
                    event.getPlayer().getInventory().setContents(BukkitUtil.removeItem(event.getPlayer().getInventory(), removeitem).getContents());
                } else if (type.equals("permission")) {
                    VaultHooker.permission.playerRemove(event.getPlayer(), (String)json);
                } else if (type.equals("money")) {
                    VaultHooker.economy.withdrawPlayer(event.getPlayer().getName(), parseCost((String)json));
                } else if (type.equals("exp")) {
                    if (isNum((String)json)) {
                        event.getPlayer().giveExp(Integer.parseInt((String)json) * -1);
                    } else {
                        throw new Exception("Input String is Not Number");
                    }
                } else if (type.equals("level")) {
                    if (isNum((String)json)) {
                        event.getPlayer().giveExpLevels(Integer.parseInt((String)json) * -1);
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
            } else if (TYPE.equals(UPDATE)) {
                event.setWillClose(true);
                Bukkit.getServer().getScheduler().runTaskLater(SteakGUI.p, new Runnable() {
                    @Override
                    public void run() {
                        MENU.open(event.getPlayer());
                    }
                }, 2);
            } else if(TYPE.equals(IF) && DATA.length == 3) {
                String first = SteakGUI.convertMessage((String)DATA[0]);
                if(first.equals("true")) {
                    JSONArray jo = (JSONArray) DATA[2];
                    for(Object task:jo) {
                        JSONObject json = (JSONObject)task;
                        ItemTaskConverter.convert(json).runTask(event, MENU);
                    }
                } else {
                    JSONArray jo = (JSONArray) DATA[3];
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

    public ArrayList<ClickType> getClickType() {
        if(CLICKTYPE != null) {
            return CLICKTYPE;
        } else {
            return null;
        }
    }

    public void setClickType(ArrayList<ClickType> type) {
        CLICKTYPE = type;
    }

    private double parseCost(String cost) {
        if(isNum(cost)) {
            return Integer.parseInt(cost);
        } else {
            return Double.parseDouble(cost);
        }
    }

    private boolean isDouble(String cost) {
        try {
            Double.parseDouble(cost);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean isCost(String cost) {
        if(isNum(cost)) {
            return true;
        } else if(isDouble(cost)) {
            return true;
        } else {
            return false;
        }
    }
}
