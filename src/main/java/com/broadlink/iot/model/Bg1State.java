package com.broadlink.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bg1State {
    @JsonProperty("pwr") private Integer pwr;
    @JsonProperty("pwr1") private Integer pwr1;
    @JsonProperty("pwr2") private Integer pwr2;
    @JsonProperty("maxworktime") private Integer maxworktime;
    @JsonProperty("maxworktime1") private Integer maxworktime1;
    @JsonProperty("maxworktime2") private Integer maxworktime2;
    @JsonProperty("idcbrightness") private Integer idcbrightness;

    public boolean isPwr() { return pwr != null && pwr == 1; }
    public boolean isPwr1() { return pwr1 != null && pwr1 == 1; }
    public boolean isPwr2() { return pwr2 != null && pwr2 == 1; }
    public Integer getPwr() { return pwr; }
    public void setPwr(Integer v) { this.pwr = v; }
    public Integer getPwr1() { return pwr1; }
    public void setPwr1(Integer v) { this.pwr1 = v; }
    public Integer getPwr2() { return pwr2; }
    public void setPwr2(Integer v) { this.pwr2 = v; }
    public Integer getMaxworktime() { return maxworktime; }
    public void setMaxworktime(Integer v) { this.maxworktime = v; }
    public Integer getMaxworktime1() { return maxworktime1; }
    public void setMaxworktime1(Integer v) { this.maxworktime1 = v; }
    public Integer getMaxworktime2() { return maxworktime2; }
    public void setMaxworktime2(Integer v) { this.maxworktime2 = v; }
    public Integer getIdcbrightness() { return idcbrightness; }
    public void setIdcbrightness(Integer v) { this.idcbrightness = v; }
}
