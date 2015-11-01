/*
 * ItemTaskConverter.java
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

import org.bukkit.event.inventory.ClickType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import tk.itstake.steakgui.itemtask.ItemTask;

import java.util.ArrayList;

/**
 * Created by bexco on 2015-07-26.
 */
public class ItemTaskConverter {

    public static JSONObject convert(ItemTask task) {
        JSONObject taskjson = new JSONObject();
        JSONArray datajson = new JSONArray();
        for(Object data:task.getData()) {
            datajson.add(data);
        }
        taskjson.put("type", task.getType());
        ArrayList<ClickType> clickTypes = task.getClickType();
        String clickType = "";
        if(clickTypes == null) {
            clickType = "ALL";
        } else {
            for(ClickType ct:clickTypes) {
                if(clickType.equals("")) {
                    clickType = ct.name();
                } else {
                    clickType = "," + ct.name();
                }
            }
        }
        taskjson.put("clicktype", clickType);
        taskjson.put("data", datajson);
        return taskjson;
    }

    public static ItemTask convert(JSONObject json) {
        String type = (String)json.get("type");
        String[] clicktype = ((String)json.get("clicktype")).split(",");
        JSONArray data = (JSONArray)json.get("data");
        Object[] dataarray = new String[data.size()];
        int i = 0;
        for(Object d:data) {
            dataarray[i] = (String) d;
            i++;
        }
        if(clicktype[0].equals("ALL")) {
            return new ItemTask(type, dataarray);
        } else {
            ArrayList<ClickType> clickTypes = new ArrayList<>();
            for(String ctype:clicktype) {
                clickTypes.add(ClickType.valueOf(ctype));
            }
            return new ItemTask(type, dataarray, clickTypes);
        }
    }
}
