/*
 * StaticMenuItem.java
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
package ninja.amp.ampmenus.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * A {@link ninja.amp.ampmenus.items.MenuItem} whose icon never changes.
 */
public class StaticMenuItem extends MenuItem {

    public StaticMenuItem(String displayName, ItemStack icon, String... lore) {
        super(displayName, icon, lore);
        setNameAndLore(getIcon(), getDisplayName(), getLore());
    }

    @Override
    public ItemStack getFinalIcon(Player player) {
        return getIcon();
    }
}
