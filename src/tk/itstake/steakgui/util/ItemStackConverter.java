package tk.itstake.steakgui.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by bexco on 2015-07-26.
 */
public class ItemStackConverter {

    public static JSONObject convert(ItemStack item) {
        JSONObject map = new JSONObject();
        map.put("amount", item.getAmount());
        map.put("type", item.getType().toString());
        map.put("data", item.getDurability());
        JSONObject meta = new JSONObject();
        if(item.getItemMeta().getDisplayName() != null) {
            meta.put("name", item.getItemMeta().getDisplayName());
        }
        if(item.getItemMeta().getLore() != null && item.getItemMeta().getLore().size() != 0) {
            meta.put("lore", item.getItemMeta().getLore());
        }
        if(item.getEnchantments() != null && item.getEnchantments().size() != 0) {
            meta.put("enchantments", item.getEnchantments());
        }
        try {
            Class.forName("org.bukkit.inventory.ItemFlag");
            if(item.getItemMeta().getItemFlags() != null && item.getItemMeta().getItemFlags().size() != 0) {
                meta.put("flags", item.getItemMeta().getItemFlags());
            }
        } catch (ClassNotFoundException e) {
        }
        map.put("meta", meta);
        return map;
    }

    public static ItemStack convert(JSONObject json) {
        ItemStack item = new ItemStack(Material.valueOf(((String) json.get("type")).toUpperCase()), (int)(long)json.get("amount"), (short)(long)json.get("data"));
        if(json.containsKey("meta")) {
            JSONObject metajson = (JSONObject)json.get("meta");
            ItemMeta meta = item.getItemMeta();
            if(metajson.containsKey("name")) {
                meta.setDisplayName((String)metajson.get("name"));
            }
            if(metajson.containsKey("lore")) {
                meta.setLore((List<String>) metajson.get("lore"));
            }
            if(metajson.containsKey("enchantments")) {
                JSONObject enchantment = (JSONObject)metajson.get("enchantments");
                for(Object enchant:enchantment.keySet()) {
                    Integer level = (int)(long)enchantment.get(enchant);
                    meta.addEnchant(Enchantment.getByName((String)enchant), level, true);
                }
            }
            if(metajson.containsKey("flags")) {
                try {
                    Class.forName("org.bukkit.inventory.ItemFlag");
                    JSONArray flagobj = (JSONArray)metajson.get("flags");
                    org.bukkit.inventory.ItemFlag[] flaglist = new org.bukkit.inventory.ItemFlag[flagobj.size()];
                    int ai = 0;
                    for(Object flag:flagobj.toArray()) {
                        flaglist[ai] = org.bukkit.inventory.ItemFlag.valueOf((String)flag);
                        ai++;
                    }
                    meta.addItemFlags(flaglist);
                } catch (ClassNotFoundException e) {
                }
            }
            item.setItemMeta(meta);
        }
        return item;
    }

}
