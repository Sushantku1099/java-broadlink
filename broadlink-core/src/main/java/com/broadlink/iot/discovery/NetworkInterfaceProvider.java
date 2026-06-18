package com.broadlink.iot.discovery;

import java.net.*;
import java.util.*;

/** Enumerates IPv4 non-internal network interfaces for discovery broadcast. */
public class NetworkInterfaceProvider {
    public List<NetIface> getAvailableInterfaces() {
        List<NetIface> r = new ArrayList<>();
        try {
            for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (ni.isLoopback()) continue;
                for (InterfaceAddress a : ni.getInterfaceAddresses()) {
                    if (a.getAddress() instanceof Inet4Address) {
                        String ip = a.getAddress().getHostAddress();
                        String bc = computeBroadcast(ip, a.getNetworkPrefixLength());
                        r.add(new NetIface(ip, bc != null ? bc : "255.255.255.255"));
                    }
                }
            }
        } catch (SocketException e) { throw new RuntimeException("Failed to enumerate interfaces", e); }
        return r;
    }

    static String computeBroadcast(String ip, short prefix) {
        try {
            byte[] b = InetAddress.getByName(ip).getAddress();
            int mask = prefix == 0 ? 0 : (0xFFFFFFFF << (32 - prefix));
            int v = ((b[0]&0xFF)<<24)|((b[1]&0xFF)<<16)|((b[2]&0xFF)<<8)|(b[3]&0xFF);
            int bc = (v & mask) | (~mask & 0xFFFFFFFF);
            return ((bc>>24)&0xFF)+"."+((bc>>16)&0xFF)+"."+((bc>>8)&0xFF)+"."+(bc&0xFF);
        } catch (Exception e) { return null; }
    }

    public static class NetIface {
        public final String address, broadcastAddress;
        public NetIface(String a, String b) { this.address = a; this.broadcastAddress = b; }
    }
}
