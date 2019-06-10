int red = 255;
int green = 0;
int blue = 0;


public static void color(){
    Bukkit.getScheduler().scheduleAsyncRepeatingTask(MCKTools.getInst(), new Runnable() {
        public void run() {
            if(red > 0 && blue == 0) {
                red-=3;
                green+=3;
            }
            if(green > 0 && red == 0){
                green-=3;
                blue+=3;
            }
            if(blue > 0 && green == 0){
                red+=3;
                blue-=3;
            }
        }
    }, 0, 1);
}
	

Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {
    @Override
    public void run() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (!pl.getName().equalsIgnoreCase(player.getName())) {
                CraftPlayer plp = (CraftPlayer) pl;
                ItemStack bootsItem = new ItemStack(Item.getById(301));
                int color = Color.fromRGB(this.red, this.green, this.blue).asRGB();
                NBTTagCompound tag = new NBTTagCompound();
                tag.set("display", new NBTTagCompound());
                NBTTagCompound displayTag = tag.getCompound("display");
                displayTag.setInt("color", color);
                bootsItem.setTag(tag);
                PacketPlayOutEntityEquipment bootsPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 0, bootsItem);
                plp.getHandle().playerConnection.sendPacket(bootsPacket);
            }
        }
    }
}, 0, 2);
