package com.broadlink.iot.setup;

import com.broadlink.iot.core.BroadlinkTransport;
import com.broadlink.iot.protocol.Checksum;
import com.broadlink.iot.security.BroadlinkCrypto.SecurityMode;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

/** Wi-Fi provisioning for new Broadlink devices in AP mode. */
public class DeviceProvisioningService {

    public CompletableFuture<SetupResult> provision(String ssid, String password, SecurityMode mode) {
        return CompletableFuture.supplyAsync(() -> {
            byte[] packet = buildSetupPacket(ssid, password, mode.getCode());
            try {
                InetAddress broadcast = InetAddress.getByName("255.255.255.255");
                BroadlinkTransport transport = new BroadlinkTransport(null);
                transport.sendBroadcast(packet, broadcast, 80);
                transport.close();
                return new SetupResult(ssid, password, mode);
            } catch (Exception e) {
                throw new RuntimeException("Provisioning failed: " + e.getMessage(), e);
            }
        });
    }

    static byte[] buildSetupPacket(String ssid, String password, int securityMode) {
        byte[] sb = ssid.getBytes(StandardCharsets.US_ASCII);
        byte[] pb = password.getBytes(StandardCharsets.US_ASCII);
        byte[] packet = new byte[0x88];
        packet[0x26] = 0x14;
        packet[0x84] = (byte) sb.length;
        packet[0x85] = (byte) pb.length;
        packet[0x86] = (byte) securityMode;
        System.arraycopy(sb, 0, packet, 0x44, sb.length);
        System.arraycopy(pb, 0, packet, 0x64, pb.length);
        int cs = Checksum.compute(packet);
        packet[0x20] = (byte) (cs & 0xFF);
        packet[0x21] = (byte) ((cs >> 8) & 0xFF);
        return packet;
    }

    public static class SetupResult {
        public final String ssid, password;
        public final SecurityMode mode;
        public SetupResult(String s, String p, SecurityMode m) { this.ssid=s; this.password=p; this.mode=m; }
    }
}
