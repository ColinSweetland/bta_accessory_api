package csweetla.accessoryapi.impl.mixins;

import csweetla.accessoryapi.AccessoryApiMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiInventory;
import net.minecraft.core.player.inventory.Container;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiInventory.class, remap = false)
public abstract class GuiInventoryMixin extends GuiContainer {

	@Shadow
	private GuiButton armorButton;

	@Shadow
	protected float xSize_lo;

	@Unique
	private static final int INV_U = 0, INV_V = 0, INV_W = 154, INV_H = 72;

	@Unique
	private static final int  CRAFT_X = 117, CRAFT_U = 172, CRAFT_V = 0, CRAFT_W = 36, CRAFT_H = 72;

	@Unique
	private static final int SLOT_U = 154, SLOT_V = 54, SLOT_W = 18, SLOT_H = 18;

	@Unique
	private static final int CORNER_INSET = 7;

	public GuiInventoryMixin(Container container) {
		super(container);
	}

	@Inject(method = "init", at = @At("TAIL"))
	public void init(CallbackInfo ci) {
		if (this.armorButton != null) {
			this.armorButton.xPosition = (width - xSize) / 2 + CORNER_INSET + 2;
			this.armorButton.yPosition = (height - ySize) / 2 + CORNER_INSET + 2;
		}
	}

	@Inject(method = "drawGuiContainerBackgroundLayer", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V", ordinal = 0))
	public void bindGuiTexture(float f, CallbackInfo ci) {
		Minecraft minecraft = Minecraft.getMinecraft(this);

		int startX = (width - xSize) / 2;
		int startY = (height - ySize) / 2;
		int texture_id;

		// force compatibility with the aether mod, which must load its assets from a jar for legal reasons *sigh*
		if (AccessoryApiMain.aether_loaded) {
			texture_id = minecraft.renderEngine.getTexture("assets/aether/gui/inventory.png");
			minecraft.renderEngine.bindTexture(texture_id);
			drawTexturedModalRect(startX, startY, INV_U, INV_V, 175, 165);
			return;
		}

		texture_id = minecraft.renderEngine.getTexture("assets/accessory_api/inventory.png");
		minecraft.renderEngine.bindTexture(texture_id);

		drawTexturedModalRect(startX + CORNER_INSET,startY + CORNER_INSET,INV_U, INV_V, INV_W, INV_H);

		drawTexturedModalRect(startX + CRAFT_X + CORNER_INSET, startY + CORNER_INSET, CRAFT_U, CRAFT_V, CRAFT_W, CRAFT_H);

		// i just guessed this lol
		int FIRST_ACCESSORY_SLOT_OFFSET = INV_W / 2 - 16;
		int SLOT_WIDTH = 18;
		int NUM_SLOTS = 4 + AccessoryApiMain.accessorySlotKeys.size();
		int SLOTS_PER_COLUMN = 4;

		for (int i = 0; i < NUM_SLOTS; i++) {
			int row = i % SLOTS_PER_COLUMN;
			int column = i / SLOTS_PER_COLUMN;
			drawTexturedModalRect(startX + FIRST_ACCESSORY_SLOT_OFFSET + SLOT_WIDTH * column,
				startY + CORNER_INSET + SLOT_WIDTH * row,
				SLOT_U, SLOT_V, SLOT_W, SLOT_H);
		}
	}

	// move the player 'doll' over to the left some
	@Redirect(method = "drawGuiContainerBackgroundLayer", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTranslatef(FFF)V",ordinal = 0))
	public void translatePlayerModel(float x, float y, float z) {
		GL11.glTranslatef(x - 18, y, z);
	}

	// make the player model face correctly given the new shift
	@Redirect(method = "drawGuiContainerBackgroundLayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/player/EntityPlayerSP;yRot:F", opcode = Opcodes.PUTFIELD, ordinal = 0))
	private void fixPlayerModelYaw(EntityPlayerSP instance, float yaw) {
		int startX = (width - xSize) / 2;
		instance.yRot = (float) Math.atan((startX + 33 - this.xSize_lo) / 40.0F) * 40.0F;
	}

	// don't draw the inventory text
	@Inject(method = "drawGuiContainerForegroundLayer", at = @At("HEAD"), cancellable = true)
	public void renderForeground(CallbackInfo ci) {
		ci.cancel();
	}
}
