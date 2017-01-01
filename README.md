## visva.micro > json-config

Part of `visva.micro` initiative, this is a simple Java-based configuration manager for managing (reading from and writing to) a configuration in JSON format, including persistency features (loading from and saving to) a file.

### TODOs

1. More return types to be supported by the generic `getConfigParamValue(T returnType, String... params)` method of `ConfigManager`.
2. Initialization of `ConfigManager` should support additional options, like providing the `URL` of the config file.
3. Reloading options.
