package com.broadlink.iot.spi;

import com.broadlink.iot.model.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ManualJsonSerializer implements JsonSerializer {

    @Override @SuppressWarnings("unchecked")
    public <T> T fromJson(byte[] json, Class<T> type) {
        String s = new String(json, StandardCharsets.UTF_8);
        if (type == Sp4State.class) return (T) parseSp4(s);
        if (type == Bg1State.class) return (T) parseBg1(s);
        if (type == S3State.class) return (T) parseS3(s);
        if (type == LightBulbState.class) return (T) parseLb(s);
        if (type == S3HubPage.class) return (T) parsePage(s);
        return (T) parseMap(s);
    }

    @Override
    public byte[] toJson(Object obj) {
        String s;
        if (obj instanceof Sp4State) s = fmtSp4((Sp4State)obj);
        else if (obj instanceof Bg1State) s = fmtBg1((Bg1State)obj);
        else if (obj instanceof S3State) s = fmtS3((S3State)obj);
        else if (obj instanceof LightBulbState) s = fmtLb((LightBulbState)obj);
        else if (obj instanceof Map) s = fmtMap((Map<?,?>)obj);
        else s = "{}";
        return s.getBytes(StandardCharsets.UTF_8);
    }

    private Sp4State parseSp4(String s) {
        Sp4State st = new Sp4State();
        st.setPwr(intVal(s,"pwr")); st.setNtlight(intVal(s,"ntlight")); st.setIndicator(intVal(s,"indicator"));
        Integer nb = intVal(s,"ntlbrightness"); if (nb!=null) st.setNtlbrightness(nb);
        Integer mw = intVal(s,"maxworktime"); if (mw!=null) st.setMaxworktime(mw);
        st.setChildlock(intVal(s,"childlock")); return st;
    }
    private Bg1State parseBg1(String s) {
        Bg1State st = new Bg1State();
        st.setPwr(intVal(s,"pwr")); st.setPwr1(intVal(s,"pwr1")); st.setPwr2(intVal(s,"pwr2"));
        Integer mw = intVal(s,"maxworktime"); if (mw!=null) st.setMaxworktime(mw);
        Integer mw1 = intVal(s,"maxworktime1"); if (mw1!=null) st.setMaxworktime1(mw1);
        Integer mw2 = intVal(s,"maxworktime2"); if (mw2!=null) st.setMaxworktime2(mw2);
        Integer ib = intVal(s,"idcbrightness"); if (ib!=null) st.setIdcbrightness(ib); return st;
    }
    private S3State parseS3(String s) {
        S3State st = new S3State();
        st.setDid(strVal(s,"did")); st.setPwr1(intVal(s,"pwr1")); st.setPwr2(intVal(s,"pwr2")); st.setPwr3(intVal(s,"pwr3")); return st;
    }
    private LightBulbState parseLb(String s) {
        LightBulbState st = new LightBulbState();
        st.setPwr(intVal(s,"pwr")); st.setRed(intVal(s,"red")); st.setBlue(intVal(s,"blue")); st.setGreen(intVal(s,"green"));
        st.setBrightness(intVal(s,"brightness")); st.setColortemp(intVal(s,"colortemp")); st.setHue(intVal(s,"hue"));
        st.setSaturation(intVal(s,"saturation")); st.setTransitionduration(intVal(s,"transitionduration"));
        st.setMaxworktime(intVal(s,"maxworktime")); st.setBulbColormode(intVal(s,"bulb_colormode"));
        st.setBulbScenes(strVal(s,"bulb_scenes")); st.setBulbScene(strVal(s,"bulb_scene")); return st;
    }
    private S3HubPage parsePage(String s) {
        S3HubPage p = new S3HubPage();
        String t = strVal(s,"total"); p.total = t!=null ? Integer.parseInt(t) : 0;
        String list = arrVal(s,"list"); p.list = new ArrayList<>();
        if (list != null && !list.isEmpty()) for (String item : list.split(",")) {
            String trimmed = item.trim().replace("\"","");
            if (!trimmed.isEmpty()) p.list.add(trimmed);
        } return p;
    }
    @SuppressWarnings("unchecked")
    private Map<String,Object> parseMap(String s) {
        Map<String,Object> m = new LinkedHashMap<>();
        String inner = s.replace("{","").replace("}","").trim();
        if (inner.isEmpty()) return m;
        for (String pair : inner.split(",")) {
            String[] kv = pair.split(":",2);
            if (kv.length==2) {
                String key = kv[0].trim().replace("\"","");
                String val = kv[1].trim().replace("\"","");
                try { m.put(key, Integer.parseInt(val)); } catch (NumberFormatException e) { m.put(key,val); }
            }
        } return m;
    }

    private String fmtSp4(Sp4State s) {
        StringBuilder sb = new StringBuilder("{");
        sep(sb).append("\"pwr\":").append(s.getPwr());
        if (s.getNtlight()!=null) sep(sb).append("\"ntlight\":").append(s.getNtlight());
        if (s.getIndicator()!=null) sep(sb).append("\"indicator\":").append(s.getIndicator());
        if (s.getNtlbrightness()!=null) sep(sb).append("\"ntlbrightness\":").append(s.getNtlbrightness());
        if (s.getMaxworktime()!=null) sep(sb).append("\"maxworktime\":").append(s.getMaxworktime());
        if (s.getChildlock()!=null) sep(sb).append("\"childlock\":").append(s.getChildlock());
        return sb.append("}").toString();
    }
    private String fmtBg1(Bg1State s) {
        StringBuilder sb = new StringBuilder("{");
        sep(sb).append("\"pwr\":").append(s.getPwr());
        if (s.getPwr1()!=null) sep(sb).append("\"pwr1\":").append(s.getPwr1());
        if (s.getPwr2()!=null) sep(sb).append("\"pwr2\":").append(s.getPwr2());
        if (s.getMaxworktime()!=null) sep(sb).append("\"maxworktime\":").append(s.getMaxworktime());
        if (s.getMaxworktime1()!=null) sep(sb).append("\"maxworktime1\":").append(s.getMaxworktime1());
        if (s.getMaxworktime2()!=null) sep(sb).append("\"maxworktime2\":").append(s.getMaxworktime2());
        if (s.getIdcbrightness()!=null) sep(sb).append("\"idcbrightness\":").append(s.getIdcbrightness());
        return sb.append("}").toString();
    }
    private String fmtS3(S3State s) {
        StringBuilder sb = new StringBuilder("{");
        if (s.getDid()!=null) { sep(sb).append("\"did\":\"").append(s.getDid()).append('"'); }
        if (s.getPwr1()!=null) sep(sb).append("\"pwr1\":").append(s.getPwr1());
        if (s.getPwr2()!=null) sep(sb).append("\"pwr2\":").append(s.getPwr2());
        if (s.getPwr3()!=null) sep(sb).append("\"pwr3\":").append(s.getPwr3());
        return sb.append("}").toString();
    }
    private String fmtLb(LightBulbState s) {
        StringBuilder sb = new StringBuilder("{");
        if (s.getPwr()!=null) sep(sb).append("\"pwr\":").append(s.getPwr());
        if (s.getRed()!=null) sep(sb).append("\"red\":").append(s.getRed());
        if (s.getBlue()!=null) sep(sb).append("\"blue\":").append(s.getBlue());
        if (s.getGreen()!=null) sep(sb).append("\"green\":").append(s.getGreen());
        if (s.getBrightness()!=null) sep(sb).append("\"brightness\":").append(s.getBrightness());
        if (s.getColortemp()!=null) sep(sb).append("\"colortemp\":").append(s.getColortemp());
        if (s.getHue()!=null) sep(sb).append("\"hue\":").append(s.getHue());
        if (s.getSaturation()!=null) sep(sb).append("\"saturation\":").append(s.getSaturation());
        if (s.getTransitionduration()!=null) sep(sb).append("\"transitionduration\":").append(s.getTransitionduration());
        if (s.getMaxworktime()!=null) sep(sb).append("\"maxworktime\":").append(s.getMaxworktime());
        if (s.getBulbColormode()!=null) sep(sb).append("\"bulb_colormode\":").append(s.getBulbColormode());
        if (s.getBulbScenes()!=null) sep(sb).append("\"bulb_scenes\":\"").append(s.getBulbScenes()).append('"');
        if (s.getBulbScene()!=null) sep(sb).append("\"bulb_scene\":\"").append(s.getBulbScene()).append('"');
        return sb.append("}").toString();
    }
    private String fmtMap(Map<?,?> m) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<?,?> e : m.entrySet()) { if (!first) sb.append(","); first = false;
            sb.append('"').append(e.getKey()).append("\":");
            Object v = e.getValue(); if (v instanceof String) sb.append('"').append(v).append('"'); else sb.append(v);
        } return sb.append("}").toString();
    }

    private Integer intVal(String s, String key) { String v = rawVal(s,key); if (v==null) return null; try { return Integer.parseInt(v); } catch (NumberFormatException e) { return null; } }
    private String strVal(String s, String key) { return rawVal(s,key); }
    private String rawVal(String s, String key) {
        String search = "\""+key+"\":";
        int i = s.indexOf(search); if (i<0) return null; i+=search.length();
        while (i<s.length()&&(s.charAt(i)==' '||s.charAt(i)=='\t')) i++;
        if (i>=s.length()) return null;
        if (s.charAt(i)=='"') { int j=s.indexOf('"',i+1); return j>i ? s.substring(i+1,j) : null; }
        int j=i; while (j<s.length()&&s.charAt(j)!=','&&s.charAt(j)!='}'&&s.charAt(j)!=' ') j++;
        return j>i ? s.substring(i,j) : null;
    }
    private String arrVal(String s, String key) {
        String search = "\""+key+"\":[";
        int i=s.indexOf(search); if (i<0) return null; i+=search.length(); int j=s.indexOf(']',i); return j>i ? s.substring(i,j) : null;
    }
    private StringBuilder sep(StringBuilder sb) { return sb.length()>1 ? sb.append(",") : sb; }

    public static class S3HubPage { public List<String> list; public int total; }
}
