package ninja.amp.ampmenus;

import ninja.amp.ampmenus.menus.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Created by ITSTAKE on 2015-09-25.
 */
public class DragListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryDrag (org.bukkit.event.inventory.InventoryDragEvent event){
        if (event.getWhoClicked() instanceof Player && event.getInventory().getHolder() instanceof MenuHolder) {
            event.setCancelled(true);
        }
    }

}
