package xyz;

import com.earth2me.essentials.*;
import com.earth2me.essentials.commands.WarpNotFoundException;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EssentialsUtils{
	
	#
	#How to get essentials object \/
	#
	#essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
	#
	
    public static Warps getWarps(Essentials essentials) {
        return essentials.getWarps();
    }

	#name - warp name
    public static Location getWarp(String name, Essentials essentials){
        try {
            return essentials.getWarps().getWarp(name);
        } catch (WarpNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidWorldException e) {
            e.printStackTrace();
        }
        return null;
    }

	#kitName - kit name
	#String currencySymbol - symbol of money
	#onlyItems - whether should only items be returned
	#
	#get items of kit - cast list of Objects to list of ItemStacks
	#
    @SuppressWarnings("rawtypes")
    public static List<Object> getKitContents(String kitName, Essentials essenitals, String currencySymbol, boolean onlyItems) {
        final List<String> itemList = new ArrayList<String>();
        final Map<String, Object> kit = essentials.getKits().getKit(kitName);
        final Object kitItems = kit.get("items");
        if (kitItems instanceof List) {
            for (Object item : (List) kitItems) {
                if (item instanceof String) {
                    itemList.add(item.toString());
                    continue;
                }
            }
        }
        if(itemList.get(0) == null) {
            return null;
        }
        List<Object> list = new ArrayList<>();
        for (String kitItem : itemList) {
            if (kitItem.startsWith(currencySymbol)) {
                if(!onlyItems) {
                    list.add(kitItem);
                }
                continue;
            }
            if (kitItem.startsWith("/")) {
                if(!onlyItems) {
                    list.add(kitItem);
                }
                continue;
            }
            final String[] parts = kitItem.split(" +");
            try {
                final ItemStack parseStack = essentials.getItemDb().get(parts[0], parts.length > 1 ? Integer.parseInt(parts[1]) : 1);
                if (parseStack.getType() == Material.AIR) {
                    list.add(new ItemStack(Material.AIR));
                    continue;
                }
                final MetaItemStack metaStack = new MetaItemStack(parseStack);
                if (parts.length > 2) {
                    metaStack.parseStringMeta(null, true, parts, 2, essentials);
                }
                list.add(metaStack.getItemStack());
            } catch(Exception ex) {}
        }
        return list;
    }

    public static User getUser(Player player, Essentials essentials){
        return new User(player, essentials);
    }
}