package com.broadlink.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LightBulbState {
    @JsonProperty("pwr") private Integer pwr;
    @JsonProperty("red") private Integer red;
    @JsonProperty("blue") private Integer blue;
    @JsonProperty("green") private Integer green;
    @JsonProperty("brightness") private Integer brightness;
    @JsonProperty("colortemp") private Integer colortemp;
    @JsonProperty("hue") private Integer hue;
    @JsonProperty("saturation") private Integer saturation;
    @JsonProperty("transitionduration") private Integer transitionduration;
    @JsonProperty("maxworktime") private Integer maxworktime;
    @JsonProperty("bulb_colormode") private Integer bulbColormode;
    @JsonProperty("bulb_scenes") private String bulbScenes;
    @JsonProperty("bulb_scene") private String bulbScene;

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
