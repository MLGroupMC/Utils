package xyz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.earth2me.essentials.MetaItemStack;

import net.ess3.api.IEssentials;

public class EssentialsUtils{
	
    //kitName - kit name
    //iess - objekt IEssentials (cast Essentials na IEssentials)
    //currencySymbol - symbol waluty z Essentiala (możliwy do pobrania z api)
    //onlyItems - czy w liście mają znajdować się tylko itemy z kitu czy też itemy razem z pieniędzmi itp.
    @SuppressWarnings("rawtypes")
    public static List<Object> getKitContents(String kitName, IEssentials iess, String currencySymbol, boolean onlyItems) {
        final List<String> itemList = new ArrayList<String>();
        final Map<String, Object> kit = iess.getKits().getKit(kitName);
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
                final ItemStack parseStack = iess.getItemDb().get(parts[0], parts.length > 1 ? Integer.parseInt(parts[1]) : 1);
                if (parseStack.getType() == Material.AIR) {
                    list.add(new ItemStack(Material.AIR));
                    continue;
                }
                final MetaItemStack metaStack = new MetaItemStack(parseStack);
                if (parts.length > 2) {
                    metaStack.parseStringMeta(null, true, parts, 2, iess);
                }
                list.add(metaStack.getItemStack());
            } catch(Exception ex) {}
        }
        return list;
    }
}