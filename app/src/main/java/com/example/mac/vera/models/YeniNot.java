package com.example.mac.vera.models;

public class YeniNot {

    private String key;
    private String baslik;
    private String icerik;
    private String onem_renk;
    private String konum;
    private String tarih;
    private String saat;
    private String bildirim;

    public YeniNot(String baslik, String icerik, String onem_renk, String konum, String tarih, String saat, String bildirim) {
        this.baslik = baslik;
        this.icerik = icerik;
        this.onem_renk = onem_renk;
        this.konum = konum;
        this.tarih = tarih;
        this.saat = saat;
        this.bildirim = bildirim;
    }

    public YeniNot(String key, String baslik, String icerik, String onem_renk, String konum, String tarih, String saat, String bildirim) {
        this.key = key;
        this.baslik = baslik;
        this.icerik = icerik;
        this.onem_renk = onem_renk;
        this.konum = konum;
        this.tarih = tarih;
        this.saat = saat;
        this.bildirim = bildirim;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public YeniNot() {
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

    public String getKonum() {
        return konum;
    }

    public void setKonum(String konum) {
        this.konum = konum;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
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
