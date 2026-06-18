package com.broadlink.iot.model;

/** State for BG1 gang switch. */
public class Bg1State {
    private Integer pwr, pwr1, pwr2, maxworktime, maxworktime1, maxworktime2, idcbrightness;

    public boolean isPwr() { return pwr != null && pwr == 1; }
    public boolean isPwr1() { return pwr1 != null && pwr1 == 1; }
    public boolean isPwr2() { return pwr2 != null && pwr2 == 1; }
    public int maxworktimeVal() { return maxworktime != null ? maxworktime : 0; }
    public int maxworktime1Val() { return maxworktime1 != null ? maxworktime1 : 0; }
    public int maxworktime2Val() { return maxworktime2 != null ? maxworktime2 : 0; }
    public int idcbrightnessVal() { return idcbrightness != null ? idcbrightness : 0; }

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
