# java-broadlink

A promise based Java module created from Node.js API [node-broadlink](https://github.com/ThomasTavernier/node-broadlink) and Python API [python-broadlink](http://github.com/mjg59/python-broadlink) for controlling [Broadlink](http://www.ibroadlink.com/rm/) IR/RF controllers and IoT devices.

**Author:** [Sushant Sagar](https://github.com/Sushantku1099)

---

## Platforms Supported

| Platform | Module | External Dependencies |
|---|---|---|
| **Android (Java/Kotlin)** | `broadlink-core` + `broadlink-android` | Zero (built-in JSON + `android.util.Log`) |
| **Spring Boot** | Maven `java-broadlink` | Jackson + SLF4J |
| **Desktop Java / JavaFX** | `broadlink-core` | **Zero** (ManualJsonSerializer built-in) |
| **Kotlin (any platform)** | `broadlink-core` | **Zero** |
| **CLI / Server / Microservice** | `broadlink-core` | **Zero** |

---

## Android App Setup

```groovy
// app/build.gradle
dependencies {
    implementation project(':broadlink-core')
    implementation project(':broadlink-android')
}
```

```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.CHANGE_WIFI_MULTICAST_STATE"/>
```

```kotlin
val client = BroadlinkClient()
val devices = client.discover().get()
val device = devices[0]
device.authenticate().get()

if (device is RmMiniDevice) {
    device.enterLearning().get()
    val irCode = device.checkData().get()
    device.sendData(irCode).get()
}
```

---

## Quick Start (Java)

```java
BroadlinkClient broadlink = new BroadlinkClient();
List<BroadlinkDevice> devices = broadlink.discover().get();
devices.get(0).authenticate().get();
```

---

## Example Use

Setup a new device on your local wireless network:

1. Put the device into AP Mode
2. Long press the reset button until the blue LED is blinking quickly.
3. Long press again until blue LED is blinking slowly.
4. Manually connect to the WiFi SSID named **BroadlinkProv**.
5. Run `setup()` and provide your ssid, network password (if secured), and set the security mode.
6. Security mode options are `NONE`, `WEP`, `WPA1`, `WPA2`, `WPA1_2`.

```java
import com.broadlink.iot.security.BroadlinkCrypto.SecurityMode;
broadlink.setup("myssid", "mynetworkpass", SecurityMode.WPA2).get();
```

Discover available devices:

```java
List<BroadlinkDevice> devices = broadlink.discover().get();
BroadlinkDevice device = devices.get(0);
device.authenticate().get();
```

### Remote Control (IR + RF)

```java
import com.broadlink.iot.device.remote.*;

// Enter IR learning mode
RmMiniDevice rm = (RmMiniDevice) device;
rm.enterLearning().get();
byte[] irPacket = rm.checkData().get();
String hex = RmMiniDevice.PulseCodec.pulsesToHex(irPacket);
rm.sendData(irPacket).get();
rm.sendData(hex).get();

// RF sweep (RmPro / Rm4Pro only)
RmProDevice rmpro = (RmProDevice) device;
rmpro.sweepFrequency().get();
rmpro.cancelSweepFrequency().get();
boolean found = rmpro.checkFrequency().get(); // true = locked onto frequency
rmpro.findRfPacket().get();
byte[] rfPacket = rmpro.checkData().get();

// Temperature (RmPro)
double temp = rmpro.checkTemperature().get(); // e.g. 45.5

// Temperature + Humidity (Rm4)
Rm4MiniDevice rm4 = (Rm4MiniDevice) device;
double[] sensors = rm4.checkSensors().get();
// sensors[0] = temp (degC), sensors[1] = humidity (%)
```

### Smart Plugs & Power Strips

```java
import com.broadlink.iot.device.power.*;
import com.broadlink.iot.model.Sp4State;

// SP2/SP3 - Set power and check state
Sp2Device sp2 = (Sp2Device) device;
sp2.setPower(true).get();
sp2.checkPower().get();

// SP2s - Energy monitoring
Sp2sDevice sp2s = (Sp2sDevice) device;
sp2s.getEnergy().get();

// SP3 - Nightlight control
Sp3Device sp3 = (Sp3Device) device;
sp3.setNightlight(true).get();
sp3.checkNightlight().get();

// SP4 - Full JSON state get/set
Sp4Device sp4 = (Sp4Device) device;
Sp4State state = sp4.getState().get();
// state.isPwr(), state.isNtlight(), state.isIndicator(), state.isChildlock()
state.setPwr(false);
sp4.setState(state).get();

// MP1 - 4-socket power strip
Mp1Device mp1 = (Mp1Device) device;
mp1.setPower(1, true).get();  // socket 1 ON
Mp1Device.Mp1PowerState ms = mp1.checkPower().get();
// ms.s1, ms.s2, ms.s3, ms.s4
```

### Environmental Sensor (A1)

```java
import com.broadlink.iot.device.sensor.A1Device;

A1Device a1 = (A1Device) device;
A1Device.A1SensorData data = a1.checkSensors().get();
// data.t = temperature (degC)
// data.h = humidity (%)
// data.l = light ("dark"|"dim"|"normal"|"bright"|"unknown")
// data.a = air quality ("excellent"|"good"|"normal"|"bad"|"unknown")
// data.n = noise ("quiet"|"normal"|"noisy"|"unknown")
```

### Curtain Motor (Dooya)

```java
import com.broadlink.iot.device.cover.DooyaDevice;

DooyaDevice dooya = (DooyaDevice) device;
dooya.open().get();
dooya.closeCurtain().get();
dooya.stop().get();
int percentage = dooya.getPercentage().get();
dooya.setPercentageAndWait(50).get(); // move to 50% and wait
```

### Thermostat (Hysen)

```java
import com.broadlink.iot.device.climate.HysenDevice;

HysenDevice hysen = (HysenDevice) device;
double roomTemp = hysen.getTemp().get();
hysen.getExternalTemp().get();
hysen.setTemp(22.5).get();
hysen.setPower(1, 0).get();      // power on, unlock
hysen.setMode(1, 2, 0).get();    // auto mode, loop mode, sensor
hysen.setTime(11, 55, 37, 7).get();
```

### Alarm System (S1C)

```java
import com.broadlink.iot.device.alarm.S1CDevice;

S1CDevice s1c = (S1CDevice) device;
S1CDevice.S1CStatus status = s1c.getSensorsStatus().get();
// status.count
for (S1CDevice.S1CSensor sensor : status.sensors) {
    // sensor.type ("Door Sensor"|"Key Fob"|"Motion Sensor")
    // sensor.serial (8-char hex)
    // sensor.status
}
```

### Smart Hub (S3)

```java
import com.broadlink.iot.device.hub.S3HubDevice;
import com.broadlink.iot.model.S3State;

S3HubDevice s3 = (S3HubDevice) device;
List<String> dids = s3.getSubDevices().get();
for (String did : dids) {
    S3State s3state = s3.getState(did).get();
    // s3state.isPwr1(), s3state.isPwr2(), s3state.isPwr3()
}
```

### Smart Bulbs (Lb1/Lb2)

```java
import com.broadlink.iot.device.light.*;
import com.broadlink.iot.model.LightBulbState;

Lb1Device lb1 = (Lb1Device) device;
LightBulbState lbState = lb1.getState().get();

// Set red at full brightness
LightBulbState newState = new LightBulbState();
newState.setPwr(1);
newState.setRed(255); newState.setGreen(0); newState.setBlue(0);
newState.setBrightness(100);
lb1.setState(newState).get();
```

---

## Type-Safe Device Handling

```java
for (BroadlinkDevice device : broadlink.discover().get()) {
    device.authenticate().get();

    if (device instanceof RmProDevice rmpro) {
        rmpro.sweepFrequency().get();
        while (!rmpro.checkFrequency().get()) Thread.sleep(200);
        rmpro.findRfPacket().get();
        byte[] rfPacket = rmpro.checkData().get();
        rmpro.sendData(rfPacket).get();

    } else if (device instanceof Sp4Device sp4) {
        sp4.setPower(true).get();
        System.out.println(sp4.checkPower().get());

    } else if (device instanceof A1Device a1) {
        var d = a1.checkSensors().get();
        System.out.printf("Temp: %.1f C  Humidity: %.1f %%\n", d.t, d.h);
    }
}
```

---

## Async (CompletableFuture)

```java
broadlink.discover().thenCompose(devices -> {
    BroadlinkDevice device = devices.get(0);
    return device.authenticate().thenApply(d -> device);
}).thenAccept(device -> {
    if (device instanceof RmMiniDevice rm) {
        rm.enterLearning()
            .thenCompose(v -> rm.checkData())
            .thenAccept(data -> System.out.println("IR learned: " + data.length + " bytes"));
    }
}).get();
```

---

## Supported Devices (124 Type IDs · 19 Device Classes)

| Type IDs | Class | Category |
|---|---|---|
| 0x2737–0x27DE | `RmMiniDevice` | RM mini / RM mini 3 |
| 0x5F36–0x6508 | `RmMiniBDevice` | RM mini B-variant |
| 0x2712–0x27C3 | `RmProDevice` | RM pro / pro+ / home / plus |
| 0x51DA–0x653A | `Rm4MiniDevice` | RM4 mini / RM4C / RM4S |
| 0x520B–0x653C | `Rm4ProDevice` | RM4 pro / RM4C pro |
| 0x0000 | `Sp1Device` | SP1 |
| 0x2717–0x7D0D | `Sp2Device` | SP2 / SP mini / NEO / Honeywell |
| 0x2711–0x2736 | `Sp2sDevice` | SP2 energy / NEO PRO / Ego |
| 0x2733–0x7D00 | `Sp3Device` | SP3 / SP3-EU |
| 0x9479–0x947A | `Sp3sDevice` | SP3S-US / SP3S-EU |
| 0x7568–0xA5D3 | `Sp4Device` | SP4L / SP4M / MCB1 / SCB1E |
| 0x5115–0x6494 | `Sp4bDevice` | SP4 Rev B / AHC/U-01 / SCB2 |
| 0x51E3 | `Bg1Device` | BG800/BG900 Gang Switch |
| 0x4EB5–0x4F65 | `Mp1Device` | MP1-1K4S / MP1-1K3S2U |
| 0x2714 | `A1Device` | e-Sensor |
| 0x2722 | `S1CDevice` | S2KIT Alarm |
| 0xA59C–0xA64D | `S3HubDevice` | S3 Hub |
| 0x5043–0x644E | `Lb1Device` | LB1 / SB800TD / LB27 R1 |
| 0xA4F4–0xA5F7 | `Lb2Device` | LB27 R1 |
| 0x4EAD | `HysenDevice` | HY02/HY03 Thermostat |
| 0x4E4D | `DooyaDevice` | DT360E-45/20 Curtain Motor |

---

## Project Structure

```
java-broadlink/
├── broadlink-core/                ⭐ ZERO deps. Android, Desktop, Server -- sab pe chalta hai
│   └── com.broadlink.iot
│       ├── BroadlinkClient           Public API facade
│       ├── core/
│       │   ├── BroadlinkDevice       Abstract base for all devices
│       │   ├── BroadlinkCipher       AES-128-CBC encrypt/decrypt
│       │   ├── BroadlinkPacketCodec  56-byte header + checksum builder
│       │   ├── BroadlinkTransport    UDP socket management
│       │   ├── DeviceIdentity        Key, IV, device ID, sequence counter
│       │   └── DeviceMetadata        MAC, host, type, model, name
│       ├── protocol/
│       │   ├── BinaryStruct          <I, <H, <bb, <HHHBBI binary codec
│       │   ├── Checksum              Additive 0xBEAF checksum
│       │   └── Crc16Modbus           CRC-16/MODBUS for Hysen thermostat
│       ├── codec/JsonCodec           GEN4 + GEN4B JSON-over-binary
│       ├── device/
│       │   ├── remote/               RmMini, RmPro, RmMiniB, Rm4Mini, Rm4Pro
│       │   ├── power/                Sp1, Sp2, Sp2s, Sp3, Sp3s, Sp4, Sp4b, Bg1, Mp1
│       │   ├── sensor/A1Device       Environmental sensor
│       │   ├── hub/S3HubDevice       Sub-device management
│       │   ├── light/                Lb1, Lb2 smart bulbs
│       │   ├── climate/HysenDevice   Thermostat
│       │   ├── cover/DooyaDevice     Curtain motor
│       │   └── alarm/S1CDevice       Alarm system
│       ├── model/                    Sp4State, Bg1State, S3State, LightBulbState
│       ├── discovery/                UDP broadcast + 124-type device registry
│       ├── security/BroadlinkCrypto  Key constants + SecurityMode enum
│       ├── setup/                    Wi-Fi provisioning service
│       └── spi/                      Logger, JsonSerializer (pluggable interfaces)
│
├── broadlink-android/              Android-specific module (2 files)
│   ├── AndroidLogger.java          android.util.Log adapter
│   └── AndroidLoggerProvider.java  ServiceLoader provider
│
├── broadlink-json-gson/            Optional Gson module (1 file)
│   └── GsonSerializer.java
│
├── src/                            Standalone build (Jackson + SLF4J)
├── lib/                            Dependencies for src/ build
├── pom.xml                         Maven build descriptor
└── build.sh                        ./build.sh = compile + 53 tests
```

---

## SPI — Pluggable Architecture

`broadlink-core` has **ZERO hard dependencies**. Everything works via Java `ServiceLoader`:

| SPI Interface | Default (built-in) | Android Plugin | Desktop Plugin |
|---|---|---|---|
| `BroadlinkLogger` | No-op (silent) | `AndroidLogger` (`android.util.Log`) | `Slf4jLogger` |
| `JsonSerializer` | `ManualJsonSerializer` (handles all 4 models, ~150 lines) | `GsonSerializer` | `JacksonSerializer` |
| `NetworkProvider` | `StandardNetworkProvider` | `AndroidNetworkProvider` (MulticastLock) | Same as standard |

---

## Build from Source

```bash
git clone https://github.com/Sushantku1099/java-broadlink.git
cd java-broadlink
./build.sh
```

```
=== Compiling sources (42 files) ===
OK: 55 classes

=== Compiling tests (8 files) ===
OK: 8 test classes

=== Running tests ===
Tests found:    53
Tests started:  53
Tests succeeded:53
Tests failed:   0
Tests skipped:  0

ALL TESTS PASSED
```

---

## Requirements

| Module | Min JDK | External Dependencies |
|---|---|---|
| `broadlink-core` | **JDK 11** | **Zero** |
| `broadlink-android` | Android API 21+ | Android SDK only |
| `broadlink-json-gson` | JDK 11 | Gson 2.8+ |
| `src/` (standalone) | JDK 11 | Jackson 2.17+, SLF4J 2.0+ |

---

## Installation

### Maven

```xml
<dependency>
    <groupId>com.broadlink</groupId>
    <artifactId>java-broadlink</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'com.broadlink:java-broadlink:1.0.0'
```

### Manual JAR

Download the latest JAR from [Releases](https://github.com/Sushantku1099/java-broadlink/releases).

---

## License

MIT — see [LICENSE](LICENSE)

---

**Author:** [Sushant Sagar](https://github.com/Sushantku1099)  
Based on [node-broadlink](https://github.com/ThomasTavernier/node-broadlink) by Thomas Tavernier and [python-broadlink](https://github.com/mjg59/python-broadlink) by Matthew Garrett.

---

[![Maven Central](https://img.shields.io/maven-central/v/io.github.sushantku1099/java-broadlink)](https://central.sonatype.com/artifact/io.github.sushantku1099/java-broadlink)

## Maven Central

```xml
<dependency>
    <groupId>io.github.sushantku1099</groupId>
    <artifactId>java-broadlink</artifactId>
    <version>1.0.0</version>
</dependency>
```
