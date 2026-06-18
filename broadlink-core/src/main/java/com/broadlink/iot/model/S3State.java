package com.broadlink.iot.model;

/** State for S3 hub sub-device. */
public class S3State {
    private String did;
    private Integer pwr1, pwr2, pwr3;

    public boolean isPwr1() { return pwr1 != null && pwr1 == 1; }
    public boolean isPwr2() { return pwr2 != null && pwr2 == 1; }
    public boolean isPwr3() { return pwr3 != null && pwr3 == 1; }

    public String getDid() { return did; }
    public void setDid(String v) { this.did = v; }
    public Integer getPwr1() { return pwr1; }
    public void setPwr1(Integer v) { this.pwr1 = v; }
    public Integer getPwr2() { return pwr2; }
    public void setPwr2(Integer v) { this.pwr2 = v; }
    public Integer getPwr3() { return pwr3; }
    public void setPwr3(Integer v) { this.pwr3 = v; }
}
