# Broadlink Android Demo App

Minimal Android app using `java-broadlink` to discover and control Broadlink devices.

## Setup

```groovy
// build.gradle (Module: app)
dependencies {
    implementation 'io.github.sushantku1099:java-broadlink:1.0.0'
}
```

```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.CHANGE_WIFI_MULTICAST_STATE"/>
```

## Usage

```kotlin
// MainActivity.kt
class MainActivity : AppCompatActivity() {

    private val client = BroadlinkClient()
    private var device: BroadlinkDevice? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            // 1. Discover devices
            val devices = client.discover().get()
            device = devices.firstOrNull() ?: return@launch

            // 2. Authenticate
            device!!.authenticate().get()

            // 3. Use it
            if (device is RmMiniDevice) {
                (device as RmMiniDevice).enterLearning().get()
                val irCode = (device as RmMiniDevice).checkData().get()
                (device as RmMiniDevice).sendData(irCode).get()
            }
        }
    }
}
```
