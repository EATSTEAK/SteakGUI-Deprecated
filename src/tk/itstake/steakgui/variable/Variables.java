package tk.itstake.steakgui.variable;

import io.netty.util.internal.StringUtil;
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
            strarray[i] = SteakGUI.convertMessage(str, menu, player);
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
                    if(isNum(data[0]) && isNum(data[1])) {
                        return parseNum(data[0]) + parseNum(data[1]) + "";
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            case "sub":
                if(data.length == 2) {
                    if(isNum(data[0]) && isNum(data[1])) {
                        return parseNum(data[0]) - parseNum(data[1]) + "";
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            case "multiply":
                if(data.length == 2) {
                    if(isNum(data[0]) && isNum(data[1])) {
                        return parseNum(data[0]) * parseNum(data[1]) + "";
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            case "divide":
                if(data.length == 2) {
                    if(isNum(data[0]) && isNum(data[1]) && parseNum(data[1]) > 0) {
                        return parseNum(data[0]) / parseNum(data[1]) + "";
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            case "if":
                if(data.length == 4) {
                    String syntax = VariableConverter.convert(data[0], menu, player);
                    if(syntaxConvert(syntax)) {
                        return VariableConverter.convert(data[1], menu, player);
                    } else {
                        return "";
                    }
                } else if(data.length == 5) {
                    String syntax = VariableConverter.convert(data[0], menu, player);
                    if(syntaxConvert(syntax)) {
                        return VariableConverter.convert(data[2], menu, player);
                    } else {
                        return VariableConverter.convert(data[3], menu, player);
                    }
                } else {
                    return null;
                }
            case "money":
                return VaultHooker.economy.getBalance(player.getName()) + "";
            case "prefix":
                return VaultHooker.chat.getPlayerPrefix(player);
            case "suffix":
                return VaultHooker.chat.getPlayerPrefix(player);
            case "integer":
                if(data.length == 1 && isNum(data[0])) {
                    return parseNum(data[0]) + "";
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

    public static boolean syntaxConvert(String syntax) {
        String[] orsplited = syntax.split("\\|\\|");
        for(String st:orsplited) {
            String[] andsp = st.split("&&");
            ArrayList<Boolean> andbool = new ArrayList<>();
            for(String sp:andsp) {
                andbool.add(ifConvert(sp));
            }
            if(!andbool.contains(false)) {
                return true;
            }
        }
        return false;
    }

    private static boolean ifConvert(String sp) {
        String[] targetmath = new String[]{"==", "!=", "<=", ">=", "?=", ">", "<"};
        for(String math:targetmath) {
            String[] splited = sp.split(math);
            if(splited.length == 2) {
                String one = splited[0].trim();
                String two = splited[1].trim();
                switch(math) {
                    case "==":
                        return one.equals(two);
                    case "!=":
                        return !one.equals(two);
                    case "<=":
                        return (parseNum(one) <= parseNum(two));
                    case ">=":
                        return (parseNum(one) >= parseNum(two));
                    case "?=":
                        return one.contains(two);
                    case ">":
                        return (parseNum(one) > parseNum(two));
                    case "<":
                        return (parseNum(one) < parseNum(two));
                    default:
                        return false;
                }
            }
        }
        return false;
    }


    private static double parseNum(String cost) {
        if(isInt(cost)) {
            return Integer.parseInt(cost);
        } else {
            return Double.parseDouble(cost);
        }
    }

    private static boolean isNum(String cost) {
        if(isInt(cost)) {
            return true;
        } else if(isDouble(cost)) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isDouble(String cost) {
        try {
            Double.parseDouble(cost);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
