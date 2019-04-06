try {
    Field a = EntityHuman.class.getDeclaredField("enderChest");
    a.setAccessible(true);
    inventoryEnderChest enderChest = (InventoryEnderChest) a.get((EntityHuman) ((CraftPlayer) player).getHandle());
    Field chestA = InventorySubcontainer.class.getDeclaredField("a");
    chestA.setAccessible(true);
    chestA.set(enderChest, ChatUtil.color("&6Ender Chest Name"));
} catch(Exception ex){
    ex.printStackTrace();
}