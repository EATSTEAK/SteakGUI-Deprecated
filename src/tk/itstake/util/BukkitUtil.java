package tk.itstake.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
}
