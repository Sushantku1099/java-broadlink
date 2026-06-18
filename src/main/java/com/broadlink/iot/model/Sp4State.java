package com.broadlink.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sp4State {
    @JsonProperty("pwr") private Integer pwr;
    @JsonProperty("ntlight") private Integer ntlight;
    @JsonProperty("indicator") private Integer indicator;
    @JsonProperty("ntlbrightness") private Integer ntlbrightness;
    @JsonProperty("maxworktime") private Integer maxworktime;
    @JsonProperty("childlock") private Integer childlock;

    public Sp4State() {}
    // Convenience accessors returning primitives
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
    // Jackson accessors
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
