package xyz;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ColorParticlesUtil {
    public void sendParticle(Player player, Location location, int red, int green, int blue) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchFieldException {
        if(player.getLocation().getWorld() == location.getWorld()) {
            Class<?> packetClass = getNMSClass("PacketPlayOutWorldParticles");
            Class<?> enumClass = getNMSClass("EnumParticle");
            Method m = enumClass.getMethod("a", int.class);
            Object o = m.invoke(enumClass, 30);
            Constructor<?> packetConstructor = packetClass.getConstructor(enumClass, boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class, int[].class);
            Object packet = packetConstructor.newInstance(o, false, (float) location.getX(), (float) location.getY(), (float) location.getZ(), convertColor(red), convertColor(green), convertColor(blue), 1F, 0, null);
            Method sendPacket = getNMSClass("PlayerConnection").getMethod("sendPacket", getNMSClass("Packet"));
            sendPacket.invoke(getConnection(player), packet);
        }
    }

    public void spawnParticle(Location location, int red, int green, int blue) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            try {
                sendParticle(player, location, red, green, blue);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        });
    }

    private Class<?> getNMSClass(String nmsClassString) throws ClassNotFoundException {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        String name = "net.minecraft.server." + version + "." + nmsClassString;
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

    private float convertColor(int color){
        return (color*0.007843F)-1F;
    }

}
