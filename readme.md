
![Phantom Loader](logo.png)

Phantom Loader is an API that automates part of the process of creating Minecraft mods for multiple mod loaders by generating loader-specific code at build time.

## About

Normally, when creating a mod for both Fabric and Forge, mod developers have to create separate modules for both mod loaders and add loader-specific code that has mostly the same function.
**Phantom Loader** allows you to automatically generate Forge and Fabric initializer classes on build time by keeping all code, or most of the code, in the common module.

The **Phantom Library** also takes care of abstracting each mod loader's basic functionalities to allow the developer to only depend on the abstraction from the common module.

### Before:

```
Project
 |--common
 |    |--src/main/java
 |    |    |--common/...
 |    |    |--AbstractRegistry.java
 |    |    |--CommonInit.java
 |    |--src/main/resources
 |    |    |--assets/...
 |    |    |--data/...
 |    |--build.gradle
 |--fabric
 |    |--src/main/java
 |    |    |--FabricRegistry.java
 |    |    |--FabricInitializer.java
 |    |--src/main/resources
 |    |    |--fabric.mod.json
 |    |--build.gradle
 |--forge
      |--src/main/java
      |    |--ForgeRegistry.java
      |    |--ForgeInitializer.java
      |--src/main/resources
      |    |--META-INF/mods.toml
      |--build.gradle
```

### After:

```
Project
 |--common
 |    |--src/main/java
 |    |    |--content/...
 |    |    |--ModRegistry.java
 |    |--src/main/resource
 |    |    |--assets/...
 |    |    |--data/...
 |    |--build.gradle
 |--fabric
 |    |--build.gradle
 |--forge
      |--build.gradle
```

## For developers

Documentation for setting up and using **Phantom Loader** can be found on the [Wiki](https://github.com/PhantomLoader/PhantomLoader/wiki).

An example mod using **Phantom Loader** can be found [here](https://github.com/PhantomLoader/ExampleMod).

## Credits

Author: [HexagonNico](https://github.com/HexagonNico)

## License

This software is licensed under the [Apache License 2.0](license)
