package dev.matthiesen.default_server_listings.common.interfaces;

import com.electronwill.nightconfig.core.Config;
import dev.matthiesen.default_server_listings.common.DefaultServerListingsCommon;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;

import java.util.List;
import java.util.stream.Stream;

public record ServerListingEntry(String name, String address, ServerData.ServerPackStatus resourcePackStatus, ServerData.Type type) {
    public ServerListingEntry(String name, String address, ServerData.ServerPackStatus resourcePackStatus) {
        this(name, address, resourcePackStatus, ServerData.Type.OTHER);
    }

    public static final List<Config> DEFAULT_ENTRIES = Stream.of(
            new ServerListingEntry("Local Host", "127.0.0.1", ServerData.ServerPackStatus.PROMPT)
    ).map(ServerListingEntry::serialize).toList();

    public static ServerListingEntry deserialize(Config config) {
        String name = config.get("name");
        String address = config.get("address");
        ServerData.ServerPackStatus resourcePackStatus = config.getEnum("resourcePackStatus", ServerData.ServerPackStatus.class);
        return new ServerListingEntry(name, address, resourcePackStatus);
    }

    public static boolean isValid(Config config) {
        return config.contains("name") && config.contains("address") && config.contains("resourcePackStatus");
    }

    public Config serialize() {
        Config config = Config.inMemory();
        config.set("name", name);
        config.set("address", address);
        config.set("resourcePackStatus", resourcePackStatus.name());
        return config;
    }

    public ServerData toServerData() {
        ServerData serverData = new ServerData(name, address, type);
        serverData.setResourcePackStatus(resourcePackStatus);
        return serverData;
    }

    public boolean isNotInServerList(ServerList serverList) {
        return serverList.get(address) == null;
    }

    public void appendToServerList(ServerList serverList) {
        if (isNotInServerList(serverList)) {
            serverList.add(toServerData(), false);
            DefaultServerListingsCommon.INSTANCE.createInfoLog("Added default server listing: " + name + " (" + address + ")");
        } else {
            DefaultServerListingsCommon.INSTANCE.createInfoLog("Server listing already exists: " + name + " (" + address + ")");
        }
    }
}