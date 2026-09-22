package dev.matthiesen.default_server_listings.common;

import dev.matthiesen.default_server_listings.common.interfaces.Events;
import dev.matthiesen.default_server_listings.common.interfaces.ServerListingEntry;
import dev.matthiesen.libs.faststats.Token;
import dev.matthiesen.matthiesen_core.common.AbstractCommonMod;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.ModConfigType;
import net.minecraft.client.multiplayer.ServerList;
import org.jetbrains.annotations.NotNull;

public final class DefaultServerListingsCommon extends AbstractCommonMod {
    public static final String MOD_ID = "default_server_listings";
    public static final String MOD_NAME = "Default Server Listings";
    public static @Token final String METRICS_TOKEN = "73c31632a940a1c70e95a0df03ccfbae";
    public static final DefaultServerListingsCommon INSTANCE = new DefaultServerListingsCommon();

    public DefaultServerListingsCommon() {
        super(MOD_ID, MOD_NAME);
    }

    @Override
    public @Token @NotNull String getMetricsToken() {
        return METRICS_TOKEN;
    }

    public void initialize() {
        super.initialize();
        registerModConfig(MOD_ID, ModConfigType.STARTUP, DefaultServerListingsConfig.CONFIG_SPEC, "default_server_listings.toml");

        Events.CLIENT_STARTED.subscribe(event -> {
            if (!DefaultServerListingsConfig.isEnabled()) {
                createInfoLog("Default Server Listings mod is disabled in the config.");
                return;
            }
            ServerList serverList = new ServerList(event.client());
            serverList.load();
            for (ServerListingEntry entry : DefaultServerListingsConfig.getServerListings()) {
                entry.appendToServerList(serverList);
            }
            serverList.save();
        });

        createInfoLog("Initialized");
    }
}
