package com.example.belajarnamahewan.Model;

public class Hewan {

    private String id, nama, bing, desk, image, suara, jenis;

    public Hewan() {
    }

    public Hewan(String id, String nama, String bing, String desk, String image, String suara, String jenis) {
        this.id = id;
        this.nama = nama;
        this.bing = bing;
        this.desk = desk;
        this.image = image;
        this.suara = suara;
        this.jenis = jenis;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getBing() {
        return bing;
    }

    public void setBing(String bing) {
        this.bing = bing;
    }

    public String getDesk() {
        return desk;
    }

    public void setDesk(String desk) {
        this.desk = desk;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSuara() {
        return suara;
    }

    public void setSuara(String suara) {
        this.suara = suara;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }
}
