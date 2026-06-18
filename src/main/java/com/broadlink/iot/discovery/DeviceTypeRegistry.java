package com.broadlink.iot.discovery;

import com.broadlink.iot.core.BroadlinkDevice;
import com.broadlink.iot.core.DeviceMetadata;
import com.broadlink.iot.device.alarm.*;
import com.broadlink.iot.device.climate.*;
import com.broadlink.iot.device.cover.*;
import com.broadlink.iot.device.hub.*;
import com.broadlink.iot.device.light.*;
import com.broadlink.iot.device.remote.*;
import com.broadlink.iot.device.sensor.*;
import com.broadlink.iot.device.power.*;
import java.util.*;

/** Maps 123 device type IDs to Java device classes. */
public class DeviceTypeRegistry {
    private final Map<Integer, DeviceEntry> byTypeId = new HashMap<>();

    public DeviceTypeRegistry() {
        register(0x0000, Sp1Device.class, "SP1", "Broadlink");
        register(0x2717, Sp2Device.class, "NEO", "Ankuoo");
        register(0x2719, Sp2Device.class, "SP2-compatible", "Honeywell");
        register(0x271A, Sp2Device.class, "SP2-compatible", "Honeywell");
        register(0x2720, Sp2Device.class, "SP mini", "Broadlink");
        register(0x2728, Sp2Device.class, "SP2-compatible", "URANT");
        register(0x273E, Sp2Device.class, "SP mini", "Broadlink");
        register(0x7530, Sp2Device.class, "SP2", "Broadlink (OEM)");
        register(0x7539, Sp2Device.class, "SP2-IL", "Broadlink (OEM)");
        register(0x753E, Sp2Device.class, "SP mini 3", "Broadlink");
        register(0x7540, Sp2Device.class, "MP2", "Broadlink");
        register(0x7544, Sp2Device.class, "SP2-CL", "Broadlink");
        register(0x7546, Sp2Device.class, "SP2-UK/BR/IN", "Broadlink (OEM)");
        register(0x7547, Sp2Device.class, "SC1", "Broadlink");
        register(0x7918, Sp2Device.class, "SP2", "Broadlink (OEM)");
        register(0x7919, Sp2Device.class, "SP2-compatible", "Honeywell");
        register(0x791A, Sp2Device.class, "SP2-compatible", "Honeywell");
        register(0x7D0D, Sp2Device.class, "SP mini 3", "Broadlink (OEM)");
        register(0x2711, Sp2sDevice.class, "SP2", "Broadlink");
        register(0x2716, Sp2sDevice.class, "NEO PRO", "Ankuoo");
        register(0x271D, Sp2sDevice.class, "Ego", "Efergy");
        register(0x2736, Sp2sDevice.class, "SP mini+", "Broadlink");
        register(0x2733, Sp3Device.class, "SP3", "Broadlink");
        register(0x7D00, Sp3Device.class, "SP3-EU", "Broadlink (OEM)");
        register(0x9479, Sp3sDevice.class, "SP3S-US", "Broadlink");
        register(0x947A, Sp3sDevice.class, "SP3S-EU", "Broadlink");
        register(0x7568, Sp4Device.class, "SP4L-CN", "Broadlink");
        register(0x756C, Sp4Device.class, "SP4M", "Broadlink");
        register(0x756F, Sp4Device.class, "MCB1", "Broadlink");
        register(0x7579, Sp4Device.class, "SP4L-EU", "Broadlink");
        register(0x757B, Sp4Device.class, "SP4L-AU", "Broadlink");
        register(0x7583, Sp4Device.class, "SP mini 3", "Broadlink");
        register(0x7587, Sp4Device.class, "SP4L-UK", "Broadlink");
        register(0x7D11, Sp4Device.class, "SP mini 3", "Broadlink");
        register(0xA569, Sp4Device.class, "SP4L-UK", "Broadlink");
        register(0xA56A, Sp4Device.class, "MCB1", "Broadlink");
        register(0xA56B, Sp4Device.class, "SCB1E", "Broadlink");
        register(0xA56C, Sp4Device.class, "SP4L-EU", "Broadlink");
        register(0xA589, Sp4Device.class, "SP4L-UK", "Broadlink");
        register(0xA5D3, Sp4Device.class, "SP4L-EU", "Broadlink");
        register(0x5115, Sp4bDevice.class, "SCB1E", "Broadlink");
        register(0x51E2, Sp4bDevice.class, "AHC/U-01", "BG Electrical");
        register(0x6111, Sp4bDevice.class, "MCB1", "Broadlink");
        register(0x6113, Sp4bDevice.class, "SCB1E", "Broadlink");
        register(0x618B, Sp4bDevice.class, "SP4L-EU", "Broadlink");
        register(0x6489, Sp4bDevice.class, "SP4L-AU", "Broadlink");
        register(0x648B, Sp4bDevice.class, "SP4M-US", "Broadlink");
        register(0x648C, Sp4bDevice.class, "SP4L-US", "Broadlink");
        register(0x6494, Sp4bDevice.class, "SCB2", "Broadlink");
        register(0x51E3, Bg1Device.class, "BG800/BG900", "BG Electrical");
        register(0x4EB5, Mp1Device.class, "MP1-1K4S", "Broadlink");
        register(0x4EF7, Mp1Device.class, "MP1-1K4S", "Broadlink (OEM)");
        register(0x4F1B, Mp1Device.class, "MP1-1K3S2U", "Broadlink (OEM)");
        register(0x4F65, Mp1Device.class, "MP1-1K3S2U", "Broadlink");
        register(0x2737, RmMiniDevice.class, "RM mini 3", "Broadlink");
        register(0x278F, RmMiniDevice.class, "RM mini", "Broadlink");
        register(0x27C2, RmMiniDevice.class, "RM mini 3", "Broadlink");
        register(0x27C7, RmMiniDevice.class, "RM mini 3", "Broadlink");
        register(0x27CC, RmMiniDevice.class, "RM mini 3", "Broadlink");
        register(0x27CD, RmMiniDevice.class, "RM mini 3", "Broadlink");
        register(0x27D0, RmMiniDevice.class, "RM mini 3", "Broadlink");
        register(0x27D1, RmMiniDevice.class, "RM mini 3", "Broadlink");
        register(0x27D3, RmMiniDevice.class, "RM mini 3", "Broadlink");
        register(0x27DC, RmMiniDevice.class, "RM mini 3", "Broadlink");
        register(0x27DE, RmMiniDevice.class, "RM mini 3", "Broadlink");
        register(0x2712, RmProDevice.class, "RM pro/pro+", "Broadlink");
        register(0x272A, RmProDevice.class, "RM pro", "Broadlink");
        register(0x273D, RmProDevice.class, "RM pro", "Broadlink");
        register(0x277C, RmProDevice.class, "RM home", "Broadlink");
        register(0x2783, RmProDevice.class, "RM home", "Broadlink");
        register(0x2787, RmProDevice.class, "RM pro", "Broadlink");
        register(0x278B, RmProDevice.class, "RM plus", "Broadlink");
        register(0x2797, RmProDevice.class, "RM pro+", "Broadlink");
        register(0x279D, RmProDevice.class, "RM pro+", "Broadlink");
        register(0x27A1, RmProDevice.class, "RM plus", "Broadlink");
        register(0x27A6, RmProDevice.class, "RM plus", "Broadlink");
        register(0x27A9, RmProDevice.class, "RM pro+", "Broadlink");
        register(0x27C3, RmProDevice.class, "RM pro+", "Broadlink");
        register(0x5F36, RmMiniBDevice.class, "RM mini 3", "Broadlink");
        register(0x6507, RmMiniBDevice.class, "RM mini 3", "Broadlink");
        register(0x6508, RmMiniBDevice.class, "RM mini 3", "Broadlink");
        register(0x51DA, Rm4MiniDevice.class, "RM4 mini", "Broadlink");
        register(0x5209, Rm4MiniDevice.class, "RM4 TV mate", "Broadlink");
        register(0x520C, Rm4MiniDevice.class, "RM4 mini", "Broadlink");
        register(0x520D, Rm4MiniDevice.class, "RM4C mini", "Broadlink");
        register(0x5211, Rm4MiniDevice.class, "RM4C mate", "Broadlink");
        register(0x5212, Rm4MiniDevice.class, "RM4 TV mate", "Broadlink");
        register(0x5216, Rm4MiniDevice.class, "RM4 mini", "Broadlink");
        register(0x521C, Rm4MiniDevice.class, "RM4 mini", "Broadlink");
        register(0x6070, Rm4MiniDevice.class, "RM4C mini", "Broadlink");
        register(0x610E, Rm4MiniDevice.class, "RM4 mini", "Broadlink");
        register(0x610F, Rm4MiniDevice.class, "RM4C mini", "Broadlink");
        register(0x62BC, Rm4MiniDevice.class, "RM4 mini", "Broadlink");
        register(0x62BE, Rm4MiniDevice.class, "RM4C mini", "Broadlink");
        register(0x6364, Rm4MiniDevice.class, "RM4S", "Broadlink");
        register(0x648D, Rm4MiniDevice.class, "RM4 mini", "Broadlink");
        register(0x6539, Rm4MiniDevice.class, "RM4C mini", "Broadlink");
        register(0x653A, Rm4MiniDevice.class, "RM4 mini", "Broadlink");
        register(0x520B, Rm4ProDevice.class, "RM4 pro", "Broadlink");
        register(0x5213, Rm4ProDevice.class, "RM4 pro", "Broadlink");
        register(0x5218, Rm4ProDevice.class, "RM4C pro", "Broadlink");
        register(0x6026, Rm4ProDevice.class, "RM4 pro", "Broadlink");
        register(0x6184, Rm4ProDevice.class, "RM4C pro", "Broadlink");
        register(0x61A2, Rm4ProDevice.class, "RM4 pro", "Broadlink");
        register(0x649B, Rm4ProDevice.class, "RM4 pro", "Broadlink");
        register(0x653C, Rm4ProDevice.class, "RM4 pro", "Broadlink");
        register(0x2714, A1Device.class, "e-Sensor", "Broadlink");
        register(0x2722, S1CDevice.class, "S2KIT", "Broadlink");
        register(0xA59C, S3HubDevice.class, "S3", "Broadlink");
        register(0xA64D, S3HubDevice.class, "S3", "Broadlink");
        register(0x5043, Lb1Device.class, "SB800TD", "Broadlink (OEM)");
        register(0x504E, Lb1Device.class, "LB1", "Broadlink");
        register(0x606E, Lb1Device.class, "SB500TD", "Broadlink (OEM)");
        register(0x60C7, Lb1Device.class, "LB1", "Broadlink");
        register(0x60C8, Lb1Device.class, "LB1", "Broadlink");
        register(0x6112, Lb1Device.class, "LB1", "Broadlink");
        register(0x644B, Lb1Device.class, "LB1", "Broadlink");
        register(0x644C, Lb1Device.class, "LB27 R1", "Broadlink");
        register(0x644E, Lb1Device.class, "LB26 R1", "Broadlink");
        register(0xA4F4, Lb2Device.class, "LB27 R1", "Broadlink");
        register(0xA5F7, Lb2Device.class, "LB27 R1", "Broadlink");
        register(0x4EAD, HysenDevice.class, "HY02/HY03", "Hysen");
        register(0x4E4D, DooyaDevice.class, "DT360E-45/20", "Dooya");
    }

    private void register(int id, Class<? extends BroadlinkDevice> c, String m, String mfg) {
        byTypeId.put(id, new DeviceEntry(id, c, m, mfg));
    }

    public DeviceEntry get(int id) {
        return byTypeId.getOrDefault(id, new DeviceEntry(id, BroadlinkDevice.class, null, null));
    }

    public int size() { return byTypeId.size(); }

    public static class DeviceEntry {
        public final int typeId;
        public final Class<? extends BroadlinkDevice> deviceClass;
        public final String model, manufacturer;
        public DeviceEntry(int i, Class<? extends BroadlinkDevice> c, String m, String mf) {
            this.typeId=i; this.deviceClass=c; this.model=m; this.manufacturer=mf;
        }
    }
}
