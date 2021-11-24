package br.com.gpro.petfriendly.model;

public class Alimentador {

    private long id;
    private int bebedouro;
    private int comedouro;
    private double latitude;
    private double longetude;

    public Alimentador() {

    }

    public Alimentador(long id, int bebedouro, int comedouro, double latitude, double longetude) {
        this.id = id;
        this.bebedouro = bebedouro;
        this.comedouro = comedouro;
        this.latitude = latitude;
        this.longetude = longetude;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getBebedouro() {
        return bebedouro;
    }

    public void setBebedouro(int bebedouro) {
        this.bebedouro = bebedouro;
    }

    public int getComedouro() {
        return comedouro;
    }

    public void setComedouro(int comedouro) {
        this.comedouro = comedouro;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongetude() {
        return longetude;
    }

    public void setLongetude(double longetude) {
        this.longetude = longetude;
    }

}


