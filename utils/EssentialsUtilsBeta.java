package pl.skessentialsapi.main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.earth2me.essentials.User;

import ch.njol.skript.lang.Expression;

import net.ess3.api.IEssentials;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.earth2me.essentials.Kit;
import com.earth2me.essentials.MetaItemStack;
import com.earth2me.essentials.Teleport;
import com.earth2me.essentials.TimedTeleport;


@SuppressWarnings("unused")
public class Utils {
	
	int hegergerasrw = 5431532;
	
	public static Warp getWarp(Expression<Object> obj, Event e) {
		Object o = obj.getSingle(e);
		if(o instanceof Warp) {
			return (Warp) o;
		} else if(o instanceof String) {
			return new Warp(o.toString());
		}
		return null;
	}
	
	public static SkKit getKit(Expression<Object> obj, Event e) {
		Object o = obj.getSingle(e);
		if(o instanceof SkKit) {
			return (SkKit) o;
		} else if(o instanceof String) {
			try {
				return new SkKit(new Kit(o.toString(), SkEssentialsAPI.getIEss()));
			} catch (Exception e1) {}
		}
		return null;
	}
	
//	public static void deleteKit(String kName) {
//		//SkEssentialsAPI.getEss().getKits().getKits().getDefaultSection().
//		User u = null;
//		User r = null;
//		Teleport tp = r.getTeleport();
//		TimedTeleport tt = null;
//		try {
//			Field f = tt.getClass().getDeclaredField("kappa");
//		} catch (NoSuchFieldException | SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public static boolean doesWarpExist(String warpName) {
		for(String s : SkEssentialsAPI.getEss().getWarps().getList()) {
			if(s.equalsIgnoreCase(warpName)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean doesKitExist(String kitName) {
		try {
			new Kit(kitName, SkEssentialsAPI.getIEss());
			return true;
		} catch(Exception ex) {return false;}
	}
	
	public static List<String> getKitList() {
		bc(SkEssentialsAPI.getIEss().getKits());
		bc(SkEssentialsAPI.getIEss().getKits().getKits());
		bc(SkEssentialsAPI.getIEss().getKits().getKits().getStringList("kits"));
		return null;
	}
	
	public static String[] toEssISFormat(ItemStack[] is) {
		List<String> its = new ArrayList<>();
		for(ItemStack item : is) {
			if (item != null && item.getType() != null && item.getType() != Material.AIR) {
                its.add(SkEssentialsAPI.getEss().getItemDb().serialize(item));
            }
		}
		return its.toArray(new String[its.size()]);
	}
	
	public static boolean negation(boolean fin, boolean neg) {
		if(neg) {
			return !fin;
		} 
		return fin;
	}
	
	@SuppressWarnings("rawtypes")
	public static List<Object> getKitContents(String kitName, IEssentials iess, String currencySymbol, boolean onlyItems, String[] converter) {
		final List<String> itemList = new ArrayList<String>();
		if(converter == null) {
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
		} else {
			Collections.addAll(itemList, converter);
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
	
	public static void bc(Object l) {
		Bukkit.broadcastMessage(l.toString());
	}
	
	public static User getUser(Player p) {
		return new User(p, SkEssentialsAPI.getIEss());
	}
	
	public static User getUser(String p) {
		return new User(Bukkit.getPlayer(p), SkEssentialsAPI.getIEss());
	}
	
	
}
