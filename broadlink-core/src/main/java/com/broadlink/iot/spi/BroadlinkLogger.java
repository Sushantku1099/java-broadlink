package com.broadlink.iot.spi;

public interface BroadlinkLogger {
    void debug(String msg);
    void info(String msg);
    void warn(String msg);
    void error(String msg, Throwable t);

    static BroadlinkLogger getLogger(String name) {
        try {
            for (BroadlinkLoggerProvider p : java.util.ServiceLoader.load(BroadlinkLoggerProvider.class)) {
                BroadlinkLogger l = p.getLogger(name); if (l != null) return l;
            }
        } catch (Exception ignored) {}
        return new NoopLogger();
    }

    interface BroadlinkLoggerProvider { BroadlinkLogger getLogger(String name); }

    final class NoopLogger implements BroadlinkLogger {
        public void debug(String m) {}
        public void info(String m) {}
        public void warn(String m) {}
        public void error(String m, Throwable t) {}
    }
}
