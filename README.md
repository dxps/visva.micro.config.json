## visva.micro > config > json

Part of `visva.micro` initiative, this is a simple to use Java-based configuration manager that provides configuration data access features:
- reading specific elements (subset) from configuration
- writing specific elements (subset) to configuration
    - creating new entries
    - updating existing entries

Internally, configuration data is persistend to a file in JSON format.

### TODOs

1. More return types to be supported by the generic `getConfigParamValue(T returnType, String... params)` method of `ConfigManager`.
2. Initialization of `ConfigManager` should support additional options
      - example: providing the `URL` of the config file, for read-only cases.
3. Reloading options.
