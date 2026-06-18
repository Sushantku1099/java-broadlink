package com.broadlink.iot;

import com.broadlink.iot.core.BroadlinkDevice;
import com.broadlink.iot.discovery.DeviceDiscoveryService;
import com.broadlink.iot.security.BroadlinkCrypto.SecurityMode;
import com.broadlink.iot.setup.DeviceProvisioningService;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Public API facade for the java-broadlink SDK.
 * Usage:
 *   BroadlinkClient client = new BroadlinkClient();
 *   List<BroadlinkDevice> devices = client.discover().get();
 *   devices.get(0).authenticate().get();
 *   if (device instanceof RmMiniDevice rm) { rm.enterLearning().get(); }
 */
public class BroadlinkClient {

    private final DeviceDiscoveryService discoveryService;
    private final DeviceProvisioningService provisioningService;

    public BroadlinkClient() {
        this.discoveryService = new DeviceDiscoveryService();
        this.provisioningService = new DeviceProvisioningService();
    }

    public CompletableFuture<List<BroadlinkDevice>> discover() {
        return discoveryService.discover();
    }

    public CompletableFuture<List<BroadlinkDevice>> discover(long timeoutMs) {
        return discoveryService.discover(80, timeoutMs);
    }

    public CompletableFuture<List<BroadlinkDevice>> discover(int port, long timeoutMs) {
        return discoveryService.discover(port, timeoutMs);
    }

    public CompletableFuture<DeviceProvisioningService.SetupResult> setup(
            String ssid, String password, SecurityMode mode) {
        return provisioningService.provision(ssid, password, mode);
    }
}
