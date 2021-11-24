package br.com.gpro.petfriendly.model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocalizacaoGeocoder {

    private String cidade;
    private String cep;
    private String rua;
    private String bairro;

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public LocalizacaoGeocoder getInfo(Context context, Alimentador alimentador) {

        LocalizacaoGeocoder localizacaoGeocoder = new LocalizacaoGeocoder();


        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(alimentador.getLatitude(), alimentador.getLongetude(), 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);

                localizacaoGeocoder.setCidade(address.getSubAdminArea());
                localizacaoGeocoder.setCep(address.getPostalCode());
                localizacaoGeocoder.setRua(address.getThoroughfare());
                localizacaoGeocoder.setBairro(address.getSubLocality());

            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return localizacaoGeocoder;
    }
}
