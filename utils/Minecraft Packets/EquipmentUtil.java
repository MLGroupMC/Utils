package xyz;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EquipmentUtil {

    private String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
    private int versionNumber = Integer.parseInt(version.split("_")[1]);
    
    /*
        player - player on which item will be displayed
        slot - slot in wich item will be displayed (0 - main hand, 1 - off hand, 2 - boots, 3 - leggings, 4 - chestplate, 5 - helmet, 
          if version of minecraft will be lower or equal 1.8 and you put item in slot 1 item will be displayed in main hand)
        item - item which will be displayed in slot
        
        ! Item will be visible only for other players not for the player on which item will be displayed
    */
    
    public void setPlayerSlot(Player player, int slot, ItemStack item) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        Class<?> packetClass = getNMSClass("PacketPlayOutEntityEquipment");
        Class<?> bukkitItemstackClass = getCraftItemStack();
        Class<?> itemstackClass = getNMSClass("ItemStack");

        Method m = bukkitItemstackClass.getMethod("asNMSCopy", ItemStack.class);
        Object o = m.invoke(bukkitItemstackClass, item);

        if(versionNumber <= 8){
            if(slot == 1){
                slot = 0;
            } else if(slot >= 2 && slot <= 5){
                slot -= 1;
            } else if(slot > 5){
                slot = 0;
            }
        }

        Constructor<?> packetConstructor = packetClass.getConstructor(int.class, int.class, itemstackClass);
        Object packet = packetConstructor.newInstance(player.getEntityId(), slot, o);

        Method sendPacket = getNMSClass("PlayerConnection").getMethod("sendPacket", getNMSClass("Packet"));

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.equals(player)){
                sendPacket.invoke(getConnection(onlinePlayer), packet);
            }
        }
    }

    private Class<?> getNMSClass(String nmsClassString) throws ClassNotFoundException {
        String name = "net.minecraft.server." + version + nmsClassString;
        Class<?> nmsClass = Class.forName(name);
        return nmsClass;
    }

    private Class<?> getCraftItemStack() throws ClassNotFoundException {
        String name = "org.bukkit.craftbukkit." + version + "inventory.CraftItemStack";
        Class<?> nmsClass = Class.forName(name);
        return nmsClass;
    }


    private Object getConnection(Player player) throws SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method getHandle = player.getClass().getMethod("getHandle");
        Object nmsPlayer = getHandle.invoke(player);
        Field conField = nmsPlayer.getClass().getField("playerConnection");
        Object con = conField.get(nmsPlayer);
        return con;
    }

}
