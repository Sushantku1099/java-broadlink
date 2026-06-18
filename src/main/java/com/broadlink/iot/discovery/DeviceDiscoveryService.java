package com.broadlink.iot.discovery;

import com.broadlink.iot.core.BroadlinkDevice;
import com.broadlink.iot.core.BroadlinkTransport;
import com.broadlink.iot.core.DeviceMetadata;
import com.broadlink.iot.protocol.Checksum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.*;

/**
 * UDP broadcast discovery service. Broadcasts 48-byte discovery packet (cmd 0x06)
 * on all IPv4 interfaces and collects responses from Broadlink devices.
 */
public class DeviceDiscoveryService {

    private static final Logger log = LoggerFactory.getLogger(DeviceDiscoveryService.class);
    private static final int DEFAULT_PORT = 80;
    private static final long DEFAULT_TIMEOUT_MS = 5000;

    private final NetworkInterfaceProvider niProvider;
    private final DeviceTypeRegistry registry;

    public DeviceDiscoveryService() {
        this.niProvider = new NetworkInterfaceProvider();
        this.registry = new DeviceTypeRegistry();
    }

    public DeviceDiscoveryService(NetworkInterfaceProvider niProvider, DeviceTypeRegistry registry) {
        this.niProvider = niProvider;
        this.registry = registry;
    }

    public CompletableFuture<List<BroadlinkDevice>> discover() {
        return discover(DEFAULT_PORT, DEFAULT_TIMEOUT_MS);
    }

    public CompletableFuture<List<BroadlinkDevice>> discover(int port, long timeout) {
        List<NetworkInterfaceProvider.NetIface> interfaces = niProvider.getAvailableInterfaces();
        if (interfaces.isEmpty()) {
            return CompletableFuture.completedFuture(Collections.emptyList());
        }

        List<BroadlinkDevice> devices = new CopyOnWriteArrayList<>();
        List<DatagramSocket> sockets = new ArrayList<>();
        byte[] discoveryPacket = buildDiscoveryPacket();

        for (NetworkInterfaceProvider.NetIface iface : interfaces) {
            try {
                DatagramSocket socket = BroadlinkTransport.createListeningSocket(
                    InetAddress.getByName(iface.address));
                sockets.add(socket);

                InetAddress broadcast = InetAddress.getByName(iface.broadcastAddress);
                DatagramPacket sendPacket = new DatagramPacket(
                    discoveryPacket, discoveryPacket.length, broadcast, port);
                socket.send(sendPacket);
                log.debug("Discovery broadcast on {} -> {}", iface.address, iface.broadcastAddress);

                Thread listener = new Thread(() -> listen(socket, devices));
                listener.setDaemon(true);
                listener.start();
            } catch (Exception e) {
                log.warn("Failed to bind/send on {}: {}", iface.address, e.getMessage());
            }
        }

        CompletableFuture<List<BroadlinkDevice>> future = new CompletableFuture<>();
        CompletableFuture.delayedExecutor(timeout, TimeUnit.MILLISECONDS).execute(() -> {
            for (DatagramSocket s : sockets) {
                try { s.close(); } catch (Exception ignored) {}
            }
            log.debug("Discovery complete: {} devices found", devices.size());
            future.complete(new ArrayList<>(devices));
        });
        return future;
    }

    private void listen(DatagramSocket socket, List<BroadlinkDevice> devices) {
        try {
            socket.setSoTimeout(500);
            while (!socket.isClosed()) {
                try {
                    byte[] buf = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);

                    InetSocketAddress sender = (InetSocketAddress) packet.getSocketAddress();
                    byte[] data = Arrays.copyOf(packet.getData(), packet.getLength());

                    int deviceType = (data[0x34] & 0xFF) | ((data[0x35] & 0xFF) << 8);
                    byte[] mac = new byte[6];
                    for (int i = 0; i < 6; i++) mac[i] = data[0x3F - i];

                    boolean dup = false;
                    for (BroadlinkDevice d : devices) {
                        if (Arrays.equals(d.getMetadata().getMac(), mac)) { dup = true; break; }
                    }
                    if (dup) continue;

                    int nameEnd = 0x40;
                    while (nameEnd < 0x7E && data[nameEnd] != 0) nameEnd++;
                    String name = new String(data, 0x40, nameEnd - 0x40, StandardCharsets.UTF_8);

                    boolean isLocked = data.length > 0x7F && data[0x7F] != 0;

                    DeviceTypeRegistry.DeviceEntry entry = registry.get(deviceType);
                    DeviceMetadata metadata = new DeviceMetadata(
                        sender, mac, deviceType,
                        entry.model, entry.manufacturer, name, isLocked);

                    BroadlinkDevice device = instantiateDevice(entry, metadata);
                    devices.add(device);
                    log.debug("Discovered: {} (0x{})", name, Integer.toHexString(deviceType));
                } catch (SocketTimeoutException ignored) {}
            }
        } catch (Exception e) {
            if (!socket.isClosed()) log.debug("Discovery listener: {}", e.getMessage());
        }
    }

    static byte[] buildDiscoveryPacket() {
        ZonedDateTime now = ZonedDateTime.now();
        int tzHours = now.getOffset().getTotalSeconds() / 3600;
        int yearM1900 = now.getYear() - 1900;

        byte[] packet = new byte[0x30];
        if (tzHours < 0) {
            packet[0x08] = (byte) (0xFF + tzHours - 1);
            packet[0x09] = (byte) 0xFF;
            packet[0x0A] = (byte) 0xFF;
            packet[0x0B] = (byte) 0xFF;
        } else {
            packet[0x08] = (byte) tzHours;
        }
        packet[0x0C] = (byte) (yearM1900 & 0xFF);
        packet[0x0D] = (byte) ((yearM1900 >> 8) & 0xFF);
        packet[0x0E] = (byte) now.getMinute();
        packet[0x0F] = (byte) now.getHour();
        packet[0x10] = (byte) (yearM1900 % 100);
        packet[0x11] = (byte) (now.getDayOfWeek().getValue() % 7);
        packet[0x12] = packet[0x11];
        packet[0x13] = (byte) (now.getMonthValue() - 1);
        packet[0x26] = 0x06;

        int cs = Checksum.compute(packet);
        packet[0x20] = (byte) (cs & 0xFF);
        packet[0x21] = (byte) ((cs >> 8) & 0xFF);
        return packet;
    }

    private BroadlinkDevice instantiateDevice(DeviceTypeRegistry.DeviceEntry entry, DeviceMetadata metadata) {
        try {
            Constructor<? extends BroadlinkDevice> ctor =
                entry.deviceClass.getDeclaredConstructor(DeviceMetadata.class);
            return ctor.newInstance(metadata);
        } catch (Exception e) {
            log.warn("Failed to instantiate {}: {}", entry.deviceClass.getSimpleName(), e.getMessage());
            return new BroadlinkDevice(metadata);
        }
    }
}
