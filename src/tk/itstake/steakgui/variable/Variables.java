package tk.itstake.steakgui.variable;

import ninja.amp.ampmenus.events.ItemClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.util.SteakGUIPlugin;
import tk.itstake.steakgui.util.VaultHooker;
import tk.itstake.util.BukkitUtil;
import tk.itstake.util.MessageHandler;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ITSTAKE on 2015-08-11.
 */
public class Variables {
    HashMap<String, String[]> placeholders = new HashMap<>();
    public Variables() {
        placeholders.put("player", new String[]{});
    }

    public boolean isPlaceHolder(String placeholder) {
        if(placeholders.containsKey(placeholder)) {
            return true;
        } else {
            return false;
        }
    }

    public String convert(String placeholder, Menu menu, Player player) {
        return convert(placeholder, new String[]{}, menu, player);
    }

    public String convert(String placeholder, ArrayList<String> data, Menu menu, Player player) {
        String[] strarray = new String[data.size()];
        int i = 0;
        for(String str:data) {
            strarray[i] = str;
            i++;
        }
        return convert(placeholder, strarray, menu, player);
    }

    public String convert(String var, String[] data, Menu menu, Player player) {
        switch(var) {
            case "player":
                return player.getName();
            case "playerdisplay":
                return player.getDisplayName();
            case "playerlist":
                return player.getPlayerListName();
            case "playerexp":
                return player.getExp() + "";
            case "playerlevel":
                return player.getLevel() + 0.0 + "";
            case "playerleftexp":
                return player.getExpToLevel() + 0.0 + "";
            case "playertotalexp":
                return player.getTotalExperience() + 0.0 + "";
            case "playerfood":
                return player.getFoodLevel() + 0.0 + "";
            case "playerhealth":
                return player.getHealth() + "";
            case "playermaxhealth":
                return player.getMaxHealth() + "";
            case "playergamemode":
                return player.getGameMode().toString();
            case "menutitle":
                return menu.getTitle();
            case "add":
                if(data.length == 2) {
                    if(isDoubleable(data[0]) && isDoubleable(data[1])) {
                        return toDouble(data[0]) + toDouble(data[1]) + "";
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            case "sub":
                if(data.length == 2) {
                    if(isDoubleable(data[0]) && isDoubleable(data[1])) {
                        return toDouble(data[0]) - toDouble(data[1]) + "";
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            case "multiply":
                if(data.length == 2) {
                    if(isDoubleable(data[0]) && isDoubleable(data[1])) {
                        return toDouble(data[0]) * toDouble(data[1]) + "";
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            case "divide":
                if(data.length == 2) {
                    if(isDoubleable(data[0]) && isDoubleable(data[1]) && toDouble(data[1]) > 0) {
                        return toDouble(data[0]) / toDouble(data[1]) + "";
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            case "if":
                new MessageHandler().sendConsoleMessage("&bDebug: &r" + data.length);
                if(data.length == 3) {
                    String first = VariableConverter.convert(data[0], menu, player);
                    String sec = VariableConverter.convert(data[1], menu, player);
                    if(first.equals(sec)) {
                        return VariableConverter.convert(data[2], menu, player);
                    } else {
                        return null;
                    }
                } else if(data.length == 4) {
                    String first = VariableConverter.convert(data[0], menu, player);
                    String sec = VariableConverter.convert(data[1], menu, player);
                    if(first.equals(sec)) {
                        return VariableConverter.convert(data[2], menu, player);
                    } else {
                        return VariableConverter.convert(data[3], menu, player);
                    }
                } else {
                    return null;
                }
            case "money":
                return VaultHooker.economy.getBalance((OfflinePlayer)player) + "";
            case "prefix":
                return VaultHooker.chat.getPlayerPrefix(player);
            case "suffix":
                return VaultHooker.chat.getPlayerPrefix(player);
            case "integer":
                if(data.length == 1 && isDoubleable(data[0])) {
                    return toDouble(data[0]).intValue() + "";
                } else {
                    return null;
                }
            case "replace":
                if(data.length == 3) {
                    return data[0].replace(data[1], data[2]);
                } else {
                    return null;
                }
            case "maxplayers":
                return Bukkit.getServer().getMaxPlayers() + "";
            case "currentplayers":
                return BukkitUtil.allPlayers().length + "";
            default:
                return SteakGUIPlugin.getVariables(var, data, menu, player);
        }
    }


    private boolean isDoubleable(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            try {
                Double.parseDouble(str);
                return true;
            } catch (Exception e2) {
                return false;
            }
        }
    }

    private Double toDouble(String str) {
        double d = 0.0;
        try {
            d = Integer.parseInt(str) + 0.0;
        } catch (Exception e) {
            d = Double.parseDouble(str);
        }
        return d;
    }
}
