package com.broadlink.iot.android;

import com.broadlink.iot.spi.BroadlinkLogger;

public class AndroidLoggerProvider implements BroadlinkLogger.BroadlinkLoggerProvider {
    @Override public BroadlinkLogger getLogger(String name) {
        return new AndroidLogger(name);
    }
}
