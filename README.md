## (visva.micro) config > json

Part of `visva.micro` initiative, this is a simple to use Java-based configuration manager that provides configuration data access features:
- reading specific elements (subset) from configuration
- writing specific elements (subset) to configuration
    - creating new entries
    - updating existing entries

Internally, configuration data is persistend to a file in JSON format.

### TODOs

1. More return types to be supported by the generic `getConfigParamValue(T returnType, String... params)` method of `ConfigManager`.
2. Initialization of `ConfigManager` should support additional options
      - For read-only needs, the `URL` of the config file is enough.
3. Reloading options.
      - Auto-detecting a config file change and run a callback.
4. Show details of the config data source
      - update timestamp and checksum of the file
