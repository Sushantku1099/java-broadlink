package com.broadlink.iot.model;

/** State model for SP4/SP4B smart plugs. Booleans serialized as 0/1 integers in JSON. */
public class Sp4State {
    private Integer pwr;
    private Integer ntlight;
    private Integer indicator;
    private Integer ntlbrightness;
    private Integer maxworktime;
    private Integer childlock;

    public Sp4State() {}
    public boolean isPwr() { return pwr != null && pwr == 1; }
    public boolean isNtlight() { return ntlight != null && ntlight == 1; }
    public boolean isIndicator() { return indicator != null && indicator == 1; }
    public boolean isChildlock() { return childlock != null && childlock == 1; }
    public int ntlbrightnessVal() { return ntlbrightness != null ? ntlbrightness : 0; }
    public int maxworktimeVal() { return maxworktime != null ? maxworktime : 0; }
    public void setPwr(boolean v) { this.pwr = v ? 1 : 0; }
    public void setNtlight(boolean v) { this.ntlight = v ? 1 : 0; }
    public void setIndicator(boolean v) { this.indicator = v ? 1 : 0; }
    public void setChildlock(boolean v) { this.childlock = v ? 1 : 0; }
    public void setNtlbrightness(int v) { this.ntlbrightness = v; }
    public void setMaxworktime(int v) { this.maxworktime = v; }
    public Integer getPwr() { return pwr; }
    public void setPwr(Integer v) { this.pwr = v; }
    public Integer getNtlight() { return ntlight; }
    public void setNtlight(Integer v) { this.ntlight = v; }
    public Integer getIndicator() { return indicator; }
    public void setIndicator(Integer v) { this.indicator = v; }
    public Integer getNtlbrightness() { return ntlbrightness; }
    public void setNtlbrightness(Integer v) { this.ntlbrightness = v; }
    public Integer getMaxworktime() { return maxworktime; }
    public void setMaxworktime(Integer v) { this.maxworktime = v; }
    public Integer getChildlock() { return childlock; }
    public void setChildlock(Integer v) { this.childlock = v; }
}
