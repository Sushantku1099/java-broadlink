package com.broadlink.iot.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Closeable;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.CompletableFuture;

/**
 * UDP transport layer for Broadlink device communication.
 */
public class BroadlinkTransport implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(BroadlinkTransport.class);
    private final DatagramSocket socket;
    private final InetSocketAddress target;
    private volatile long timeoutMs = 10_000;

    public BroadlinkTransport(InetSocketAddress target) {
        this.target = target;
        try {
            this.socket = new DatagramSocket(null);
            this.socket.setReuseAddress(true);
            this.socket.bind(null);
            log.debug("Bound to {}", socket.getLocalSocketAddress());
        } catch (SocketException e) {
            throw new RuntimeException("Failed to create UDP socket", e);
        }
    }

    public CompletableFuture<byte[]> sendReceive(byte[] packet) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                DatagramPacket sendPacket = new DatagramPacket(
                    packet, packet.length, target.getAddress(), target.getPort());
                socket.send(sendPacket);

                byte[] buf = new byte[4096];
                DatagramPacket recvPacket = new DatagramPacket(buf, buf.length);
                socket.setSoTimeout((int) timeoutMs);
                socket.receive(recvPacket);

                byte[] response = new byte[recvPacket.getLength()];
                System.arraycopy(recvPacket.getData(), 0, response, 0, recvPacket.getLength());
                return response;
            } catch (SocketTimeoutException e) {
                throw new RuntimeException("Timeout waiting for response from " + target, e);
            } catch (IOException e) {
                throw new RuntimeException("UDP I/O error for " + target, e);
            }
        });
    }

    public void sendBroadcast(byte[] packet, InetAddress broadcastAddress, int port) {
        try {
            DatagramSocket bs = new DatagramSocket(null);
            bs.setBroadcast(true);
            bs.bind(new InetSocketAddress(0));
            bs.send(new DatagramPacket(packet, packet.length, broadcastAddress, port));
            bs.close();
        } catch (IOException e) {
            throw new RuntimeException("Broadcast failed", e);
        }
    }

    public static DatagramSocket createListeningSocket(InetAddress bindAddress) {
        try {
            DatagramSocket s = new DatagramSocket(null);
            s.setReuseAddress(true);
            s.setBroadcast(true);
            s.bind(new InetSocketAddress(bindAddress, 0));
            return s;
        } catch (IOException e) {
            throw new RuntimeException("Failed to bind discovery socket on " + bindAddress, e);
        }
    }

    public void setTimeout(long ms) { this.timeoutMs = ms; }

    @Override
    public void close() {
        if (socket != null && !socket.isClosed()) socket.close();
    }
}
