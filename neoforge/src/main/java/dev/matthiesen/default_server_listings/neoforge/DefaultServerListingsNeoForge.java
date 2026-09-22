package dev.matthiesen.default_server_listings.neoforge;

import dev.matthiesen.default_server_listings.common.DefaultServerListingsCommon;
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
                DefaultServerListingsCommon.Events.CLIENT_STARTED.emit(new DefaultServerListingsCommon.ClientStarted(Minecraft.getInstance()))
        );
    }
}
