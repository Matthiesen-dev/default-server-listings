package dev.matthiesen.default_server_listings.common;

import com.electronwill.nightconfig.core.Config;
import net.minecraft.client.multiplayer.ServerData;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public final class DefaultServerListingsConfig {
    public static final DefaultServerListingsConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    static {
        Pair<DefaultServerListingsConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(DefaultServerListingsConfig::new);
        CONFIG = specPair.getLeft();
        CONFIG_SPEC = specPair.getRight();
    }

    public static List<ServerListingEntry> getServerListings() {
        return CONFIG.serverListings.get().stream()
                .map(ServerListingEntry::deserialize)
                .toList();
    }

    public ModConfigSpec.BooleanValue enabled;
    public ModConfigSpec.ConfigValue<List<? extends Config>> serverListings;

    public DefaultServerListingsConfig(ModConfigSpec.Builder builder) {
        builder.comment("Default Server Listings Configuration")
                .translation("configuration.default_server_listings.config")
                .push("config");
        enabled = builder.comment("Enable or disable the Default Server Listings mod")
                .translation("configuration.default_server_listings.config.enabled")
                .define("enabled", true);

        builder.comment(
                        "List of default server listings",
                        "Each entry should be a config object with the following fields:",
                        "  - name: The display name of the server",
                        "  - address: The IP address or domain of the server",
                        "  - resourcePackStatus: The resource pack status (ENABLED, DISABLED, PROMPT)"
                )
                .translation("configuration.default_server_listings.config.serverListings")
                .push("serverListings");
        serverListings = builder.comment("Server List Entries")
                .translation("configuration.default_server_listings.config.serverListings.entries")
                .defineList(
                        List.of("entries"),
                        List.of(),
                        null,
                        entry -> entry instanceof Config && DefaultServerListingsConfig.ServerListingEntry.isValid((Config) entry)
                );
        builder.pop(); // pop the "serverListings" section

        builder.pop(); // pop the "config" section
    }

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
}
