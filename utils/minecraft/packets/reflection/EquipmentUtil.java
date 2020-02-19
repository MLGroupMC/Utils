package xyz;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EquipmentUtil {
    /*
        player - player on which item will be displayed
        slot - slot in which item will be displayed (0 - main hand, 1 - off hand, 2 - boots, 3 - leggings, 4 - chestplate, 5 - helmet,
          if version of minecraft doesn't support off hand, slot 1 will be main hand)
        item - item which will be displayed in given slot

        ! Item will be visible only for other players, not for the player on which item is displayed !
    */

    public void setPlayerSlot(Player player, int slot, ItemStack item) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        Class<?> packetClass = getNMSClass("PacketPlayOutEntityEquipment");
        Class<?> bukkitItemstackClass = getCraftItemStack();
        Class<?> itemstackClass = getNMSClass("ItemStack");

        Object packet = null;

        if(useNewPackets()) {
            Class<?> enumClass = getNMSClass("EnumItemSlot");

            Method m = bukkitItemstackClass.getMethod("asNMSCopy", ItemStack.class);
            Method m2 = enumClass.getMethod("valueOf", String.class);
            Object o = m.invoke(bukkitItemstackClass, item);
            Object o2 = null;

            if (slot == 0) o2 = m2.invoke(enumClass, "MAINHAND");
            if (slot == 1) o2 = m2.invoke(enumClass, "OFFHAND");
            if (slot == 2) o2 = m2.invoke(enumClass, "FEET");
            if (slot == 3) o2 = m2.invoke(enumClass, "LEGS");
            if (slot == 4) o2 = m2.invoke(enumClass, "CHEST");
            if (slot == 5) o2 = m2.invoke(enumClass, "HEAD");

            Constructor<?> packetConstructor = packetClass.getConstructor(int.class, enumClass, itemstackClass);
            packet = packetConstructor.newInstance(player.getEntityId(), o2, o);
        } else{
            Method m = bukkitItemstackClass.getMethod("asNMSCopy", ItemStack.class);
            Object o = m.invoke(bukkitItemstackClass, item);

            if (getVersionNumber() <= 8){
                if(slot == 1){
                    slot = 0;
                } else if(slot >= 2 && slot <= 5){
                    slot -= 1;
                } else if(slot > 5){
                    slot = 0;
                }
            }

            Constructor<?> packetConstructor = packetClass.getConstructor(int.class, int.class, itemstackClass);
            packet = packetConstructor.newInstance(player.getEntityId(), slot, o);
        }

        Method sendPacket = getNMSClass("PlayerConnection").getMethod("sendPacket", getNMSClass("Packet"));

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.equals(player)){
                sendPacket.invoke(getConnection(onlinePlayer), packet);
            }
        }
    }

    private Class<?> getNMSClass(String nmsClassString) throws ClassNotFoundException {
        String version = getVersion();
        String name = "net.minecraft.server." + version + nmsClassString;
        Class<?> nmsClass = Class.forName(name);
        return nmsClass;
    }

    private Class<?> getCraftItemStack() throws ClassNotFoundException {
        String version = getVersion();
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

    public boolean useNewPackets(){
        if(getVersionNumber() >= 13){
            return true;
        }
        return false;
    }

    public int getVersionNumber() {
        return Integer.parseInt(getVersion().split("_")[1]);
    }

    public String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

}
