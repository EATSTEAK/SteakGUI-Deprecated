package tk.itstake.steakgui.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.gui.GUIItem;
import tk.itstake.steakgui.itemtask.ItemTask;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.variable.VariableConverter;
import tk.itstake.util.JSONUtil;
import tk.itstake.util.LanguageHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bexco on 2015-07-26.
 */
public class MenuFileHandler {

    private static HashMap<String, Menu> loadedmenu = new HashMap<>();

    public static void saveMenu(Menu menu) {
        JSONObject menuConfig = new JSONObject();
        menuConfig.put("dummyhelp", new LanguageHandler().getLanguage("menufile.wannahelp"));
        menuConfig.put("title", menu.getTitle());
        menuConfig.put("size", menu.getSize());
        JSONObject slot = new JSONObject();
        for(Integer key:menu.getItemArray().keySet()) {
            GUIItem item = menu.getItemArray().get(key);
            JSONObject keyarray = new JSONObject();
            keyarray.put("perm", item.getPermission());
            JSONArray taskarray = new JSONArray();
            for(ItemTask task: item.getTasks()) {
                JSONObject itemTaskHash = ItemTaskConverter.convert(task);
                taskarray.add(itemTaskHash);
            }
            keyarray.put("task", taskarray);
            JSONObject itemStackHash = ItemStackConverter.convert(item.getItemStack());
            keyarray.put("item", itemStackHash);
            slot.put(key, keyarray);
        }
        menuConfig.put("slot", slot);
        Plugin plugin = Bukkit.getPluginManager().getPlugin("SteakGUI");
        File dataFolder = plugin.getDataFolder();
        if(dataFolder.exists() && dataFolder.isDirectory()) {
            dataFolder.mkdir();
        }
        File menuFolder = new File(dataFolder.toString() + File.separator + "menu");
        if(!menuFolder.exists() || !menuFolder.isDirectory()) {
            menuFolder.mkdir();
        }
        try {
            FileWriter fw = new FileWriter(new File(menuFolder.toString() + File.separator + menu.getName() + ".json"));
            fw.write(JSONUtil.getPretty(menuConfig.toJSONString()));
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadMenu(menu.getName(), true);
    }

    public static ArrayList<String> listMenu() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("SteakGUI");
        File dataFolder = plugin.getDataFolder();
        if(dataFolder.exists() && dataFolder.isDirectory()) {
            dataFolder.mkdir();
        }
        File menuFolder = new File(dataFolder.toString() + File.separator + "menu");
        if(!menuFolder.exists() || !menuFolder.isDirectory()) {
            menuFolder.mkdir();
        }
        FilenameFilter jsonfilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.endsWith(".json")) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        ArrayList<String> listmenu = new ArrayList<>();
        for(File file:menuFolder.listFiles(jsonfilter)) {
            listmenu.add(file.getName().replace(".json", ""));
        }
        return listmenu;
    }

    public static void deleteMenu(String name) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("SteakGUI");
        File dataFolder = plugin.getDataFolder();
        if(dataFolder.exists() && dataFolder.isDirectory()) {
            dataFolder.mkdir();
        }
        File menuFolder = new File(dataFolder.toString() + File.separator + "menu");
        if(!menuFolder.exists() || !menuFolder.isDirectory()) {
            menuFolder.mkdir();
        }
        File menuFile = new File(menuFolder.toString() + File.separator + name + ".json");
        menuFile.delete();
    }

    public static Menu loadMenu(String name, boolean forcereload) {
        if(!loadedmenu.containsKey(name) || forcereload) {
            File dataFolder = SteakGUI.p.getDataFolder();
            if (dataFolder.exists() && dataFolder.isDirectory()) {
                dataFolder.mkdir();
            }
            File menuFolder = new File(dataFolder.toString() + File.separator + "menu");
            if (!menuFolder.exists() || !menuFolder.isDirectory()) {
                menuFolder.mkdir();
            }
            File menuFile = new File(menuFolder.toString() + File.separator + name + ".json");
            try {
                FileReader fr = new FileReader(menuFile);
                JSONParser jp = new JSONParser();
                JSONObject menujson = (JSONObject) jp.parse(fr);
                JSONObject slotarray = (JSONObject) menujson.get("slot");
                HashMap<Integer, GUIItem> slotmap = new HashMap<>();
                for (Object slot : slotarray.keySet()) {
                    JSONObject guiarray = (JSONObject) slotarray.get(slot);
                    ItemStack item = ItemStackConverter.convert((JSONObject) guiarray.get("item"));
                    ArrayList<ItemTask> taskarray = new ArrayList<>();
                    for (Object obj : ((JSONArray) guiarray.get("task"))) {
                        ItemTask task = ItemTaskConverter.convert((JSONObject) obj);
                        taskarray.add(task);
                    }

                    GUIItem guiitem = new GUIItem(item, (String) guiarray.get("perm"), taskarray);
                    slotmap.put(Integer.parseInt((String) slot), guiitem);
                }
                Menu menu = new Menu(name, (String) menujson.get("title"), (int) (long) menujson.get("size"), slotmap);
                fr.close();
                loadedmenu.put(name, menu);
                return menu;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return loadedmenu.get(name);
        }
    }

    public static Menu loadMenu(String name) {
        return loadMenu(name, false);
    }

    public static void reloadMenu() {
        loadedmenu = new HashMap<>();
    }

}
