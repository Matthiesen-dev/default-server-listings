package dev.matthiesen.default_server_listings.common;

import com.electronwill.nightconfig.core.Config;
import dev.matthiesen.default_server_listings.common.interfaces.ServerListingEntry;
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
        builder.comment("Default Server Listings Configuration")
                .translation("default_server_listings.configuration.config")
                .push("config");
        enabled = builder.comment("Enable or disable the Default Server Listings mod")
                .translation("default_server_listings.configuration.config.enabled")
                .define("enabled", true);

        builder.comment(
                        "List of default server listings",
                        "Each entry should be a config object with the following fields:",
                        "  - name: The display name of the server",
                        "  - address: The IP address or domain of the server",
                        "  - resourcePackStatus: The resource pack status (ENABLED, DISABLED, PROMPT)"
                )
                .translation("default_server_listings.configuration.config.serverListings")
                .push("serverListings");
        serverListings = builder.comment("Server List Entries")
                .translation("default_server_listings.configuration.config.serverListings.entries")
                .defineList(
                        List.of("entries"),
                        ServerListingEntry.DEFAULT_ENTRIES,
                        null,
                        entry -> entry instanceof Config && ServerListingEntry.isValid((Config) entry)
                );
        builder.pop(); // pop the "serverListings" section

        builder.pop(); // pop the "config" section
    }
}
