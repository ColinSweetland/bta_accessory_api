# THIS MOD IS UNFINISHED AND WON'T BE UPDATED

It has been integrated into the as of now WIP bta aether port. I'm only leaving it up in case anyone wants to reference this code.

# BTA Accessory Api [WIP BETA]

A library which other 'Better than Adventure!' mods can use to add accessories and accessory slots, in a hopefully compatible way.

# Based on 

This mod is mainly a port/rewrite of 

https://github.com/matthewperiut/accessory-api

Also influenced by:

https://github.com/UselessBullets/TerrainAPI

# Usage

Implement AccessoryApiEntryPoint in a class and register slot types and edit slotKeys to make slots show up in game
```java
// make sure to add this entrypoint in fabric.mod.json aswell
public class AccessoryEntrypoint implements AccessoryApiEntrypoint {
	void onInitialize(List<String> slotKeys) {
        // register accessory type with key 'ring', with icon index as second argument (using HalpLibe's texturehelper)
        AccessoryTypeRegistry.add("ring", TextureHelper.getOrCreateItemTextureIndex("mod_id", "ring.png"));
        
        // add two ring slots
        slotKeys.add("ring");
        slotKeys.add("ring");
    }
}
// now, when the game starts, there should be two ring slots that can insert any ring Accessory
```

Accessory Items are any item that implements the 'Accessory' interface
```java
public class PointlessRing extends Item implements Accessory {
    
    /* insert other item related code */
    
    String[] getAccessoryTypes(ItemStack item) {
        return new String[]{"ring"};
    }
    
    default void onAccessoryAdded(EntityPlayer player, ItemStack accessory) {
        player.addChatMessage("Put on a ring");
    }
    
    default void onAccessoryRemoved(EntityPlayer player, ItemStack accessory) {
        player.addChatMessage("Removed a ring");
    }
}
```

There is also a 'TickableWhileWorn' interface, for Accessories which receive ticks while worn.

## Issues

1. The UI is hardcoded to be similar to a famous b1.7.3 sky mod
2. Only up to 8 slots are supported
3. Api isn't actually that nice for modders!
4. Don't support custom renderers like the original library
5. Could probably support more 'hooks' for accessories
