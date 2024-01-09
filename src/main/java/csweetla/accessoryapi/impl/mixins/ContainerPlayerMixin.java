package csweetla.accessoryapi.impl.mixins;

import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.ContainerPlayer;
import net.minecraft.core.player.inventory.InventoryCrafting;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotArmor;
import net.minecraft.core.player.inventory.slot.SlotCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import csweetla.accessoryapi.AccessoryApiMain;
import csweetla.accessoryapi.impl.AccessorySlot;

@Mixin(value = ContainerPlayer.class, remap = false)
public abstract class ContainerPlayerMixin extends Container {

	@Redirect(method="<init>(Lnet/minecraft/core/player/inventory/InventoryPlayer;Z)V", at=@At(value = "INVOKE", target = "Lnet/minecraft/core/player/inventory/ContainerPlayer;addSlot(Lnet/minecraft/core/player/inventory/slot/Slot;)V"))
	private void changeTopSlotPositions(ContainerPlayer instance, Slot slot) {

		// the player 'doll' display area is the width of 3 slots = 3 * 18 = 54 -> shift 54 to display on the right
		if (slot instanceof SlotArmor)
			slot.xDisplayPosition += 54;

		// slotCrafting is actually only the result
		if (slot instanceof SlotCrafting) {
			//I fr guessed these
			slot.xDisplayPosition -= 10;
			slot.yDisplayPosition += 26;
		}

		// crafting matrix
		if (slot.getInventory() instanceof InventoryCrafting) {
			slot.yDisplayPosition -= 18;
			slot.xDisplayPosition += 37;
		}

		instance.addSlot(slot);
	}

	// add all custom accessory slots
	@Inject(method = "<init>(Lnet/minecraft/core/player/inventory/InventoryPlayer;Z)V", at = @At(value = "TAIL"))
	private void addSlots(InventoryPlayer inv, boolean par2, CallbackInfo ci) {
		// 36 default + 4 default armor
		int slotnum = 40;
		assert(this.getSlot(36) instanceof SlotArmor);

		// slot 5 is the helmet slot, we will place the accessories relative to it.
		int startX = this.getSlot(5).xDisplayPosition;
		int startY = this.getSlot(5).yDisplayPosition;
		int slot_w = 18;

		for (int i = 0; i < AccessoryApiMain.accessorySlotKeys.size(); i++) {
			int row_num = (i % 4);
			// col 0 is the already placed armor slots
			int col_num = (i / 4) + 1;
			addSlot(new AccessorySlot(inv, slotnum++,startX + slot_w * col_num, startY + slot_w * row_num, AccessoryApiMain.accessorySlotKeys.get(i)));
		}

	}
}
