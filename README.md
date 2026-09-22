# Default Server Listings

<div>
  <img src="https://mods.matthiesen.dev/badges/matthiesenCore.svg" alt="Matthiesen Core">
</div>

Default Server Listings is a Simple client side mod that adds default server list entries to the server-list. This is useful for modpacks that want to provide
a curated list (or even just a single entry) of servers for their pack users to connect to. When the mod is installed and enabled, missing entries from the configured
list will be automatically added to the client's server list. If the mod is disabled, no entries will be added to the server list.

## Quick Start

A default configuration file is generated with a default example when the mod is first run. You can edit this file and include it when you export your modpack from your launcher.

The configuration file is located at `<game_directory>/config/default-server-listings.toml` and by default looks like this:

```toml
#Default Server Listings Configuration
[config]
	#Enable or disable the Default Server Listings mod
	enabled = true

		#List of default server listings
		#Each entry should be a config object with the following fields:
		#  - name: The display name of the server
		#  - address: The IP address or domain of the server
		#  - resourcePackStatus: The resource pack status (ENABLED, DISABLED, PROMPT)
		#Server List Entries
		[[config.serverListings.entries]]
			name = "Local Host"
			resourcePackStatus = "PROMPT"
			address = "127.0.0.1"
```

## Requirements

- [Matthiesen Core](https://modrinth.com/mod/matthiesen-core)
- [Fabric API](https://modrinth.com/mod/fabric-api) (Fabric only)
- [Forge Config API Port](https://modrinth.com/mod/forge-config-api-port) (Fabric only)

## Docs

Documentation for this mod can be found at [mods.matthiesen.dev](https://mods.matthiesen.dev/default-server-listings/)

## Version Compatibility

| Minecraft Version | Matthiesen Core Version | Mod Version |
|-------------------|-------------------------|-------------|
| 1.21.1            | 1.x.x                   | 1.x.x       |

## FastStats Metrics

This mod uses [FastStats](https://faststats.dev) to collect anonymous usage statistics. This helps the developer understand
how this mod is being used and improve it over time. You can learn more about the data collected and how it is used by visiting
[FastStats: Information](https://faststats.dev/info).

You can also view the data collected by this mod on the [FastStats: Default Server Listings](https://faststats.dev/project/default-server-listings) page.

To opt out of this data collection, set the `enabled` property to `false` in the `<game_directory>/config/matthiesen_core/metrics.properties` file.

## License

MIT - see `LICENSE`.
