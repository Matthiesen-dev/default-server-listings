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
        builder.comment("Default Server Listings Configuration").push("config");

        enabled = builder.comment("Enable or disable the Default Server Listings mod")
                .define("enabled", true);
        serverListings = builder.comment("List of default server listings")
                .defineList(
                        "serverListings",
                        List.of(),
                        null,
                        entry -> entry instanceof Config && DefaultServerListingsConfig.ServerListingEntry.isValid((Config) entry)
                );

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
