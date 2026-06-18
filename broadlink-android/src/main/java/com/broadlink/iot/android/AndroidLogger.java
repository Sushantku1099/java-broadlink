package com.broadlink.iot.android;

import com.broadlink.iot.spi.BroadlinkLogger;
import android.util.Log;

/**
 * android.util.Log adapter. Zero extra dependencies.
 */
public class AndroidLogger implements BroadlinkLogger {

    private final String tag;

    public AndroidLogger(String name) { this.tag = name; }

    @Override public void debug(String msg) { Log.d(tag, msg); }
    @Override public void info(String msg)  { Log.i(tag, msg); }
    @Override public void warn(String msg)  { Log.w(tag, msg); }
    @Override public void error(String msg, Throwable t) { Log.e(tag, msg, t); }
}
