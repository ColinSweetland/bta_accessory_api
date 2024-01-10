package csweetla.accessoryapi.API;

import java.util.List;

public interface AccessoryApiEntrypoint {
	/**
	 * This method is run by AccessoryAPI at startup. You will want to register your slot types using AccessoryTypeRegistry,
	 * and then edit the slots param below to your liking.
	 * @param slotKeys List of Accessory type keys for accessory slots that will be visible in the inventory.
	 *              Every mod who implements this interface gets to edit before the game runs
	 * TODO: better way to do this?
	 */
	public void onInitialize(List<String> slotKeys);
}
