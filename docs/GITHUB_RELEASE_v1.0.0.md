# java-broadlink v1.0.0 — Initial Release

Production-grade Java SDK for Broadlink IoT devices. Controls 124 device types across 19 categories.

## Installation

### Maven
```xml
<dependency>
    <groupId>io.github.sushantku1099</groupId>
    <artifactId>java-broadlink</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle
```groovy
implementation 'io.github.sushantku1099:java-broadlink:1.0.0'
```

## Supported Devices

| Type IDs | Class | Category |
|---|---|---|
| 0x2737–0x27DE | `RmMiniDevice` | RM mini / RM mini 3 |
| 0x2712–0x27C3 | `RmProDevice` | RM pro / RM pro+ / RM home |
| 0x51DA–0x653A | `Rm4MiniDevice` | RM4 mini / RM4C / RM4S |
| 0x520B–0x653C | `Rm4ProDevice` | RM4 pro / RM4C pro |
| 0x0000–0xA5D3 | `Sp1–Sp4b` | Smart Plugs |
| 0x4EB5–0x4F65 | `Mp1Device` | Power Strip |
| 0x2714 | `A1Device` | Environmental Sensor |
| 0x5043–0xA5F7 | `Lb1/Lb2` | Smart Bulbs |
| 0x4EAD | `HysenDevice` | Thermostat |
| 0x4E4D | `DooyaDevice` | Curtain Motor |
| 0x2722 | `S1CDevice` | Alarm System |
| 0xA59C–0xA64D | `S3HubDevice` | Smart Hub |

## What's Included

- **19 device classes**, 124 type IDs
- **Multi-platform**: Android, Spring Boot, Desktop, Kotlin
- **Zero-dependency** `broadlink-core` module
- **SPI pluggable**: Logger, JsonSerializer, NetworkProvider
- **53 tests** validating packet byte structures
- Android, Gson, and Jackson optional modules

## Author

[Sushant Sagar](https://github.com/Sushantku1099)

Based on [node-broadlink](https://github.com/ThomasTavernier/node-broadlink) and [python-broadlink](https://github.com/mjg59/python-broadlink).
