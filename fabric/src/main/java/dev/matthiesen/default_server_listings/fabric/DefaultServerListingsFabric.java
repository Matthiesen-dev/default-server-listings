package dev.matthiesen.default_server_listings.fabric;

import dev.matthiesen.default_server_listings.common.DefaultServerListingsCommon;
import dev.matthiesen.default_server_listings.common.interfaces.ClientStarted;
import dev.matthiesen.default_server_listings.common.interfaces.Events;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public final class DefaultServerListingsFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        var commonInstance = DefaultServerListingsCommon.INSTANCE;
        commonInstance.createInfoLog("Loading for Fabric Mod Loader");
        commonInstance.initialize();

        ClientLifecycleEvents.CLIENT_STARTED.register(client ->
                Events.CLIENT_STARTED.emit(new ClientStarted(client))
        );
    }
}
