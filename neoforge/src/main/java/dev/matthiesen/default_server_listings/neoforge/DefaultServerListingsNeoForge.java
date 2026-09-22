package dev.matthiesen.default_server_listings.neoforge;

import dev.matthiesen.default_server_listings.common.DefaultServerListingsCommon;
import dev.matthiesen.default_server_listings.common.interfaces.ClientStarted;
import dev.matthiesen.default_server_listings.common.interfaces.Events;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod(value = DefaultServerListingsCommon.MOD_ID, dist = Dist.CLIENT)
public final class DefaultServerListingsNeoForge {
    public static final DefaultServerListingsCommon INSTANCE = DefaultServerListingsCommon.INSTANCE;

    public DefaultServerListingsNeoForge(IEventBus modBus) {
        INSTANCE.createInfoLog("Loading for NeoForge Mod Loader");
        INSTANCE.initialize();
        modBus.addListener(this::onClientStarted);
    }

    public void onClientStarted(FMLLoadCompleteEvent event) {
        event.enqueueWork(() ->
                Events.CLIENT_STARTED.emit(new ClientStarted(Minecraft.getInstance()))
        );
    }
}
