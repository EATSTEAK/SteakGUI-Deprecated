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
