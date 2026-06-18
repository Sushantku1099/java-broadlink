package com.broadlink.iot.model;

/** State for LB1/LB2 smart bulbs. */
public class LightBulbState {
    private Integer pwr, red, blue, green, brightness, colortemp, hue, saturation;
    private Integer transitionduration, maxworktime, bulbColormode;
    private String bulbScenes, bulbScene;

    public boolean isPwr() { return pwr != null && pwr == 1; }

    public Integer getPwr() { return pwr; }
    public void setPwr(Integer v) { this.pwr = v; }
    public Integer getRed() { return red; }
    public void setRed(Integer v) { this.red = v; }
    public Integer getBlue() { return blue; }
    public void setBlue(Integer v) { this.blue = v; }
    public Integer getGreen() { return green; }
    public void setGreen(Integer v) { this.green = v; }
    public Integer getBrightness() { return brightness; }
    public void setBrightness(Integer v) { this.brightness = v; }
    public Integer getColortemp() { return colortemp; }
    public void setColortemp(Integer v) { this.colortemp = v; }
    public Integer getHue() { return hue; }
    public void setHue(Integer v) { this.hue = v; }
    public Integer getSaturation() { return saturation; }
    public void setSaturation(Integer v) { this.saturation = v; }
    public Integer getTransitionduration() { return transitionduration; }
    public void setTransitionduration(Integer v) { this.transitionduration = v; }
    public Integer getMaxworktime() { return maxworktime; }
    public void setMaxworktime(Integer v) { this.maxworktime = v; }
    public Integer getBulbColormode() { return bulbColormode; }
    public void setBulbColormode(Integer v) { this.bulbColormode = v; }
    public String getBulbScenes() { return bulbScenes; }
    public void setBulbScenes(String v) { this.bulbScenes = v; }
    public String getBulbScene() { return bulbScene; }
    public void setBulbScene(String v) { this.bulbScene = v; }
}
