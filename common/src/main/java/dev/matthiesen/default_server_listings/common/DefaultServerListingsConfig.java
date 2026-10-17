package dev.matthiesen.default_server_listings.common;

import com.electronwill.nightconfig.core.Config;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.stream.Stream;

public final class DefaultServerListingsConfig {
    public static final DefaultServerListingsConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    static {
        Pair<DefaultServerListingsConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(DefaultServerListingsConfig::new);
        CONFIG = specPair.getLeft();
        CONFIG_SPEC = specPair.getRight();
    }

    public static boolean isEnabled() {
        return CONFIG.enabled.getAsBoolean();
    }

    public static List<ServerListingEntry> getServerListings() {
        return CONFIG.serverListings.get().stream()
                .map(ServerListingEntry::deserialize)
                .toList();
    }

    public ModConfigSpec.BooleanValue enabled;
    public ModConfigSpec.ConfigValue<List<? extends Config>> serverListings;

    public DefaultServerListingsConfig(ModConfigSpec.Builder builder) {
        builder.comment(
                        "Default Server Listings Configuration",
                        "Each entry should be a config object with the following fields:",
                        "  - name: The display name of the server",
                        "  - address: The IP address or domain of the server",
                        "  - resourcePackStatus: The resource pack status (ENABLED, DISABLED, PROMPT)"
                )
                .translation("default_server_listings.configuration.config")
                .push("config");
        enabled = builder.comment("Enable or disable the Default Server Listings mod")
                .translation("default_server_listings.configuration.config.enabled")
                .define("enabled", true);
        serverListings = builder.comment("Server List Entries")
                .translation("default_server_listings.configuration.config.serverListings")
                .defineList(
                        List.of("serverListings"),
                        ServerListingEntry.DEFAULT_ENTRIES,
                        null,
                        entry -> entry instanceof Config && ServerListingEntry.isValid((Config) entry)
                );

        builder.pop(); // pop the "config" section
    }

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
}
