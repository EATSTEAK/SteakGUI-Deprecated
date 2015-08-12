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
