package br.com.gpro.petfriendly.service;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.util.List;
import java.util.Locale;

public class BuscaEnderecoCompleto {

    private Context context;
    private String strAdd;
    private double latitude;
    private double longetude;

    public BuscaEnderecoCompleto(Context context, double latitude, double longetude) {
        this.latitude = latitude;
        this.longetude = longetude;
        this.context = context;
    }

    public String getEnderecoCompleto() {

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(this.latitude, this.longetude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
                 Log.w("Localização Atual: ", "Nenhum endereço retornado!");
            }
        } catch (Exception e) {
            e.printStackTrace();
             Log.w("Localização Atual: ", "Canont get Address!");
        }
        return strAdd;
    }
}
