package csweetla.accessoryapi;

import csweetla.accessoryapi.API.AccessoryApiEntrypoint;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class AccessoryApiMain implements ModInitializer {
    public static final String MOD_ID = "accessory_api";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static List<String> accessorySlotKeys;

    @Override
    public void onInitialize() {

		accessorySlotKeys = new ArrayList<>();

		// CREDIT: useless's terrain api
		FabricLoader.getInstance().getEntrypoints(MOD_ID, AccessoryApiEntrypoint.class).forEach(api -> {
			// Make sure the method is implemented
			try {
				api.getClass().getDeclaredMethod("onInitialize");
				api.onInitialize(accessorySlotKeys);
			} catch (NoSuchMethodException ignored) {
			}

		});

		// throw error, because theirs no way to display > 8 accessory slots in a non-broken way, currently
		if (accessorySlotKeys.size() > 8) {
			throw new RuntimeException("Accessory API can't handle more than 8 accessory slots in the current version");
		}

		LOGGER.info("AccessoryApiMain initialized.");
    }
}
