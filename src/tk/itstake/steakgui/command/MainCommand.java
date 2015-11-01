/*
 * MainCommand.java
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

package tk.itstake.steakgui.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.gui.GUIItem;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.itemtask.ItemTask;
import tk.itstake.steakgui.menueditor.EditorMain;
import tk.itstake.steakgui.util.MenuFileHandler;
import tk.itstake.util.ConfigHandler;
import tk.itstake.util.LanguageHandler;
import tk.itstake.util.MessageHandler;

import java.util.HashMap;

/**
 * Created by bexco on 2015-07-26.
 */
public class MainCommand {
    static MessageHandler mh = new MessageHandler();
    static LanguageHandler lh = new LanguageHandler();
    public static boolean runCmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length <= 0) {
            helpCmd(sender, label);
            return true;
        } else if(args.length == 1) {
            switch(args[0]) {
                case "help":
                    helpCmd(sender, label);
                    return true;
                case "open":
                    helpCmd(sender, label, "open");
                    return true;
                case "setting":
                    helpCmd(sender, label, "setting");
                    return true;
                case "create":
                    helpCmd(sender, label, "create");
                    return true;
                case "delete":
                    helpCmd(sender, label, "delete");
                    return true;
                case "list":
                    listCmd(sender);
                    return true;
                case "reload":
                    reloadCmd(sender);
                    return true;
                default:
                    helpCmd(sender, label);
                    return true;
            }
        } else if(args.length == 2) {
            switch(args[0]) {
                case "help":
                    helpCmd(sender, label, args[1]);
                    return true;
                case "open":
                    openCmd(sender, label, args[1]);
                    return true;
                case "setting":
                    settingCmd(sender, label, args[1]);
                    return true;
                case "create":
                    createCmd(sender, label, args[1]);
                    return true;
                case "delete":
                    deleteCmd(sender, label, args[1]);
                    return true;
                default:
                    helpCmd(sender, label);
                    return true;
            }
        } else if(args.length == 3 && args[0].equals("open"))  {
            openCmd(sender, label, args[1], args[2]);
            return true;
        }
        return false;
    }

    private static void reloadCmd(CommandSender sender) {
        ConfigHandler.loadConfig();
        SteakGUI.lh.languageLoad();
        MenuFileHandler.reloadMenu();
        mh.sendMessage(sender, lh.getLanguage("command.reloaded"));
    }

    private static void deleteCmd(CommandSender sender, String name, String arg) {
        if((sender instanceof Player && sender.hasPermission("steakgui.setting")) || !(sender instanceof Player)) {
            MenuFileHandler.deleteMenu(arg);
            mh.sendMessage(sender, lh.getLanguage("menu.deleted", new String[]{arg}));
        } else {
            mh.sendMessage(sender, lh.getLanguage("nopermission"));
        }
    }

    private static void createCmd(CommandSender sender, String name, String arg) {
        if((sender instanceof Player && sender.hasPermission("steakgui.setting")) || !(sender instanceof Player)) {
            if(arg.matches("^[a-zA-Z1-9_]*$")) {
                HashMap<Integer, GUIItem> itemarray = new HashMap<>();
                ItemStack defaultitem = new ItemStack(Material.STONE, 1);
                itemarray.put(0, new GUIItem(defaultitem, "", new ItemTask(ItemTask.MESSAGE, new String[]{lh.getLanguage("menu.noitemtask")})));
                Menu newmenu = new Menu(arg, arg, 1, itemarray);
                MenuFileHandler.saveMenu(newmenu);
                mh.sendMessage(sender, lh.getLanguage("menu.created", new String[]{arg}));
            } else {
                mh.sendMessage(sender, lh.getLanguage("command.notmatchedformat"));
            }
        } else {
            mh.sendMessage(sender, lh.getLanguage("nopermission"));
        }
    }

    private static void settingCmd(CommandSender sender, String name, String arg) {
        if(sender instanceof Player && sender.hasPermission("steakgui.setting")) {
            if(MenuFileHandler.isMenu(arg)) {
                new EditorMain().show(MenuFileHandler.loadMenu(arg), (Player)sender);
            } else {
                mh.sendMessage(sender, lh.getLanguage("notexistmenu"));
            }
        } else {
            mh.sendMessage(sender, lh.getLanguage("nopermission"));
        }
    }

    private static void openCmd(CommandSender sender, String name, String arg, String arg1) {
        if(sender instanceof Player && sender.getName().equals(arg1)) {
            final Player target = Bukkit.getPlayer(arg1);
            if(target.hasPermission("steakgui.open")) {
                if(MenuFileHandler.isMenu(arg)) {
                    final Menu targetmenu = MenuFileHandler.loadMenu(arg);
                    if (targetmenu != null) {
                        Bukkit.getScheduler().runTaskLater(SteakGUI.p, new Runnable() {
                            @Override
                            public void run() {
                                targetmenu.open(target);
                            }
                        }, 2);

                    } else {
                        mh.sendMessage(target, lh.getLanguage("menu.notexist"));
                    }
                } else {
                    mh.sendMessage(target, lh.getLanguage("menu.notexist"));
                }
            } else {
                mh.sendMessage(target, lh.getLanguage("nopermission"));
            }
        } else if(!sender.getName().equals(arg1)) {
            if((sender instanceof  Player && sender.hasPermission("steakgui.open.others")) || !(sender instanceof Player)) {
                Player target = Bukkit.getPlayer(arg1);
                if(target.isOnline()) {
                    if(MenuFileHandler.isMenu(arg)) {
                        Menu targetmenu = MenuFileHandler.loadMenu(arg);
                        if (targetmenu != null) {
                            targetmenu.open(target);
                        } else {
                            mh.sendMessage(target, lh.getLanguage("menu.notexist"));
                        }
                    } else {
                        mh.sendMessage(target, lh.getLanguage("menu.notexist"));
                    }
                } else {
                    mh.sendMessage(target, lh.getLanguage("offlineplayer"));
                }
            } else {
                mh.sendMessage(sender, lh.getLanguage("nopermission"));
            }
        } else {
            mh.sendMessage(sender, lh.getLanguage("noconsole"));
        }
    }

    private static void openCmd(CommandSender sender, String name, String arg) {
        if(sender instanceof Player) {
            openCmd(sender, name, arg, sender.getName());
        } else {
            mh.sendMessage(sender, lh.getLanguage("noconsole"));
        }
    }

    private static void listCmd(CommandSender sender) {
        if(sender instanceof Player && !((Player)sender).hasPermission("steakgui.setting")) {
            mh.sendMessage(sender, lh.getLanguage("nopermission"));
        } else {
            mh.sendMessage(sender, lh.getLanguage("command.list.intro"));
            int ai = 0;
            for (String menu : MenuFileHandler.listMenu()) {
                mh.sendMessage(sender, lh.getLanguage("command.list.format", new String[]{menu}));
                ai++;
            }
            if (ai == 0) {
                mh.sendMessage(sender, lh.getLanguage("command.list.nomenu"));
            }
        }
    }

    private static void helpCmd(CommandSender sender, String cmd) {
        helpCmd(sender, cmd, "help");
    }

    private static void helpCmd(CommandSender sender, String cmd, String label) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("SteakGUI");
        if(label.equals("help")) {
            mh.sendMessage(sender, lh.getLanguage("command.help.intro", new String[]{plugin.getDescription().getVersion()}));
            if(sender instanceof Player) {
                Player player = (Player)sender;
                if(player.hasPermission("steakgui.open")) {
                    mh.sendMessage(sender, lh.getLanguage("command.help.open", new String[]{cmd}));
                } else if(player.hasPermission("steakgui.open.others")) {
                    mh.sendMessage(sender, lh.getLanguage("command.help.open.others", new String[]{cmd}));
                }
                if(player.hasPermission("steakgui.admin")) {
                    mh.sendMessage(sender, lh.getLanguage("command.help.setting", new String[]{cmd}));
                    mh.sendMessage(sender, lh.getLanguage("command.help.create", new String[]{cmd}));
                    mh.sendMessage(sender, lh.getLanguage("command.help.delete", new String[]{cmd}));
                    mh.sendMessage(sender, lh.getLanguage("command.help.list", new String[]{cmd}));
                    mh.sendMessage(sender, lh.getLanguage("command.help.reload", new String[]{cmd}));
                }
            } else {
                mh.sendMessage(sender, lh.getLanguage("command.help.open.console", new String[]{cmd}));
                mh.sendMessage(sender, lh.getLanguage("command.help.create", new String[]{cmd}));
                mh.sendMessage(sender, lh.getLanguage("command.help.delete", new String[]{cmd}));
                mh.sendMessage(sender, lh.getLanguage("command.help.list", new String[]{cmd}));
                mh.sendMessage(sender, lh.getLanguage("command.help.reload", new String[]{cmd}));
            }
            mh.sendMessage(sender, lh.getLanguage("command.help.author", new String[]{plugin.getDescription().getVersion()}));
        } else if(label.equals("open")) {
            if(sender instanceof Player) {
                Player player = (Player)sender;
                if(player.hasPermission("steakgui.open")) {
                    if(player.hasPermission("steakgui.open.others")) {
                        mh.sendMessage(player, lh.getLanguage("command.wronguse", new String[]{"/" + cmd + " " + label + " <" + lh.getLanguage("command.wronghelp.arg.menu") + "> [" + lh.getLanguage("command.wronghelp.arg.player") + "]"}));
                    } else {
                        mh.sendMessage(player, lh.getLanguage("command.wronguse", new String[]{"/" + cmd + " " + label + " <" + lh.getLanguage("command.wronghelp.arg.menu") + ">"}));
                    }
                } else {
                    mh.sendMessage(player, lh.getLanguage("nopermission"));
                }
            } else {
                mh.sendConsoleMessage(lh.getLanguage("command.wronguse", new String[]{"/" + cmd + " " + label + " <" + lh.getLanguage("command.wronghelp.arg.menu") + "> <" + lh.getLanguage("command.wronghelp.arg.player") + ">"}));
            }
        } else if(label.equals("setting")) {
            if(sender instanceof Player) {
                Player player = (Player)sender;
                if(player.hasPermission("steakgui.setting")) {
                        mh.sendMessage(player, lh.getLanguage("command.wronguse", new String[]{"/" + cmd + " " + label + " <" + lh.getLanguage("command.wronghelp.arg.menu") + ">"}));
                } else {
                    mh.sendMessage(player, lh.getLanguage("nopermission"));
                }
            } else {
                mh.sendConsoleMessage(lh.getLanguage("noconsole"));
            }
        } else if(label.equals("create")) {
            if(sender instanceof Player) {
                Player player = (Player)sender;
                if(player.hasPermission("steakgui.setting")) {
                    mh.sendMessage(player, lh.getLanguage("command.wronguse", new String[]{"/" + cmd + " " + label + " <" + lh.getLanguage("command.wronghelp.arg.menu") + ">"}));
                } else {
                    mh.sendMessage(player, lh.getLanguage("nopermission"));
                }
            } else {
                mh.sendConsoleMessage(lh.getLanguage("command.wronguse", new String[]{"/" + cmd + " " + label + " <" + lh.getLanguage("command.wronghelp.arg.menu") + ">"}));
            }
        } else if(label.equals("delete")) {
            if(sender instanceof Player) {
                Player player = (Player)sender;
                if(player.hasPermission("steakgui.setting")) {
                    mh.sendMessage(player, lh.getLanguage("command.wronguse", new String[]{"/" + cmd + " " + label + " <" + lh.getLanguage("command.wronghelp.arg.menu") + ">"}));
                } else {
                    mh.sendMessage(player, lh.getLanguage("nopermission"));
                }
            } else {
                mh.sendConsoleMessage(lh.getLanguage("command.wronguse", new String[]{"/" + cmd + " " + label + " <" + lh.getLanguage("command.wronghelp.arg.menu") + ">"}));
            }
        }
    }
}
