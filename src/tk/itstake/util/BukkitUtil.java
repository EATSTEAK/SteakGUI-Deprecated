/*
 * BukkitUtil.java
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
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * Created by koohyomin on 15. 7. 30..
 */
public class BukkitUtil {

    public static Player[] allPlayers() {
        Class<?>[] parameterTypes = {};
        Object obj = null;
        try {
            obj = Bukkit.class.getMethod("getOnlinePlayers", parameterTypes).invoke(null ,(Object[])null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(obj instanceof Player[]) {
            Player[] onlinePlayers = (Player[]) obj;
            return onlinePlayers;
        } else if(obj instanceof Collection) {
            Collection<? extends Player> onlinePlayer = (Collection<? extends Player>) obj;
            Player[] onlinePlayers = new Player[onlinePlayer.size()];
            int i = 0;
            for(Player player: onlinePlayer) {
                onlinePlayers[i] = player;
                i++;
            }
            return onlinePlayers;
        } else {
            return null;
        }
    }

    public static Inventory removeItem(Inventory inv, ItemStack stack) {
        int amount = stack.getAmount();
        int index = 0;
        ItemStack[] stacks = inv.getContents();
        for(ItemStack target:inv.getContents()) {
            if(target != null) {
                if (amount > 0) {
                    if (target.isSimilar(stack)) {
                        if (target.getAmount() > amount) {
                            target.setAmount(target.getAmount() - amount);
                            stacks[index] = target;
                            amount = 0;
                        } else if (target.getAmount() == amount) {
                            target = null;
                            stacks[index] = target;
                            amount = 0;
                        } else {
                            amount = amount - target.getAmount();
                            target = null;
                            stacks[index] = target;
                        }
                    }
                } else {
                    break;
                }
            }
            index++;
        }
        inv.setContents(stacks);
        return inv;
    }


    public static boolean hasItem(Inventory inv, ItemStack stack) {
        int amount = stack.getAmount();
        for(ItemStack target:inv.getContents()) {
            if(target != null) {
                if (amount > 0) {
                    if (target.isSimilar(stack)) {
                        if (target.getAmount() > amount) {
                            return true;
                        } else if (target.getAmount() == amount) {
                            return true;
                        } else {
                            amount = amount - target.getAmount();
                        }
                    }
                } else {
                    return false;
                }
            }
        }
        return false;
    }
}
