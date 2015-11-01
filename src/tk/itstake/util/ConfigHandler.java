/*
 * ConfigHandler.java
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

package tk.itstake.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;

/**
 * Created by ITSTAKE on 2015-08-06.
 */
public class ConfigHandler {
    public static JSONObject config = null;
    public static Plugin plugin = null;
    public static void loadConfig() {
        plugin = Bukkit.getPluginManager().getPlugin("SteakGUI");
        File conffolder = plugin.getDataFolder();
        if(!conffolder.exists()) {
            conffolder.mkdir();
        }
        // Load Current Selected Language
        if (conffolder.exists() && conffolder.isDirectory()) {
            File[] langlist = conffolder.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if (name.equals("config.json")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            // If Detected Language
            if (langlist.length > 0) {
                // Load Language
                try {
                    FileReader fr = new FileReader(langlist[0]);
                    JSONParser jp = new JSONParser();
                    config = (JSONObject) jp.parse(fr);
                    fr.close();
                } catch (Exception e) {
                    config = getDefaultConfig();
                    makeDefaultFile();
                }
            } else {
                // If Can't Detect Language File
                config = getDefaultConfig();
                makeDefaultFile();
            }
        }
    }

    private static void makeDefaultFile() {
        // If Can't Load Config File
        plugin = Bukkit.getPluginManager().getPlugin("SteakGUI");
        File conffile = new File(plugin.getDataFolder().toString() + File.separator + "config.json");
        // Write New File
        try {
            FileWriter fw = new FileWriter(conffile);
            config = getDefaultConfig();
            fw.write(JSONUtil.getPretty(config.toJSONString()));
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONObject getDefaultConfig() {
        JSONObject defaultconf = new JSONObject();
        defaultconf.put("lang", "ko_KR");
        return defaultconf;
    }

    public static Object getConfig(String path) {
        if(config == null) {
            config = getDefaultConfig();
        }
        return config.get(path);
    }

    public static boolean hasConfig(String path) {
        if(config == null) {
            config = getDefaultConfig();
        }
        return config.containsKey(path);
    }

    public static void setConfig(String path, String value) {
        if(config == null) {
            config = getDefaultConfig();
        }
        config.put(path, value);
        plugin = Bukkit.getPluginManager().getPlugin("SteakGUI");
        File conffile = new File(plugin.getDataFolder().toString() + File.separator + "config.json");
        // Write New File
        try {
            FileWriter fw = new FileWriter(conffile);
            fw.write(JSONUtil.getPretty(config.toJSONString()));
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
