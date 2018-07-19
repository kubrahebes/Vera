package com.example.mac.vera.models;

public class YeniNot {
private String Uid;
    private String key;
    private String baslik;
    private String icerik;
    private String onem_renk;
    private Double xKordinat;
    private Double yKordinat;
    private String tarih;
    private String konum_name;
    private String saat;
    private String bildirim;

    public YeniNot(String uid, String key, String baslik, String icerik, String onem_renk, Double xKordinat, Double yKordinat, String tarih, String konum_name, String saat, String bildirim) {
        Uid = uid;
        this.key = key;
        this.baslik = baslik;
        this.icerik = icerik;
        this.onem_renk = onem_renk;
        this.xKordinat = xKordinat;
        this.yKordinat = yKordinat;
        this.tarih = tarih;
        this.konum_name = konum_name;
        this.saat = saat;
        this.bildirim = bildirim;
    }

    public YeniNot() {
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getIcerik() {
        return icerik;
    }

    public void setIcerik(String icerik) {
        this.icerik = icerik;
    }

    public String getOnem_renk() {
        return onem_renk;
    }

    public void setOnem_renk(String onem_renk) {
        this.onem_renk = onem_renk;
    }

    public Double getxKordinat() {
        return xKordinat;
    }

    public void setxKordinat(Double xKordinat) {
        this.xKordinat = xKordinat;
    }

    public Double getyKordinat() {
        return yKordinat;
    }

    public void setyKordinat(Double yKordinat) {
        this.yKordinat = yKordinat;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getKonum_name() {
        return konum_name;
    }

    public void setKonum_name(String konum_name) {
        this.konum_name = konum_name;
    }

    public String getSaat() {
        return saat;
    }

    public void setSaat(String saat) {
        this.saat = saat;
    }

    public String getBildirim() {
        return bildirim;
    }

    public void setBildirim(String bildirim) {
        this.bildirim = bildirim;
    }
}
