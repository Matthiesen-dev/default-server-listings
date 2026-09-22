package dev.matthiesen.default_server_listings.common.interfaces;

import com.electronwill.nightconfig.core.Config;
import net.minecraft.client.multiplayer.ServerData;

public record ServerListingEntry(String name, String address, ServerData.ServerPackStatus resourcePackStatus, ServerData.Type type) {
    public ServerListingEntry(String name, String address, ServerData.ServerPackStatus resourcePackStatus) {
        this(name, address, resourcePackStatus, ServerData.Type.OTHER);
    }

    public static ServerListingEntry deserialize(Config config) {
        String name = config.get("name");
        String address = config.get("address");
        ServerData.ServerPackStatus resourcePackStatus = config.getEnum("resourcePackStatus", ServerData.ServerPackStatus.class);
        return new ServerListingEntry(name, address, resourcePackStatus);
    }

    public static boolean isValid(Config config) {
        return config.contains("name") && config.contains("address") && config.contains("resourcePackStatus");
    }
}