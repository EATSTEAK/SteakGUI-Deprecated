/*
 * SteakGUIPlugin.java
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

package tk.itstake.steakgui.util;

import ninja.amp.ampmenus.events.ItemClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.gui.Menu;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by ITSTAKE on 2015-08-13.
 */
public class SteakGUIPlugin {
    public static void runTask(String type, Object[] data, ItemClickEvent event, Menu MENU) {
        SteakGUI p = (SteakGUI) SteakGUI.p;
        for(String plugin:p.getPluginList()) {
            if(Bukkit.getPluginManager().getPlugin(plugin) != null) {
                try {
                    Bukkit.getPluginManager().getPlugin(plugin).getClass().getMethod("runTask", new Class[]{String.class, Object[].class, ItemClickEvent.class, Menu.class}).invoke(Bukkit.getPluginManager().getPlugin(plugin), type, data, event, MENU);
                } catch (InvocationTargetException e) {
                } catch (NoSuchMethodException e) {
                } catch (IllegalAccessException e) {
                }
            }
        }
    }

    public static String getVariables(String var, String[] data, Menu menu, Player player) {
        SteakGUI p = (SteakGUI) SteakGUI.p;
        ArrayList<String> varlist = new ArrayList<>();
        for(String plugin:p.getPluginList()) {
            if(Bukkit.getPluginManager().getPlugin(plugin) != null) {
                try {
                    varlist.add((String) Bukkit.getPluginManager().getPlugin(plugin).getClass().getMethod("getVariables", new Class[]{String.class, String[].class, Menu.class, Player.class}).invoke(Bukkit.getPluginManager().getPlugin(plugin), var, data, menu, player));
                } catch (InvocationTargetException e) {
                } catch (NoSuchMethodException e) {
                } catch (IllegalAccessException e) {
                }
            }
        }
        if(varlist.size() == 1) {
            return varlist.get(0);
        } else if(varlist.size() > 1) {
            try {
                throw new Exception("So Many Variables");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }
}
