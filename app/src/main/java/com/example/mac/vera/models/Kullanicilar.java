package com.example.mac.vera.models;

public class Kullanicilar {
   private String Uid;
   private String kullanici_adi;
   private String e_posta;


    public Kullanicilar(String Uid, String kullanici_adi, String e_posta) {
        this.Uid = Uid;
        this.kullanici_adi = kullanici_adi;
        this.e_posta = e_posta;

    }

    public Kullanicilar() {
    }

    public String getKey() {
        return Uid;
    }

    public void setKey(String key) {
        this.Uid = key;
    }

    public String getKullanici_adi() {
        return kullanici_adi;
    }

    public void setKullanici_adi(String kullanici_adi) {
        this.kullanici_adi = kullanici_adi;
    }

    public String getE_posta() {
        return e_posta;
    }

    public void setE_posta(String e_posta) {
        this.e_posta = e_posta;
    }


}
