/*
 * VariableConverter.java
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

package tk.itstake.steakgui.variable;

import ninja.amp.ampmenus.events.ItemClickEvent;
import org.bukkit.entity.Player;
import sun.plugin2.message.Message;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.util.MessageHandler;

import java.util.ArrayList;

/**
 * Created by ITSTAKE on 2015-08-11.
 */
public class VariableConverter {
    public static String convert(String message, Menu menu, Player player) {
        int variablestart = 0;
        String variable = "";
        ArrayList<String> variablelist = new ArrayList<>();
        for(String l:message.split("")) {
            if(l.equals("<")) {
                variablestart++;
            }
            if(variablestart > 0) {
                variable = variable + l;
            } else if(variable.length() != 0) {
                variablelist.add(variable);
                variable = "";
            }
            if(l.equals(">") && variablestart > 0) {
                variablestart--;
            }
        }
        if(variable.length() != 0) {
            variablelist.add(variable);
            variable = "";
        }
        variablestart = 0;
        Variables vari = new Variables();
        for(String v:variablelist) {
            String var = v;
            var = var.substring(1, var.length()-1);
            if(var.split(":", 2).length == 2) {
                String splited = var.split(":", 2)[1];
                String pl = "";
                String data = "";
                ArrayList<String> datalist = new ArrayList<>();
                boolean isPlain = false;
                for(String l:splited.split("")) {
                    if(l.equals("'") && !pl.equals("\\")) {
                        isPlain = !isPlain;
                    } else {
                        if (isPlain) {
                            data = data + l;
                        } else if (l.equals(",")) {
                            datalist.add(data);
                            data = "";
                        }
                    }
                    pl = l;
                }
                if(data.length() != 0) {
                    datalist.add(data);
                    data = "";
                }
                if(vari.convert(var.split(":", 2)[0], datalist, menu, player) != null) {
                    message = message.replace(v, vari.convert(var.split(":", 2)[0], datalist, menu, player));
                }
            } else {
                if(vari.convert(var, menu, player) != null) {
                    message = message.replace(v, vari.convert(var, menu, player));
                }
            }

        }
        return message;
    }
}
