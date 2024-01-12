package csweetla.accessoryapi.impl.mixins;


import csweetla.accessoryapi.API.TickableWhileWorn;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityPlayer.class, remap = false)
public class EntityPlayerMixin {
	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo ci) {
		EntityPlayer player = (EntityPlayer) ((Object) this);
		for (int i = 0; i < player.inventory.armorInventory.length; i++) {
			ItemStack item = player.inventory.armorInventory[i];
			if (item != null) {
				if (item.getItem() instanceof TickableWhileWorn) {
					ItemStack newItem = ((TickableWhileWorn) item.getItem()).tickWhileWorn(player, item, i);
					if (newItem != item) {
						player.inventory.armorInventory[i] = newItem;
					}
				}
			}
		}
	}

}
