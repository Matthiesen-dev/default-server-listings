package dev.matthiesen.default_server_listings.common;

import dev.matthiesen.default_server_listings.common.interfaces.Events;
import dev.matthiesen.libs.faststats.Token;
import dev.matthiesen.matthiesen_core.common.AbstractCommonMod;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.ModConfigType;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import org.jetbrains.annotations.NotNull;

public final class DefaultServerListingsCommon extends AbstractCommonMod {
    public static final String MOD_ID = "default_server_listings";
    public static final String MOD_NAME = "Default Server Listings";
    public static @Token final String METRICS_TOKEN = "73c31632a940a1c70e95a0df03ccfbae";
    public static final DefaultServerListingsCommon INSTANCE = new DefaultServerListingsCommon();

    public static String modConfig(String path) {
        return MOD_ID + "/" + path + ".toml";
    }

    public DefaultServerListingsCommon() {
        super(MOD_ID, MOD_NAME);
    }

    @Override
    public @Token @NotNull String getMetricsToken() {
        return METRICS_TOKEN;
    }

    public void initialize() {
        super.initialize();
        registerModConfig(MOD_ID, ModConfigType.STARTUP, DefaultServerListingsConfig.CONFIG_SPEC, modConfig("config"));

        Events.CLIENT_STARTED.subscribe(event -> {
            if (!DefaultServerListingsConfig.isEnabled()) {
                createInfoLog("Default Server Listings mod is disabled in the config.");
                return;
            }
            ServerList serverList = new ServerList(event.client());
            serverList.load();
            for (var entry : DefaultServerListingsConfig.getServerListings()) {
                String ip = entry.address();
                if (serverList.get(ip) == null) {

                    ServerData serverData = new ServerData(entry.name(), ip, entry.type());
                    serverData.setResourcePackStatus(entry.resourcePackStatus());
                    serverList.add(serverData, false);

                    createInfoLog("Added default server listing: " + entry.name() + " (" + ip + ")");
                } else {
                    createInfoLog("Server listing already exists: " + entry.name() + " (" + ip + ")");
                }
            }
        });

        createInfoLog("Initialized");
    }
}
